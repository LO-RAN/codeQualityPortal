package com.compuware.caqs.presentation.consult.actions.actionplan.util;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.KiviatChartFactory;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanFactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.comparators.FactorBeanComparator;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActionPlanKiviatUtils {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static ActionPlanKiviatUtils instance = new ActionPlanKiviatUtils();
    private static final String KIVIAT_TITLE_MSG = "caqs.synthese.kiviat";

    private ActionPlanKiviatUtils() {
    }

    public static ActionPlanKiviatUtils getInstance() {
        return instance;
    }

    public File getKiviatImageInFileForReport(String destDir, String fileName,
            ElementBean eltBean, String idBline, ActionPlanPriority maxPriority, String title,
            int width, int height, boolean showTitle,
            MessageResources resources, Locale locale) {
        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setResources(resources);
        imgConfig.setWidth(width);
        imgConfig.setHeight(height);

        if (showTitle) {
            if (title == null) {
                title = resources.getMessage(locale, KIVIAT_TITLE_MSG);
            }
            imgConfig.setTitle(title);
        }
        imgConfig.setLocale(locale);

        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();

        FactorBeanCollection realKiviatDatas = actionPlanSvc.getRealKiviatDatas(eltBean, idBline, null);
        FactorBeanCollection simulatedKiviatDatas = ActionPlanSimulateGoalsMark.getInstance().computeKiviatScoresForReport(eltBean, idBline, maxPriority);

        File kiviatFile = null;
        try {
            kiviatFile = new File(destDir, fileName);
            this.retrieveKiviatImage(realKiviatDatas, simulatedKiviatDatas, imgConfig, kiviatFile, true, null);
        } catch (IOException exc) {
            logger.error("error while generating kiviat image", exc);
        }
        return kiviatFile;
    }

    public FactorBeanCollection convertFromActionPlan(ActionPlanElementBeanCollection<ActionPlanFactorBean> factors) {
        FactorBeanCollection retour = null;
        if (factors != null) {
            retour = new FactorBeanCollection(factors.getIdEa(), factors.getIdBline());
            for (ActionPlanFactorBean factor : factors) {
                FactorBean fb = new FactorBean();
                fb.setId(factor.getId());
                fb.setNote(factor.getCorrectedScore());
                retour.add(fb);
            }
        }
        return retour;
    }

    public File getKiviatTemporaryFile(ElementBean eltBean, String idBline, ActionPlanPriority maxPriority, String title,
            int width, int height, boolean showTitle, boolean showLegend, HttpServletRequest request) {
        MessageResources resources = RequestUtil.getResources(request);

        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setResources(resources);
        imgConfig.setWidth(width);
        imgConfig.setHeight(height);

        Locale locale = RequestUtil.getLocale(request);

        if (showTitle) {
            if (title == null) {
                title = resources.getMessage(locale, KIVIAT_TITLE_MSG);
            }
            imgConfig.setTitle(title);
        }
        imgConfig.setLocale(locale);

        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getCompleteActionPlan(eltBean, idBline, false, request);
        FactorBeanCollection realKiviatDatas = actionPlanSvc.getRealKiviatDatas(eltBean, idBline, request);
        FactorBeanCollection simulatedKiviatDatas = null;
        if (ElementType.EA.equals(eltBean.getTypeElt())) {
            ActionPlanSimulateGoalsMark.getInstance().computeKiviatScores(eltBean, idBline, maxPriority, false, request);
            simulatedKiviatDatas = this.convertFromActionPlan(ap.getFactors());
        } else {
            simulatedKiviatDatas = this.convertFromActionPlan(ActionPlanSimulateGoalsMark.getInstance().computeProjectKivatSimulatedScores(
                    eltBean, idBline, maxPriority, RequestUtil.getConnectedUserId(request), request));
        }

        File kiviatFile = null;
        try {
            kiviatFile = File.createTempFile("caqsactionplantemp", ".png");
            this.retrieveKiviatImage(realKiviatDatas, simulatedKiviatDatas, imgConfig, kiviatFile, showLegend, null);
        } catch (IOException exc) {
            logger.error("error while generating kiviat image", exc);
        }
        return kiviatFile;
    }

    private void retrieveKiviatImage(FactorBeanCollection reals, FactorBeanCollection simulatedValues,
            GraphImageConfig imgConfig, File result, boolean showLegend, ChartConfig cfg) throws IOException {
        if (reals != null && reals.size() > 0) {
            Collections.sort(reals, new FactorBeanComparator(false, "lib", imgConfig.getLocale()));
            StringBuffer xmlout = new StringBuffer();
            XmlDatasetGenerator.writeFactorDataKiviatActionPlan(xmlout, reals, simulatedValues, imgConfig.getLocale(), imgConfig.getResources());
            InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
            if(cfg==null) {
                cfg = ChartConfigGenerator.getSimulationKiviatConfig();
            }
            cfg.setTitle(imgConfig.getTitle());
            JFreeChart kiviatChart = KiviatChartFactory.createFromXml(str, cfg);
            if (imgConfig.getTitle() != null) {
                TextTitle title = new TextTitle(imgConfig.getTitle(), this.getKiviatTitleFont());
                kiviatChart.setTitle(title);
            }
            if (!showLegend) {
                kiviatChart.removeLegend();
            }
            ChartUtilities.saveChartAsPNG(result, kiviatChart, imgConfig.getWidth(), imgConfig.getHeight());
        }
    }

    private Font getKiviatTitleFont() {
        return new Font("Helvetica", Font.BOLD, 12);
    }

    public File retrieveDomainActionPlanKiviatImage(List<ElementBean> elts, ActionPlanPriority priority, HttpServletRequest request) throws IOException {
        File kiviatFile = null;
        Map<String, Double> notes = new HashMap<String, Double>();
        Map<String, Double> apNotes = new HashMap<String, Double>();
        Map<String, Integer> nbElts = new HashMap<String, Integer>();
        FactorBeanCollection simulatedKiviatDatas = null;
        Synthese synthese = new Synthese();

        for (ElementBean e : elts) {
            List<FactorBean> fList = synthese.retrieveKiviat(e, e.getBaseline().getId());
            simulatedKiviatDatas = ActionPlanKiviatUtils.getInstance().convertFromActionPlan(ActionPlanSimulateGoalsMark.getInstance().computeProjectKivatSimulatedScores(
                    e, e.getBaseline().getId(), priority, RequestUtil.getConnectedUserId(request), request));
            for (FactorBean fb : fList) {
                String idFact = fb.getId();
                Double note = notes.get(idFact);
                FactorBean simulFB = simulatedKiviatDatas.get(idFact);
                Double simul = apNotes.get(idFact);
                Integer nbElt = nbElts.get(idFact);
                if (note == null) {
                    note = new Double(0);
                    nbElt = new Integer(0);
                    simul = new Double(0);
                }
                double n = note.doubleValue() + fb.getNote() * e.getPoids();
                int i = nbElt.intValue() + e.getPoids();
                double s = simul.doubleValue() + simulFB.getNote() *
                        e.getPoids();
                notes.put(idFact, new Double(n));
                nbElts.put(idFact, new Integer(i));
                apNotes.put(idFact, new Double(s));
            }
        }

        FactorBeanCollection factorsForKiviat = new FactorBeanCollection(null, null);
        FactorBeanCollection simulFactorsForKiviat = new FactorBeanCollection(null, null);
        Set<String> idsFactor = notes.keySet();
        for (Iterator<String> it = idsFactor.iterator(); it.hasNext();) {
            String idFact = it.next();

            Double note = notes.get(idFact);
            Integer nbElt = nbElts.get(idFact);
            Double simul = apNotes.get(idFact);

            FactorBean fb = new FactorBean();
            fb.setId(idFact);
            double thisNote = note.doubleValue() / nbElt.doubleValue();
            fb.setNote(thisNote);
            factorsForKiviat.add(fb);
            fb = new FactorBean();
            fb.setId(idFact);
            thisNote = simul.doubleValue() / nbElt.doubleValue();
            fb.setNote(thisNote);
            simulFactorsForKiviat.add(fb);
        }

        int width = 400;
        String widthStr = request.getParameter("width");

        if (widthStr != null && widthStr.matches("[0-9]+")) {
            width = Integer.parseInt(widthStr);
        }


        if (!factorsForKiviat.isEmpty()) {
            GraphImageConfig imgConfig = new GraphImageConfig();
            imgConfig.setWidth(width);
            imgConfig.setHeight(350);

            Locale locale = RequestUtil.getLocale(request);
            MessageResources resources = RequestUtil.getResources(request);

            imgConfig.setLocale(locale);
            imgConfig.setResources(resources);

            kiviatFile = File.createTempFile("domainSynthesis", ".png");
            if (kiviatFile != null && kiviatFile.exists()) {
                try {
                    this.retrieveKiviatImage(factorsForKiviat, simulFactorsForKiviat, imgConfig, kiviatFile, true, ChartConfigGenerator.getDomainSimulationKiviatConfig());
                } catch (IOException e) {
                    logger.error("Error reading file: " + kiviatFile.getName(), e);
                }
            }
        }
        return kiviatFile;
    }
}

package com.compuware.caqs.business.consult;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.KiviatChartFactory;
import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.domain.dataschemas.comparators.FactorBeanComparator;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Synthese {

    private DaoFactory daoFactory = DaoFactory.getInstance();
    private ImageUtil imgUtil = ImageUtil.getInstance();

    public List<FactorBean> retrieveKiviat(ElementBean eltBean) {
        BaselineBean blineBean = eltBean.getBaseline();
        FactorDao factorFacade = daoFactory.getFactorDao();
        List<FactorBean> result = factorFacade.retrieveFactorsByElementBaseline(blineBean.getId(), eltBean.getProject().getId(), eltBean.getId());
        return result;
    }

    public List<FactorBean> retrieveKiviat(ElementBean eltBean, String idBline) {
        List<FactorBean> result = null;
        if (idBline != null) {
            FactorDao factorFacade = daoFactory.getFactorDao();
            result = factorFacade.retrieveFactorsByElementBaseline(idBline, eltBean.getProject().getId(), eltBean.getId());
        } else {
            result = retrieveKiviat(eltBean);
        }
        return result;
    }

    public void retrieveKiviatImage(List<FactorBean> values, final GraphImageConfig imgConfig, File result) throws IOException {
        if (values != null && values.size() > 0) {
            Collections.sort(values, new FactorBeanComparator(false, "lib", imgConfig.getLocale()));
            StringBuffer xmlout = new StringBuffer();
            XmlDatasetGenerator.writeFactorData(xmlout, values, imgConfig.getLocale(), imgConfig.getResources());
            InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
            ChartConfig cfg = ChartConfigGenerator.getKiviatConfig();
            cfg.setTitle(imgConfig.getTitle());
            JFreeChart kiviatChart = KiviatChartFactory.createFromXml(str, cfg);
            ChartUtilities.saveChartAsPNG(result, kiviatChart, imgConfig.getWidth(), imgConfig.getHeight());
        }
    }

    public void retrieveDashboardKiviatImage(List<FactorBean> values, final GraphImageConfig imgConfig, File result) throws IOException {
        if (values != null && values.size() > 0) {
            Collections.sort(values, new FactorBeanComparator(false, "lib", imgConfig.getLocale()));
            StringBuffer xmlout = new StringBuffer();
            XmlDatasetGenerator.writeFactorData(xmlout, values, imgConfig.getLocale(), imgConfig.getResources());
            InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
            ChartConfig cfg = ChartConfigGenerator.getKiviatConfig();
            JFreeChart kiviatChart = KiviatChartFactory.createFromXml(str, cfg);
            kiviatChart.removeLegend();
            kiviatChart.getPlot().setOutlinePaint(new Color(0, 0, 0, 0));
            ChartUtilities.saveChartAsPNG(result, kiviatChart, imgConfig.getWidth(), imgConfig.getHeight(), null, true, 0);
        }
    }

    public File retrieveDomainKiviatImage(List<ElementBean> elts, GraphImageConfig imgConfig) throws IOException {
        File kiviatFile = null;
        Synthese synthese = new Synthese();

        Map<String, Double> notes = new HashMap<String, Double>();
        Map<String, Integer> nbElts = new HashMap<String, Integer>();

        for (ElementBean e : elts) {
            List<FactorBean> fList = synthese.retrieveKiviat(e, e.getBaseline().getId());
            for (FactorBean fb : fList) {
                String idFact = fb.getId();
                Double note = notes.get(idFact);
                Integer nbElt = nbElts.get(idFact);
                if (note == null) {
                    note = new Double(0);
                    nbElt = new Integer(0);
                }
                double n = note.doubleValue() + fb.getNote() * e.getPoids();
                int i = nbElt.intValue() + e.getPoids();
                notes.put(idFact, new Double(n));
                nbElts.put(idFact, new Integer(i));
            }
        }

        List<FactorBean> factorsForKiviat = new ArrayList<FactorBean>();
        Set<String> idsFactor = notes.keySet();
        for (Iterator<String> it = idsFactor.iterator(); it.hasNext();) {
            String idFact = it.next();

            Double note = notes.get(idFact);
            Integer nbElt = nbElts.get(idFact);

            FactorBean fb = new FactorBean();
            fb.setId(idFact);
            double thisNote = note.doubleValue() / nbElt.doubleValue();
            fb.setNote(thisNote);
            factorsForKiviat.add(fb);
        }

        if (!factorsForKiviat.isEmpty()) {
            kiviatFile = File.createTempFile("domainSynthesis", ".png");
            this.retrieveKiviatImage(factorsForKiviat, imgConfig, kiviatFile);
        }
        return kiviatFile;
    }

    public File retrieveKiviatImage(ElementBean eltBean, GraphImageConfig imgConfig) throws IOException {
        File result = imgUtil.retrieveExistingImage(eltBean.getId(), eltBean.getBaseline().getId(), imgConfig);
        if (!result.exists()) {
            List<FactorBean> values = this.retrieveKiviat(eltBean);
            retrieveKiviatImage(values, imgConfig, result);
        }
        return result;
    }

    public File retrieveKiviatImage(ElementBean eltBean, String idBline, GraphImageConfig imgConfig) throws IOException {
        File result = imgUtil.retrieveExistingImage(eltBean.getId(), idBline, imgConfig);
        if (!result.exists()) {
            List<FactorBean> values = this.retrieveKiviat(eltBean, idBline);
            retrieveKiviatImage(values, imgConfig, result);
        }
        return result;
    }

    public File retrieveDashboardKiviatImage(ElementBean eltBean, String idBline, GraphImageConfig imgConfig) throws IOException {
        File result = imgUtil.retrieveExistingFavoriteKiviatImage(eltBean.getId(), idBline, imgConfig);
        if (!result.exists()) {
            List<FactorBean> values = this.retrieveKiviat(eltBean, idBline);
            retrieveDashboardKiviatImage(values, imgConfig, result);
        }
        return result;
    }

    public Map<String, Double> retrieveVolumetryMetrics(ElementBean eltBean, String idBline) {
        MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
        return metriqueFacade.retrieveVolumetryMetrics(eltBean, idBline);
    }

    public List<Volumetry> retrieveVolumetry(ElementBean eltBean) {
        BaselineBean blineBean = eltBean.getBaseline();
        ElementDao elementFacade = daoFactory.getElementDao();
        List<Volumetry> retour = null;
        if (blineBean != null) {
            retour = elementFacade.retrieveVolumetry(eltBean.getId(), blineBean.getId());
        }
        return retour;
    }
}

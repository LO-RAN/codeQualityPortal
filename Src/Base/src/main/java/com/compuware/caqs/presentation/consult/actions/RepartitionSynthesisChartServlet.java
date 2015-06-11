package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.service.FactorSvc;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.awt.Color;

public class RepartitionSynthesisChartServlet extends Action {

    /**
     * Logger.
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        Locale loc = RequestUtil.getLocale(request);
        String idLanguage = request.getParameter("idLanguage");
        Map<String, List<ElementBean>> languageBeansMap = (Map<String, List<ElementBean>>) request.getSession().getAttribute("languagesElts");
        MessageResources resources = RequestUtil.getResources(request);
        String idFac = request.getParameter("idFac");
        String noData = resources.getMessage(loc, "caqs.domainsynthese.repartition.nodata");
        String libLanguage = request.getParameter("libLanguage");

        if (idLanguage != null && languageBeansMap != null) {
            List<ElementBean> liste = languageBeansMap.get(idLanguage);

            FactorSvc factorsvc = FactorSvc.getInstance();

            if (liste != null && !liste.isEmpty()) {
                List<ChartElement> elts = new ArrayList<ChartElement>();

                for (ElementBean elt : liste) {
                    double mark = 0.0;
                    if (Constants.ALL_FACTORS.equals(idFac)) {
                        mark = factorsvc.getAverageFactorMarkForElement(
                                elt.getId(), elt.getBaseline().getId());
                    } else {
                        FactorBean fb = factorsvc.retrieveFactorAndJustByIdElementBaseline(
                                elt.getBaseline().getId(),
                                elt.getProject().getId(),
                                elt.getId(), idFac);
                        if (fb != null) {
                            mark = fb.getNote();
                        }
                    }
                    if (mark > 0.0) {
                        this.addValueToList(elts, mark);
                    }
                }

                Collections.sort(elts);

                float[][] valeurs = {
                    {1.25f, 1.75f},
                    {2.25f, 2.75f},
                    {3.25f, 3.75f},
                    {4.0f}
                };
                Color[] couleurs = {
                    Color.RED,
                    Color.ORANGE,
                    new Color(0, 128, 0),
                    Color.GREEN
                };
                XYSeries[] xyseries = {
                    new XYSeries(""),
                    new XYSeries(""),
                    new XYSeries(""),
                    new XYSeries("")
                };

                for (ChartElement elt : elts) {
                    boolean ok = false;
                    for (int i = 0; i < valeurs.length && !ok; i++) {
                        float[] tab = valeurs[i];
                        for (int j = 0; j < tab.length && !ok; j++) {
                            if (Math.abs(elt.getValue() - tab[j]) <= 0.25f) {
                                XYDataItem item = null;
                                List items = xyseries[i].getItems();
                                if(!items.isEmpty()) {
                                    for(Iterator it = items.iterator(); it.hasNext();) {
                                        XYDataItem thisitem = (XYDataItem) it.next();
                                        if(thisitem.getXValue()==tab[j]) {
                                            item = thisitem;
                                            break;
                                        }
                                    }
                                }
                                if(item == null) {
                                    item = new XYDataItem(tab[j], 0);
                                }
                                if (items != null) {
                                    item.setY(item.getYValue() + elt.getNbElts().doubleValue());
                                }
                                xyseries[i].add(item);
                                ok = true;
                            }
                        }
                    }

                }

                XYSeriesCollection xyseriescollection = new XYSeriesCollection();
                for (int i = 0; i < xyseries.length; i++) {
                    xyseriescollection.addSeries(xyseries[i]);
                }

                String absMsg = "";
                if (Constants.ALL_FACTORS.equals(idFac)) {
                    absMsg = resources.getMessage(loc,
                            "caqs.domainsynthese.repartition.all_factors");
                } else {
                    FactorBean fb = new FactorBean();
                    fb.setId(idFac);
                    absMsg = fb.getLib(loc);
                }

                NumberAxis abscisse = new NumberAxis(absMsg);
                abscisse.setAutoRangeIncludesZero(false);
                abscisse.setTickUnit(new NumberTickUnit(.5));
                abscisse.setLowerBound(1);
                abscisse.setUpperBound(4.5D);

                NumberAxis ordonnee = new NumberAxis(resources.getMessage(loc, 
                        "caqs.domainSynthesis.nbEa"));
                ordonnee.setAutoRangeIncludesZero(false);
                ordonnee.setTickUnit(new NumberTickUnit(1));


                JFreeChart jfreechart = ChartFactory.createXYBarChart(
                        libLanguage,
                        absMsg,
                        true,
                        resources.getMessage(loc, "caqs.domainSynthesis.nbEa"),
                        xyseriescollection,
                        PlotOrientation.VERTICAL,
                        false,
                        false,
                        false);
                XYPlot xyplot = (XYPlot) jfreechart.getPlot();
                xyplot.setDomainAxis(abscisse);
                xyplot.setRangeAxis(ordonnee);
                ChartUtilities.applyCurrentTheme(jfreechart);
                xyplot.setBackgroundPaint(Color.white);
                xyplot.setDomainGridlinePaint(Color.DARK_GRAY);
                xyplot.setRangeGridlinePaint(Color.DARK_GRAY);
                xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
                xyplot.setNoDataMessage(noData);


                XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
                xybarrenderer.setMargin(0.60);
                for (int i = 0; i < xyseries.length; i++) {
                    xybarrenderer.setSeriesPaint(i, couleurs[i]);
                }



                File kiviatFile = File.createTempFile("dialectSynthesis" +
                        idLanguage, ".png");
                if (kiviatFile != null && kiviatFile.exists()) {
                    try {
                        ChartUtilities.saveChartAsPNG(kiviatFile, jfreechart, 300, 300);
                        ServletOutputStream out = response.getOutputStream();
                        byte[] result = ImageUtil.getInstance().getImageBytes(kiviatFile);
                        out.write(result);
                        out.flush();
                        kiviatFile.delete();
                    } catch (IOException e) {
                        logger.error("Error reading file: " +
                                kiviatFile.getName(), e);
                    }
                }
            }
        } else {
            logger.error("Erreur lors de la recuperation de la synthese des ea pour le langage " +
                    idLanguage);
        }

        return null;
    }

    private void addValueToList(List<ChartElement> liste, double value) {
        boolean found = false;
        for (ChartElement elt : liste) {
            if (elt.getValue() == value) {
                found = true;
                elt.addElement();
                break;
            }
        }
        if (!found) {
            ChartElement elt = new ChartElement(value);
            liste.add(elt);
        }
    }

    private class ChartElement implements Comparable<ChartElement> {

        private int nbElts;
        private double value;

        public ChartElement(double val) {
            this.nbElts = 1;
            this.value = val;
        }

        public void addElement() {
            this.nbElts++;
        }

        public int compareTo(ChartElement o) {
            int retour = 0;
            if (o instanceof ChartElement) {
                ChartElement otherChartElement = (ChartElement) o;
                Double val = new Double(value);
                Double otherVal = new Double(otherChartElement.value);
                retour = val.compareTo(otherVal);
            }
            return retour;
        }

        public Integer getNbElts() {
            return new Integer(this.nbElts);
        }

        public double getValue() {
            return this.value;

        }
    }
}

package com.compuware.caqs.presentation.util;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.PieChartFactory;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.service.EvolutionSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

/**
 *
 * @author cwfr-dzysman
 */
public class EvolutionsUtils {

    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public static String generatePieChart(String idElt, String idBline, String idPreviousBline,
            ElementsCategory category, FilterBean filter,
            HttpServletRequest request) {
        String fileName = "";
        Locale locale = RequestUtil.getLocale(request);
        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setWidth(500);
        imgConfig.setHeight(300);

        String widthStr = request.getParameter("width");
        String heightStr = request.getParameter("height");

        if (widthStr != null && widthStr.matches("[0-9]+")) {
            imgConfig.setWidth(Integer.parseInt(widthStr));
        }
        if (heightStr != null && heightStr.matches("[0-9]+")) {
            imgConfig.setHeight(Integer.parseInt(heightStr));
        }

        MessageResources resources = RequestUtil.getResources(request);
        imgConfig.setTitle(resources.getMessage(locale, "caqs.evolution.repartition."
                + category.getStringCode()));

        Collection repartition = null;
        EvolutionSvc evolutionSvc = EvolutionSvc.getInstance();
        if (ElementsCategory.NEW_BAD.equals(category)) {
            repartition = evolutionSvc.retrieveRepartitionNewAndBadElements(idElt, idBline, idPreviousBline, filter);
        } else if (ElementsCategory.OLD_WORST.equals(category)) {
            repartition = evolutionSvc.retrieveRepartitionOldAndWorstElements(idElt, idBline, idPreviousBline, filter);
        } else if (ElementsCategory.OLD_BETTER.equals(category)) {
            repartition = evolutionSvc.retrieveRepartitionOldAndBetterElements(idElt, idBline, idPreviousBline, filter);
        } else if (ElementsCategory.OLD_BETTER_WORST.equals(category)) {
            repartition = evolutionSvc.retrieveRepartitionOldBetterWorstElements(idElt, idBline, idPreviousBline, filter);
        } else if (ElementsCategory.BAD_STABLE.equals(category)) {
            repartition = evolutionSvc.retrieveRepartitionBadStableElements(idElt, idBline, idPreviousBline, filter);
        }
        try {
            StringBuffer xmlout = new StringBuffer();
            XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, RequestUtil.getLocale(request));
            InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
            JFreeChart pieChart = PieChartFactory.createFromXml(str, ChartConfigGenerator.getPieConfig(imgConfig.getTitle(), true));
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            fileName = ServletUtilities.saveChartAsPNG(pieChart, imgConfig.getWidth(), imgConfig.getHeight(), info, request.getSession());
        } catch (IOException exc) {
            mLog.error(exc.getMessage());
        }
        return fileName;
    }

    public static String createImageMap(String fileName) throws IOException {
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ChartUtilities.writeImageMap(pw, fileName, info, false);
        pw.flush();
        return sw.getBuffer().toString();
    }
}

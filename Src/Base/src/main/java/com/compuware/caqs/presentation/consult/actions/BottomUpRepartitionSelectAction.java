package com.compuware.caqs.presentation.consult.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.PieChartFactory;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BottomUpSyntheseSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import net.sf.json.JSONObject;

public class BottomUpRepartitionSelectAction extends ExtJSAjaxAction {

    // --------------------------------------------------------- Public Methods
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Extract attributes we will need

        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();
        JSONObject obj = new JSONObject();
        MessagesCodes ms = MessagesCodes.NO_ERROR;
        try {
            String s = retrieveRepartition(mapping, request, session, errors, response);
            obj.put("datas", s);
        } catch (IOException exc) {
            logger.error(exc);
            ms = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(obj, ms);

        return obj;

    }

    protected String retrieveRepartition(ActionMapping mapping,
            HttpServletRequest request,
            HttpSession session,
            ActionErrors errors,
            HttpServletResponse response) throws IOException {
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String filterDesc = RequestHelper.retrieve(request, session, "filter", JdbcDAOUtils.DATABASE_STRING_NOFILTER);
        String filterTypeElt = RequestHelper.retrieve(request, session, "typeElt", ElementType.ALL);
        if ("ALL".equals(filterTypeElt)) {
            filterTypeElt = "%";
        }

        Locale locale = RequestUtil.getLocale(request);

        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setWidth(400);
        imgConfig.setHeight(220);

        String type = request.getParameter("type");

        String widthStr = request.getParameter("width");
        String heightStr = request.getParameter("height");

        if (widthStr != null && widthStr.matches("[0-9]+")) {
            imgConfig.setWidth(Integer.parseInt(widthStr));
        }
        if (heightStr != null && heightStr.matches("[0-9]+")) {
            imgConfig.setHeight(Integer.parseInt(heightStr));
        }

        MessageResources resources = this.getResources(request);
        String ameliorationTitle = resources.getMessage(locale, "caqs.pieapplet.amelioration");

        Collection repartition = null;
        String targetTitle = "";

        BottomUpSyntheseSvc bottomUpSyntheseSvc = BottomUpSyntheseSvc.getInstance();
        if ("crit".equals(type)) {
            repartition = bottomUpSyntheseSvc.retrieveRepartitionByCriterion(eltBean, filterDesc, filterTypeElt);
            targetTitle = resources.getMessage(locale, "caqs.critere");
        } else {
            repartition = bottomUpSyntheseSvc.retrieveRepartitionByFactor(eltBean, filterDesc, filterTypeElt);
            targetTitle = resources.getMessage(locale, "caqs.objectif");
        }
        imgConfig.setTitle(ameliorationTitle + ' ' + targetTitle);
        return createChart(request, session, repartition, imgConfig);
    }

    private String createChart(
            HttpServletRequest request,
            HttpSession session,
            Collection repartition,
            GraphImageConfig imgConfig) throws IOException {
        StringBuffer xmlout = new StringBuffer();
        XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, RequestUtil.getLocale(request));

        InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
        JFreeChart pieChart = PieChartFactory.createFromXml(str, ChartConfigGenerator.getPieConfig(imgConfig.getTitle(), true));
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String fileName = ServletUtilities.saveChartAsPNG(pieChart, imgConfig.getWidth(), imgConfig.getHeight(), info, session);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ChartUtilities.writeImageMap(pw, fileName, info, false);
        pw.flush();
        String retour = new String(fileName.getBytes());
        retour += new String("!!!!".getBytes());
        retour += new String(sw.toString());//getBuffer().toString().getBytes("UTF-8"));
        return retour;
    }
}

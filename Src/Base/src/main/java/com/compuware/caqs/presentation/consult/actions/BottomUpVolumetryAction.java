package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.PieChartFactory;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.business.util.StringFormatUtil;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.SyntheseCorrectionBean;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BottomUpSyntheseSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.Collection;
import net.sf.json.JSONArray;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

public class BottomUpVolumetryAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = this.doRetrieve(request, request.getSession());
        return obj;
    }

    private JSONObject doRetrieve(HttpServletRequest request,
            HttpSession session) {
        JSONObject retour = new JSONObject();

        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        if (eltBean != null) {
            Locale loc = RequestUtil.getLocale(request);
            this.retrieveSummary(retour, eltBean, loc, request);
            if (ElementType.EA.equals(eltBean.getTypeElt())) {
                try {
                    retrieveRepartition(retour, request, session);
                } catch (IOException exc) {
                    mLog.error("BottomUpVolumetryAction IOException", exc);
                }
            }
        }
        return retour;
    }

    private void retrieveSummary(JSONObject obj, ElementBean eltBean, Locale loc, HttpServletRequest request) {
        String filterDesc = RequestHelper.retrieve(request, request.getSession(), "filter", JdbcDAOUtils.DATABASE_STRING_NOFILTER);
        String filterTypeElt = request.getParameter("teltFilter");
        if (filterTypeElt == null) {
            filterTypeElt = ElementType.ALL;
        }
        FilterBean filter = new FilterBean(filterDesc, filterTypeElt);

        SyntheseCorrectionBean syntheseCorr = new SyntheseCorrectionBean(RequestUtil.getLocale(request));
        if (ElementType.EA.equals(eltBean.getTypeElt())) {
            retrieveCorrectionSyntheseForEa(eltBean, filter, syntheseCorr);
        } else {
            retrieveRecursiveSummary(eltBean, filter, syntheseCorr);
        }

        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);
        obj.put("volumetryNbRejets", nf.format(syntheseCorr.getNbEltsRejets()));
        obj.put("volumetryNbReserve", nf.format(syntheseCorr.getNbEltsReserve()));
        obj.put("volumetryNbAccepte", nf.format(syntheseCorr.getNbEltsAccepte()));
        obj.put("volumetryNbAmeliore", syntheseCorr.getPctEltsCorr());
        obj.put("volumetryNbAmelioration1", nf.format(syntheseCorr.getNbCorrRejets()));
        obj.put("volumetryPctAmelioration1", syntheseCorr.getPctCorrRejets());
        obj.put("volumetryNbAmelioration2", nf.format(syntheseCorr.getNbCorrReserve()));
        obj.put("volumetryPctAmelioration2", syntheseCorr.getPctCorrReserve());
        obj.put("volumetryTotalAmelioration", nf.format(syntheseCorr.getNbCorrTotal()));

        setAdditionnalMetrics(obj, request);
        request.setAttribute("syntheseCorrection", syntheseCorr);
        request.getSession().setAttribute("filter", filterDesc);
        request.getSession().setAttribute("typeElt", filterTypeElt);
    }

    private void setAdditionnalMetrics(JSONObject obj, HttpServletRequest request) {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        Locale loc = RequestUtil.getLocale(request);
        String metList = dynProp.getProperty(Constants.BOTTOMUP_ADDITIONNAL_METRIC_LIST_KEY);
        MetriqueBean[] metBeanArray = null;
        JSONArray array = new JSONArray();
        if (metList != null && metList.length() > 0) {
            String[] metArray = metList.split(",");
            metBeanArray = new MetriqueBean[metArray.length];
            MetriqueBean currentMetricBean = null;
            for (int i = 0; i < metArray.length; i++) {
                currentMetricBean = new MetriqueBean();
                currentMetricBean.setId(metArray[i]);
                metBeanArray[i] = currentMetricBean;
                JSONObject metObj = new JSONObject();
                metObj.put("id", metArray[i]);
                metObj.put("lib", currentMetricBean.getLib(loc));
                array.add(metObj);
            }
        } else {
            metBeanArray = new MetriqueBean[0];
        }
        obj.put("additionnalMetrics", array);
        request.getSession().setAttribute("additionnalMetrics", metBeanArray);
    }

    private void retrieveRecursiveSummary(ElementBean eltBean, FilterBean filter, SyntheseCorrectionBean syntheseCorr) {
        List<ElementBean> allEa = BottomUpSyntheseSvc.getInstance().retrieveRecursiveSubElements(eltBean);
        for (ElementBean bean : allEa) {
            bean.setBaseline(eltBean.getBaseline());
            bean.setProject(eltBean.getProject());
            retrieveCorrectionSyntheseForEa(bean, filter, syntheseCorr);
        }
    }

    private void retrieveCorrectionSyntheseForEa(ElementBean eltBean, FilterBean filter, SyntheseCorrectionBean syntheseCorr) {
        BottomUpSyntheseSvc.getInstance().retrieveCorrectionSynthese(eltBean, filter, syntheseCorr);
    }

    protected void retrieveRepartition(JSONObject obj, HttpServletRequest request,
            HttpSession session) throws IOException {
        Locale loc = RequestUtil.getLocale(request);
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

        Collection repartitionCrit = null;
        Collection repartitionObj = null;

        BottomUpSyntheseSvc bottomUpSyntheseSvc = BottomUpSyntheseSvc.getInstance();
        if (type == null || "crit".equals(type)) {
            repartitionCrit = bottomUpSyntheseSvc.retrieveRepartitionByCriterion(eltBean, filterDesc, filterTypeElt);
            String targetTitle = resources.getMessage(locale, "caqs.critere");
            imgConfig.setTitle(ameliorationTitle + ' ' + targetTitle);
            createChart(obj, loc, session, repartitionCrit, imgConfig, "crit");
        }
        if (type == null || "obj".equals(type)) {
            repartitionObj = bottomUpSyntheseSvc.retrieveRepartitionByFactor(eltBean, filterDesc, filterTypeElt);
            String targetTitle = resources.getMessage(locale, "caqs.objectif");
            imgConfig.setTitle(ameliorationTitle + ' ' + targetTitle);
            createChart(obj, loc, session, repartitionObj, imgConfig, "obj");
        }

        if (type != null) {
            obj.put("type", type);
        }

    }

    private void createChart(
            JSONObject obj,
            Locale loc,
            HttpSession session,
            Collection repartition,
            GraphImageConfig imgConfig,
            String prefix) throws IOException {
        StringBuffer xmlout = new StringBuffer();
        XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, loc);

        InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
        JFreeChart pieChart = PieChartFactory.createFromXml(str, ChartConfigGenerator.getPieConfig(imgConfig.getTitle(), true));
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        String fileName = ServletUtilities.saveChartAsPNG(pieChart, imgConfig.getWidth(), imgConfig.getHeight(), info, session);
        obj.put(prefix + "-piechartFileName", fileName);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ChartUtilities.writeImageMap(pw, fileName, info, false);
        pw.flush();
        obj.put(prefix + "-piechartImageMap", sw.getBuffer().toString());
        pw.close();
    }
}

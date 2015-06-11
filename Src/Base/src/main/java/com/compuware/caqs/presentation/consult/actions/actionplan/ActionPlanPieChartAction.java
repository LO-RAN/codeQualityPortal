package com.compuware.caqs.presentation.consult.actions.actionplan;

import java.awt.Color;
import java.awt.Font;
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.PieChartFactory;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorRepartitionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.FactorRepartitionBeanList;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanSimulateGoalsMark;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import net.sf.json.JSONObject;

public class ActionPlanPieChartAction extends ExtJSAjaxAction {
	/**
	 * Logger. 
	 */
	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
	public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        HttpSession session = request.getSession();
        
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
		String      idBline = eltBean.getBaseline().getId();

        Locale locale = RequestUtil.getLocale(request);

        GraphImageConfig imgConfig = new GraphImageConfig();
        imgConfig.setWidth(300);
        imgConfig.setHeight(220);
        
        String widthStr = request.getParameter("width");
        String heightStr = request.getParameter("height");

        if (widthStr != null && widthStr.matches("[0-9]+")) {
        	imgConfig.setWidth(Integer.parseInt(widthStr));
        }
        if (heightStr != null && heightStr.matches("[0-9]+")) {
        	imgConfig.setHeight(Integer.parseInt(heightStr));
        }
        
        FactorRepartitionBeanList repartitionObj = ActionPlanSimulateGoalsMark.getInstance().calculatePieChartMarks(
        		eltBean, idBline, request);
        
        MessageResources resources = this.getResources(request);
        String ameliorationTitle = resources.getMessage(locale, "caqs.pieapplet.amelioration");
        String targetTitle = resources.getMessage(locale, "caqs.objectif");
        imgConfig.setTitle(ameliorationTitle + ' ' + targetTitle);
        MessagesCodes msg = MessagesCodes.NO_ERROR;
        try {
            String datas = createChart(request, session, repartitionObj, imgConfig);
            retour.put("datas", datas);
        } catch(IOException exc) {
            logger.error(exc);
            msg = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(retour, msg);
        return retour;
	}
	
	private String createChart(
    		HttpServletRequest request,
            HttpSession session,
            Collection<FactorRepartitionBean> repartition,
            GraphImageConfig imgConfig) throws IOException {
        StringBuffer xmlout = new StringBuffer();
        XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, RequestUtil.getLocale(request));
        
        InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
        JFreeChart pieChart = PieChartFactory.createFromXml(str, ChartConfigGenerator.getPieConfig(imgConfig.getTitle(), true));
        pieChart.setBackgroundPaint(Color.WHITE);
        TextTitle title = new TextTitle(imgConfig.getTitle(), this.getKiviatTitleFont());
        pieChart.setTitle(title);
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
	
	
	private Font getKiviatTitleFont() {
		return new Font("Helvetica", Font.BOLD, 12);
	}
}

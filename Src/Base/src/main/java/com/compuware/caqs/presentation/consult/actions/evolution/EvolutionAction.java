/*
 * EvolutionServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */

package com.compuware.caqs.presentation.consult.actions.evolution;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.data.xml.DatasetTags;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.EvolutionBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.EvolutionSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author cwfr-fdubois
 */
public class EvolutionAction extends Action {


    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        LoggerManager.pushContexte("Evolutionservlet");

        response.setContentType("text/html;charset=" + Constants.GLOBAL_CHARSET);
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        String type = request.getParameter("type");
        String target = request.getParameter("target");

        Locale loc = RequestUtil.getLocale(request);

    	EvolutionSvc evolutionSvc = EvolutionSvc.getInstance();
        Collection<BaselineBean> baselines = evolutionSvc.retrieveBaselines(eltBean);
        Collection factors = evolutionSvc.retrieveEvolution(eltBean, target);

        PrintWriter out = response.getWriter();

        if ((type == null) && (baselines.size() <= 2)) {
            type = "BAR";
        }

        if (type != null && type.equals("BAR")) {
            writeBarData(out, baselines, factors, loc);
        } else {
            writeLineData(out, baselines, factors, loc);
        }
        out.flush();

        LoggerManager.popContexte();
        
        return null;
    }

    private void writeLineData(PrintWriter out, Collection<BaselineBean> baselines, Collection factors, Locale loc) {
    	out.println("LINE");
        out.println("<?xml version=\"1.0\" encoding=\"" + Constants.GLOBAL_CHARSET + "\"?>");
        out.println();
        out.println("<" + DatasetTags.CATEGORYDATASET_TAG + ">");
        Iterator fiter = factors.iterator();
        while (fiter.hasNext()) {
            EvolutionBean fbean = (EvolutionBean) fiter.next();
            out.println("<" + DatasetTags.SERIES_TAG + " name=\"" + fbean.getLib(loc) + "\">");
            Iterator<BaselineBean> biter = baselines.iterator();
            while (biter.hasNext()) {
                BaselineBean bb = biter.next();
            	String key = bb.getLib();
                out.println("<" + DatasetTags.ITEM_TAG + ">");
                out.print("<" + DatasetTags.KEY_TAG + ">");
                out.print(key);
                out.println("</" + DatasetTags.KEY_TAG + ">");
                out.print("<" + DatasetTags.VALUE_TAG + ">");
                double d = fbean.getDoubleEntry(bb.getId());
                out.print(d - 0.005);
                out.println("</" + DatasetTags.VALUE_TAG + ">");
                out.println("</" + DatasetTags.ITEM_TAG + ">");
            }
            out.println("</" + DatasetTags.SERIES_TAG + ">");
        }
        out.println("</" + DatasetTags.CATEGORYDATASET_TAG + ">");
    }

    private void writeBarData(PrintWriter out, Collection<BaselineBean> baselines, Collection factors, Locale loc) {
        out.println("BAR");
        out.println("<?xml version=\"1.0\" encoding=\"" + Constants.GLOBAL_CHARSET + "\"?>");
        out.println();
        out.println("<" + DatasetTags.CATEGORYDATASET_TAG + ">");
        Iterator<BaselineBean> biter = baselines.iterator();
        while (biter.hasNext()) {
            BaselineBean bb = biter.next();
            String idBline = bb.getId();
            out.println("<" + DatasetTags.SERIES_TAG + " name=\"" + idBline + "\">");
            Iterator fiter = factors.iterator();
            while (fiter.hasNext()) {
            	EvolutionBean fbean = (EvolutionBean) fiter.next();
                out.println("<" + DatasetTags.ITEM_TAG + ">");
                out.print("<" + DatasetTags.KEY_TAG + ">");
                out.print(fbean.getLib(loc));
                out.println("</" + DatasetTags.KEY_TAG + ">");
                out.print("<" + DatasetTags.VALUE_TAG + ">");
                double d = fbean.getDoubleEntry(idBline);
                out.print(d - 0.005);
                out.println("</" + DatasetTags.VALUE_TAG + ">");
                out.println("</" + DatasetTags.ITEM_TAG + ">");
            }
            out.println("</" + DatasetTags.SERIES_TAG + ">");
        }
        out.println("</" + DatasetTags.CATEGORYDATASET_TAG + ">");
    }

}

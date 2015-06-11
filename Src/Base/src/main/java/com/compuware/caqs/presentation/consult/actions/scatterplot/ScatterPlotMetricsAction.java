/*
 * ScatterPlotServlet.java
 *
 * Created on 24 mars 2004, 13:44
 */
package com.compuware.caqs.presentation.consult.actions.scatterplot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author cwfr-fdubois
 */
public class ScatterPlotMetricsAction extends Action {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        LoggerManager.pushContexte("ScatterPlotRetrieve");

        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idTelt = request.getParameter("idTelt");
        try {
            if (eltBean == null) {
                String idElt = request.getParameter("id_elt");
                String idBline = request.getParameter("id_bline");
                ElementSvc elementSvc = ElementSvc.getInstance();
                eltBean = elementSvc.retrieveElement(idElt, idBline);
            }

            DaoFactory daoFactory = DaoFactory.getInstance();
            MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
            Map<String, MetriqueDefinitionBean> metMap = metriqueFacade.retrieveMetriqueDefinition(eltBean.getBaseline().getId(), idTelt);
            logger.debug("Number of metrics: " + metMap.size());

            StringBuffer buff = new StringBuffer();
            Set<String> s = metMap.keySet();
            for (Iterator<String> it = s.iterator(); it.hasNext();) {
                String key = it.next();
                MetriqueDefinitionBean val = metMap.get(key);
                buff.append(key).append(';').append(val.getLib(RequestUtil.getLocale(request))).append('\n');
            }
            logger.debug("sending : "+buff.toString());
            GZIPOutputStream zout = new GZIPOutputStream(response.getOutputStream());
            zout.write(buff.toString().getBytes());//Constants.GLOBAL_CHARSET));
            zout.flush();
            zout.close();
            LoggerManager.popContexte();
        } catch (CaqsException e) {
            throw new ServletException(e);
        }
        return null;
    }
}

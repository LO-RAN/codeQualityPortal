package com.compuware.caqs.presentation.consult.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class ScatterPlotPrepareAction extends ElementSelectedActionAbstract {


    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public static final String MET_H_KEY = "metH";
    public static final String MET_V_KEY = "metV";
    public static final String CENTER_H_KEY = "centerH";
    public static final String CENTER_V_KEY = "centerV";

    // --------------------------------------------------------- Public Methods


    public ActionForward doExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        // Extract attributes we will need

        LoggerManager.pushContexte("ScatterPlotPrepare");
        HttpSession session = request.getSession();
        //ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        //DaoFactory daoFactory = DaoFactory.getInstance();
        //MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
        //Map metMap = metriqueFacade.retrieveMetriqueDefinition(eltBean.getBaseline().getId());
        //logger.debug("Number of metrics: "+metMap.size());
        String metH = request.getParameter(MET_H_KEY);
        request.setAttribute(MET_H_KEY, metH);
        session.setAttribute(MET_H_KEY, metH);
        String metV = request.getParameter(MET_V_KEY);
        request.setAttribute(MET_V_KEY, metV);
        session.setAttribute(MET_V_KEY, metV);
        logger.debug("metH="+metH+", metV="+metV);
        forwardParameter(CENTER_H_KEY, request);
        forwardParameter(CENTER_V_KEY, request);
        //request.setAttribute("metriqueCollection", metMap.values());

        LoggerManager.popContexte();

        return mapping.findForward("success");

    }

    private void forwardParameter(String parameterName, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String param = request.getParameter(parameterName);
        if (param == null) {
            param = (String)session.getAttribute(parameterName);
        }
        request.setAttribute(parameterName, param);
        session.setAttribute(parameterName, param);
    }

    /*private MetriqueDefinitionBean getMetriqueDefinition(String idMet, Map metMap) {
    	MetriqueDefinitionBean result = null;
        if (metMap != null) {
        	result = (MetriqueDefinitionBean)metMap.get(idMet);         
		}
        if (result == null) {
            result = new MetriqueDefinitionBean();
            result.setId(idMet);
        }
        return result;
    }*/

}

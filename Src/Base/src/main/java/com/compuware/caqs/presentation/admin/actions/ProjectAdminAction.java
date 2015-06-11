package com.compuware.caqs.presentation.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ProjectAdminAction extends Action {
	protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

	private static final long serialVersionUID = -1928302713876938119L;

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
    	return mapping.findForward("success");
	}

}

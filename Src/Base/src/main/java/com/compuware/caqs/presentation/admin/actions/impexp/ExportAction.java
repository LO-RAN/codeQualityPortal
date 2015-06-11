package com.compuware.caqs.presentation.admin.actions.impexp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.impexp.ImportExportSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ExportAction extends Action {

    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public static final String IDPRO_PARAM_KEY = "idPro";
    public static final String LIBPRO_PARAM_KEY = "libPro";
	
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

    	String idPro = request.getParameter(IDPRO_PARAM_KEY);
    	String libPro = request.getParameter(LIBPRO_PARAM_KEY);

        ImportExportSvc.getInstance().launchRemoteExportProject(idPro, libPro,
                RequestUtil.getConnectedUserId(request), request);
    	
        return null;
    }
}

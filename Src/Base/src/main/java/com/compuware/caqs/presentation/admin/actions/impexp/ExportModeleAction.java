package com.compuware.caqs.presentation.admin.actions.impexp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.impexp.ImportExportSvc;
import net.sf.json.JSONObject;

public class ExportModeleAction extends ExtJSAjaxAction {

    public static final String IDUSA_PARAM_KEY = "idUsa";

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String idUsa = request.getParameter(IDUSA_PARAM_KEY);
        ImportExportSvc.getInstance().launchRemoteExportModel(idUsa,
                RequestUtil.getConnectedUserId(request), request);
        this.fillJSONObjectWithReturnCode(obj, retour);

        return obj;
    }
	
    
}

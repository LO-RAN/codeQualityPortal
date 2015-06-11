package com.compuware.caqs.presentation.admin.actions.impexp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.impexp.ImportExportSvc;

public class ImportZipAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	JSONObject obj = new JSONObject();
        String target = request.getParameter("target");
    	String fileName = request.getParameter("fileName");
        String idUser = RequestUtil.getConnectedUserId(request);

        ImportExportSvc.getInstance().launchRemoteImportZip(target, fileName, idUser, request);
        return obj;
    }
}

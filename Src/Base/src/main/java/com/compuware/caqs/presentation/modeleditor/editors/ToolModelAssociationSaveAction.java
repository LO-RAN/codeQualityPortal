package com.compuware.caqs.presentation.modeleditor.editors;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;

import com.compuware.caqs.service.OutilsSvc;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import net.sf.json.JSONObject;

public class ToolModelAssociationSaveAction extends ExtJSAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String toolId = request.getParameter("toolId");
        String modelId = request.getParameter("modelId");
        String associate = request.getParameter("associate");

        boolean b = Boolean.parseBoolean(associate);
        if (b) {
            OutilsSvc.getInstance().addToolAssociationToModel(modelId, toolId);
        } else {
            OutilsSvc.getInstance().removeToolAssociationForModel(modelId, toolId);
        }


        return retour;
    }
}

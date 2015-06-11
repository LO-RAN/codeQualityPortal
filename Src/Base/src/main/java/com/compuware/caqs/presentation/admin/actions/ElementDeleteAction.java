package com.compuware.caqs.presentation.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.AdminSvc;

public final class ElementDeleteAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        String idElt = request.getParameter("id_elt");
        String idTelt = request.getParameter("id_telt");

        AdminSvc adminSvc = AdminSvc.getInstance();

        boolean retour = adminSvc.delete(idElt, idTelt);
        if(retour) {
            ElementBean fatherBean = adminSvc.retrieveFatherElement(idElt);
            this.removeProjectFromCache(fatherBean);
        }
        JSONObject obj = new JSONObject();
        obj.put("success", retour);
        return obj;
    }

    public void removeProjectFromCache(ElementBean eltBean) {
        String idPro = "";
        AdminSvc adminSvc = AdminSvc.getInstance();
        if (eltBean != null) {
            if (eltBean.getProject() != null) {
                idPro = eltBean.getProject().getId();
            } else {
                ElementBean elt = adminSvc.retrieveElementById(eltBean.getId());
                if (elt.getProject() != null) {
                    idPro = elt.getProject().getId();
                }
            }
            if (!"".equals(idPro)) {
                adminSvc.clearCache(idPro);
            }
        }
    }
}

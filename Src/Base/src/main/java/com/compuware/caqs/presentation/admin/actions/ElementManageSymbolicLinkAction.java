package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.util.RequestUtil;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.AdminSvc;
import org.apache.struts.util.MessageResources;

public final class ElementManageSymbolicLinkAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject object = new JSONObject();
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        MessagesCodes retour = MessagesCodes.NO_ERROR;

        String action = request.getParameter("action");

        AdminSvc adminSvc = AdminSvc.getInstance();

        String fatherIdElt = request.getParameter("new_father_id");
        String childIdElt = request.getParameter("new_child_id");
        if ("create".equals(action)) {
            if (fatherIdElt == null || childIdElt == null) {
                mLog.error("Erreur lors de l'ajout du lien symbolique : fatherId=" + fatherIdElt + ", childId=" + childIdElt);
                retour = MessagesCodes.CREATE_SYMLINK_ERROR;
            } else {
                retour = adminSvc.createSymbolicLink(fatherIdElt,
                        childIdElt);
            }
        } else if ("delete".equals(action)) {
            if (fatherIdElt == null || childIdElt == null) {
                mLog.error("Erreur lors de la suppression du lien symbolique : fatherId=" + fatherIdElt + ", childId=" + childIdElt);
                retour = MessagesCodes.DELETE_SYMLINK_ERROR;
            } else {
                retour = adminSvc.deleteSymbolicLink(fatherIdElt, childIdElt);
            }
        }

        this.fillJSONObjectWithReturnCode(object, retour);
        return object;
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

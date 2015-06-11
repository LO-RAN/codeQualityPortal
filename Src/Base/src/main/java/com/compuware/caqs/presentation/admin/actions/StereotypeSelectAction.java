/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 09:26:10
*/

package com.compuware.caqs.presentation.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.StereotypeBean;
import com.compuware.caqs.service.AdminSvc;

public class StereotypeSelectAction extends StereotypeAction {

    public static final String ID_STEREOTYPE = "idStereotype";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String idStereotype = request.getParameter(ID_STEREOTYPE);
        if (idStereotype != null) {
            AdminSvc adminSvc = AdminSvc.getInstance();
            StereotypeBean stereotypeBean = adminSvc.retrieveStereotype(idStereotype);
            form = beanToForm(stereotypeBean);
        }
        return mapping.findForward("success");
    }
}

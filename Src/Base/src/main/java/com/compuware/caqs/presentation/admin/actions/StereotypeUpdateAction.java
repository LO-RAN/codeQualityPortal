/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 09:26:41
*/

package com.compuware.caqs.presentation.admin.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.StereotypeBean;
import com.compuware.caqs.presentation.admin.forms.StereotypeForm;
import com.compuware.caqs.service.AdminSvc;

public class StereotypeUpdateAction extends StereotypeAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        StereotypeForm stereotypeForm = (StereotypeForm)form;
        StereotypeBean stereotypeBean = formToBean(stereotypeForm);
        AdminSvc adminSvc = AdminSvc.getInstance();
        adminSvc.storeStereotype(stereotypeBean);
        return mapping.findForward("success");
    }

}

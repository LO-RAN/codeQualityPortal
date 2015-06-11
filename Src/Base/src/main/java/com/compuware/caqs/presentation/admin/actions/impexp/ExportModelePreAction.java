package com.compuware.caqs.presentation.admin.actions.impexp;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.service.AdminSvc;

public class ExportModelePreAction extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

    	AdminSvc adminSvc = AdminSvc.getInstance();
    	Collection<UsageBean> usageCollection = adminSvc.retrieveAllModels();
    	request.setAttribute("usageCollection", usageCollection);
    	
    	return mapping.findForward("success");
    }	
}

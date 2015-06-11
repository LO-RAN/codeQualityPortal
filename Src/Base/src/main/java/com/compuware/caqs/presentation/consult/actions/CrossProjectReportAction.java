package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.service.FactorSvc;

public final class CrossProjectReportAction extends Action {


    // --------------------------------------------------------- Public Methods


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {

    	FactorSvc factorSvc = FactorSvc.getInstance();
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=\"CrossProjectReport.csv\"");
    	factorSvc.writeAllElementFactors(response.getWriter());
    	
        return null;

    }
   
}

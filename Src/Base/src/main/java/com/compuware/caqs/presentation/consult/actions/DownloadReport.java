package com.compuware.caqs.presentation.consult.actions;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.common.actions.DownloadZipAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.report.ReportSvc;

public class DownloadReport extends DownloadZipAction {
	 
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException {

        HttpSession session = request.getSession();
        
        String idElt = request.getParameter("id_elt");
        String idBline = request.getParameter("id_bline");
        
        ElementBean eltBean = null;
        if(idElt!=null) {
        	eltBean = ElementSvc.getInstance().retrieveElementById(idElt);
        } else {
        	eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        }
        if(idBline==null) {
        	idBline = eltBean.getBaseline().getId();
        }

        ActionForward forward = null;
        if (eltBean != null) {
        	File docFile = ReportSvc.getInstance().getReportFile(eltBean, idBline, RequestUtil.getLocale(request));
        	if(docFile!=null && docFile.exists() && docFile.isFile()) {
        		forward = returnFile(mapping, docFile, response);	        		
        	}
        }

        return forward;
	
	}
}

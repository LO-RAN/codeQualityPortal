package com.compuware.caqs.presentation.admin.actions.i18n;

import com.compuware.caqs.presentation.util.CaqsUtils;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.compuware.caqs.presentation.admin.forms.i18n.InternationalizationUploadForm;
import com.compuware.caqs.service.InternationalizationSvc;

public class InternationalizationUpdateAction extends Action {
	
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
    	throws IOException, ServletException {
    	
    	InternationalizationUploadForm uploadForm = (InternationalizationUploadForm)form;
    	FormFile file = uploadForm.getFile();
    	if (file != null && file.getFileSize() > 0) {
    		InternationalizationSvc internationalisationSvc = InternationalizationSvc.getInstance();
    		internationalisationSvc.saveInternationalizationDataFromCsv(file.getInputStream());
    	}
    	else {
    		if (request.getParameter("refresh") != null) {
        		InternationalizationSvc internationalisationSvc = InternationalizationSvc.getInstance();
        		internationalisationSvc.initResourceBundles();
    		}
    	}
		
    	return mapping.findForward("success");
    }
}

package com.compuware.caqs.presentation.admin.actions;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.actions.DownloadZipAction;
import com.compuware.caqs.service.impexp.ImportExportSvc;

public class DownloadProject extends DownloadZipAction {
	 
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException {
        
        String idPro = request.getParameter("idPro");
        
        ActionForward forward = null;
        File docFile = ImportExportSvc.getInstance().retrieveProjectFile(idPro, null);
        if(docFile!=null && docFile.exists() && docFile.isFile()) {
        	forward = returnFile(mapping, docFile, response);	        		
        }

        return forward;
	
	}
}

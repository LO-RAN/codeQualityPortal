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

public class DownloadModel extends DownloadZipAction {
	 
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException {
        
        String idUsa = request.getParameter("idUsa");
        
        ActionForward forward = null;
        File docFile = ImportExportSvc.getInstance().retrieveModeleFile(idUsa);
        if(docFile!=null && docFile.exists() && docFile.isFile()) {
        	forward = returnFile(mapping, docFile, response);	        		
        }

        return forward;
	
	}  
}

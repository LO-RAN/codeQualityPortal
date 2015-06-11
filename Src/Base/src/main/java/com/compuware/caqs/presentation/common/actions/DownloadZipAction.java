package com.compuware.caqs.presentation.common.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

public class DownloadZipAction extends Action {
	
	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
	
	protected ActionForward returnFile(ActionMapping mapping, File zipFile, HttpServletResponse response) {

		byte[] fileContent = null;
		OutputStream out = null;
        ActionForward retour = null;
		try {
			fileContent = getFileContent(zipFile);

			if (fileContent == null) {
				retour = mapping.findForward("failure");
			} else {
                out = response.getOutputStream();
                // Set your response headers here
                response.setContentType("application/x-download");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFile.getName() + "\"");
                response.setContentLength(fileContent.length);

                out.write(fileContent);
            }
		}
		catch (IOException ioe) {
			retour = mapping.findForward("failure");
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					//ignore subtly
					logger.error("Error during closing stream", ioe);
				}
			}
		}

		return retour;
	}
    
    private byte[] getFileContent(File f) {
    	byte[] result = null;
        try {
            result = new byte[(int)f.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(f));
            input.read(result);
            input.close();
        } catch (IOException e) {
            logger.error("Error reading zip file: "+f.getName(), e);
            result = null;
        }
    	return result;
    }
}

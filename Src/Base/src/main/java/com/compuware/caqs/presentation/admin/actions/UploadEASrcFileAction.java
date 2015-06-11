package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.business.util.AntExecutor;
import com.compuware.caqs.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;

public class UploadEASrcFileAction extends ExtJSAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String srcFile = request.getParameter("srcFilePath");
        String sourceDir = request.getParameter("sourceDir");
        this.saveSrcFile(srcFile, sourceDir, retour, request);
        return retour;
    }
    private static final String EXTRACT_SRC_FILES_TARGET = "extractSrc";

    private ActionForward saveSrcFile(String srcFilePath, String sourceDir, JSONObject retour, HttpServletRequest request) {
        ActionForward result = null;
        boolean uploadOK = true;
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        if (srcFilePath != null && !"".equals(srcFilePath)) {
            File zipFileToImport = new File(srcFilePath);
            if (zipFileToImport.isFile()) {
                try {
                    String baseDir = getSourcePath(sourceDir);
                    File dir = new File(baseDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File tmpFile = new File(dir, "src.zip");
                    FileTools.copy(zipFileToImport, tmpFile);
                    AntExecutor command = new AntExecutor(mLog);
                    command.processAntScript(EXTRACT_SRC_FILES_TARGET, getAntProperties(baseDir));
                    File srcDir = new File(dir, "/src/");
                    if (!srcDir.exists()) {
                        retour.put("uploadError", resources.getMessage(loc, "caqs.element.upload.src.nosrcdir"));
                        uploadOK = false;
                    }
                } catch (FileNotFoundException e) {
                    retour.put("uploadError", resources.getMessage(loc, "caqs.element.upload.src.filenotfound"));
                    mLog.error("Source upload file not found", e);
                    uploadOK = false;
                } catch (IOException e) {
                    mLog.error("Error getting source upload file data", e);
                    retour.put("uploadError", resources.getMessage(loc, "caqs.element.upload.src.ioexception"));
                    uploadOK = false;
                }
                retour.put("uploadDone", uploadOK);
            }
        }
        return result;
    }

    private String getSourcePath(String relativePath) {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String basePath = null;
        try {
            basePath = new File(dynProp.getProperty(Constants.SRC_BASE_PATH)).getCanonicalPath();
        } catch (IOException ex) {
            mLog.error("Can't get full path : " + ex.getMessage());
        }
        String result = relativePath;
        if (basePath != null) {
            result = FileTools.concatPath(basePath, result);
        }
        return result;
    }

    private Properties getAntProperties(String srcPath) {
        Properties result = new Properties();
        result.put("SRC_DIR", srcPath);
        return result;
    }
}

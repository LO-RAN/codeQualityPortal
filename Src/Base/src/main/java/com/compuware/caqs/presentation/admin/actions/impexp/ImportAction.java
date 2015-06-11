package com.compuware.caqs.presentation.admin.actions.impexp;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.presentation.admin.forms.impexp.ImportUploadForm;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.impexp.ImportExportSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import net.sf.json.JSONObject;

public class ImportAction extends ExtJSAjaxAction {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes retour = MessagesCodes.ERROR_DURING_FILE_UPLOAD;
        ImportUploadForm uploadForm = (ImportUploadForm) form;
        FormFile file = uploadForm.getFile();
        boolean success = false;
        if (file != null && file.getFileSize() > 0) {
            ImportExportSvc importSvc = ImportExportSvc.getInstance();
            String destDir = importSvc.getImportDestinationDir();
            try {
                String fileName = file.getFileName();
                File zipFile = importSvc.createFile(destDir, fileName, file.getInputStream());
                if (zipFile != null && zipFile.exists()) {
                    String absolutePath = zipFile.getAbsolutePath();
                    absolutePath = absolutePath.replaceAll("\\\\", "/");
                    obj.put("filePathToUpload", absolutePath);
                    success = true;
                }
            } catch (IOException e) {
                logger.error("Error during import", e);
            }
        }
        if(success) {
            retour = MessagesCodes.NO_ERROR;
        }
        this.fillJSONObjectWithReturnCode(obj, retour);

        return obj;
    }
}

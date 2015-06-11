package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.presentation.consult.actions.upload.UploadFactory;
import com.compuware.caqs.presentation.consult.actions.upload.uploaders.AbstractCaqsUploader;
import com.compuware.caqs.presentation.util.RequestUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.forms.UploadDataForm;
import com.compuware.caqs.service.UploadDataSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.JndiReader;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

public class UploadDataAction extends ElementSelectedActionAbstract {

    protected static Logger mLog = LoggerManager.getLogger("UploadDynamique");

    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        ActionForward forward = mapping.findForward("success");
        ActionErrors errors = new ActionErrors();

        UploadDataForm uploadForm = (UploadDataForm) form;
        FormFile file = uploadForm.getFile();

        mLog.info("Received an Upload Request.");
        HttpSession session = request.getSession(true);
        response.setContentType("text/html");

        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        String baseLineId = eltBean.getBaseline().getId();

        String caqsHome = (String) JndiReader.getValue(Constants.CAQS_HOME_KEY, Constants.CAQS_HOME_DEFAULT);
        String mSavePath = File.separator + Constants.UPLOAD_DIRECTORY_KEY +
                File.separator + baseLineId;
        mSavePath = FileTools.concatPath(caqsHome, mSavePath);
        File dir = new File(mSavePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tmpFile = File.createTempFile("data-", "", dir);
        FileOutputStream writer = new FileOutputStream(tmpFile);
        writer.write(file.getFileData());
        writer.flush();
        writer.close();

        try {
            AbstractCaqsUploader uploader = UploadFactory.getInstance().create(tmpFile, uploadForm);
            UploadDataSvc uploadSvc = UploadDataSvc.getInstance();
            if (uploader != null) {
                uploader.setApplicationEntity(eltBean);
                if (uploader.extractMetrics()) {
                    uploadSvc.uploadLoadedMetricsList(uploader);
                }
                MessagesSvc.getInstance().addMessageAndSetCompleted(TaskId.DATA_UPLOAD,
                        eltBean.getId(), baseLineId, null, null,
                        RequestUtil.getConnectedUserId(request));
            }
            else {
                errors.add("upload", new ActionMessage("caqs.upload.error.nouploaderfound"));
            }
        }
        catch (CaqsException e) {
            mLog.error("Error during upload", e);
            if (e.getMsgKey() != null) {
                errors.add("upload", new ActionMessage(e.getMsgKey()));
            }
            forward = mapping.findForward("failure");
        }
        addErrors(request, errors);
        return forward;
    }
}

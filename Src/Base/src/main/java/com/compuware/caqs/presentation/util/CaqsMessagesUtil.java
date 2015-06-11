package com.compuware.caqs.presentation.util;

import java.io.File;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.business.report.Reporter;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.service.impexp.ImportExportSvc;
import com.compuware.caqs.util.ErrorFileUtil;
import net.sf.json.JSONObject;

public class CaqsMessagesUtil {

    public static final void fillSpecialMessageTaskInfos(JSONObject obj,
            CaqsMessageBean message, HttpServletRequest request) {
        TaskId taskId = message.getTask().getTaskId();
        if (taskId.hasSpecialMessage()) {
            if (CaqsMessagesUtil.statusForWhichDisplayMsg(taskId, message)) {
                obj.put("specialAction", "true");
                obj.put("specialActionHref", CaqsMessagesUtil.getSpecialActionHrefForTask(taskId, message, request));
                obj.put("specialActionId", CaqsMessagesUtil.getSpecialActionIdForTask(taskId, message));
                obj.put("specialActionTooltip",
                        CaqsMessagesUtil.getSpecialMessageTooltipForTask(taskId, request));
                obj.put("specialActionImg", CaqsMessagesUtil.getSpecialActionImgForTask(taskId, message));
                obj.put("specialActionType", CaqsMessagesUtil.getSpecialMessageType(taskId));
            }
        }
    }

    private static final String getSpecialMessageType(TaskId taskId) {
        String retour = "";
        if (taskId.equals(TaskId.GENERATING_REPORT)) {
            retour = "url";
        } else if (taskId.equals(TaskId.EXPORT_MODEL)) {
            retour = "url";
        } else if (taskId.equals(TaskId.EXPORT_PROJECT)) {
            retour = "url";
        } else if (taskId.equals(TaskId.ANALYSING)) {
            retour = "popup";
        }
        return retour;
    }

    private static final boolean statusForWhichDisplayMsg(TaskId taskId, CaqsMessageBean message) {
        boolean retour = false;
        if (taskId.equals(TaskId.GENERATING_REPORT)) {
            retour = message.getStatus().equals(MessageStatus.COMPLETED);
        } else if (taskId.equals(TaskId.EXPORT_MODEL)) {
            retour = message.getStatus().equals(MessageStatus.COMPLETED);
        } else if (taskId.equals(TaskId.EXPORT_PROJECT)) {
            retour = message.getStatus().equals(MessageStatus.COMPLETED);
        } else if (taskId.equals(TaskId.ANALYSING)) {
            File errorFile = ErrorFileUtil.getErrorFile(message.getIdBline(), "analysis");
            retour = (errorFile != null && errorFile.exists());
        }
        return retour;
    }

    private static final String getSpecialMessageTooltipForTask(TaskId t, HttpServletRequest request) {
        String retour = null;
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        if (t.equals(TaskId.GENERATING_REPORT)) {
            retour = resources.getString("caqs.synthese.recupReport");
        } else if (t.equals(TaskId.EXPORT_MODEL)) {
            retour = resources.getString("caqs.admin.recupModel");
        } else if (t.equals(TaskId.EXPORT_PROJECT)) {
            retour = resources.getString("caqs.admin.recupProject");
        } else if (t.equals(TaskId.ANALYSING)) {
            retour = resources.getString("caqs.messages.analysisFailed.specialActionMsg");
        }

        return retour;
    }

    private static final String getSpecialActionIdForTask(TaskId t, CaqsMessageBean message) {
        String retour = null;

        if (message != null) {
            if (t.equals(TaskId.GENERATING_REPORT)) {
                retour = "dowloadReportImg" + message.getEltBean().getId() +
                        message.getIdBline();
            } else if (t.equals(TaskId.EXPORT_MODEL)) {
                retour = "dowloadModelImg" + message.getOtherId();
            } else if (t.equals(TaskId.EXPORT_PROJECT)) {
                retour = "dowloadProjectImg" + message.getOtherId();
            } else if (t.equals(TaskId.ANALYSING)) {
                retour = "analysingFailed" + message.getIdBline();
            }
        }

        return retour;
    }

    private static final String getSpecialActionHrefForTask(TaskId t,
            CaqsMessageBean message, HttpServletRequest request) {
        String retour = null;

        if (message != null) {
            if (t.equals(TaskId.GENERATING_REPORT)) {
                Reporter reporter = new Reporter();
                File f = reporter.getReportFile(message.getEltBean(), message.getIdBline(), RequestUtil.getLocale(request));
                if (f != null && f.exists() && f.isFile()) {
                    retour = request.getContextPath() +
                            "/DownloadReport.do?id_elt=" +
                            message.getEltBean().getId() +
                            "&id_bline=" + message.getIdBline();
                }
            } else if (t.equals(TaskId.EXPORT_MODEL)) {
                String idUsa = message.getOtherId();
                File f = ImportExportSvc.getInstance().retrieveModeleFile(idUsa);
                if (f != null && f.exists() && f.isFile()) {
                    retour = request.getContextPath() +
                            "/DownloadModel.do?idUsa=" + idUsa;
                }
            } else if (t.equals(TaskId.EXPORT_PROJECT)) {
                String idPro = message.getOtherId();
                File f = ImportExportSvc.getInstance().retrieveProjectFile(idPro, null);
                if (f != null && f.exists() && f.isFile()) {
                    retour = request.getContextPath() +
                            "/DownloadProject.do?idPro=" + idPro;
                }
            } else if (t.equals(TaskId.ANALYSING)) {
                retour = "/RetrieveTextForAnalysisFailed.do?idBline=" +
                        message.getIdBline();
            }

        }

        return retour;
    }

    private static final String getSpecialActionImgForTask(TaskId t, CaqsMessageBean message) {
        String retour = null;

        if (message != null) {
            if (t.equals(TaskId.GENERATING_REPORT)) {
                retour = "/images/Word2007.png";
            } else if (t.equals(TaskId.EXPORT_MODEL)) {
                retour = "/images/page_white_zip.gif";
            } else if (t.equals(TaskId.EXPORT_PROJECT)) {
                retour = "/images/page_white_zip.gif";
            } else if (t.equals(TaskId.ANALYSING)) {
                retour = "/images/application.gif";
            }
        }

        return retour;
    }
}

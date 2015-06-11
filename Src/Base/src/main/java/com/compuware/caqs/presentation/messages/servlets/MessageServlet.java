package com.compuware.caqs.presentation.messages.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.presentation.common.servlets.ResultServlet;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ProjectSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public class MessageServlet extends ResultServlet {

    /**
     *
     */
    private static final long serialVersionUID = -3859196089257816568L;

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPro = request.getParameter("projectId");
        String idBline = request.getParameter("baselineId");
        String paramPercentage = request.getParameter("percent");
        String idUser = request.getParameter("userId");
        String status = request.getParameter("status");
        String idEa = request.getParameter("id_ea");
        String step = request.getParameter("step");

        AnalysisResult result = new AnalysisResult();

        LoggerManager.pushContexte("MessageServlet");
        if (idBline != null && paramPercentage != null) {
            int percent = 0;
            try {
                percent = Integer.parseInt(paramPercentage);
            } catch (NumberFormatException e) {
                logger.error("MessageServlet : bad percentage : " + paramPercentage, e);
            }

            TaskId task = TaskId.ANALYSING;
            String taskParameter = request.getParameter("task");
            if (taskParameter != null) {
                task = TaskId.valueOf(taskParameter);
            }

            //on recherche l'identifiant du message
            String idMess = MessagesSvc.getInstance().retrieveNotFinishedMessageId(idEa, idBline, task);
            if (idMess == null) {
                //le message n'existe pas. on le cree
                List<String> l = new ArrayList<String>();
                if (idPro != null) {
                    ProjectBean project = ProjectSvc.getInstance().retrieveProjectById(idPro);
                    ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);
                    String libPro = resources.getString("caqs.error.projectNotFound");
                    if (project != null) {
                        libPro = project.getLib();
                    }
                    l.add(Constants.MESSAGES_LIBPRJ_INFO1 + libPro);
                }
                idMess = MessagesSvc.getInstance().addMessageWithStatus(task, idEa,
                        idBline, idUser, l, null, MessageStatus.IN_PROGRESS);
            }
            logger.debug("MessageServlet : received step : " + step);
            if (step != null && !"".equals(step)) {
                MessagesSvc.getInstance().setInProgressMessageToStep(idMess, step);
            }
            MessagesSvc.getInstance().setMessagePercentage(idMess, percent);
            if (status != null && !"".equals(status)) {
                MessagesSvc.getInstance().setMessageTaskStatus(idMess, MessageStatus.valueOf(status));
            }
            result.setSuccess(true);
        }
        LoggerManager.popContexte();
        return result;
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Message servlet";
    }
}

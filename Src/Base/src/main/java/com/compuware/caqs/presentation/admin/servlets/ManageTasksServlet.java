package com.compuware.caqs.presentation.admin.servlets;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.presentation.common.servlets.ResultServlet;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cwfr-dzysman
 */
public class ManageTasksServlet extends ResultServlet {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

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
    protected AnalysisResult doProcessRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        AnalysisResult result = new AnalysisResult();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        LoggerManager.pushContexte("TasksServlet");
        if (action == null) {
            //on fait toutes les actions que l'on a prevues
        } else {
            TaskId actionToDo = TaskId.safeValueOf(action);
            if (actionToDo.equals(TaskId.UNDEFINED)) {
                result.setSuccess(false);
                String allActions = "";
                TaskId[] tasks = TaskId.getAllProcessable();
                boolean first = true;
                for(int i=0; i<tasks.length; i++) {
                    if(!first) {
                        allActions += ", ";
                    }
                    allActions += tasks[i].toString();
                    first = false;
                }
                String msg = resources.getString("caqs.errormessage.actionnotfound");
                MessageFormat format = new MessageFormat(msg);
                msg = format.format(new Object[]{allActions});
                result.setMessage(msg);
            } else {
                List<CaqsMessageBean> tasks = MessagesSvc.getInstance().retrieveAllActionsToDoFor(actionToDo);
                if (!tasks.isEmpty()) {
                    if (actionToDo.equals(TaskId.DELETE_ELEMENTS)) {
                        //on veut supprimer les elements flagges
                        for (CaqsMessageBean mb : tasks) {
                            MessagesSvc.getInstance().setMessageTaskStatus(mb.getIdMessage(), MessageStatus.IN_PROGRESS);
                            retour = ElementSvc.getInstance().deletePeremptedElement(mb.getEltBean().getId());
                            if (!retour.equals(MessagesCodes.NO_ERROR)) {
                                MessagesSvc.getInstance().setMessageTaskStatus(mb.getIdMessage(), MessageStatus.FAILED);
                                logger.error("error while deleting element "+mb.getEltBean().getLib());
                                break;
                            } else {
                                MessagesSvc.getInstance().setMessageTaskStatus(mb.getIdMessage(), MessageStatus.COMPLETED);
                                logger.info("element "+mb.getEltBean().getLib()+" deleted.");
                            }
                        }
                    }
                }
                result.setSuccess(retour.equals(MessagesCodes.NO_ERROR));
                if (!retour.equals(MessagesCodes.NO_ERROR)) {
                    result.setMessage(resources.getString(retour.getCode()));
                }
            }
        }

        LoggerManager.popContexte();
        return result;
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Manage Task servlet";
    }
}

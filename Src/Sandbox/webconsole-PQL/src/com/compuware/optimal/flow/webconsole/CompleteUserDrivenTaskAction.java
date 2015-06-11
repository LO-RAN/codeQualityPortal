/*
 * © 2002 Compuware Corporation. All rights reserved.
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 */

/* Disclaimer
 * You have a royalty-free right to use, modify, reproduce and distribute this
 * sample code (and/or any modified version) in any way you find useful,
 * provided that you agree that Compuware has no warranty obligations or
 * liability for any sample code which has been modified.
 */
package com.compuware.optimal.flow.webconsole;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.RedirectingActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import com.compuware.optimal.flow.*;
import com.compuware.optimal.flow.webconsole.*;


/**
 * This <strong>Action</strong> class is called when the user clicks the link
 * of a User-Driven task on the userDrivenTaskList.jsp. This class is designed
 * to start interactive task components that have a URL defined. This class
 * cannot be used for actions on interactive tasks without a URL defined.
 *
 * @see UserDrivenTaskListAction
 */


public final class CompleteUserDrivenTaskAction extends Action {
    
    
    // --------------------------------------------------------- Public Methods
    
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another Web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping ActionMapping used to select this instance.
     * @param actionForm Optional ActionForm bean for this request (if any).
     * @param request HTTP request being processed.
     * @param response HTTP response being created.
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
        
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources();
        
        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();
        
        HttpSession session  = request.getSession();
        User user            = (User) session.getAttribute(Constants.USER_KEY);
        WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);
        String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);
        
        TaskDef            taskdef       = null;
        AvailableTask      availabletask = null;
        Task               task          = null;
        String             tasktype      = null;
        String             taskURI       = null;
        ParameterItemsBean paramitems    = null;
        ParameterItemBean  paramitem     = null;
        
        if (servlet.getDebug() >= 1) {
            servlet.log("getMethod: " + request.getMethod());
            servlet.log("getPathInfo: " + request.getPathInfo());
            servlet.log("getQueryString: " + request.getQueryString());
            servlet.log("getRequestURI: " + request.getRequestURI());
            servlet.log("getRequestURL: " + request.getRequestURL());
        }
        
        // The parameter taskID holds the id of the User-Driven (Available) task to Complete
        String taskid = request.getParameter("taskID");
        
        // The task to complete can be manual or interactive.
        // The tasktype property of the Task's definition should be inspected for this.
        try {
            AvailableTaskList availabletasklist = new AvailableTaskList(w);
            TaskList tasklist                   = new TaskList(w);
            
            AvailableTaskFilter availfilter = new AvailableTaskFilter(AvailableTask.TASK_ID,
            Operator.STRING_EQUALS,
            taskid);
            availabletasklist.setFilter(availfilter);
            List availabletasks = availabletasklist.getTop();
            Iterator availabletaskiterator = availabletasks.iterator();
            // only one availabletask will be returned for a specific task_id
            availabletask     = (AvailableTask)availabletaskiterator.next();
            taskURI           = availabletask.getURI();
            
            // The task definition can be read with the task object method getTaskDef().
            task              = (Task)tasklist.getItemFromID(availabletask.getTaskID());
            taskdef           = task.getTaskDef();
            tasktype          = taskdef.getTaskType().toString();
        }
        catch (WorkflowException e) {
            if (servlet.getDebug() >= 1)
                servlet.log("CompleteUserDrivenTaskAction, WorkflowException gettask: " + e.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.fatal.gettask") );
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.consult") );
            saveErrors(request, errors);
            // show the error on the main menu
            return (new ActionForward(mapping.getInput()));
        }
        
        if (tasktype.equals("Manual")) {
            try {
                ParameterData params = task.getParameters();
                session.setAttribute("TaskParameter", params);
                return (mapping.findForward("fillparams"));
            }
            catch (WorkflowException e) {
                if (servlet.getDebug() >= 1)
                    servlet.log("CompleteUserDrivenTaskAction, WorkflowException gettask: " + e.getMessage());
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.fatal.gettask") );
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.consult") );
                saveErrors(request, errors);
                // show the error on the main menu
                return (new ActionForward(mapping.getInput()));
            }
        }
        
        if (tasktype.equals("Interactive")) {
            // Start of a new browser with the Interactive task, the tag target="_blank" in the link
            // of the jsp manages this.
            ActionForward forward = null;
            forward = new RedirectingActionForward();
            // The Workflow API Encoded the URI it is decoded here
            URLDecoder urldecoder = new URLDecoder();
            String taskuri = urldecoder.decode(taskURI);
            // Add the actorname to the uri as well, is needed to create a WorkflowConnector object
            // in the other (receiving) application.
            String taskuriapp = taskuri.concat("&ActorName=" + user.getUsername());
            forward.setPath(taskuriapp);
            return(forward);
        }
        
        // Forward control to the specified success URI, or error page
        errors.add(ActionErrors.GLOBAL_ERROR,
        new ActionError("error.fatal.invalidaction") );
        errors.add(ActionErrors.GLOBAL_ERROR,
        new ActionError("error.consult") );
        saveErrors(request, errors);
        // show the error on the main menu
        return (new ActionForward(mapping.getInput()));
        
    }
    
}
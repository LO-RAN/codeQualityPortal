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
 * This <strong>Action</strong> class is called when a link of a
 * Workflow-Driven task is clicked on the workflowDrivenTaskList.jsp. This
 * class processes the following actions:<br>
 * <ul>
 * <li><code>Execute</code></li>
 * <li><code>Abort</code></li>
 * <li><code>Reserve</code></li>
 * <li><code>Cancel Reservation</code></li>
 * </ul>
 * <br>
 * The OptimalFlow process model used by the sample application specifies the
 * OrderEntry application components using URLs. When the Execute action is
 * performed, the URL is retrieved from OptimalFlow and the related component
 * is started in a new browser window.
 * <br>
 * <strong>NOTE:</strong><br>
 * The getURI method of Task returns a URLEncoded URI. This class uses the
 * URLDecode.decode() method before forwarding the request.
 */

public final class ProcessWorkFlowDrivenTaskAction extends Action {


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

    TaskList           tasklist      = null;
	Task               task          = null;
	String             taskURI       = null;
	TaskDef            taskdef       = null;
	String             tasktype      = null;

    if (servlet.getDebug() >= 1) {
    	servlet.log("getMethod: " + request.getMethod());
    	servlet.log("getPathInfo: " + request.getPathInfo());
     	servlet.log("getQueryString: " + request.getQueryString());
     	servlet.log("getRequestURI: " + request.getRequestURI());
    	servlet.log("getRequestURL: " + request.getRequestURL());
	}

    // on input an action parameter is expected
    String action = null;
    action = request.getParameter("action");
    if (action == null) {
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.noaction") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    }

    // the task object is always needed
    try {
		tasklist = new TaskList(w);
        task = (Task)tasklist.getItemFromID(request.getParameter("taskID"));
        taskdef = (TaskDef)task.getTaskDef();
        tasktype = taskdef.getTaskType().toString();
        taskURI  = task.getURI();
	}
	catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("ProcessWorkFlowDrivenTaskAction, WorkflowException when retrieving the task: "
		                + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.gettask") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
	}

	// Always check first if we are dealing with an interactive task with a URL defined.
	if (tasktype.equals("Manual")) {
     	errors.add(ActionErrors.GLOBAL_ERROR,
    	           new ActionError("error.fatal.invalidaction.manual") );
     	errors.add(ActionErrors.GLOBAL_ERROR,
    	           new ActionError("error.fatal.invalidaction.manual1") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	           new ActionError("error.consult") );
        saveErrors(request, errors);
    	// show the error on the main menu
   	    return (new ActionForward(mapping.getInput()));
   	}

   	if (tasktype.equals("Interactive")) {
		if (taskURI.startsWith("?identifier")) {
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	           new ActionError("error.fatal.invalidaction.interactive.noURI") );
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	           new ActionError("error.consult") );
            saveErrors(request, errors);
         	// show the error on the main menu
      	    return (new ActionForward(mapping.getInput()));
		}
   	}

    if (action.equals("Abort")) {
		// the task needs to be aborted
		try {
		    task.abort();
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("ProcessWorkFlowDrivenTaskAction, WorkflowException on abort of task: "
		                + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.taskabort") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    	}

	}

    if (action.equals("Reserve")) {
		try {
		    task.reserve();
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("ProcessWorkFlowDrivenTaskAction, WorkflowException on reserve of task: "
		                + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.taskreserve") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    	}

	}

    if (action.equals("CancelReservation")) {
		try {
		    task.cancelReservation();
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("ProcessWorkFlowDrivenTaskAction WorkflowException occurred: " + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.taskcancelreservation") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    	}

	}

    if (action.equals("Execute")) {
		// The task needs to be executed, in the application interactive web components are used.
		// The URL is obtained from the task object and decoded using method URLDecoder.decode

        // To be added!
		// error checking .... is the task an interactive one, and has it an url defined
		try {
			// Get the URI
			taskURI = task.getURI();
			URLDecoder urldecoder = new URLDecoder();
			String taskuri = urldecoder.decode(taskURI);

			ActionForward forward = null;
			forward = new RedirectingActionForward();

			// add the actorname to the uri as well
			String taskuriapp = taskuri.concat("&ActorName=" + user.getUsername());
			if (servlet.getDebug() >= 1)
			    servlet.log("URI after concat of ActorName: " + taskuriapp);
			forward.setPath(taskuriapp);
			// start of a new browser with the Interactive task, the tag target="_blank" in the link
			// of the jsp manages this.
			return(forward);
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("ProcessWorkFlowDrivenTaskAction, WorkflowException on Execute of the task: "
		                + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.taskexecute") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    	}

	}

	// Forward control to the specified success URI
	return (mapping.findForward("showworkflowdriven"));

    }


}

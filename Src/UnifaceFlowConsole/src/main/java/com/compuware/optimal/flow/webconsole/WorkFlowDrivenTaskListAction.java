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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import com.compuware.optimal.flow.*;

/**
 * Implementation of <strong>Action</strong> class that prepares the data
 * needed to build up the Workflow-driven task list.
 *
 * The <strong>ActorTaskList</strong> object of the Workflow Connector is used
 * to retrieve the information about the workflow-driven tasks assigned to the
 * logged on Actor. The information about each retrieved task is stored in the
 * <strong>WorkFlowItemBean</strong> and each WorkFlowItemBean is added to the
 * <strong>WorkFlowItemsBean</strong> object.
 * <br>
 * The workFlowDrivenTaskList.jsp can iterate through the WorkFlowItemsBean and
 * display the properties of each WorkFlowItemBean.
 * <br>
 * The JSP checks the state property of the WorkFlowItemBean
 * and, depending on its value, allows one or more actions to be performed on
 * the task.
 *
 * @see WorkFlowItemBean
 * @see WorkFlowItemsBean
 * @see ProcessTaskAction
 */

public final class WorkFlowDrivenTaskListAction extends Action {


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
	String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);
	WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);

    // Synchronize on the WorkflowConnector to prevent that deleteInstances influences the actions
    // of the ProcessTaskAction class.
	synchronized(w) {

	// Define the Workflow API lists that are saved in request context
	ActorTaskList actortasklist = null;

	// Define Workflow API objects needed
	Task task  = null;
	TaskDef taskdef = null;
	List tasks = null;
	Iterator taskiterator = null;

    // This action retrieves the tasks assigned to the user that logged on
    try {
		actortasklist              = new ActorTaskList(w);
		ActorList actorlist        = new ActorList(w);
		// The Actor is needed, the id of the actor wlli be set as identifier on the actortasklist object.
		Actor actor                = actorlist.getActorFromName(user.getUsername());
		// the identifier of the Actor is set on the list
		actortasklist.setIdentifier(actor.getID());
		// A list of task objects is returned.
		tasks = actortasklist.getTop();
	}
	catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("WorkFlowDrivenTaskListAction WorkflowException occurred: " + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.retrievetasks") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
	}

	// For each task a bean is created that holds the task data.
	// All the beans are stored as an ArrayList in the WorkFlowItemsBean object.
    taskiterator = tasks.iterator();

    WorkFlowItemsBean workflowitemsbean = new WorkFlowItemsBean();
	try {
        while (taskiterator.hasNext()) {
    		task = (Task)taskiterator.next();
    		WorkFlowItemBean workflowitembean = new WorkFlowItemBean();
    		taskdef = task.getTaskDef();
		    workflowitembean.setName(task.getName());
		    workflowitembean.setId(task.getID());
		    workflowitembean.setState(task.getState().toString());
		    workflowitembean.setType(taskdef.getTaskType().toString());
		    workflowitembean.setPriority(task.getPriority());
		    workflowitembean.setVar1Label(task.getVar1Label());
		    workflowitembean.setVar1(task.getVar1());
		    workflowitembean.setVar2Label(task.getVar2Label());
		    workflowitembean.setVar2(task.getVar2());
		    workflowitembean.setVar3Label(task.getVar3Label());
		    workflowitembean.setVar3(task.getVar3());
        	workflowitemsbean.add(workflowitembean);
		}
    	// Free up resources, no Workflow Objects needed in JSP view.
		w.deleteInstances();
	}

    catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("WorkFlowDrivenTaskListAction, WorkFlowException when creating beaninfo: "
		                + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.retrievetasks") );
       	errors.add(ActionErrors.GLOBAL_ERROR,
       	    	   new ActionError("error.consult") );
       	saveErrors(request, errors);
       	// show the error on the main menu
   	    return (new ActionForward(mapping.getInput()));
	}

	// Save the reference to the workflowitemsbean in request scope.
	request.setAttribute(Constants.WORKFLOWDRIVEN_KEY, workflowitemsbean);

	// Forward control to the specified success URI
	return (mapping.findForward("success"));

    }
    }

}

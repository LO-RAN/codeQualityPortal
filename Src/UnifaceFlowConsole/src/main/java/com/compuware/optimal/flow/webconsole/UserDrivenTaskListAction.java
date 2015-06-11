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
 * This <strong>Action</strong> class reads all the User-Driven tasks that the
 * current user is allowed to start.
 *
 * User-Driven tasks are retrieved by using the methods of the Workflow
 * Connector <strong>AvailableTaskList</strong> class. A filter is set on the
 * actor ID of the current user. For each <strong>AvailableTask</strong>, a
 * <strong>UserDrivenItemBean</strong> is created and the properties are
 * populated with some properties of the AvailableTask and with the TaskType
 * property of the <strong>TaskDef</strong> object. The object
 * <strong>UserDrivenItemsBean</strong> then is stored in request scope.
 * The getTasks method is used in the JSP userDrivenTaskList.jsp. The iterate
 * tag within the JSP uses tasks as a property of the UserDrivenItemsBean. The
 * method <code>getTasks()</code> of the UserDrivenItemsBean returns a List of
 * UserDrivenItemBean's.
 *
 * @see UserDrivenItemBean
 * @see UserDrivenItemsBean
 * @see ProcessTaskAction
 */

public final class UserDrivenTaskListAction extends Action {


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

	// Extract attributes needed
	Locale locale = getLocale(request);
	MessageResources messages = getResources();

	// ActionErrors needed for error passing
	ActionErrors errors = new ActionErrors();

	HttpSession session  = request.getSession();
	User user            = (User) session.getAttribute(Constants.USER_KEY);
	WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);
	String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);

	// Define the Workflow API lists that will be saved in request scope
	AvailableTaskList userdriventasklist = null;
	List udtasks = null;
	Iterator udtasksiterator;
	AvailableTask udtask      = null;
	TaskDefList   taskdeflist = null;
	TaskDef       taskdef     = null;

	UserDrivenItemsBean userdrivenitemsbean = null;

    // This action retrieves the User-Driven tasks for the logged on user
    try {
		userdriventasklist              = new AvailableTaskList(w);
		ActorList actorlist             = new ActorList(w);
		Actor actor                     = actorlist.getActorFromName(user.getUsername());
		AvailableTaskFilter availfilter = new AvailableTaskFilter(AvailableTask.ACTOR_ID,
		                                                          Operator.STRING_EQUALS,
		                                                          actor.getID());
		userdriventasklist.setFilter(availfilter);
		udtasks = userdriventasklist.getTop();
		if (servlet.getDebug() >= 1) {
		    servlet.log("Number of Available tasks returned: " + userdriventasklist.getNumber());
		}

    	// Iterate through the available tasks and for each available task get its TaskType from the
    	// definition.
    	taskdeflist         = new TaskDefList(w);
    	udtasksiterator     = udtasks.iterator();
        userdrivenitemsbean = new UserDrivenItemsBean();
    	while (udtasksiterator.hasNext()) {
    		udtask  = (AvailableTask)udtasksiterator.next();
    		taskdef = (TaskDef)taskdeflist.getItemFromID(udtask.getDefinitionID());
    		UserDrivenItemBean userdrivenitembean = new UserDrivenItemBean();
    		userdrivenitembean.setId(udtask.getID());
    		userdrivenitembean.setName(udtask.getName());
    		userdrivenitembean.setReleaseName(udtask.getReleaseName());
    		userdrivenitembean.setReleaseState(udtask.getReleaseState());
    		userdrivenitembean.setReleaseVersion(udtask.getReleaseVersion());
    		userdrivenitembean.setTaskId(udtask.getTaskID());
    		userdrivenitembean.setTaskType(taskdef.getTaskType().toString());
    		// add the userdrivenitembean to the collection of beans maintained in userdrivenitemsbean
    		userdrivenitemsbean.add(userdrivenitembean);
		}
    	// Free up resources, no Workflow Objects needed in JSP view.
    	w.deleteInstances();
	}

	catch (WorkflowException e) {
		if (servlet.getDebug() >= 1) {
		    servlet.log("UserDrivenTaskListAction WorkflowException occurred: " + e.getMessage());
		}
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.retrievetasks") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
	}

	// Save the reference to the userdrivenitemsbean in request scope.
	request.setAttribute(Constants.USERDRIVEN_KEY, userdrivenitemsbean);

	// Forward control to the specified success URI
	return (mapping.findForward("success"));

    }

}

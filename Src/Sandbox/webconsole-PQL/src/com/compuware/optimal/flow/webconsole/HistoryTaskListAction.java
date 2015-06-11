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
 * Implementation of <strong>Action</strong> class that prepares the data needed to
 * build up a History task list.
 *
 * The step size, selected filter option, filter value, and selected sort
 * option of the ActionForm are set here for the TaskList object. This is done
 * the first time this action is called or after a reset.
 * If the incoming action is <code>Top</code> or <code>Bottom</code> the
 * Position of the TaskList is saved in session scope. If the action is
 * <code>Top</code>, <code>Next</code>, <code>Previous</code>, or
 * <code>Bottom</code>, the Position that was saved before is set on the task
 * list. The tasks are then retrieved using one of the methods
 * <code>getTop</code>, <code>getNext</code>, <code>getPrevious</code>, or
 * <code>getBottom</code>.
 */

public final class HistoryTaskListAction extends Action {


    // --------------------------------------------------------- Public Methods


    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response.
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

	TaskList   tasklist   = null;
	TaskFilter taskfilter = null;
	TaskOrder  taskorder  = null;
	List       tasks      = null;
	ActorList  actorlist  = null;
	Actor      actor      = null;
	Position   position   = null;

    // First time, action will be null forward to success, which will
    // display the jsp.
    String action = request.getParameter("action");
    if (action == null)
    	return (mapping.findForward("success"));

    // When an action is present it should be Top, Next, Previous, Bottom or Reset.
    // All actions need the previous Position and Stepsize that was saved.

    // Get the form fields.
	String filterselect = ((HistoryTaskListForm) form).getFilterselect();
	String filtervalue  = ((HistoryTaskListForm) form).getFiltervalue();
	String sortselect   = ((HistoryTaskListForm) form).getSortselect();
	String stepsize     = ((HistoryTaskListForm) form).getStepsize();

    if (action.equals("Reset")) {
		session.removeAttribute(Constants.POSITION_KEY);
        form.reset(mapping, request);
    	return (mapping.findForward("success"));
	}

    // The Actor is always needed
    try {
		actorlist = new ActorList(w);
		// The Actor is needed, the id of the actor is set on the filter field PROCESSOR.
		actor = actorlist.getActorFromName(user.getUsername());
	}
   	catch (WorkflowException e) {
   		if (servlet.getDebug() >= 1)
   		    servlet.log("HistoryTaskListAction WorkflowException occurred: " + e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
       	           new ActionError("error.fatal.retrievetasks") );
       	errors.add(ActionErrors.GLOBAL_ERROR,
       	    	   new ActionError("error.consult") );
       	saveErrors(request, errors);
       	// show the error on the main menu
   	    return (new ActionForward(mapping.getInput()));
	}


    // The position was saved in session scope or null
    position = (Position)session.getAttribute(Constants.POSITION_KEY);
    if (servlet.getDebug() >= 1)
        servlet.log("Position retrieved from session: " + position);

    // If position is empty, create the tasklist and set the Filter, Ordering
    // and stepsize.

    if (position == null) {
        try {
    		tasklist = new TaskList(w);
    		tasklist.setStepSize( Integer.parseInt(stepsize) );
    		// In this application the history tasks processed by the actor
    		// that is logged on are displayed.
     		taskfilter = new TaskFilter(Task.PROCESSOR, Operator.STRING_EQUALS,
    		                            actor.getID());
    		// Check if a filter option was selected
    		if (filterselect.equals("nameequals"))
    		    taskfilter.add(Task.NAME, Operator.STRING_EQUALS, filtervalue);
    		if (filterselect.equals("stateequals"))
    		    taskfilter.add(Task.STATE, Operator.TOKEN_EQUALS, new Token(filtervalue));
    		// Set the filter on the tasklist
    		tasklist.setFilter(taskfilter);
    		// Check if a sort option was selected
    		if (sortselect.equals("nameasc"))
    		    taskorder = new TaskOrder(Task.NAME);
    		if (sortselect.equals("namedesc"))
    		    taskorder = new TaskOrder(Task.NAME, false);
    		if (sortselect.equals("stateasc"))
    		    taskorder = new TaskOrder(Task.STATE);
    		if (sortselect.equals("statedesc"))
    		    taskorder = new TaskOrder(Task.STATE, false);
    		if (!sortselect.equals("none"))
    		    tasklist.setOrder(taskorder);
    	}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1)
    		    servlet.log("HistoryTaskListAction WorkflowException occurred: " + e.getMessage());
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	           new ActionError("error.fatal.retrievetasks") );
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	    	   new ActionError("error.consult") );
        	saveErrors(request, errors);
        	// show the error on the main menu
    	    return (new ActionForward(mapping.getInput()));
    	}
	}
    else {
		// set the postion on the tasklist
        try {
    		tasklist = new TaskList(w);
    		tasklist.setPosition(position);
    	}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1)
    		    servlet.log("HistoryTaskListAction WorkflowException occurred: " + e.getMessage());
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	           new ActionError("error.fatal.retrievetasks") );
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	    	   new ActionError("error.consult") );
        	saveErrors(request, errors);
        	// show the error on the main menu
    	    return (new ActionForward(mapping.getInput()));
    	}
	}

    if (action.equals("Top")) {
        try {
            tasks = tasklist.getTop();
		}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1)
    		    servlet.log("HistoryTaskListAction(Top) WorkflowException occurred: " + e.getMessage());
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	           new ActionError("error.fatal.retrievetasks") );
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	    	   new ActionError("error.consult") );
           	saveErrors(request, errors);
         	// show the error on the main menu
    	    return (new ActionForward(mapping.getInput()));
    	}
	}


    // Next
    if (action.equals("Next")) {
        try {
			tasks = tasklist.getNext();
		}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1)
    		    servlet.log("HistoryTaskListAction(Next) WorkflowException occurred: " + e.getMessage());
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	           new ActionError("error.fatal.retrievetasks") );
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	    	   new ActionError("error.consult") );
           	saveErrors(request, errors);
         	// show the error on the main menu
    	    return (new ActionForward(mapping.getInput()));
    	}
	}


    // Previous
    if (action.equals("Previous")) {
        try {
			tasks = tasklist.getPrevious();
		}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1)
    		    servlet.log("HistoryTaskListAction(Previous) WorkflowException occurred: " + e.getMessage());
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	           new ActionError("error.fatal.retrievetasks") );
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	    	   new ActionError("error.consult") );
           	saveErrors(request, errors);
         	// show the error on the main menu
    	    return (new ActionForward(mapping.getInput()));
    	}
	}


    // Bottom
    if (action.equals("Bottom")) {
        try {
			tasks = tasklist.getBottom();
		}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1)
    		    servlet.log("HistoryTaskListAction(Bottom) WorkflowException occurred: " + e.getMessage());
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	           new ActionError("error.fatal.retrievetasks") );
           	errors.add(ActionErrors.GLOBAL_ERROR,
           	    	   new ActionError("error.consult") );
           	saveErrors(request, errors);
         	// show the error on the main menu
    	    return (new ActionForward(mapping.getInput()));
    	}
	}


	// Save the position of the tasklist in session scope.
	try {
		position = tasklist.getPosition();
	    }
    catch (WorkflowException e) {
    	if (servlet.getDebug() >= 1)
    	    servlet.log("HistoryTaskListAction(getPosition) WorkflowException occurred: " +
    	                e.getMessage());
       	errors.add(ActionErrors.GLOBAL_ERROR,
       	           new ActionError("error.fatal.retrievetasks") );
       	errors.add(ActionErrors.GLOBAL_ERROR,
       	    	   new ActionError("error.consult") );
       	saveErrors(request, errors);
      	// show the error on the main menu
        return (new ActionForward(mapping.getInput()));
    }
	session.setAttribute(Constants.POSITION_KEY, position);

	// Save the reference to the tasks in request scope.
	request.setAttribute(Constants.HISTORY_KEY, tasks);

	// Forward control to the specified success URI
	return (mapping.findForward("success"));

    }

}

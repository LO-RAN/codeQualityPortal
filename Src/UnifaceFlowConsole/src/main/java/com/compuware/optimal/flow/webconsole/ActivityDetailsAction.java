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

import com.compuware.optimal.flow.Package;
import com.compuware.optimal.flow.Process;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * This <strong>Action</strong> class collects activity definition and instance
 * data from a specified activity id.
 * <br>
 * On input, the <code>action</code> parameter is read. If the parameter exists
 * and it contains the value <code>ShowActivityDetails</code>, the data of the selected
 * activity is retrieved.
 * <br>
 * The HTML document packageProcessInstances.html is the main
 * document and is started after choosing the option Process activities from
 * the main menu (mainMenu.jsp). The activity details are displayed by the JSP
 * showDetails.jsp.
 *
 */

public final class ActivityDetailsAction extends Action {


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
	String connectionCfg = (String) session.getAttribute(Constants.CONNECTIONCONFIG_KEY);
	WorkflowConnector w  = (WorkflowConnector) session.getAttribute(Constants.CONNECTOR_KEY);

    String action = null;
    action = request.getParameter("action");

    if (servlet.getDebug() >= 1 ) {
        servlet.log("ActivityInstanceListAction action = " + action);
 	    servlet.log("getMethod: " + request.getMethod());
	    servlet.log("getPathInfo: " + request.getPathInfo());
	    servlet.log("getQueryString: " + request.getQueryString());
	    servlet.log("getRequestURI: " + request.getRequestURI());
	    servlet.log("getRequestURL: " + request.getRequestURL());
	}

    if (action.equals("ShowActivityDetails")) {

		String         activityID     = null;
		String         classification = null;
		String         defID          = null;
		String         actorID        = null;
		Actor		   processor      = null;
		Activity       activity       = null;
		ActivityDef    activityDef    = null;
		TaskDef        taskDef        = null;
		RecurrenceComponent recurrenceComponent = null;

        //Extract parameters from request
		activityID = request.getParameter("activityID");
		classification = request.getParameter("classification");
		defID = request.getParameter("defID");

	    // Read the properties of the Activity and of Task, FlowControl or Process definition
        try {
		    if ((activityID != null) && !(activityID.equals("")))  {
			  activity = getActivity(activityID, w);
			  if (classification.equals("Task")) {
				if (activity.getState().equals("Processed")) {
				  // Obtain the actor who performed the task.
				  // First we have to re-obtain the Task from the Activity
			      TaskList taskList = new TaskList(w);
			      Task t = (Task)taskList.getItemFromID(activityID);
				  actorID = t.getProcessor();

				  if ((actorID != null) && !(actorID.equals(""))) {
					  ActorList al = new ActorList(w);
					  processor = (Actor)al.getItemFromID(actorID);
				  }
			    }
			  }
		    }
			if (classification.equals("Task") || classification.equals("Recurrence")) {
			  TaskDefList taskDefList = new TaskDefList(w);
			  activityDef = (ActivityDef)taskDefList.getItemFromID(defID);
		    } else if (classification.equals("FlowControl")) {
			  FlowControlDefList flowControlDefList	= new FlowControlDefList(w);
			  activityDef = (ActivityDef)flowControlDefList.getItemFromID(defID);
		    } else if (classification.equals("Process")) {
			  ProcessDefList processDefList= new ProcessDefList(w);
			  activityDef = (ActivityDef)processDefList.getItemFromID(defID);
		    }

		    // For Classification Recurrence extra properties from RecurrenceComponent are displayed.
		    if (classification.equals("Recurrence")) {
		      taskDef = (TaskDef)activityDef;
		      recurrenceComponent = taskDef.getRecurrenceComponent();
		    }

	    }
		catch (WorkflowException e) {
            if (servlet.getDebug() >= 1) {
         		servlet.log("ActivityDetailsAction, WorkflowException on get activity: "
        		            + e.getMessage());
     		}
			errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.fatal.retrieve activity") );
            errors.add(ActionErrors.GLOBAL_ERROR,
               	   	   new ActionError("error.consult") );
            saveErrors(request, errors);
            // show the error on the main menu
            return (mapping.findForward("failure"));
    	}

		// The reference to the activity and activity definition details is saved.
		if (activity != null) {
    		request.setAttribute(Constants.ACTIVITY_KEY, activity);
		}
		if (activityDef != null) {
			request.setAttribute(Constants.ACTIVITYDEF_KEY, activityDef);
		}
		if (processor != null) {
			request.setAttribute(Constants.PROCESSOR_KEY, processor);
		}
		if (recurrenceComponent != null) {
			request.setAttribute(Constants.RECURRENCE_COMPONENT_KEY, recurrenceComponent);
		}

		// Forward control to the specified success URI
		return (mapping.findForward("showactivitydetails"));

    }

    // program flow should never reach this point...
    errors.add(ActionErrors.GLOBAL_ERROR,
      	       new ActionError("error.fatal.programflow") );
    errors.add(ActionErrors.GLOBAL_ERROR,
      	   	   new ActionError("error.consult") );
    saveErrors(request, errors);
    return (mapping.findForward("failure"));

    }


    // --------------------------------------------------------- Private Methods

    /**
     * Obtain an activity object based on its id.
     * The reference to this activity can be used by the JSP to display the process
     * details.
     */

    private Activity getActivity(String id, WorkflowConnector w)
    throws WorkflowException {

        ActivityList activitylist = null;
        Activity     activity     = null;

        activitylist = new ActivityList(w);
        activity     = (Activity)activitylist.getItemFromID(id);

        return(Activity)activity;

	}

}

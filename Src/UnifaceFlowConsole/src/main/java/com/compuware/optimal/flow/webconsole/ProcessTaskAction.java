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
import java.lang.Boolean;
import java.lang.Double;
import java.lang.String;
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
 * User-driven or Workflow-Driven task is clicked on the userDrivenTaskList.jsp or
 * workflowDrivenTaskList.jsp.
 * This class processes the following actions for a User-driven task:<br>
 * <ul>
 * <li><code>Complete</code></li>
 * </ul><br>
 * This class processes the following actions for a Workflow-driven task:<br>
 * <ul>
 * <li><code>Execute</code></li>
 * <li><code>Abort</code></li>
 * <li><code>Reserve</code></li>
 * <li><code>Cancel Reservation</code></li>
 * </ul>
 * <br>
 * When a Execute or Complete action is performed on an interactive task, a check will
 * be done if a correct URL is modeled. This class cannot be used for actions on
 * interactive tasks without a URL defined.
 * The OptimalFlow process model used by the sample application specifies the
 * OrderEntry application components using URLs. When the Execute action of Workflow-driven
 * task or Complete action of a User-driven task is performed, the URL is retrieved
 * from OptimalFlow and the related component is started in a new browser window.
 * <br>
 * <strong>NOTE:</strong><br>
 * The getURI method of Task returns a URLEncoded URI. This class uses the
 * URLDecode.decode() method before forwarding the request.
 * <br>
 * For a manual task the parameters of the task are read and prepared to
 * be listed by the JSP showManualUserDriven.jsp. Next this JSP will start the class
 * <strong>CompleteManualTaskAction</strong> when pressing the <code>Complete</code>
 * button.
 *
 * @see UserDrivenTaskListAction
 * @see WorkFlowDrivenTaskListAction
 * @see CompleteManualTaskAction
 */


public final class ProcessTaskAction extends Action {


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

    // Synchronize on the WorkflowConnector to prevent that deleteInstances of the
    // WorkflowDrivenTaskListAction class is of influence.
	synchronized(w) {

	TaskDef            taskdef       = null;
	AvailableTask      availabletask = null;
	Task               task          = null;
	String             tasktype      = null;
	boolean            isUserDriven  = false;
	String             taskURI       = null;
	String             taskname      = null;
	ParameterData      paramdata     = null;
	ParameterItemsBean paramitems    = null;
	ParameterItemBean  paramitem     = null;
	Set                inputs, outputs;
	Iterator           inputsit, outputsit;

    if (servlet.getDebug() >= 1) {
 	    servlet.log("getMethod: " + request.getMethod());
	    servlet.log("getPathInfo: " + request.getPathInfo());
	    servlet.log("getQueryString: " + request.getQueryString());
	    servlet.log("getRequestURI: " + request.getRequestURI());
	    servlet.log("getRequestURL: " + request.getRequestURL());
        servlet.log("list getParameterNames and values");
    	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
    		String s = (String)e.nextElement();
    		String v = request.getParameter(s);
    	    servlet.log("Parameter: " + s + " = " + v);
		}
	}

    // on input a process and action parameter are expected
    String process = null;
    String action = null;
    process = request.getParameter("process");
    action = request.getParameter("action");
    if (process == null || action == null) {
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.noaction") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    }

    // When the process and action parameters are there, check there
    // values.
    if (process.equals("UserDriven")) {
		if ( !(action.equals("Complete")) ) {
    	    // Forward control to the error page
     	    errors.add(ActionErrors.GLOBAL_ERROR,
     	               new ActionError("error.fatal.invaliduserdrivenaction") );
    	    errors.add(ActionErrors.GLOBAL_ERROR,
    	               new ActionError("error.consult") );
            saveErrors(request, errors);
     	    // show the error on the main menu
            return (new ActionForward(mapping.getInput()));
		}
	}
	else {
		if (process.equals("WorkflowDriven")) {
			if ( !(action.equals("Abort") || action.equals("Cancel") ||
			       action.equals("CancelReservation") || action.equals("Complete") ||
			       action.equals("Execute") || action.equals("Reserve")) ) {
                	   // Forward control to the error page
                	   errors.add(ActionErrors.GLOBAL_ERROR,
     	                          new ActionError("error.fatal.invalidworkflowdrivenaction") );
    	               errors.add(ActionErrors.GLOBAL_ERROR,
    	                          new ActionError("error.consult") );
                       saveErrors(request, errors);
     	               // show the error on the main menu
                       return (new ActionForward(mapping.getInput()));
			}
		}
		else {
       	   // Forward control to the error page
      	   errors.add(ActionErrors.GLOBAL_ERROR,
                      new ActionError("error.fatal.invalidprocessing") );
           errors.add(ActionErrors.GLOBAL_ERROR,
                      new ActionError("error.consult") );
           saveErrors(request, errors);
           // show the error on the main menu
           return (new ActionForward(mapping.getInput()));
	   }
	}

    // The parameter taskID holds the id of the task to process.
    String taskid = request.getParameter("taskID");

    // The task to process can userdriven or workflowdriven,
    // the process parameter thas was saved in request scope can be inspected for this.
    // Also an action parameter is passed that contains the action to perform.
    // If the action is Complete or Execute the task to complete/execute can be manual or interactive,
    // the tasktype property of the Task's definition is inspected for this.
    if (process.equals("UserDriven")) {
        try {
         	AvailableTaskList availabletasklist = new AvailableTaskList(w);
        	TaskList          tasklist          = new TaskList(w);

        	AvailableTaskFilter availfilter = new AvailableTaskFilter(AvailableTask.TASK_ID,
    	                                                              Operator.STRING_EQUALS,
    	                                                              taskid);
    	    availabletasklist.setFilter(availfilter);
    	    List availabletasks = availabletasklist.getTop();
        	Iterator availabletaskiterator = availabletasks.iterator();
        	// only one availabletask will be returned for a specific task_id
        	availabletask = (AvailableTask)availabletaskiterator.next();
        	taskURI       = availabletask.getURI();
        	taskname      = availabletask.getName();

            // The task definition can be read with the task object method getTaskDef().
        	task         = (Task)tasklist.getItemFromID(availabletask.getTaskID());
            taskdef      = task.getTaskDef();
            tasktype     = taskdef.getTaskType().toString();
            isUserDriven = taskdef.getIsUserDriven();
        }

        catch (WorkflowException e) {
     		if (servlet.getDebug() >= 1) {
        	    servlet.log("ProcessTaskAction, WorkflowException when retrieving the user-driven task: "
        	                + e.getMessage());
			}
    	    errors.add(ActionErrors.GLOBAL_ERROR,
    	    	       new ActionError("error.fatal.gettask") );
    	    errors.add(ActionErrors.GLOBAL_ERROR,
    	    	       new ActionError("error.consult") );
    	    saveErrors(request, errors);
            // show the error on the main menu
       	    return (new ActionForward(mapping.getInput()));
        }
	}

    if (process.equals("WorkflowDriven")) {
        try {
    		TaskList tasklist = new TaskList(w);
            task = (Task)tasklist.getItemFromID(request.getParameter("taskID"));
            taskname     = task.getName();
            taskdef      = (TaskDef)task.getTaskDef();
            tasktype     = taskdef.getTaskType().toString();
            isUserDriven = taskdef.getIsUserDriven();
            taskURI      = task.getURI();

    	}
    	catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1) {
		        servlet.log("ProcessTaskAction, WorkflowException when retrieving the workflow-driven task: "
		                + e.getMessage());
			}
       	    errors.add(ActionErrors.GLOBAL_ERROR,
     	               new ActionError("error.fatal.gettask") );
    	    errors.add(ActionErrors.GLOBAL_ERROR,
    	        	   new ActionError("error.consult") );
     	    saveErrors(request, errors);
    	    // show the error on the main menu
	        return (new ActionForward(mapping.getInput()));
	    }
	}

    if ((action.equals("Complete") || action.equals("Execute")) && (tasktype.equals("Manual"))) {
   		// Read the parameters of the Manual task.
		String s = new String();
		Boolean b = new Boolean(false);
		Double d = new Double(0);
   		paramitems = new ParameterItemsBean();
        try {
   			paramdata = task.getParameters();
   			// put each parameter in a ParameterBean
   			// first only the Input Parameters
   			inputs   = paramdata.getInputs();
   			inputsit = inputs.iterator();
   			String p;
   			while (inputsit.hasNext()) {
   				p = (String)inputsit.next();
   				// Check if input only
   				if (!paramdata.isOutput(p)) {
   					paramitem = new ParameterItemBean();
   					paramitem.setName(p);
   					paramitem.setInputOnly("true");
   					paramitem.setValue(paramdata.getValue(p).toString());

   					java.lang.Class c = paramdata.getType(p);
   					if (c.isAssignableFrom(s.getClass())) {
						paramitem.setDataType("String");
					}
					else {
						if (c.isAssignableFrom(b.getClass())) {
							paramitem.setDataType("Boolean");
						}
						else {
							if (c.isAssignableFrom(d.getClass())); {
								paramitem.setDataType("Double");
							}
						}
					}
   					paramitems.add(paramitem);
   				}
   			}
   			// get the output and input/output parameters
   			outputs   = paramdata.getOutputs();
   			outputsit = outputs.iterator();
   			while (outputsit.hasNext()) {
   				p = (String)outputsit.next();
   				paramitem = new ParameterItemBean();
   				paramitem.setName(p);
				paramitem.setInputOnly("false");

				java.lang.Class c = paramdata.getType(p);
   				if (c.isAssignableFrom(s.getClass())) {
					paramitem.setDataType("String");
				}
				else {
					if (c.isAssignableFrom(b.getClass())) {
						paramitem.setDataType("Boolean");
					}
					else {
						if (c.isAssignableFrom(d.getClass())); {
							paramitem.setDataType("Double");
						}
					}
				}

   				paramitem.setValue(paramdata.getValue(p).toString());
   				paramitems.add(paramitem);
   			}

   			// For WorkflowDriven tasks a begin method is needed on task, this
   			// will set its state to Executing.
   			if (!isUserDriven ) {
				task.begin();
			}

   		}

   		catch (WorkflowException e) {
    		if (servlet.getDebug() >= 1) {
		        servlet.log("ProcessTaskAction, WorkflowException when completing/executing the manual task: "
		                + e.getMessage());
			}
   		   	errors.add(ActionErrors.GLOBAL_ERROR,
       	               new ActionError("error.fatal.begincomplete.manual") );
       	    errors.add(ActionErrors.GLOBAL_ERROR,
       	               new ActionError("error.consult") );
            saveErrors(request, errors);
       	    // show the error on the main menu
   	        return (new ActionForward(mapping.getInput()));
   		}

   		// Save the paramitems in session scope and forward.
   		session.setAttribute(Constants.PARAMETERITEMS_KEY, paramitems);
   		session.setAttribute(Constants.TASK_KEY, taskid);
   		session.setAttribute(Constants.TASKNAME_KEY, taskname);
   		return (mapping.findForward("showmanualtask"));

   	}

  	if ((action.equals("Complete") || action.equals("Execute")) && (tasktype.equals("Interactive"))) {
		if (taskURI.startsWith("?identifier")) {
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	           new ActionError("error.fatal.invalidaction.interactive.noURI") );
        	errors.add(ActionErrors.GLOBAL_ERROR,
        	           new ActionError("error.consult") );
            saveErrors(request, errors);
         	// show the error on the main menu
      	    return (new ActionForward(mapping.getInput()));
		}
		// In the sample application interactive web components are used.
		// The URL is obtained from the task object and decoded using method URLDecoder.decode

       	ActionForward forward = null;
       	forward = new RedirectingActionForward();

   		// The Workflow API Encoded the URI, it is decoded here
   		URLDecoder urldecoder = new URLDecoder();

        // javac 1.3
   		// ---> comment the following lines when using javac 1.4 <---
   		String taskuri = urldecoder.decode(taskURI);
   		taskuri = new String(taskuri.getBytes("UTF-8"), "UTF-8");

   		// javac 1.4
   		// ---> uncomment the following line when using javac 1.4 <---
   		//String taskuri = urldecoder.decode(taskURI, "UTF-8");

   		// Add the actorname to the uri as well, is needed to create a WorkflowConnector object
   		// in the other (receiving) application.
   		String taskuriapp = taskuri.concat("&ActorName=" + user.getUsername());
		if (servlet.getDebug() >= 1) {
		    servlet.log("URI after concat of ActorName: " + taskuriapp);
		}
   		forward.setPath(taskuriapp);
       	// Start of a new browser with the Interactive task, the tag target="_blank" in the link
       	// of the jsp manages this.
   		return(forward);
   	}

    if (process.equals("WorkflowDriven") && action.equals("Abort")) {
		// the task needs to be aborted
		try {
		    task.abort();
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1)
		    servlet.log("ProcessTaskAction, WorkflowException on abort of task: "
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

    if (process.equals("WorkflowDriven") && action.equals("Reserve")) {
		try {
		    task.reserve();
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1) {
		    servlet.log("ProcessTaskAction, WorkflowException on reserve of task: "
		                + e.getMessage());
		}
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.taskreserve") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    	}

	}

    if (process.equals("WorkflowDriven") && action.equals("CancelReservation")) {
		try {
		    task.cancelReservation();
		}
		catch (WorkflowException e) {
		if (servlet.getDebug() >= 1) {
		    servlet.log("ProcessTaskAction WorkflowException occurred: " + e.getMessage());
		}
       	errors.add(ActionErrors.GLOBAL_ERROR,
     	           new ActionError("error.fatal.taskcancelreservation") );
    	errors.add(ActionErrors.GLOBAL_ERROR,
    	    	   new ActionError("error.consult") );
     	saveErrors(request, errors);
    	// show the error on the main menu
	    return (new ActionForward(mapping.getInput()));
    	}

	}

    if (process.equals("WorkflowDriven") && action.equals("Cancel")) {
		try {
			if (!isUserDriven) {
		        task.cancel();
			}
		}
		catch (WorkflowException e) {
		    if (servlet.getDebug() >= 1) {
		        servlet.log("ProcessTaskAction WorkflowException occurred: " + e.getMessage());
		    }
       	    errors.add(ActionErrors.GLOBAL_ERROR,
     	               new ActionError("error.fatal.taskcancel") );
    	    errors.add(ActionErrors.GLOBAL_ERROR,
    	        	   new ActionError("error.consult") );
     	    saveErrors(request, errors);
    	    // show the error on the main menu
	        return (new ActionForward(mapping.getInput()));
    	}

    	session.removeAttribute(Constants.TASK_KEY);
		session.removeAttribute(Constants.TASKNAME_KEY);
		session.removeAttribute(Constants.PARAMETERITEMS_KEY);

	}

	// Forward control to the specified success URI
	if (isUserDriven) {
		return (mapping.findForward("showuserdriven"));
	}
	else {
    	return (mapping.findForward("showworkflowdriven"));
	}

    }
    }

}

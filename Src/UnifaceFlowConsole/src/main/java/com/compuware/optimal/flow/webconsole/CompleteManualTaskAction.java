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
import java.util.Locale;
import java.util.List;
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
 * This <strong>Action</strong> class is called when completing a manual task.
 * The JSP showManualTask.jsp has displayed the User-driven or Workflow-driven
 * manual task. When the Complete button is clicked control is forwarded to this
 * Action class. This class will read the parameters passed and complete the task.
 *
 * @see UserDrivenTaskListAction
 * @see WorkFlowDrivenTaskListAction
 */


public final class CompleteManualTaskAction extends Action {


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

	Task               task          = null;
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
	}

    if (servlet.getDebug() >= 1) {
    	servlet.log("list getParameterNames and values");
    	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
    		String s = (String)e.nextElement();
    		String v = request.getParameter(s);
    	    servlet.log("Parameter: " + s + " = " + v);
		}
	}

    // The parameter taskID holds the id of the task to Complete
    String taskid = request.getParameter("taskID");
    String action = request.getParameter("action");

	if (action.equals("Complete")) {
		// read the task to be completed
		try {
			TaskList tasklist = new TaskList(w);
			task = (Task)tasklist.getItemFromID(taskid);
			// read the parameters belonging to the task
			paramdata = task.getParameters();

			// First build the ParameterItemsBean again, this time the InOut/Out
			// parameters are saved with the values that come from the parameters that
			// were passed on to this action class.

       		// Read the parameters of the Manual task.
	  		String s = new String();
	  		Boolean b = new Boolean(false);
	  		Double d = new Double(0);
	     	paramitems = new ParameterItemsBean();
     		// put each parameter in a ParameterBean
     		// first only the Input Parameters, their value is not changed.
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
  				// Store the value of the parameter passed to this action class.
     			paramitem.setValue((String)request.getParameter(p));
     			paramitems.add(paramitem);
     		}

            // Now try to store the parameters passed in the paramdata (ParameterData) object.
		    outputs = paramdata.getOutputs();
		    outputsit = outputs.iterator();
   			while (outputsit.hasNext()) {
   				p = (String)outputsit.next();
   				// Check for correct value
   				String v = (String)request.getParameter(p);
   				java.lang.Class c = paramdata.getType(p);
   				if (c.isAssignableFrom(s.getClass())) {
   					servlet.log("String value is set; v = " + v);
           			paramdata.setValue(p, v);
   				}
   				else {
   					if (c.isAssignableFrom(b.getClass())) {
   						// check for correct Boolean value
   						b = new Boolean(v);
   						servlet.log("Boolean value is set; v = " + v + " b = " + b);
   						paramdata.setValue(p, b);
   					}
   					else {
   						if (c.isAssignableFrom(d.getClass())); {
   							d = new Double(v);
       						servlet.log("Double value is set; v = " + v + " d = " + d);
   							paramdata.setValue(p, d);
   						}
   					}
   				}
       	    }

       	    task.complete(paramdata);

   		}

		catch (NumberFormatException n) {
			if (servlet.getDebug() >= 1) {
			    servlet.log(n.getMessage());
			}
            session.setAttribute(Constants.PARAMETERITEMS_KEY, paramitems);
			errors.add(ActionErrors.GLOBAL_ERROR,
			           new ActionError("error.completemanual.numberformat") );
            saveErrors(request, errors);
   	        return (new ActionForward(mapping.getInput()));
		}

		catch (IllegalArgumentException i) {
			if (servlet.getDebug() >= 1) {
			    servlet.log(i.getMessage());
			}
            session.setAttribute(Constants.PARAMETERITEMS_KEY, paramitems);
			errors.add(ActionErrors.GLOBAL_ERROR,
			           new ActionError("error.completemanual.illegalargument") );
            saveErrors(request, errors);
   	        return (new ActionForward(mapping.getInput()));
		}

   		catch (WorkflowException e) {
			if (servlet.getDebug() >= 1) {
			    servlet.log(e.getMessage());
			}
   		   	errors.add(ActionErrors.GLOBAL_ERROR,
       	               new ActionError("error.fatal.complete.task") );
       	    errors.add(ActionErrors.GLOBAL_ERROR,
       	               new ActionError("error.consult") );
            saveErrors(request, errors);
   	        return (new ActionForward(mapping.getInput()));
   		}

        session.removeAttribute(Constants.TASK_KEY);
        session.removeAttribute(Constants.TASKNAME_KEY);
        session.removeAttribute(Constants.PARAMETERITEMS_KEY);
   		return (mapping.findForward("success"));
	}

	// Forward control to the error page
   	errors.add(ActionErrors.GLOBAL_ERROR,
   	           new ActionError("error.fatal.invalidaction") );
   	errors.add(ActionErrors.GLOBAL_ERROR,
   	           new ActionError("error.consult") );
    saveErrors(request, errors);
  	// show the error on the main menu
    return (new ActionForward(mapping.getInput()));

    }

}

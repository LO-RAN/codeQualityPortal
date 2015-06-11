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
/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.compuware.optimal.flow.webconsole;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
 * This <strong>Action</strong> class validates a user logon. After the user
 * submits the logon form, this class is called and makes a connection to the
 * OptimalFlow Java Workflow Connector. If the connection is successful, the
 * <strong>WorkflowConnector</strong> object is stored in session scope. A
 * <strong>User</strong> object holding the user name is also stored in session
 * scope. Both objects can later be used by the other action classes that need
 * a WorkflowConnector object to create Java Workflow Connector list objects.
 * <br>
 * The user name of the User class is used to display the user name on several
 * forms. However, instead of storing the User object, you could decide to
 * store the Java Workflow Connector <strong>Actor</strong>
 * object. The Actor class has information about the Workflow Actor, such as
 * Actor location, working hours, system roles
 * (e.g., Task List Manager or Business Manager), and more. If you need this
 * information in the Web Console application, this is a good idea. By checking
 * if a Actor is a Task List Manager (method isTaskListManager), you can
 * control the methods that the Actor is allowed to call.
 * <br>
 * <strong>NOTE:</strong>
 * <br>
 * This class reads the connection string used by the WorkflowConnector object
 * from the context parameter with the name optimalflow-server-connection.
 * This class sets the MaxInactiveInterval for a session to the value of the
 * context parameter max-inactive-interval.
 */

public final class LogonAction extends Action {


    // --------------------------------------------------------- Public Methods


    /**
     *
     *
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
	User user = null;

    HttpSession session    = request.getSession();
    ServletContext context = session.getServletContext();
    String connectionCfg   = context.getInitParameter("optimalflow-server-connection");

	WorkflowConnector w  = null;

	// Validate the request parameters specified by the user
	ActionErrors errors = new ActionErrors();
	String username = ((LogonForm) form).getUsername();
	String password = ((LogonForm) form).getPassword();

	// make a connection to the Optimalflow Workflow API
	try {
   	    // Open a connection.
   	    w = new WorkflowConnector (connectionCfg, username, password);
	}
	catch (WorkflowException e) {
		errors.add(ActionErrors.GLOBAL_ERROR,
		           new ActionError("error.username.connectionfailed") );
		if (servlet.getDebug() >= 1)
	        servlet.log("connection failed for user: " + username + " WorkflowException: " + e.getMessage());
	}

	// Report any errors discovered back to the original form
	if (!errors.empty()) {
	    saveErrors(request, errors);
	    return (new ActionForward(mapping.getInput()));
	}

	// Logon is ok, create a User object and save the username in it
	// If needed the User class can be extended with properties like isBusinessManager, Roles.
	user = new User();
	user.setUsername(username);

    Integer i = new Integer(context.getInitParameter("max-inactive-interval"));
    session.setMaxInactiveInterval(i.intValue());
    if (servlet.getDebug() >= 1)
	    servlet.log("LogonAction, MaxInactiveInterval: " + session.getMaxInactiveInterval());

    // The workflowconnector is saved in User, when a session timeout occurs
    // the workflowconnector object is properly closed.
    user.setWorkflowConnector(w);
	// Save the logged-in user in session scope.
	session.setAttribute(Constants.USER_KEY, user);
	// Save the WorkflowConnector object w in session scope.
	session.setAttribute(Constants.CONNECTOR_KEY, w);

	if (servlet.getDebug() >= 1)
	    servlet.log("LogonAction: User '" + username +
	                "' logged on in session " + session.getId());

    // Remove the obsolete form bean
	if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope()))
                request.removeAttribute(mapping.getAttribute());
            else
                session.removeAttribute(mapping.getAttribute());
		}

	// Forward control to the specified success URI
	return (mapping.findForward("success"));

    }

}

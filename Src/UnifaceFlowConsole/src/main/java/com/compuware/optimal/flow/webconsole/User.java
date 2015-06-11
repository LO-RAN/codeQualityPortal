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

import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSession;

import javax.servlet.ServletContext;

//import java.io.IOException;

import com.compuware.optimal.flow.*;

/**
 *
 * Object that represents a logged-on user of the OptimalFlow Workflow
 * Connector.
 *
 * The user name is saved and the <strong>WorkflowConnector</strong> object
 * is saved within the <strong>User</strong> object. When a session timeout
 * occurs, the User object is notified because it implements
 * the HttpSessionBindingListener interface. The method valueUnbound gets the
 * control and here the close method of the WorkflowConnector object is called.
 *
 * @see LogonAction
 */

public final class User implements HttpSessionBindingListener {


    // =================================================== Instance Variables


    /**
     * The username (must be unique).
     */
    private String username = null;

    /**
     * The reference to the WorkflowConnector object.
     */
    private WorkflowConnector workflowconnector = null;


    // =========================================================== Properties


    /**
     * Return the username.
     */
    public String getUsername() {

	return (this.username);

    }


    /**
     * Set the username.
     *
     * @param username New username.
     */
    public void setUsername(String username) {

	this.username = username;

    }

    /**
     * Set the reference to the WorkflowConnector.
     * This reference is used when a session timeout occurs. The close method
     * of the WorkflowConnector object is called. This ensures that session
     * records are removed from the OptimalFlow session table.
     *
     * @param workflowconnector workflowconnector reference.
     */

    public void setWorkflowConnector(WorkflowConnector workflowconnector) {

	this.workflowconnector = workflowconnector;

    }


    /**
     * This class implements the HttpSessionBindingListener interface.
     */

    public void valueBound(HttpSessionBindingEvent event) {

	}

    /**
     * This class implements the HttpSessionBindingListener interface.
     * The valueUnbound method calls the close method of the
     * <strong>WorkflowConnector</strong> object to close
     * it properly.
     *
     */

    public void valueUnbound(HttpSessionBindingEvent event) {

        HttpSession session = event.getSession();
        ServletContext servletcontext = session.getServletContext();

        servletcontext.log("User.valueUnbound: Session ended for user " + username);

        try {
            workflowconnector.close();
            servletcontext.log("User.valueUnbound: WorkflowConnector object closed");
		}
		catch (WorkflowException e) {
			servletcontext.log("User.valueUnbound", e.fillInStackTrace());
		}

	}

}

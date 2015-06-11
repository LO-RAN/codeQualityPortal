/**
 * 
 */
package com.compuware.caqs.security.auth;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * @author cwfr-fdubois
 *
 */
public class SessionListener implements HttpSessionListener {

    // déclaration du logger
    static protected Logger logger = Logger.getLogger("Security");

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {
		logger.trace("Session created: " + arg0.toString());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		logger.trace("Session destroyed: " + arg0.toString());
	}

}

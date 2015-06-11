/**
 * 
 */
package com.compuware.caqs.security.auth;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

/**
 * @author cwfr-fdubois
 *
 */
public class SessionBindingListener implements HttpSessionBindingListener {

    // déclaration du logger
    static protected Logger logger = Logger.getLogger("Security");
    /** Id de l'utilisateur dans la base optimalview
     */
    protected String userId = "";
    protected String cookie = null;

    /** Méthode d'accès à l'identifiant de l'utilisateur.
     * @return l'identifiant de l'utilisateur
     */
    public String getId() {
        return this.userId;
    }

    /** Méthode d'accès à l'identifiant de l'utilisateur.
     * @param userId identifiant de l'utilisateur
     */
    public void setId(String userId) {
        this.userId = userId;
    }

    /**
     * @return Returns the cookie.
     */
    public String getCookie() {
        return cookie;
    }

    /**
     * @param cookie The cookie to set.
     */
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0) {
        logger.trace("valueBound: user=" + this.userId + ", cookie="
                + this.cookie);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent arg0) {
        logger.trace("valueUnbound: user=" + this.userId + ", cookie="
                + this.cookie);
        SessionManager.getInstance().removeUser(cookie);
    }
}

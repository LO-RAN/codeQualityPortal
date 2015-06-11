package com.compuware.caqs.security.auth;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class UserData {

    private static org.apache.log4j.Logger logger = Logger.getLogger("Security");

    private String id = null;
	private String sessionId = null;
	private String cookie = null;
	
	private Date loginDateTime = new Date();
	private Date lastAccessDate = new Date();
	
	private Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();

	public UserData(String id, String cookie, String sessionId, HttpSession session) {
		this.id = id;
		this.cookie = cookie;
		this.sessionId = sessionId;
		this.sessionMap.put(session.getId(), session);
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

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the sessionId.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId The sessionId to set.
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return Returns the loginDateTime.
	 */
	public Date getLoginDateTime() {
		return loginDateTime;
	}

	/**
	 * @param loginDateTime The loginDateTime to set.
	 */
	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	/**
	 * @return Returns the lastAccessDate.
	 */
	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	/**
	 * @param lastAccessDate The lastAccessDate to set.
	 */
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	/**
	 * @return Returns the sessionMap.
	 */
	public Map<String, HttpSession> getSessionMap() {
		return sessionMap;
	}

	/**
	 * @param sessionMap The sessionMap to set.
	 */
	public void setSessionMap(Map<String, HttpSession> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public void addSessions(Map<String, HttpSession> sessions) {
		if (sessions != null) {
			Set<String> sessionKeys = sessions.keySet();
			Iterator<String> sessionKeyIter = sessionKeys.iterator();
			while (sessionKeyIter.hasNext()) {
				String currentSessionKey = sessionKeyIter.next();
				if (!this.sessionMap.containsKey(currentSessionKey)) {
					this.sessionMap.put(currentSessionKey, sessions.get(currentSessionKey));
				}
			}
		}
	}
	
	public void cleanSessions() {
		Collection<HttpSession> sessionColl = this.sessionMap.values();
		Iterator<HttpSession> sessionIter = sessionColl.iterator();
		while (sessionIter.hasNext()) {
			HttpSession currentSession = sessionIter.next();
			try {
				currentSession.invalidate();
			}
			catch (IllegalStateException e) {
				logger.trace("Error invalidating session", e);
			}
			finally {
				sessionIter.remove();
			}
		}
	}
	
}

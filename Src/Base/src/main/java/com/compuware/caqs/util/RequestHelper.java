/*
 * RequestHelper.java
 *
 * Created on 10 juin 2004, 09:57
 */

package com.compuware.caqs.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author cwfr-fdubois
 */
public class RequestHelper {

    /**
     * Creates a new instance of RequestHelper
     */
    public RequestHelper() {
    }

    public static String retrieve(HttpServletRequest request, HttpSession session, String key, String defaultValue) {
        String result = request.getParameter(key);
        if (result == null) {
            result = (String) request.getAttribute(key);
        }
        if (result == null) {
            result = (String) session.getAttribute(key);
        }
        if (result == null || result.length() == 0) {
            result = defaultValue;
        }
        return result;
    }

    public static void forwardParameter(HttpServletRequest request, String key) {
    	request.setAttribute(key, request.getParameter(key));
    }

    public static String getParameterAndForward(HttpServletRequest request, String key) {
    	String result = request.getParameter(key);
    	if (result == null) {
    		result = (String)request.getAttribute(key);
    	}
    	request.setAttribute(key, result);
    	return result;
    }
}

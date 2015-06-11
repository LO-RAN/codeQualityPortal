// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WRDServlet.java

package com.compuware.carscode.unifaceview.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.compuware.caqs.security.auth.SessionManager;
import com.compuware.uniface.urd.WRDServlet;

// Referenced classes of package com.compuware.uniface.urd:
//            AnUSPServlet, URDException, WRDExec, WebForm, 
//            URDDebug, AServlet, URDConnector, IMiddleware

public class CarscodeServlet extends WRDServlet {

	Logger logger = Logger.getLogger("security");
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		String pathInfo = request.getPathInfo();
        if (pathInfo.endsWith("logon.logout")) {
    		doCaqsLogout(request);
        }
        super.doGet(request, response);
	}

	private void doCaqsLogout(HttpServletRequest request) {
        String cookie = getCookieValue("CUwdSessionID", (HttpServletRequest)request);
		SessionManager.getInstance().removeUser(cookie);
	}
	
   private String getCookieValue(String name, HttpServletRequest request) {
       String result = null;
	   Cookie[] cookies = request.getCookies();
	   if (cookies != null && cookies.length > 0) {
		   Cookie currentCookie = null;
		   for (int i = 0; i < cookies.length && result == null; i++) {
			   currentCookie = cookies[i];
			   if (currentCookie.getName().equalsIgnoreCase(name)) {
				   String cookieValue = null;
				   try {
					   cookieValue = URLDecoder.decode(currentCookie.getValue(), "utf-8");
				   }
				   catch (UnsupportedEncodingException e) {
					   cookieValue = URLDecoder.decode(currentCookie.getValue());
				   }
				   result = cookieValue;
			   }
		   }
	   }
       return result;
   }
	   
}

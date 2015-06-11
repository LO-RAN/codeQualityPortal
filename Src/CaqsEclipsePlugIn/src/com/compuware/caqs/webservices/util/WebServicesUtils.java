package com.compuware.caqs.webservices.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.eclipse.jface.preference.IPreferenceStore;

import com.compuware.caqs.eclipseplugin.preferences.PreferenceConstants;
import com.compuware.caqs.plugin.Activator;
import com.compuware.caqs.webservices.callback.CaqsTestPasswordCallbackHandler;

public class WebServicesUtils {

	public static void addAuthenticationDetails(Client client) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String login = store.getString(PreferenceConstants.P_CAQS_LOGIN);
		org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();

		Map<String, Object> outProps = new HashMap<String, Object>();

		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);
		outProps.put(WSHandlerConstants.ACTION,
				WSHandlerConstants.USERNAME_TOKEN);
		// Specify our username
		outProps.put(WSHandlerConstants.USER, login);
		// Password type : plain text
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		// for hashed password use:
		// properties.put(WSHandlerConstants.PASSWORD_TYPE,
		// WSConstants.PW_DIGEST);
		// Callback used to retrieve password for given user.
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
				CaqsTestPasswordCallbackHandler.class.getName());
	}

	public static void addAuthenticationDetails(String login, String password,
			Client client) {
		org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();

		Map<String, Object> outProps = new HashMap<String, Object>();

		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);
		outProps.put(WSHandlerConstants.ACTION,
				WSHandlerConstants.USERNAME_TOKEN);
		// Specify our username
		outProps.put(WSHandlerConstants.USER, login);
		// Password type : plain text
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		// for hashed password use:
		// properties.put(WSHandlerConstants.PASSWORD_TYPE,
		// WSConstants.PW_DIGEST);
		// Callback used to retrieve password for given user.
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
				CaqsTestPasswordCallbackHandler.class.getName());
	}
}

package com.compuware.caqs.webservices.callback;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.eclipse.jface.preference.IPreferenceStore;

import com.compuware.caqs.eclipseplugin.preferences.PreferenceConstants;
import com.compuware.caqs.plugin.Activator;

public class CaqsTestPasswordCallbackHandler implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		// set the password for our message.
		IPreferenceStore store = Activator.getDefault()
        .getPreferenceStore();
		String password = store.getString(PreferenceConstants.P_CAQS_TMP_PASSWORD);
		if(password==null || "".equals(password)) {
			password = store.getString(PreferenceConstants.P_CAQS_PASSWORD);
		}
		pc.setPassword(password);
	}
}

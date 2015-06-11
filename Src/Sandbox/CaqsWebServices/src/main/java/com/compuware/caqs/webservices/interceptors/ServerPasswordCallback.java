package com.compuware.caqs.webservices.interceptors;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.ws.security.WSPasswordCallback;

/**
 *
 * @author cwfr-dzysman
 */
public class ServerPasswordCallback implements CallbackHandler {
    

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_WEBSERVICES_LOGGER_KEY);

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0]; 
        // this seems ridiculous, but is necessary for passing authentication on to Spring-Security.
        // we're essentially bypassing CXF's WSS4JInterceptor by ensuring that the password callback always matches the client password.
        pc.setPassword( pc.getPassword() );
    }
}

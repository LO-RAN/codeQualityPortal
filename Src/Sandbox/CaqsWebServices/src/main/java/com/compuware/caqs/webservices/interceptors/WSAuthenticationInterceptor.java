package com.compuware.caqs.webservices.interceptors;

import java.util.Map;
import java.util.Vector;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.WSUsernameTokenPrincipal;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.concurrent.SessionIdentifierAware;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.util.Assert;

/**
 *
 * @author cwfr-dzysman
 */
public class WSAuthenticationInterceptor extends WSS4JInInterceptor implements InitializingBean {

    private AuthenticationManager authenticationManager;

    public WSAuthenticationInterceptor() {
        super();
    }

    public WSAuthenticationInterceptor(Map<String, Object> properties) {
        super(properties);
    }

    public void afterPropertiesSet() throws Exception {
        // ensure the 2 objects we need are not null
        Assert.notNull(getAuthenticationManager(), "Authentication manager must be set");
        Assert.notNull(getProperties(), "Interceptor properties must be set, even if empty");
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        super.handleMessage(message);
        // get out the results from the message context
        Vector<WSHandlerResult> result = (Vector<WSHandlerResult>) message.getContextualProperty(WSHandlerConstants.RECV_RESULTS);
        if (result != null) {
            for (WSHandlerResult res : result) {
                // loop through security engine results
                for (WSSecurityEngineResult securityResult : (Vector<WSSecurityEngineResult>) res.getResults()) {
                    int action = securityResult.getAction();
                    // determine if the action was a username token
                    if ((action & WSConstants.UT) > 0) {
                        // get the principal object
                        WSUsernameTokenPrincipal principal = (WSUsernameTokenPrincipal) securityResult.getPrincipal();
                        Authentication authentication = new UsernamePasswordAuthenticationToken(principal.getName(), principal.getPassword());
                        ((UsernamePasswordAuthenticationToken)authentication).setDetails(new SessionIdentifierAware() {
                            public String getSessionId() {
                                return "999999999";
                            }
                        });
                        authentication = authenticationManager.authenticate(authentication);
                        if (!authentication.isAuthenticated()) {
                            System.out.println("This user is not authentic.");
                            //throw new AuthenticationException( "This user is not authentic." );
                        }
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } else {
            throw new RuntimeException("No security results!!");
        }
    }

    /**
     * @return the authenticationManager
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /**
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}

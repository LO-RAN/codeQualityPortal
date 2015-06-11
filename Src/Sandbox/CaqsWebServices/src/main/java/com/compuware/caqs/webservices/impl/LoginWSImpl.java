package com.compuware.caqs.webservices.impl;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.webservices.LoginWS;
import com.compuware.toolbox.util.logging.LoggerManager;
import javax.jws.WebService;

/**
 *
 * @author cwfr-dzysman
 */
@WebService(//endpointInterface = "com.compuware.caqs.webservices.LoginWS",
serviceName = "LoginWS")
public class LoginWSImpl /*implements LoginWS */{

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_WEBSERVICES_LOGGER_KEY);

    public boolean validateAuthentication(String login, String password) {
        boolean retour = false;
        /*if (login != null) {
            // no password is passed in
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login,
                    password);
            Principal p = null;
            authRequest.setDetails(new SessionIdentifierAware() {

                @Override
                public String getSessionId() {
                    return "999999999";
                }
            });

            try {
                Object obj = BusinessFactory.getInstance().getBean("caqsAuthenticationManager");
                Authentication authResult = ((AuthenticationManager) obj).authenticate(authRequest);
                retour = authResult.isAuthenticated();
            } catch (AuthenticationException failed) {
                // Authentication failed
                logger.debug("Authentication request for user: " + login
                        + " failed: " + failed.toString());
            }
        }*/
        retour = true;

        return retour;
    }
}

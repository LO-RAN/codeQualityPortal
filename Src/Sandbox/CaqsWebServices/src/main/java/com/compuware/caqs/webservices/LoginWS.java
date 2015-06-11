package com.compuware.caqs.webservices;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author cwfr-dzysman
 */
@WebService
public interface LoginWS {

    @WebMethod
    public boolean validateAuthentication(@WebParam(name="login") String login, @WebParam(name="password") String password);
}

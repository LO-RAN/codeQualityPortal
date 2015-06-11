
package com.compuware.caqs.webservices.projectinfos;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

import org.eclipse.jface.preference.IPreferenceStore;

import com.compuware.caqs.eclipseplugin.preferences.PreferenceConstants;
import com.compuware.caqs.plugin.Activator;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ProjectInfosWS", targetNamespace = "http://impl.webservices.caqs.compuware.com/")
public class ProjectInfosWS
    extends Service
{

    private final static URL PROJECTINFOSWS_WSDL_LOCATION;
    private final static WebServiceException PROJECTINFOSWS_EXCEPTION;
    private final static QName PROJECTINFOSWS_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "ProjectInfosWS");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
        	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    		String urlHost = store.getString(PreferenceConstants.P_CAQS_URL);
            url = new URL(urlHost+"/caqs/services/ProjectInfosWS?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PROJECTINFOSWS_WSDL_LOCATION = url;
        PROJECTINFOSWS_EXCEPTION = e;
    }

    public ProjectInfosWS() {
        super(__getWsdlLocation(), PROJECTINFOSWS_QNAME);
    }

    public ProjectInfosWS(URL wsdlLocation) {
        super(wsdlLocation, PROJECTINFOSWS_QNAME);
    }

    public ProjectInfosWS(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * 
     * @return
     *     returns ProjectInfosWSImpl
     */
    @WebEndpoint(name = "ProjectInfosWSImplPort")
    public ProjectInfosWSImpl getProjectInfosWSImplPort() {
        return super.getPort(new QName("http://impl.webservices.caqs.compuware.com/", "ProjectInfosWSImplPort"), ProjectInfosWSImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ProjectInfosWSImpl
     */
    @WebEndpoint(name = "ProjectInfosWSImplPort")
    public ProjectInfosWSImpl getProjectInfosWSImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.webservices.caqs.compuware.com/", "ProjectInfosWSImplPort"), ProjectInfosWSImpl.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PROJECTINFOSWS_EXCEPTION!= null) {
            throw PROJECTINFOSWS_EXCEPTION;
        }
        return PROJECTINFOSWS_WSDL_LOCATION;
    }

}

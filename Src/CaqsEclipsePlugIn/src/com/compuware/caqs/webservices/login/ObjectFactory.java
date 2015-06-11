
package com.compuware.caqs.webservices.login;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the javaapplication5.newtest3 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValidateAuthentication_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "validateAuthentication");
    private final static QName _ValidateAuthenticationResponse_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "validateAuthenticationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: javaapplication5.newtest3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ValidateAuthenticationResponse }
     * 
     */
    public ValidateAuthenticationResponse createValidateAuthenticationResponse() {
        return new ValidateAuthenticationResponse();
    }

    /**
     * Create an instance of {@link ValidateAuthentication }
     * 
     */
    public ValidateAuthentication createValidateAuthentication() {
        return new ValidateAuthentication();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateAuthentication }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservices.caqs.compuware.com/", name = "validateAuthentication")
    public JAXBElement<ValidateAuthentication> createValidateAuthentication(ValidateAuthentication value) {
        return new JAXBElement<ValidateAuthentication>(_ValidateAuthentication_QNAME, ValidateAuthentication.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateAuthenticationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservices.caqs.compuware.com/", name = "validateAuthenticationResponse")
    public JAXBElement<ValidateAuthenticationResponse> createValidateAuthenticationResponse(ValidateAuthenticationResponse value) {
        return new JAXBElement<ValidateAuthenticationResponse>(_ValidateAuthenticationResponse_QNAME, ValidateAuthenticationResponse.class, null, value);
    }

}

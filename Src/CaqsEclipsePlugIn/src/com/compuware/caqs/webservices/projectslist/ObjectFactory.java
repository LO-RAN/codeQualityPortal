
package com.compuware.caqs.webservices.projectslist;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the javaapplication5.newtest2 package. 
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

    private final static QName _GetProjectsListForUserResponse_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "getProjectsListForUserResponse");
    private final static QName _GetProjectsListForUser_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "getProjectsListForUser");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: javaapplication5.newtest2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetProjectsListForUserResponse }
     * 
     */
    public GetProjectsListForUserResponse createGetProjectsListForUserResponse() {
        return new GetProjectsListForUserResponse();
    }

    /**
     * Create an instance of {@link GetProjectsListForUser }
     * 
     */
    public GetProjectsListForUser createGetProjectsListForUser() {
        return new GetProjectsListForUser();
    }

    /**
     * Create an instance of {@link ProjectWS }
     * 
     */
    public ProjectWS createProjectWS() {
        return new ProjectWS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProjectsListForUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservices.caqs.compuware.com/", name = "getProjectsListForUserResponse")
    public JAXBElement<GetProjectsListForUserResponse> createGetProjectsListForUserResponse(GetProjectsListForUserResponse value) {
        return new JAXBElement<GetProjectsListForUserResponse>(_GetProjectsListForUserResponse_QNAME, GetProjectsListForUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProjectsListForUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservices.caqs.compuware.com/", name = "getProjectsListForUser")
    public JAXBElement<GetProjectsListForUser> createGetProjectsListForUser(GetProjectsListForUser value) {
        return new JAXBElement<GetProjectsListForUser>(_GetProjectsListForUser_QNAME, GetProjectsListForUser.class, null, value);
    }

}

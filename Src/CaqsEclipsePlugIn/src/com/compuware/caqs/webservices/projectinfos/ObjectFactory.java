
package com.compuware.caqs.webservices.projectinfos;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the javaapplication5.newtest5 package. 
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

    private final static QName _RetrieveAnalyzedSourceResponse_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "retrieveAnalyzedSourceResponse");
    private final static QName _RetrieveAnalyzedSource_QNAME = new QName("http://impl.webservices.caqs.compuware.com/", "retrieveAnalyzedSource");
    private final static QName _RetrieveAnalyzedSourceResponseReturn_QNAME = new QName("", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: javaapplication5.newtest5
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RetrieveAnalyzedSourceResponse }
     * 
     */
    public RetrieveAnalyzedSourceResponse createRetrieveAnalyzedSourceResponse() {
        return new RetrieveAnalyzedSourceResponse();
    }

    /**
     * Create an instance of {@link RetrieveAnalyzedSource }
     * 
     */
    public RetrieveAnalyzedSource createRetrieveAnalyzedSource() {
        return new RetrieveAnalyzedSource();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveAnalyzedSourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservices.caqs.compuware.com/", name = "retrieveAnalyzedSourceResponse")
    public JAXBElement<RetrieveAnalyzedSourceResponse> createRetrieveAnalyzedSourceResponse(RetrieveAnalyzedSourceResponse value) {
        return new JAXBElement<RetrieveAnalyzedSourceResponse>(_RetrieveAnalyzedSourceResponse_QNAME, RetrieveAnalyzedSourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveAnalyzedSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservices.caqs.compuware.com/", name = "retrieveAnalyzedSource")
    public JAXBElement<RetrieveAnalyzedSource> createRetrieveAnalyzedSource(RetrieveAnalyzedSource value) {
        return new JAXBElement<RetrieveAnalyzedSource>(_RetrieveAnalyzedSource_QNAME, RetrieveAnalyzedSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = RetrieveAnalyzedSourceResponse.class)
    public JAXBElement<byte[]> createRetrieveAnalyzedSourceResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_RetrieveAnalyzedSourceResponseReturn_QNAME, byte[].class, RetrieveAnalyzedSourceResponse.class, ((byte[]) value));
    }

}

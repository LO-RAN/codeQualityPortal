/**
 * ABSTRACTHTML_PORTLET_PROPERTYType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface;

public class ABSTRACTHTML_PORTLET_PROPERTYType  implements java.io.Serializable {
    private java.lang.String PORTLET_ID;

    private java.lang.String PROPERTY_ID;

    private java.lang.String HTML_CONTENT;

    public ABSTRACTHTML_PORTLET_PROPERTYType() {
    }

    public ABSTRACTHTML_PORTLET_PROPERTYType(
           java.lang.String PORTLET_ID,
           java.lang.String PROPERTY_ID,
           java.lang.String HTML_CONTENT) {
           this.PORTLET_ID = PORTLET_ID;
           this.PROPERTY_ID = PROPERTY_ID;
           this.HTML_CONTENT = HTML_CONTENT;
    }


    /**
     * Gets the PORTLET_ID value for this ABSTRACTHTML_PORTLET_PROPERTYType.
     * 
     * @return PORTLET_ID
     */
    public java.lang.String getPORTLET_ID() {
        return PORTLET_ID;
    }


    /**
     * Sets the PORTLET_ID value for this ABSTRACTHTML_PORTLET_PROPERTYType.
     * 
     * @param PORTLET_ID
     */
    public void setPORTLET_ID(java.lang.String PORTLET_ID) {
        this.PORTLET_ID = PORTLET_ID;
    }


    /**
     * Gets the PROPERTY_ID value for this ABSTRACTHTML_PORTLET_PROPERTYType.
     * 
     * @return PROPERTY_ID
     */
    public java.lang.String getPROPERTY_ID() {
        return PROPERTY_ID;
    }


    /**
     * Sets the PROPERTY_ID value for this ABSTRACTHTML_PORTLET_PROPERTYType.
     * 
     * @param PROPERTY_ID
     */
    public void setPROPERTY_ID(java.lang.String PROPERTY_ID) {
        this.PROPERTY_ID = PROPERTY_ID;
    }


    /**
     * Gets the HTML_CONTENT value for this ABSTRACTHTML_PORTLET_PROPERTYType.
     * 
     * @return HTML_CONTENT
     */
    public java.lang.String getHTML_CONTENT() {
        return HTML_CONTENT;
    }


    /**
     * Sets the HTML_CONTENT value for this ABSTRACTHTML_PORTLET_PROPERTYType.
     * 
     * @param HTML_CONTENT
     */
    public void setHTML_CONTENT(java.lang.String HTML_CONTENT) {
        this.HTML_CONTENT = HTML_CONTENT;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTHTML_PORTLET_PROPERTYType)) return false;
        ABSTRACTHTML_PORTLET_PROPERTYType other = (ABSTRACTHTML_PORTLET_PROPERTYType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.PORTLET_ID==null && other.getPORTLET_ID()==null) || 
             (this.PORTLET_ID!=null &&
              this.PORTLET_ID.equals(other.getPORTLET_ID()))) &&
            ((this.PROPERTY_ID==null && other.getPROPERTY_ID()==null) || 
             (this.PROPERTY_ID!=null &&
              this.PROPERTY_ID.equals(other.getPROPERTY_ID()))) &&
            ((this.HTML_CONTENT==null && other.getHTML_CONTENT()==null) || 
             (this.HTML_CONTENT!=null &&
              this.HTML_CONTENT.equals(other.getHTML_CONTENT())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getPORTLET_ID() != null) {
            _hashCode += getPORTLET_ID().hashCode();
        }
        if (getPROPERTY_ID() != null) {
            _hashCode += getPROPERTY_ID().hashCode();
        }
        if (getHTML_CONTENT() != null) {
            _hashCode += getHTML_CONTENT().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTHTML_PORTLET_PROPERTYType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTHTML_PORTLET_PROPERTY", "ABSTRACTHTML_PORTLET_PROPERTYType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PORTLET_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PORTLET_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PROPERTY_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PROPERTY_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HTML_CONTENT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "HTML_CONTENT"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

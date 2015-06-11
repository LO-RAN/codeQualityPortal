/**
 * ABSTRACTFOOTERType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTFOOTER.services.applic.uniface;

public class ABSTRACTFOOTERType  implements java.io.Serializable {
    private java.lang.String FOOTER_ID;

    private java.lang.String FOOTER_CONTENT_TOP;

    private java.lang.String FOOTER_CONTENT_BOTTOM;

    public ABSTRACTFOOTERType() {
    }

    public ABSTRACTFOOTERType(
           java.lang.String FOOTER_ID,
           java.lang.String FOOTER_CONTENT_TOP,
           java.lang.String FOOTER_CONTENT_BOTTOM) {
           this.FOOTER_ID = FOOTER_ID;
           this.FOOTER_CONTENT_TOP = FOOTER_CONTENT_TOP;
           this.FOOTER_CONTENT_BOTTOM = FOOTER_CONTENT_BOTTOM;
    }


    /**
     * Gets the FOOTER_ID value for this ABSTRACTFOOTERType.
     * 
     * @return FOOTER_ID
     */
    public java.lang.String getFOOTER_ID() {
        return FOOTER_ID;
    }


    /**
     * Sets the FOOTER_ID value for this ABSTRACTFOOTERType.
     * 
     * @param FOOTER_ID
     */
    public void setFOOTER_ID(java.lang.String FOOTER_ID) {
        this.FOOTER_ID = FOOTER_ID;
    }


    /**
     * Gets the FOOTER_CONTENT_TOP value for this ABSTRACTFOOTERType.
     * 
     * @return FOOTER_CONTENT_TOP
     */
    public java.lang.String getFOOTER_CONTENT_TOP() {
        return FOOTER_CONTENT_TOP;
    }


    /**
     * Sets the FOOTER_CONTENT_TOP value for this ABSTRACTFOOTERType.
     * 
     * @param FOOTER_CONTENT_TOP
     */
    public void setFOOTER_CONTENT_TOP(java.lang.String FOOTER_CONTENT_TOP) {
        this.FOOTER_CONTENT_TOP = FOOTER_CONTENT_TOP;
    }


    /**
     * Gets the FOOTER_CONTENT_BOTTOM value for this ABSTRACTFOOTERType.
     * 
     * @return FOOTER_CONTENT_BOTTOM
     */
    public java.lang.String getFOOTER_CONTENT_BOTTOM() {
        return FOOTER_CONTENT_BOTTOM;
    }


    /**
     * Sets the FOOTER_CONTENT_BOTTOM value for this ABSTRACTFOOTERType.
     * 
     * @param FOOTER_CONTENT_BOTTOM
     */
    public void setFOOTER_CONTENT_BOTTOM(java.lang.String FOOTER_CONTENT_BOTTOM) {
        this.FOOTER_CONTENT_BOTTOM = FOOTER_CONTENT_BOTTOM;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTFOOTERType)) return false;
        ABSTRACTFOOTERType other = (ABSTRACTFOOTERType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.FOOTER_ID==null && other.getFOOTER_ID()==null) || 
             (this.FOOTER_ID!=null &&
              this.FOOTER_ID.equals(other.getFOOTER_ID()))) &&
            ((this.FOOTER_CONTENT_TOP==null && other.getFOOTER_CONTENT_TOP()==null) || 
             (this.FOOTER_CONTENT_TOP!=null &&
              this.FOOTER_CONTENT_TOP.equals(other.getFOOTER_CONTENT_TOP()))) &&
            ((this.FOOTER_CONTENT_BOTTOM==null && other.getFOOTER_CONTENT_BOTTOM()==null) || 
             (this.FOOTER_CONTENT_BOTTOM!=null &&
              this.FOOTER_CONTENT_BOTTOM.equals(other.getFOOTER_CONTENT_BOTTOM())));
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
        if (getFOOTER_ID() != null) {
            _hashCode += getFOOTER_ID().hashCode();
        }
        if (getFOOTER_CONTENT_TOP() != null) {
            _hashCode += getFOOTER_CONTENT_TOP().hashCode();
        }
        if (getFOOTER_CONTENT_BOTTOM() != null) {
            _hashCode += getFOOTER_CONTENT_BOTTOM().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTFOOTERType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER", "ABSTRACTFOOTERType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FOOTER_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FOOTER_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FOOTER_CONTENT_TOP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FOOTER_CONTENT_TOP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FOOTER_CONTENT_BOTTOM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FOOTER_CONTENT_BOTTOM"));
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

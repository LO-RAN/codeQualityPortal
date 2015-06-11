/**
 * STANDARDCOOKIE_CONTEXTType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package STANDARDCOOKIE_CONTEXT.services.applic.uniface;

public class STANDARDCOOKIE_CONTEXTType  implements java.io.Serializable {
    private java.lang.String COOKIES_IN;

    private java.lang.String COOKIES_OUT;

    public STANDARDCOOKIE_CONTEXTType() {
    }

    public STANDARDCOOKIE_CONTEXTType(
           java.lang.String COOKIES_IN,
           java.lang.String COOKIES_OUT) {
           this.COOKIES_IN = COOKIES_IN;
           this.COOKIES_OUT = COOKIES_OUT;
    }


    /**
     * Gets the COOKIES_IN value for this STANDARDCOOKIE_CONTEXTType.
     * 
     * @return COOKIES_IN
     */
    public java.lang.String getCOOKIES_IN() {
        return COOKIES_IN;
    }


    /**
     * Sets the COOKIES_IN value for this STANDARDCOOKIE_CONTEXTType.
     * 
     * @param COOKIES_IN
     */
    public void setCOOKIES_IN(java.lang.String COOKIES_IN) {
        this.COOKIES_IN = COOKIES_IN;
    }


    /**
     * Gets the COOKIES_OUT value for this STANDARDCOOKIE_CONTEXTType.
     * 
     * @return COOKIES_OUT
     */
    public java.lang.String getCOOKIES_OUT() {
        return COOKIES_OUT;
    }


    /**
     * Sets the COOKIES_OUT value for this STANDARDCOOKIE_CONTEXTType.
     * 
     * @param COOKIES_OUT
     */
    public void setCOOKIES_OUT(java.lang.String COOKIES_OUT) {
        this.COOKIES_OUT = COOKIES_OUT;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof STANDARDCOOKIE_CONTEXTType)) return false;
        STANDARDCOOKIE_CONTEXTType other = (STANDARDCOOKIE_CONTEXTType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.COOKIES_IN==null && other.getCOOKIES_IN()==null) || 
             (this.COOKIES_IN!=null &&
              this.COOKIES_IN.equals(other.getCOOKIES_IN()))) &&
            ((this.COOKIES_OUT==null && other.getCOOKIES_OUT()==null) || 
             (this.COOKIES_OUT!=null &&
              this.COOKIES_OUT.equals(other.getCOOKIES_OUT())));
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
        if (getCOOKIES_IN() != null) {
            _hashCode += getCOOKIES_IN().hashCode();
        }
        if (getCOOKIES_OUT() != null) {
            _hashCode += getCOOKIES_OUT().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(STANDARDCOOKIE_CONTEXTType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDCOOKIE_CONTEXT", "STANDARDCOOKIE_CONTEXTType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("COOKIES_IN");
        elemField.setXmlName(new javax.xml.namespace.QName("", "COOKIES_IN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("COOKIES_OUT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "COOKIES_OUT"));
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

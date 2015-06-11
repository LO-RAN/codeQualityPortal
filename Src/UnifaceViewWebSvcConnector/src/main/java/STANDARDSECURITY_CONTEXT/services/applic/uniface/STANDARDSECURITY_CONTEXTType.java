/**
 * STANDARDSECURITY_CONTEXTType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package STANDARDSECURITY_CONTEXT.services.applic.uniface;

public class STANDARDSECURITY_CONTEXTType  implements java.io.Serializable {
    private java.lang.String USERNAME;

    private java.lang.String PASSWORD;

    private java.lang.String PASSWORDTYPE;

    private java.lang.String NONCE;

    private java.util.Calendar CREATED;

    public STANDARDSECURITY_CONTEXTType() {
    }

    public STANDARDSECURITY_CONTEXTType(
           java.lang.String USERNAME,
           java.lang.String PASSWORD,
           java.lang.String PASSWORDTYPE,
           java.lang.String NONCE,
           java.util.Calendar CREATED) {
           this.USERNAME = USERNAME;
           this.PASSWORD = PASSWORD;
           this.PASSWORDTYPE = PASSWORDTYPE;
           this.NONCE = NONCE;
           this.CREATED = CREATED;
    }


    /**
     * Gets the USERNAME value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @return USERNAME
     */
    public java.lang.String getUSERNAME() {
        return USERNAME;
    }


    /**
     * Sets the USERNAME value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @param USERNAME
     */
    public void setUSERNAME(java.lang.String USERNAME) {
        this.USERNAME = USERNAME;
    }


    /**
     * Gets the PASSWORD value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @return PASSWORD
     */
    public java.lang.String getPASSWORD() {
        return PASSWORD;
    }


    /**
     * Sets the PASSWORD value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @param PASSWORD
     */
    public void setPASSWORD(java.lang.String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }


    /**
     * Gets the PASSWORDTYPE value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @return PASSWORDTYPE
     */
    public java.lang.String getPASSWORDTYPE() {
        return PASSWORDTYPE;
    }


    /**
     * Sets the PASSWORDTYPE value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @param PASSWORDTYPE
     */
    public void setPASSWORDTYPE(java.lang.String PASSWORDTYPE) {
        this.PASSWORDTYPE = PASSWORDTYPE;
    }


    /**
     * Gets the NONCE value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @return NONCE
     */
    public java.lang.String getNONCE() {
        return NONCE;
    }


    /**
     * Sets the NONCE value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @param NONCE
     */
    public void setNONCE(java.lang.String NONCE) {
        this.NONCE = NONCE;
    }


    /**
     * Gets the CREATED value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @return CREATED
     */
    public java.util.Calendar getCREATED() {
        return CREATED;
    }


    /**
     * Sets the CREATED value for this STANDARDSECURITY_CONTEXTType.
     * 
     * @param CREATED
     */
    public void setCREATED(java.util.Calendar CREATED) {
        this.CREATED = CREATED;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof STANDARDSECURITY_CONTEXTType)) return false;
        STANDARDSECURITY_CONTEXTType other = (STANDARDSECURITY_CONTEXTType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.USERNAME==null && other.getUSERNAME()==null) || 
             (this.USERNAME!=null &&
              this.USERNAME.equals(other.getUSERNAME()))) &&
            ((this.PASSWORD==null && other.getPASSWORD()==null) || 
             (this.PASSWORD!=null &&
              this.PASSWORD.equals(other.getPASSWORD()))) &&
            ((this.PASSWORDTYPE==null && other.getPASSWORDTYPE()==null) || 
             (this.PASSWORDTYPE!=null &&
              this.PASSWORDTYPE.equals(other.getPASSWORDTYPE()))) &&
            ((this.NONCE==null && other.getNONCE()==null) || 
             (this.NONCE!=null &&
              this.NONCE.equals(other.getNONCE()))) &&
            ((this.CREATED==null && other.getCREATED()==null) || 
             (this.CREATED!=null &&
              this.CREATED.equals(other.getCREATED())));
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
        if (getUSERNAME() != null) {
            _hashCode += getUSERNAME().hashCode();
        }
        if (getPASSWORD() != null) {
            _hashCode += getPASSWORD().hashCode();
        }
        if (getPASSWORDTYPE() != null) {
            _hashCode += getPASSWORDTYPE().hashCode();
        }
        if (getNONCE() != null) {
            _hashCode += getNONCE().hashCode();
        }
        if (getCREATED() != null) {
            _hashCode += getCREATED().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(STANDARDSECURITY_CONTEXTType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("USERNAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "USERNAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSWORD");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PASSWORD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PASSWORDTYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PASSWORDTYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NONCE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NONCE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CREATED");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CREATED"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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

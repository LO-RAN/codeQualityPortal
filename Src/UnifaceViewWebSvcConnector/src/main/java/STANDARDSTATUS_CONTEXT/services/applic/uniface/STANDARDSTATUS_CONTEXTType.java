/**
 * STANDARDSTATUS_CONTEXTType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package STANDARDSTATUS_CONTEXT.services.applic.uniface;

public class STANDARDSTATUS_CONTEXTType  implements java.io.Serializable {
    private java.lang.String ID;

    private java.lang.String STATUS;

    private java.lang.String STATUSCODE;

    private java.lang.String STATUSMESSAGE;

    public STANDARDSTATUS_CONTEXTType() {
    }

    public STANDARDSTATUS_CONTEXTType(
           java.lang.String ID,
           java.lang.String STATUS,
           java.lang.String STATUSCODE,
           java.lang.String STATUSMESSAGE) {
           this.ID = ID;
           this.STATUS = STATUS;
           this.STATUSCODE = STATUSCODE;
           this.STATUSMESSAGE = STATUSMESSAGE;
    }


    /**
     * Gets the ID value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @return ID
     */
    public java.lang.String getID() {
        return ID;
    }


    /**
     * Sets the ID value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @param ID
     */
    public void setID(java.lang.String ID) {
        this.ID = ID;
    }


    /**
     * Gets the STATUS value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @return STATUS
     */
    public java.lang.String getSTATUS() {
        return STATUS;
    }


    /**
     * Sets the STATUS value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @param STATUS
     */
    public void setSTATUS(java.lang.String STATUS) {
        this.STATUS = STATUS;
    }


    /**
     * Gets the STATUSCODE value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @return STATUSCODE
     */
    public java.lang.String getSTATUSCODE() {
        return STATUSCODE;
    }


    /**
     * Sets the STATUSCODE value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @param STATUSCODE
     */
    public void setSTATUSCODE(java.lang.String STATUSCODE) {
        this.STATUSCODE = STATUSCODE;
    }


    /**
     * Gets the STATUSMESSAGE value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @return STATUSMESSAGE
     */
    public java.lang.String getSTATUSMESSAGE() {
        return STATUSMESSAGE;
    }


    /**
     * Sets the STATUSMESSAGE value for this STANDARDSTATUS_CONTEXTType.
     * 
     * @param STATUSMESSAGE
     */
    public void setSTATUSMESSAGE(java.lang.String STATUSMESSAGE) {
        this.STATUSMESSAGE = STATUSMESSAGE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof STANDARDSTATUS_CONTEXTType)) return false;
        STANDARDSTATUS_CONTEXTType other = (STANDARDSTATUS_CONTEXTType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.STATUS==null && other.getSTATUS()==null) || 
             (this.STATUS!=null &&
              this.STATUS.equals(other.getSTATUS()))) &&
            ((this.STATUSCODE==null && other.getSTATUSCODE()==null) || 
             (this.STATUSCODE!=null &&
              this.STATUSCODE.equals(other.getSTATUSCODE()))) &&
            ((this.STATUSMESSAGE==null && other.getSTATUSMESSAGE()==null) || 
             (this.STATUSMESSAGE!=null &&
              this.STATUSMESSAGE.equals(other.getSTATUSMESSAGE())));
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
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getSTATUS() != null) {
            _hashCode += getSTATUS().hashCode();
        }
        if (getSTATUSCODE() != null) {
            _hashCode += getSTATUSCODE().hashCode();
        }
        if (getSTATUSMESSAGE() != null) {
            _hashCode += getSTATUSMESSAGE().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(STANDARDSTATUS_CONTEXTType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("STATUS");
        elemField.setXmlName(new javax.xml.namespace.QName("", "STATUS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("STATUSCODE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "STATUSCODE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("STATUSMESSAGE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "STATUSMESSAGE"));
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

/**
 * ABSTRACTGROUPType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTGROUP.services.applic.uniface;

public class ABSTRACTGROUPType  implements java.io.Serializable {
    private java.lang.String GROUP_ID;

    private java.lang.String NAME;

    private java.lang.String ROLE;

    private java.lang.String EXTERNAL_ID;

    public ABSTRACTGROUPType() {
    }

    public ABSTRACTGROUPType(
           java.lang.String GROUP_ID,
           java.lang.String NAME,
           java.lang.String ROLE,
           java.lang.String EXTERNAL_ID) {
           this.GROUP_ID = GROUP_ID;
           this.NAME = NAME;
           this.ROLE = ROLE;
           this.EXTERNAL_ID = EXTERNAL_ID;
    }


    /**
     * Gets the GROUP_ID value for this ABSTRACTGROUPType.
     * 
     * @return GROUP_ID
     */
    public java.lang.String getGROUP_ID() {
        return GROUP_ID;
    }


    /**
     * Sets the GROUP_ID value for this ABSTRACTGROUPType.
     * 
     * @param GROUP_ID
     */
    public void setGROUP_ID(java.lang.String GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }


    /**
     * Gets the NAME value for this ABSTRACTGROUPType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTGROUPType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the ROLE value for this ABSTRACTGROUPType.
     * 
     * @return ROLE
     */
    public java.lang.String getROLE() {
        return ROLE;
    }


    /**
     * Sets the ROLE value for this ABSTRACTGROUPType.
     * 
     * @param ROLE
     */
    public void setROLE(java.lang.String ROLE) {
        this.ROLE = ROLE;
    }


    /**
     * Gets the EXTERNAL_ID value for this ABSTRACTGROUPType.
     * 
     * @return EXTERNAL_ID
     */
    public java.lang.String getEXTERNAL_ID() {
        return EXTERNAL_ID;
    }


    /**
     * Sets the EXTERNAL_ID value for this ABSTRACTGROUPType.
     * 
     * @param EXTERNAL_ID
     */
    public void setEXTERNAL_ID(java.lang.String EXTERNAL_ID) {
        this.EXTERNAL_ID = EXTERNAL_ID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTGROUPType)) return false;
        ABSTRACTGROUPType other = (ABSTRACTGROUPType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.GROUP_ID==null && other.getGROUP_ID()==null) || 
             (this.GROUP_ID!=null &&
              this.GROUP_ID.equals(other.getGROUP_ID()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.ROLE==null && other.getROLE()==null) || 
             (this.ROLE!=null &&
              this.ROLE.equals(other.getROLE()))) &&
            ((this.EXTERNAL_ID==null && other.getEXTERNAL_ID()==null) || 
             (this.EXTERNAL_ID!=null &&
              this.EXTERNAL_ID.equals(other.getEXTERNAL_ID())));
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
        if (getGROUP_ID() != null) {
            _hashCode += getGROUP_ID().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getROLE() != null) {
            _hashCode += getROLE().hashCode();
        }
        if (getEXTERNAL_ID() != null) {
            _hashCode += getEXTERNAL_ID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTGROUPType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GROUP_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "GROUP_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ROLE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ROLE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EXTERNAL_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EXTERNAL_ID"));
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

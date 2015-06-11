/**
 * ABSTRACTGROUP_MEMBERSHIPType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface;

public class ABSTRACTGROUP_MEMBERSHIPType  implements java.io.Serializable {
    private java.lang.String GROUP_ID;

    private java.lang.String MEMBERSHIP_ID;

    public ABSTRACTGROUP_MEMBERSHIPType() {
    }

    public ABSTRACTGROUP_MEMBERSHIPType(
           java.lang.String GROUP_ID,
           java.lang.String MEMBERSHIP_ID) {
           this.GROUP_ID = GROUP_ID;
           this.MEMBERSHIP_ID = MEMBERSHIP_ID;
    }


    /**
     * Gets the GROUP_ID value for this ABSTRACTGROUP_MEMBERSHIPType.
     * 
     * @return GROUP_ID
     */
    public java.lang.String getGROUP_ID() {
        return GROUP_ID;
    }


    /**
     * Sets the GROUP_ID value for this ABSTRACTGROUP_MEMBERSHIPType.
     * 
     * @param GROUP_ID
     */
    public void setGROUP_ID(java.lang.String GROUP_ID) {
        this.GROUP_ID = GROUP_ID;
    }


    /**
     * Gets the MEMBERSHIP_ID value for this ABSTRACTGROUP_MEMBERSHIPType.
     * 
     * @return MEMBERSHIP_ID
     */
    public java.lang.String getMEMBERSHIP_ID() {
        return MEMBERSHIP_ID;
    }


    /**
     * Sets the MEMBERSHIP_ID value for this ABSTRACTGROUP_MEMBERSHIPType.
     * 
     * @param MEMBERSHIP_ID
     */
    public void setMEMBERSHIP_ID(java.lang.String MEMBERSHIP_ID) {
        this.MEMBERSHIP_ID = MEMBERSHIP_ID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTGROUP_MEMBERSHIPType)) return false;
        ABSTRACTGROUP_MEMBERSHIPType other = (ABSTRACTGROUP_MEMBERSHIPType) obj;
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
            ((this.MEMBERSHIP_ID==null && other.getMEMBERSHIP_ID()==null) || 
             (this.MEMBERSHIP_ID!=null &&
              this.MEMBERSHIP_ID.equals(other.getMEMBERSHIP_ID())));
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
        if (getMEMBERSHIP_ID() != null) {
            _hashCode += getMEMBERSHIP_ID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTGROUP_MEMBERSHIPType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GROUP_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "GROUP_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MEMBERSHIP_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MEMBERSHIP_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
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

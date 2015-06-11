/**
 * ABSTRACTPORTLET_CATEGORYType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTPORTLET_CATEGORY.services.applic.uniface;

public class ABSTRACTPORTLET_CATEGORYType  implements java.io.Serializable {
    private java.lang.String PORTLET_CATEGORY_ID;

    private java.lang.String NAME;

    private java.lang.String DESCRIPTION;

    private java.lang.String ORDER;

    public ABSTRACTPORTLET_CATEGORYType() {
    }

    public ABSTRACTPORTLET_CATEGORYType(
           java.lang.String PORTLET_CATEGORY_ID,
           java.lang.String NAME,
           java.lang.String DESCRIPTION,
           java.lang.String ORDER) {
           this.PORTLET_CATEGORY_ID = PORTLET_CATEGORY_ID;
           this.NAME = NAME;
           this.DESCRIPTION = DESCRIPTION;
           this.ORDER = ORDER;
    }


    /**
     * Gets the PORTLET_CATEGORY_ID value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @return PORTLET_CATEGORY_ID
     */
    public java.lang.String getPORTLET_CATEGORY_ID() {
        return PORTLET_CATEGORY_ID;
    }


    /**
     * Sets the PORTLET_CATEGORY_ID value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @param PORTLET_CATEGORY_ID
     */
    public void setPORTLET_CATEGORY_ID(java.lang.String PORTLET_CATEGORY_ID) {
        this.PORTLET_CATEGORY_ID = PORTLET_CATEGORY_ID;
    }


    /**
     * Gets the NAME value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the ORDER value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @return ORDER
     */
    public java.lang.String getORDER() {
        return ORDER;
    }


    /**
     * Sets the ORDER value for this ABSTRACTPORTLET_CATEGORYType.
     * 
     * @param ORDER
     */
    public void setORDER(java.lang.String ORDER) {
        this.ORDER = ORDER;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTPORTLET_CATEGORYType)) return false;
        ABSTRACTPORTLET_CATEGORYType other = (ABSTRACTPORTLET_CATEGORYType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.PORTLET_CATEGORY_ID==null && other.getPORTLET_CATEGORY_ID()==null) || 
             (this.PORTLET_CATEGORY_ID!=null &&
              this.PORTLET_CATEGORY_ID.equals(other.getPORTLET_CATEGORY_ID()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION()))) &&
            ((this.ORDER==null && other.getORDER()==null) || 
             (this.ORDER!=null &&
              this.ORDER.equals(other.getORDER())));
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
        if (getPORTLET_CATEGORY_ID() != null) {
            _hashCode += getPORTLET_CATEGORY_ID().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        if (getORDER() != null) {
            _hashCode += getORDER().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTPORTLET_CATEGORYType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PORTLET_CATEGORY_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PORTLET_CATEGORY_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DESCRIPTION");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DESCRIPTION"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ORDER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ORDER"));
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

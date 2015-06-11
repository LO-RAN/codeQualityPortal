/**
 * ABSTRACTPORTAL_DOMAINType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTPORTAL_DOMAIN.services.applic.uniface;

public class ABSTRACTPORTAL_DOMAINType  implements java.io.Serializable {
    private java.lang.String PORTAL_DOMAIN_ID;

    private java.lang.String NAME;

    private java.lang.String DESCRIPTION;

    public ABSTRACTPORTAL_DOMAINType() {
    }

    public ABSTRACTPORTAL_DOMAINType(
           java.lang.String PORTAL_DOMAIN_ID,
           java.lang.String NAME,
           java.lang.String DESCRIPTION) {
           this.PORTAL_DOMAIN_ID = PORTAL_DOMAIN_ID;
           this.NAME = NAME;
           this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the PORTAL_DOMAIN_ID value for this ABSTRACTPORTAL_DOMAINType.
     * 
     * @return PORTAL_DOMAIN_ID
     */
    public java.lang.String getPORTAL_DOMAIN_ID() {
        return PORTAL_DOMAIN_ID;
    }


    /**
     * Sets the PORTAL_DOMAIN_ID value for this ABSTRACTPORTAL_DOMAINType.
     * 
     * @param PORTAL_DOMAIN_ID
     */
    public void setPORTAL_DOMAIN_ID(java.lang.String PORTAL_DOMAIN_ID) {
        this.PORTAL_DOMAIN_ID = PORTAL_DOMAIN_ID;
    }


    /**
     * Gets the NAME value for this ABSTRACTPORTAL_DOMAINType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTPORTAL_DOMAINType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTPORTAL_DOMAINType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTPORTAL_DOMAINType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTPORTAL_DOMAINType)) return false;
        ABSTRACTPORTAL_DOMAINType other = (ABSTRACTPORTAL_DOMAINType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.PORTAL_DOMAIN_ID==null && other.getPORTAL_DOMAIN_ID()==null) || 
             (this.PORTAL_DOMAIN_ID!=null &&
              this.PORTAL_DOMAIN_ID.equals(other.getPORTAL_DOMAIN_ID()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION())));
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
        if (getPORTAL_DOMAIN_ID() != null) {
            _hashCode += getPORTAL_DOMAIN_ID().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTPORTAL_DOMAINType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PORTAL_DOMAIN_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PORTAL_DOMAIN_ID"));
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

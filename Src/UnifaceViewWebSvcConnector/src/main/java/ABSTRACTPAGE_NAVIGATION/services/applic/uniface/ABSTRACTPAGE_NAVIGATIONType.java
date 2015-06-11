/**
 * ABSTRACTPAGE_NAVIGATIONType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTPAGE_NAVIGATION.services.applic.uniface;

public class ABSTRACTPAGE_NAVIGATIONType  implements java.io.Serializable {
    private java.lang.String PORTAL_PAGE_ID;

    private java.lang.String TYPE;

    private java.lang.String NAME;

    private java.lang.String DESCRIPTION;

    private java.lang.String BEFORE_LOGIN_ORDER;

    public ABSTRACTPAGE_NAVIGATIONType() {
    }

    public ABSTRACTPAGE_NAVIGATIONType(
           java.lang.String PORTAL_PAGE_ID,
           java.lang.String TYPE,
           java.lang.String NAME,
           java.lang.String DESCRIPTION,
           java.lang.String BEFORE_LOGIN_ORDER) {
           this.PORTAL_PAGE_ID = PORTAL_PAGE_ID;
           this.TYPE = TYPE;
           this.NAME = NAME;
           this.DESCRIPTION = DESCRIPTION;
           this.BEFORE_LOGIN_ORDER = BEFORE_LOGIN_ORDER;
    }


    /**
     * Gets the PORTAL_PAGE_ID value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @return PORTAL_PAGE_ID
     */
    public java.lang.String getPORTAL_PAGE_ID() {
        return PORTAL_PAGE_ID;
    }


    /**
     * Sets the PORTAL_PAGE_ID value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @param PORTAL_PAGE_ID
     */
    public void setPORTAL_PAGE_ID(java.lang.String PORTAL_PAGE_ID) {
        this.PORTAL_PAGE_ID = PORTAL_PAGE_ID;
    }


    /**
     * Gets the TYPE value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @return TYPE
     */
    public java.lang.String getTYPE() {
        return TYPE;
    }


    /**
     * Sets the TYPE value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @param TYPE
     */
    public void setTYPE(java.lang.String TYPE) {
        this.TYPE = TYPE;
    }


    /**
     * Gets the NAME value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the BEFORE_LOGIN_ORDER value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @return BEFORE_LOGIN_ORDER
     */
    public java.lang.String getBEFORE_LOGIN_ORDER() {
        return BEFORE_LOGIN_ORDER;
    }


    /**
     * Sets the BEFORE_LOGIN_ORDER value for this ABSTRACTPAGE_NAVIGATIONType.
     * 
     * @param BEFORE_LOGIN_ORDER
     */
    public void setBEFORE_LOGIN_ORDER(java.lang.String BEFORE_LOGIN_ORDER) {
        this.BEFORE_LOGIN_ORDER = BEFORE_LOGIN_ORDER;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTPAGE_NAVIGATIONType)) return false;
        ABSTRACTPAGE_NAVIGATIONType other = (ABSTRACTPAGE_NAVIGATIONType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.PORTAL_PAGE_ID==null && other.getPORTAL_PAGE_ID()==null) || 
             (this.PORTAL_PAGE_ID!=null &&
              this.PORTAL_PAGE_ID.equals(other.getPORTAL_PAGE_ID()))) &&
            ((this.TYPE==null && other.getTYPE()==null) || 
             (this.TYPE!=null &&
              this.TYPE.equals(other.getTYPE()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION()))) &&
            ((this.BEFORE_LOGIN_ORDER==null && other.getBEFORE_LOGIN_ORDER()==null) || 
             (this.BEFORE_LOGIN_ORDER!=null &&
              this.BEFORE_LOGIN_ORDER.equals(other.getBEFORE_LOGIN_ORDER())));
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
        if (getPORTAL_PAGE_ID() != null) {
            _hashCode += getPORTAL_PAGE_ID().hashCode();
        }
        if (getTYPE() != null) {
            _hashCode += getTYPE().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        if (getBEFORE_LOGIN_ORDER() != null) {
            _hashCode += getBEFORE_LOGIN_ORDER().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTPAGE_NAVIGATIONType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PORTAL_PAGE_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PORTAL_PAGE_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TYPE"));
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
        elemField.setFieldName("BEFORE_LOGIN_ORDER");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BEFORE_LOGIN_ORDER"));
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

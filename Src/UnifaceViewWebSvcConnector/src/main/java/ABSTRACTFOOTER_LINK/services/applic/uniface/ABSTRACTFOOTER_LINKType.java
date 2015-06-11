/**
 * ABSTRACTFOOTER_LINKType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTFOOTER_LINK.services.applic.uniface;

public class ABSTRACTFOOTER_LINKType  implements java.io.Serializable {
    private java.lang.String FOOTER_ID;

    private java.lang.String LINK_ID;

    private java.lang.String ORDER;

    private java.lang.String NAME;

    private java.lang.String DESCRIPTION;

    private java.lang.String LINK_URL;

    public ABSTRACTFOOTER_LINKType() {
    }

    public ABSTRACTFOOTER_LINKType(
           java.lang.String FOOTER_ID,
           java.lang.String LINK_ID,
           java.lang.String ORDER,
           java.lang.String NAME,
           java.lang.String DESCRIPTION,
           java.lang.String LINK_URL) {
           this.FOOTER_ID = FOOTER_ID;
           this.LINK_ID = LINK_ID;
           this.ORDER = ORDER;
           this.NAME = NAME;
           this.DESCRIPTION = DESCRIPTION;
           this.LINK_URL = LINK_URL;
    }


    /**
     * Gets the FOOTER_ID value for this ABSTRACTFOOTER_LINKType.
     * 
     * @return FOOTER_ID
     */
    public java.lang.String getFOOTER_ID() {
        return FOOTER_ID;
    }


    /**
     * Sets the FOOTER_ID value for this ABSTRACTFOOTER_LINKType.
     * 
     * @param FOOTER_ID
     */
    public void setFOOTER_ID(java.lang.String FOOTER_ID) {
        this.FOOTER_ID = FOOTER_ID;
    }


    /**
     * Gets the LINK_ID value for this ABSTRACTFOOTER_LINKType.
     * 
     * @return LINK_ID
     */
    public java.lang.String getLINK_ID() {
        return LINK_ID;
    }


    /**
     * Sets the LINK_ID value for this ABSTRACTFOOTER_LINKType.
     * 
     * @param LINK_ID
     */
    public void setLINK_ID(java.lang.String LINK_ID) {
        this.LINK_ID = LINK_ID;
    }


    /**
     * Gets the ORDER value for this ABSTRACTFOOTER_LINKType.
     * 
     * @return ORDER
     */
    public java.lang.String getORDER() {
        return ORDER;
    }


    /**
     * Sets the ORDER value for this ABSTRACTFOOTER_LINKType.
     * 
     * @param ORDER
     */
    public void setORDER(java.lang.String ORDER) {
        this.ORDER = ORDER;
    }


    /**
     * Gets the NAME value for this ABSTRACTFOOTER_LINKType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTFOOTER_LINKType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTFOOTER_LINKType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTFOOTER_LINKType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the LINK_URL value for this ABSTRACTFOOTER_LINKType.
     * 
     * @return LINK_URL
     */
    public java.lang.String getLINK_URL() {
        return LINK_URL;
    }


    /**
     * Sets the LINK_URL value for this ABSTRACTFOOTER_LINKType.
     * 
     * @param LINK_URL
     */
    public void setLINK_URL(java.lang.String LINK_URL) {
        this.LINK_URL = LINK_URL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTFOOTER_LINKType)) return false;
        ABSTRACTFOOTER_LINKType other = (ABSTRACTFOOTER_LINKType) obj;
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
            ((this.LINK_ID==null && other.getLINK_ID()==null) || 
             (this.LINK_ID!=null &&
              this.LINK_ID.equals(other.getLINK_ID()))) &&
            ((this.ORDER==null && other.getORDER()==null) || 
             (this.ORDER!=null &&
              this.ORDER.equals(other.getORDER()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION()))) &&
            ((this.LINK_URL==null && other.getLINK_URL()==null) || 
             (this.LINK_URL!=null &&
              this.LINK_URL.equals(other.getLINK_URL())));
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
        if (getLINK_ID() != null) {
            _hashCode += getLINK_ID().hashCode();
        }
        if (getORDER() != null) {
            _hashCode += getORDER().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        if (getLINK_URL() != null) {
            _hashCode += getLINK_URL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTFOOTER_LINKType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER_LINK", "ABSTRACTFOOTER_LINKType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FOOTER_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FOOTER_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LINK_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LINK_ID"));
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
        elemField.setFieldName("LINK_URL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LINK_URL"));
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

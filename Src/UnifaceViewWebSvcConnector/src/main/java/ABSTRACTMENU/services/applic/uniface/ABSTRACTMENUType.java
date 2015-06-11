/**
 * ABSTRACTMENUType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTMENU.services.applic.uniface;

public class ABSTRACTMENUType  implements java.io.Serializable {
    private java.lang.String MENU_ID;

    private java.lang.String MENUBAR_ID;

    private java.lang.String ORDER;

    private java.lang.String NAME;

    private java.lang.String LINK_DISPLAY_NAME;

    private java.lang.String LINK_MSG_TEXT;

    private java.lang.String LINK_URL;

    private java.lang.String LINK_NEW_WIN_FLAG;

    public ABSTRACTMENUType() {
    }

    public ABSTRACTMENUType(
           java.lang.String MENU_ID,
           java.lang.String MENUBAR_ID,
           java.lang.String ORDER,
           java.lang.String NAME,
           java.lang.String LINK_DISPLAY_NAME,
           java.lang.String LINK_MSG_TEXT,
           java.lang.String LINK_URL,
           java.lang.String LINK_NEW_WIN_FLAG) {
           this.MENU_ID = MENU_ID;
           this.MENUBAR_ID = MENUBAR_ID;
           this.ORDER = ORDER;
           this.NAME = NAME;
           this.LINK_DISPLAY_NAME = LINK_DISPLAY_NAME;
           this.LINK_MSG_TEXT = LINK_MSG_TEXT;
           this.LINK_URL = LINK_URL;
           this.LINK_NEW_WIN_FLAG = LINK_NEW_WIN_FLAG;
    }


    /**
     * Gets the MENU_ID value for this ABSTRACTMENUType.
     * 
     * @return MENU_ID
     */
    public java.lang.String getMENU_ID() {
        return MENU_ID;
    }


    /**
     * Sets the MENU_ID value for this ABSTRACTMENUType.
     * 
     * @param MENU_ID
     */
    public void setMENU_ID(java.lang.String MENU_ID) {
        this.MENU_ID = MENU_ID;
    }


    /**
     * Gets the MENUBAR_ID value for this ABSTRACTMENUType.
     * 
     * @return MENUBAR_ID
     */
    public java.lang.String getMENUBAR_ID() {
        return MENUBAR_ID;
    }


    /**
     * Sets the MENUBAR_ID value for this ABSTRACTMENUType.
     * 
     * @param MENUBAR_ID
     */
    public void setMENUBAR_ID(java.lang.String MENUBAR_ID) {
        this.MENUBAR_ID = MENUBAR_ID;
    }


    /**
     * Gets the ORDER value for this ABSTRACTMENUType.
     * 
     * @return ORDER
     */
    public java.lang.String getORDER() {
        return ORDER;
    }


    /**
     * Sets the ORDER value for this ABSTRACTMENUType.
     * 
     * @param ORDER
     */
    public void setORDER(java.lang.String ORDER) {
        this.ORDER = ORDER;
    }


    /**
     * Gets the NAME value for this ABSTRACTMENUType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTMENUType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the LINK_DISPLAY_NAME value for this ABSTRACTMENUType.
     * 
     * @return LINK_DISPLAY_NAME
     */
    public java.lang.String getLINK_DISPLAY_NAME() {
        return LINK_DISPLAY_NAME;
    }


    /**
     * Sets the LINK_DISPLAY_NAME value for this ABSTRACTMENUType.
     * 
     * @param LINK_DISPLAY_NAME
     */
    public void setLINK_DISPLAY_NAME(java.lang.String LINK_DISPLAY_NAME) {
        this.LINK_DISPLAY_NAME = LINK_DISPLAY_NAME;
    }


    /**
     * Gets the LINK_MSG_TEXT value for this ABSTRACTMENUType.
     * 
     * @return LINK_MSG_TEXT
     */
    public java.lang.String getLINK_MSG_TEXT() {
        return LINK_MSG_TEXT;
    }


    /**
     * Sets the LINK_MSG_TEXT value for this ABSTRACTMENUType.
     * 
     * @param LINK_MSG_TEXT
     */
    public void setLINK_MSG_TEXT(java.lang.String LINK_MSG_TEXT) {
        this.LINK_MSG_TEXT = LINK_MSG_TEXT;
    }


    /**
     * Gets the LINK_URL value for this ABSTRACTMENUType.
     * 
     * @return LINK_URL
     */
    public java.lang.String getLINK_URL() {
        return LINK_URL;
    }


    /**
     * Sets the LINK_URL value for this ABSTRACTMENUType.
     * 
     * @param LINK_URL
     */
    public void setLINK_URL(java.lang.String LINK_URL) {
        this.LINK_URL = LINK_URL;
    }


    /**
     * Gets the LINK_NEW_WIN_FLAG value for this ABSTRACTMENUType.
     * 
     * @return LINK_NEW_WIN_FLAG
     */
    public java.lang.String getLINK_NEW_WIN_FLAG() {
        return LINK_NEW_WIN_FLAG;
    }


    /**
     * Sets the LINK_NEW_WIN_FLAG value for this ABSTRACTMENUType.
     * 
     * @param LINK_NEW_WIN_FLAG
     */
    public void setLINK_NEW_WIN_FLAG(java.lang.String LINK_NEW_WIN_FLAG) {
        this.LINK_NEW_WIN_FLAG = LINK_NEW_WIN_FLAG;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTMENUType)) return false;
        ABSTRACTMENUType other = (ABSTRACTMENUType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.MENU_ID==null && other.getMENU_ID()==null) || 
             (this.MENU_ID!=null &&
              this.MENU_ID.equals(other.getMENU_ID()))) &&
            ((this.MENUBAR_ID==null && other.getMENUBAR_ID()==null) || 
             (this.MENUBAR_ID!=null &&
              this.MENUBAR_ID.equals(other.getMENUBAR_ID()))) &&
            ((this.ORDER==null && other.getORDER()==null) || 
             (this.ORDER!=null &&
              this.ORDER.equals(other.getORDER()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.LINK_DISPLAY_NAME==null && other.getLINK_DISPLAY_NAME()==null) || 
             (this.LINK_DISPLAY_NAME!=null &&
              this.LINK_DISPLAY_NAME.equals(other.getLINK_DISPLAY_NAME()))) &&
            ((this.LINK_MSG_TEXT==null && other.getLINK_MSG_TEXT()==null) || 
             (this.LINK_MSG_TEXT!=null &&
              this.LINK_MSG_TEXT.equals(other.getLINK_MSG_TEXT()))) &&
            ((this.LINK_URL==null && other.getLINK_URL()==null) || 
             (this.LINK_URL!=null &&
              this.LINK_URL.equals(other.getLINK_URL()))) &&
            ((this.LINK_NEW_WIN_FLAG==null && other.getLINK_NEW_WIN_FLAG()==null) || 
             (this.LINK_NEW_WIN_FLAG!=null &&
              this.LINK_NEW_WIN_FLAG.equals(other.getLINK_NEW_WIN_FLAG())));
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
        if (getMENU_ID() != null) {
            _hashCode += getMENU_ID().hashCode();
        }
        if (getMENUBAR_ID() != null) {
            _hashCode += getMENUBAR_ID().hashCode();
        }
        if (getORDER() != null) {
            _hashCode += getORDER().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getLINK_DISPLAY_NAME() != null) {
            _hashCode += getLINK_DISPLAY_NAME().hashCode();
        }
        if (getLINK_MSG_TEXT() != null) {
            _hashCode += getLINK_MSG_TEXT().hashCode();
        }
        if (getLINK_URL() != null) {
            _hashCode += getLINK_URL().hashCode();
        }
        if (getLINK_NEW_WIN_FLAG() != null) {
            _hashCode += getLINK_NEW_WIN_FLAG().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTMENUType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU", "ABSTRACTMENUType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MENU_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MENU_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MENUBAR_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MENUBAR_ID"));
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
        elemField.setFieldName("LINK_DISPLAY_NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LINK_DISPLAY_NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LINK_MSG_TEXT");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LINK_MSG_TEXT"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LINK_NEW_WIN_FLAG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LINK_NEW_WIN_FLAG"));
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

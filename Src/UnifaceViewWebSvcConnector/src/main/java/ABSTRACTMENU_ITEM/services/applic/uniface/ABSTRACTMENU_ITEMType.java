/**
 * ABSTRACTMENU_ITEMType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTMENU_ITEM.services.applic.uniface;

public class ABSTRACTMENU_ITEMType  implements java.io.Serializable {
    private java.lang.String MENUITEM_ID;

    private java.lang.String MENU_ID;

    private java.lang.String ORDER;

    private java.lang.String NAME;

    private java.lang.String LINK_DISPLAY_NAME;

    private java.lang.String LINK_URL;

    private java.lang.String LINK_NEW_WIN_FLAG;

    public ABSTRACTMENU_ITEMType() {
    }

    public ABSTRACTMENU_ITEMType(
           java.lang.String MENUITEM_ID,
           java.lang.String MENU_ID,
           java.lang.String ORDER,
           java.lang.String NAME,
           java.lang.String LINK_DISPLAY_NAME,
           java.lang.String LINK_URL,
           java.lang.String LINK_NEW_WIN_FLAG) {
           this.MENUITEM_ID = MENUITEM_ID;
           this.MENU_ID = MENU_ID;
           this.ORDER = ORDER;
           this.NAME = NAME;
           this.LINK_DISPLAY_NAME = LINK_DISPLAY_NAME;
           this.LINK_URL = LINK_URL;
           this.LINK_NEW_WIN_FLAG = LINK_NEW_WIN_FLAG;
    }


    /**
     * Gets the MENUITEM_ID value for this ABSTRACTMENU_ITEMType.
     * 
     * @return MENUITEM_ID
     */
    public java.lang.String getMENUITEM_ID() {
        return MENUITEM_ID;
    }


    /**
     * Sets the MENUITEM_ID value for this ABSTRACTMENU_ITEMType.
     * 
     * @param MENUITEM_ID
     */
    public void setMENUITEM_ID(java.lang.String MENUITEM_ID) {
        this.MENUITEM_ID = MENUITEM_ID;
    }


    /**
     * Gets the MENU_ID value for this ABSTRACTMENU_ITEMType.
     * 
     * @return MENU_ID
     */
    public java.lang.String getMENU_ID() {
        return MENU_ID;
    }


    /**
     * Sets the MENU_ID value for this ABSTRACTMENU_ITEMType.
     * 
     * @param MENU_ID
     */
    public void setMENU_ID(java.lang.String MENU_ID) {
        this.MENU_ID = MENU_ID;
    }


    /**
     * Gets the ORDER value for this ABSTRACTMENU_ITEMType.
     * 
     * @return ORDER
     */
    public java.lang.String getORDER() {
        return ORDER;
    }


    /**
     * Sets the ORDER value for this ABSTRACTMENU_ITEMType.
     * 
     * @param ORDER
     */
    public void setORDER(java.lang.String ORDER) {
        this.ORDER = ORDER;
    }


    /**
     * Gets the NAME value for this ABSTRACTMENU_ITEMType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTMENU_ITEMType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the LINK_DISPLAY_NAME value for this ABSTRACTMENU_ITEMType.
     * 
     * @return LINK_DISPLAY_NAME
     */
    public java.lang.String getLINK_DISPLAY_NAME() {
        return LINK_DISPLAY_NAME;
    }


    /**
     * Sets the LINK_DISPLAY_NAME value for this ABSTRACTMENU_ITEMType.
     * 
     * @param LINK_DISPLAY_NAME
     */
    public void setLINK_DISPLAY_NAME(java.lang.String LINK_DISPLAY_NAME) {
        this.LINK_DISPLAY_NAME = LINK_DISPLAY_NAME;
    }


    /**
     * Gets the LINK_URL value for this ABSTRACTMENU_ITEMType.
     * 
     * @return LINK_URL
     */
    public java.lang.String getLINK_URL() {
        return LINK_URL;
    }


    /**
     * Sets the LINK_URL value for this ABSTRACTMENU_ITEMType.
     * 
     * @param LINK_URL
     */
    public void setLINK_URL(java.lang.String LINK_URL) {
        this.LINK_URL = LINK_URL;
    }


    /**
     * Gets the LINK_NEW_WIN_FLAG value for this ABSTRACTMENU_ITEMType.
     * 
     * @return LINK_NEW_WIN_FLAG
     */
    public java.lang.String getLINK_NEW_WIN_FLAG() {
        return LINK_NEW_WIN_FLAG;
    }


    /**
     * Sets the LINK_NEW_WIN_FLAG value for this ABSTRACTMENU_ITEMType.
     * 
     * @param LINK_NEW_WIN_FLAG
     */
    public void setLINK_NEW_WIN_FLAG(java.lang.String LINK_NEW_WIN_FLAG) {
        this.LINK_NEW_WIN_FLAG = LINK_NEW_WIN_FLAG;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTMENU_ITEMType)) return false;
        ABSTRACTMENU_ITEMType other = (ABSTRACTMENU_ITEMType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.MENUITEM_ID==null && other.getMENUITEM_ID()==null) || 
             (this.MENUITEM_ID!=null &&
              this.MENUITEM_ID.equals(other.getMENUITEM_ID()))) &&
            ((this.MENU_ID==null && other.getMENU_ID()==null) || 
             (this.MENU_ID!=null &&
              this.MENU_ID.equals(other.getMENU_ID()))) &&
            ((this.ORDER==null && other.getORDER()==null) || 
             (this.ORDER!=null &&
              this.ORDER.equals(other.getORDER()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.LINK_DISPLAY_NAME==null && other.getLINK_DISPLAY_NAME()==null) || 
             (this.LINK_DISPLAY_NAME!=null &&
              this.LINK_DISPLAY_NAME.equals(other.getLINK_DISPLAY_NAME()))) &&
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
        if (getMENUITEM_ID() != null) {
            _hashCode += getMENUITEM_ID().hashCode();
        }
        if (getMENU_ID() != null) {
            _hashCode += getMENU_ID().hashCode();
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
        new org.apache.axis.description.TypeDesc(ABSTRACTMENU_ITEMType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_ITEM", "ABSTRACTMENU_ITEMType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MENUITEM_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MENUITEM_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MENU_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "MENU_ID"));
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

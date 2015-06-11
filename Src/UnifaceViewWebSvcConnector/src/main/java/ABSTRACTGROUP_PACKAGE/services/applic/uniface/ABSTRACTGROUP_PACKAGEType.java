/**
 * ABSTRACTGROUP_PACKAGEType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTGROUP_PACKAGE.services.applic.uniface;

public class ABSTRACTGROUP_PACKAGEType  implements java.io.Serializable {
    private java.lang.String GROUP_PACKAGE_ID;

    private java.lang.String NAME;

    private java.lang.String DESCRIPTION;

    private java.lang.String TYPE;

    private java.lang.String ACTIVE_FLAG;

    private java.lang.String DEFAULT_PAGE;

    private java.lang.String DEFAULT_THEME;

    private java.lang.String DATE_CREATE;

    private java.lang.String DATE_UPDATED;

    public ABSTRACTGROUP_PACKAGEType() {
    }

    public ABSTRACTGROUP_PACKAGEType(
           java.lang.String GROUP_PACKAGE_ID,
           java.lang.String NAME,
           java.lang.String DESCRIPTION,
           java.lang.String TYPE,
           java.lang.String ACTIVE_FLAG,
           java.lang.String DEFAULT_PAGE,
           java.lang.String DEFAULT_THEME,
           java.lang.String DATE_CREATE,
           java.lang.String DATE_UPDATED) {
           this.GROUP_PACKAGE_ID = GROUP_PACKAGE_ID;
           this.NAME = NAME;
           this.DESCRIPTION = DESCRIPTION;
           this.TYPE = TYPE;
           this.ACTIVE_FLAG = ACTIVE_FLAG;
           this.DEFAULT_PAGE = DEFAULT_PAGE;
           this.DEFAULT_THEME = DEFAULT_THEME;
           this.DATE_CREATE = DATE_CREATE;
           this.DATE_UPDATED = DATE_UPDATED;
    }


    /**
     * Gets the GROUP_PACKAGE_ID value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return GROUP_PACKAGE_ID
     */
    public java.lang.String getGROUP_PACKAGE_ID() {
        return GROUP_PACKAGE_ID;
    }


    /**
     * Sets the GROUP_PACKAGE_ID value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param GROUP_PACKAGE_ID
     */
    public void setGROUP_PACKAGE_ID(java.lang.String GROUP_PACKAGE_ID) {
        this.GROUP_PACKAGE_ID = GROUP_PACKAGE_ID;
    }


    /**
     * Gets the NAME value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return NAME
     */
    public java.lang.String getNAME() {
        return NAME;
    }


    /**
     * Sets the NAME value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param NAME
     */
    public void setNAME(java.lang.String NAME) {
        this.NAME = NAME;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the TYPE value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return TYPE
     */
    public java.lang.String getTYPE() {
        return TYPE;
    }


    /**
     * Sets the TYPE value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param TYPE
     */
    public void setTYPE(java.lang.String TYPE) {
        this.TYPE = TYPE;
    }


    /**
     * Gets the ACTIVE_FLAG value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return ACTIVE_FLAG
     */
    public java.lang.String getACTIVE_FLAG() {
        return ACTIVE_FLAG;
    }


    /**
     * Sets the ACTIVE_FLAG value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param ACTIVE_FLAG
     */
    public void setACTIVE_FLAG(java.lang.String ACTIVE_FLAG) {
        this.ACTIVE_FLAG = ACTIVE_FLAG;
    }


    /**
     * Gets the DEFAULT_PAGE value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return DEFAULT_PAGE
     */
    public java.lang.String getDEFAULT_PAGE() {
        return DEFAULT_PAGE;
    }


    /**
     * Sets the DEFAULT_PAGE value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param DEFAULT_PAGE
     */
    public void setDEFAULT_PAGE(java.lang.String DEFAULT_PAGE) {
        this.DEFAULT_PAGE = DEFAULT_PAGE;
    }


    /**
     * Gets the DEFAULT_THEME value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return DEFAULT_THEME
     */
    public java.lang.String getDEFAULT_THEME() {
        return DEFAULT_THEME;
    }


    /**
     * Sets the DEFAULT_THEME value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param DEFAULT_THEME
     */
    public void setDEFAULT_THEME(java.lang.String DEFAULT_THEME) {
        this.DEFAULT_THEME = DEFAULT_THEME;
    }


    /**
     * Gets the DATE_CREATE value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return DATE_CREATE
     */
    public java.lang.String getDATE_CREATE() {
        return DATE_CREATE;
    }


    /**
     * Sets the DATE_CREATE value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param DATE_CREATE
     */
    public void setDATE_CREATE(java.lang.String DATE_CREATE) {
        this.DATE_CREATE = DATE_CREATE;
    }


    /**
     * Gets the DATE_UPDATED value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @return DATE_UPDATED
     */
    public java.lang.String getDATE_UPDATED() {
        return DATE_UPDATED;
    }


    /**
     * Sets the DATE_UPDATED value for this ABSTRACTGROUP_PACKAGEType.
     * 
     * @param DATE_UPDATED
     */
    public void setDATE_UPDATED(java.lang.String DATE_UPDATED) {
        this.DATE_UPDATED = DATE_UPDATED;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTGROUP_PACKAGEType)) return false;
        ABSTRACTGROUP_PACKAGEType other = (ABSTRACTGROUP_PACKAGEType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.GROUP_PACKAGE_ID==null && other.getGROUP_PACKAGE_ID()==null) || 
             (this.GROUP_PACKAGE_ID!=null &&
              this.GROUP_PACKAGE_ID.equals(other.getGROUP_PACKAGE_ID()))) &&
            ((this.NAME==null && other.getNAME()==null) || 
             (this.NAME!=null &&
              this.NAME.equals(other.getNAME()))) &&
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION()))) &&
            ((this.TYPE==null && other.getTYPE()==null) || 
             (this.TYPE!=null &&
              this.TYPE.equals(other.getTYPE()))) &&
            ((this.ACTIVE_FLAG==null && other.getACTIVE_FLAG()==null) || 
             (this.ACTIVE_FLAG!=null &&
              this.ACTIVE_FLAG.equals(other.getACTIVE_FLAG()))) &&
            ((this.DEFAULT_PAGE==null && other.getDEFAULT_PAGE()==null) || 
             (this.DEFAULT_PAGE!=null &&
              this.DEFAULT_PAGE.equals(other.getDEFAULT_PAGE()))) &&
            ((this.DEFAULT_THEME==null && other.getDEFAULT_THEME()==null) || 
             (this.DEFAULT_THEME!=null &&
              this.DEFAULT_THEME.equals(other.getDEFAULT_THEME()))) &&
            ((this.DATE_CREATE==null && other.getDATE_CREATE()==null) || 
             (this.DATE_CREATE!=null &&
              this.DATE_CREATE.equals(other.getDATE_CREATE()))) &&
            ((this.DATE_UPDATED==null && other.getDATE_UPDATED()==null) || 
             (this.DATE_UPDATED!=null &&
              this.DATE_UPDATED.equals(other.getDATE_UPDATED())));
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
        if (getGROUP_PACKAGE_ID() != null) {
            _hashCode += getGROUP_PACKAGE_ID().hashCode();
        }
        if (getNAME() != null) {
            _hashCode += getNAME().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        if (getTYPE() != null) {
            _hashCode += getTYPE().hashCode();
        }
        if (getACTIVE_FLAG() != null) {
            _hashCode += getACTIVE_FLAG().hashCode();
        }
        if (getDEFAULT_PAGE() != null) {
            _hashCode += getDEFAULT_PAGE().hashCode();
        }
        if (getDEFAULT_THEME() != null) {
            _hashCode += getDEFAULT_THEME().hashCode();
        }
        if (getDATE_CREATE() != null) {
            _hashCode += getDATE_CREATE().hashCode();
        }
        if (getDATE_UPDATED() != null) {
            _hashCode += getDATE_UPDATED().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTGROUP_PACKAGEType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GROUP_PACKAGE_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "GROUP_PACKAGE_ID"));
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
        elemField.setFieldName("TYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ACTIVE_FLAG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACTIVE_FLAG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DEFAULT_PAGE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DEFAULT_PAGE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DEFAULT_THEME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DEFAULT_THEME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DATE_CREATE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DATE_CREATE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DATE_UPDATED");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DATE_UPDATED"));
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

/**
 * ABSTRACTPORTAL_PAGE_PORTLETType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface;

public class ABSTRACTPORTAL_PAGE_PORTLETType  implements java.io.Serializable {
    private java.lang.String PAGE_ID;

    private java.lang.String PORTLET_ID;

    private java.lang.String ORDER;

    private java.lang.String ACTIVE_FLAG;

    private java.lang.String COLUMN;

    private java.lang.String OWNER_ID;

    private java.lang.String STATE;

    public ABSTRACTPORTAL_PAGE_PORTLETType() {
    }

    public ABSTRACTPORTAL_PAGE_PORTLETType(
           java.lang.String PAGE_ID,
           java.lang.String PORTLET_ID,
           java.lang.String ORDER,
           java.lang.String ACTIVE_FLAG,
           java.lang.String COLUMN,
           java.lang.String OWNER_ID,
           java.lang.String STATE) {
           this.PAGE_ID = PAGE_ID;
           this.PORTLET_ID = PORTLET_ID;
           this.ORDER = ORDER;
           this.ACTIVE_FLAG = ACTIVE_FLAG;
           this.COLUMN = COLUMN;
           this.OWNER_ID = OWNER_ID;
           this.STATE = STATE;
    }


    /**
     * Gets the PAGE_ID value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return PAGE_ID
     */
    public java.lang.String getPAGE_ID() {
        return PAGE_ID;
    }


    /**
     * Sets the PAGE_ID value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param PAGE_ID
     */
    public void setPAGE_ID(java.lang.String PAGE_ID) {
        this.PAGE_ID = PAGE_ID;
    }


    /**
     * Gets the PORTLET_ID value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return PORTLET_ID
     */
    public java.lang.String getPORTLET_ID() {
        return PORTLET_ID;
    }


    /**
     * Sets the PORTLET_ID value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param PORTLET_ID
     */
    public void setPORTLET_ID(java.lang.String PORTLET_ID) {
        this.PORTLET_ID = PORTLET_ID;
    }


    /**
     * Gets the ORDER value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return ORDER
     */
    public java.lang.String getORDER() {
        return ORDER;
    }


    /**
     * Sets the ORDER value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param ORDER
     */
    public void setORDER(java.lang.String ORDER) {
        this.ORDER = ORDER;
    }


    /**
     * Gets the ACTIVE_FLAG value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return ACTIVE_FLAG
     */
    public java.lang.String getACTIVE_FLAG() {
        return ACTIVE_FLAG;
    }


    /**
     * Sets the ACTIVE_FLAG value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param ACTIVE_FLAG
     */
    public void setACTIVE_FLAG(java.lang.String ACTIVE_FLAG) {
        this.ACTIVE_FLAG = ACTIVE_FLAG;
    }


    /**
     * Gets the COLUMN value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return COLUMN
     */
    public java.lang.String getCOLUMN() {
        return COLUMN;
    }


    /**
     * Sets the COLUMN value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param COLUMN
     */
    public void setCOLUMN(java.lang.String COLUMN) {
        this.COLUMN = COLUMN;
    }


    /**
     * Gets the OWNER_ID value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return OWNER_ID
     */
    public java.lang.String getOWNER_ID() {
        return OWNER_ID;
    }


    /**
     * Sets the OWNER_ID value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param OWNER_ID
     */
    public void setOWNER_ID(java.lang.String OWNER_ID) {
        this.OWNER_ID = OWNER_ID;
    }


    /**
     * Gets the STATE value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @return STATE
     */
    public java.lang.String getSTATE() {
        return STATE;
    }


    /**
     * Sets the STATE value for this ABSTRACTPORTAL_PAGE_PORTLETType.
     * 
     * @param STATE
     */
    public void setSTATE(java.lang.String STATE) {
        this.STATE = STATE;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTPORTAL_PAGE_PORTLETType)) return false;
        ABSTRACTPORTAL_PAGE_PORTLETType other = (ABSTRACTPORTAL_PAGE_PORTLETType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.PAGE_ID==null && other.getPAGE_ID()==null) || 
             (this.PAGE_ID!=null &&
              this.PAGE_ID.equals(other.getPAGE_ID()))) &&
            ((this.PORTLET_ID==null && other.getPORTLET_ID()==null) || 
             (this.PORTLET_ID!=null &&
              this.PORTLET_ID.equals(other.getPORTLET_ID()))) &&
            ((this.ORDER==null && other.getORDER()==null) || 
             (this.ORDER!=null &&
              this.ORDER.equals(other.getORDER()))) &&
            ((this.ACTIVE_FLAG==null && other.getACTIVE_FLAG()==null) || 
             (this.ACTIVE_FLAG!=null &&
              this.ACTIVE_FLAG.equals(other.getACTIVE_FLAG()))) &&
            ((this.COLUMN==null && other.getCOLUMN()==null) || 
             (this.COLUMN!=null &&
              this.COLUMN.equals(other.getCOLUMN()))) &&
            ((this.OWNER_ID==null && other.getOWNER_ID()==null) || 
             (this.OWNER_ID!=null &&
              this.OWNER_ID.equals(other.getOWNER_ID()))) &&
            ((this.STATE==null && other.getSTATE()==null) || 
             (this.STATE!=null &&
              this.STATE.equals(other.getSTATE())));
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
        if (getPAGE_ID() != null) {
            _hashCode += getPAGE_ID().hashCode();
        }
        if (getPORTLET_ID() != null) {
            _hashCode += getPORTLET_ID().hashCode();
        }
        if (getORDER() != null) {
            _hashCode += getORDER().hashCode();
        }
        if (getACTIVE_FLAG() != null) {
            _hashCode += getACTIVE_FLAG().hashCode();
        }
        if (getCOLUMN() != null) {
            _hashCode += getCOLUMN().hashCode();
        }
        if (getOWNER_ID() != null) {
            _hashCode += getOWNER_ID().hashCode();
        }
        if (getSTATE() != null) {
            _hashCode += getSTATE().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTPORTAL_PAGE_PORTLETType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PAGE_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PAGE_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PORTLET_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "PORTLET_ID"));
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
        elemField.setFieldName("ACTIVE_FLAG");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ACTIVE_FLAG"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("COLUMN");
        elemField.setXmlName(new javax.xml.namespace.QName("", "COLUMN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OWNER_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OWNER_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("STATE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "STATE"));
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

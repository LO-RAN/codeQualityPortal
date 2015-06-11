/**
 * ABSTRACTJOB_TRIGGERType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTJOB_TRIGGER.services.applic.uniface;

public class ABSTRACTJOB_TRIGGERType  implements java.io.Serializable {
    private java.lang.String TRIGGER_NAME;

    private java.lang.String TRIGGER_GROUP;

    private java.lang.String JOB_NAME;

    private java.lang.String JOB_GROUP;

    private java.lang.String IS_VOLATILE;

    private java.lang.String TRIGGER_TYPE;

    private java.lang.String DESCRIPTION;

    private java.lang.String START_TIME;

    private java.lang.String END_TIME;

    private java.lang.String CRON_EXPRESSION;

    public ABSTRACTJOB_TRIGGERType() {
    }

    public ABSTRACTJOB_TRIGGERType(
           java.lang.String TRIGGER_NAME,
           java.lang.String TRIGGER_GROUP,
           java.lang.String JOB_NAME,
           java.lang.String JOB_GROUP,
           java.lang.String IS_VOLATILE,
           java.lang.String TRIGGER_TYPE,
           java.lang.String DESCRIPTION,
           java.lang.String START_TIME,
           java.lang.String END_TIME,
           java.lang.String CRON_EXPRESSION) {
           this.TRIGGER_NAME = TRIGGER_NAME;
           this.TRIGGER_GROUP = TRIGGER_GROUP;
           this.JOB_NAME = JOB_NAME;
           this.JOB_GROUP = JOB_GROUP;
           this.IS_VOLATILE = IS_VOLATILE;
           this.TRIGGER_TYPE = TRIGGER_TYPE;
           this.DESCRIPTION = DESCRIPTION;
           this.START_TIME = START_TIME;
           this.END_TIME = END_TIME;
           this.CRON_EXPRESSION = CRON_EXPRESSION;
    }


    /**
     * Gets the TRIGGER_NAME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return TRIGGER_NAME
     */
    public java.lang.String getTRIGGER_NAME() {
        return TRIGGER_NAME;
    }


    /**
     * Sets the TRIGGER_NAME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param TRIGGER_NAME
     */
    public void setTRIGGER_NAME(java.lang.String TRIGGER_NAME) {
        this.TRIGGER_NAME = TRIGGER_NAME;
    }


    /**
     * Gets the TRIGGER_GROUP value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return TRIGGER_GROUP
     */
    public java.lang.String getTRIGGER_GROUP() {
        return TRIGGER_GROUP;
    }


    /**
     * Sets the TRIGGER_GROUP value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param TRIGGER_GROUP
     */
    public void setTRIGGER_GROUP(java.lang.String TRIGGER_GROUP) {
        this.TRIGGER_GROUP = TRIGGER_GROUP;
    }


    /**
     * Gets the JOB_NAME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return JOB_NAME
     */
    public java.lang.String getJOB_NAME() {
        return JOB_NAME;
    }


    /**
     * Sets the JOB_NAME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param JOB_NAME
     */
    public void setJOB_NAME(java.lang.String JOB_NAME) {
        this.JOB_NAME = JOB_NAME;
    }


    /**
     * Gets the JOB_GROUP value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return JOB_GROUP
     */
    public java.lang.String getJOB_GROUP() {
        return JOB_GROUP;
    }


    /**
     * Sets the JOB_GROUP value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param JOB_GROUP
     */
    public void setJOB_GROUP(java.lang.String JOB_GROUP) {
        this.JOB_GROUP = JOB_GROUP;
    }


    /**
     * Gets the IS_VOLATILE value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return IS_VOLATILE
     */
    public java.lang.String getIS_VOLATILE() {
        return IS_VOLATILE;
    }


    /**
     * Sets the IS_VOLATILE value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param IS_VOLATILE
     */
    public void setIS_VOLATILE(java.lang.String IS_VOLATILE) {
        this.IS_VOLATILE = IS_VOLATILE;
    }


    /**
     * Gets the TRIGGER_TYPE value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return TRIGGER_TYPE
     */
    public java.lang.String getTRIGGER_TYPE() {
        return TRIGGER_TYPE;
    }


    /**
     * Sets the TRIGGER_TYPE value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param TRIGGER_TYPE
     */
    public void setTRIGGER_TYPE(java.lang.String TRIGGER_TYPE) {
        this.TRIGGER_TYPE = TRIGGER_TYPE;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the START_TIME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return START_TIME
     */
    public java.lang.String getSTART_TIME() {
        return START_TIME;
    }


    /**
     * Sets the START_TIME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param START_TIME
     */
    public void setSTART_TIME(java.lang.String START_TIME) {
        this.START_TIME = START_TIME;
    }


    /**
     * Gets the END_TIME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return END_TIME
     */
    public java.lang.String getEND_TIME() {
        return END_TIME;
    }


    /**
     * Sets the END_TIME value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param END_TIME
     */
    public void setEND_TIME(java.lang.String END_TIME) {
        this.END_TIME = END_TIME;
    }


    /**
     * Gets the CRON_EXPRESSION value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @return CRON_EXPRESSION
     */
    public java.lang.String getCRON_EXPRESSION() {
        return CRON_EXPRESSION;
    }


    /**
     * Sets the CRON_EXPRESSION value for this ABSTRACTJOB_TRIGGERType.
     * 
     * @param CRON_EXPRESSION
     */
    public void setCRON_EXPRESSION(java.lang.String CRON_EXPRESSION) {
        this.CRON_EXPRESSION = CRON_EXPRESSION;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTJOB_TRIGGERType)) return false;
        ABSTRACTJOB_TRIGGERType other = (ABSTRACTJOB_TRIGGERType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.TRIGGER_NAME==null && other.getTRIGGER_NAME()==null) || 
             (this.TRIGGER_NAME!=null &&
              this.TRIGGER_NAME.equals(other.getTRIGGER_NAME()))) &&
            ((this.TRIGGER_GROUP==null && other.getTRIGGER_GROUP()==null) || 
             (this.TRIGGER_GROUP!=null &&
              this.TRIGGER_GROUP.equals(other.getTRIGGER_GROUP()))) &&
            ((this.JOB_NAME==null && other.getJOB_NAME()==null) || 
             (this.JOB_NAME!=null &&
              this.JOB_NAME.equals(other.getJOB_NAME()))) &&
            ((this.JOB_GROUP==null && other.getJOB_GROUP()==null) || 
             (this.JOB_GROUP!=null &&
              this.JOB_GROUP.equals(other.getJOB_GROUP()))) &&
            ((this.IS_VOLATILE==null && other.getIS_VOLATILE()==null) || 
             (this.IS_VOLATILE!=null &&
              this.IS_VOLATILE.equals(other.getIS_VOLATILE()))) &&
            ((this.TRIGGER_TYPE==null && other.getTRIGGER_TYPE()==null) || 
             (this.TRIGGER_TYPE!=null &&
              this.TRIGGER_TYPE.equals(other.getTRIGGER_TYPE()))) &&
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION()))) &&
            ((this.START_TIME==null && other.getSTART_TIME()==null) || 
             (this.START_TIME!=null &&
              this.START_TIME.equals(other.getSTART_TIME()))) &&
            ((this.END_TIME==null && other.getEND_TIME()==null) || 
             (this.END_TIME!=null &&
              this.END_TIME.equals(other.getEND_TIME()))) &&
            ((this.CRON_EXPRESSION==null && other.getCRON_EXPRESSION()==null) || 
             (this.CRON_EXPRESSION!=null &&
              this.CRON_EXPRESSION.equals(other.getCRON_EXPRESSION())));
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
        if (getTRIGGER_NAME() != null) {
            _hashCode += getTRIGGER_NAME().hashCode();
        }
        if (getTRIGGER_GROUP() != null) {
            _hashCode += getTRIGGER_GROUP().hashCode();
        }
        if (getJOB_NAME() != null) {
            _hashCode += getJOB_NAME().hashCode();
        }
        if (getJOB_GROUP() != null) {
            _hashCode += getJOB_GROUP().hashCode();
        }
        if (getIS_VOLATILE() != null) {
            _hashCode += getIS_VOLATILE().hashCode();
        }
        if (getTRIGGER_TYPE() != null) {
            _hashCode += getTRIGGER_TYPE().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        if (getSTART_TIME() != null) {
            _hashCode += getSTART_TIME().hashCode();
        }
        if (getEND_TIME() != null) {
            _hashCode += getEND_TIME().hashCode();
        }
        if (getCRON_EXPRESSION() != null) {
            _hashCode += getCRON_EXPRESSION().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTJOB_TRIGGERType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_TRIGGER", "ABSTRACTJOB_TRIGGERType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TRIGGER_NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TRIGGER_NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TRIGGER_GROUP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TRIGGER_GROUP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("JOB_NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JOB_NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("JOB_GROUP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JOB_GROUP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IS_VOLATILE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IS_VOLATILE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TRIGGER_TYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TRIGGER_TYPE"));
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
        elemField.setFieldName("START_TIME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "START_TIME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("END_TIME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "END_TIME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CRON_EXPRESSION");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CRON_EXPRESSION"));
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

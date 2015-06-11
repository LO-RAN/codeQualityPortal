/**
 * ABSTRACTJOB_DETAILType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTJOB_DETAIL.services.applic.uniface;

public class ABSTRACTJOB_DETAILType  implements java.io.Serializable {
    private java.lang.String JOB_NAME;

    private java.lang.String JOB_GROUP;

    private java.lang.String JOB_CLASS_NAME;

    private java.lang.String IS_DURABLE;

    private java.lang.String IS_VOLITILE;

    private java.lang.String IS_STATEFUL;

    private java.lang.String REQUESTS_RECOVERY;

    private java.lang.String DESCRIPTION;

    public ABSTRACTJOB_DETAILType() {
    }

    public ABSTRACTJOB_DETAILType(
           java.lang.String JOB_NAME,
           java.lang.String JOB_GROUP,
           java.lang.String JOB_CLASS_NAME,
           java.lang.String IS_DURABLE,
           java.lang.String IS_VOLITILE,
           java.lang.String IS_STATEFUL,
           java.lang.String REQUESTS_RECOVERY,
           java.lang.String DESCRIPTION) {
           this.JOB_NAME = JOB_NAME;
           this.JOB_GROUP = JOB_GROUP;
           this.JOB_CLASS_NAME = JOB_CLASS_NAME;
           this.IS_DURABLE = IS_DURABLE;
           this.IS_VOLITILE = IS_VOLITILE;
           this.IS_STATEFUL = IS_STATEFUL;
           this.REQUESTS_RECOVERY = REQUESTS_RECOVERY;
           this.DESCRIPTION = DESCRIPTION;
    }


    /**
     * Gets the JOB_NAME value for this ABSTRACTJOB_DETAILType.
     * 
     * @return JOB_NAME
     */
    public java.lang.String getJOB_NAME() {
        return JOB_NAME;
    }


    /**
     * Sets the JOB_NAME value for this ABSTRACTJOB_DETAILType.
     * 
     * @param JOB_NAME
     */
    public void setJOB_NAME(java.lang.String JOB_NAME) {
        this.JOB_NAME = JOB_NAME;
    }


    /**
     * Gets the JOB_GROUP value for this ABSTRACTJOB_DETAILType.
     * 
     * @return JOB_GROUP
     */
    public java.lang.String getJOB_GROUP() {
        return JOB_GROUP;
    }


    /**
     * Sets the JOB_GROUP value for this ABSTRACTJOB_DETAILType.
     * 
     * @param JOB_GROUP
     */
    public void setJOB_GROUP(java.lang.String JOB_GROUP) {
        this.JOB_GROUP = JOB_GROUP;
    }


    /**
     * Gets the JOB_CLASS_NAME value for this ABSTRACTJOB_DETAILType.
     * 
     * @return JOB_CLASS_NAME
     */
    public java.lang.String getJOB_CLASS_NAME() {
        return JOB_CLASS_NAME;
    }


    /**
     * Sets the JOB_CLASS_NAME value for this ABSTRACTJOB_DETAILType.
     * 
     * @param JOB_CLASS_NAME
     */
    public void setJOB_CLASS_NAME(java.lang.String JOB_CLASS_NAME) {
        this.JOB_CLASS_NAME = JOB_CLASS_NAME;
    }


    /**
     * Gets the IS_DURABLE value for this ABSTRACTJOB_DETAILType.
     * 
     * @return IS_DURABLE
     */
    public java.lang.String getIS_DURABLE() {
        return IS_DURABLE;
    }


    /**
     * Sets the IS_DURABLE value for this ABSTRACTJOB_DETAILType.
     * 
     * @param IS_DURABLE
     */
    public void setIS_DURABLE(java.lang.String IS_DURABLE) {
        this.IS_DURABLE = IS_DURABLE;
    }


    /**
     * Gets the IS_VOLITILE value for this ABSTRACTJOB_DETAILType.
     * 
     * @return IS_VOLITILE
     */
    public java.lang.String getIS_VOLITILE() {
        return IS_VOLITILE;
    }


    /**
     * Sets the IS_VOLITILE value for this ABSTRACTJOB_DETAILType.
     * 
     * @param IS_VOLITILE
     */
    public void setIS_VOLITILE(java.lang.String IS_VOLITILE) {
        this.IS_VOLITILE = IS_VOLITILE;
    }


    /**
     * Gets the IS_STATEFUL value for this ABSTRACTJOB_DETAILType.
     * 
     * @return IS_STATEFUL
     */
    public java.lang.String getIS_STATEFUL() {
        return IS_STATEFUL;
    }


    /**
     * Sets the IS_STATEFUL value for this ABSTRACTJOB_DETAILType.
     * 
     * @param IS_STATEFUL
     */
    public void setIS_STATEFUL(java.lang.String IS_STATEFUL) {
        this.IS_STATEFUL = IS_STATEFUL;
    }


    /**
     * Gets the REQUESTS_RECOVERY value for this ABSTRACTJOB_DETAILType.
     * 
     * @return REQUESTS_RECOVERY
     */
    public java.lang.String getREQUESTS_RECOVERY() {
        return REQUESTS_RECOVERY;
    }


    /**
     * Sets the REQUESTS_RECOVERY value for this ABSTRACTJOB_DETAILType.
     * 
     * @param REQUESTS_RECOVERY
     */
    public void setREQUESTS_RECOVERY(java.lang.String REQUESTS_RECOVERY) {
        this.REQUESTS_RECOVERY = REQUESTS_RECOVERY;
    }


    /**
     * Gets the DESCRIPTION value for this ABSTRACTJOB_DETAILType.
     * 
     * @return DESCRIPTION
     */
    public java.lang.String getDESCRIPTION() {
        return DESCRIPTION;
    }


    /**
     * Sets the DESCRIPTION value for this ABSTRACTJOB_DETAILType.
     * 
     * @param DESCRIPTION
     */
    public void setDESCRIPTION(java.lang.String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTJOB_DETAILType)) return false;
        ABSTRACTJOB_DETAILType other = (ABSTRACTJOB_DETAILType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.JOB_NAME==null && other.getJOB_NAME()==null) || 
             (this.JOB_NAME!=null &&
              this.JOB_NAME.equals(other.getJOB_NAME()))) &&
            ((this.JOB_GROUP==null && other.getJOB_GROUP()==null) || 
             (this.JOB_GROUP!=null &&
              this.JOB_GROUP.equals(other.getJOB_GROUP()))) &&
            ((this.JOB_CLASS_NAME==null && other.getJOB_CLASS_NAME()==null) || 
             (this.JOB_CLASS_NAME!=null &&
              this.JOB_CLASS_NAME.equals(other.getJOB_CLASS_NAME()))) &&
            ((this.IS_DURABLE==null && other.getIS_DURABLE()==null) || 
             (this.IS_DURABLE!=null &&
              this.IS_DURABLE.equals(other.getIS_DURABLE()))) &&
            ((this.IS_VOLITILE==null && other.getIS_VOLITILE()==null) || 
             (this.IS_VOLITILE!=null &&
              this.IS_VOLITILE.equals(other.getIS_VOLITILE()))) &&
            ((this.IS_STATEFUL==null && other.getIS_STATEFUL()==null) || 
             (this.IS_STATEFUL!=null &&
              this.IS_STATEFUL.equals(other.getIS_STATEFUL()))) &&
            ((this.REQUESTS_RECOVERY==null && other.getREQUESTS_RECOVERY()==null) || 
             (this.REQUESTS_RECOVERY!=null &&
              this.REQUESTS_RECOVERY.equals(other.getREQUESTS_RECOVERY()))) &&
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
        if (getJOB_NAME() != null) {
            _hashCode += getJOB_NAME().hashCode();
        }
        if (getJOB_GROUP() != null) {
            _hashCode += getJOB_GROUP().hashCode();
        }
        if (getJOB_CLASS_NAME() != null) {
            _hashCode += getJOB_CLASS_NAME().hashCode();
        }
        if (getIS_DURABLE() != null) {
            _hashCode += getIS_DURABLE().hashCode();
        }
        if (getIS_VOLITILE() != null) {
            _hashCode += getIS_VOLITILE().hashCode();
        }
        if (getIS_STATEFUL() != null) {
            _hashCode += getIS_STATEFUL().hashCode();
        }
        if (getREQUESTS_RECOVERY() != null) {
            _hashCode += getREQUESTS_RECOVERY().hashCode();
        }
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTJOB_DETAILType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_DETAIL", "ABSTRACTJOB_DETAILType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("JOB_CLASS_NAME");
        elemField.setXmlName(new javax.xml.namespace.QName("", "JOB_CLASS_NAME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IS_DURABLE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IS_DURABLE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IS_VOLITILE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IS_VOLITILE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IS_STATEFUL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "IS_STATEFUL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("REQUESTS_RECOVERY");
        elemField.setXmlName(new javax.xml.namespace.QName("", "REQUESTS_RECOVERY"));
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

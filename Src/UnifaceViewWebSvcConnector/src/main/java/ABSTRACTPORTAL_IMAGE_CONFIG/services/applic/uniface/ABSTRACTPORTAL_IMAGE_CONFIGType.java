/**
 * ABSTRACTPORTAL_IMAGE_CONFIGType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface;

public class ABSTRACTPORTAL_IMAGE_CONFIGType  implements java.io.Serializable {
    private java.lang.String ID;

    private java.lang.String WEB_PATH;

    private java.lang.String FILE_PATH;

    public ABSTRACTPORTAL_IMAGE_CONFIGType() {
    }

    public ABSTRACTPORTAL_IMAGE_CONFIGType(
           java.lang.String ID,
           java.lang.String WEB_PATH,
           java.lang.String FILE_PATH) {
           this.ID = ID;
           this.WEB_PATH = WEB_PATH;
           this.FILE_PATH = FILE_PATH;
    }


    /**
     * Gets the ID value for this ABSTRACTPORTAL_IMAGE_CONFIGType.
     * 
     * @return ID
     */
    public java.lang.String getID() {
        return ID;
    }


    /**
     * Sets the ID value for this ABSTRACTPORTAL_IMAGE_CONFIGType.
     * 
     * @param ID
     */
    public void setID(java.lang.String ID) {
        this.ID = ID;
    }


    /**
     * Gets the WEB_PATH value for this ABSTRACTPORTAL_IMAGE_CONFIGType.
     * 
     * @return WEB_PATH
     */
    public java.lang.String getWEB_PATH() {
        return WEB_PATH;
    }


    /**
     * Sets the WEB_PATH value for this ABSTRACTPORTAL_IMAGE_CONFIGType.
     * 
     * @param WEB_PATH
     */
    public void setWEB_PATH(java.lang.String WEB_PATH) {
        this.WEB_PATH = WEB_PATH;
    }


    /**
     * Gets the FILE_PATH value for this ABSTRACTPORTAL_IMAGE_CONFIGType.
     * 
     * @return FILE_PATH
     */
    public java.lang.String getFILE_PATH() {
        return FILE_PATH;
    }


    /**
     * Sets the FILE_PATH value for this ABSTRACTPORTAL_IMAGE_CONFIGType.
     * 
     * @param FILE_PATH
     */
    public void setFILE_PATH(java.lang.String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ABSTRACTPORTAL_IMAGE_CONFIGType)) return false;
        ABSTRACTPORTAL_IMAGE_CONFIGType other = (ABSTRACTPORTAL_IMAGE_CONFIGType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.WEB_PATH==null && other.getWEB_PATH()==null) || 
             (this.WEB_PATH!=null &&
              this.WEB_PATH.equals(other.getWEB_PATH()))) &&
            ((this.FILE_PATH==null && other.getFILE_PATH()==null) || 
             (this.FILE_PATH!=null &&
              this.FILE_PATH.equals(other.getFILE_PATH())));
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
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getWEB_PATH() != null) {
            _hashCode += getWEB_PATH().hashCode();
        }
        if (getFILE_PATH() != null) {
            _hashCode += getFILE_PATH().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ABSTRACTPORTAL_IMAGE_CONFIGType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_IMAGE_CONFIG", "ABSTRACTPORTAL_IMAGE_CONFIGType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WEB_PATH");
        elemField.setXmlName(new javax.xml.namespace.QName("", "WEB_PATH"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FILE_PATH");
        elemField.setXmlName(new javax.xml.namespace.QName("", "FILE_PATH"));
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


package com.compuware.caqs.webservices.projectslist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for projectWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="projectWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fullPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lib" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "projectWS", propOrder = {
    "fullPath",
    "id",
    "lib"
})
public class ProjectWS {

    protected String fullPath;
    protected String id;
    protected String lib;

    /**
     * Gets the value of the fullPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * Sets the value of the fullPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFullPath(String value) {
        this.fullPath = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the lib property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLib() {
        return lib;
    }

    /**
     * Sets the value of the lib property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLib(String value) {
        this.lib = value;
    }
    
    @Override
    public String toString() {
    	return this.getFullPath();
    }

}

/*
 * © 2002 Compuware Corporation. All rights reserved.
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 */

/* Disclaimer
 * You have a royalty-free right to use, modify, reproduce and distribute this 
 * sample code (and/or any modified version) in any way you find useful,
 * provided that you agree that Compuware has no warranty obligations or 
 * liability for any sample code which has been modified.
 */
package com.compuware.optimal.flow.webconsole;

public class ParameterItemBean {

   private String name;
   private String datatype;
   private String value;

   public ParameterItemBean() {
      this.name = "";
      this.datatype = "";
   }

   public void setName(String name) {
	   this.name = name;
   }

   public String getName() {
	   return this.name;
   }

   public void setDataType(String datatype) {
	   this.datatype = datatype;
   }

   public String getDataType() {
	   return this.datatype;
   }

   public void setValue(String value) {
	   this.value = value;
   }

   public String getValue() {
	   return this.value;
   }
}


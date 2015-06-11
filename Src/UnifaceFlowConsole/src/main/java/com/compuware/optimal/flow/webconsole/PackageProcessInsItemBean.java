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


/**
 * This class contains information about each item of the Package->ProcessDef->Process tree.
 * The child information related to this item is stored in the Collection
 * ppbeans holding <strong>PackageProcessInsItemsBean</strong> objects.
 *
 * @see ActivityInstanceListAction
 * @see PackageProcessInsItemsBean
 */

public class PackageProcessInsItemBean {

   /**
    * The time the related workflow item was created.
    */
   private String creationtime;

   /**
    * The id of this item.
    */
   private String id;

   /**
    * The name of this item.
    */
   private String name;

   /**
    * The entityname of this item.
    */
   private String entityname;

   /**
    * The var1 process variable this item.
    */
   private String var1;

   /**
    * The Collection with child information.
    */
   private PackageProcessInsItemsBean ppbeans;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */
   public PackageProcessInsItemBean() {
      this.creationtime = "";
      this.entityname = "";
      this.id = "";
      this.name = "";
      this.var1 = "";
      this.ppbeans = null;
   }

   /**
    * Set the creation time.
    */
   public void setCreationTime(String creationtime) {
	   this.creationtime = creationtime;
   }

   /**
    * Get the creation time.
    */
   public String getCreationTime() {
	   return this.creationtime;
   }

   /**
    * Set the ID of this item.
    */
   public void setId(String id) {
	   this.id = id;
   }

   /**
    * Get the ID of this item.
    */
   public String getId() {
	   return this.id;
   }

   /**
    * Set the name of this item.
    */

   public void setName(String name) {
	   this.name = name;
   }

   /**
    * Get the name of this item.
    */

   public String getName() {
	   return this.name;
   }

   /**
    * Set the entity name of this item,
    */

   public void setEntityName(String entityname) {
	   this.entityname = entityname;
   }

   /**
    * Get the entity name of this item,
    */

   public String getEntityName() {
	   return this.entityname;
   }

   /**
    * Set var1 of this item.
    */
   public void setVar1(String var1) {
	   this.var1 = var1;
   }

   /**
    * Get var1 of this item.
    */
   public String getVar1() {
	   return this.var1;
   }

   /**
    * Set the ppbeans Collection that holds
    * <strong>PackageProcessInstItemsBean</strong> objects.
    */

   public void setPpbeans(PackageProcessInsItemsBean ppbeans) {
	   this.ppbeans = ppbeans;
   }

   /**
    * Get the ppbeans Collection.
    */
   public PackageProcessInsItemsBean getPpbeans() {
	   return this.ppbeans;
   }

}

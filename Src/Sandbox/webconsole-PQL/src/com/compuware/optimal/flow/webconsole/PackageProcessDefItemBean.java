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

import java.util.Date;

/**
 * This class holds versioned <strong>Package</strong> and
 * <strong>ProcessDef</strong> objects. The related child information of such a
 * item is also kept in this class. The child information is stored in a
 * Collection of the type <strong>PackageProcessDefItemsBean</strong>,
 * consisting of PackageProcessDefItemBean's.
 *
 * @see ProcessInstanceListAction
 * @see PackageProcessDefItemsBean
 */

public class PackageProcessDefItemBean {

   /**
    * The ID of this item.
    */
   private String id;

   /**
    * The name of this item.
    */
   private String name;

   /**
    * The entity name of this item. The entityname can be <code>Package</code>
    * or <code>ProcessDef<code>.
    */
   private String entityname;
   
   /**
    * The next Scheduled Time for the process.
    */
   private Date scheduledTime;

   /**
    * The Collection with child information.
    */
   private PackageProcessDefItemsBean ppbeans;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */
   public PackageProcessDefItemBean() {
      this.id = "";
      this.name = "";
      this.entityname = "";
      this.ppbeans = null;
      this.scheduledTime = null;
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
    * Set the entity name of this item.
    */

   public void setEntityName(String entityname) {
	   this.entityname = entityname;
   }

   /**
    * Get the entity name of this item.
    */

   public String getEntityName() {
	   return this.entityname;
   }

   /**
    * Set the ppbeans property containing child information. The child
    * information is a Collection of PackageProcessDefItemBean's that are
    * kept in a PackagaProcessDefItemsBean object.
    */

   public void setPpbeans(PackageProcessDefItemsBean ppbeans) {
	   this.ppbeans = ppbeans;
   }

   /**
    * Get the Collection of PackageProcessDefItemBean's.
    */

   public PackageProcessDefItemsBean getPpbeans() {
	   return this.ppbeans;
   }

   /**
    * Set the next scheduled time of this item.
    */

   public void setScheduledTime(Date st) {
	   this.scheduledTime = st;
   }

   /**
    * Get the entity name of this item.
    */

   public Date getScheduledTime() {
	   return this.scheduledTime;
   }

}

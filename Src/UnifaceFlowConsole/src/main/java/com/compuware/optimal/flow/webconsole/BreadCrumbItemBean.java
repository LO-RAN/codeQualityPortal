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
 * This bean holds an item of a breadcrumb trail.
 *
 * @see BreadCrumbBean
 */

public class BreadCrumbItemBean {

   /**
    * The id of this item.
    */
   private String id;

   /**
    * The name of this item.
    */
   private String name;

   /**
    * The property of the item that describes if this item is a defintion
    * or an activity. Possible values <code>Definition</code> or <code>Activity</code>.
    */
   private String definitionOrActivity;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */

   public BreadCrumbItemBean() {
      this.id = null;
      this.definitionOrActivity = null;
   }

   /**
    * Set the id of this item.
    */
   public void setId(String id) {
	   this.id = id;
   }

   /**
    * Get the id of this item.
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
    * Get the id of this item.
    */
   public String getName() {
	   return this.name;
   }

   /**
    * Set the definitionOrActivity property of this item.
    */
   public void setDefinitionOrActivity(String defOrAct) {
	   this.definitionOrActivity = defOrAct;
   }

   /**
    * Get the definitionOrActivity property of this item.
    */
   public String getDefinitionOrActivity() {
	   return this.definitionOrActivity;
   }

}

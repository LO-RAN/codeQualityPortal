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

import java.util.List;
import java.util.ArrayList;

/**
 * This bean holds the items of the tree list displayed by the JSP
 * showOrganizationTree.jsp. For each <strong>Unit</strong>,
 * <strong>Actor</strong>, or <strong>Role</strong> the ID, name, entity name
 * and its child information (as a list of other OrganizationItemBean's) are
 * saved.
 *
 * @see OrganizationItemsBean
 * @see OrganizationListAction
 */

public class OrganizationItemBean {

   /**
    * The id of this item.
    */
   private String id;

   /**
    * The name of this item.
    */
   private String name;

   /**
    * The entityname of this item. The entityname is saved within this bean, however it
    * is not used by the related JSP. It is added to show that it is there and can be used.
    */
   private String entityname;

   /**
    * The Collection with child information. Each Unit has related Actors and
    * each Actor has related Roles.
    */
   private List orgitemlist;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */
   public OrganizationItemBean() {
      this.id = "";
      this.name = "";
      this.entityname = "";
      this.orgitemlist = new ArrayList();
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

   /** Set the entity name of this item.
    *
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
    * Get the list of OrganizationItemBean's: the Actors of the Unit, the Roles
    * of the Actor.
    */

   public List getOrgItemList() {
	   return this.orgitemlist;
   }

   /**
    * Add a <strong>OrganizationItemBean</strong> to the list of
    * OrganizationItemBean's.
    */

   public Boolean add(OrganizationItemBean item) {
	   return(new Boolean(this.orgitemlist.add(item)));
   }

}

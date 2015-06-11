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
 * This bean holds <strong>OrganizationItemBean</strong> objects. This class
 * is stored in request scope by the <strong>OrganizationListAction</strong>.
 * For the sample application, this class holds a list of Units.
 *
 * @see OrganizationItemBean
 * @see OrganizationListAction
 */
public class OrganizationItemsBean {

   /**
    * The list of Units containing Actors, the Actors containing Roles.
    */
   private List orglist;

   /**
    * Creates a OrganizationItemsBean object.
    */
   public OrganizationItemsBean() {
      this.orglist = new ArrayList();
   }

   /**
    * Get the list of units, containing actors, and the actors containing roles.
    */
   public List getOrganizationList() {
	   return this.orglist;
   }

   /**
    * Add a <strong>OrganizationItemBean</strong> to the internal list of the
    * <strong>OrganizationItemsBean</strong>.
    */
   public Boolean add(OrganizationItemBean item) {
	   return(new Boolean(this.orglist.add(item)));
   }

}

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
 * Holds <strong>PackageProcessDefItemBean</strong> objects. This class is
 * maintained in request scope and is created by the
 * <strong>ProcessInstanceListAction</strong>.
 *
 * @see ProcessInstanceListAction
 * @see PackageProcessDefItemBean
 */
public class PackageProcessInsItemsBean {

   /**
    * The list of packages/processdef's.
    */
   private List pplist;

   /**
    * Creates a PackageProcessInsItemsBean object.
    */
   public PackageProcessInsItemsBean() {
      this.pplist = new ArrayList();
   }

   /**
    * Get the list of packages and process definitions.
    */
   public List getPackageProcesses() {
	   return this.pplist;
   }

   /**
    * Add a <strong>PackageProcessInsItemBean</strong> to the internal list of
    * the <strong>PackageProcessInsItemsBean</strong> object.
    */
   public Boolean add(PackageProcessInsItemBean item) {
	   return(new Boolean(this.pplist.add(item)));
   }

}


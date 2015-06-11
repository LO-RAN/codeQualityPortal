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

import java.util.LinkedList;

/**
 * This bean holds a breadcrumb trail.
 *
 * @see BreadCrumbItemBean
 */

public class BreadCrumbBean {

   /**
    * The breadcrumb this bean is holding.
    */
   private LinkedList breadcrumb;

   /**
    * The default empty constructor of this bean. All properties are initialized here.
    */

   public BreadCrumbBean() {
      this.breadcrumb = new LinkedList();
   }

   /**
    * Set the breadcrumb of this item.
    */
   public void setBreadCrumb(LinkedList breadcrumb) {
	   this.breadcrumb = breadcrumb;
   }

   /**
    * Get the breadcrumb of this item.
    */
   public LinkedList getBreadCrumb() {
	   return this.breadcrumb;
   }

   /**
    * Add a <strong>BreadCrumbItemBean</strong> to the beginning of the breadcrumb.
    */

   public void addFirst(BreadCrumbItemBean item) {
       this.breadcrumb.addFirst(item);
       return;
   }

   /**
    * Add a <strong>BreadCrumbItemBean</strong> to the end of the breadcrumb.
    */

   public void add(BreadCrumbItemBean item) {
       this.breadcrumb.add(item);
       return;
   }

   /**
    * Remove a <strong>BreadCrumbItemBean</strong> from the breadcrumb.
    */

   public BreadCrumbItemBean removeLast() {
	   return(BreadCrumbItemBean)this.breadcrumb.removeLast();
   }

   /**
    * Get the number of objects that are kept in this bean.
    */

   public int size() {
	   return this.breadcrumb.size();
   }
}

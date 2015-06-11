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
 * This bean holds <strong>UserDrivenItemBean</strong> objects. This class is
 * maintained in request scope and is created by the
 * <strong>UserDrivenTaskListAction</strong>.
 *
 * @see UserDrivenTaskListAction
 * @see ProcessTaskAction
 */
public class UserDrivenItemsBean {

   /**
    * The list of UserDrivenItemBean objects.
    */
   private List tasks;

   /**
    * Creates a new UserDrivenItemsBean object.
    */
   public UserDrivenItemsBean() {
      this.tasks = new ArrayList();
   }

   /**
    * Get the list of UserDrivenItemBeans.
    */
   public List getTasks() {
	   return this.tasks;
   }

   /**
    * Add a <strong>UserDrivenItemBean</strong> to the internal list of the
    * <strong>UserDrivenItemsBean</strong> object.
    */
   public void add(UserDrivenItemBean item) {
	   boolean b = this.tasks.add(item);
   }

}


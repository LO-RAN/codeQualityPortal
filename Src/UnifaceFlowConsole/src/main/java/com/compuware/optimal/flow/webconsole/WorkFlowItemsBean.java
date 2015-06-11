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
 * This bean holds <strong>WorkFlowItemBean</strong> objects. This class is
 * maintained in request scope and is created by the
 * <strong>WorkFlowDrivenTaskListAction</strong>.
 *
 * @see WorkFlowDrivenTaskListAction
 * @see ProcessTaskAction
 */
public class WorkFlowItemsBean {

   /**
    * The list of tasks or WorkFlowItemBean objects.
    */
   private List tasks;

   /**
    * Creates a new WorkFlowItemsBean object.
    */
   public WorkFlowItemsBean() {
      this.tasks = new ArrayList();
   }

   /**
    * Get the List of WorkFlowItemBeans.
    */
   public List getTasks() {
	   return this.tasks;
   }

   /**
    * Add a WorkFlowItemBean to the internal list of the WorkFlowItemsBean object.
    */
   public void add(WorkFlowItemBean item) {
	   boolean b = this.tasks.add(item);
   }

}


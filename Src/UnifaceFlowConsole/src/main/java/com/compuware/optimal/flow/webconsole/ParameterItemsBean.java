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

public class ParameterItemsBean {

   private List params;

   public ParameterItemsBean() {
      this.params = new ArrayList();
   }

   public List getparams() {
	   return this.params;
   }

   public void add(ParameterItemBean item) {
	   boolean b = this.params.add(item);
   }

}


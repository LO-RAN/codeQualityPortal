/**
 * 
 */
package com.compuware.carscode.parser.bean;

import java.util.Comparator;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementBeanComparator implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object o1, Object o2) {
		int result = 0;
		if (o1 != null && o2 != null) {
			IElementBean elt1 = (IElementBean)o1;
			IElementBean elt2 = (IElementBean)o2;
			if (elt1.getDescParent() != null && elt2.getDescParent() == null) {
				result = 1;
			}
			else if (elt1.getDescParent() == null && elt2.getDescParent() != null) {
				result = -1;
			}
		}
		return result;
	}

}

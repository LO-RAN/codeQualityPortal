/**
 * 
 */
package com.compuware.caqs.business.parser.bean;

import java.util.Comparator;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementBeanComparator implements Comparator<IElementBean> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(IElementBean, IElementBean)
	 */
	public int compare(IElementBean elt1, IElementBean elt2) {
		int result = 0;
		if (elt1 != null && elt2 != null) {
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

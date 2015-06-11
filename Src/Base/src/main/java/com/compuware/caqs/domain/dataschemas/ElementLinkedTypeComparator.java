/**
 * 
 */
package com.compuware.caqs.domain.dataschemas;

import java.util.Comparator;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementLinkedTypeComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		int result = 0;
		if (arg0 instanceof ElementLinked && arg1 instanceof ElementLinked) {
			result = compare((ElementLinked)arg0, (ElementLinked)arg1);
		}
		return result;
	}

	public int compare(ElementLinked arg0, ElementLinked arg1) {
		int result = 0;
		if (arg0.getTypeElt() != null && arg1.getTypeElt() != null) {
			if (!arg0.getTypeElt().equalsIgnoreCase(arg1.getTypeElt())) {
				if (arg0.getTypeElt().equalsIgnoreCase(ElementType.PRJ)) {
					result = -1;
				}
				else if (arg0.getTypeElt().equalsIgnoreCase(ElementType.SSP)) {
					if (arg1.getTypeElt().equalsIgnoreCase(ElementType.EA)) {
						result = -1;
					}
					else {
						result = 1;
					}
				}
				else if (arg0.getTypeElt().equalsIgnoreCase(ElementType.EA)) {
					result = 1;
				}
			}
			else  {
				result = arg0.getLib().compareTo(arg1.getLib());
			}
		}
		return result;
	}
}

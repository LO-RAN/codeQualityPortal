package com.compuware.caqs.domain.dataschemas.comparators;

import org.apache.commons.beanutils.BeanComparator;

public class ReverseBeanComparator extends BeanComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2158303139116433271L;

	public ReverseBeanComparator(String property) {
		super(property);
	}
	
	public int compare(Object arg0, Object arg1) {
		return - super.compare(arg0, arg1);
	}
	
}

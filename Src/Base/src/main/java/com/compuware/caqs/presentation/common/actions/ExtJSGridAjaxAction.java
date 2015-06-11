package com.compuware.caqs.presentation.common.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import com.compuware.caqs.domain.dataschemas.comparators.ReverseBeanComparator;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;

public abstract class ExtJSGridAjaxAction extends ExtJSAjaxAction {
	
	public void sort(List coll, Comparator comparator) {
		Collections.sort(coll, comparator);
	}
	
	protected void sortAsc(List coll, String property) {
		BeanComparator naturalOrderBeanComparator = new BeanComparator(property);
		Collections.sort(coll, naturalOrderBeanComparator);
	}
	
	protected void sortDesc(List coll, String property) {
		BeanComparator reverseNaturalOrderBeanComparator = new ReverseBeanComparator(property);
		Collections.sort(coll, reverseNaturalOrderBeanComparator);
	}

	protected void sortAsc(List coll, Comparator comp) {
		Collections.sort(coll, comp);
	}
	
	protected void sortDesc(List coll, Comparator comp) {
		Collections.sort(coll, comp);
	}

}

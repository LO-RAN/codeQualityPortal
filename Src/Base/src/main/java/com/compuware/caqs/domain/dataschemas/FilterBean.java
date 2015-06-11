package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class FilterBean implements Serializable {

	private static final long serialVersionUID = -11646001899405895L;
	
	private String filterDesc = null;
	private String typeElt = null;
	
	public FilterBean() {
	}
	
	public FilterBean(String filterDesc, String typeElt) {
		this.filterDesc = filterDesc;
		this.typeElt = typeElt;
	}
	
	/**
	 * @return Returns the filterDesc.
	 */
	public String getFilterDesc() {
		return filterDesc;
	}
	/**
	 * @param filterDesc The filterDesc to set.
	 */
	public void setFilterDesc(String filterDesc) {
		this.filterDesc = filterDesc;
	}
	/**
	 * @return Returns the typeElt.
	 */
	public String getTypeElt() {
		return typeElt;
	}
	/**
	 * @param typeElt The typeElt to set.
	 */
	public void setTypeElt(String typeElt) {
		this.typeElt = typeElt;
	}
	
    public boolean applyFilter() {
    	return applyFilterDescElt() || applyFilterTypeElt();
    }
    	
    public boolean applyFilterDescElt() {
    	boolean result = false;
    	if (filterDesc != null && !filterDesc.equals(JdbcDAOUtils.DATABASE_STRING_NOFILTER)) {
    		result = true;
    	}
    	return result;
    }
    	
    public boolean applyFilterTypeElt() {
    	boolean result = false;
    	if (typeElt != null && !typeElt.equals(ElementType.ALL)) {
    		result = true;
    	}
    	return result;
    }
    
    public String toString() {
    	StringBuffer result = new StringBuffer();
    	if (filterDesc != null) {
    		result.append(filterDesc);
    	}
    	result.append('-');
    	if (typeElt != null) {
    		result.append(typeElt);
    	}
    	return result.toString();
    }
    	
}

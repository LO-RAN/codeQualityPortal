package com.compuware.carscode.parser.bean;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

public interface IElementBean {

	/**
	 * @return Returns the descElt.
	 */
	public abstract String getDescElt();

	/**
	 * @param descElt The descElt to set.
	 */
	public abstract void setDescElt(String descElt);

	/**
	 * @return Returns the descParent.
	 */
	public abstract String getDescParent();

	/**
	 * @param descParent The descParent to set.
	 */
	public abstract void setDescParent(String descParent);

	/**
	 * @return Returns the metricMap.
	 */
	public abstract Map getMetricMap();

	/**
	 * @param metricMap The metricMap to set.
	 */
	public abstract void setMetricMap(Map metricMap);

	/**
	 * @return Returns the typeElt.
	 */
	public abstract String getTypeElt();

	/**
	 * @param typeElt The typeElt to set.
	 */
	public abstract void setTypeElt(String typeElt);

	/*
	 * @param idMet the metric ID.
	 * @param value the metric value as String. 
	 * @param createIfZero flag to cerate of not if the value is equal to 0.
	 */
	public void setMetric(String idMet, String value, boolean createIfZero) throws ParseException;

	/**
	 * @return The element filePath.
	 */
	public abstract String getFilePath();

	/**
	 * @param typeElt The filePath to set.
	 */
	public abstract void setFilePath(String filePath);

	/**
	 * @return the links
	 */
	public abstract Set getLinks();

	/**
	 * @param links the links to set
	 */
	public abstract void setLinks(Set links);

	public abstract void addLink(String eltLinkDesc);
	
}
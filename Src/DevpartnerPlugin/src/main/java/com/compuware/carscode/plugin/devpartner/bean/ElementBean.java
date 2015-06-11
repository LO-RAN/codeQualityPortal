/**
 * 
 */
package com.compuware.carscode.plugin.devpartner.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementBean {

	private String name;
	private String filePath;
	private Map metricMap = new HashMap();
	private String parentName;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the metricMap
	 */
	public Map getMetricMap() {
		return metricMap;
	}
	/**
	 * @param metricMap the metricMap to set
	 */
	public void setMetricMap(Map metricMap) {
		this.metricMap = metricMap;
	}
	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
}

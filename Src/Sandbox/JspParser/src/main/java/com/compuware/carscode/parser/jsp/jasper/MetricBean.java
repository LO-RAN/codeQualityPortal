package com.compuware.carscode.parser.jsp.jasper;

public class MetricBean {

	private String id;
	private int value;
	
	public MetricBean() {
	}
	
	public MetricBean(String id, int value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
}

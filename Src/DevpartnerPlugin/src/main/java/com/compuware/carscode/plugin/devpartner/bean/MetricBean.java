/**
 * 
 */
package com.compuware.carscode.plugin.devpartner.bean;

/** Represent a rule detection from Devpartner Studio.
 * @author cwfr-fdubois
 *
 */
public class MetricBean {

	String id;
	double value;
	String line = "";
	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId(String prefix) {
		return prefix + id;
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
	public double getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return Returns the line.
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param line The line to set.
	 */
	public void setLine(String line) {
		if (line != null) {
			this.line = line;
		}
	}
	
	public void incValue() {
		this.value += 1;
	}
	
	public void addLine(String line) {
		String thisLine = this.getLine();
		if(!thisLine.equals(line) && !thisLine.startsWith(line+",") && !thisLine.endsWith(","+line) && !thisLine.contains(","+line+",")) {
			if (line != null && line.length() > 0 && !line.equals("0")) {
				this.line += "," + line; 
			}
		}
	}
	
}

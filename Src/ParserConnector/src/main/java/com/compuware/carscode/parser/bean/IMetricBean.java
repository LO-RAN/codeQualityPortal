package com.compuware.carscode.parser.bean;

import java.util.Set;

public interface IMetricBean {

	/**
	 * @return Returns the id.
	 */
	public abstract String getId();

	/**
	 * @return Returns the id.
	 */
	public abstract String getId(String prefix);

	/**
	 * @param id The id to set.
	 */
	public abstract void setId(String id);

	/**
	 * @return Returns the value.
	 */
	public abstract double getValue();

	/**
	 * @param value The value to set.
	 */
	public abstract void setValue(double value);

	/**
	 * @return Returns the line.
	 */
	public abstract Set getLines();

	/**
	 * @return Returns the line.
	 */
	public abstract String getLinesAsString();

	/**
	 * @param line The line set to set.
	 */
	public abstract void setLines(Set line);

	/**
	 * @param line The line set to set as csv.
	 */
	public abstract void setLines(String line);

	public abstract void incValue();

	public abstract void incValue(double val);

	public abstract void addLine(String line);

	public abstract void addLines(Set line);

}
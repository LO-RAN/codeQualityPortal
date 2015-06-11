package com.compuware.caqs.business.parser.bean;

import java.util.Set;

public interface IMetricBean {

	/**
	 * @return the id
	 */
	public abstract String getId();

	/**
	 * @param id the id to set
	 */
	public abstract void setId(String id);

	/**
	 * @return the value
	 */
	public abstract double getValue();

	/**
	 * @param value the value to set
	 */
	public abstract void setValue(double value);

	/**
	 * @return the lines
	 */
	public abstract Set<Integer> getLines();

	/**
	 * @param lines the lines to set
	 */
	public abstract void setLines(Set<Integer> lines);

	/**
	 * Add lines to the line set.
	 * @param lines the lines to add.
	 */
	public abstract void addLines(Set<Integer> lines);
	
	/**
	 * Add a new violation.
	 * @param idx the index of the detection.
	 * @param lineIndexes the line indexes.
	 */
	public abstract void addViolation(int idx, int[] lineIndexes);

	/**
	 * Add a new violation.
	 * @param line the violation line.
	 */
	public abstract void addViolation(String line);

	/**
	 * Add a new violation.
	 * @param line the violation line.
	 */
	public abstract void addViolation(Integer line);

	/**
	 * Add a new violation.
	 */
	public abstract void incValue();

	/**
	 * Add a new violation.
	 */
	public abstract void incValue(double value);

	/**
	 * @return Returns the line.
	 */
	public abstract String getLinesAsString();

	
}
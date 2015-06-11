/**
 * 
 */
package com.compuware.caqs.business.parser.bean;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author cwfr-fdubois
 *
 */
public class MetricBean implements IMetricBean {

	/** The violation id. */
	String id = null;
	
	/** The number of detections. */
	double value = 0;
	
	/** The lines where violations have been detected. */
	Set<Integer> lines = new TreeSet<Integer>();

	/**
	 * Default constructor.
	 */
	public MetricBean() {
	}
	
	/**
	 * Constructor.
	 * @param id the violation id.
	 */
	public MetricBean(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#getValue()
	 */
	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#setValue(double)
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#getLines()
	 */
	public Set<Integer> getLines() {
		return lines;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#setLines(java.util.Set)
	 */
	public void setLines(Set<Integer> lines) {
		this.lines = lines;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#addLines(java.util.Set)
	 */
	public void addLines(Set<Integer> lines) {
		this.lines.addAll(lines);
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#addViolation(int, int[])
	 */
	public void addViolation(int idx, int[] lineIndexes) {
		if (lineIndexes  != null) {
			addViolation("" + getLine(lineIndexes, idx));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#addViolation(int)
	 */
	public void addViolation(Integer line) {
		this.lines.add(line);
		incValue();
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#addViolation(java.lang.String)
	 */
	public void addViolation(String line) {
		if (line  != null) {
			addViolation(Integer.valueOf(line));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#incValue()
	 */
	public void incValue() {
		this.value++;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.bean.IMetricBean#incValue(double)
	 */
	public void incValue(double value) {
		this.value += value;
	}
	
	protected int getLine(int[] lineIndexes, int offset) {
		int result = Arrays.binarySearch(lineIndexes, offset);
		if (result < 0) {
			result = - result - 1;
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#getLinesAsString()
	 */
	public String getLinesAsString() {
		StringBuilder result = new StringBuilder();
		if (this.lines != null && this.lines.size() > 0) {
			Iterator<Integer> lineIter = this.lines.iterator();
			while (lineIter.hasNext()) {
				if (result.length() > 0) {
					result.append(',');
				}
				result.append(lineIter.next());
			}
		}
		return result.toString();
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(getClass().getName()).append("[");
		result.append("Id=").append(this.id);
		result.append(", Value=").append(this.value);
		result.append(", Lines=").append(this.lines.toString());
		result.append(']');
		return result.toString();
	}
	

}

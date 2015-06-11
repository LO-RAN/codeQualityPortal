/**
 * 
 */
package com.compuware.carscode.parser.bean;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/** Represent a rule detection from Devpartner Studio.
 * @author cwfr-fdubois
 *
 */
public class MetricBean implements IMetricBean {

	String id;
	double value;
	Set lines = new TreeSet();
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#getId(java.lang.String)
	 */
	public String getId(String prefix) {
		return prefix + id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#getValue()
	 */
	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#setValue(double)
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#getLines()
	 */
	public Set getLines() {
		return lines;
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#getLinesAsString()
	 */
	public String getLinesAsString() {
		StringBuilder result = new StringBuilder();
		if (this.lines != null && this.lines.size() > 0) {
			Iterator lineIter = this.lines.iterator();
			while (lineIter.hasNext()) {
				if (result.length() > 0) {
					result.append(',');
				}
				result.append((Integer)lineIter.next());
			}
		}
		return result.toString();
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#setLines(java.util.Set)
	 */
	public void setLines(Set lines) {
		if (lines != null) {
			this.lines = lines;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#setLines(java.lang.String)
	 */
	public void setLines(String lines) {
		if (lines != null) {
			this.addLine(lines);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#incValue()
	 */
	public void incValue() {
		this.value += 1;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#incValue()
	 */
	public void incValue(double val) {
		this.value += val;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#addLine(java.lang.String)
	 */
	public void addLine(String line) {
		if(line != null) {
			String[] allLines = line.split(",");
			if (allLines != null && allLines.length > 0) {
				for (int i = 0; i < allLines.length; i++) {
					if (allLines[i] != null && allLines[i].length() > 0) {
						this.lines.add(Integer.valueOf(allLines[i]));
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IMetricBean#addLine(java.util.Set)
	 */
	public void addLines(Set lines) {
		if (lines != null) {
			this.lines.addAll(lines);
		}
	}
	
}

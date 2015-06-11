package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class LineOfMetricsBean implements Serializable {

	private Collection<MetriqueLineBean> metricColl = new ArrayList<MetriqueLineBean>();
	private int line;
	
	public LineOfMetricsBean(int line) {
		this.line = line;
	}
	
	/**
	 * @return Returns the line.
	 */
	public int getLine() {
		return line;
	}
	/**
	 * @param line The line to set.
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return Returns the metricColl.
	 */
	public Collection<MetriqueLineBean> getMetricColl() {
		return metricColl;
	}

	/**
	 * @param metricColl The metricColl to set.
	 */
	public void setMetricColl(Collection<MetriqueLineBean> metricColl) {
		this.metricColl = metricColl;
	}
	
	/**
	 * Add a metric to the metric collection.
	 * @param met the metric to add.
	 */
	public void addMetricLine(MetriqueLineBean met) {
		this.metricColl.add(met);
	}

}

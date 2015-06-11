/**
 * 
 */
package com.compuware.caqs.domain.dataschemas;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public class ParagraphBean {

	private String name;
	private int debut;
	private int fin;
	private Map metricMap = new HashMap();
	
	/**
	 * @return Returns the debut.
	 */
	public int getDebut() {
		return debut;
	}
	
	/**
	 * @param debut The debut to set.
	 */
	public void setDebut(int debut) {
		this.debut = debut;
	}
	
	/**
	 * @param debut The debut to set.
	 */
	public void setDebut(String debut) {
		if (debut != null && debut.length() > 0) {
			this.debut = Integer.parseInt(debut);
		}
	}
	
	/**
	 * @return Returns the fin.
	 */
	public int getFin() {
		return fin;
	}
	
	/**
	 * @param fin The fin to set.
	 */
	public void setFin(int fin) {
		this.fin = fin;
	}
	
	/**
	 * @param fin The fin to set.
	 */
	public void setFin(String fin) {
		if (fin != null && fin.length() > 0) {
			this.fin = Integer.parseInt(fin);
		}
	}
	
	/**
	 * @return Returns the metricMap.
	 */
	public Map getMetricMap() {
		return metricMap;
	}
	
	/**
	 * @param metricMap The metricMap to set.
	 */
	public void setMetricMap(Map metricMap) {
		this.metricMap = metricMap;
	}
	
	public void addMetric(String id, String value) {
		MetriqueBean met = (MetriqueBean)this.metricMap.get(id);
		if (met == null) {
			met = new MetriqueBean();
			met.setId(id);
			met.setValbrute(Double.parseDouble(value));
			this.metricMap.put(id, met);
		}
		else {
			met.setValbrute(met.getValbrute() + Double.parseDouble(value));
		}
	}
	
	public void addLine(String id, String line) {
		MetriqueBean met = (MetriqueBean)this.metricMap.get(id);
		if (met == null) {
			met = new MetriqueBean();
			met.setId(id);
			met.setValbrute(1);
			this.metricMap.put(id, met);
		}
		else {
			met.setValbrute(met.getValbrute() + 1);
		}
		met.addLine(line);
	}
	
	public boolean isParagraphForLine(int line) {
		return (line >= debut) && (line <= fin);
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}

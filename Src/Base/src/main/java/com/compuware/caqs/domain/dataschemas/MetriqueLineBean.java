package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

public class MetriqueLineBean implements Comparable, Serializable {

	private static final long serialVersionUID = -5725283704424616118L;
	
	private String idMet;
	private int line;
	
	public MetriqueLineBean(String idMet, int line) {
		this.idMet = idMet;
		this.line = line;
	}
	
	
	/**
	 * @return Returns the idMet.
	 */
	public String getIdMet() {
		return idMet;
	}
	/**
	 * @param idMet The idMet to set.
	 */
	public void setIdMet(String idMet) {
		this.idMet = idMet;
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


	public int compareTo(Object arg0) {
		int result = 0;
		if (arg0 instanceof MetriqueLineBean) {
			int compareLine = ((MetriqueLineBean)arg0).getLine();
			result = this.line - compareLine;
		}
		return result;
	}
	
}

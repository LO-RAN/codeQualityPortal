/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.copypaste;

/**
 * @author cwfr-fdubois
 *
 */
public class CopyPasteElement {

	String idElt = null;
	String descElt = null;
	int line = 0;
	
	/**
	 * @return the idElt
	 */
	public String getIdElt() {
		return idElt;
	}
	
	/**
	 * @param idElt the idElt to set
	 */
	public void setIdElt(String idElt) {
		this.idElt = idElt;
	}
	
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return the descElt
	 */
	public String getDescElt() {
		return descElt;
	}

	/**
	 * @param descElt the descElt to set
	 */
	public void setDescElt(String descElt) {
		this.descElt = descElt;
	}
	
	
}

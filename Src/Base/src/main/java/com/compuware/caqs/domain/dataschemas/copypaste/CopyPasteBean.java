/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.copypaste;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cwfr-fdubois
 *
 */
public class CopyPasteBean {

	String id = null;
	String idBline = null;
	int lines = 0;
	List<CopyPasteElement> elements = new ArrayList<CopyPasteElement>();
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the idBline
	 */
	public String getIdBline() {
		return idBline;
	}

	/**
	 * @param idBline the idBline to set
	 */
	public void setIdBline(String idBline) {
		this.idBline = idBline;
	}

	/**
	 * @return the lines
	 */
	public int getLines() {
		return lines;
	}
	
	/**
	 * @param lines the lines to set
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}

	/**
	 * @return the elements
	 */
	public List<CopyPasteElement> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<CopyPasteElement> elements) {
		this.elements = elements;
	}
	
	/**
	 * Add a new element to the list.
	 * @param element the new element.
	 */
	public void addElement(CopyPasteElement element) {
		this.elements.add(element);
	}
	
}

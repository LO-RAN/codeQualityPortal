package com.compuware.caqs.presentation.applets.scatterplot.draw;

import javax.swing.JPanel;

public class SelectedElementPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5423564144715352246L;
	private String label = null;
	private String idElt = null;
	
	public SelectedElementPanel(String label, String idElt) {
		this.label = label;
		this.idElt = idElt;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString() {
		return this.label;
	}
	
	public String getIdElt() {
		return idElt;
	}

	public void setIdElt(String idElt) {
		this.idElt = idElt;
	}
	
}

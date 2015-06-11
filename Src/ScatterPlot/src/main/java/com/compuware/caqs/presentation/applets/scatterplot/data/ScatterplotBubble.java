package com.compuware.caqs.presentation.applets.scatterplot.data;

import java.util.Vector;

public class ScatterplotBubble extends Vector<Element> {

	/**
	 * 
	 */
	private static final long 	serialVersionUID = 2599520160377578859L;
	private ScatterPlotDataSet	container = null;
	private Point				point = null;
	
	private Vector<Element> 	selectedElements = new Vector<Element>();
	
	public ScatterplotBubble(ScatterPlotDataSet d, Point p) {
		this.container = d;
		this.point = p;
	}
	
	public boolean isSelected() {
		return !this.selectedElements.isEmpty();
	}

	public void setSelected(boolean selected) {
		for(Element elt : selectedElements) {
			elt.setElementSelected(false);
		}			
		selectedElements.clear();
		this.container.deselectBubble(this);
		if(selected) {
			for(Element elt : this) {
				this.selectedElements.add(elt);
				elt.setElementSelected(true);
			}
			this.container.selectBubble(this);
		}
	}
	
	public void setSelected(Element elt, boolean selected) {
		elt.setElementSelected(selected);
		if(selected) {
			if(!isSelected()) {
				this.container.selectBubble(this);
			}
			if(!this.selectedElements.contains(elt)) {
				this.selectedElements.add(elt);
			}
		} else {
			this.selectedElements.remove(elt);
			if(!this.isSelected()) {
				this.container.deselectBubble(this);
			}
		}
	}
	
	public double getX() {
		return this.point.getX();
	}
	
	public double getY() {
		return this.point.getY();
	}
	
	public Vector<Element> getSelectedElements() {
		return this.selectedElements;
	}

}

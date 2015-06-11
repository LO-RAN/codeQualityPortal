package com.compuware.caqs.presentation.applets.scatterplot.data;

public class Element {
	private Point 					point;
	private String					label;
	private int					indice;
	private int					serie;
	private int					item;
	private ScatterplotBubble		container;
	private boolean 				elementSelected;
	private String					idElt;
	
	public Element(String l, Point p, int i, String e) {
		this.point = p;
		this.label = l;
		this.indice = i;
		this.elementSelected = false;
		this.idElt = e;
	}
	
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String toString() {
		return this.getLabel();
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public int getSerie() {
		return serie;
	}

	public void setSerie(int serie) {
		this.serie = serie;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}
	
	public boolean equals(Object o) {
		boolean retour = false;
		if(o instanceof Element) {
			if(this.getLabel()==null) {
				retour = (((Element)o).getLabel()==null);
			} else {
				retour = this.getLabel().equals(((Element)o).getLabel());
			}
		}
		return retour;
	}

	public ScatterplotBubble getContainer() {
		return container;
	}

	public void setContainer(ScatterplotBubble container) {
		this.container = container;
	}
	
	public void setSelected(boolean s) {
		this.container.setSelected(this, s);
		//this.setElementSelected(true);
	}

	public boolean isElementSelected() {
		return elementSelected;
	}

	public void setElementSelected(boolean elementSelected) {
		this.elementSelected = elementSelected;
	}

	public String getIdElt() {
		return idElt;
	}

	public void setIdElt(String idElt) {
		this.idElt = idElt;
	}

}
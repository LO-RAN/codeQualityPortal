package com.compuware.caqs.presentation.applets.scatterplot.data;

public class Point3D extends Point {
	private final Double   	z;
	private int				nbElts;
	private Point			key;

	Point3D(Double x, Double y, Double z, int colSize, Point k) {
		super(x, y);
		this.z = z;
		this.nbElts = colSize;
		this.key = k;
	}

	Double getZ() {
		Double retour = z;
		/*if(this.selected) {
			retour = new Double(z.doubleValue()+4.0);
		}*/
		return retour;
	}

	public int getNbElts() {
		return nbElts;
	}

	public void setNbElts(int nbElts) {
		this.nbElts = nbElts;
	}

	public Point getKey() {
		return key;
	}

	public void setKey(Point key) {
		this.key = key;
	}

}
package com.compuware.caqs.presentation.applets.scatterplot.data;

public class Point implements Comparable<Point> {
	private Double x;
	private Double y;

	Point(Double x, Double y) {
		this.x = x;
		this.y = y;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Point))
			return false;
		Point p2 = (Point) o;
		return x.equals(p2.getX()) && y.equals(p2.getY());
	}

	public int hashCode() {
		return (x.toString() + y.toString()).hashCode();

	}

	public int compareTo(Point p) {
		int retour = 0;
		retour = this.x.compareTo(p.getX());
		if(retour==0) {
			retour = this.y.compareTo(p.getY());
		}
		return retour;
	}
}
package com.compuware.caqs.domain.chart.datasets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

import com.compuware.caqs.business.chart.resources.Messages;

public class ScatterPlotDataSet extends AbstractXYZDataset implements
		XYZDataset {

	private class Point {
		private Double x;

		private Double y;

		Point(Double x, Double y) {
			this.x = x;
			this.y = y;
		}

		Double getX() {
			return x;
		}

		Double getY() {
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
	}

	private class Point3D extends Point {
		private Double z;

		Point3D(Double x, Double y, Double z) {
			super(x, y);
			this.z = z;
		}

		Double getZ() {
			return z;
		}
	}

	private static final String labels[] = { "scatterplot.plotsize.0",
			"scatterplot.plotsize.1", "scatterplot.plotsize.2",
			"scatterplot.plotsize.3" };

	private List<Point3D> series[] = new ArrayList[4];

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Locale userLocale;

	public ScatterPlotDataSet(String str, Locale loc) throws IOException {
		Point point;
		Collection<String[]> collection;
		Hashtable<Point, Collection<String[]>> hm = new Hashtable<Point, Collection<String[]>>();

		if (loc != null) {
			userLocale = loc;
		} else {
			userLocale = Locale.getDefault();
		}

		BufferedReader br = new BufferedReader(new StringReader(str));
		String record = null;

		while ((record = br.readLine()) != null) {
			String[] item = record.split(";");
			if (item[0].equalsIgnoreCase(Messages.getString("scatterplot.max", userLocale))) {
				continue;
			}
			if (item[0].equalsIgnoreCase(Messages.getString("scatterplot.center", userLocale))) {
				continue;
			}
			if (item.length > 2) {
				point = new Point(new Double(item[2]), new Double(item[1]));

				collection = hm.get(point);
				if (null == collection) {
					collection = new ArrayList<String[]>();
					hm.put(point, collection);
				}
				collection.add(item);
			}
		}

		series[0] = new ArrayList<Point3D>();
		series[1] = new ArrayList<Point3D>();
		series[2] = new ArrayList<Point3D>();
		series[3] = new ArrayList<Point3D>();

		int serie;
		double pointSize;

		Iterator<Point> iter = hm.keySet().iterator();
		while (iter.hasNext()) {
			point = iter.next();
			int colSize = hm.get(point).size();

			if (colSize > 20) {
				// add to "more than 20 methods" serie
				serie = 3;
				pointSize = 4;
			} else if (colSize > 10) {
				// add to "from 11 to 20" serie
				serie = 2;
				pointSize = 3;
			} else if (colSize > 5) {
				// add to "from 6 to 10" serie					
				serie = 1;
				pointSize = 2;
			} else {
				// add to "up to 5" serie
				serie = 0;
				pointSize = 1;
			}

			series[serie].add(new Point3D(point.getX(), point.getY(), new Double(pointSize)));
		}
	}

	public int getSeriesCount() {
		return 4;
	}

	public Comparable<String> getSeriesKey(int serie) {
		return Messages.getString(labels[serie], userLocale);
	}

	/**
	 * returns the number of items in the serie
	 */
	public int getItemCount(int serie) {
		// return xVal[serie].length;
		return series[serie].size();
	}

	/**
	 * returns the X value of item in serie
	 */
	public Number getX(int serie, int item) {
		// return new Double(xVal[serie][item]);
		Point3D point = series[serie].get(item);
		return point.getX();
	}

	/**
	 * returns the Y value of item in serie
	 */
	public Number getY(int serie, int item) {
		// return new Double(yVal[serie][item]);
		Point3D point = series[serie].get(item);
		return point.getY();
	}

	/**
	 * returns the Z value of item in serie
	 */
	public Number getZ(int serie, int item) {
		// return new Double(zVal[serie][item]);
		Point3D point = series[serie].get(item);
		return point.getZ();
	}

}

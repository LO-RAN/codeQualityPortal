package com.compuware.caqs.presentation.applets.scatterplot.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

import com.compuware.caqs.business.chart.resources.Messages;

public class ScatterPlotDataSet extends AbstractXYZDataset implements
		XYZDataset {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<Element> 					allElements = new Vector<Element>();
	private Hashtable<Point, ScatterplotBubble> hm = null;
	private List<ScatterplotBubble> 			selectedBubbles = new ArrayList<ScatterplotBubble>();
	private double								maxX = 0.0;
	private double								maxY = 0.0;
	private double								minX = 0.0;
	private double								minY = 0.0;
	
	
	private static final String labels[] = { 
			"scatterplot.plotsize.0",
			"scatterplot.plotsize.1", 
			"scatterplot.plotsize.2",
			"scatterplot.plotsize.3" };

	private List<Point3D> series[] = new ArrayList[4];

	private final Locale userLocale;
	
	public ScatterPlotDataSet(String str, Locale loc) throws IOException {
		Point point;
		hm = new Hashtable<Point, ScatterplotBubble>();

		if (loc != null) {
			userLocale = loc;
		} else {
			userLocale = Locale.getDefault();
		}

		BufferedReader br = new BufferedReader(new StringReader(str));
		String record = null;
		
		int cpt = 0;
		while ((record = br.readLine()) != null) {
			String[] item = record.split(";");
			if (!item[0].equalsIgnoreCase(Messages.getString("scatterplot.max", userLocale))
				&& !item[0].equalsIgnoreCase(Messages.getString("scatterplot.center", userLocale))
				&& item.length > 2) {
				point = new Point(new Double(item[1]), new Double(item[2]));
				if(point.getX().doubleValue()<minX) {
					minX = point.getX().doubleValue();
				}
				if(point.getX().doubleValue()>maxX) {
					maxX = point.getX().doubleValue();
				}
				if(point.getY().doubleValue()<minY) {
					minY = point.getY().doubleValue();
				}
				if(point.getY().doubleValue()>maxY) {
					maxY = point.getY().doubleValue();
				}

				ScatterplotBubble collection = hm.get(point);
				if (null == collection) {
					collection = new ScatterplotBubble(this, point);
					hm.put(point, collection);
				}
				String label = item[0];
				if(label.startsWith("'")) {
					label = label.substring(1);
				}
				Element elt = new Element(label, point, cpt, item[3]);
				elt.setItem(collection.size());
				collection.add(elt);
				elt.setContainer(collection);
				this.allElements.add(elt);
				cpt++;
			}
		}

		series[0] = new ArrayList<Point3D>();
		series[1] = new ArrayList<Point3D>();
		series[2] = new ArrayList<Point3D>();
		series[3] = new ArrayList<Point3D>();

		int serie;
		Double pointSize = new Double(1.0);

		Iterator<Point> iter = hm.keySet().iterator();
		while (iter.hasNext()) {
			point = iter.next();
			ScatterplotBubble bubble = (ScatterplotBubble) hm.get(point);
			int colSize = bubble.size();

			if (colSize > 20) {
				// add to "more than 20 methods" serie
				serie = 3;
			} else if (colSize > 10) {
				// add to "from 11 to 20" serie
				serie = 2;
			} else if (colSize > 5) {
				// add to "from 6 to 10" serie					
				serie = 1;
			} else {
				// add to "up to 5" serie
				serie = 0;
			}

			for(Element elt : bubble) {
				elt.setSerie(serie);
			}
			
			series[serie].add(new Point3D(point.getX(), point.getY(), pointSize, colSize, point));
		}
	}
	
	public Vector<Element> getAllElements() {
		return this.allElements;
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
		return series[serie].size();
	}

	/**
	 * returns the X value of item in serie
	 */
	public Number getX(int serie, int item) {
		Point3D point = series[serie].get(item);
		return point.getX();
	}

	/**
	 * returns the Y value of item in serie
	 */
	public Number getY(int serie, int item) {
		Point3D point = series[serie].get(item);
		return point.getY();
	}

	/**
	 * returns the Z value of item in serie
	 */
	public Number getZ(int serie, int item) {
		Point3D point = series[serie].get(item);
		return point.getZ();
	}

	public int getNbElementsForPoint(int serie, int item) {
		Point3D point = series[serie].get(item);
		return point.getNbElts();
	}
	
	public Hashtable<Point, ScatterplotBubble> getAllBubbles() {
		return this.hm;
	}

	public void changeElementsSelection(int serie, int item) {
		Point3D point3d = series[serie].get(item);
		if(point3d!=null) {
			ScatterplotBubble elements = this.hm.get(point3d.getKey());
			if(elements!=null) {
				boolean alreadySelected = elements.isSelected();
				elements.setSelected(!alreadySelected);
			}
		}
	}
	
	public ScatterplotBubble getElements(int serie, int item) {
		ScatterplotBubble retour = null;
		if(item<series[serie].size()) {
			Point3D point3d = series[serie].get(item);
			if(point3d!=null) {
				retour = this.hm.get(point3d.getKey());
			}
		}
		return retour;
	}
	
	public void deselectAll() {
		List<ScatterplotBubble> selBubbles = new ArrayList<ScatterplotBubble>(this.selectedBubbles);
		for(Iterator<ScatterplotBubble> it = selBubbles.iterator(); it.hasNext(); ) {
			ScatterplotBubble bubble = it.next();
			bubble.setSelected(false);
		}
	}
	
	public void selectBubble(ScatterplotBubble b) {
		this.selectedBubbles.add(b);
	}

	public void deselectBubble(ScatterplotBubble b) {
		this.selectedBubbles.remove(b);
	}
	
	public List<ScatterplotBubble> getSelectedBubbles() {
		return this.selectedBubbles;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

}

package com.compuware.caqs.presentation.applets.scatterplot.util;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

public class ChartPanelUtils {
	private static ChartPanelUtils instance = null;
	
	private ChartPanelUtils() {
	}
	
	public static ChartPanelUtils getInstance() {
		if(ChartPanelUtils.instance==null) {
			ChartPanelUtils.instance = new ChartPanelUtils();
		}
		return ChartPanelUtils.instance;
	}

	public Point2D translatePointFromScreenToChart(final ChartPanel panel, final XYPlot plot,
			final double x, final double y) {
		Point2D point2d = panel.translateScreenToJava2D(new Point((int)x, (int)y));
		ChartRenderingInfo chartrenderinginfo = panel.getChartRenderingInfo();
		java.awt.geom.Rectangle2D rectangle2d = chartrenderinginfo.getPlotInfo().getDataArea();
		ValueAxis valueaxis = plot.getDomainAxis();
		org.jfree.ui.RectangleEdge rectangleedge = plot.getDomainAxisEdge();
		ValueAxis valueaxis1 = plot.getRangeAxis();
		org.jfree.ui.RectangleEdge rectangleedge1 = plot.getRangeAxisEdge();
		double d = valueaxis.java2DToValue(point2d.getX(), rectangle2d, rectangleedge);
		double d1 = valueaxis1.java2DToValue(point2d.getY(), rectangle2d, rectangleedge1);
		return new Point2D.Double(d, d1);
	}
}

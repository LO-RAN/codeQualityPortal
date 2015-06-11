package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.Color;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.presentation.applets.scatterplot.ScatterplotDataRetriever;
import com.compuware.caqs.presentation.applets.scatterplot.data.Metric;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterPlotDataSet;

public class ScatterplotDrawer {
	/**
	 * 
	 */
	private static final long 					serialVersionUID = 3285567923156972378L;
	private final JFreeChart 					jfreechart;
	private final ChartPanel					scatterplotPanel;
	private final ScatterplotAppletContainer	parent;
	private final ValueMarker 					rangemarker;
	private final ValueMarker 					domainmarker;

	public ScatterplotDrawer(ScatterplotAppletContainer p, Locale loc) {
		this.parent = p;
		ScatterPlotDataSet datas = this.parent.getDatasRetriever().getPlotDatas();
		Metric metricX = this.parent.getDatasRetriever().getMetricX();
		Metric metricY = this.parent.getDatasRetriever().getMetricY();
		String labelX = "";
		String labelY = "";
		if(metricX!=null) {
			labelX = metricX.getLib();
		}
		if(metricY!=null) {
			labelY = metricY.getLib();
		}
		jfreechart = ChartFactory.createBubbleChart(
				null,//title
				labelX,//xAxisLabel
				labelY,//yAxisLabel
				datas,//dataset
				PlotOrientation.VERTICAL,//orientation
				true,//legend
				true,//tooltip
				false);//urls
		XYPlot xyplot = jfreechart.getXYPlot();
		
		xyplot.setRenderer(new ScatterplotBubbleRenderer(datas));
		
		xyplot.setForegroundAlpha(1F);

		rangemarker = new ValueMarker(new Double(this.parent.getDatasRetriever().getCenterV()));
		rangemarker.setLabel(this.parent.getDatasRetriever().getCenterV());
		rangemarker.setLabelAnchor(RectangleAnchor.CENTER);
		rangemarker.setLabelTextAnchor(TextAnchor.CENTER);
		xyplot.addRangeMarker(rangemarker);

		domainmarker = new ValueMarker(new Double(this.parent.getDatasRetriever().getCenterH()));
		domainmarker.setLabel(this.parent.getDatasRetriever().getCenterH());
		domainmarker.setLabelAnchor(RectangleAnchor.CENTER);
		domainmarker.setLabelTextAnchor(TextAnchor.CENTER);
		xyplot.addDomainMarker(domainmarker);

		xyplot.setDomainCrosshairVisible(true);
		xyplot.setRangeCrosshairVisible(true);
		
		ChartConfig config = this.getScatterPlotConfig();

		jfreechart.setBackgroundPaint(config.getBackgroundColor());

		xyplot.setNoDataMessage(config.getNoDataMessage());
        xyplot.setBackgroundPaint(config.getBackgroundColor());
		scatterplotPanel = new ScatterplotChartPanel(jfreechart, this.parent, loc);
	}

	private ChartConfig getScatterPlotConfig() {
		ChartConfig config = new ChartConfig();
		config.setNoDataMessage("NODATA");
		config.setTitle("Scatterplot");
		config.setBackgroundColor(Color.WHITE);
		return config;
	}

	public ChartPanel getComponent() {
		return this.scatterplotPanel;
	}
	
	public void deselectAll() {
		this.parent.getDatasRetriever().getPlotDatas().deselectAll();
		this.redraw();
	}
	
	public void redraw() {
		this.jfreechart.fireChartChanged();
	}
	
	public void refreshDatas() {
		XYPlot xyplot = jfreechart.getXYPlot();
		ScatterplotDataRetriever retriever = this.parent.getDatasRetriever();
		ScatterPlotDataSet newDatas = retriever.getPlotDatas();
		
		((ScatterplotBubbleRenderer)xyplot.getRenderer()).setDatas(newDatas);
		xyplot.setDataset(newDatas);

		rangemarker.setValue(new Double(retriever.getCenterV()));
		rangemarker.setLabel(retriever.getCenterV());

		domainmarker.setValue(new Double(retriever.getCenterH()));
		domainmarker.setLabel(retriever.getCenterH());

		xyplot.getDomainAxis().setLabel(this.parent.getDatasRetriever().getMetricX().getLib());
		xyplot.getRangeAxis().setLabel(this.parent.getDatasRetriever().getMetricY().getLib());
		xyplot.getDomainAxis().setRange(newDatas.getMinX() - 2, newDatas.getMaxX() + 2);
		xyplot.getRangeAxis().setRange(newDatas.getMinY() - 2, newDatas.getMaxY() + 2);
	}

}
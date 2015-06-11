package com.compuware.caqs.business.chart.factory;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xml.DatasetReader;

import com.compuware.caqs.domain.chart.config.EvolutionConfig;
import java.awt.Font;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

public class EvolutionChartFactory {

	private static final Color REJET_COLOR = new Color(255, 226, 226);
    private static final Color RESERVE_COLOR = new Color(255, 255, 204);
    private static final Color ACCEPTE_COLOR = new Color(226, 255, 226);

	public static JFreeChart createFromXml(InputStream str, EvolutionConfig config, String xLabel, String yLabel) throws IOException {
        CategoryDataset cds = DatasetReader.readCategoryDatasetFromXML(str);
    	return createChart(cds, config, xLabel, yLabel);
	}

    public static JFreeChart createChart(CategoryDataset categorydataset, EvolutionConfig config, String xLabel, String yLabel) {
        JFreeChart lineChart = ChartFactory.createLineChart(
                config.getTitle(),
                xLabel,
                yLabel,  // range axis label
                categorydataset,            // data
                PlotOrientation.VERTICAL,   // orientation
                true,                      // include legend
                false,                      // tooltips
                false);

        customizeChart(lineChart, config);
        return lineChart;
    }
    
    private static void customizeChart(JFreeChart lineChart, EvolutionConfig config) {
    	lineChart.setBackgroundPaint(config.getBackgroundColor());
        CategoryPlot plot = lineChart.getCategoryPlot();
    	plot.setNoDataMessage(config.getNoDataMessage());
        plot.getRangeAxis(0).setLowerBound(1);

        addIntervalMarker(plot, 1.0, 2.0, config.getRejectedLabel(), REJET_COLOR);
        addIntervalMarker(plot, 2.0, 3.0, config.getReserveLabel(), RESERVE_COLOR);
        addIntervalMarker(plot, 3.0, 4.1, config.getAcceptedLabel(), ACCEPTE_COLOR);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(false);
        renderer.setShapesFilled(false);
        lineChart.getCategoryPlot().getRangeAxis(0).setUpperBound(4.1);
        lineChart.getLegend().setPosition(RectangleEdge.RIGHT);
    }
    
    private static void addIntervalMarker(CategoryPlot plot, double min, double max, String label, Color c) {
        IntervalMarker target = new IntervalMarker(min, max);
        target.setLabel(label);
        target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
        target.setLabelAnchor(RectangleAnchor.LEFT);
        target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        target.setPaint(c);
        plot.addRangeMarker(target, Layer.BACKGROUND);
    }

}

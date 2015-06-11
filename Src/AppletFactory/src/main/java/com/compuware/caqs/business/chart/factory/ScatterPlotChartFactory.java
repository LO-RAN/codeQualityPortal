package com.compuware.caqs.business.chart.factory;

import java.awt.Color;
import java.io.IOException;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.domain.chart.datasets.ScatterPlotDataSet;

public class ScatterPlotChartFactory {

    public static JFreeChart createFromString(String str, ChartConfig config, String xLabel, String yLabel, Locale loc) throws IOException {
        ScatterPlotDataSet spds = new ScatterPlotDataSet(str, loc);
        return createChart(spds, config, xLabel, yLabel);
    }

    public static JFreeChart createChart(XYZDataset xyzdataset, ChartConfig config, String xLabel, String yLabel) {
        JFreeChart jfreechart = ChartFactory.createBubbleChart(
                null, yLabel, xLabel, xyzdataset, PlotOrientation.HORIZONTAL, true, false, false);
        customizeChart(jfreechart, config);
        return jfreechart;
    }

    private static void customizeChart(JFreeChart chart, ChartConfig config) {
        XYPlot xyplot = chart.getXYPlot();
        xyplot.setForegroundAlpha(1F);

        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);

        ValueMarker rangemarker = new ValueMarker(config.getCenterY());
        rangemarker.setLabel(""+config.getCenterY());
        rangemarker.setLabelAnchor(RectangleAnchor.CENTER);
        rangemarker.setLabelTextAnchor(TextAnchor.CENTER);
        xyplot.addRangeMarker(rangemarker);

        ValueMarker domainmarker = new ValueMarker(config.getCenterX());
        domainmarker.setLabel(""+config.getCenterX());
        domainmarker.setLabelAnchor(RectangleAnchor.CENTER);
        domainmarker.setLabelTextAnchor(TextAnchor.CENTER);
        xyplot.addDomainMarker(domainmarker);

        chart.setBackgroundPaint(config.getBackgroundColor());
        xyplot.setBackgroundPaint(config.getPlotBackgroundColor());

        initSeriesPaint(xyplot, config.getSeriesPaint());

        xyplot.setNoDataMessage(config.getNoDataMessage());

        /*
        LegendTitle legendtitle = new LegendTitle(xyplot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legendtitle);
         */
    }

    private static void initSeriesPaint(XYPlot plot, Color[] seriesPaint) {
        if (plot != null && seriesPaint != null) {
            for (int i = 0; i < seriesPaint.length; i++) {
                plot.getRenderer().setSeriesPaint(i, seriesPaint[i]);
            }
        }
    }
}

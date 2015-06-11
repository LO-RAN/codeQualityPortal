package com.compuware.caqs.business.chart.factory;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xml.DatasetReader;
import org.jfree.util.Rotation;

import com.compuware.caqs.domain.chart.config.ChartConfig;

public class PieChartFactory {

	public static JFreeChart createFromXml(InputStream str, ChartConfig config) throws IOException {
        PieDataset pds = DatasetReader.readPieDatasetFromXML(str);
    	return createChart(pds, config);
	}

    public static JFreeChart createChart(PieDataset piedataset, ChartConfig config) {
        JFreeChart pieChart = null;
        if (config.isChart3D()) {
        	pieChart = ChartFactory.createPieChart3D(config.getTitle(), piedataset, false, true, false);
        }
        else {
        	pieChart = ChartFactory.createPieChart(config.getTitle(), piedataset, false, true, false);
        }
        customizeChart(pieChart, config);
        return pieChart;
    }
    
    private static void customizeChart(JFreeChart pieChart, ChartConfig config) {
    	pieChart.setBackgroundPaint(config.getBackgroundColor());
    	PiePlot plot = (PiePlot)pieChart.getPlot();
    	plot.setBackgroundPaint(config.getPlotBackgroundColor());
    	plot.setNoDataMessage(config.getNoDataMessage());
    	
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}\n({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()
        ));
        plot.setToolTipGenerator(new StandardPieToolTipGenerator(
            "{0} = {1}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()
        ));

        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));

    	plot.setNoDataMessage(config.getNoDataMessage());
    	
    	if (config.isChart3D()) {
            PiePlot3D pieplot3d = (PiePlot3D)pieChart.getPlot();
            pieplot3d.setStartAngle(290D);
            pieplot3d.setDirection(Rotation.CLOCKWISE);
            pieplot3d.setForegroundAlpha(0.5F);
    	}

        plot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
    }
    
}

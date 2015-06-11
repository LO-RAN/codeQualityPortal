package com.compuware.caqs.business.chart.factory;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xml.DatasetReader;
import org.jfree.ui.RectangleEdge;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.domain.chart.plot.KiviatPlot;
import com.compuware.caqs.business.chart.urls.KiviatURLGenerator;

public class KiviatChartFactory {

	public static JFreeChart createFromXml(InputStream str, ChartConfig config) throws IOException {
        CategoryDataset cds = DatasetReader.readCategoryDatasetFromXML(str);
    	return createChart(cds, config);
	}

    public static JFreeChart createChart(CategoryDataset categorydataset, ChartConfig config) {
        KiviatPlot kiviatplot = new KiviatPlot(categorydataset, config.getRealSerieNum());
        JFreeChart jfreechart = new JFreeChart(config.getTitle(), TextTitle.DEFAULT_FONT, kiviatplot, false);
        customizeChart(jfreechart, config);
        return jfreechart;
    }
    
    private static void customizeChart(JFreeChart kiviatChart, ChartConfig config) {
    	kiviatChart.setBackgroundPaint(config.getBackgroundColor());
    	KiviatPlot kiviatplot = (KiviatPlot)kiviatChart.getPlot();
    	kiviatplot.setInteriorGap(0.40000000000000002D);
    	
    	kiviatplot.setURLGenerator(new KiviatURLGenerator("../mainFrame.jsp", "fact_idx"));
    	
    	initSeriesPaint(kiviatplot, config.getSeriesPaint());
        
    	kiviatplot.setNoDataMessage(config.getNoDataMessage());

    	kiviatplot.setLabelGenerator(new StandardCategoryItemLabelGenerator());

    	kiviatplot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
    	
    	LegendTitle legendtitle = new LegendTitle(kiviatplot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        kiviatChart.addSubtitle(legendtitle);
    }
    
	private static void initSeriesPaint(KiviatPlot plot, Color[] seriesPaint) {
		if (plot != null && seriesPaint != null) {
			for (int i = 0; i < seriesPaint.length; i++) {
		    	plot.setSeriesPaint(i, seriesPaint[i]);
			}
		}
	}
}

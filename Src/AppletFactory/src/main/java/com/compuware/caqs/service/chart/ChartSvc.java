package com.compuware.caqs.service.chart;

import java.io.IOException;
import java.io.InputStream;

import org.jfree.chart.JFreeChart;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.business.chart.factory.KiviatChartFactory;
import com.compuware.caqs.business.chart.factory.PieChartFactory;

public class ChartSvc {

	public JFreeChart createPieChartFromXml(InputStream str, ChartConfig config) throws IOException {
		return PieChartFactory.createFromXml(str, config);
	}

	public JFreeChart createKiviatFromXml(InputStream str, ChartConfig config) throws IOException {
		return KiviatChartFactory.createFromXml(str, config);
	}

}

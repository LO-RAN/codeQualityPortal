package com.compuware.caqs.business.chart.config;

import java.awt.Color;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.domain.chart.config.EvolutionConfig;

public class ChartConfigGenerator {

    public static ChartConfig getKiviatConfig() {
        ChartConfig config = new ChartConfig();
        config.setNoDataMessage("NODATA");
        config.setBackgroundColor(new Color(255, 255, 255));
        Color[] seriesPaint = new Color[4];
        seriesPaint[0] = Color.GREEN;
        seriesPaint[1] = Color.ORANGE;
        seriesPaint[2] = Color.RED;
        seriesPaint[3] = Color.BLUE;
        config.setSeriesPaint(seriesPaint);
        config.setRealSerieNum(3);
        return config;
    }

    public static ChartConfig getSimulationKiviatConfig() {
        ChartConfig config = new ChartConfig();
        config.setNoDataMessage("NODATA");
        config.setBackgroundColor(new Color(255, 255, 255));
        Color[] seriesPaint = new Color[5];
        seriesPaint[0] = Color.GREEN;
        seriesPaint[1] = Color.ORANGE;
        seriesPaint[2] = Color.RED;
        seriesPaint[3] = Color.BLACK;
        seriesPaint[4] = Color.BLUE;
        config.setRealSerieNum(3);
        config.setSeriesPaint(seriesPaint);
        return config;
    }

    public static ChartConfig getDomainSimulationKiviatConfig() {
        ChartConfig config = new ChartConfig();
        config.setNoDataMessage("NODATA");
        config.setBackgroundColor(new Color(255, 255, 255));
        Color[] seriesPaint = new Color[5];
        seriesPaint[0] = Color.GREEN;
        seriesPaint[1] = Color.ORANGE;
        seriesPaint[2] = Color.RED;
        seriesPaint[3] = Color.BLACK;
        seriesPaint[4] = Color.BLUE;
        config.setRealSerieNum(3);
        config.setSeriesPaint(seriesPaint);
        return config;
    }

    public static ChartConfig getPieConfig(String title, boolean chart3D) {
        ChartConfig config = getPieConfig(title);
        config.setChart3D(chart3D);
        return config;
    }

    public static ChartConfig getPieConfig(String title) {
        ChartConfig config = new ChartConfig();
        config.setNoDataMessage("NODATA");
        config.setTitle(title);
        config.setBackgroundColor(new Color(255, 255, 255));
        return config;
    }

    public static ChartConfig getScatterPlotConfig() {
        ChartConfig config = new ChartConfig();
        config.setNoDataMessage("NODATA");
        config.setTitle("Scatterplot");
        config.setBackgroundColor(new Color(255, 255, 255));
        Color[] seriesPaint = new Color[4];
        seriesPaint[0] = Color.BLUE;
        seriesPaint[1] = Color.YELLOW;
        seriesPaint[2] = Color.LIGHT_GRAY;
        seriesPaint[3] = Color.BLACK;
        config.setSeriesPaint(seriesPaint);
        config.setRealSerieNum(3);
        return config;
    }

    public static EvolutionConfig getEvolutionConfig() {
        EvolutionConfig config = new EvolutionConfig();
        config.setNoDataMessage("NODATA");
        config.setTitle("Evolution");
        config.setBackgroundColor(new Color(255, 255, 255));
        return config;
    }
}

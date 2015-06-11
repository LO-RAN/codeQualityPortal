package com.compuware.caqs.domain.chart.config;

import java.awt.Color;

public class ChartConfig {

    private String title;
    private String noDataMessage;
    private Color backgroundColor;
    private Color plotBackgroundColor;
    private Color[] seriesPaint;
    private boolean chart3D = false;
    private int width = 0;
    private int height = 0;
    private int realSerieNum = 0;
    private int centerX=7;
    private int centerY=10;

    public int getRealSerieNum() {
        return realSerieNum;
    }

    public void setRealSerieNum(int realSerieNum) {
        this.realSerieNum = realSerieNum;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getNoDataMessage() {
        return noDataMessage;
    }

    public void setNoDataMessage(String noDataMessage) {
        this.noDataMessage = noDataMessage;
    }

    public Color[] getSeriesPaint() {
        return seriesPaint;
    }

    public void setSeriesPaint(Color[] seriesPaint) {
        this.seriesPaint = seriesPaint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the chart3D
     */
    public boolean isChart3D() {
        return chart3D;
    }

    /**
     * @param chart3D the chart3D to set
     */
    public void setChart3D(boolean chart3D) {
        this.chart3D = chart3D;
    }

    public Color getPlotBackgroundColor() {
        return plotBackgroundColor;
    }

    public void setPlotBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
    
}

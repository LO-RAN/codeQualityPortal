package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.BorderLayout;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.compuware.caqs.presentation.applets.scatterplot.ScatterplotDataRetriever;

public class ScatterplotAppletContainer extends JPanel {
	/**
	 * 
	 */
	private static final long 				serialVersionUID = 310082980465453283L;
	private ScatterplotDrawer 					scatterplotDrawer = null;
	private ScatterplotEastPanel				rightPanel = null;
	private ScatterplotNorthPanel				northPanel = null;
	private final ScatterplotDataRetriever		datasRetriever;
	private final String 						serverAdress;

	
	public ScatterplotAppletContainer(ScatterplotDataRetriever d, String serverAdress, Locale loc) {
		this.datasRetriever = d;
		this.serverAdress = serverAdress;
		this.initLayoutAndComponents(loc);
	}
	
	private void initLayoutAndComponents(Locale loc) {
		this.setLayout(new BorderLayout());
		JSplitPane mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPanel.setResizeWeight(1.0);
		this.scatterplotDrawer = new ScatterplotDrawer(this, loc);
		mainPanel.setLeftComponent(this.scatterplotDrawer.getComponent());

		this.rightPanel = new ScatterplotEastPanel(this.datasRetriever.getPlotDatas(), this, loc);
		mainPanel.setRightComponent(this.rightPanel);
		mainPanel.setOneTouchExpandable(true);
		//mainPanel.setContinuousLayout(true);
		
		this.add(mainPanel, BorderLayout.CENTER);
		
		this.northPanel = new ScatterplotNorthPanel(this, loc);
		this.add(this.northPanel, BorderLayout.NORTH);
	}
	
	public ScatterplotEastPanel getEastPanel() {
		return this.rightPanel;
	}
	
	public ScatterplotDrawer getScatterplotPanel() {
		return this.scatterplotDrawer;
	}
	
	public ScatterplotDataRetriever getDatasRetriever() {
		return this.datasRetriever;
	}
	
	public void reset() {
		this.getEastPanel().deselectAll();
		this.getScatterplotPanel().deselectAll();
	}
	
	public String getServerAdress() {
		return this.serverAdress;
	}

}
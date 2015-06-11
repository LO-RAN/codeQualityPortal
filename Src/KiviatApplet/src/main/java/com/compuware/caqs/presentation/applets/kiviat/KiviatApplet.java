package com.compuware.caqs.presentation.applets.kiviat;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.JApplet;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.service.chart.ChartSvc;

public class KiviatApplet extends JApplet {

    /**
	 * Compiler generated serial version ID.
	 */
	private static final long serialVersionUID = 6683388317937180249L;

    private static final java.awt.Color BGCOLOR = new java.awt.Color(255, 255, 255);
    
    // Variables declaration - do not modify
    private ChartPanel jFreePanel;
    private ChartConfig config;
    
    private String m_servletUrl;
    
	/** Initializes the applet JFreeApplet */
    public void init() {
        initParameters();
        initComponents();
    }

    private void initParameters() {
        // get the host name and port of the applet's web server
        String servletStr = this.getParameter("SERVLET");
        this.m_servletUrl = this.getCodeBase() + "/"+servletStr;

        config = new ChartConfig();
        config.setNoDataMessage(getParameter("NODATA"));
        config.setTitle(getParameter("TITLE"));
        config.setBackgroundColor(KiviatApplet.BGCOLOR);
        Color[] seriesPaint = new Color[4];
        seriesPaint[0] = Color.GREEN;
    	seriesPaint[1] = Color.ORANGE;
    	seriesPaint[2] = Color.RED;
    	seriesPaint[3] = Color.BLUE;
    	config.setSeriesPaint(seriesPaint);
    }

    /** This method is called from within the init() method to
     * initialize the form.
     */
    private void initComponents() {
        retrieveData();
        getContentPane().add(jFreePanel, java.awt.BorderLayout.CENTER);
    }

    private void retrieveData() {
        java.net.URL servletUrl;
        try {
            servletUrl = new java.net.URL(this.m_servletUrl);

            java.net.URLConnection servletConnection = servletUrl.openConnection();
            java.io.InputStream stream = servletConnection.getInputStream();

            // Nécessaire: le flux provenant de la servlet est mal interprété à la création du camenbert.
            // La lecture et le stockage du flux dans un String règle lengthproblème.
            java.io.InputStreamReader reader = new java.io.InputStreamReader(stream);
            java.io.BufferedReader br = new java.io.BufferedReader(reader);
            String xmlFile = "";
            String line;
            while ((line = br.readLine()) != null) {
                xmlFile += line+"\n";
            }
            InputStream str = new ByteArrayInputStream(xmlFile.getBytes());
            ChartSvc chartSvc = new ChartSvc();
	    	JFreeChart kiviatChart = chartSvc.createKiviatFromXml(str, this.config);
		    if (jFreePanel == null)
		        jFreePanel = new ChartPanel(kiviatChart);
		    else
		        jFreePanel.setChart(kiviatChart);
		    KiviatMouseListener mlistener = new KiviatMouseListener();
		    mlistener.setApplet(this);
		    jFreePanel.addChartMouseListener(mlistener);
	        stream.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
    }    
      
}

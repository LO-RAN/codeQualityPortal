/*
 * JFreeApplet.java
 *
 * Created on 29 juillet 2004, 10:50
 */
package com.compuware.caqs.presentation.applets.pie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.service.chart.ChartSvc;

/**
 *
 * @author  cwfr-fdubois
 */
public class PieApplet extends javax.swing.JApplet implements ActionListener {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 6850338718263909035L;
	
	private static final int FONT_SIZE = 12;
	// Constantes
    private static final String CRIT = "CRIT";
    private static final String FACT = "FACT";
    private static final java.awt.Color BGCOLOR = new java.awt.Color(255, 255, 255);
    //private static String NO_DATA_MSG = "";
    
    // Variables declaration - do not modify
    private ChartPanel jFreePanel;
    private JRadioButton critButton = null;
    private JRadioButton factButton = null;
    private final ButtonGroup group = new ButtonGroup();
    private JLabel titleLabel  = null;
    
    private boolean toolbarDisabled = false;
    private String urlParameterSuffix = null;
    
    private ChartConfig config;
    
    private String mServletUrl;
    // End of variables declaration

    /** Initializes the applet JFreeApplet */
    public void init() {
        initParameters();
        initComponents();
    }
    
    private void initParameters() {
        // get the host name and port of the applet's web server
        String servletStr = this.getParameter("SERVLET");
        this.mServletUrl = this.getCodeBase() + "/"+servletStr;
        toolbarDisabled = (this.getParameter("TOOLBARDISABLED") != null);
        urlParameterSuffix = this.getParameter("PARAMETERSUFFIX");
        if (urlParameterSuffix == null) {
        	urlParameterSuffix = "";
        }
        if (!toolbarDisabled) {
        	critButton = new JRadioButton(this.getParameter("CRIT"));
        	factButton = new JRadioButton(this.getParameter("OBJ"));
        }
        config = new ChartConfig();
        config.setNoDataMessage(getParameter("NODATA"));
        titleLabel = new JLabel(getParameter("AMELIO"));
        config.setBackgroundColor(PieApplet.BGCOLOR);
    }

    /** This method is called from within the init() method to
     * initialize the form.
     */
    private void initComponents() {
        retrieveData(PieApplet.FACT);
        getContentPane().add(jFreePanel, java.awt.BorderLayout.CENTER);
        JPanel toolBar = new JPanel();
        toolBar.add(titleLabel);
        if (!toolbarDisabled) {
	        critButton.addActionListener(this);
	        factButton.addActionListener(this);
	        factButton.setBackground(PieApplet.BGCOLOR);
	        factButton.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, PieApplet.FONT_SIZE));
	        critButton.setBackground(PieApplet.BGCOLOR);
	        critButton.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, PieApplet.FONT_SIZE));
	        // Group the radio buttons.
	        group.add(factButton);
	        group.add(critButton);    
	
	        toolBar.add(factButton);
	        toolBar.add(critButton);
	        factButton.setSelected(true);
        }
        toolBar.setBackground(PieApplet.BGCOLOR);
        getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);
    }
    
    /**
     * Récupération des données à partir de la servlet.
     * @param type de données à récupérer.
     */
    private void retrieveData(String type) {
        java.net.URL servletUrl;
        try {
            if (type.equals(PieApplet.CRIT)) {
                servletUrl = new java.net.URL(this.mServletUrl+"?type="+PieApplet.CRIT+urlParameterSuffix);
            }
            else {
                servletUrl = new java.net.URL(this.mServletUrl+"?type="+PieApplet.FACT+urlParameterSuffix);
            }

            java.net.URLConnection servletConnection = servletUrl.openConnection();
            java.io.InputStream stream = servletConnection.getInputStream();

            /* Nécessaire: le flux provenant de la servlet est mal interprété à la création du camenbert.
             * La lecture et le stockage du flux dans un String règle lengthproblème. */
            java.io.InputStreamReader reader = new java.io.InputStreamReader(stream);
            java.io.BufferedReader br = new java.io.BufferedReader(reader);
            String xmlFile = "";
            String line;
            while ((line = br.readLine()) != null) {
                xmlFile += line+"\n";
            }
            InputStream str = new ByteArrayInputStream(xmlFile.getBytes());
            ChartSvc chartSvc = new ChartSvc();
            JFreeChart pieChart = chartSvc.createPieChartFromXml(str, config);
            if (jFreePanel == null) {
                jFreePanel = new ChartPanel(pieChart);
            }
            else {
                jFreePanel.setChart(pieChart);
            }
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JRadioButton) {
        	JRadioButton bt = (JRadioButton)e.getSource();
            if (bt.equals(critButton)) {
                retrieveData(PieApplet.CRIT);
            }
            else {
                retrieveData(PieApplet.FACT);
            }
        }
        repaint();
    }

 }

/*
 * LegendWindow.java
 *
 * Created on 7 novembre 2002, 11:00
 */

package com.compuware.caqs.presentation.applets.architecture.panels.legend;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.compuware.caqs.presentation.applets.architecture.I18n;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class LegendWindow extends JFrame{

    /** Creates a new instance of LegendWindow */
    public LegendWindow() {
        this.setTitle(I18n.getString("Legend"));
        this.setSize(200,130);
        this.getContentPane().setLayout(new BorderLayout());
        LegendPanel internalPanel = new LegendPanel();        
        this.getContentPane().add("Center",internalPanel);        
        this.setResizable(false);        
        this.setVisible(true); 
        internalPanel.repaint();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(false);
    }      
}

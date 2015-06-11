/*
 * LegendPanel.java
 *
 * Created on 7 novembre 2002, 11:21
 */

package com.compuware.caqs.presentation.applets.architecture.panels.legend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.compuware.caqs.presentation.applets.architecture.I18n;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class LegendPanel extends JPanel  {

    protected Graphics offgraphics;
    protected Dimension offscreensize;
    
    /** Creates a new instance of LegendPanel */
    public LegendPanel() {     
    }
    
    public void paint(Graphics g) {
        Dimension d = getSize();
                
        g.setColor(new Color(255,255,255));
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.black);
        g.drawRect(0, 0, d.width-1, d.height-1);
        
        g.setColor(Color.black);
        g.drawLine(10,15,45,15);
        g.drawString(I18n.getString("architectureLink"),50,20);
        g.setColor(Color.green);
        g.drawLine(10,35,45,35);
        g.drawString(I18n.getString("realLinkOk"),50,40);
        g.setColor(Color.red);
        g.drawLine(10,55,45,55);
        g.drawString(I18n.getString("realLinkContresens"),50,60);
        g.setColor(Color.gray);
        g.drawLine(10,75,45,75);
        g.drawString(I18n.getString("realLinkNonPrevu"),50,80);       
    }
}

/*
 * DrawnObjectPanel.java
 *
 * Created on 25 octobre 2002, 15:37
 */

/**
 *
 * @author  fxa
 */
package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


abstract public class DrawnObjectPanel extends JPanel implements ActionListener{    
    
	//protected AppletArchitecture m_architectureApplet; 
    
    public Dimension getMinimumSize() {
		return new Dimension(300, 75);
	}

	public Dimension getMaximumSize() {
		return new Dimension(300, 75);
	}

	public Dimension getPreferredSize() {
		return new Dimension(300, 75);
	}
}
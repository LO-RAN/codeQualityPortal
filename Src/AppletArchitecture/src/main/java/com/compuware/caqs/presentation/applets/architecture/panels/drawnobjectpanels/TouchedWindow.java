/*
 * AssignedWindow.java
 *
 * Created on 4 novembre 2002, 17:19
 */

package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.BorderLayout;

import com.compuware.caqs.domain.architecture.serializeddata.Node;

public class TouchedWindow extends javax.swing.JFrame {       
    protected Node node;
    protected DrawnObjectPanelNode nodePanel;
    
    /** Creates a new instance of AssignedWindow */
    public TouchedWindow(Node n, DrawnObjectPanelNode nodePanel) { 
        
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        this.node = n;
        this.nodePanel = nodePanel;
        this.setSize(400,400);
        this.getContentPane().setLayout(new BorderLayout());        
        this.createPanel();
        this.setVisible(true);      
        this.setAlwaysOnTop(false);
    }
    
    private void createPanel(){       
        this.getContentPane().add("Center",new TouchedElementsPanel(this.node,this.nodePanel));
    }        
}

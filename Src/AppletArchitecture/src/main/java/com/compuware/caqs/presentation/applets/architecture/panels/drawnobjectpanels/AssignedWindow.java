/*
 * AssignedWindow.java
 *
 * Created on 4 novembre 2002, 17:19
 */

package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.BorderLayout;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsList;
import com.compuware.caqs.domain.architecture.serializeddata.Node;

public class AssignedWindow extends javax.swing.JFrame {       
    protected Node m_node;
    protected DrawnObjectPanelNode m_nodePanel;
    protected ElementsList m_elements;
    protected ArchitectureModel m_model;
    
    /** Creates a new instance of AssignedWindow */
    public AssignedWindow(Node n, DrawnObjectPanelNode nodePanel) { 
        this.m_model = ArchitectureModel.getInstance();
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        this.m_node = n;
        this.m_nodePanel = nodePanel;
        this.m_elements = this.m_node.getElements();
        this.setSize(400,400);
        this.getContentPane().setLayout(new BorderLayout());        
        this.createPanel();
        this.setVisible(true);      
        this.setAlwaysOnTop(false);
    }
    
    private void createPanel(){       
        this.getContentPane().add("Center",new AssignedElementsPanel(this.m_node,this.m_nodePanel));
    }        
}

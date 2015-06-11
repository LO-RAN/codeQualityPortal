/*
 * AssignWindow.java
 *
 * Created on 4 novembre 2002, 17:14
 */

package com.compuware.caqs.presentation.applets.architecture.panels.selection;


import java.awt.BorderLayout;


public class SelectionWindow extends javax.swing.JFrame {        
    
    /** Creates a new instance of AssignWindow */    
    public SelectionWindow() {
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        this.setSize(800,400);
        this.getContentPane().setLayout(new BorderLayout());        
        this.createPanel();
        this.validate();
        this.setVisible(true);
    }
    
    private void createPanel(){    	
        this.getContentPane().add("Center",new SelectionPanel());       
    }

}

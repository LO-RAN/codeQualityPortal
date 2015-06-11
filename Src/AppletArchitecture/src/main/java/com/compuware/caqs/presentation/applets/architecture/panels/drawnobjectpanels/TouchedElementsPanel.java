/*
 * AssignedElementsPanel.java
 *
 * Created on 20 mars 2003, 13:43
 */

package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Element;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsList;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.presentation.applets.architecture.I18n;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class TouchedElementsPanel extends javax.swing.JPanel {
    
    private java.awt.List m_assignedList;
    private javax.swing.JLabel m_label;
    private javax.swing.JButton m_uploadButton;
    
    protected Node m_node;
    protected boolean m_shiftOn = false;
    protected DrawnObjectPanelNode m_nodePanel;
    protected ArchitectureModel m_model;
    
    /** Creates new form AssignedElementsPanel */
    public TouchedElementsPanel( Node node,DrawnObjectPanelNode nodePanel) {
        this.m_model = ArchitectureModel.getInstance();
        this.m_node = node;
        this.m_nodePanel = nodePanel;
        initComponents();
        this.m_label.setText(this.m_node.getLbl());
        this.fillAssignedElementList();
        
        if (!this.m_model.isModifiable()){
            this.m_uploadButton.setVisible(false);
        }
    }
    
    private void fillAssignedElementList(){
        this.m_assignedList.removeAll();
        if (this.m_node!=null){
            ElementsList elts = this.m_node.getElements();
            //fill the unassigned element list
            Enumeration enumMaster = elts.getKeys().elements();
            while (enumMaster.hasMoreElements()){
                String master = (String) enumMaster.nextElement();
                this.m_assignedList.add( master );
            }
            this.repaint();
        }
    }
    
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        m_assignedList = new java.awt.List();
        m_uploadButton = new javax.swing.JButton();
        m_label = new javax.swing.JLabel();
        
        setLayout(new java.awt.GridBagLayout());
        
        m_assignedList.setMultipleMode(true);
        m_assignedList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_assignedListKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                m_assignedListKeyReleased(evt);
            }
        });
        m_assignedList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m_assignedListMouseClicked(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(m_assignedList, gridBagConstraints);
        
        m_uploadButton.setText("Upload");
        m_uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	m_uploadBtnActionPerformed(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(m_uploadButton, gridBagConstraints);
                
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        add(m_label, gridBagConstraints);
        this.validate();
        
    }

    private void m_assignedListMouseClicked(java.awt.event.MouseEvent evt) {
        // Add your handling code here:
        if(this.m_shiftOn){
            int[] indexes=  this.m_assignedList.getSelectedIndexes();
            for (int i =  indexes[0] ; i < indexes[indexes.length-1] ; i++){
                this.m_assignedList.select(i);
                
            }
        }
    }
    
    private void m_assignedListKeyReleased(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
        if (evt.getKeyCode()==KeyEvent.VK_SHIFT){
            this.m_shiftOn = false;
        }
    }
    
    private void m_assignedListKeyPressed(java.awt.event.KeyEvent evt) {
        // Add your handling code here:
        if (evt.getKeyCode()==KeyEvent.VK_SHIFT){
            this.m_shiftOn = true;
        }
    }
    
    private void m_uploadBtnActionPerformed(java.awt.event.ActionEvent evt) {

    }
    
    
    
}

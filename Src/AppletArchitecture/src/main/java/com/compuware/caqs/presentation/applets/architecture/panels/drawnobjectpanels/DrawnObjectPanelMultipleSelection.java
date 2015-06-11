/*
 * EdgePanel.java
 *
 * Created on 25 octobre 2002, 15:43
 */

/**
 *
 * @author  fxa
 */
package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.domain.architecture.serializeddata.NodeArchitectureModule;
import com.compuware.caqs.presentation.applets.architecture.I18n;

public class DrawnObjectPanelMultipleSelection extends DrawnObjectPanel implements ActionListener {

	protected Vector<Node> m_nodes;

	protected JTextArea m_list = new JTextArea();

	protected JButton m_deleteBtn = new JButton(I18n.getString("Effacer"));
	protected JButton m_groupBtn;

	protected JLabel m_label = new JLabel();

	protected JToggleButton m_lockAllButton = null;

	protected JButton m_deleteAllButton = null;
	
	protected ArchitectureModel m_model;

	
	/**
	 * Creates a new instance of EdgePanel
	 */
	public DrawnObjectPanelMultipleSelection( Vector<Node> selectedNodes) {
		this.m_model = ArchitectureModel.getInstance();
		this.m_nodes = selectedNodes;
		StringBuffer text = new StringBuffer();
		for (int i = 0; i < this.m_nodes.size(); i++) {
			Node currNode = this.m_nodes.elementAt(i);

			text.append(currNode.getLbl() + "\n");
		}
		this.m_list.setText(text.toString());

		this.setLayout( new java.awt.GridBagLayout());
		JScrollPane js = new JScrollPane();
		js.setBorder(new TitledBorder("Selected Elements"));
		js.setViewportView(this.m_list);
		//this.add(new JLabel("Multiple Selection Contains"), BorderLayout.NORTH);
		
		GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
		this.add(js, gridBagConstraints);
		
		this.m_lockAllButton = new JToggleButton("lock all");	
        this.m_lockAllButton.setSelected(false);
        this.m_lockAllButton.addItemListener(new LockAllItemListner(selectedNodes,false));
        gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        this.add(this.m_lockAllButton,gridBagConstraints);
        
        m_deleteAllButton = new JButton("delete All");
        this.m_deleteAllButton.addActionListener(new DeleteAllActionListner(this.m_model, selectedNodes));
        gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        this.add(this.m_deleteAllButton,gridBagConstraints);
        
        m_groupBtn = new JButton(I18n.getString("Group"));
        this.m_groupBtn.addActionListener(new GroupActionListner(this.m_model, selectedNodes));
        gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        this.add(this.m_groupBtn,gridBagConstraints);
        
	}

	public void actionPerformed(ActionEvent e) {
	}

	
}

class LockAllItemListner implements ItemListener {
	Vector<Node> m_selectedNodes;
	Boolean m_initialState;
	public LockAllItemListner(Vector<Node> selectedNodes,boolean initialState){
		m_selectedNodes = selectedNodes;
		m_initialState = initialState;
	}
	public void itemStateChanged(ItemEvent e) {
		
		m_initialState = !m_initialState;
		for(int i = 0 ; i < m_selectedNodes.size();i++){			
			m_selectedNodes.elementAt(i).setIsAutoLayoutProtected(m_initialState);
		}
		
	}
}

class DeleteAllActionListner implements ActionListener {
	Vector<Node> m_selectedNodes;
	ArchitectureModel m_model;
	
	public DeleteAllActionListner(ArchitectureModel model, Vector<Node> selectedNodes){
		m_selectedNodes = selectedNodes;	
		m_model = model;
	}
	
	public void actionPerformed(ActionEvent e) {
        
    	for(int i = 0 ; i < m_selectedNodes.size();i++){		
    		Node n = m_selectedNodes.elementAt(i);
    		this.m_model.getEdgesList().deleteNode(n);
		}	
    	ArchitectureModel.getInstance().fireSelectedItemDeleted();
    	this.m_model.fireModelChangedEvent();
	}
}

class GroupActionListner implements ActionListener {
	Vector<Node> m_selectedNodes;
	ArchitectureModel m_model;
	
	public GroupActionListner(ArchitectureModel model, Vector<Node> selectedNodes){
		m_selectedNodes = selectedNodes;	
		m_model = model;
	}
	
	public void actionPerformed(ActionEvent e) {
		// find top left of contained nodes.
		Node n = this.m_selectedNodes.elementAt(0);
		// initialize with first node coordinnates
		double upperLeftX = n.getUpperLeftX();
		double upperLeftY = n.getUpperLeftY();
		double upperRigthX = n.getUpperRightX();
		double upperRigthY = n.getUpperRightY();
		double bottomLeftX = n.getBottomLeftX();
		double bottomLeftY = n.getBottomLeftY();
		double bottomRightX = n.getBottomRightX();
		double bottomRightY = n.getBottomRightY();

		int minX = (int) Math.min(upperLeftX, bottomLeftX);
		int maxX = (int) Math.max(upperRigthX, bottomRightX);
		int minY = (int) Math.min(upperLeftY, upperRigthY);
		int maxY = (int) Math.max(bottomLeftY, bottomRightY);

		for (int i = 0; i < this.m_selectedNodes.size(); i++) {
			n = this.m_selectedNodes.elementAt(i);

			if (upperLeftX > n.getUpperLeftX()) {
				upperLeftX = n.getUpperLeftX();
			}
			if (bottomLeftX > n.getBottomLeftX()) {
				bottomLeftX = n.getBottomLeftX();
			}
			if (upperLeftY > n.getUpperLeftX()) {
				upperLeftY = n.getUpperLeftY();
			}
			if (upperRigthX < n.getUpperRightX()) {
				upperRigthX = n.getUpperRightX();
			}
			if (upperRigthY > n.getUpperRightY()) {
				upperRigthY = n.getUpperRightY();
			}
			if (bottomLeftY < n.getBottomLeftY()) {
				bottomLeftY = n.getBottomLeftY();
			}
			if (bottomRightX < n.getBottomRightX()) {
				bottomRightX = n.getBottomRightX();
			}
			if (bottomRightY < n.getBottomRightY()) {
				bottomRightY = n.getBottomRightY();
			}
		}

		minX = (int) Math.min(upperLeftX, bottomLeftX);
		maxX = (int) Math.max(upperRigthX, bottomRightX);
		minY = (int) Math.min(upperLeftY, upperRigthY);
		maxY = (int) Math.max(bottomLeftY, bottomRightY);

		Point2D.Double topLeft = new Point2D.Double(minX, minY);
		Point2D.Double bottomRight = new Point2D.Double(maxX, maxY);
		
		NodeArchitectureModule newNode = new NodeArchitectureModule("UnNamed_Group",(minX+maxX)/2,(minY+maxY)/2,0,maxX-minX,maxY-minY);
    	for(int i = 0 ; i < m_selectedNodes.size();i++){		
    		Node currentNode = m_selectedNodes.elementAt(i);
    		newNode.addChildObject(currentNode);
    		//currentNode.setIsInAGroup(true);
    		//currentNode.setGroup(newNode);
    		//ArchitectureModel.getInstance().getNodeList().remove(currentNode);
		}	
    	
    	ArchitectureModel.getInstance().addNode(newNode);
    	ArchitectureModel.getInstance().fireSelectedItemDeleted();
    	this.m_model.fireModelChangedEvent();
	}
}

/*
 * NodePanel2.java
 *
 * Created on 20 mars 2003, 14:10
 */

package com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels;

import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsList;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.ErrorDialog;

/**
 * @author cwfr-fxalbouy
 */
public class DrawnObjectPanelNode extends DrawnObjectPanel {
	// Variables declaration - do not modify//:variables
	// private java.awt.List list1;
	private javax.swing.JList list1;
	private JScrollPane listScrollPane;

	private javax.swing.JButton m_deleteBtn;
	private javax.swing.JLabel m_layerLbl;
	private javax.swing.JTextField m_nameFld;
	private javax.swing.JLabel m_nameLbl;
	private javax.swing.JButton m_propertiesBtn;
	private javax.swing.JButton m_saveBtn;
	private javax.swing.JToggleButton m_collapsedBtn;
	private javax.swing.JButton m_ungroupButton;
	private javax.swing.JTextField m_weightFld;
	private javax.swing.JLabel m_weightLbl;
	private javax.swing.JButton zOrderDownBtn;
	private javax.swing.JButton zOrderUpBtn;

	protected JCheckBox m_fixedCheck;
	// End of variables declaration//:variables

	protected Node m_node;
	protected ArchitectureModel m_model;

	/**
	 * Creates new form NodePanel2
	 */

	public DrawnObjectPanelNode() {
	}

	public DrawnObjectPanelNode(Node node) {
		this.m_model = ArchitectureModel.getInstance();
		this.m_node = node;

		initComponents();
		this.m_nameFld.setText(this.m_node.getLbl());
		this.m_weightFld.setText("" + this.m_node.getWeight());

		// init list

		this.initList();

		if (!this.m_model.isModifiable()) {
			this.m_saveBtn.setVisible(false);
			this.m_deleteBtn.setVisible(false);
		}
		this.updateLayer();
	}

	public void initList() {
		this.list1.removeAll();
		ElementsList list = this.m_node.getElements();
		Vector keys = list.getKeys();

		DefaultListModel dlm = new DefaultListModel();

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.elementAt(i);
			dlm.addElement(name);
		}
		this.list1.setModel(dlm);

	}

	private void updateLayer() {
		int layer = this.m_node.getZOrder();
		int totLayer = this.m_model.getNodeList().getNodesVector().size() - 1;
		this.m_layerLbl.setText(I18n.getString("layer") + layer + "/" + totLayer);
	}

	protected void updateNode() {
		if (this.m_nameFld.getText().length() > 0) {
			this.m_node.setLbl(this.m_nameFld.getText());
		} else {
			this.m_node.setLbl(I18n.getString("Unnamed"));
			this.m_nameFld.setText(this.m_node.getLbl());
		}

		if (this.m_weightFld.getText().length() > 0) {
			boolean error = false;
			int i = 1;
			try {
				i = Integer.parseInt(this.m_weightFld.getText());
				this.m_node.setWeight(i);
			} catch (Exception e) {
				error = true;
			}
			if (i < 1) {
				error = true;
			}

			if (error) {
				ErrorDialog dlg = new ErrorDialog(new java.awt.Frame(), true);
				dlg.setVisible(true);
				this.m_weightFld.setText("1");
				this.m_node.setWeight(1);
			}
		} else {
			this.m_weightFld.setText("1");
			this.m_node.setWeight(1);
		}
		this.updateLayer();
		this.repaint();
		this.m_model.fireModelChangedEvent();
	}

	public void actionPerformed(java.awt.event.ActionEvent actionEvent) {

	}

	private void initComponents() {// :initComponents

		m_nameFld = new javax.swing.JTextField();
		m_deleteBtn = new javax.swing.JButton();
		m_saveBtn = new javax.swing.JButton();
		m_collapsedBtn = new javax.swing.JToggleButton("Expanded");
		m_ungroupButton = new javax.swing.JButton(I18n.getString("ungroup"));
		
		
		m_ungroupButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_node.ungroup();
			}
		});
		
		m_collapsedBtn.setSelected(this.m_node.isExpanded());		
		m_collapsedBtn.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				m_node.setIsExpanded(!m_node.isExpanded());
				m_collapsedBtn.setSelected(m_node.isExpanded());
				m_model.fireModelChangedEvent();
			}
		});

		list1 = new javax.swing.JList();
		this.listScrollPane = new JScrollPane();
		this.listScrollPane.setBorder(new TitledBorder("Contained Elements"));
		this.listScrollPane.setViewportView(this.list1);
		m_weightFld = new javax.swing.JTextField();
		m_propertiesBtn = new javax.swing.JButton();
		m_weightLbl = new javax.swing.JLabel();
		m_nameLbl = new javax.swing.JLabel();
		zOrderUpBtn = new javax.swing.JButton();
		zOrderDownBtn = new javax.swing.JButton();
		m_layerLbl = new javax.swing.JLabel();
		m_fixedCheck = new JCheckBox("Fixed");
		if (this.m_node.isAutoLayoutProtected()) {
			m_fixedCheck.setSelected(true);
		}
		m_fixedCheck.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_node.setIsAutoLayoutProtected(!m_node.isAutoLayoutProtected());
				m_fixedCheck.setSelected(m_node.isAutoLayoutProtected());
			}
		});
		setLayout(new java.awt.GridBagLayout());

		m_nameFld.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				m_nameFldKeyReleased(evt);
			}
		});

		// Layout Components
		java.awt.GridBagConstraints gridBagConstraints;
		
		// line 0
		m_nameLbl.setText(I18n.getString("name") + " :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_nameLbl, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_nameFld, gridBagConstraints);

		m_weightLbl.setText(I18n.getString("weight") + " :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_weightLbl, gridBagConstraints);
		
		m_weightFld.setText("1");
		m_weightFld.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent evt) {
				m_weightFldKeyReleased(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_weightFld, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.gridheight = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		
		add(this.listScrollPane, gridBagConstraints);
		// line 1
		m_deleteBtn.setText(I18n.getString("Effacer"));
		m_deleteBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_deleteBtnActionPerformed(evt);
			}
		});

		//line 2
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_deleteBtn, gridBagConstraints);

		m_saveBtn.setText(I18n.getString("save"));
		m_saveBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_saveBtnActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_saveBtn, gridBagConstraints);

		

		
		m_propertiesBtn.setText(I18n.getString("properties"));
		m_propertiesBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_propertiesBtnActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_propertiesBtn, gridBagConstraints);

		// Group management
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_collapsedBtn, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_ungroupButton, gridBagConstraints);
		// Layer mangement
		m_layerLbl.setText(I18n.getString("layer"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_layerLbl, gridBagConstraints);

		zOrderUpBtn.setText(I18n.getString("_+_"));
		zOrderUpBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zOrderUpBtnActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(zOrderUpBtn, gridBagConstraints);

		zOrderDownBtn.setText(I18n.getString("_-_"));
		zOrderDownBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zOrderDownBtnActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(zOrderDownBtn, gridBagConstraints);

		// last line
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		add(m_fixedCheck, gridBagConstraints);
	}// :initComponents

	private void zOrderDownBtnActionPerformed(java.awt.event.ActionEvent evt) {// :event_zOrderDownBtnActionPerformed
		// Add your handling code here:
		int i = this.m_model.getNodeList().getNodesVector().indexOf(this.m_node);
		if ((i - 1) > -1) {
			Node n = (Node) this.m_model.getNodeList().getNodesVector().elementAt(i - 1);
			this.m_model.getNodeList().getNodesVector().setElementAt(this.m_node, i - 1);
			this.m_node.setZOrder(i - 1);
			this.m_model.getNodeList().getNodesVector().setElementAt(n, i);
			n.setZOrder(i);
			this.updateLayer();
			this.m_model.fireModelChangedEvent();
		}
	}

	private void zOrderUpBtnActionPerformed(java.awt.event.ActionEvent evt) {// :event_zOrderUpBtnActionPerformed
		// Add your handling code here:
		int i = this.m_model.getNodeList().getNodesVector().indexOf(this.m_node);
		if ((i + 1) < this.m_model.getNodeList().getNodesVector().size()) {
			Node n = (Node) this.m_model.getNodeList().getNodesVector().elementAt(i + 1);
			this.m_model.getNodeList().getNodesVector().setElementAt(this.m_node, i + 1);
			this.m_node.setZOrder(i + 1);
			this.m_model.getNodeList().getNodesVector().setElementAt(n, i);
			n.setZOrder(i);
			this.updateLayer();
			this.m_model.fireModelChangedEvent();
		}
	}

	private void m_propertiesBtnActionPerformed(java.awt.event.ActionEvent evt) {// :event_m_propertiesBtnActionPerformed
		// Add your handling code here:
		new AssignedWindow(this.m_node, this);
	}

	private void m_weightFldKeyReleased(java.awt.event.KeyEvent evt) {// :event_m_weightFldKeyReleased
		// Add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			this.updateNode();
		}
	}

	private void m_nameFldKeyReleased(java.awt.event.KeyEvent evt) {// :event_m_nameFldKeyReleased
		// Add your handling code here:
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			this.updateNode();
		}
	}

	private void m_saveBtnActionPerformed(java.awt.event.ActionEvent evt) {// :event_m_saveBtnActionPerformed
		// Add your handling code here:
		this.updateNode();
	}

	private void m_deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {// :event_m_deleteBtnActionPerformed
		// Add your handling code here:
		int result = JOptionPane.showConfirmDialog(null, I18n.getString("confirm_element_delete")); // le
		// premier
		// paramètre
		// est
		// la
		// fenêtre
		// mère.
		// Il
		// sert
		// à
		// centrer
		// la
		// boite
		// de
		// dialogue
		// dans
		// la
		// fenêtre
		// mère.
		switch (result) {
		case JOptionPane.YES_OPTION:
			this.m_model.getEdgesList().deleteNode(this.m_node);
			// this.m_edgesList.deleteNode(this.m_node);
			ArchitectureModel.getInstance().fireSelectedItemDeleted();
			break;
		}
		this.m_model.fireModelChangedEvent();
	}

}

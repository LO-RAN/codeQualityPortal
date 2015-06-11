/*
 * SelectionPanel.java
 *
 * Created on 17 mars 2003, 14:27
 */
package com.compuware.caqs.presentation.applets.architecture.panels.selection;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.DrawnObject;
import com.compuware.caqs.domain.architecture.serializeddata.Element;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsList;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.domain.architecture.serializeddata.NodeArchitectureModule;
import com.compuware.caqs.domain.architecture.serializeddata.NodeList;
import com.compuware.caqs.presentation.applets.architecture.I18n;

/**
 * 
 * @author cwfr-fxalbouy
 */
public class SelectionPanel extends javax.swing.JPanel {

    private javax.swing.JTree unassignedTreeView;
    private javax.swing.JTree assignedTreeView;
    private DefaultMutableTreeNode unassignedBaseTreeNode = new DefaultMutableTreeNode("EA");
    private DefaultMutableTreeNode assignedBaseTreeNode = new DefaultMutableTreeNode("Node");
    private javax.swing.JButton addHierarchieBtn;
    private javax.swing.JButton removeHierarchieBtn;
    private javax.swing.JButton createNodeAndAddHierarchieBtn;
    private javax.swing.JLabel architectureModuleLbl;
    private javax.swing.JLabel unassignedLbl;
    protected boolean shiftOn = false;
    protected NodeList nl;
    protected ArrayList<Node> nodes;
    protected Node currentNode;
    protected TreePath lastPath;

    /** Creates new form SelectionPanel */
    public SelectionPanel() {
        this.nl = ArchitectureModel.getInstance().getNodeList();

        initComponents();

        // fill the unassigned element list
        this.fillUnAssignedElementList();
        this.fillAssignedElementList();
        this.setSize(400, 400);

        if (!ArchitectureModel.getInstance().isModifiable()) {
            this.removeHierarchieBtn.setVisible(false);
            this.addHierarchieBtn.setVisible(false);
            this.createNodeAndAddHierarchieBtn.setVisible(false);
        }
    }

    private void fillAssignedElementList() {
        this.nodes = ArchitectureModel.getInstance().getNodeList().getAffectableNodes();

        this.assignedBaseTreeNode = new DefaultMutableTreeNode("Architecture Model");
        DefaultTreeModel model = (DefaultTreeModel) assignedTreeView.getModel();
        model.setRoot(assignedBaseTreeNode);
        if (this.nodes != null && this.nodes.size() > 0) {
            Iterator<Node> i = this.nodes.iterator();
            while (i.hasNext()) {
                Node currNode = i.next();
                if (!currNode.isInAGroup()) {
                    NodeTreeNode theNodeInTheTree = new NodeTreeNode(currNode);
                    this.assignedBaseTreeNode.add(theNodeInTheTree);
                    fillNodeContainedElements(currNode, theNodeInTheTree);

                    if (currNode == currentNode) {
                        lastPath = new TreePath(theNodeInTheTree.getPath());
                    }
                }
            }
            // this.m_currentNode = this.m_nodes.get(0);
            this.assignedTreeView.setCellRenderer(new MyTreeCellRenderer());
            this.assignedTreeView.setExpandsSelectedPaths(true);
            this.assignedTreeView.repaint();
        }
    }

    private void fillNodeContainedElements(Node node, DefaultMutableTreeNode baseTreeNode) {
        Vector<DrawnObject> childNodes = node.getChildObjects();
        int numberOfSubNodes = childNodes.size();
        for (int i = 0; i < numberOfSubNodes; i++) {
            Node childNode = (Node) childNodes.elementAt(i);
            NodeTreeNode parentNode = new NodeTreeNode(childNode);
            baseTreeNode.add(parentNode);
            fillNodeContainedElements(childNode, parentNode);

        }

        // add the elements directly contained in the node
        ElementsList elts = node.getElements();
        for (Element master : elts) {
            if (!(elts.contains(master.getParent()))) {
                ElementTreeNode parentNode = new ElementTreeNode(master);
                createChildTreeNode(false, master, parentNode);
                // m_unassignedBaseTreeNode.add(parentNode);
                baseTreeNode.add(parentNode);
            }
        }
    }

    private void fillUnAssignedElementList() {
        this.unassignedTreeView.removeAll();
        unassignedBaseTreeNode.removeAllChildren();

        // Enumeration enumMaster = this.m_elementsList.elements();
        ElementsList el = ArchitectureModel.getInstance().getUnassignedElementsList();
        for (Element master : el) {
            if (!(el.contains(master.getParent()))) {
                ElementTreeNode parentNode = new ElementTreeNode(master);
                createChildTreeNode(true, master, parentNode);
                unassignedBaseTreeNode.add(parentNode);
            }
        }
        DefaultTreeModel model = (DefaultTreeModel) unassignedTreeView.getModel();
        model.setRoot(unassignedBaseTreeNode);
        this.unassignedTreeView.repaint();
    }

    private void createChildTreeNode(boolean forUnassignedPanel, Element elt, ElementTreeNode treeNode) {
        Vector<Element> children = elt.getChildren();
        for (Element childElt : children) {
            ElementTreeNode childTreeNode = new ElementTreeNode(childElt);
            treeNode.add(childTreeNode);
            createChildTreeNode(forUnassignedPanel, childElt, childTreeNode);
        }
    }

    private void initComponents() {// :initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        // m_nodeChoice = new java.awt.Choice();
        unassignedLbl = new javax.swing.JLabel();
        architectureModuleLbl = new javax.swing.JLabel();

        addHierarchieBtn = new javax.swing.JButton();
        removeHierarchieBtn = new javax.swing.JButton();
        createNodeAndAddHierarchieBtn = new javax.swing.JButton();

        if (this.nl.size() > 0) {
            addHierarchieBtn.setEnabled(true);
            removeHierarchieBtn.setEnabled(true);
        } else {
            addHierarchieBtn.setEnabled(false);
            removeHierarchieBtn.setEnabled(false);
        }

        unassignedTreeView = new javax.swing.JTree(unassignedBaseTreeNode);
        assignedTreeView = new javax.swing.JTree(assignedBaseTreeNode);
        this.assignedTreeView.addMouseListener(new MyTreeMouseListener());

        setLayout(new java.awt.GridBagLayout());

        // Jtree unassigned Elements
        JScrollPane js = new JScrollPane(unassignedTreeView);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(js, gridBagConstraints);

        // node choice dropdwon
		/*m_nodeChoice.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
        nodeChoiceItemStateChanged(evt);
        }
        });*/

        /*gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(m_nodeChoice, gridBagConstraints);
         */
        // JTree assigned
        JScrollPane js2 = new JScrollPane(assignedTreeView);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(js2, gridBagConstraints);

        unassignedLbl.setText(I18n.getString("unAssigned"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(unassignedLbl, gridBagConstraints);

        architectureModuleLbl.setText(I18n.getString("architecture_Module"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(architectureModuleLbl, gridBagConstraints);

        // addHierarchie button
        addHierarchieBtn.setText(I18n.getString("add_->_"));
        addHierarchieBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtn2ActionPerformed(evt, true, false);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        add(addHierarchieBtn, gridBagConstraints);

        // addHierarchie button
        removeHierarchieBtn.setText(I18n.getString("<-_delete"));
        removeHierarchieBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt, true);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        add(removeHierarchieBtn, gridBagConstraints);

        // add createNodeAndAddHierarchieBtn button
        createNodeAndAddHierarchieBtn.setText(I18n.getString("create_and_add_->_"));
        createNodeAndAddHierarchieBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNodeAndAddHierarchieBtnActionPerformed(evt, true);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        add(createNodeAndAddHierarchieBtn, gridBagConstraints);
    }

    private void m_assignedElementListKeyReleased(java.awt.event.KeyEvent evt) {// :event_m_assignedElementListKeyReleased
        // Add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.shiftOn = false;
        }
    }

    private void m_assignedElementListKeyPressed(java.awt.event.KeyEvent evt) {// :event_m_assignedElementListKeyPressed
        // Add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.shiftOn = true;
        }
    }

    private void createNodeAndAddHierarchieBtnActionPerformed(java.awt.event.ActionEvent evt, boolean addAlsoChildren) {
        // add the new node
        NodeArchitectureModule n = new NodeArchitectureModule("unnamed");
        ArchitectureModel.getInstance().addNode(n);
        // fillNodeList();
        this.currentNode = n;

        addBtn2ActionPerformed(evt, addAlsoChildren, true);

        this.fillAssignedElementList();
    }

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt, boolean removeAlsoChildren) {
        TreePath[] selectedItemsZ = this.assignedTreeView.getSelectionPaths();

        Vector<Element> selectedItems = new Vector<Element>();
        if (selectedItemsZ != null) {
            for (int pathsCnt = 0; pathsCnt < selectedItemsZ.length; pathsCnt++) {
                TreePath path = selectedItemsZ[pathsCnt];
                Object[] obj = path.getPath();

                // get the last element of the path
                ElementTreeNode currentNode = (ElementTreeNode) obj[obj.length -
                        1];
                selectedItems.add(currentNode.getElement());

                DefaultTreeModel model = (DefaultTreeModel) assignedTreeView.getModel();
                model.removeNodeFromParent(currentNode);

                Element elt = currentNode.getElement();
                this.currentNode.removeElement(elt);
                ArchitectureModel.getInstance().getUnassignedElementsList().addElement(elt);

                this.unassignChild(elt, currentNode, removeAlsoChildren);

            }
        }
        this.fillUnAssignedElementList();
        this.fillAssignedElementList();
        ArchitectureModel.getInstance().computeRealLinks();
    }

    private void unassignChild(Element elt, ElementTreeNode node, boolean remove) {
        Vector<Element> children = elt.getChildren();
        for (int childrenCount = 0; childrenCount < children.size(); childrenCount++) {
            Element eltChild = children.elementAt(childrenCount);
            if (remove) {
                ElementTreeNode newNode = new ElementTreeNode(eltChild);

                node.add(newNode);
                this.unassignChild(eltChild, newNode, remove);
                this.currentNode.removeElement(eltChild);
                ArchitectureModel.getInstance().getUnassignedElementsList().addElement(eltChild);
            }

            relinkToParent(eltChild, node, remove);

        }
    }

    private void relinkToParent(Element eltChild, ElementTreeNode node, boolean remove) {
        // detect children and relink tree structure
        DefaultTreeModel model = (DefaultTreeModel) unassignedTreeView.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        int childCount = root.getChildCount();
        Vector<ElementTreeNode> toBeRemoved = new Vector<ElementTreeNode>();
        for (int w = 0; w < childCount; w++) {
            ElementTreeNode child = (ElementTreeNode) root.getChildAt(w);
            if (child.toString().compareTo(eltChild.getLabel()) == 0) {
                toBeRemoved.add(child);
                // System.out.println("to be removed" + child);
            }
        }

        for (int z = 0; z < toBeRemoved.size(); z++) {
            if (!remove) {
                model.insertNodeInto(toBeRemoved.elementAt(z), node, 0);
                model.nodeStructureChanged(unassignedBaseTreeNode);
            } else {
                model.removeNodeFromParent(toBeRemoved.elementAt(z));
            }
        }
    }

    private void addBtn2ActionPerformed(java.awt.event.ActionEvent evt, boolean addAlsoChildren, boolean setNodeName) {// :event_button2ActionPerformed
        // Add your handling code here:
        TreePath[] selectedItemsZ = this.unassignedTreeView.getSelectionPaths();

        Vector<Element> selectedItems = new Vector<Element>();
        if (selectedItemsZ != null) {
            for (int pathsCnt = 0; pathsCnt < selectedItemsZ.length; pathsCnt++) {
                TreePath path = selectedItemsZ[pathsCnt];
                Object[] obj = path.getPath();

                // get the last element of the path
                ElementTreeNode currentNode = (ElementTreeNode) obj[obj.length -
                        1];
                selectedItems.add(currentNode.getElement());

                if (setNodeName) {
                    this.currentNode.setLbl(currentNode.getElement().getLabel());
                }

                DefaultTreeModel model = (DefaultTreeModel) unassignedTreeView.getModel();
                model.removeNodeFromParent(currentNode);

                Element elt = currentNode.getElement();
                this.currentNode.addElement(elt);
                ArchitectureModel.getInstance().getUnassignedElementsList().removeElement(elt);
                if (addAlsoChildren) {
                    this.assignChild(elt);
                }
            }
        }

        this.fillAssignedElementList();
        this.assignedTreeView.expandPath(lastPath);
        this.assignedTreeView.setExpandsSelectedPaths(true);
        ArchitectureModel.getInstance().computeRealLinks();

    }

    private void assignChild(Element elt) {
        Vector<Element> children = elt.getChildren();
        for (int childrenCount = 0; childrenCount < children.size(); childrenCount++) {
            Element eltChild = children.elementAt(childrenCount);
            if (!eltChild.isAssigned()) {
                this.assignChild(eltChild);
                if (!this.currentNode.getElements().contains(eltChild)) {
                    this.currentNode.addElement(eltChild);
                }
                ArchitectureModel.getInstance().getUnassignedElementsList().removeElement(eltChild);
            }
        }
    }

    private void nodeChoiceItemStateChanged(java.awt.event.ItemEvent evt) {// :event_nodeChoiceItemStateChanged
        // Add your handling code here:
        // this.m_currentNode =
        // this.m_nodes.get(this.m_nodeChoice.getSelectedIndex());
        // this.fillAssignedElementList();
    }

    class MyTreeMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        public void mousePressed(MouseEvent e) {
            //System.out.println("Mousse pressed in the tree");
            lastPath = assignedTreeView.getSelectionPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) assignedTreeView.getLastSelectedPathComponent();
            if (node instanceof NodeTreeNode) {
                currentNode = ((NodeTreeNode) node).getElement();
                addHierarchieBtn.setEnabled(true);
                removeHierarchieBtn.setEnabled(true);
                createNodeAndAddHierarchieBtn.setEnabled(true);
            } else {
                addHierarchieBtn.setEnabled(false);
                // removeHierarchieBtn.setEnabled(false);
                // createNodeAndAddHierarchieBtn.setEnabled(false);
            }

        }

        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
        }
    }
}

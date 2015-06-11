package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.compuware.caqs.presentation.applets.scatterplot.data.Element;

public class ScatterplotTree {
	/**
	 * 
	 */
	private static final long 	serialVersionUID = -5798076724172032782L;

	private	JTree						tree = null;
	private ScatterplotAppletContainer	container = null;


	public ScatterplotTree(ScatterplotAppletContainer p) {
		this.container = p;
		this.createTree();
	}

	private void createTree() {
		DefaultMutableTreeNode root = this.constructTree();
		this.tree = new JTree(root);
		this.tree.setEditable(false);
		this.tree.setRootVisible(false);
		this.tree.setShowsRootHandles(true);
		this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		this.tree.addTreeSelectionListener(new ScatterplotTreeSelectionListener(this.container));
	}

	public void refreshTree() {
		Container parentComponent = this.tree.getParent();
		parentComponent.remove(this.tree);
		this.createTree();
	}

	private DefaultMutableTreeNode constructTree() {
		Vector<Element> allElements = this.container.getDatasRetriever().getPlotDatas().getAllElements();
		SPMap tree = new SPMap("root");
		for(Element elt : allElements) {
			this.addElementToMap(tree, elt);
		}
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
		this.addNodes(root, tree);
		root = this.sortTree(root);
		return root;
	}

	private void addNodes(DefaultMutableTreeNode rootNode, SPMap eltMap) {
		Enumeration<SPMap> nodesValues = eltMap.elements();
		while(nodesValues.hasMoreElements()) {
			SPMap spmap = nodesValues.nextElement();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(spmap);
			this.addNodes(node, spmap);
			rootNode.add(node);
		}
	}

	private void addElementToMap(SPMap tree, Element elt) {
		String[] arbo = elt.getLabel().split("\\.");
		if(arbo!=null && arbo.length>0) {
			SPMap current = tree;
			for(int i=0; i<arbo.length; i++) {
				SPMap temp = current.get(arbo[i]);
				if(temp==null) {
					temp = new SPMap(arbo[i]);
					current.put(arbo[i], temp);
				}
				temp.addElement(elt);
				current = temp;
			}
		}
	}

	public JTree getComponent() {
		return this.tree;
	}

	public void clearSelection() {
		this.tree.clearSelection();
	}

	public DefaultMutableTreeNode sortTree(DefaultMutableTreeNode root) {
		List<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
		for (int i = 0; i < root.getChildCount(); i++) {
			nodes.add((DefaultMutableTreeNode)root.getChildAt(i));
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
			String nt = node.getUserObject().toString();
			for (int j=0; j<i; j++) {
				DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) root.getChildAt(j);
				String np = prevNode.getUserObject().toString();
				if (nt.compareToIgnoreCase(np)<0) {
					root.insert(node, j);
					root.insert(prevNode, i);
					node = prevNode;
				}
			}
		}
		if(!nodes.isEmpty()) {
			for(DefaultMutableTreeNode node : nodes) {
				sortTree(node);
			}
		}
		return root;
	}
}

class SPMap extends Hashtable<String, SPMap> {
	private static final long 	serialVersionUID = -51541722151033645L;
	private List<Element>		associatedElements = null;
	private String				nodeLabel = null;

	public SPMap(String s) {
		this.associatedElements = new ArrayList<Element>();
		this.nodeLabel = s;
	}

	public void addElement(Element elt) {
		this.associatedElements.add(elt);
	}

	public String getNodeLabel() {
		return this.nodeLabel;
	}

	public void setNodeLabel(String s) {
		this.nodeLabel = s;
	}

	public String toString() {
		return this.nodeLabel;
	}

	public List<Element> getAssociatedElements() {
		return this.associatedElements;
	}
}


class ScatterplotTreeSelectionListener implements TreeSelectionListener {
	private ScatterplotAppletContainer container = null;

	public ScatterplotTreeSelectionListener(ScatterplotAppletContainer c) {
		this.container = c;
	}

	public void valueChanged(TreeSelectionEvent e) {
		this.container.getDatasRetriever().getPlotDatas().deselectAll();
		this.container.getEastPanel().removeElementsFromList();
		Object o = e.getSource();
		if(o instanceof JTree) {
			JTree tree = (JTree) o;
			TreePath[] pathes = tree.getSelectionPaths();
			if(pathes!=null) {
				for(int i=0; i<pathes.length; i++) {
					Object cmp = pathes[i].getLastPathComponent();
					if(cmp instanceof DefaultMutableTreeNode) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)cmp;
						Object val = node.getUserObject();
						if(val instanceof SPMap) {
							SPMap value = (SPMap) val;
							List<Element> elts = value.getAssociatedElements();
							for(Element elt : elts) {
								elt.setSelected(true);
							}
						}
					}
				}
			}
		}		
		this.container.getEastPanel().majSelectedElementsList();
		this.container.getScatterplotPanel().redraw();
	}
}
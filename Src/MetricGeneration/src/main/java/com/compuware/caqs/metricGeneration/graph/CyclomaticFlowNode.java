/**
 * 
 */
package com.compuware.caqs.metricGeneration.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cwfr-fdubois
 *
 */
public class CyclomaticFlowNode extends Node {
	
	int nbLogicalOp = 0;
	
	List<Node> elseContent = new ArrayList<Node>();
	
	public CyclomaticFlowNode(String nodeText, int lineNo, int nbLogical) {
		super(nodeText, lineNo);
		this.nbLogicalOp = nbLogical;
	}

	public String getNodeText() {
		return this.nodeText + '(' + this.nbLogicalOp + ')';
	}
	
	public void setElseContent(List<Node> elseContent) {
		this.elseContent = elseContent;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#getReducedGraph()
	 */
	@Override
	public Node getReducedGraph(Class searchedNode) {
		Node result = null;
		List<Node> resultContent = getReducedGraph(this.content, searchedNode);
		List<Node> resultElseContent = getReducedGraph(this.elseContent, searchedNode);
		if (!resultContent.isEmpty() || !resultElseContent.isEmpty()) {
			result = new CyclomaticFlowNode(this.nodeText, this.lineNo, this.nbLogicalOp);
			result.setContent(resultContent);
			((CyclomaticFlowNode)result).setElseContent(resultElseContent);
		}
		return result;
	}

	public List<Node> getReducedGraph(List<Node> contentList, Class searchedNode) {
		List<Node> resultContent = new ArrayList<Node>();
		if (contentList != null && !contentList.isEmpty()) {
			Iterator<Node> contentIter = contentList.iterator();
			Node currentNode = null;
			Node subReducedNode = null;
			while (contentIter.hasNext()) {
				currentNode = contentIter.next();
				if (currentNode.getClass().isAssignableFrom(searchedNode)) {
					resultContent.add(currentNode);
				}
				else {
					subReducedNode = currentNode.getReducedGraph(searchedNode);
					if (subReducedNode != null) {
						resultContent.add(subReducedNode);
					}
				}
			}
		}
		return resultContent;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#calculateCyclomatic()
	 */
	@Override
	public int calculateCyclomatic() {
		int result = 1 + this.nbLogicalOp;
		if (!this.content.isEmpty() || !this.elseContent.isEmpty()) {
			result += calculateSubCyclomatic(this.content);
			result += calculateSubCyclomatic(this.elseContent);
		}
		return result;
	}
	
	private int calculateSubCyclomatic(List<Node> resultContent) {
		int result = 0;
		if (!resultContent.isEmpty()) {
			Iterator<Node> contentIter = resultContent.iterator();
			Node currentNode = null;
			while (contentIter.hasNext()) {
				currentNode = contentIter.next();
				result += currentNode.calculateCyclomatic();
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#calculateIntegration()
	 */
	@Override
	public int calculateIntegration() {
		int result = 0;
		if (!isContentEmpty()) {
			Iterator<Node> contentIter = this.content.iterator();
			Node currentNode = null;
			while (contentIter.hasNext()) {
				currentNode = contentIter.next();
				result += currentNode.calculateIntegration();
			}
			if (result > 0) {
				result += 1 + this.nbLogicalOp;
			}
		}
		return result;
	}

}

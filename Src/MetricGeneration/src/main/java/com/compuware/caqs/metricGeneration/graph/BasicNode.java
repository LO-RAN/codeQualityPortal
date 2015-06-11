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
public class BasicNode extends Node {

	public BasicNode(String nodeText, int lineNo) {
		super(nodeText, lineNo);
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#getReducedGraph()
	 */
	@Override
	public Node getReducedGraph(Class searchedNode) {
		Node result = null;
		List<Node> resultContent = new ArrayList<Node>();
		if (!isContentEmpty()) {
			Iterator<Node> contentIter = this.content.iterator();
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
		if (!resultContent.isEmpty()) {
			result = new BasicNode(this.nodeText, this.lineNo);
			result.setContent(resultContent);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#calculateCyclomatic()
	 */
	@Override
	public int calculateCyclomatic() {
		int result = 0;
		if (!isContentEmpty()) {
			Iterator<Node> contentIter = this.content.iterator();
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
		}
		return result;
	}

}

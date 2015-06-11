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
public abstract class Node {

	String nodeText;
	int lineNo;
	
	List<Node> content = new ArrayList<Node>();
	
	public Node(String nodeText, int lineNo) {
		this.nodeText = nodeText;
		this.lineNo = lineNo;
	}
	
	public String getNodeText() {
		return this.nodeText;
	}
	
	/**
	 * @return Returns the content.
	 */
	public List<Node> getContent() {
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(List<Node> content) {
		this.content = content;
	}

	public boolean isContentEmpty() {
		return this.content.isEmpty();
	}
	
	public abstract Node getReducedGraph(Class searchedNode);

	public abstract int calculateCyclomatic();
	
	public abstract int calculateIntegration();
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(getNodeText()).append(':').append(this.lineNo).append(' ');
		if (!this.content.isEmpty()) {
			result.append("{\n");
			Iterator<Node> contentIter = this.content.iterator();
			Node currentNode = null;
			while (contentIter.hasNext()) {
				currentNode = contentIter.next();
				result.append(currentNode.toString());
			}
			result.append("\n}\n");
		}
		return result.toString();
	}
	
}

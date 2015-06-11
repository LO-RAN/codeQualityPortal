/**
 * 
 */
package com.compuware.caqs.metricGeneration.graph;

/**
 * @author cwfr-fdubois
 *
 */
public class LeafNode extends Node {

	public LeafNode(String nodeText, int lineNo) {
		super(nodeText, lineNo);
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#getReducedGraph()
	 */
	@Override
	public Node getReducedGraph(Class searchedNode) {
		Node result = null;
		if (this.getClass().isAssignableFrom(searchedNode)) {
			result = this;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#calculateCyclomatic()
	 */
	@Override
	public int calculateCyclomatic() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#calculateIntegration()
	 */
	@Override
	public int calculateIntegration() {
		return 0;
	}

}

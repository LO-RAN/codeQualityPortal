/**
 * 
 */
package com.compuware.caqs.metricGeneration.graph;

/**
 * @author cwfr-fdubois
 *
 */
public class MethodCallNode extends LeafNode {

	public MethodCallNode(String nodeText, int lineNo) {
		super(nodeText, lineNo);
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.metricGeneration.graph.Node#calculateIntegration()
	 */
	@Override
	public int calculateIntegration() {
		return 1;
	}

}

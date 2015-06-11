package com.compuware.caqs.domain.dataschemas.callsto;

public class CallGraphEdge {

	private CallGraphNode node = null;
	private String lib = null;
	private int value;
	
	/**
	 * @return Returns the lib.
	 */
	public String getLib() {
		return lib;
	}
	
	/**
	 * @param lib The lib to set.
	 */
	public void setLib(String lib) {
		this.lib = lib;
	}
	
	/**
	 * @return Returns the node.
	 */
	public CallGraphNode getNode() {
		return node;
	}
	
	/**
	 * @param node The node to set.
	 */
	public void setNode(CallGraphNode node) {
		this.node = node;
	}
	
	/**
	 * @return Returns the value.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @param value The value to set.
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
}

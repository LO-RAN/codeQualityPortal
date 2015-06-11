package com.compuware.caqs.presentation.applets.scatterplot.data;

public class ElementType {
	/**
	 * id
	 */
	private String id;
	/**
	 * lib
	 */
	private String lib;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLib() {
		return lib;
	}
	public void setLib(String lib) {
		this.lib = lib;
	}
	
	public String toString() {
		return this.lib;
	}
}

package com.compuware.caqs.presentation.applets.scatterplot.data;


public class Metric implements Comparable<Metric> {
	private String id = null;
	private String lib = null;
	
	public Metric(String i, String l) {
		this.id = i;
		this.lib = l;
	}
	
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

    @Override
	public String toString() {
		return this.getLib();
	}

    public int compareTo(Metric m) {
        return this.getLib().compareToIgnoreCase(m.getLib());
    }
}
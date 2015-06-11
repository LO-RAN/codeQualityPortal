package com.compuware.caqs.domain.dataschemas.rights;

public class RightBean {
	private String id;
	private String lib;
	
	
	public RightBean(String id) {
		this.id = id;
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

}

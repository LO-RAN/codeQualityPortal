package com.compuware.caqs.ant.taskdef;

public class Entity {

	// mandatory attribute
	private String id="";
	
	// optionnal attribute
	private String option="";

	public void setId(String id) {
		this.id = id;
	}	
	public String getId() {
		return this.id;
	}
	
	public void setOption(String option) {
		this.option = option;
	}
	public String getOption() {
		return this.option;
	}
}

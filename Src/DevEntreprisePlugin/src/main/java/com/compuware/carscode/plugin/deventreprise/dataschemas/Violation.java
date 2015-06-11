package com.compuware.carscode.plugin.deventreprise.dataschemas;

public class Violation {
	private String owner;
	private String line;
	
	public Violation(String line, String owner) {
		this.line = line;
		this.owner = owner;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
}

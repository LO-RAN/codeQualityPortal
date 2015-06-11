package com.compuware.carscode.plugin.deventreprise.dataschemas;

public class Variable {
	private int id;
	private String name;
	private String owner;
	private String line;
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private boolean moreThan18Digits = false;
	public boolean isMoreThan18Digits() {
		return moreThan18Digits;
	}
	public void setMoreThan18Digits(boolean moreThan18Digits) {
		this.moreThan18Digits = moreThan18Digits;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}

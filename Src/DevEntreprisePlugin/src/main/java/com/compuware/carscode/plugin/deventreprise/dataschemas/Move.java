package com.compuware.carscode.plugin.deventreprise.dataschemas;

public class Move {
	private String from;
	private String to;
	private String line;
	private String owner;
	
	public Move(String f, String t) {
		from = f;
		to = t;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}

package com.compuware.caqs.ant.taskdef;

public class Connection {
    private String refid;
	private String protocol = "UNET";
	private String host = "localhost";
	private String port = "14000";
	private String user = "caqs";
	private String password = "cpwrcaqs";
	private String name = "uniface_flow";

	public void setRefid(String id) {
		this.refid = id;
	}
	public String getRefid() {
		return this.refid;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return this.protocol+":"+this.host+"+"+this.port+"|"+this.user+"|"+this.password+"|"+this.name;
	}
}

package com.compuware.carscode.plugin.deventreprise.dataschemas;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticViolation {
	private String code;
	private List<String> sourceLine;
	private List<String> fileLine;
	
	public DiagnosticViolation(String code) {
		this.sourceLine = new ArrayList<String>();
		this.fileLine = new ArrayList<String>();
		this.code = code;
	}
	
	public void addAll(DiagnosticViolation dv) {
		if(dv!=null) {
			this.sourceLine.addAll(dv.getSourceLine());
			this.fileLine.addAll(dv.getFileLine());
		}
	}
	
	public void addViolation(String sl, String fl) {
		this.sourceLine.add(sl);
		this.fileLine.add(fl);
	}

	public String getCode() {
		return code;
	}

	public List<String> getSourceLine() {
		return sourceLine;
	}

	public List<String> getFileLine() {
		return fileLine;
	}
	
	public String toString(String metricName) {
		StringBuffer sb = new StringBuffer();
		sb.append("    <metric id=\""+metricName+"\" value=\""+this.fileLine.size()+"\" lines=\"");
		boolean first = true;
		for(String line : fileLine) {
			if(!first) {
				sb.append(",");
			}
			if(!"".equals(line)) {
				sb.append(line);
			}
			first = false;
		}
		sb.append("\" />\n");
		return sb.toString();
	}	
}

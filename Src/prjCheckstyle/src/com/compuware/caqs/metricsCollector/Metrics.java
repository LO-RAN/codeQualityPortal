package com.compuware.caqs.metricsCollector;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.CPD.*;

import com.compuware.caqs.pmd.PMD;
public class Metrics {
	private Tokens tokens;
	private Map sources;
	private VBClassMetrics vbCM;
	private VBMethodMetrics vbMM;
	private PHPMetrics phpMM;
	private String langue;
	
	public Metrics(String dir, Language lang){
		
		langue=lang.getLanguage();
		if(langue=="VB"){
			GUI.extensionField.setText(".vb, .bas, .BAS, .frm, .FRM, .cls, .CLS, .ctl, .CTL");
			System.out.println("Module;LOC;VG;CLOC");
			vbCM=new VBClassMetrics(dir);
	        System.out.println("Function;LOC;VG;CLOC");
	        vbMM=new VBMethodMetrics(dir);
        
		}
		if(langue=="PHPParseur"){
			System.out.println("Module;LOC;VG;CLOC");
			phpMM=new PHPMetrics(dir);
	        
	        
			}
	}
	
	public String getClassResults(){
		if(langue=="VB"){
		return VBClassMetrics.results;
		}
		if(langue=="PHPParseur"){
			return PHPMetrics.fileResults;
		}
		return null;
	}
	
	public String getMethodResults(){
		if(langue=="VB"){
			return VBMethodMetrics.results;
		}
		if(langue=="PHPParseur"){
			return PHPMetrics.functionResults;
		}
		return null;
	}
	
	public int calculateVg(SourceCode source){
		List l=tokens.getTokens();
		String tok="";
		
		System.out.println("nb token : "+l.size());
		for(int i=0;i<l.size();i++){
			System.out.println("token : "+((TokenEntry)l.get(i)).getBeginLine());
		}
		return l.size();
	}
}

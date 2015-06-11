/*
 * XmlGenerator.java
 *
 * Created on 29 janvier 2004, 16:33
 */

package com.compuware.caqs.business.report;

import java.io.PrintWriter;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.toolbox.io.FileTools;
import java.util.Locale;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class XmlGenerator extends ReportGenerator {
    
    String mDtdLocation = "";
    
    /** Creates a new instance of XmlGenerator */
    public XmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, loc);
        setNoteSeuil(noteSeuil);
    }
    
    public void setDtdLocation(String dtdLocation) {
        this.mDtdLocation = dtdLocation;
    }
    
	
	protected String getTendanceLabel(double tendance, double note) {
	    String tendanceLabel = "unchanged";
        if (tendance < note && tendance > 0)
        	tendanceLabel = "up";
        else if (tendance > note)
            tendanceLabel = "down";
		else if (tendance == 0)
	        tendanceLabel = "none";
        return tendanceLabel;
	}
    
    protected void printReportHeader(PrintWriter out) {
        out.println("<?xml version=\"1.0\" encoding=\""+ Constants.GLOBAL_CHARSET +"\" ?>");
        out.println("<!DOCTYPE REPORT SYSTEM \"" + FileTools.concatPath(this.mDtdLocation, "report.dtd") + "\">");
        out.println("<REPORT>");
    }
    
    protected void printReportFooter(PrintWriter out) {
        out.println("</REPORT>");
    }
    
    protected String getXmlCompliantString(String src) {
        String result = "";
        if (src != null) {
            result = src.replaceAll("\"", "");
            result = result.replaceAll("<BR/>", "@BR/@");
            result = result.replaceAll("<BR />", "@BR/@");
            result = result.replaceAll("<BR\\s*/?+>", "@BR/@");
            result = result.replaceAll("<", "&lt;");
            result = result.replaceAll("&", "&amp;");
        }
        return result;
    }
    
}

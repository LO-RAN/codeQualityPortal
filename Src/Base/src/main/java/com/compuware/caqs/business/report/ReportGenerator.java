/*
 * ReportGenerator.java
 *
 * Created on 29 janvier 2004, 16:19
 */

package com.compuware.caqs.business.report;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class ReportGenerator {
    
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
	
    ElementBean eltBean = null;
    NumberFormat nf = null;
    NumberFormat intf = null;
    NumberFormat pctf = null;
    double noteSeuil = 3.0;
    
    Locale locale = null;
    
    /** Creates a new instance of ReportGenerator */
    public ReportGenerator(ElementBean eltBean, Locale loc) {
        this.locale=loc;
    	this.eltBean = eltBean;
        this.nf = StringFormatUtil.getMarkFormatter(locale);
        this.intf = StringFormatUtil.getIntegerFormatter(locale);
        this.pctf = StringFormatUtil.getFormatter(locale, 1, 1);
    }
    
    public double getNoteSeuil() {
		return this.noteSeuil;
	}

	public void setNoteSeuil(double noteSeuil) {
		this.noteSeuil = noteSeuil;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale loc) {
		this.locale = loc;
	}

	public abstract void retrieveData();
    
    public abstract void generate(File destinationDir) throws java.io.IOException;
    
    protected PrintWriter createWriter(File destination, String name) throws IOException {
        File f = new File(destination, name);
        OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(f), Constants.GLOBAL_CHARSET);
        BufferedWriter buf = new BufferedWriter(w);
        PrintWriter out = new PrintWriter(buf);
        return out;
    }
        
}

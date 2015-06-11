
/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) François-Xavier ALBOUY<p>
 * Société : Software & Process<p>
 * @author François-Xavier ALBOUY
 * @version 1.0
 */
package com.compuware.caqs.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class LogWriter {
    public static final int NONE = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int DEBUG = 3;
    
    protected static final String ERROR_TEXT = "error";
    protected static final String INFO_TEXT = "info";
    protected static final String DEBUG_TEXT = "debug";
    
    protected PrintWriter pw;
    protected String owner;
    protected int loggerLevel;
    
    public LogWriter(String owner, int logLevel, PrintWriter pw) {
        this.pw = pw;
        this.owner = owner;
        this.loggerLevel = logLevel;
    }
    
    public LogWriter(String owner, int logLevel) {
        this(owner, logLevel, null);
    }
    
    public int getLogLevel() {
        return this.loggerLevel;
    }
    
    public PrintWriter getPrintWriter() {
        return this.pw;
    }
    
    public void setLogLevel(int logLevel) {
        this.loggerLevel = logLevel;
    }
    
    public void setPrintWriter(PrintWriter pPw) {
        this.pw = pPw;
    }
    
    public void log(String msg, int severityLevel) {
        if (this.pw != null) {
            if (severityLevel <= this.loggerLevel) {
                this.pw.println("[" + new Date() + "]  " +
                getSeverityString(severityLevel) + ": " +
                this.owner + ": " + msg);
            }
        }
    }
    
    public void log(Throwable t, String msg, int severityLevel) {
        log(msg + " : " + toTrace(t), severityLevel);
    }
    
    private String getSeverityString(int severityLevel) {
        String retour = "Unknown";
        switch (severityLevel) {
            case ERROR:
                retour = ERROR_TEXT;
                break;
            case INFO:
                retour = INFO_TEXT;
                break;
            case DEBUG:
                retour = DEBUG_TEXT;
                break;
            default:
                retour = "Unknown";
        }
        return retour;
    }
    
    private String toTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter tPw = new PrintWriter(sw);
        e.printStackTrace(tPw);
        tPw.flush();
        return sw.toString();
    }
}
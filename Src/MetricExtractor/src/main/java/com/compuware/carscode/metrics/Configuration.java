package com.compuware.carscode.metrics;

import java.util.Properties;

/** Configuration classe for MetricExtractors.
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 27 févr. 2006
 * Time: 12:23:58
 */
public class Configuration {

    public static final String FILEFILTER_REGEXP_KEY = "metrics.regexp.filefilter";
    public static final String MAINPROC_REGEXP_SEPARATOR_KEY = "metrics.regexp.mainproc.separator";
    public static final String MAINPROC_REGEXP_KEY = "metrics.regexp.mainproc";
    public static final String PROC_REGEXP_KEY = "metrics.regexp.proc";
    public static final String END_REGEXP_KEY = "metrics.regexp.end";
    public static final String COMMENT_REGEXP_KEY = "metrics.regexp.comments";
    public static final String EMPTYLINE_REGEXP_KEY = "metrics.regexp.empty";
    public static final String COMPLEXITY_REGEXP_KEY = "metrics.regexp.complexity";
    public static final String NEEDEND_REGEXP_KEY = "metrics.regexp.needEnd";

    private String fileFilterRegexp;
    private String mainProcRegexpSeparator;
    private String[] mainProcRegexp;
    private String procRegexp;
    private String[] endRegexp;
    private String[] commentRegexp;
    private String emptyRegexp;
    private String[] complexityRegexp;
    private String[] needEndRegexp;

    public String getFileFilterRegexp() {
        return fileFilterRegexp;
    }

    public void setFileFilterRegexp(String fileFilterRegexp) {
        this.fileFilterRegexp = fileFilterRegexp;
    }

    public String[] getCommentRegexp() {
        return commentRegexp;
    }

    public void setCommentRegexp(String[] commentRegexp) {
        this.commentRegexp = commentRegexp;
    }

    public String getEmptyRegexp() {
        return emptyRegexp;
    }

    public void setEmptyRegexp(String emptyRegexp) {
        this.emptyRegexp = emptyRegexp;
    }

    public String[] getComplexityRegexp() {
        return complexityRegexp;
    }

    public void setComplexityRegexp(String[] complexityRegexp) {
        this.complexityRegexp = complexityRegexp;
    }
    
    public String[] getNeedEndRegexp() {
        return needEndRegexp;
    }

    public void setNeedEndRegexp(String[] needEndRegexp) {
        this.needEndRegexp = needEndRegexp;
    }
    
	public String[] getEndRegexp() {
		return endRegexp;
	}

	public void setEndRegexp(String[] endRegexp) {
		this.endRegexp = endRegexp;
	}

	public String getMainProcRegexpSeparator() {
		return mainProcRegexpSeparator;
	}

	public void setMainProcRegexpSeparator(String mainProcRegexpSeparator) {
		this.mainProcRegexpSeparator = mainProcRegexpSeparator;
	}

	public String[] getMainProcRegexp() {
		return mainProcRegexp;
	}

	public void setMainProcRegexp(String[] mainProcRegexp) {
		this.mainProcRegexp = mainProcRegexp;
	}

	public String getProcRegexp() {
		return procRegexp;
	}

	public void setProcRegexp(String procRegexp) {
		this.procRegexp = procRegexp;
	}

    public void init(Properties prop) {
        fileFilterRegexp = prop.getProperty(FILEFILTER_REGEXP_KEY);
        mainProcRegexpSeparator = prop.getProperty(MAINPROC_REGEXP_SEPARATOR_KEY);
        String mainProcRegexpStr = prop.getProperty(MAINPROC_REGEXP_KEY);
        mainProcRegexp = mainProcRegexpStr.split(mainProcRegexpSeparator);
        procRegexp = prop.getProperty(PROC_REGEXP_KEY);
        String endRegexpStr = prop.getProperty(END_REGEXP_KEY);
        endRegexp = endRegexpStr.split(",");
        String commentRegexpStr = prop.getProperty(COMMENT_REGEXP_KEY);
        commentRegexp = commentRegexpStr.split(",");
        emptyRegexp = prop.getProperty(EMPTYLINE_REGEXP_KEY);
        String complexityRegexpStr = prop.getProperty(COMPLEXITY_REGEXP_KEY);
        complexityRegexp = complexityRegexpStr.split(",");
        String needEndRegexpStr = prop.getProperty(NEEDEND_REGEXP_KEY);
        needEndRegexp = needEndRegexpStr.split(",");
    }

}

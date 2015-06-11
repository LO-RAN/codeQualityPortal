/*
 * ListAndRegexpFilter.java
 *
 * Created on 11 mars 2004, 17:11
 */

package com.compuware.toolbox.io.filter;

/**
 *
 * @author  cwfr-fdubois
 */
public class RegexpFilter implements java.io.FileFilter {
    
    private String regexp = null;
    private boolean acceptDirectory = false;

    /** Creates a new instance of ListAndRegexpFilter */
    public RegexpFilter() {
    }
    
    public boolean accept(java.io.File file) {
        boolean result = false;
        if (file != null) {
	        if (!file.isDirectory()) {
	            String fileName = file.getName();
	            result = java.util.regex.Pattern.matches(regexp, fileName);
	        }
	        else if (acceptDirectory) {
	            result = true;
	        }
        }
        return result;
    }
    
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Class: ");
        buff.append(this.getClass().getName());
        buff.append('\n');
        buff.append("Regexp: ");
        buff.append(this.regexp);
        buff.append('\n');
        return buff.toString();
    }
    
    public void setRegexp(java.lang.String regexp) {
        this.regexp = regexp;
    }

    public void setAcceptDirectory(boolean acceptDirectory) {
        this.acceptDirectory = acceptDirectory;
    }

	public boolean isAcceptDirectory() {
		return this.acceptDirectory;
	}

	public String getRegexp() {
		return this.regexp;
	}

    /*
    public static void main(String[] args) {
        java.io.File f = new java.io.File("D:/Copytest/srccopy/");
        RegexpFilter filter = new RegexpFilter();
        filter.setRegexp("C[A-Z0-9]B[A-Z0-9]{3,}AK?.cob");
        System.out.println(filter);
        File[] res = f.listFiles(filter);
        System.out.println(res[0]);
    }
    */

}

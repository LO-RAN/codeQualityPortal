/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.CPD;

import java.io.File;
import java.io.FilenameFilter;

public class AnyLanguage implements Language {

    public static class AnyFileOrDirectoryFilter implements FilenameFilter {
    	String extension;
    	public AnyFileOrDirectoryFilter(String extension) {
    		this.extension = extension;
    	}
        public boolean accept(File dir, String filename) {
            return filename.endsWith(extension) || (new File(dir.getAbsolutePath() + System.getProperty("file.separator") + filename).isDirectory());
        }
    }

    public String getLanguage() {
        return "any";
    }
    
    private AnyTokenizer tokenizer;
    private String extension;
    
    public AnyLanguage(String extension) {
    	this.extension = extension;
        tokenizer = new AnyTokenizer();
    }
    
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public FilenameFilter getFileFilter() {
        return new AnyFileOrDirectoryFilter(this.extension);
    }
}

/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 * @authors: Zev Blut zb@ubit.com
 */
package com.compuware.caqs.CPD;

import java.io.File;
import java.io.FilenameFilter;

public class VBLanguage implements Language {

    public static class RubyFileOrDirectoryFilter implements FilenameFilter {
        public boolean accept(File dir, String filename) {
            return filename.endsWith("vb") || filename.endsWith("frm") || 
		filename.endsWith("cls") || filename.endsWith("bas") || 
		(new File(dir.getAbsolutePath() + System.getProperty("file.separator") + filename).isDirectory());
        }
    }

    public Tokenizer getTokenizer() {
        return null;//new RubyTokenizer();
    }

    public String getLanguage() {
        return "VB";
    }
    
    public FilenameFilter getFileFilter() {
        return null;//new RubyFileOrDirectoryFilter();
    }
}

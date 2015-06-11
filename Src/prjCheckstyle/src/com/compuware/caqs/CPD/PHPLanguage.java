/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.CPD;

import java.io.File;
import java.io.FilenameFilter;

public class PHPLanguage implements Language {

    public static class PHPFileOrDirectoryFilter implements FilenameFilter {
        public boolean accept(File dir, String filename) {
            return filename.endsWith("php") || filename.endsWith("class") || (new File(dir.getAbsolutePath() + System.getProperty("file.separator") + filename).isDirectory());
        }
    }

    public Tokenizer getTokenizer() {
        return new PHPTokenizer();
    }

    public String getLanguage() {
        return "PHP";
    }
    
    public FilenameFilter getFileFilter() {
        return new PHPFileOrDirectoryFilter();
    }
}

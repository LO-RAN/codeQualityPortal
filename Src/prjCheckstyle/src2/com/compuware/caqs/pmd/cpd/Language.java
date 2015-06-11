/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.cpd;

import java.io.FilenameFilter;

public interface Language {
	
	String fileSeparator = System.getProperty("file.separator");
	
    public Tokenizer getTokenizer();

    public FilenameFilter getFileFilter();
}

/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.CPD;

import java.io.FilenameFilter;

public interface Language {
    public Tokenizer getTokenizer();
    public String getLanguage();
    public FilenameFilter getFileFilter();
}

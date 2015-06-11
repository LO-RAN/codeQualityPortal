/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.pmd.quickfix;

public interface Fix {
    String fix(String code, int lineNumber);

    String getLabel();
}

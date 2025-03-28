/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package com.compuware.caqs.CPD;

import java.util.HashMap;
import java.util.Map;

public class TokenEntry implements Comparable {

    public static final TokenEntry EOF = new TokenEntry();

    private String tokenSrcID;
    private int beginLine;
    private int index;
    private int identifier;
    private int hashCode;

    private final static Map Tokens = new HashMap();
    private static int TokenCount = 0;

    private TokenEntry() {
        this.identifier = 0;
        this.tokenSrcID = "EOFMarker";
    }

    public TokenEntry(String image, String tokenSrcID, int beginLine) {
        Integer i = (Integer) Tokens.get(image);
        if (i == null) {
            i = new Integer(Tokens.size() + 1);
            Tokens.put(image, i);
        }
        this.identifier = i.intValue();
        this.tokenSrcID = tokenSrcID;
        this.beginLine = beginLine;
        this.index = TokenCount++;
    }

    public static TokenEntry getEOF() {
        TokenCount++;
        return EOF;
    }

    public static void clearImages() {
        Tokens.clear();
        TokenCount = 0;
    }

    public String getTokenSrcID() {
        return tokenSrcID;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public int getIndex() {
        return this.index;
    }

    public int hashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public boolean equals(Object o) {
        TokenEntry other = (TokenEntry) o;
        return other.hashCode == hashCode;
    }

    public int compareTo(Object o) {
        TokenEntry other = (TokenEntry) o;
        return getIndex() - other.getIndex();
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.domain.dataschemas.upload;

/**
 *
 * @author cwfr-dzysman
 */
public enum Separator {

    SEMI_COLON("semiColon", ';'),
    COMMA("comma", ','),
    TABULATION("tabulation", '\t');
    private String id;
    private char separatorChar;

    private Separator(String i, char c) {
        this.id = i;
        this.separatorChar = c;
    }

    public String getId() {
        return this.id;
    }

    public char getCharSeparator() {
        return this.separatorChar;
    }

    public static Separator fromId(String i) {
        Separator retour = null;
        if (i != null) {
            for (Separator s : Separator.values()) {
                if(i.equals(s.getId())) {
                    retour = s;
                    break;
                }
            }
        }
        return retour;
    }
}

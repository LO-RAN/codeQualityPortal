package com.compuware.caqs.pmd.ant;

public class RuleSetWrapper {
    private String file;

    public final String getFile() {
        return file;
    }

    public final void addText(String t) {
        this.file = t;
    }
}

package com.compuware.caqs.pmd.renderers;

import java.io.IOException;
import java.io.StringWriter;

import com.compuware.caqs.pmd.Report;

public abstract class AbstractRenderer implements Renderer {

    protected boolean showSuppressedViolations = true;

    public void showSuppressedViolations(boolean show) {
        this.showSuppressedViolations = show;
    }

    public String render(Report report) {
        StringWriter w = new StringWriter();
        try {
            render(w, report);
        } catch (IOException e) {
            throw new Error("StringWriter doesn't throw IOException", e);
        }
        return w.toString();
    }

}

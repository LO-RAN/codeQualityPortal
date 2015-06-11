/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.upload;

/**
 *
 * @author cwfr-dzysman
 */
public enum FileType {
    XML_METRICS("upload.xmlmetrics", false),
    CSV_METRICS("upload.csvmetrics", true),
    ISTROBE_PROFILE("upload.strobe", false),
    CODE_COVERAGE_DEVPARTNER("upload.csvdevpartnercov", false),
    CALLS_TO("upload.csvcallsto", false);

    private String id;
    private boolean separator;

    private FileType(String i, boolean n) {
        this.id = i;
        this.separator = n;
    }

    public String getId() {
        return this.id;
    }

    public boolean needsSeparator() {
        return this.separator;
    }
}

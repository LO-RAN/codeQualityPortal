package com.compuware.carscode.filesearch.bean;

import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 16 févr. 2006
 * Time: 17:24:33
 * To change this template use File | Settings | File Templates.
 */
public class ViolationBean {
    private String fileName;
    private int nb = 0;
    private String lines = "";

    public ViolationBean(String fileName) {
        this.fileName = fileName;
    }

    public void addViolation(String line) {
        if (nb > 0) {
            lines = lines + ',';
        }
        lines = lines + line;
        nb++;
    }

    public void print(String errorLib, PrintWriter out) {
        if (nb > 0) {
            out.print(fileName);
            out.print(" line ");
            out.print(lines);
            out.print(";");
            out.print(errorLib);
            out.print(";");
            out.print(nb);
            out.println(";");
        }
    }
}

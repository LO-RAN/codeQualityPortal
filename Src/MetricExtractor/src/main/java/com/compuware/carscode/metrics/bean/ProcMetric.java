package com.compuware.carscode.metrics.bean;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 24 févr. 2006
 * Time: 12:31:37
 * To change this template use File | Settings | File Templates.
 */
public class ProcMetric {
    private String name;
    private int nocl;
    private int cloc;
    private int vg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNocl() {
        return nocl;
    }

    public void setNocl(int nocl) {
        this.nocl = nocl;
    }

    public int getCloc() {
        return cloc;
    }

    public void setCloc(int cloc) {
        this.cloc = cloc;
    }

    public int getVg() {
        return vg;
    }

    public void setVg(int vg) {
        this.vg = vg;
    }

    public void remove(Collection others) {
        Iterator i = others.iterator();
        while(i.hasNext()) {
            ProcMetric  procMet = (ProcMetric)i.next();
            this.nocl -= procMet.getNocl();
            this.vg -= procMet.getVg();
            this.cloc -= procMet.getCloc();
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(name).append(";");
        result.append(nocl).append(";");
        result.append(vg+1).append(";");
        result.append(cloc).append(";");
        return result.toString();
    }
}

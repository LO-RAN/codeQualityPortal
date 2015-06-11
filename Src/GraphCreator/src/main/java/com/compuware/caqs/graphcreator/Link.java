/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.graphcreator;

/**
 *
 * @author cwfr-fdubois
 */
public class Link implements Comparable {

    private String source;
    private String target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int compareTo(Object o) {
        int result = 0;
        if (o != null && o instanceof Link) {
            result = this.source.compareTo(((Link)o).getSource());
            if (result == 0) {
                result = this.target.compareTo(((Link)o).getTarget());
            }
        }
        return result;
    }

    public boolean equals(Object o) {
        boolean result = false;
        if (o != null && o instanceof Link) {
            result = this.source.equals(((Link)o).getSource())&& this.target.equals(((Link)o).getTarget());
        }
        return result;
    }

}

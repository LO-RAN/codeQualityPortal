/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.graphcreator;

/**
 *
 * @author cwfr-fdubois
 */
public class Node implements Comparable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int compareTo(Object o) {
        return this.id.compareTo(((Node)o).getId());
    }

    public boolean equals(Object o) {
        return this.id.equals(((Node)o).getId());
    }

}

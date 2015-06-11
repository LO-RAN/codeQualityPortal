/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.applets.graphapplet;

/**
 *
 * @author cwfr-fdubois
 */
public class NodeData {

    private String id;
    private String desc;
    private String lib;

    private boolean mainNode;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLib() {
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public boolean isMainNode() {
        return mainNode;
    }

    public void setMainNode(boolean mainNode) {
        this.mainNode = mainNode;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o != null && o instanceof NodeData) {
            result = this.id.equals(((NodeData)o).getId());
        }
        return result;
    }

}

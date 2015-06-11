/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 *
 * @author cwfr-dzysman
 */
public class LanguageBean implements Serializable {
    private String id;
    private String lib;
    private String desc;

    public LanguageBean(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getLib() {
        return lib;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}

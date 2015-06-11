package com.compuware.caqs.domain.dataschemas.modeleditor;

import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class LangueBean {
    private String id;
    private String lib;
    private String desc;
    //number of texts declared for this language
    private int nbTexts;
    private Locale locale;

    public int getNbTexts() {
        return nbTexts;
    }

    public void setNbTexts(int nbTexts) {
        this.nbTexts = nbTexts;
    }

    public LangueBean() {
        
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LangueBean(String id) {
        this.id = id;
        this.locale = new Locale(id);
    }

    public String getId() {
        return id;
    }

    public String getLib() {
        return lib;
    }

    public void setId(String id) {
        this.id = id;
        this.locale = new Locale(id);
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public Locale getLocale() {
        return this.locale;
    }
}

package com.compuware.caqs.domain.dataschemas.webservices;

/**
 *
 * @author cwfr-dzysman
 */
public class ProjectWS {
    private String id;
    private String lib;
    private String fullPath;

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
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

    
}

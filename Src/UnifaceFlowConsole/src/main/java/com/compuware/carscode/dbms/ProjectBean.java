package com.compuware.carscode.dbms;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 5 janv. 2006
 * Time: 11:41:01
 * DTO used for project information management.
 */
public class ProjectBean {

    private String id;
    private String lib;

    /**
     * Return the projectId.
     * @return the projectId
     */
    public String getId() {
        return id;
    }

    /**
     * Set the project ID.
     * @param id the project ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return the project name.
     * @return the project name.
     */
    public String getLib() {
        return lib;
    }

    /**
     * Set the project name.
     * @param lib the project name.
     */
    public void setLib(String lib) {
        this.lib = lib;
    }

}

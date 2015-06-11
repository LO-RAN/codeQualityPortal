/*
 * Users.java
 *
 * Created on 29 octobre 2002, 12:11
 */
package com.compuware.caqs.security.auth;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-sbedu
 */
public class Users extends SessionBindingListener {

    // d�claration du logger
    private static Logger logger = Logger.getLogger("Ihm");
    // Users Infos
    /** Prenom de l'utilisateur
     */
    private String firstName = "";
    /** Nom de l'utilisateur
     */
    private String lastName = "";
    /** Email de l'utilisateur
     */
    private String email = "";
    private Map<String, String> accessRights = null;
    private Map<String, String> roleMap = null;

    /** Cree une nouvelle instance de la classe Users */
    public Users() {
    }

    /** M�thode d'acc�s au prenom de l'utilisateur.
     * @return le prenom de l'utilisateur
     */
    public String getFirstName() {
        return this.firstName;
    }

    /** M�thode d'acc�s au nom de l'utilisateur.
     * @return le nom de l'utilisateur
     */
    public String getlastName() {
        return this.lastName;
    }

    /** M�thode d'acc�s � l'email de l'utilisateur.
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return this.email;
    }

    /** M�thode d'acc�s au prenom de l'utilisateur.
     * @param fname prenom de l'utilisateur
     */
    public void setFirstName(String fname) {
        this.firstName = fname;
    }

    /** M�thode d'acc�s au nom de l'utilisateur.
     * @param lname nom de l'utilisateur
     */
    public void setLastName(String lname) {
        this.lastName = lname;
    }

    /** M�thode d'acc�s � l'email de l'utilisateur.
     * @param email email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param accessRights the accessRights to set
     */
    public void setAccessRights(Map<String, String> accessRights) {
        this.accessRights = accessRights;
    }

    /**
     * @param roleMap the role map to set
     */
    public void setRoles(Map<String, String> pRoleMap) {
        this.roleMap = pRoleMap;
    }

    /**
     * Check for user access to the given part.
     * @param key the part key.
     * @return true if the user has access to the given part, false else.
     */
    public boolean access(String key) {
        boolean result = false;
        if (this.accessRights != null && key != null) {
            result = this.accessRights.containsKey(key.toUpperCase());
        }
        logger.debug("Users.access(" + key + ") => " + result);
        return result;
    }

    public boolean isInRole(String[] roleArray) {
        boolean result = false;
        for (int i = 0; i < roleArray.length && !result; i++) {
            result = (this.roleMap.containsKey(roleArray[i]));
        }
        return result;
    }

    public String toString() {
        return this.userId;
    }
}

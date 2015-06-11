/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.security.userdetails;

import org.springframework.security.userdetails.ldap.LdapUserDetailsImpl;

/**
 * The common ldap information for user.
 * @author cwfr-fdubois
 */
public class CommonLdapUserDetails extends LdapUserDetailsImpl implements CommonUserDetails {

    /** The user lastname. */
    private String lastname;

    /** The user firstname. */
    private String firstname;

    /** The user email. */
    private String email;

    /** {@inheritDoc} */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /** {@inheritDoc} */
    public String getLastname() {
        return this.lastname;
    }

    /** {@inheritDoc} */
    public void setFirstname(String firstname) {
        this.setFirstname(firstname);
    }

    /** {@inheritDoc} */
    public String getFirstname() {
        return this.firstname;
    }

    /** {@inheritDoc} */
    public void setEmail(String email) {
        this.setEmail(email);
    }

    /** {@inheritDoc} */
    public String getEmail() {
        return this.email;
    }

}

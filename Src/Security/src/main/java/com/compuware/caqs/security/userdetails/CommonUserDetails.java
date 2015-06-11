/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.security.userdetails;

import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author cwfr-fdubois
 */
public interface CommonUserDetails extends UserDetails {

    /**
     * Set the user lastname
     * @param lastname the user lastname.
     */
    void setLastname(String lastname);

    /**
     * Get the user lastname.
     * @return the user lastname.
     */
    String getLastname();

    /**
     * Set the user firstname
     * @param firstname the user firstname.
     */
    void setFirstname(String firstname);

    /**
     * Get the user firstname.
     * @return the user firstname.
     */
    String getFirstname();

    /**
     * Set the user email.
     * @param email the user email.
     */
    void setEmail(String email);

    /**
     * get the user email.
     * @return the user email.
     */
    String getEmail();

}

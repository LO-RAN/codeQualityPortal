/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.business.security;

import java.util.List;

import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.security.auth.Users;

/**
 *
 * @author cwfr-fdubois
 */
public interface IPortal {

    /**
     * Get portal user information.
     * @param id the user id.
     * @return the user information.
     * @throws com.compuware.caqs.exception.CaqsException if a portal access error occured.
     */
    Users getUserInfos(String id) throws CaqsException;

    /**
     * Retrieve all users from the portal database.
     * @return the list of all users registered in the portal.
     * @throws com.compuware.caqs.exception.CaqsException if a portal access error occured.
     */
    List<UserBean> getAllUsers() throws CaqsException;

}

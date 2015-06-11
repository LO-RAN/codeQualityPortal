package com.compuware.caqs.dao.interfaces;

import java.util.List;

import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.security.auth.Users;

/**
 * The Portal database accessor.
 * @author cwfr-fdubois
 */
public interface PortalUserDao {

    /**
     * Get portal user information.
     * @param id the user id.
     * @return the user information.
     * @throws com.compuware.caqs.exception.DataAccessException occured if an exception occured during dao execution.
     */
    Users getUserInfos(String id) throws DataAccessException;

    /** Recupere tous les utilisateur du portail.
    * @return Collection contenant les Id et les noms des utilisateurs
    * @throws com.compuware.caqs.exception.DataAccessException occured if an exception occured during dao execution.
    */
    List<UserBean> getAllUsers() throws DataAccessException;

}

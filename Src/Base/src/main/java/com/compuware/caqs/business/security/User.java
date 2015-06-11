/**
 * 
 */
package com.compuware.caqs.business.security;

import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.UsersDao;
import com.compuware.caqs.domain.dataschemas.UserBean;

/**
 * @author cwfr-fdubois
 *
 */
public class User {

    public List<UserBean> retrieveAllUsersByElementId(String idElt) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsersDao userFacade = daoFactory.getUsersDao();
        return userFacade.retrieveAllUsersByElementId(idElt);
    }

    public List<UserBean> retrieveAllUsersByFatherElementId(String idElt) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsersDao userFacade = daoFactory.getUsersDao();
        return userFacade.retrieveAllUsersByFatherElementId(idElt);
    }

    public void updateRights(String id, String[] userArray) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsersDao userDao = daoFactory.getUsersDao();
        userDao.deleteRights(id);
        userDao.addRights(id, userArray);
    }

}

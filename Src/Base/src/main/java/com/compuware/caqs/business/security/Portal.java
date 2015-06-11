package com.compuware.caqs.business.security;

import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.PortalUserDao;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.security.auth.Users;

/**
 * The Portal accessor.
 * @author cwfr-fdubois
 */
public class Portal implements IPortal {

    /**
     * {@inheritDoc}
     */
    public List<UserBean> getAllUsers() throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        PortalUserDao portalUserDao = daoFactory.getPortalUserDao();
        return portalUserDao.getAllUsers();
    }

    /**
     * {@inheritDoc}
     */
    public Users getUserInfos(String id) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        PortalUserDao portalUserDao = daoFactory.getPortalUserDao();
        return portalUserDao.getUserInfos(id);
    }
}

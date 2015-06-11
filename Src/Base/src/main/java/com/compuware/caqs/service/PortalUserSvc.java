package com.compuware.caqs.service;

import java.util.List;

import com.compuware.caqs.business.security.IPortal;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;

/**
 * The portal access service.
 * @author cwfr-fdubois
 */
public class PortalUserSvc {

    /** The spring key for Portal accessor bean. */
    private static final String PORTAL = "portal_service_provider";
    /** The service singleton. */
    private static PortalUserSvc instance = new PortalUserSvc();

    /**
     * Private constructor to initialize the singleton.
     */
    private PortalUserSvc() {
    }

    /**
     * Get the unique instance of the service.
     * @return the unique instance of the service.
     */
    public static PortalUserSvc getInstance() {
        return instance;
    }

    /**
     * Retrieve all users from the portal database.
     * @return the list of all users registered in the portal.
     * @throws com.compuware.caqs.exception.CaqsException if a portal access error occured.
     */
    public List<UserBean> getAllUsers() throws CaqsException {
        BusinessFactory factory = BusinessFactory.getInstance();
        IPortal portal = (IPortal) factory.getBean(PORTAL);
        return portal.getAllUsers();
    }

    /**
     * Get portal user information.
     * @param id the user id.
     * @return the user information.
     * @throws com.compuware.caqs.exception.CaqsException if a portal access error occured.
     */
    public Users getUserInfos(String id) throws CaqsException {
        BusinessFactory factory = BusinessFactory.getInstance();
        IPortal portal = (IPortal) factory.getBean(PORTAL);
        return portal.getUserInfos(id);
    }
}

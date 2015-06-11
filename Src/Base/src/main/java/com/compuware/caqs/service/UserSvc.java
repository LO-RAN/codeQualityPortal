package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;

/**
 *
 * @author cwfr-lizac
 */
public class UserSvc {

    private static final UserSvc instance = new UserSvc();

    private UserSvc() {
    }

    public static UserSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * retrieves users by id and/or lastname
     * @param id user's id pattern
     * @param lastname user's lastname pattern
     * @return list of users
     */
    public List<UserBean> retrieveUsersByIdLastname(String id, String lastname) {
        return DaoFactory.getInstance().getUserDao().retrieveUsersByIdLastname(id, lastname);
    }

    /**
     * retrieves a user by id
     * @param id user's id pattern
     * @return a user or null
     */
    public UserBean retrieveUserById(String id) {
        return DaoFactory.getInstance().getUserDao().retrieveUserById(id);
    }

    /**
     * saves a userbean, updating it if it already exists, creating it otherwise
     * @param lb the userbean to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveUserBean(UserBean ub) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getUserDao().saveUserBean(ub);
        } catch (DataAccessException exc) {
            logger.error(exc.getMessage());
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes a userbean
     * @param id the id of the userbean to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteUserBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getUserDao().deleteUserBean(id);
        } catch (DataAccessException exc) {
            logger.error(exc.getMessage());
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }
    /**
     * retrieves all users
     * @return all users or an empty list if none exists
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public List<UserBean> retrieveAllusers() {
        return DaoFactory.getInstance().getUserDao().retrieveAllUsers();
    }

    public List<RoleBean> retrieveAllRoles() {
        return DaoFactory.getInstance().getUserDao().getAllCaqsRoles();
    }

    public List<RoleBean> retrieveUserRoles(String userId) {
        return DaoFactory.getInstance().getUserDao().getUserRoles(userId);
    }

}

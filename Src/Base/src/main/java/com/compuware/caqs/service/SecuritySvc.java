package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.StatisticsDao;
import com.compuware.caqs.dao.interfaces.UsersDao;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.security.stats.ConnectionStatData;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 *
 */
public class SecuritySvc {

    private static final SecuritySvc instance = new SecuritySvc();
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private SecuritySvc() {
    }

    public static SecuritySvc getInstance() {
        return instance;
    }

    public Map<String, String> retrieveAccessRightFromGroups(Collection<String> groups) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsersDao usersDao = daoFactory.getUsersDao();
        return usersDao.retrieveUserAccessRights(groups);
    }

    public Map<String, String> retrieveRolesFromGroups(Collection<String> groups) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsersDao usersDao = daoFactory.getUsersDao();
        return usersDao.retrieveRoleFromGroup(groups);
    }

    public void udpateLastLoginTime(String userId) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsersDao usersDao = daoFactory.getUsersDao();
        try{
        usersDao.udpateLastLoginTime(userId);
                } catch (DataAccessException exc) {
            logger.error(exc.getMessage());
        }
    }

    public void updateStatistics(ConnectionStatData stats) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StatisticsDao statisticsDao = daoFactory.getStatisticsDao();
        statisticsDao.updateStatistics(stats);
    }

    public List<ConnectionStatData> retrieveAllStatistics() throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StatisticsDao statisticsDao = daoFactory.getStatisticsDao();
        return statisticsDao.retrieveAllStatistics();
    }

    public ConnectionStatData retrieveLastStatistics() throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StatisticsDao statisticsDao = daoFactory.getStatisticsDao();
        return statisticsDao.retrieveLastStatistics();
    }
}

package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.StatisticsDao;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.security.stats.ConnectionStatData;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

import org.apache.log4j.Logger;

/**
 * Statistics data access object class.
 * @author  cwfr-fdubois
 */
public class StatisticsDbmsDao implements StatisticsDao {

    /** The logger. */
    private static Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);

    /** The unique instance of the DAO. */
    private static StatisticsDao singleton = new StatisticsDbmsDao();

    /**
     * Get the unique instance of the DAO.
     * @return the unique instance of the DAO.
     */
    public static StatisticsDao getInstance() {
    	return StatisticsDbmsDao.singleton;
    }

    /** Creates a new unique instance of the Class */
    private StatisticsDbmsDao() {
    }

    /** Query to retrieve existing statistics data for the given period. */
    private static final String STAT_SELECT_FOR_UPDATE = "Select period From Stats Where period = ?";

    /** Query to update existing statistics data for the given period. */
    private static final String STAT_UPDATE = "Update Stats Set sumconn = ?, maxconn = ? Where period = ?";

    /** Query to insert new statistics data for a given period. */
    private static final String STAT_INSERT = "Insert Into Stats (period, sumconn, maxconn) values (?,?,?)";

    /**
     * {@inheritDoc}
     * @throws DataAccessException if an error occured during the database update.
     */
    public void updateStatistics(ConnectionStatData statData) throws DataAccessException {
        if (statData != null && statData.getDay() != null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                connection.setAutoCommit(false);
                pstmt = connection.prepareStatement(STAT_SELECT_FOR_UPDATE);
                Date d = new Date(statData.getDay().getTimeInMillis());
                pstmt.setDate(1, d);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    pstmt.close();
                    pstmt = connection.prepareStatement(STAT_UPDATE);
                    pstmt.setInt(1, statData.getSumNbUser());
                    pstmt.setInt(2, statData.getMaxNbUser());
                    pstmt.setDate(3, d);
                    pstmt.executeUpdate();
                }
                else {
                    pstmt.close();
                    pstmt = connection.prepareStatement(STAT_INSERT);
                    pstmt.setDate(1, d);
                    pstmt.setInt(2, statData.getSumNbUser());
                    pstmt.setInt(3, statData.getMaxNbUser());
                    pstmt.executeUpdate();
                }
                JdbcDAOUtils.commit(connection);
            }
            catch (SQLException e) {
                logger.error("Error during statistics update/insert", e);
                JdbcDAOUtils.rollbackConnection(connection);
                throw new DataAccessException("Error during statistics update/insert", e);
            }
            finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }

    /** Query to retrieve all statistics. */
    private static final String ALL_STATISTICS_RETRIEVE_QUERY = "Select period, sumconn, maxconn From Stats order by period";

    /**
     * {@inheritDoc}
     * @throws DataAccessException if an error occured during the database access.
     */
    public List<ConnectionStatData> retrieveAllStatistics() throws DataAccessException {
        List<ConnectionStatData> result = new ArrayList<ConnectionStatData>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ConnectionStatData currentData = null;
        try {
            pstmt = connection.prepareStatement(ALL_STATISTICS_RETRIEVE_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Date day = rs.getDate("period");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(day.getTime());
                currentData = new ConnectionStatData(cal, rs.getInt("sumconn"), rs.getInt("maxconn"));
                result.add(currentData);
            }
        }
        catch (SQLException e) {
            logger.error("Error during statistics retrieve", e);
            JdbcDAOUtils.rollbackConnection(connection);
            throw new DataAccessException("Error during statistics retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        	JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** Query to retrieve the last saved statistics. */
    private static final String LAST_STATISTICS_RETRIEVE_QUERY =
            "Select period, sumconn, maxconn"
            + " From Stats"
            + " Where period = ?";

    /**
     * {@inheritDoc}
     * @throws DataAccessException
     */
    public ConnectionStatData retrieveLastStatistics() throws DataAccessException {
        ConnectionStatData result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(LAST_STATISTICS_RETRIEVE_QUERY);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            pstmt.setDate(1, new Date(cal.getTimeInMillis()));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Date day = rs.getDate("period");
                cal.setTimeInMillis(day.getTime());
                result = new ConnectionStatData(cal, rs.getInt("sumconn"), rs.getInt("maxconn"));
            }
        }
        catch (SQLException e) {
            logger.error("Error during statistics retrieve", e);
            JdbcDAOUtils.rollbackConnection(connection);
            throw new DataAccessException("Error during statistics retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        	JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

}

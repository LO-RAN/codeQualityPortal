package com.compuware.caqs.dao.interfaces;

import java.util.List;

import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.security.stats.ConnectionStatData;

/**
 * Statistics data access object.
 * @author cwfr-fdubois
 */
public interface StatisticsDao {

    /**
     * Update the connection statistics.
     * @param statData the connection statistic data.
     * @throws DataAccessException if an error occured during the database update.
     */
	public void updateStatistics(ConnectionStatData statData) throws DataAccessException;

    /**
     * Retrieve all statistics.
     * @return the list of daily statistic data.
     * @throws DataAccessException if an error occured during the database access.
     */
    public List<ConnectionStatData> retrieveAllStatistics() throws DataAccessException;

    /**
     * Retrieve the last statistics.
     * @return the last saved statistics.
     * @throws DataAccessException if an error occured during the database access.
     */
    public ConnectionStatData retrieveLastStatistics() throws DataAccessException;

}
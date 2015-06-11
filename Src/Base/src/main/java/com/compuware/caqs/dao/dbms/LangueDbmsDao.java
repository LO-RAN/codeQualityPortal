package com.compuware.caqs.dao.dbms;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.LangueDao;
import com.compuware.caqs.domain.dataschemas.modeleditor.LangueBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public class LangueDbmsDao implements LangueDao {

    /**
     * Logger
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    /**
     * Data cache object
     */
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    /**
     * Singleton
     */
    private static LangueDao singleton = new LangueDbmsDao();

    /**
     * @return the instance of the singleton
     */
    public static LangueDao getInstance() {
        return LangueDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private LangueDbmsDao() {
    }

    /**
     * @{@inheritDoc }
     */
    public List<LangueBean> retrieveLanguesByIdLib(String id, String lib) {
        StringBuffer query = new StringBuffer("SELECT l.id, l.lib, l.description ");
        StringBuffer fromClause = new StringBuffer(" FROM Langue l");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(l.id) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" lower(l.lib) like '").append(lib.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<LangueBean> result = new ArrayList<LangueBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(query.toString());
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                LangueBean langue = new LangueBean();
                langue.setId(rs.getString("id"));
                langue.setLib(rs.getString("lib"));
                langue.setDesc(rs.getString("description"));
                result.add(langue);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving languages", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_LANGUAGE_BY_ID = "SELECT id, lib, description, count(id_langue) as nbTexts " +
            " FROM LANGUE, i18n " +
            " WHERE id = id_langue " +
            " AND id = ? " +
            " GROUP BY id, lib, description";
    private static final String RETRIEVE_LANGUAGE_BY_ID_WITHOUT_COUNT = "SELECT id, lib, description " +
            " FROM LANGUE " +
            " WHERE id = ? ";
    private static final String RETRIEVE_ALL_LANGUAGES = "SELECT id, lib, description " +
            " FROM LANGUE ORDER BY lib";

    /**
     * @{@inheritDoc }
     */
    public LangueBean retrieveLangueById(String id) {
        LangueBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_LANGUAGE_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new LangueBean();
                result.setId(rs.getString("id"));
                result.setLib(rs.getString("lib"));
                result.setDesc(rs.getString("description"));
                result.setNbTexts(rs.getInt("nbTexts"));
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                stmt = connection.prepareStatement(RETRIEVE_LANGUAGE_BY_ID_WITHOUT_COUNT);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = new LangueBean();
                    result.setId(rs.getString("id"));
                    result.setLib(rs.getString("lib"));
                    result.setDesc(rs.getString("description"));
                    result.setNbTexts(0);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving language", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String CREATE_LANGUE_QUERY = "INSERT INTO LANGUE (ID,LIB,DESCRIPTION) VALUES (?, ?, ?)";
    private static final String UPDATE_LANGUE_QUERY = "UPDATE LANGUE SET LIB = ?, DESCRIPTION = ? WHERE ID = ?";

    /**
     * @{@inheritDoc }
     */
    public void saveLangueBean(LangueBean lb) throws DataAccessException {
        PreparedStatement update = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_LANGUAGE_BY_ID);
            stmt.setString(1, lb.getId());
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                update = connection.prepareStatement(UPDATE_LANGUE_QUERY);
                update.setString(1, lb.getLib());
                update.setString(2, lb.getDesc());
                update.setString(3, lb.getId());
            } else {
                update = connection.prepareStatement(CREATE_LANGUE_QUERY);
                update.setString(1, lb.getId());
                update.setString(2, lb.getLib());
                update.setString(3, lb.getDesc());
            }
            int nb = update.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("Error updating language", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(update);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    
    private static final String DELETE_LANGUE_QUERY = "DELETE FROM LANGUE WHERE " +
            " id = ?";

    /**
     * @{@inheritDoc }
     */
    public void deleteLangueBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_LANGUE_QUERY);
            stmt.setString(1, id);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error deleting language", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public List<LangueBean> retrieveAllLanguages() {
        List<LangueBean> result = new ArrayList<LangueBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ALL_LANGUAGES);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                LangueBean langue = new LangueBean();
                langue.setId(rs.getString("id"));
                langue.setLib(rs.getString("lib"));
                langue.setDesc(rs.getString("description"));
                result.add(langue);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving languages", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
}

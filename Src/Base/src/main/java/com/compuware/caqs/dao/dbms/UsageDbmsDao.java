/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:22
 */
package com.compuware.caqs.dao.dbms;

// Imports for DBMS transactions.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.dao.util.QueryUtil;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorModelBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author  cwfr-fdubois
 */
public class UsageDbmsDao implements UsageDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String USAGE_BY_ID_REQUEST =
            "SELECT id_usa, APU_LIMIT_MEDIUM, APU_LIMIT_LONG FROM Modele WHERE id_usa=?";
    private static final String ALL_USAGES_REQUEST =
            "SELECT id_usa FROM Modele";
    private static final String USAGE_BY_ELEMENTID_REQUEST =
            "SELECT usa.id_usa FROM Modele usa, Element elt WHERE elt.id_elt=? And elt.id_usa=usa.id_usa";
    private static final String CREATE_MODEL_QUERY =
            "INSERT INTO MODELE(ID_USA, APU_LIMIT_MEDIUM, APU_LIMIT_LONG, dapplication_usa, dints_usa, dmaj_usa, dperemption_usa) " +
            " VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_MODEL_QUERY =
            "UPDATE MODELE SET APU_LIMIT_MEDIUM=?, APU_LIMIT_LONG=?, dapplication_usa=?, dints_usa=?, dmaj_usa=?, dperemption_usa=? " +
            " WHERE ID_USA = ?";
    private static final String RETRIEVE_MODEL_WITH_ASSOCIATION_COUNT_BY_ID =
            "SELECT m.id_usa, m.dints_usa, m.dmaj_usa, m.dapplication_usa, " +
            " m.dperemption_usa, count(e.id_usa) as nbEAs, APU_LIMIT_MEDIUM, APU_LIMIT_LONG " +
            " FROM modele m, element e" +
            " WHERE m.id_usa = ? " +
            " AND m.id_usa = e.id_usa and e.ID_TELT='EA'"+
            " GROUP BY m.id_usa, m.dints_usa, m.dmaj_usa, m.dapplication_usa, " +
            " m.dperemption_usa, m.APU_LIMIT_MEDIUM, m.APU_LIMIT_LONG ";
    private static final String RETRIEVE_MODEL_WITHOUT_ASSOCIATION_COUNT_BY_ID =
            "SELECT m.id_usa, m.dints_usa, m.dmaj_usa, m.dapplication_usa, " +
            " m.dperemption_usa, APU_LIMIT_MEDIUM, APU_LIMIT_LONG " +
            " FROM modele m" +
            " WHERE m.id_usa = ? ";
    private static final String ARCHIVE_MODEL_QUERY =
            "UPDATE element SET id_usa = ? WHERE id_usa = ? AND DPEREMPTION IS NULL";

    private static UsageDao singleton = new UsageDbmsDao();

    public static UsageDao getInstance() {
        return UsageDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private UsageDbmsDao() {
    }

    /**
     * @{@inheritDoc }
     */
    public Collection<UsageBean> retrieveAllUsages() {
        Collection<UsageBean> result = new ArrayList<UsageBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_USAGES_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UsageBean newUsage = new UsageBean();
                newUsage.setId(rs.getString("id_usa"));
                result.add(newUsage);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    public UsageBean retrieveUsageById(java.lang.String id) {
        UsageBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        result = UsageDbmsDao.retrieveUsageById(id, connection);
        JdbcDAOUtils.closeConnection(connection);
        return result;
    }

    public static UsageBean retrieveUsageById(java.lang.String id, Connection connection) {
        UsageBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(USAGE_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new UsageBean();
                result.setId(rs.getString("id_usa"));
                result.setLowerLimitLongRun(rs.getDouble("APU_LIMIT_LONG"));
                result.setLowerLimitMediumRun(rs.getDouble("APU_LIMIT_MEDIUM"));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return result;
    }

    public UsageBean retrieveUsageByElementId(String idElt) {
        UsageBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            pstmt = connection.prepareStatement(USAGE_BY_ELEMENTID_REQUEST);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new UsageBean();
                result.setId(rs.getString("id_usa"));
            }
        } catch (SQLException e) {
            logger.error("Error during Project retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String GOAL_CRIT_WEIGHT_QUERY = "Select id_fact, id_crit, poids" +
            " From Facteur_Critere" + " Where id_usa = ?" + " order by id_fact";

    public Map<String, Map<String, Double>> retrieveFactorDefinition(String idUsa) {
        Map<String, Map<String, Double>> factdefs = new HashMap<String, Map<String, Double>>();
        Map<String, Double> critdefs = new HashMap<String, Double>();
        PreparedStatement ptstmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            ptstmt = connection.prepareStatement(GOAL_CRIT_WEIGHT_QUERY);
            ptstmt.setString(1, idUsa);
            rs = ptstmt.executeQuery();
            String actualIdFact = "";
            String lastIdFact = "";
            while (rs.next()) {
                actualIdFact = rs.getString(1);
                String idCrit = rs.getString(2);
                double poids = rs.getDouble(3);
                if (lastIdFact.length() > 0 && !actualIdFact.equals(lastIdFact)) {
                    factdefs.put(lastIdFact, critdefs);
                    critdefs = new HashMap<String, Double>();
                }
                critdefs.put(idCrit, new Double(poids));
                lastIdFact = actualIdFact;
            }
            factdefs.put(lastIdFact, critdefs);
        } catch (SQLException e) {
            logger.error("Error initializing factor definition", e);
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(ptstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return factdefs;
    }
    private static final String CRITERONS_FOR_ELEMENT_TYPE_QUERY = "Select id_crit From Critere_Usage" +
            " Where id_usa = ?" + " And id_telt = ?";
    private static final String CRITERONS_FOR_ELEMENT_TYPES_QUERY = "Select id_crit From Critere_Usage" +
            " Where id_usa = ?" + " And id_telt IN @INCLAUSE@";

    public Collection<String> retrieveCriterionsForElement(String idUsa, String idTelt) {
        return retrieveCriterionsForElement(idUsa, idTelt, CRITERONS_FOR_ELEMENT_TYPE_QUERY);
    }

    public Collection<String> retrieveCriterionsForElement(String idUsa, Collection<String> idTeltColl) {
        QueryUtil queryUtil = QueryUtil.getInstance();
        return retrieveCriterionsForElement(idUsa, null, queryUtil.replaceInClause(CRITERONS_FOR_ELEMENT_TYPES_QUERY, idTeltColl));
    }

    private Collection<String> retrieveCriterionsForElement(String idUsa, String idTelt, String query) {
        Collection<String> result = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(query);
            // Execution de la requete.
            stmt.setString(1, idUsa);
            if (idTelt != null) {
                stmt.setString(2, idTelt);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                String idCrit = rs.getString(1);
                result.add(idCrit);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    public Map<String, Collection<String>> retrieveCriterionMapForElement(String idUsa, Collection<String> typeEltColl) {
        Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();
        if (typeEltColl != null) {
            Iterator<String> i = typeEltColl.iterator();
            String idTElt = null;
            while (i.hasNext()) {
                idTElt = i.next();
                result.put(idTElt, retrieveCriterionsForElement(idUsa, idTElt));
            }
        }
        return result;
    }
    private static final String CRIT_TELT_QUERY =
            "Select id_crit, id_telt" + " From Critere_usage" +
            " Where id_usa = ?";

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.interfaces.UsageDao#retrieveCriterionDefinitionMapByElementType(java.lang.String)
     */
    public Map<String, String> retrieveCriterionElementTypeMap(String idUsa) throws DataAccessException {
        Map<String, String> result = new HashMap<String, String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(CRIT_TELT_QUERY);
            // Execution de la requete.
            stmt.setString(1, idUsa);
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                result.put(rs.getString("id_crit"), rs.getString("id_telt"));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving criterion element type map", e);
            throw new DataAccessException("Error retrieving criterion element type map", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public List<UsageBean> retrieveModelsByIdAndLib(String id, String lib, String idLoc) {
        List<UsageBean> result = new ArrayList<UsageBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            StringBuffer query = new StringBuffer("SELECT id_usa ");
            StringBuffer fromClause = new StringBuffer(" FROM Modele m");
            StringBuffer whereClause = new StringBuffer(" WHERE ");
            boolean alreadyOneClause = false;

            if (!"%".equals(id)) {
                //il y a une recherche sur l'identifiant du critere
                whereClause = whereClause.append(" lower(m.id_usa) like '").append(id.toLowerCase()).append("' ");
                alreadyOneClause = true;
            }

            if (!"%".equals(lib)) {
                //il y a une recherche sur le lib
                fromClause = fromClause.append(" , I18N libI18n ");
                if (alreadyOneClause) {
                    whereClause = whereClause.append(" AND ");
                }
                whereClause = whereClause.append(" libI18n.table_name = 'modele' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLoc).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = m.id_usa");
                alreadyOneClause = true;
            }

            query.append(fromClause);
            if (alreadyOneClause) {
                query.append(whereClause);
            }
            // Preparation de la requete.
            stmt = connection.prepareStatement(query.toString());
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                UsageBean newUsage = new UsageBean();
                newUsage.setId(rs.getString("id_usa"));
                result.add(newUsage);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving models", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public void deleteQualityModel(String id) throws DataAccessException {
        boolean hasError = false;
        Statement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
        } catch (SQLException e) {
            logger.error("Error setting autocommit to false", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM REGLE WHERE ID_USA = '" + id + "'";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error deleting regle for " + id, e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM FACTEUR_CRITERE WHERE ID_USA = '" + id +
                    "'";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error deleting facteur_critere for " + id, e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM CRITERE_USAGE WHERE ID_USA = '" + id +
                    "'";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error deleting critere_usage for " + id, e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM MODELE WHERE ID_USA = '" + id + "'";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error deleting model", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        try {
            stmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error committing", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    /**
     * @{@inheritDoc }
     */
    public void saveModelBean(UsageBean usage) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (retrieveUsageById(usage.getId(), connection) == null) {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_MODEL_QUERY);
                stmt.setString(1, usage.getId());
                stmt.setDouble(2, usage.getLowerLimitMediumRun());
                stmt.setDouble(3, usage.getLowerLimitLongRun());
                stmt.setTimestamp(4, usage.getDapplication());
                stmt.setTimestamp(5, usage.getDinst());
                stmt.setTimestamp(6, usage.getDmaj());
                stmt.setTimestamp(7, usage.getDperemption());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } else {
                stmt = connection.prepareStatement(UPDATE_MODEL_QUERY);
                stmt.setDouble(1, usage.getLowerLimitMediumRun());
                stmt.setDouble(2, usage.getLowerLimitLongRun());
                stmt.setTimestamp(3, usage.getDapplication());
                stmt.setTimestamp(4, usage.getDinst());
                stmt.setTimestamp(5, usage.getDmaj());
                stmt.setTimestamp(6, usage.getDperemption());
                stmt.setString(7, usage.getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating tool", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void duplicateModel(String modelId, String newModelId) throws DataAccessException {
        boolean hasError = false;
        Statement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
        } catch (SQLException e) {
            logger.error("Error setting autocommit to false", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        if (!hasError) {
            try {
                String query = "INSERT INTO MODELE(ID_USA, APU_LIMIT_MEDIUM, APU_LIMIT_LONG) " +
                        " SELECT '" + newModelId +
                        "', APU_LIMIT_MEDIUM, APU_LIMIT_LONG FROM MODELE WHERE ID_USA = '" +
                        modelId + "'";
                stmt.addBatch(query);
            } catch (SQLException e) {
                logger.error("Error duplicating modele", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                if (hasError) {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closeConnection(connection);
                }
            }
        }

        if (!hasError) {
            try {
                String query = "INSERT INTO OUTILS_MODELE(ID_USA, ID_OUTILS) " +
                        " SELECT '" + newModelId +
                        "', ID_OUTILS FROM OUTILS_MODELE WHERE ID_USA = '" +
                        modelId + "'";
                stmt.addBatch(query);
            } catch (SQLException e) {
                logger.error("Error duplicating critere_usage", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                if (hasError) {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closeConnection(connection);
                }
            }
        }

        if (!hasError) {
            try {
                String query = "INSERT INTO CRITERE_USAGE(ID_USA, ID_CRIT, ID_TELT) " +
                        " SELECT '" + newModelId +
                        "', ID_CRIT, ID_TELT FROM CRITERE_USAGE WHERE ID_USA = '" +
                        modelId + "'";
                stmt.addBatch(query);
            } catch (SQLException e) {
                logger.error("Error duplicating critere_usage", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                if (hasError) {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closeConnection(connection);
                }
            }
        }

        if (!hasError) {
            try {
                String query = "INSERT INTO FACTEUR_CRITERE(ID_USA, ID_CRIT, ID_FACT, POIDS) " +
                        " SELECT '" + newModelId +
                        "', ID_CRIT, ID_FACT, POIDS FROM FACTEUR_CRITERE WHERE ID_USA = '" +
                        modelId + "'";
                stmt.addBatch(query);
            } catch (SQLException e) {
                logger.error("Error duplicating facteur_usage", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                // Fermeture du resultat et de la requete.
                if (hasError) {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closeConnection(connection);
                }
            }
        }

        if (!hasError) {
            try {
                String query = "INSERT INTO REGLE(ID_USA, ID_CRIT, ID_MET) " +
                        " SELECT '" + newModelId +
                        "', ID_CRIT, ID_MET FROM REGLE WHERE ID_USA = '" +
                        modelId + "'";
                stmt.addBatch(query);
            } catch (SQLException e) {
                logger.error("Error duplicating regle", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                // Fermeture du resultat et de la requete.
                if (hasError) {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closeConnection(connection);
                }
            }
        }

        if (!hasError) {
            try {
                String query = "INSERT INTO I18N(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) " +
                        " SELECT 'modele', COLUMN_NAME, '" + newModelId +
                        "', ID_LANGUE, TEXT FROM I18N WHERE TABLE_NAME = 'modele' AND ID_TABLE = '" +
                        modelId + "'";
                stmt.addBatch(query);
            } catch (SQLException e) {
                logger.error("Error duplicating i18n", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                // Fermeture du resultat et de la requete.
                if (hasError) {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closeConnection(connection);
                }
            }
        }

        if (!hasError) {
            try {
                stmt.executeBatch();
                connection.commit();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error committing", e);
                hasError = true;
                throw new DataAccessException(e);
            } finally {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    private static final String RETRIEVE_CRITERIONS_TO_PURGE_FROM_MODEL =
            "SELECT id_crit FROM CRITERE_USAGE " +
            " WHERE id_usa = ? AND " +
            " id_crit NOT IN (" +
            " SELECT distinct(id_crit) FROM facteur_critere WHERE id_usa = ?" +
            " )";

    /**
     * @{@inheritDoc }
     */
    public List<String> removeGoalFromModel(String modelId, String goalId) throws DataAccessException {
        List<String> criterionsToPurge = new ArrayList<String>();
        boolean hasError = false;
        Statement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
        } catch (SQLException e) {
            logger.error("Error setting autocommit to false", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM FACTEUR_CRITERE " +
                    " WHERE id_usa = '" + modelId + "' AND id_fact = '" + goalId +
                    "'";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error removing from facteur_critere", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        ResultSet rs = null;
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = connection.prepareStatement(RETRIEVE_CRITERIONS_TO_PURGE_FROM_MODEL);
            preparedStmt.setString(1, modelId);
            preparedStmt.setString(2, modelId);
            rs = preparedStmt.executeQuery();
            while (rs.next()) {
                criterionsToPurge.add(rs.getString("id_crit"));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving critere_usage to remove", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(preparedStmt);
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM CRITERE_USAGE " +
                    " WHERE id_usa = '" + modelId + "' AND " +
                    " id_crit NOT IN (" +
                    " SELECT distinct(id_crit) FROM facteur_critere WHERE id_usa = '" +
                    modelId + "'" +
                    " )";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error purging critere_usage", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        try {
            String query = "DELETE FROM REGLE " +
                    " WHERE id_usa = '" + modelId + "' AND " +
                    " id_crit NOT IN (" +
                    " SELECT distinct(id_crit) FROM CRITERE_USAGE WHERE id_usa = '" +
                    modelId + "'" +
                    " )";
            stmt.addBatch(query);
        } catch (SQLException e) {
            logger.error("Error purging regle", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            if (hasError) {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        try {
            stmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error committing", e);
            hasError = true;
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return criterionsToPurge;
    }

    /**
     * @{@inheritDoc }
     */
    public ModelEditorModelBean retrieveModelWithAssociationCountById(String id) {
        ModelEditorModelBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_MODEL_WITH_ASSOCIATION_COUNT_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new ModelEditorModelBean();
                result.setId(rs.getString("id_usa"));
                result.setDinst(rs.getTimestamp("dints_usa"));
                result.setDmaj(rs.getTimestamp("dmaj_usa"));
                result.setDapplication(rs.getTimestamp("dapplication_usa"));
                result.setDperemption(rs.getTimestamp("dperemption_usa"));
                result.setLowerLimitLongRun(rs.getDouble("APU_LIMIT_LONG"));
                result.setLowerLimitMediumRun(rs.getDouble("APU_LIMIT_MEDIUM"));
                result.setNbEAAssociated(rs.getInt("nbEAs"));
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                stmt = connection.prepareStatement(RETRIEVE_MODEL_WITHOUT_ASSOCIATION_COUNT_BY_ID);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = new ModelEditorModelBean();
                    result.setId(rs.getString("id_usa"));
                    result.setDinst(rs.getTimestamp("dints_usa"));
                    result.setDmaj(rs.getTimestamp("dmaj_usa"));
                    result.setDapplication(rs.getTimestamp("dapplication_usa"));
                    result.setDperemption(rs.getTimestamp("dperemption_usa"));
                    result.setLowerLimitLongRun(rs.getDouble("APU_LIMIT_LONG"));
                    result.setLowerLimitMediumRun(rs.getDouble("APU_LIMIT_MEDIUM"));
                    result.setNbEAAssociated(0);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving model", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public void archiveModel(String modelId, String archivedModelId) {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(ARCHIVE_MODEL_QUERY);
            stmt.setString(1, archivedModelId);
            stmt.setString(2, modelId);
            // Execution de la requete.
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error retrieving model", e);
        } finally {
            // Fermeture de la requete.
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

}

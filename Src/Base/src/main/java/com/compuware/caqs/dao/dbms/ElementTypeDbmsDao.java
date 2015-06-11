/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.dao.dbms;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.ElementTypeDao;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.modeleditor.ElementTypeBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cwfr-dzysman
 */
public class ElementTypeDbmsDao implements ElementTypeDao {

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
    private static ElementTypeDao singleton = new ElementTypeDbmsDao();

    /**
     * retrieves all element types
     */
    private static final String RETRIEVE_ALL_ELEMENT_TYPES = 
            "SELECT id_telt FROM Type_Element" +
            " WHERE id_telt != '"+ElementType.ENTRYPOINT+"'";

    /**
     * retrieves models and criterions using an element type
     */
    private static final String RETRIEVE_ASSOCIATED_MODELS_AND_CRITERIONS_FOR_ELEMENT_TYPE =
            "SELECT id_usa, id_crit FROM critere_usage" +
            " WHERE id_telt = ?";

    /**
     * @return the instance of the singleton
     */
    public static ElementTypeDao getInstance() {
        return ElementTypeDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private ElementTypeDbmsDao() {
    }

    /**
     * @{@inheritDoc }
     */
    public List<ElementType> retrieveAllElementTypes() {
        List<ElementType> result = new ArrayList<ElementType>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ALL_ELEMENT_TYPES);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                ElementType newCriterion = new ElementType();
                newCriterion.setId(rs.getString("id_telt"));
                result.add(newCriterion);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving element types", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    public List<ElementType> retrieveElementTypesByIdLib(String id, String lib,
            String idLang) {
        StringBuffer query = new StringBuffer("SELECT e.id_telt ");
        StringBuffer fromClause = new StringBuffer(" FROM Type_element e");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(e.id_telt) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            fromClause = fromClause.append(" , I18N libI18n ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" libI18n.table_name = 'type_element' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLang).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = e.id_telt");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<ElementType> result = new ArrayList<ElementType>();
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
                ElementType newCriterion = new ElementType();
                newCriterion.setId(rs.getString("id_telt"));
                result.add(newCriterion);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving element types", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_ELEMENT_TYPE_BY_ID_WITHOUT_CRITERE =
            "SELECT id_telt, dinst_telt, dmaj_telt, dapplication_telt, " +
            " dperemption_telt, has_Source, is_File " +
            " FROM type_element " +
            " WHERE id_telt = ?";
    private static final String RETRIEVE_ELEMENT_TYPE_BY_ID =
            "SELECT te.id_telt, te.dinst_telt, te.dmaj_telt, te.dapplication_telt, " +
            " te.dperemption_telt, te.has_Source, te.is_File, count(id_crit) as nbCrit " +
            " FROM type_element te, Critere_usage cu" +
            " WHERE te.id_telt = ? " +
            " AND cu.id_telt = te.id_telt " +
            " GROUP BY te.id_telt, te.dinst_telt, te.dmaj_telt, te.dapplication_telt, " +
            " te.dperemption_telt, te.has_Source, te.is_File";

    /**
     * @{@inheritDoc }
     */
    public ElementTypeBean retrieveElementTypeById(String id) {
        ElementTypeBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ELEMENT_TYPE_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new ElementTypeBean();
                result.setId(rs.getString("id_telt"));
                result.setDinst(rs.getTimestamp("dinst_telt"));
                result.setDmaj(rs.getTimestamp("dmaj_telt"));
                result.setDapplication(rs.getTimestamp("dapplication_telt"));
                result.setDperemption(rs.getTimestamp("dperemption_telt"));
                result.setHasSource(rs.getBoolean("has_source"));
                result.setIsFile(rs.getBoolean("is_file"));
                result.setNbCriterionsAssociated(rs.getInt("nbCrit"));
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                stmt = connection.prepareStatement(RETRIEVE_ELEMENT_TYPE_BY_ID_WITHOUT_CRITERE);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = new ElementTypeBean();
                    result.setId(rs.getString("id_telt"));
                    result.setDinst(rs.getTimestamp("dinst_telt"));
                    result.setDmaj(rs.getTimestamp("dmaj_telt"));
                    result.setDapplication(rs.getTimestamp("dapplication_telt"));
                    result.setDperemption(rs.getTimestamp("dperemption_telt"));
                    result.setHasSource(rs.getBoolean("has_source"));
                    result.setIsFile(rs.getBoolean("is_file"));
                   result.setNbCriterionsAssociated(0);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving element type", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String CREATE_ELEMENT_TYPE_QUERY = "INSERT INTO " +
            "TYPE_ELEMENT(ID_TELT,DINST_TELT,DMAJ_TELT,DAPPLICATION_TELT,DPEREMPTION_TELT,HAS_SOURCE,IS_FILE)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ELEMENT_TYPE_QUERY = "UPDATE TYPE_ELEMENT SET " +
            " DINST_TELT = ?, DMAJ_TELT = ?, " +
            " DAPPLICATION_TELT = ?, DPEREMPTION_TELT = ?, HAS_SOURCE = ?, " +
            " IS_FILE = ? " +
            " WHERE ID_TELT = ?";

    /**
     * @{@inheritDoc }
     */
    public void saveElementTypeBean(ElementTypeBean et) throws DataAccessException {
        PreparedStatement update = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ELEMENT_TYPE_BY_ID_WITHOUT_CRITERE);
            stmt.setString(1, et.getId());
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                update = connection.prepareStatement(UPDATE_ELEMENT_TYPE_QUERY);
                update.setTimestamp(1, et.getDinst());
                update.setTimestamp(2, et.getDmaj());
                update.setTimestamp(3, et.getDapplication());
                update.setTimestamp(4, et.getDperemption());
                update.setBoolean(5, et.hasSource());
                update.setBoolean(6, et.isFile());
                update.setString(7, et.getId());
            } else {
                update = connection.prepareStatement(CREATE_ELEMENT_TYPE_QUERY);
                update.setString(1, et.getId());
                update.setTimestamp(2, et.getDinst());
                update.setTimestamp(3, et.getDmaj());
                update.setTimestamp(4, et.getDapplication());
                update.setTimestamp(5, et.getDperemption());
                update.setBoolean(6, et.hasSource());
                update.setBoolean(7, et.isFile());
            }
            int nb = update.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("Error creating element type", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(update);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String DELETE_ELEMENT_TYPE_QUERY = "DELETE FROM TYPE_ELEMENT WHERE " +
            " id_telt = ?";

    /**
     * @{@inheritDoc }
     */
    public void deleteElementTypeBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_ELEMENT_TYPE_QUERY);
            stmt.setString(1, id);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error retrieving element type", e);
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
    public Map<String, List<CriterionDefinition>> retrieveAssociatedModelsAndCriterionsForElementType(String idTelt) {
        Map<String, List<CriterionDefinition>> result = new HashMap<String, List<CriterionDefinition>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ASSOCIATED_MODELS_AND_CRITERIONS_FOR_ELEMENT_TYPE);
            stmt.setString(1, idTelt);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                String idUsa = rs.getString("id_usa");
                List<CriterionDefinition> criterions = result.get(idUsa);
                if(criterions == null) {
                    criterions = new ArrayList<CriterionDefinition>();
                    result.put(idUsa, criterions);
                }
                CriterionDefinition cd = new CriterionDefinition();
                cd.setId(rs.getString("id_crit"));
                criterions.add(cd);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving models and criterions", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
}

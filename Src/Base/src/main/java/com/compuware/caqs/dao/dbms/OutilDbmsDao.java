package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.OutilDao;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 7 f�vr. 2006
 * Time: 11:35:42
 * To change this template use File | Settings | File Templates.
 */
public class OutilDbmsDao implements OutilDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    /** L'instance unique de la classe. */
    private static OutilDao singleton = new OutilDbmsDao();

    /** Constructeur prive. */
    private OutilDbmsDao() {
    }

    /** Methode d'acces a l'instance de la classe.
     * @return l'instance unique de la classe.
     */
    public static OutilDao getInstance() {
        return singleton;
    }
    /** La requete SQL de selection d'un stereotype a partir de son identifiant. */
    private static final String ALL_OUTIL_REQUEST = "SELECT id_outils FROM Outils";
    /** La requete SQL de selection d'un stereotype a partir de son identifiant. */
    private static final String OUTIL_BY_ID_REQUEST = "SELECT id_outils FROM Outils WHERE id_outils = ?";
    private static final String OUTILS_BY_MODEL_REQUEST =
            "SELECT id_outils FROM Outils_Modele WHERE id_usa = ?";
    private static final String OUTILS_BY_BASELINE = "Select Distinct met.outil_met" +
            " From Qametrique qa, Metrique met" +
            " Where qa.id_met = met.id_met" + " And qa.id_bline = ?" +
            " And met.outil_met is not null";
    private static final String ALL_OUTIL_WITH_METRICS_COUNT_REQUEST = "select id_outils, count(id_met) as nb from outils, metrique where id_outils = outil_met group by id_outils";

    private static final String ADD_TOOL_ASSOCIATION_TO_MODEL_QUERY =
            "INSERT INTO OUTILS_MODELE(ID_USA, ID_OUTILS) VALUES(?,?)";
    private static final String REMOVE_TOOL_ASSOCIATION_FOR_MODEL_QUERY =
            "DELETE FROM OUTILS_MODELE WHERE ID_USA=? AND ID_OUTILS=?";

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.OutilDao#retrieveOutilById(java.lang.String)
     */
    public OutilBean retrieveOutilById(java.lang.String id) {
        // Recuperation d'une connexion JDBC.
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        OutilBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve outil by id: " + id);
        try {
            pstmt = connection.prepareStatement(OUTIL_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new OutilBean();
                result.setId(rs.getString("id_outils"));
            }
        } catch (SQLException e) {
            logger.error("Error during Stereotype retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.OutilDao#retrieveOutilByBaseline(java.lang.String)
     */
    public Collection<OutilBean> retrieveOutilByBaseline(String idBline) {
        // Recuperation d'une connexion JDBC.
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Collection<OutilBean> result = new ArrayList<OutilBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve outils by baseline");
        try {
            pstmt = connection.prepareStatement(OUTILS_BY_BASELINE);
            pstmt.setString(1, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OutilBean outilBean = new OutilBean();
                outilBean.setId(rs.getString("outil_met"));
                result.add(outilBean);
            }
        } catch (SQLException e) {
            logger.error("Error during outil retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * {@inheritDoc }
     */
    public List<OutilBean> retrieveTools() {
        List<OutilBean> result = new ArrayList<OutilBean>();

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve outils");
        try {
            pstmt = connection.prepareStatement(ALL_OUTIL_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OutilBean outilBean = new OutilBean();
                outilBean.setId(rs.getString("id_outils"));
                result.add(outilBean);
            }
        } catch (SQLException e) {
            logger.error("Error during outil retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }

    /**
     * {@inheritDoc }
     */
    public List<OutilBean> retrieveOutilByIdLib(String id, String lib, String idLang) {
        StringBuffer query = new StringBuffer("SELECT o.id_outils ");
        StringBuffer fromClause = new StringBuffer(" FROM Outils o");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(o.id_outils) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            fromClause = fromClause.append(" , I18N libI18n ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" libI18n.table_name = 'outils' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLang).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = o.id_outils");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<OutilBean> result = new ArrayList<OutilBean>();
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
                OutilBean outil = new OutilBean();
                outil.setId(rs.getString("id_outils"));
                result.add(outil);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving tools", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_NUMBER_OF_ASSOCIATED_METRICS =
            " select count(id_met) as nb" +
            " from metrique " +
            " where outil_met = ?";
    private static final String RETRIEVE_NUMBER_OF_ASSOCIATED_MODELS =
            " select count(om.id_outils) as nb" +
            " from outils_modele om, outils o" +
            " where om.id_outils=o.id_outils and o.ID_OUTILS=?";

    /**
     * @{@inheritDoc }
     */
    public OutilBean retrieveOutilByIdWithMetricAndModelCount(String id) {
        OutilBean result = this.retrieveOutilById(id);
        if (result != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                // Preparation de la requete.
                stmt = connection.prepareStatement(RETRIEVE_NUMBER_OF_ASSOCIATED_METRICS);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    int nb = rs.getInt("nb");
                    result.setNbMetricsAssociated(nb);
                    if (nb == 0) {
                        JdbcDAOUtils.closeResultSet(rs);
                        JdbcDAOUtils.closePrepareStatement(stmt);
                        stmt = connection.prepareStatement(RETRIEVE_NUMBER_OF_ASSOCIATED_MODELS);
                        stmt.setString(1, id);
                        // Execution de la requete.
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            result.setNbModelsUsingIt(rs.getInt("nb"));
                        }
                    }
                }
            } catch (SQLException e) {
                logger.error("Error retrieving tool", e);
                result = null;
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }

        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public List<OutilBean> retrieveOutilsByModel(String model, Connection connection) {
        List<OutilBean> result = new ArrayList<OutilBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(OUTILS_BY_MODEL_REQUEST);
            stmt.setString(1, model);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_outils");
                OutilBean ob = new OutilBean();
                ob.setId(id);
                result.add(ob);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving tool", e);
            result = null;
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
        return result;
    }

    /**
     * {@inheritDoc }
     */
    public List<OutilBean> retrieveToolsWithMetricCount() {
        List<OutilBean> result = new ArrayList<OutilBean>();

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve outils");
        try {
            pstmt = connection.prepareStatement(ALL_OUTIL_WITH_METRICS_COUNT_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                OutilBean outilBean = new OutilBean();
                outilBean.setId(rs.getString("id_outils"));
                outilBean.setNbMetricsAssociated(rs.getInt("nb"));
                result.add(outilBean);
            }
        } catch (SQLException e) {
            logger.error("Error during outil retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public List<OutilBean> retrieveOutilsByModelWithMetricCount(String model) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        List<OutilBean> result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            result = this.retrieveOutilsByModel(model, connection);
            if (result != null) {
                stmt = connection.prepareStatement(RETRIEVE_NUMBER_OF_ASSOCIATED_METRICS);
                for (OutilBean ob : result) {
                    try {
                        stmt.setString(1, ob.getId());
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                            int nb = rs.getInt("nb");
                            ob.setNbMetricsAssociated(nb);
                        }
                    } catch (SQLException e) {
                        logger.error("Error retrieving metrics for tools", e);
                    } finally {
                        // Fermeture du resultat et de la requete.
                        JdbcDAOUtils.closeResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving metrics for tools", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }
    private static final String CREATE_OUTILS_QUERY = "INSERT INTO " +
            "OUTILS(ID_OUTILS)" +
            " VALUES(?)";

    /**
     * @{@inheritDoc }
     */
    public void saveOutilsBean(OutilBean outil) throws DataAccessException {
        PreparedStatement update = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        if (this.retrieveOutilById(outil.getId()) == null) {
            try {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_OUTILS_QUERY);
                stmt.setString(1, outil.getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } catch (SQLException e) {
                logger.error("Error creating tool", e);
                throw new DataAccessException(e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(update);
                JdbcDAOUtils.closePrepareStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    private static final String DELETE_OUTILS_QUERY = "DELETE FROM OUTILS WHERE " +
            " id_outils = ?";

    /**
     * @{@inheritDoc }
     */
    public void deleteOutilsBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_OUTILS_QUERY);
            stmt.setString(1, id);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error retrieving outils", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String RETRIEVE_TOOLS_ASSOCIATED_METRICS = "SELECT id_met " +
            " FROM metrique WHERE outil_met = ?";

    /**
     * {@inheritDoc }
     */
    public List<MetriqueDefinitionBean> retrieveAssociatedMetrics(String toolId) {
        List<MetriqueDefinitionBean> result = new ArrayList<MetriqueDefinitionBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_TOOLS_ASSOCIATED_METRICS);
            stmt.setString(1, toolId);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                MetriqueDefinitionBean mdf = new MetriqueDefinitionBean();
                mdf.setId(rs.getString("id_met"));
                result.add(mdf);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving tool's associated metrics", e);
            result = null;
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
    public void removeToolAssociationForModel(String modelId, String toolId) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(REMOVE_TOOL_ASSOCIATION_FOR_MODEL_QUERY);
            stmt.setString(1, modelId);
            stmt.setString(2, toolId);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error retrieving outils", e);
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
    public void addToolAssociationToModel(String modelId, String toolId) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(ADD_TOOL_ASSOCIATION_TO_MODEL_QUERY);
            stmt.setString(1, modelId);
            stmt.setString(2, toolId);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing added");
            }
        } catch (SQLException e) {
            logger.error("Error retrieving outils", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
}

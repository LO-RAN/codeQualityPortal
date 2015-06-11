package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.ActionPlanUnitDao;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.ArrayList;
import java.util.List;

public class ActionPlanUnitDbmsDao implements ActionPlanUnitDao {

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
    private static ActionPlanUnitDao singleton = new ActionPlanUnitDbmsDao();

    /**
     * @return the instance of the singleton
     */
    public static ActionPlanUnitDao getInstance() {
        return ActionPlanUnitDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private ActionPlanUnitDbmsDao() {
    }
    private static final String RETRIEVE_ACTION_PLAN_UNITS_QUERY = "SELECT unit_id, nb_action_plan_unit FROM Action_Plan_Unit";

    /**
     * @{@inheritDoc }
     */
    public List<ActionPlanUnit> retrieveAllActionPlanUnits() {
        List<ActionPlanUnit> result = new ArrayList<ActionPlanUnit>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ACTION_PLAN_UNITS_QUERY);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                ActionPlanUnit apu = new ActionPlanUnit(rs.getString("unit_id"));
                apu.setNbUnits(rs.getInt("nb_action_plan_unit"));
                result.add(apu);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving action plan units", e);
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
    public List<ActionPlanUnit> retrieveActionPlanUnitByIdLib(String id,
            String lib, String idLang) {
        StringBuffer query = new StringBuffer("SELECT apu.unit_id ");
        StringBuffer fromClause = new StringBuffer(" FROM action_plan_unit apu");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(apu.unit_id) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            fromClause = fromClause.append(" , I18N libI18n ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" libI18n.table_name = 'action_plan_unit' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLang).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = apu.unit_id");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<ActionPlanUnit> result = new ArrayList<ActionPlanUnit>();
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
                result.add(new ActionPlanUnit(rs.getString("unit_id")));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving action plan units", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_ACTION_PLAN_UNIT_BY_ID =
            "SELECT unit_id, nb_action_plan_unit " +
            " FROM action_plan_unit " +
            " WHERE unit_id = ?";
   
    /**
     * @{@inheritDoc }
     */
    public ActionPlanUnit retrieveActionPlanUnitById(String id) {
        ActionPlanUnit result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ACTION_PLAN_UNIT_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                String idRS = rs.getString("unit_id");
                result = new ActionPlanUnit(idRS);
                result.setNbUnits(rs.getInt("nb_action_plan_unit"));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving action plan unit", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String CREATE_ACTION_PLAN_UNIT_QUERY = "INSERT INTO " +
            "ACTION_PLAN_UNIT(UNIT_ID, NB_ACTION_PLAN_UNIT)" +
            " VALUES(?,?)";
    private static final String UPDATE_ACTION_PLAN_UNIT_QUERY = "UPDATE ACTION_PLAN_UNIT " +
            "SET NB_ACTION_PLAN_UNIT=? " +
            "WHERE UNIT_ID=?";

    /**
     * @{@inheritDoc }
     */
    public void saveActionPlanUnit(ActionPlanUnit apu) throws DataAccessException {
        PreparedStatement update = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ACTION_PLAN_UNIT_BY_ID);
            stmt.setString(1, apu.getId());
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                update = connection.prepareStatement(UPDATE_ACTION_PLAN_UNIT_QUERY);
                update.setInt(1, apu.getNbUnits());
                update.setString(2, apu.getId());
            } else {
                update = connection.prepareStatement(CREATE_ACTION_PLAN_UNIT_QUERY);
                update.setString(1, apu.getId());
                update.setInt(2, apu.getNbUnits());
            }
            int nb = update.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing created");
            }
        } catch (SQLException e) {
            logger.error("Error creating action plan unit", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(update);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String DELETE_ACTION_PLAN_UNIT_QUERY = "DELETE FROM ACTION_PLAN_UNIT WHERE " +
            " unit_id = ?";

    /**
     * @{@inheritDoc }
     */
    public void deleteActionPlanUnit(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_ACTION_PLAN_UNIT_QUERY);
            stmt.setString(1, id);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error retrieving languages", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
}

package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.TaskDao;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

public class TaskDbmsDao implements TaskDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    private static TaskDbmsDao singleton = new TaskDbmsDao();

    public static TaskDbmsDao getInstance() {
        return TaskDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private TaskDbmsDao() {
    }
    private static final String SEARCHING_TASK_FOR_ELEMENT_QUERY = "SELECT count(id_message) "
            + " FROM CAQS_MESSAGES" + " WHERE id_elt = ?" + " AND id_bline = ?"
            + " AND ID_TASK = ?" + " AND STATUS != ?" + " AND STATUS != ?";

    /**
     * @{@inheritDoc }
     */
    public boolean reportGeneratingForElement(String idElt, String idBline) {
        boolean retour = false;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("searching for report generation : " + idElt + ", "
                + idBline);
        try {
            pstmt = connection.prepareStatement(SEARCHING_TASK_FOR_ELEMENT_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, TaskId.GENERATING_REPORT.toString());
            pstmt.setString(4, MessageStatus.COMPLETED.toString());
            pstmt.setString(5, MessageStatus.FAILED.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt(1);
                retour = (nb > 0);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
    private static final String SEARCHING_FOR_COMPUTING_ELEMENT_QUERY = "SELECT count(id_message) "
            + " FROM CAQS_MESSAGES" + " WHERE id_elt = ?" + " AND id_bline = ?"
            + " AND ID_TASK = ?" + " AND STATUS != ?" + " AND STATUS != ?";

    /**
     * @{@inheritDoc }
     */
    public boolean isComputingElement(String idElt, String idBline) {
        boolean retour = false;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("searching for calculation : " + idElt + ", " + idBline);
        try {
            pstmt = connection.prepareStatement(SEARCHING_FOR_COMPUTING_ELEMENT_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, TaskId.COMPUTING.toString());
            pstmt.setString(4, MessageStatus.COMPLETED.toString());
            pstmt.setString(5, MessageStatus.FAILED.toString());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt(1);
                retour = (nb > 0);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
    private static final String ELEMENT_NEEDS_RECOMPUTE_QUERY =
            "SELECT count(id_message) "
            + " FROM CAQS_MESSAGES"
            + " WHERE id_elt in (select id_elt from ELEMENT where id_main_elt = ? " +
            " OR (id_main_elt IS NULL AND id_elt = ?))"
            + " AND id_bline = ?" + " AND SEEN = 0";

    /**
     * @{@inheritDoc }
     */
    public boolean elementNeedsRecompute(String idElt, String idBline) {
        boolean retour = false;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("searching for calculation : " + idElt + ", " + idBline);
        try {
            String query = ELEMENT_NEEDS_RECOMPUTE_QUERY;
            String addendum = this.getAllRecomputeNeededTasksForSQLQuery();
            if (!"".equals(addendum)) {
                query += " AND (" + addendum + " )";
            }
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idElt);
            pstmt.setString(3, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt(1);
                retour = (nb > 0);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    /**
     * @return une chaine de caractere qui donne, pour inclusion dans une clause where
     * d'une requete sql, la liste des taches definissant qu'un recalcul est necessaire
     */
    private String getAllRecomputeNeededTasksForSQLQuery() {
        StringBuffer retour = new StringBuffer("");
        boolean alreadyOne = false;
        for (TaskId task : TaskId.values()) {
            if (task.recomputeIsNeededAfterCompletion()) {
                if (alreadyOne) {
                    retour.append(" OR ");
                }
                alreadyOne = true;
                retour.append(" ID_TASK = '").append(task.toString()).append("'");
            }
        }
        return retour.toString();
    }
    private static final String UPDATE_TASK_AS_RECOMPUTED_QUERY = "UPDATE CAQS_MESSAGES "
            + " SET SEEN=1" + " WHERE id_elt in ("
            + "	select id_elt from ELEMENT " + "	where id_main_elt = ?"
            + "		OR (id_main_elt IS NULL AND id_elt = ?)" + "  )"
            + " AND id_bline = ?";

    /**
     * @{@inheritDoc }
     */
    public void updateElementAsRecomputed(String idElt, String idBline) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        logger.trace("searching for calculation : " + idElt + ", " + idBline);
        try {
            String query = UPDATE_TASK_AS_RECOMPUTED_QUERY;
            String addendum = this.getAllRecomputeNeededTasksForSQLQuery();
            if (!"".equals(addendum)) {
                query += " AND (" + addendum + " )";
            }
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idElt);
            pstmt.setString(3, idBline);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String SET_IN_PROGRESS_TASKS_AS_FAILED_QUERY = "UPDATE CAQS_MESSAGES "
            + " SET STATUS=?" + " WHERE STATUS=?";

    /**
     * @{@inheritDoc }
     */
    public void setInProgressTaskAsFailed() {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(SET_IN_PROGRESS_TASKS_AS_FAILED_QUERY);
            pstmt.setString(1, MessageStatus.FAILED.toString());
            pstmt.setString(2, MessageStatus.IN_PROGRESS.toString());
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
}

package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.TendanceDao;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

public class TendanceDbmsDao implements TendanceDao {

    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static TendanceDbmsDao singleton = new TendanceDbmsDao();

    public static TendanceDao getInstance() {
        return TendanceDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    protected TendanceDbmsDao() {
    }
    /** Requete de calcul des tendances pour les metriques. */
    private static final String TENDANCES_MET_REQUEST = "UPDATE Qametrique qa"
            + " SET tendance=("
            + " Select lastqa.valbrute_qamet"
            + " From Qametrique lastqa"
            + " Where lastqa.id_bline=?"
            + " And lastqa.id_elt=qa.id_elt"
            + " And lastqa.id_met=qa.id_met"
            + " )"
            + " Where qa.id_bline=?"
            + " And qa.id_elt=?";
    /** Requete de calcul des tendances pour les criteres. */
    private static final String TENDANCES_CRIT_REQUEST = "UPDATE Critere_bline crit"
            + " SET tendance=("
            + " Select lastcrit.note_cribl"
            + " From Critere_bline lastcrit"
            + " Where lastcrit.id_bline=?"
            + " And lastcrit.id_elt=crit.id_elt"
            + " And lastcrit.id_crit=crit.id_crit"
            + " And lastcrit.id_pro=crit.id_pro"
            + " )"
            + " Where crit.id_bline=?"
            + " And crit.id_elt=?";
    /** Requete de calcul des tendances pour les facteurs. */
    private static final String TENDANCES_FACT_REQUEST = "UPDATE Facteur_bline fac"
            + " SET tendance=("
            + " Select lastfac.note_facbl"
            + " From Facteur_bline lastfac"
            + " Where lastfac.id_bline=?"
            + " And lastfac.id_elt=fac.id_elt"
            + " And lastfac.id_fac=fac.id_fac"
            + " And lastfac.id_pro=fac.id_pro"
            + " )"
            + " Where fac.id_bline=?"
            + " And fac.id_elt=?";
    
    private static final String RESET_TENDANCES_MET_REQUEST = "UPDATE Qametrique "
            + " SET tendance = 0"
            + " Where id_bline=?"
            + " And id_elt=? and tendance is not null and tendance != 0";
    private static final String RESET_TENDANCES_CRIT_REQUEST = "UPDATE Critere_bline "
            + " SET tendance = 0"
            + " Where id_bline=?"
            + " And id_elt=? and tendance is not null and tendance != 0";
    private static final String RESET_TENDANCES_FACT_REQUEST = "UPDATE Facteur_bline "
            + " SET tendance = 0"
            + " Where id_bline=?"
            + " And id_elt=? and tendance is not null and tendance != 0";

    /**
     * @{@inheritDoc }
     */
    public void updateCriterionTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException {
        updateTendances(TENDANCES_CRIT_REQUEST, idElt, idCurrentBline, idPreviousBline, connection);
    }

    /**
     * @{@inheritDoc }
     */
    public void updateMetricTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException {
        updateTendances(TENDANCES_MET_REQUEST, idElt, idCurrentBline, idPreviousBline, connection);
    }

    /**
     * @{@inheritDoc }
     */
    public void updateFactorTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException {
        updateTendances(TENDANCES_FACT_REQUEST, idElt, idCurrentBline, idPreviousBline, connection);
    }

    /** Mise a jour des tendances.
     * @param request requete de mise a jour des tendances.
     */
    protected void updateTendances(String request, String idElt, String idCurrentBline, String idPreviousBline, Connection connection) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(request);
            stmt.setString(1, idPreviousBline);
            stmt.setString(2, idCurrentBline);
            stmt.setString(3, idElt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            mLog.error("Error updating tendances", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void updateAllTrends(String idElt, String idCurrentBline, String idPreviousBline) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            this.updateFactorTendances(idElt, idCurrentBline, idPreviousBline, connection);
            this.updateCriterionTendances(idElt, idCurrentBline, idPreviousBline, connection);
            this.updateMetricTendances(idElt, idCurrentBline, idPreviousBline, connection);
        } catch (DataAccessException exc) {
            mLog.error("updateAllTrends", exc);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    private void deleteTrends(String request, String idElt, String idCurrentBline, Connection connection) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(request);
            stmt.setString(1, idCurrentBline);
            stmt.setString(2, idElt);
            stmt.executeUpdate();
        } catch (SQLException exc) {
            mLog.error("deleteTrends", exc);
            throw new DataAccessException(exc);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
    }

    public void deleteAllTrends(String idElt, String idCurrentBline) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            this.deleteTrends(RESET_TENDANCES_FACT_REQUEST, idElt, idCurrentBline, connection);
            this.deleteTrends(RESET_TENDANCES_CRIT_REQUEST, idElt, idCurrentBline, connection);
            this.deleteTrends(RESET_TENDANCES_MET_REQUEST, idElt, idCurrentBline, connection);
        } catch (DataAccessException exc) {
            mLog.error("deleteAllTrends", exc);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }
}

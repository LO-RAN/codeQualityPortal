package com.compuware.caqs.dao.dbms.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.dao.dbms.TendanceDbmsDao;
import com.compuware.caqs.dao.interfaces.TendanceDao;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class TendanceSqlServerDao extends TendanceDbmsDao {

    private static TendanceSqlServerDao singleton = new TendanceSqlServerDao();

    public static TendanceDao getInstance() {
        return TendanceSqlServerDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private TendanceSqlServerDao() {
        super();
    }
    /** Requete de calcul des tendances pour les metriques. */
    private static final String TENDANCES_MET_REQUEST = "UPDATE Qametrique"
            + " SET tendance=("
            + " Select lastqa.valbrute_qamet"
            + " From Qametrique lastqa"
            + " Where lastqa.id_bline=?"
            + " And lastqa.id_elt=?"
            + " And lastqa.id_met=?"
            + " )"
            + " Where id_bline=?"
            + " And id_elt=?"
            + " And id_met=?";
    /** Requete de calcul des tendances pour les criteres. */
    private static final String TENDANCES_CRIT_REQUEST = "UPDATE Critere_bline"
            + " SET tendance=("
            + " Select lastcrit.note_cribl"
            + " From Critere_bline lastcrit"
            + " Where lastcrit.id_bline=?"
            + " And lastcrit.id_elt=?"
            + " And lastcrit.id_crit=?"
            + " )"
            + " Where id_bline=?"
            + " And id_elt=?"
            + " And id_crit=?";
    /** Requete de calcul des tendances pour les facteurs. */
    private static final String TENDANCES_FACT_REQUEST = "UPDATE Facteur_bline"
            + " SET tendance=("
            + " Select lastfac.note_facbl"
            + " From Facteur_bline lastfac"
            + " Where lastfac.id_bline=?"
            + " And lastfac.id_elt=?"
            + " And lastfac.id_fac=?"
            + " )"
            + " Where id_bline=?"
            + " And id_elt=?"
            + " And id_fac=?";

    /**
     * Mise a jour des tendances pour les criteres.
     * @param idElt identifiant de l'element.
     * @param idCurrentBline identifiant de la baseline courante.
     * @param idPreviousBline identifiant de la baseline precedente.
     * @param connection the current JDBC connection.
     * @throws DataAccessException
     */
    @Override
    public void updateCriterionTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException {
        List<String> targetList = retrieveTargetList(idElt, idCurrentBline, CRIT_LIST_RETRIEVE_QUERY, connection);
        updateTendances(TENDANCES_CRIT_REQUEST, idElt, idCurrentBline, idPreviousBline, targetList, connection);
    }

    /**
     * Mise a jour des tendances pour les objectifs.
     * @param idElt identifiant de l'element.
     * @param idCurrentBline identifiant de la baseline courante.
     * @param idPreviousBline identifiant de la baseline precedente.
     * @param connection the current JDBC connection.
     * @throws DataAccessException
     */
    @Override
    public void updateFactorTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException {
        List<String> targetList = retrieveTargetList(idElt, idCurrentBline, FACT_LIST_RETRIEVE_QUERY, connection);
        updateTendances(TENDANCES_FACT_REQUEST, idElt, idCurrentBline, idPreviousBline, targetList, connection);
    }

    /**
     * Mise a jour des tendances pour les metriques.
     * @param idElt identifiant de l'element.
     * @param idCurrentBline identifiant de la baseline courante.
     * @param idPreviousBline identifiant de la baseline precedente.
     * @param connection the current JDBC connection.
     * @throws DataAccessException
     */
    @Override
    public void updateMetricTendances(String idElt, String idCurrentBline, String idPreviousBline, Connection connection)
            throws DataAccessException {
        List<String> targetList = retrieveTargetList(idElt, idCurrentBline, MET_LIST_RETRIEVE_QUERY, connection);
        updateTendances(TENDANCES_MET_REQUEST, idElt, idCurrentBline, idPreviousBline, targetList, connection);
    }
    private static final String CRIT_LIST_RETRIEVE_QUERY = "Select distinct id_crit From Critere_bline Where id_elt=? and id_bline=?";
    private static final String FACT_LIST_RETRIEVE_QUERY = "Select distinct id_fac From Facteur_bline Where id_elt=? and id_bline=?";
    private static final String MET_LIST_RETRIEVE_QUERY = "Select distinct id_met From Qametrique Where id_elt=? and id_bline=?";

    private List<String> retrieveTargetList(String idElt, String idBline, String query, Connection connection) {
        List<String> result = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, idElt);
            stmt.setString(2, idBline);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            mLog.error("Error getting target list", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
        return result;
    }

    /** Mise a jour des tendances.
     * @param request requete de mise a jour des tendances.
     */
    protected void updateTendances(String request, String idElt, String idCurrentBline, String idPreviousBline, List<String> targetList, Connection connection)
            throws DataAccessException {
        if (targetList != null && !targetList.isEmpty()) {
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement(request);
                Iterator<String> i = targetList.iterator();
                String idTarget = null;
                while (i.hasNext()) {
                    idTarget = (String) i.next();
                    stmt.setString(1, idPreviousBline);
                    stmt.setString(2, idElt);
                    stmt.setString(3, idTarget);
                    stmt.setString(4, idCurrentBline);
                    stmt.setString(5, idElt);
                    stmt.setString(6, idTarget);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                mLog.error("Error updating tendances", e);
                throw new DataAccessException("Error updating tendances", e);
            } finally {
                JdbcDAOUtils.closePrepareStatement(stmt);
            }
        }
    }
}

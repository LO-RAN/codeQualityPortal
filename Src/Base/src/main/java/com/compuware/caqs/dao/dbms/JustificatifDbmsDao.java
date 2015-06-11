/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:16
 */
package com.compuware.caqs.dao.dbms;

// Imports for DBMS transactions.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.JustificatifDao;
import com.compuware.caqs.domain.dataschemas.JustificatifResume;
import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class JustificatifDbmsDao implements JustificatifDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String JUSTIFICATIF_BY_ID_REQUEST = "SELECT id_just, lib_just, desc_just, statut_just, cuser_just, just_link FROM Justification WHERE id_just=?";

    /** Creates a new instance of Class */
    public JustificatifDbmsDao() {
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.JustificatifDao#retrieveJustificatifById(java.lang.String, java.sql.Connection)
     */
    public JustificationBean retrieveJustificatifById(java.lang.String id, Connection connection) {
        JustificationBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String link = null;

        try {
            pstmt = connection.prepareStatement(JUSTIFICATIF_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new JustificationBean();
                result.setId(rs.getString("id_just"));
                result.setLib(rs.getString("lib_just"));
                result.setDesc(rs.getString("desc_just"));
                result.setStatut(rs.getString("statut_just"));
                result.setUser(rs.getString("cuser_just"));
                link = rs.getString("just_link");
            }
        } catch (SQLException e) {
            logger.error("Error during Justificatif retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        if (link != null && result != null) {
            result.setLinkedJustificatif(retrieveJustificatifById(link, connection));
        }
        logger.debug("Retrieve just: id=" + id + ", val=" + result);
        return result;
    }
    private static final String ALL_JUSTIFICATIONS_QUERY = "Select pro.lib_pro, just.id_just, just.lib_just, "
            + "just.dinst_just, just.cuser_just, bline.id_bline, bline.lib_bline, crit.id_crit, elt.id_elt, "
            + "elt.lib_elt, elt.id_telt, just.dmaj_just, just.desc_just"
            + " From Justification just, Critere_bline critb, Critere crit, Element elt, Projet pro, Baseline bline, Droits dr"
            + " Where just.statut_just = ?"
            + " And critb.id_just=just.id_just"
            + " And critb.id_elt=elt.id_elt"
            + " And ("
            + "    (elt.id_main_elt=dr.id_elt  And dr.id_profil_user = ?)"
            + "     Or (elt.id_elt=dr.id_elt And dr.id_profil_user = ?)"
            + ")"
            + " And pro.id_pro=elt.id_pro"
            + " And critb.id_bline=bline.id_bline"
            + " And critb.id_crit=crit.id_crit"
            + " order by pro.id_pro, just.cuser_just, bline.id_bline";

    public Collection<JustificatifResume> getAllJustifications(String req, String userId) throws DataAccessException {
        Collection<JustificatifResume> result = new ArrayList<JustificatifResume>();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(ALL_JUSTIFICATIONS_QUERY);
            pstmt.setString(1, req);
            pstmt.setString(2, userId);
            pstmt.setString(3, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                JustificatifResume resume = new JustificatifResume(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getDate(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10), rs.getString(11), rs.getDate(12), false);
                resume.setJustificatifDesc(rs.getString("desc_just"));
                result.add(resume);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des Justificatifs de Statut="
                    + req, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    private static final String CRITERION_JUST_NOTE_QUERY = "Select note_cribl, just_note_cribl"
            + " From Critere_bline"
            + " Where id_elt=?"
            + " And id_crit=?"
            + " And id_bline=?";

    public double[] getJustifiedNote(String idElt, String idCritfac, String idBline, String dbtable) throws DataAccessException {
        double[] result = {0.0, 0.0};
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (dbtable != null && dbtable.equals("CRIT")) {
                pstmt = conn.prepareStatement(CRITERION_JUST_NOTE_QUERY);
            }
            pstmt.setString(1, idElt);
            pstmt.setString(2, idCritfac);
            pstmt.setString(3, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result[0] = rs.getDouble(1);
                result[1] = rs.getDouble(2);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la note justifiee.");
            logger.error("id_bline=" + idBline + ", id_elt=" + idElt, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    private static final String JUSTIFICATION_BY_ID_QUERY =
            "Select lib_just, desc_just, statut_just, cuser_just, dinst_just, dmaj_just, just_link"
            + " From Justification"
            + " Where id_just=?";

    public JustificationBean getJustificatif(String idJust, double note) throws DataAccessException {
        JustificationBean result = null;
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(JUSTIFICATION_BY_ID_QUERY);
            pstmt.setString(1, idJust);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new JustificationBean(idJust, note, rs.getString("lib_just"), rs.getString("desc_just"), rs.getString("statut_just"),
                        rs.getString("cuser_just"), rs.getTimestamp("dinst_just"), rs.getTimestamp("dmaj_just"));
                result.setLinkedJustificatif(retrieveOldJustificatif(conn, result, rs.getString("just_link")));
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation du Justificatif "
                    + idJust, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    private static final String OLD_JUSTIFICATIF_RETRIEVE_QUERY =
            "Select lib_just, desc_just, statut_just, cuser_just, dinst_just, dmaj_just, just_link"
            + " From Justification"
            + " Where id_just=?";

    private JustificationBean retrieveOldJustificatif(Connection conn, JustificationBean just, String justLinkId) throws SQLException {
        JustificationBean result = null;
        if (conn != null && just != null && justLinkId != null) {
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = conn.prepareStatement(OLD_JUSTIFICATIF_RETRIEVE_QUERY);
                pstmt.setString(1, justLinkId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = new JustificationBean(justLinkId, just.getNote(), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5), rs.getTimestamp(6));
                }
            } catch (SQLException e) {
                logger.error(e.toString());
                throw e;
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
        }
        return result;
    }

    public void setJustification(String idElt, String idBline, String idCritfacqa, JustificationBean just, boolean update) throws DataAccessException {
        if (just != null) {
            Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                if (update) {
                    performUpdate(just, conn);
                } else {
                    performInsert(just, conn);
                }
                updateElement(conn, just, idElt, idBline, idCritfacqa);
                DataAccessCache dataCache = DataAccessCache.getInstance();
                dataCache.clearCache();
            } catch (SQLException e) {
                JdbcDAOUtils.rollbackConnection(conn);
                logger.error("Erreur lors de la mise a jour du justificatif.");
                logger.error("id_bline=" + idBline + ", id_elt=" + idElt
                        + ", id=" + idCritfacqa, e);
                throw new DataAccessException(e);
            } finally {
                JdbcDAOUtils.closeConnection(conn);
            }
        }
    }

    public void linkJustificatifs(String idJustOld, String idJustNew) throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        String request = "Update Justification Set just_link=? Where id_just=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(request);
            pstmt.setString(1, idJustOld);
            pstmt.setString(2, idJustNew);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(conn);
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(conn);
            logger.error("Erreur lors de la liaison des Justificatifs "
                    + idJustOld + " et " + idJustNew, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    public void removeJustificatif(String idJustNew) throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        String request = "delete from Justification Where id_just=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(request);
            pstmt.setString(1, idJustNew);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(conn);
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(conn);
            logger.error("Erreur lors de la suppression du Justificatif "
                    + idJustNew, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
    }
    private static final String INSERT_JUSTIFICATION_QUERY =
            "INSERT INTO Justification"
            + " VALUES (?, ?, ?, {fn now()}, null, ?, null, ?, null)";

    private void performInsert(JustificationBean just, Connection conn) throws SQLException {
        if (conn != null && just != null) {
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(INSERT_JUSTIFICATION_QUERY);
                pstmt.setString(1, just.getId());
                pstmt.setString(2, just.getLib());
                pstmt.setString(3, just.getDesc());
                pstmt.setString(4, just.getStatut());
                pstmt.setString(5, just.getUser());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                logger.error(e.toString());
                throw e;
            } finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
        }
    }
    private static final String UPDATE_JUSTIFICATION_QUERY =
            "UPDATE Justification"
            + " SET lib_just=?, desc_just=?, dmaj_just={fn now()}, statut_just=?, cuser_just=?"
            + " WHERE id_just=?";

    private void performUpdate(JustificationBean just, Connection conn) throws SQLException {
        if (conn != null && just != null) {
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(UPDATE_JUSTIFICATION_QUERY);
                pstmt.setString(1, just.getLib());
                pstmt.setString(2, just.getDesc());
                pstmt.setString(3, just.getStatut());
                pstmt.setString(4, just.getUser());
                pstmt.setString(5, just.getId());
                pstmt.executeUpdate();
                JdbcDAOUtils.commit(conn);
            } catch (SQLException e) {
                logger.error(e.toString());
                throw e;
            } finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
        }
    }
    private static final String UPDATE_CRITERION_BLINE_QUERY = "UPDATE Critere_bline"
            + " SET id_just=?, just_note_cribl=?"
            + " WHERE id_elt=?"
            + " AND id_crit=?"
            + " AND id_bline=?";

    private void updateElement(Connection conn, JustificationBean just, String idElt, String idBline, String idCritfacqa) throws SQLException {
        String request = UPDATE_CRITERION_BLINE_QUERY;

        if (conn != null && just != null) {
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(request);
                pstmt.setString(1, just.getId());
                if (just.getNote() > 0) {
                    pstmt.setDouble(2, just.getNote());
                } else {
                    pstmt.setNull(2, Types.DOUBLE);
                }
                pstmt.setString(3, idElt);
                pstmt.setString(4, idCritfacqa);
                pstmt.setString(5, idBline);
                pstmt.executeUpdate();
                JdbcDAOUtils.commit(conn);
            } catch (SQLException e) {
                logger.error(e.toString());
                throw e;
            } finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
        }
    }
    private static final String ALL_JUSTIFICATIONS_FOR_ELEMENT_QUERY =
            "Select pro.lib_pro, just.id_just, just.lib_just, just.dinst_just, just.cuser_just, bline.id_bline,"
            + " bline.lib_bline, crit.id_crit, elt.id_elt, elt.lib_elt, elt.id_telt, critb.just_note_cribl,"
            + " critb.note_cribl, just.dmaj_just, just.desc_just"
            + " From Justification just, Critere_bline critb, Critere crit, Element elt, Projet pro, Baseline bline"
            + " Where just.statut_just = ?"
            + " And critb.id_just=just.id_just"
            + " And critb.id_elt = elt.id_elt"
            + " And pro.id_pro = elt.id_pro"
            + " And ( (elt.id_main_elt IS NULL And elt.id_elt = ?) OR (elt.id_main_elt IS NOT NULL And elt.id_main_elt = ?) )"
            + " And critb.id_bline = ?"
            + " And critb.id_crit=crit.id_crit"
            + " And bline.id_bline = ?"
            + " order by pro.id_pro, just.cuser_just";

    public Collection<JustificatifResume> getAllCriterionJustificationsForElement(String req, String idEa, String idBline)
            throws DataAccessException {
        Collection<JustificatifResume> result = new ArrayList<JustificatifResume>();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(ALL_JUSTIFICATIONS_FOR_ELEMENT_QUERY);
            pstmt.setString(1, req);
            pstmt.setString(2, idEa);
            pstmt.setString(3, idEa);
            pstmt.setString(4, idBline);
            pstmt.setString(5, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                JustificatifResume resume = new JustificatifResume(
                        rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getDate(4), rs.getString(5), rs.getString(6),
                        rs.getString(7), rs.getString(8), rs.getString(9),
                        rs.getString(10), rs.getString(11), rs.getDate("dmaj_just"), false);
                resume.setNoteJustificatif(rs.getInt("just_note_cribl"));
                resume.setOldMark(rs.getInt("note_cribl"));
                resume.setJustificatifDesc(rs.getString("desc_just"));
                result.add(resume);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des Justificatifs de Statut="
                    + req, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    private static final String NB_JUSTIFICATIONS_FOR_ELEMENT_QUERY =
            "Select count(just.id_just)"
            + " From Justification just, Critere_bline critb, Critere crit, Element elt, Projet pro, Baseline bline"
            + " Where just.statut_just = ?"
            + " And critb.id_just=just.id_just"
            + " And critb.id_elt = elt.id_elt"
            + " And pro.id_pro=?"
            + " And critb.id_bline = ?"
            + " And critb.id_crit=crit.id_crit"
            + " And bline.id_bline = ?"
            + " And elt.id_pro = ?"
            + " And (elt.id_main_elt = ? OR (elt.id_elt=? AND elt.id_main_elt IS NULL))";

    public int getNbCriterionJustificationsForElement(String req, String idPro, String idBline, String idEltEA)
            throws DataAccessException {
        int result = 0;
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(NB_JUSTIFICATIONS_FOR_ELEMENT_QUERY);
            pstmt.setString(1, req);
            pstmt.setString(2, idPro);
            pstmt.setString(3, idBline);
            pstmt.setString(4, idBline);
            pstmt.setString(5, idPro);
            pstmt.setString(6, idEltEA);
            pstmt.setString(7, idEltEA);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des Justificatifs de Statut="
                    + req, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
}

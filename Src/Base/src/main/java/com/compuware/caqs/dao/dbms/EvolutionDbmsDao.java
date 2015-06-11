package com.compuware.caqs.dao.dbms;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.EvolutionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.domain.dataschemas.CriterionRepartitionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.RepartitionBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public class EvolutionDbmsDao implements EvolutionDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    private static EvolutionDao singleton = new EvolutionDbmsDao();
    private static final String FILTER_DESC_SUBQUERY =
            " And desc_elt like ";
    private static final String ORDER_BY_LIB_ELT_SUBQUERY =
            " Order by e.lib_elt";
    private static final String FILTER_TYPE_SUBQUERY =
            " And id_telt = ";
    /* Nouveau et Mauvais */
    private static final String NEW_AND_BAD_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, note1 + note2 " +
            " FROM ELEMENT_BASELINE_INFO ebi, Element e  "+
            " WHERE ebi.id_bline = ? And ebi.id_main_elt = ? " +
            " And ebi.id_elt = e.id_elt " +
            " And (ebi.note1 > 0 OR ebi.note2 > 0) " +
            " AND e.id_elt = ebi.id_elt and e.DINST_ELT > (select DMAJ_BLINE from baseline where id_bline = ?)";

    /* Ancien degrade */
    private static final String OLD_AND_WORST_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, eei.nbworst"
            + " From ELEMENT_BASELINE_INFO ebi, Element e, ELEMENT_EVOLUTION_INFO eei"
            + " Where ebi.id_bline = ?" + " And ebi.id_main_elt = ?"
            + " And ebi.id_elt = e.id_elt "
            + " And eei.nbworst > 0" + " And eei.nbbetter = 0 "
            + " AND eei.id_main_elt = ebi.id_main_elt AND eei.id_elt = ebi.id_elt AND eei.id_bline = ebi.id_bline AND eei.id_prev_bline = ?";

    /* Ancien ameliore */
    private static final String OLD_AND_BETTER_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, eei.nbbetter"
            + " From ELEMENT_BASELINE_INFO ebi, Element e, ELEMENT_EVOLUTION_INFO eei"
            + " Where ebi.id_bline = ?" + " And ebi.id_main_elt = ?"
            + " And ebi.id_elt = e.id_elt "
            + " And eei.nbworst = 0" + " And eei.nbbetter > 0"
            + " AND eei.id_main_elt = ebi.id_main_elt AND eei.id_elt = ebi.id_elt AND eei.id_bline = ebi.id_bline AND eei.id_prev_bline = ?";

    /* Ancien ameliore et degrade */
    private static final String OLD_BETTER_AND_WORST_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, eei.nbworst + eei.nbbetter"
            + " From ELEMENT_BASELINE_INFO ebi, Element e, ELEMENT_EVOLUTION_INFO eei"
            + " Where ebi.id_bline = ?" + " And ebi.id_main_elt = ?"
            + " And ebi.id_elt = e.id_elt "
            + " And eei.nbworst > 0" + " And eei.nbbetter > 0"
            + " AND eei.id_main_elt = ebi.id_main_elt AND eei.id_elt = ebi.id_elt AND eei.id_bline = ebi.id_bline AND eei.id_prev_bline = ?";

    /* Ancien mauvais et stable */
    private static final String BAD_AND_STABLE_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, eei.nbstable"
            + " From ELEMENT_BASELINE_INFO ebi, Element e, ELEMENT_EVOLUTION_INFO eei"
            + " Where ebi.id_bline = ?" + " And ebi.id_main_elt = ?"
            + " And ebi.id_elt = e.id_elt "
            + " And eei.nbworst = 0" + " And eei.nbbetter = 0"
            + " And eei.nbstable > 0"
            + " And (note1 > 0 OR note2 > 0)"
            + " AND eei.id_main_elt = ebi.id_main_elt AND eei.id_elt = ebi.id_elt AND eei.id_bline = ebi.id_bline AND eei.id_prev_bline = ?";

    /* Ancien et stable */
    private static final String STABLE_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, eei.nbstable"
            + " From ELEMENT_BASELINE_INFO ebi, Element e, ELEMENT_EVOLUTION_INFO eei"
            + " Where ebi.id_bline = ?" + " And ebi.id_main_elt = ?"
            + " And ebi.id_elt = e.id_elt "
            + " And eei.nbworst = 0" + " And eei.nbbetter = 0"
            + " And eei.nbstable > 0"
            + " AND eei.id_main_elt = ebi.id_main_elt AND eei.id_elt = ebi.id_elt AND eei.id_bline = ebi.id_bline AND eei.id_prev_bline = ?";
    private static final String REPARTITION_SELECT_FROM_SUBQUERY =
            "Select cb.id_crit, count(*)"
            + " From Critere_bline cb, ELEMENT_EVOLUTION_INFO eei";
    private static final String REPARTITION_SELECT_MULTIPLE_CB_FROM_SUBQUERY =
            "Select cb.id_crit, count(*)"
            + " From Critere_bline cb, ELEMENT_EVOLUTION_INFO eei, Critere_bline pcb";
    private static final String REPARTITION_FILTER_FROM_SUBQUERY =
            ", Element e";
    private static final String REPARTITION_FILTER_JOIN_SUBQUERY =
            " And cb.id_elt = e.id_elt" + " And eei.id_elt = e.id_elt"
            + " And eei.id_main_elt = e.id_main_elt";
    private static final String REPARTITION_FILTER_MULTIPLE_CB_JOIN_SUBQUERY =
            " And cb.id_elt = e.id_elt" + " And eei.id_elt = e.id_elt"
            + " And pcb.id_elt = e.id_elt"
            + " And eei.id_main_elt = e.id_main_elt";
    private static final String REPARTITION_WHERE_SUBQUERY =
            " Where eei.id_bline = ? And eei.id_prev_bline = ? And eei.id_main_elt = ?"
            + " And cb.id_elt = eei.id_elt " + " And cb.id_bline = eei.id_bline";
    private static final String REPARTITION_WHERE_MULTIPLE_CB_SUBQUERY =
            " Where eei.id_bline = ? And eei.id_prev_bline = ? And eei.id_main_elt = ?"
            + " And cb.id_elt = eei.id_elt And pcb.id_elt = eei.id_elt "
            + " And cb.id_bline = eei.id_bline And pcb.id_bline = eei.id_prev_bline";
    private static final String REPARTITION_GROUP_BY_SUBQUERY =
            " Group by cb.id_crit";

    /* Ancien mauvais et stable */
    private static final String REPARTITION_CRIT_BAD_AND_STABLE_QUERY =
            " And cb.note_cribl < 3"
            + " And cb.note_cribl > 0" + " AND eei.nbworst = 0"
            + " AND eei.nbbetter = 0";
    /* Ancien degrade */
    private static final String REPARTITION_CRIT_OLD_AND_WORST_QUERY =
            " And cb.note_cribl < pcb.note_cribl"
            + " AND eei.nbbetter = 0 AND eei.nbworst > 0";
    /* Ancien ameliore */
    private static final String REPARTITION_CRIT_OLD_AND_BETTER_QUERY =
            " And cb.note_cribl > pcb.note_cribl"
            + " AND eei.nbworst = 0 AND eei.nbbetter > 0";
    /* Ancien ameliore et degrade */
    private static final String REPARTITION_CRIT_OLD_BETTER_AND_WORST_QUERY =
            " And cb.note_cribl < pcb.note_cribl"
            + " AND eei.nbworst > 0" + " AND eei.nbbetter > 0";

    private static final String CRITERION_BOTTOM_UP_DETAIL =
            "Select elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.note_cribl, cb.just_note_cribl, "
            + " cb.id_just, pcb.just_note_cribl as p_just_note_cribl, pcb.note_cribl as p_note_cribl"
            + " From Critere_bline cb, Element elt, Critere_bline pcb"
            + " Where cb.id_elt=?"
            + " And cb.id_elt=elt.id_elt" + " And cb.id_bline=?"
            + " And cb.id_pro=?" + " And elt.id_pro=cb.id_pro "
            + " And elt.id_elt = pcb.id_elt "
            + " And pcb.id_crit = cb.id_crit And pcb.id_bline = ? "
            + " order by cb.id_crit";
    private final Object lock = new Object();

    public static EvolutionDao getInstance() {
        return EvolutionDbmsDao.singleton;
    }

    /**
     * @{@inheritDoc }
     */
    public void clearBaselineEvolutionDatas(String idBline) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            //on commence par peupler la table temporaire
            String request = "DELETE FROM ELEMENT_EVOLUTION_INFO WHERE id_bline = ? OR id_prev_bline = ?";
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idBline);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Erreur lors de la purge des donnees d'evolutions idbline= "
                    + idBline, e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * Creates a new instance of Class
     */
    private EvolutionDbmsDao() {
    }

    private boolean alreadyHasEvolutionData(String idEa, String idBline, String idPrevBline, Connection connection) {
        boolean retour = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //on commence par peupler la table temporaire
            String request = "SELECT count(id_elt) as nb FROM element_evolution_info "
                    + " WHERE id_main_elt = ? AND id_bline = ? AND id_prev_bline = ?";
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, idEa);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idPrevBline);
            rs = pstmt.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    int nb = rs.getInt("nb");
                    retour = (nb > 0);
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la creation des donnees d'evolution idea= "
                    + idEa + ", id_bline=" + idBline + " idprevbline="
                    + idPrevBline, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return retour;
    }

    private void cleanTemporaryTable(Connection connection, String idBline, String idPrevBline) {
        PreparedStatement pstmt = null;
        try {
            String request = "DELETE FROM TMP_ELEMENT_EVOLUTION_INFO WHERE id_bline = ? AND id_prev_bline = ?";
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idPrevBline);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Erreur lors du vidage de la table temporaire", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }

    /**
     * cree dans une base les donnees d'evolutions entre deux baselines
     */
    private void createElementsEvolutionDatas(String idEa, String idBline, BaselineBean previousBaseline, Connection connection) {
        PreparedStatement pstmt = null;
        try {
            //on commence par peupler la table temporaire
            String request = "INSERT INTO TMP_ELEMENT_EVOLUTION_INFO(ID_ELT, ID_BLINE, ID_PREV_BLINE, ID_MAIN_ELT, ID_CRIT, NOTE_CRIBL, PREV_NOTE_CRIBL)"
                    + "SELECT  elt.id_elt, cb.id_bline, pcb.id_bline, elt.id_main_elt, cb.id_crit, "
                    + " CASE WHEN cb.just_note_cribl IS NOT NULL THEN cb.just_note_cribl ELSE cb.note_cribl END, "
                    + " CASE WHEN pcb.just_note_cribl IS NOT NULL THEN pcb.just_note_cribl ELSE pcb.note_cribl END "
                    + " FROM "
                    + " ELEMENT elt, CRITERE_BLINE cb, CRITERE_BLINE pcb "
                    + " WHERE " + " elt.id_main_elt = ? "
                    + " AND elt.id_elt = cb.id_elt AND elt.id_elt = pcb.id_elt "
                    + " AND cb.id_crit = pcb.id_crit "
                    + " AND cb.id_bline = ? AND pcb.id_bline = ? ";
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, idEa);
            pstmt.setString(2, idBline);
            pstmt.setString(3, previousBaseline.getId());
            int nb = pstmt.executeUpdate();
            JdbcDAOUtils.closePrepareStatement(pstmt);
            request = "INSERT INTO TMP_ELEMENT_EVOLUTION_INFO(ID_ELT, ID_BLINE, ID_PREV_BLINE, ID_MAIN_ELT, ID_CRIT, NOTE_CRIBL, PREV_NOTE_CRIBL)"
                    + "SELECT  elt.id_elt, cb.id_bline, ?, elt.id_main_elt, cb.id_crit, "
                    + " CASE WHEN cb.just_note_cribl IS NOT NULL THEN cb.just_note_cribl ELSE cb.note_cribl END, "
                    + " NULL "
                    + " FROM ELEMENT elt, CRITERE_BLINE cb "
                    + " WHERE  elt.id_main_elt = ?"
                    + " AND elt.id_elt = cb.id_elt AND elt.id_elt IN (SELECT distinct id_elt FROM Element WHERE id_main_elt = ? and DINST_ELT > ?) "
                    + " AND cb.id_bline = ? ";
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, previousBaseline.getId());
            pstmt.setString(2, idEa);
            pstmt.setString(3, idEa);
            pstmt.setTimestamp(4, previousBaseline.getDmaj());
            pstmt.setString(5, idBline);
            nb += pstmt.executeUpdate();
            JdbcDAOUtils.closePrepareStatement(pstmt);


            if (nb > 0) {
                //si au moins une ligne a ete inseree
                JdbcDAOUtils.closePrepareStatement(pstmt);
                //on va chercher les infos et on les insere
                request = "INSERT INTO ELEMENT_EVOLUTION_INFO(ID_ELT, ID_BLINE, ID_PREV_BLINE, ID_MAIN_ELT, NBBETTER, NBSTABLE, NBWORST) "
                        + " SELECT distinct TMP_ELEMENT_EVOLUTION_INFO.id_elt, ?, ?, ?, "
                        + " coalesce(\"nbbetter\",0), coalesce(\"nbstable\",0), coalesce(\"nbworst\",0) "
                        + " FROM TMP_ELEMENT_EVOLUTION_INFO "
                        + " LEFT OUTER JOIN "
                        + " (select id_elt, count(id_crit) as \"nbbetter\" "
                        + " FROM TMP_ELEMENT_EVOLUTION_INFO "
                        + " where prev_note_cribl IS NOT NULL AND note_cribl > prev_note_cribl AND id_bline = ? AND id_prev_bline = ?"
                        + " group by id_elt) betterTab on betterTab.id_elt = TMP_ELEMENT_EVOLUTION_INFO.id_elt "
                        + " LEFT OUTER JOIN "
                        + " (select id_elt, count(id_crit) as \"nbstable\" "
                        + " from TMP_ELEMENT_EVOLUTION_INFO "
                        + " where prev_note_cribl IS NOT NULL AND note_cribl = prev_note_cribl AND id_bline = ? AND id_prev_bline = ? "
                        + " group by id_elt) stableTab on stableTab.id_elt = TMP_ELEMENT_EVOLUTION_INFO.id_elt "
                        + " LEFT OUTER JOIN "
                        + " (select id_elt, count(id_crit) as \"nbworst\" "
                        + " FROM TMP_ELEMENT_EVOLUTION_INFO "
                        + " where prev_note_cribl IS NOT NULL AND note_cribl < prev_note_cribl AND id_bline = ? AND id_prev_bline = ? "
                        + " group by id_elt) worstTab on worstTab.id_elt = TMP_ELEMENT_EVOLUTION_INFO.id_elt";
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, idBline);
                pstmt.setString(2, previousBaseline.getId());
                pstmt.setString(3, idEa);
                pstmt.setString(4, idBline);
                pstmt.setString(5, previousBaseline.getId());
                pstmt.setString(6, idBline);
                pstmt.setString(7, previousBaseline.getId());
                pstmt.setString(8, idBline);
                pstmt.setString(9, previousBaseline.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la creation des donnees d'evolution idea= "
                    + idEa + ", id_bline=" + idBline + " idprevbline="
                    + previousBaseline.getId(), e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            this.cleanTemporaryTable(connection, idBline, previousBaseline.getId());
        }
    }

    private List<ElementEvolutionSummaryBean> retrieveEvolutionSummary(String query, String idEa, String idBline, BaselineBean previousBaseline) {
        List<ElementEvolutionSummaryBean> result = new ArrayList<ElementEvolutionSummaryBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        synchronized (this.lock) {
            if (!this.alreadyHasEvolutionData(idEa, idBline, previousBaseline.getId(), connection)) {
                this.createElementsEvolutionDatas(idEa, idBline, previousBaseline, connection);
            }
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idEa);
            pstmt.setString(3, previousBaseline.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementEvolutionSummaryBean bean = new ElementEvolutionSummaryBean();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setDesc(rs.getString("desc_elt"));
                bean.setNbCriterions(rs.getInt(4));
                result.add(bean);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la synthese d'evolution: id baseline="
                    + idBline, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private String createEvolutionQuery(String query, FilterBean filter) {
        StringBuffer result = new StringBuffer(query);
        if (filter.applyFilterDescElt()) {
            result.append(FILTER_DESC_SUBQUERY).append('\'').append(filter.getFilterDesc()).append('\'');
        }
        if (filter.applyFilterTypeElt()) {
            result.append(FILTER_TYPE_SUBQUERY).append('\'').append(filter.getTypeElt()).append('\'');
        }
        result.append(ORDER_BY_LIB_ELT_SUBQUERY);
        return result.toString();
    }

    /**
     * @{@inheritDoc }
     */
    public List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        String query = createEvolutionQuery(OLD_BETTER_AND_WORST_QUERY, filter);
        return retrieveEvolutionSummary(query, idElt, idBline, previousBaseline);
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        String query = createEvolutionQuery(NEW_AND_BAD_QUERY, filter);
        return retrieveEvolutionSummary(query, idElt, idBline, previousBaseline);
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        String query = createEvolutionQuery(OLD_AND_WORST_QUERY, filter);
        return retrieveEvolutionSummary(query, idElt, idBline, previousBaseline);
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        String query = createEvolutionQuery(OLD_AND_BETTER_QUERY, filter);
        return retrieveEvolutionSummary(query, idElt, idBline, previousBaseline);
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        String query = createEvolutionQuery(STABLE_QUERY, filter);
        return retrieveEvolutionSummary(query, idElt, idBline, previousBaseline);
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt, String idBline, BaselineBean previousBaseline, FilterBean filter) {
        String query = createEvolutionQuery(BAD_AND_STABLE_QUERY, filter);
        return retrieveEvolutionSummary(query, idElt, idBline, previousBaseline);
    }
    /**
     * Query used to retrieve element volumetry for a given EA and a given baseline.
     */
    private static final String RETRIEVE_ELEMENT_VOLUMETRY_BY_TYPE_QUERY =
            "Select id_telt, total, created, deleted, dmaj_bline"
            + " From Volumetry v, Baseline b"
            + " Where id_elt = ?"
            + " And b.id_bline = v.id_bline"
            + " ORDER BY id_telt";

    /** {@inheritDoc}
     */
    public List<Volumetry> retrieveVolumetryBetweenBaselines(String idElt, BaselineBean bline, BaselineBean prevBline) {
        List<Volumetry> result = new ArrayList<Volumetry>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ELEMENT_VOLUMETRY_BY_TYPE_QUERY);
            pstmt.setString(1, idElt);
            /*pstmt.setTimestamp(2, bline.getDmaj());
            pstmt.setTimestamp(3, prevBline.getDmaj());*/
            Volumetry currentVolumetry = null;
            rs = pstmt.executeQuery();
            String oldIdTelt = null;
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("dmaj_bline");
                if (ts == null
                        || ts.after(bline.getDmaj())
                        || ts.before(prevBline.getDmaj())
                        || ts.equals(prevBline.getDmaj())) {
                    continue;
                    //visiblement ce test se fait mal dans une requete sql
                }
                String idTelt = rs.getString("id_telt");
                if (!idTelt.equals(oldIdTelt)) {
                    //changement de bean
                    currentVolumetry = new Volumetry();
                    currentVolumetry.setIdElt(idElt);
                    currentVolumetry.setIdTElt(idTelt);
                    oldIdTelt = idTelt;
                    result.add(currentVolumetry);
                }
                currentVolumetry.setTotal(currentVolumetry.getTotal()
                        + rs.getInt("total"));
                currentVolumetry.setCreated(currentVolumetry.getCreated()
                        + rs.getInt("created"));
                currentVolumetry.setDeleted(currentVolumetry.getDeleted()
                        + rs.getInt("deleted"));
            }
        } catch (SQLException e) {
            logger.error("Error during Volumetry retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private String createRepartitionQuery(String query, FilterBean filter) {
        StringBuffer result = new StringBuffer(REPARTITION_SELECT_MULTIPLE_CB_FROM_SUBQUERY);
        if (filter.applyFilter()) {
            result.append(REPARTITION_FILTER_FROM_SUBQUERY);
        }
        result.append(REPARTITION_WHERE_MULTIPLE_CB_SUBQUERY);
        if (filter.applyFilter()) {
            result.append(REPARTITION_FILTER_MULTIPLE_CB_JOIN_SUBQUERY);
            if (filter.applyFilterDescElt()) {
                result.append(FILTER_DESC_SUBQUERY).append('\'').append(filter.getFilterDesc()).append('\'');
            }
            if (filter.applyFilterTypeElt()) {
                result.append(FILTER_TYPE_SUBQUERY).append('\'').append(filter.getTypeElt()).append('\'');
            }
        }
        result.append(query);
        result.append(REPARTITION_GROUP_BY_SUBQUERY);
        return result.toString();
    }

    /** {@inheritDoc}
     */
    public Collection retrieveRepartitionNewAndBadElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        StringBuilder query = new StringBuilder("Select cb.id_crit, count(*) From Critere_bline cb, Element e "+
            " Where cb.id_bline = ? "
            + " And cb.id_elt = e.id_elt "
            + " And e.DINST_ELT > (select DMAJ_BLINE from baseline where id_bline = ?) "
            + " And e.id_main_elt = ? ");
        if (filter.applyFilter()) {
            if (filter.applyFilterDescElt()) {
                query.append(FILTER_DESC_SUBQUERY).append('\'').append(filter.getFilterDesc()).append('\'');
            }
            if (filter.applyFilterTypeElt()) {
                query.append(FILTER_TYPE_SUBQUERY).append('\'').append(filter.getTypeElt()).append('\'');
            }
        }
        query.append(" And cb.note_cribl < 3 And cb.note_cribl > 0 Group by cb.id_crit");
        return retrieveEvolutionRepartitionByCriterion(idElt, idBline, previousIdBline, query.toString());
    }

    /** {@inheritDoc}
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldAndWorstElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        String query = createRepartitionQuery(REPARTITION_CRIT_OLD_AND_WORST_QUERY, filter);
        return retrieveEvolutionRepartitionByCriterion(idElt, idBline, previousIdBline, query);
    }

    /** {@inheritDoc}
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldAndBetterElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        String query = createRepartitionQuery(REPARTITION_CRIT_OLD_AND_BETTER_QUERY, filter);
        return retrieveEvolutionRepartitionByCriterion(idElt, idBline, previousIdBline, query);
    }

    /** {@inheritDoc}
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldBetterWorstElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        String query = createRepartitionQuery(REPARTITION_CRIT_OLD_BETTER_AND_WORST_QUERY, filter);
        return retrieveEvolutionRepartitionByCriterion(idElt, idBline, previousIdBline, query);
    }

    /** {@inheritDoc}
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionBadStableElements(String idElt, String idBline, String previousIdBline, FilterBean filter) {
        StringBuffer query = new StringBuffer(REPARTITION_SELECT_FROM_SUBQUERY);
        if (filter.applyFilter()) {
            query.append(REPARTITION_FILTER_FROM_SUBQUERY);
        }
        query.append(REPARTITION_WHERE_SUBQUERY);
        if (filter.applyFilter()) {
            query.append(REPARTITION_FILTER_JOIN_SUBQUERY);
            if (filter.applyFilterDescElt()) {
                query.append(FILTER_DESC_SUBQUERY).append('\'').append(filter.getFilterDesc()).append('\'');
            }
            if (filter.applyFilterTypeElt()) {
                query.append(FILTER_TYPE_SUBQUERY).append('\'').append(filter.getTypeElt()).append('\'');
            }
        }
        query.append(REPARTITION_CRIT_BAD_AND_STABLE_QUERY);
        query.append(REPARTITION_GROUP_BY_SUBQUERY);
        return retrieveEvolutionRepartitionByCriterion(idElt, idBline, previousIdBline, query.toString());
    }

    /** {@inheritDoc}
     */
    private Collection<CriterionRepartitionBean> retrieveEvolutionRepartitionByCriterion(String idElt, String idBline, String previousIdBline, String query) {
        Collection<CriterionRepartitionBean> result = new ArrayList<CriterionRepartitionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int max = 0;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idBline);
            pstmt.setString(2, previousIdBline);
            pstmt.setString(3, idElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int nb = rs.getInt(2);
                String critId = rs.getString(1);
                result.add(new CriterionRepartitionBean(critId, nb));
                max += nb;
            }
            purgeRepartitionCollection(result, 2 * max / 100);
        } catch (SQLException e) {
            logger.error("Error during Criterion retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private static void purgeRepartitionCollection(Collection coll, double max) {
        Iterator i = coll.iterator();
        while (i.hasNext()) {
            RepartitionBean bean = (RepartitionBean) i.next();
            if (bean.getNb() <= max) {
                i.remove();
            }
        }
    }

    /** {@inheritDoc}
     */
    public List<BottomUpDetailBean> retrieveCriterionBottomUpDetail(
            String idBline, String idPreviousBline, String idElt, String idPro) {
        List<BottomUpDetailBean> result = new ArrayList<BottomUpDetailBean>();
        FactorDao factorFacade = FactorDbmsDao.getInstance();
        List<CriterionPerFactorBean> factCritWeight = factorFacade.retrieveFactorCritereDef(idBline, idElt);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BOTTOM_UP_DETAIL);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idPro);
            pstmt.setString(4, idPreviousBline);
            rs = pstmt.executeQuery();
            BottomUpDetailBean bean = null;
            while (rs.next()) {
                if (bean == null) {
                    bean = new BottomUpDetailBean();
                    ElementBean elementBean = new ElementBean();
                    elementBean.setId(rs.getString("id_elt"));
                    elementBean.setLib(rs.getString("lib_elt"));
                    elementBean.setDesc(rs.getString("desc_elt"));
                    bean.setElement(elementBean);
                    bean.setCriterions(factCritWeight);
                    result.add(bean);
                }
                String idCrit = rs.getString("id_crit");
                CriterionPerFactorBean crit = bean.lookUp(idCrit);
                if (crit != null) {
                    // Un critere a une valeur non nulle ou n'est pas le seul crit�re d'un unique facteur.
                    crit.setNote(new Double(rs.getDouble("note_cribl")));
                    crit.setJustNote(rs.getDouble("just_note_cribl"));
                    String idJust = rs.getString("id_just");
                    if (idJust != null) {
                        crit.setJustificatif(new JustificatifDbmsDao().retrieveJustificatifById(idJust, connection));
                    }
                    double pScore = rs.getDouble("p_just_note_cribl");
                    if (pScore > 0) {
                        crit.setTendance(pScore);
                    } else {
                        crit.setTendance(rs.getDouble("p_note_cribl"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Criterion retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
}

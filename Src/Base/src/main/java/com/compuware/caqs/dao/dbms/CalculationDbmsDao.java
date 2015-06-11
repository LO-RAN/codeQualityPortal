/**
 * 
 */
package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.calculation.Baseline;
import com.compuware.caqs.business.calculation.Qametrique;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.CalculationDao;
import com.compuware.caqs.dao.util.QueryUtil;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.calcul.ModuleBaselineAssoc;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 *
 */
public class CalculationDbmsDao implements CalculationDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static CalculationDao singleton = new CalculationDbmsDao();

    public static CalculationDao getInstance() {
        return CalculationDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private CalculationDbmsDao() {
    }
    private static final String METRIC_QUERY =
            "Select id_met, valbrute_qamet, notecalc_qamet, just_met, just_valbrut_qamet, just_notecalc_qamet, qa.id_elt"
            + " From Qametrique qa, Element elt"
            + " Where id_bline = ?"
            + " And qa.id_elt = elt.id_elt"
            + " And (id_main_elt = ? or elt.id_elt = ?)";

    public Map<String, Map<String, ValuedObject>> retrieveMetrics(String idMainElt, String idBline) throws DataAccessException {
        Map<String, Map<String, ValuedObject>> result = new HashMap<String, Map<String, ValuedObject>>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement metstmt = null;
        ResultSet rs = null;
        try {
            // Pr�paration de la requ�te.
            metstmt = connection.prepareStatement(METRIC_QUERY);
            // Initialisation des param�tres de la requ�te.
            metstmt.setString(1, idBline);
            metstmt.setString(2, idMainElt);
            metstmt.setString(3, idMainElt);
            // Ex�cution de la requ�te.
            rs = metstmt.executeQuery();
            Map<String, ValuedObject> currentElement = null;
            while (rs.next()) {
                // Parcours du r�sultat.
                String idMet = rs.getString("id_met");
                String idElt = rs.getString("id_elt");
                currentElement = result.get(idElt);
                if (currentElement == null) {
                    currentElement = new HashMap<String, ValuedObject>();
                    result.put(idElt, currentElement);
                }
                // Stockage des metriques resultats dans la table de hachage
                // Avec comme cle l'identifiant de la metrique.
                currentElement.put(idMet, new Qametrique(idMet, rs.getDouble("valbrute_qamet"), rs.getDouble("notecalc_qamet"), rs.getDouble("just_valbrut_qamet"), rs.getDouble("just_notecalc_qamet")));
            }
        } catch (SQLException e) {
            logger.error("Error during metric retrieve", e);
            throw new DataAccessException("Error during metric retrieve", e);
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(metstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String CRITERION_RETRIEVE_QUERY =
            "Select crit.id_crit, crit.note_cribl, crit.id_elt, pkg.coeff_pack"
            + " From Critere_bline crit, Element elt, Package pkg"
            + " Where id_bline = ?"
            + " And crit.id_elt = elt.id_elt"
            + " And (id_main_elt = ? or elt.id_elt = ?)"
            + " And ("
            + " (elt.id_pack is null And pkg.id_pack='O')"
            + " Or"
            + " (elt.id_pack is not null And elt.id_pack = pkg.id_pack)"
            + " )";

    public Map<String, Map<String, Critere>> retrieveCriterion(String idMainElt, String idBline) throws DataAccessException {
        Map<String, Map<String, Critere>> result = new HashMap<String, Map<String, Critere>>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement metstmt = null;
        ResultSet rs = null;
        try {
            // Preparation de la requete.
            metstmt = connection.prepareStatement(CRITERION_RETRIEVE_QUERY);
            // Initialisation des parametres de la requete.
            metstmt.setString(1, idBline);
            metstmt.setString(2, idMainElt);
            metstmt.setString(3, idMainElt);
            // Ex�cution de la requete.
            rs = metstmt.executeQuery();
            Map<String, Critere> currentElement = null;
            while (rs.next()) {
                // Parcours du resultat.
                String idCrit = rs.getString("id_crit");
                String idElt = rs.getString("id_elt");
                currentElement = result.get(idElt);
                if (currentElement == null) {
                    currentElement = new HashMap<String, Critere>();
                    result.put(idElt, currentElement);
                }
                // Stockage des criteres resultats dans la table de hachage
                // Avec comme cle l'identifiant du critere.
                currentElement.put(idCrit, new Critere(idCrit, rs.getDouble("note_cribl"), 1, null));
            }
        } catch (SQLException e) {
            logger.error("Error during criterion retrieve", e);
            throw new DataAccessException("Error during criterion retrieve", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(metstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String FACTOR_RETRIEVE_BY_MODEL_QUERY =
            "Select distinct id_fact From Facteur_Critere"
            + " Where id_usa = ?";

    public Collection<String> retrieveFactors(String idUsa) throws DataAccessException {
        Collection<String> result = new ArrayList<String>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement facstmt = null;
        ResultSet rs = null;
        // Initialisation des facteurs en fonctions des notes forcees prealablement.
        try {
            // Preparation de la requete.
            facstmt = connection.prepareStatement(FACTOR_RETRIEVE_BY_MODEL_QUERY);
            facstmt.setString(1, idUsa);
            // Execution de la requete.
            rs = facstmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                result.add(rs.getString("id_fact"));
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(facstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String METRIC_EXISTS_FOR_BASELINE_QUERY =
            "Select count(*) From Qametrique Where id_bline = ?";

    public boolean metricExistsForBaseline(BaselineBean baseline)
            throws DataAccessException {
        boolean result = false;
        if (baseline != null && baseline.getId() != null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            // Initialisation des facteurs en fonctions des notes forcees prealablement.
            try {
                // Preparation de la requete.
                pstmt = connection.prepareStatement(METRIC_EXISTS_FOR_BASELINE_QUERY);
                pstmt.setString(1, baseline.getId());
                // Execution de la requete.
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                logger.error("Error checking metrics", e);
                throw new DataAccessException("Error checking metrics", e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    private static final String DELETE_MODULE_BASELINE_ASSOC_QUERY =
            "Delete From Baseline_links Where parent_id_bline = ? And child_id_elt IN @INCLAUSE@";

    public boolean deleteModuleBaselineAssoc(BaselineBean baselineBean, String[] eaArray)
            throws DataAccessException {
        boolean result = false;
        if (baselineBean != null && eaArray != null && eaArray.length > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            QueryUtil queryUtil = QueryUtil.getInstance();
            try {
                String query = queryUtil.replaceInClause(DELETE_MODULE_BASELINE_ASSOC_QUERY, eaArray);
                // Preparation de la requete.
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, baselineBean.getId());
                // Execution de la requete.
                pstmt.executeUpdate();
                JdbcDAOUtils.commit(connection);
            } catch (SQLException e) {
                JdbcDAOUtils.rollbackConnection(connection);
                logger.error("Error deleting module/baseline assoc", e);
                throw new DataAccessException("Error deleting module/baseline assoc", e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    private static final String NOT_IN_CALCULATION_ELEMENT_QUERY =
            "Select elt.id_elt, max(b.id_bline)"
            + " From Element elt, Qametrique qa, Baseline b"
            + " Where elt.id_telt='EA'"
            + " And elt.id_pro = ?"
            + " And elt.id_elt NOT IN @INCLAUSE@"
            + " And (dperemption is null Or dperemption > ?)"
            + " And qa.id_elt = elt.id_elt"
            + " And qa.id_bline = b.id_bline"
            + " And b.pro_blinre = elt.id_pro"
            + " And b.dmaj_bline is not null"
            + " And (b.dmaj_bline < ? Or b.id_bline = ?)"
            + " And (b.lib_bline <> 'BaseLine d''Instanciation' OR b.lib_bline IS NULL)"
            + " Group by elt.id_elt";

    public List<ModuleBaselineAssoc> retrieveModuleBaselineAssocOutsideCalculation(
            String projectId, BaselineBean baselineBean, String[] eaArray) throws DataAccessException {
        List<ModuleBaselineAssoc> result = new ArrayList<ModuleBaselineAssoc>();
        if (baselineBean != null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            QueryUtil queryUtil = QueryUtil.getInstance();
            // Initialisation des facteurs en fonctions des notes forcees prealablement.
            try {
                String query = queryUtil.replaceInClause(NOT_IN_CALCULATION_ELEMENT_QUERY, eaArray);
                // Preparation de la requete.
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, projectId);
                pstmt.setTimestamp(2, baselineBean.getDmaj());
                pstmt.setTimestamp(3, baselineBean.getDmaj());
                pstmt.setString(4, baselineBean.getId());
                // Execution de la requete.
                rs = pstmt.executeQuery();
                ModuleBaselineAssoc assoc = null;
                ElementBean currentElement = null;
                BaselineBean currentBaseline = null;
                while (rs.next()) {
                    // Parcours du resultat.
                    currentElement = new ElementBean();
                    currentElement.setId(rs.getString("id_elt"));
                    currentBaseline = new BaselineBean();
                    currentBaseline.setId(rs.getString(2));
                    assoc = new ModuleBaselineAssoc(currentElement, currentBaseline);
                    result.add(assoc);
                }
            } catch (SQLException e) {
                logger.error("Error retrieving module/baseline assoc", e);
                throw new DataAccessException("Error retrieving module/baseline assoc", e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    private static final String SELECT_MODULE_BASELINE_ASSOC_QUERY =
            "Select child_id_elt From Baseline_links"
            + " Where parent_id_bline = ?"
            + " And child_id_bline = ?"
            + " And child_id_elt = ?";
    private static final String INSERT_MODULE_BASELINE_ASSOC_QUERY =
            "Insert Into Baseline_links (parent_id_bline, child_id_bline, child_id_elt)"
            + " Values (?, ?, ?)";

    public void createModuleBaselineAssoc(
            String projectId, BaselineBean baselineBean, String[] eaArray) throws DataAccessException {
        deleteModuleBaselineAssoc(baselineBean, eaArray);
        List<ModuleBaselineAssoc> moduleBaselineAssoc = retrieveModuleBaselineAssocOutsideCalculation(projectId, baselineBean, eaArray);
        if (moduleBaselineAssoc != null && moduleBaselineAssoc.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmtSelect = null;
            PreparedStatement pstmtInsert = null;
            ResultSet rs = null;
            try {
                connection.setAutoCommit(false);
                pstmtSelect = connection.prepareStatement(SELECT_MODULE_BASELINE_ASSOC_QUERY);
                pstmtInsert = connection.prepareStatement(INSERT_MODULE_BASELINE_ASSOC_QUERY);

                Iterator<ModuleBaselineAssoc> assocIter = moduleBaselineAssoc.iterator();
                ModuleBaselineAssoc currentAssoc = null;
                while (assocIter.hasNext()) {
                    currentAssoc = assocIter.next();
                    pstmtSelect.setString(1, baselineBean.getId());
                    pstmtSelect.setString(2, currentAssoc.getBaseline().getId());
                    pstmtSelect.setString(3, currentAssoc.getElement().getId());
                    rs = pstmtSelect.executeQuery();
                    if (!rs.next()) {
                        // Insertion des donnees dans la base.
                        // Initialisation des parametres de la requete.
                        pstmtInsert.setString(1, baselineBean.getId());
                        pstmtInsert.setString(2, currentAssoc.getBaseline().getId());
                        pstmtInsert.setString(3, currentAssoc.getElement().getId());
                        // Execution de la requete d'insertion.
                        pstmtInsert.addBatch();
                    }
                    JdbcDAOUtils.closeResultSet(rs);
                }
                pstmtInsert.executeBatch();
                JdbcDAOUtils.commit(connection);
            } catch (SQLException e) {
                JdbcDAOUtils.rollbackConnection(connection);
                logger.error("Error updating module/baseline assoc", e);
                throw new DataAccessException(e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmtSelect);
                JdbcDAOUtils.closePrepareStatement(pstmtInsert);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    private static final String MODULE_BASELINE_ASSOC_QUERY =
            "Select child_id_bline, child_id_elt"
            + " From Baseline_links"
            + " Where parent_id_bline = ?";

    public Map<String, String> retrieveModuleBaselineMap(
            String baselineId) throws DataAccessException {
        Map<String, String> result = new HashMap<String, String>();
        if (baselineId != null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            // Initialisation des facteurs en fonctions des notes forcees prealablement.
            try {
                // Preparation de la requete.
                pstmt = connection.prepareStatement(MODULE_BASELINE_ASSOC_QUERY);
                pstmt.setString(1, baselineId);
                // Execution de la requete.
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result.put(rs.getString("child_id_elt"), rs.getString("child_id_bline"));
                }
            } catch (SQLException e) {
                logger.error("Error retrieving module/baseline map", e);
                throw new DataAccessException("Error retrieving module/baseline map", e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /** Recupere les justificatifs de critere valides existant pour l'element, la baseline donnee ou la precedente.
     * @param idMainElt l'identifiant du module.
     * @param bline la baseline en cours d'analyse.
     * @return la table des justificatifs existants.
     */
    public Map<String, Map<String, Critere>> getCritereJustificatifs(String idMainElt, Baseline bline) throws DataAccessException {
        Map<String, Map<String, Critere>> result = null;
        // Recuperation des justifications existants pour la baseline en cours.
        result = getCritereJustificatifsImpl(idMainElt, bline);
        // Si aucun justificatif trouve pour la baseline en cours,
        // On recherche dans la baseline precedente.
        if (result == null || result.isEmpty()) {
            if (bline.getPreviousBaseline() != null) {
                result = getCritereJustificatifsImpl(idMainElt, bline.getPreviousBaseline());
            }
        }
        return result;
    }
    private static final String CRITERION_QUERY = "Select elt.id_elt, crit.id_crit, crit.note_cribl, crit.id_just, crit.just_note_cribl"
            + " From Critere_bline crit, Justification just, Element elt"
            + " Where crit.id_elt=elt.id_elt"
            + " And (elt.id_main_elt = ? Or elt.id_elt = ?)"
            + " And crit.id_bline = ?"
            + " And crit.id_just is not null"
            + " And crit.id_just = just.id_just"
            + " And just.statut_just <> 'REJET'";

    /** Recupere les justificatifs de criteres valides existant pour l'element, et la baseline donnee.
     * @param connection la connexion DB utilisee.
     * @param idMainElt l'identifiant du module.
     * @param bline la baseline en cours d'analyse.
     * @return la table des justificatifs existants.
     */
    private Map<String, Map<String, Critere>> getCritereJustificatifsImpl(String idMainElt, Baseline bline) throws DataAccessException {
        Map<String, Map<String, Critere>> result = new HashMap<String, Map<String, Critere>>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(CRITERION_QUERY);
            // Initialisation des parametres.
            stmt.setString(1, idMainElt);
            stmt.setString(2, idMainElt);
            stmt.setString(3, bline.getId());
            // Execution de la requete.
            rs = stmt.executeQuery();
            Map<String, Critere> currentEltCrit = null;
            while (rs.next()) {
                // Parcours du resultat et creation d'un critere justifie.
                String idElt = rs.getString("id_elt");
                String idCrit = rs.getString("id_crit");
                Critere eltJust = new Critere(idCrit, rs.getString(4), rs.getDouble(5));

                currentEltCrit = result.get(idElt);
                if (currentEltCrit == null) {
                    currentEltCrit = new HashMap<String, Critere>();
                    result.put(idElt, currentEltCrit);
                }

                // Ajout du nouveau justificatif a la table.
                currentEltCrit.put(idCrit, eltJust);
            }
        } catch (SQLException e) {
            // Erreur lors de l'execution de la requete.
            logger.error("Error during criterion justification retrieve", e);
            // Purge de la table resultat.
            result.clear();
            result = null;
            throw new DataAccessException("Error during criterion justification retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private static final String CLEAN_FACTEUR_BLINE_BY_MODEL =
            "DELETE FROM facteur_bline "
            + " WHERE id_bline = ? AND id_fac NOT IN "
            + " (SELECT DISTINCT id_fact FROM facteur_critere WHERE id_usa = ?)"
            + " AND id_elt IN (SELECT id_elt FROM element WHERE (id_main_elt = ? OR id_elt = ?))";

    private static final String CLEAN_CRITERE_BLINE_BY_MODEL =
            "DELETE FROM critere_bline "
            + " WHERE id_bline = ? AND id_crit NOT IN "
            + " (SELECT DISTINCT id_crit FROM critere_usage WHERE id_usa = ?)"
            + " AND id_elt IN (SELECT id_elt FROM element WHERE (id_main_elt = ? OR id_elt = ?))";

    private static final String CLEAN_CRITERE_NOTE_REPARTITION_BY_MODEL =
            "DELETE FROM criterenoterepartition "
            + " WHERE id_bline = ? AND id_crit NOT IN "
            + " (SELECT DISTINCT id_crit FROM critere_usage WHERE id_usa = ?)"
            + " AND id_elt IN (SELECT id_elt FROM element WHERE (id_main_elt = ? OR id_elt = ?))";

    private static final String CLEAN_POIDS_FACT_CRIT_BY_MODEL =
            "DELETE FROM poids_fact_crit "
            + " WHERE bline_poids = ?"
            + " AND id_crit NOT IN (SELECT id_crit FROM facteur_critere where id_usa = ?)"
            + " AND id_fact NOT IN (SELECT id_fact FROM facteur_critere where id_usa = ?)"
            + " AND id_elt IN (SELECT id_elt FROM element WHERE (id_main_elt = ? OR id_elt = ?))";

    /**
     * @{@inheritDoc }
     */
    public void removeResultsNotInQualityModelAnymore(String idBline, String idUsa, String idEa) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement stmt = null;

        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(CLEAN_CRITERE_NOTE_REPARTITION_BY_MODEL);
            // Initialisation des parametres.
            stmt.setString(1, idBline);
            stmt.setString(2, idUsa);
            stmt.setString(3, idEa);
            stmt.setString(4, idEa);
            // Execution de la requete.
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Erreur lors de l'execution de la requete.
            logger.error("Error during baseline's scores cleaning", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }

        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(CLEAN_CRITERE_BLINE_BY_MODEL);
            // Initialisation des parametres.
            stmt.setString(1, idBline);
            stmt.setString(2, idUsa);
            stmt.setString(3, idEa);
            stmt.setString(4, idEa);
            // Execution de la requete.
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Erreur lors de l'execution de la requete.
            logger.error("Error during baseline's scores cleaning", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }

        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(CLEAN_POIDS_FACT_CRIT_BY_MODEL);
            // Initialisation des parametres.
            stmt.setString(1, idBline);
            stmt.setString(2, idUsa);
            stmt.setString(3, idUsa);
            stmt.setString(4, idEa);
            stmt.setString(5, idEa);
            // Execution de la requete.
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Erreur lors de l'execution de la requete.
            logger.error("Error during baseline's scores cleaning", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }

        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(CLEAN_FACTEUR_BLINE_BY_MODEL);
            // Initialisation des parametres.
            stmt.setString(1, idBline);
            stmt.setString(2, idUsa);
            stmt.setString(3, idEa);
            stmt.setString(4, idEa);
            // Execution de la requete.
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Erreur lors de l'execution de la requete.
            logger.error("Error during baseline's scores cleaning", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
}

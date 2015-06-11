/*
 * CriterionDbmsDao.java
 *
 * Created on 23 janvier 2004, 12:23
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.CriterionNoteRepartition;
import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.domain.dataschemas.CriterionRepartitionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorDefinitionBean;
import com.compuware.caqs.domain.dataschemas.FactorRepartitionBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.RepartitionBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorCriterionBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 */
public class CriterionDbmsDao implements CriterionDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String CRITERION_BY_ID_REQUEST = "SELECT id_crit, dinst_crit, dapplication_crit, dperemption_crit, dmaj_crit FROM Critere WHERE id_crit=?";
    private static final String CRITERION_BY_USAGE_REQUEST =
            "SELECT cu.id_crit, cu.id_telt" + " FROM Critere_Usage cu"
            + " WHERE cu.id_usa=?";
    private static final String CRITERION_BY_CRIT_REQUEST =
            "SELECT cu.id_crit, cu.id_telt" + " FROM Critere_Usage cu"
            + " WHERE cu.id_usa=? AND cu.id_crit=?";
    private static final String CRITERION_BY_ID_FACT_REQUEST =
            "SELECT cu.id_crit, fc.poids, cu.id_telt"
            + " FROM Facteur_Critere fc, Critere_Usage cu"
            + " WHERE fc.id_fact=?" + " AND fc.id_usa=?"
            + " AND cu.id_crit=fc.id_crit" + " AND cu.id_usa=fc.id_usa";
    private static final String CRITERION_SUMMARY =
            "Select distinct elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, crit.note_cribl, crit.tendance, p.valeur_poids, crit.id_crit"
            + " From Element elt, Critere_bline crit, Poids_fact_crit p"
            + " Where crit.id_bline = ?" + " And p.bline_poids=crit.id_bline"
            + " And crit.id_elt = elt.id_elt" + " And p.id_elt = elt.id_elt"
            + " And p.id_elt = crit.id_elt" + " And p.id_crit=crit.id_crit"
            + " And p.id_fact = ?" + " And p.id_pro=crit.id_pro"
            + " And p.id_pro=elt.id_pro" + " And crit.id_pro=elt.id_pro"
            + " And elt.id_elt = ?";
    private static final String CRITERION_BOTTOM_UP_SUMMARY =
            "Select distinct elt.id_elt, elt.id_telt, telt.has_source, elt.lib_elt, elt.desc_elt, ecr.note1, ecr.note2, ecr.note3, ecr.note4"
            + " From Element elt, ELEMENT_BASELINE_INFO ecr, Type_Element telt"
            + " Where ecr.id_elt = elt.id_elt" + " And ecr.id_bline = ?"
            + " And (ecr.note1 > 0 or ecr.note2 > 0)"
            + " And ecr.id_main_elt = ?"
            + " And ecr.id_main_elt=elt.id_main_elt"
            + " And elt.desc_elt like ?" + " And elt.id_telt = telt.id_telt";
    private static final String CRITERION_BOTTOM_UP_DETAIL =
            "Select elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.note_cribl, cb.just_note_cribl, cb.id_just, cb.tendance"
            + " From Critere_bline cb, Element elt" + " Where cb.id_elt=?"
            + " And cb.id_elt=elt.id_elt" + " And cb.id_bline=?"
            + " And cb.id_pro=?" + " And elt.id_pro=cb.id_pro"
            + " order by cb.id_crit";
    private static final String CRITERION_REPARTITION_BY_FACTOR =
            "Select count(cb.id_crit), fc.id_fact"
            + " From Critere_bline cb, Facteur_critere fc, Element elt"
            + " Where elt.id_main_elt = ?" + " AND cb.id_pro = ?"
            + " AND cb.id_bline = ?" + " AND cb.note_cribl < 3"
            + " AND cb.note_cribl > 0" + " AND fc.id_crit = cb.id_crit"
            + " AND fc.id_usa = ?" + " AND elt.id_elt = cb.id_elt"
            + " AND elt.id_pro = cb.id_pro" + " AND elt.desc_elt like ?"
            + " AND elt.id_telt like ?" + " group by fc.id_fact";
    private static final String CRITERION_REPARTITION_BY_CRITERION =
            "Select count(cb.id_crit), cb.id_crit"
            + " From Critere_bline cb, Element elt"
            + " Where elt.id_main_elt = ?" + " AND cb.id_pro = ?"
            + " AND cb.id_bline = ?" + " AND cb.note_cribl < 3"
            + " AND cb.note_cribl > 0" + " AND cb.id_elt = elt.id_elt"
            + " AND elt.id_pro = cb.id_pro" + " AND elt.desc_elt like ?"
            + " AND elt.id_telt like ?" + " group by cb.id_crit";
    private static final String CREATE_CRITERION_QUERY = "INSERT INTO CRITERE (ID_CRIT, dapplication_crit, dinst_crit, dmaj_crit, dperemption_crit) "
            + " VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_CRITERION_DATE_QUERY = "UPDATE CRITERE SET dapplication_crit=?, dinst_crit=?, dmaj_crit=?, dperemption_crit=? "
            + " WHERE id_crit = ?";
    private static final String DELETE_CRITERION_QUERY = "DELETE FROM CRITERE "
            + " WHERE ID_CRIT = ?";
    private static final String RETRIEVE_NUMBER_OF_ASSOCIATED_MODELS = "SELECT "
            + " COUNT(ID_USA) AS nb FROM CRITERE_USAGE WHERE ID_CRIT = ?";
    private static final String UPDATE_CRITERION_WEIGHT_FOR_GOAL =
            "UPDATE FACTEUR_CRITERE SET poids = ? "
            + " WHERE ID_CRIT = ? AND ID_FACT = ? AND ID_USA = ?";
    private static final String UPDATE_CRITERION_TELT_FOR_MODEL =
            "UPDATE CRITERE_USAGE SET id_telt = ? "
            + " WHERE ID_CRIT = ? AND ID_USA = ?";
    private static final String SELECT_FACTEUR_CRITERE =
            "SELECT count(id_crit) as nb FROM facteur_critere "
            + " WHERE id_usa=? AND id_crit=?";
    private static final String SELECT_CRITERE_USAGE =
            "SELECT count(id_crit) as nb FROM critere_usage "
            + " WHERE id_usa=? AND id_crit=?";
    private static final String INSERT_CRITERE_USAGE =
            "INSERT INTO critere_usage(id_usa, id_crit, id_telt) VALUES(?,?,?)";
    private static final String INSERT_FACTEUR_CRITERE =
            "INSERT INTO facteur_critere(id_usa, id_fact, id_crit, poids) VALUES(?,?,?,?)";
    private static final String DELETE_CRITERE_USAGE =
            "DELETE FROM critere_usage WHERE id_usa=? AND id_crit=?";
    private static final String DELETE_FACTEUR_CRITERE =
            "DELETE FROM facteur_critere WHERE id_usa=? AND id_fact=? AND id_crit=?";
    private static final String RETRIEVE_ASSOCIATED_MODELS_GOALS_FOR_CRITERION = "SELECT id_usa, id_fact FROM facteur_critere WHERE id_crit = ?";
    private static final String REPARTITION_SELECT_FROM_SUBQUERY =
            "Select cb.id_crit, count(*)"
            + " From Critere_bline cb, ELEMENT_BASELINE_INFO ebi";
    private static final String REPARTITION_FILTER_FROM_SUBQUERY =
            ", Element e";
    private static final String REPARTITION_FILTER_JOIN_SUBQUERY =
            " And cb.id_elt = e.id_elt" + " And ebi.id_elt = e.id_elt"
            + " And ebi.id_main_elt = e.id_main_elt";
    private static final String REPARTITION_WHERE_SUBQUERY =
            " Where ebi.id_bline = ?" + " And ebi.id_main_elt = ?"
            + " And cb.id_elt = ebi.id_elt" + " And cb.id_bline = ebi.id_bline";
    private static final String REPARTITION_GROUP_BY_SUBQUERY =
            " Group by cb.id_crit";
    /* Nouveau et Mauvais */
    private static final String REPARTITION_CRIT_NEW_AND_BAD_QUERY =
            " And tendance IS NULL" + " And note_cribl < 3"
            + " And note_cribl > 0";

    /* Ancien degrade */
    private static final String REPARTITION_CRIT_OLD_AND_WORST_QUERY =
            " And tendance IS NOT NULL" + " AND note_cribl - tendance < 0"
            + " AND nbbetter = 0";

    /* Ancien ameliore */
    private static final String REPARTITION_CRIT_OLD_AND_BETTER_QUERY =
            " And tendance IS NOT NULL" + " AND note_cribl - tendance > 0"
            + " AND nbworst = 0";
    /* Ancien ameliore et degrade */
    private static final String REPARTITION_CRIT_OLD_BETTER_AND_WORST_QUERY =
            " And tendance IS NOT NULL" + " AND note_cribl - tendance < 0"
            + " AND nbworst > 0" + " AND nbbetter > 0";
    /* Ancien mauvais et stable */
    private static final String REPARTITION_CRIT_BAD_AND_STABLE_QUERY =
            " And tendance IS NOT NULL" + " And note_cribl < 3"
            + " And note_cribl > 0" + " AND nbworst = 0" + " AND nbbetter = 0";
    
    private static CriterionDao singleton = new CriterionDbmsDao();

    public static CriterionDao getInstance() {
        return CriterionDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private CriterionDbmsDao() {
    }

    /** {@inheritDoc}
     */
    public CriterionDefinition retrieveCriterionDefinitionByKey(String idCrit) {
        CriterionDefinition result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BY_ID_REQUEST);
            pstmt.setString(1, idCrit);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new CriterionDefinition();
                result.setId(rs.getString(1));
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

    /** {@inheritDoc}
     */
    public List<CriterionDefinition> retrieveCriterionDefinitionByUsage(String idUsa) {
        List<CriterionDefinition> result = new ArrayList<CriterionDefinition>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        MetriqueDao metriqueFacade = MetriqueDbmsDao.getInstance();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BY_USAGE_REQUEST);
            pstmt.setString(1, idUsa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CriterionDefinition criterion = new CriterionDefinition();
                criterion.setId(rs.getString(1));
                criterion.setIdTElt(rs.getString(2));
                List<MetriqueDefinitionBean> metDefs = metriqueFacade.retrieveMetriqueDefinitionByIdCrit(criterion.getId(), idUsa);
                criterion.setMetriquesDefinitions(metDefs);
                result.add(criterion);
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

    /** {@inheritDoc}
     */
    public CriterionDefinition retrieveCriterionDefinitionByKey(String idCrit, String idUsa) {
        CriterionDefinition result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BY_CRIT_REQUEST);
            pstmt.setString(1, idUsa);
            pstmt.setString(2, idCrit);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new CriterionDefinition();
                result.setId(rs.getString(1));
                result.setIdTElt(rs.getString(2));
                MetriqueDao metriqueFacade = MetriqueDbmsDao.getInstance();
                List<MetriqueDefinitionBean> metDefs = metriqueFacade.retrieveMetriqueDefinitionByIdCrit(result.getId(), idUsa);
                result.setMetriquesDefinitions(metDefs);
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

    /** {@inheritDoc}
     */
    public Collection<CriterionDefinition> retrieveCriterionDefinitionByFactor(String idFact, String idUsa) {
        Collection<CriterionDefinition> result = new ArrayList<CriterionDefinition>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        MetriqueDao metriqueFacade = MetriqueDbmsDao.getInstance();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BY_ID_FACT_REQUEST);
            pstmt.setString(1, idFact);
            pstmt.setString(2, idUsa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CriterionDefinition criterion = new CriterionDefinition();
                criterion.setId(rs.getString(1));
                criterion.setWeight(rs.getDouble(2));
                criterion.setIdTElt(rs.getString(3));
                List<MetriqueDefinitionBean> metDefs = metriqueFacade.retrieveMetriqueDefinitionByIdCrit(criterion.getId(), idUsa);
                criterion.setMetriquesDefinitions(metDefs);
                result.add(criterion);
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

    /** {@inheritDoc}
     */
    public List<CriterionDefinition> retrieveCriterionDefinitionByGoalAndModel(String idFact, String idUsa) {
        List<CriterionDefinition> result = new ArrayList<CriterionDefinition>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BY_ID_FACT_REQUEST);
            pstmt.setString(1, idFact);
            pstmt.setString(2, idUsa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CriterionDefinition criterion = new CriterionDefinition();
                criterion.setId(rs.getString(1));
                criterion.setWeight(rs.getDouble(2));
                criterion.setIdTElt(rs.getString(3));
                result.add(criterion);
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
    private static final String ALL_CRITERION_REQUEST = "SELECT id_crit FROM Critere ORDER BY id_crit";

    /** {@inheritDoc}
     */
    public Collection<CriterionDefinition> retrieveAllCriterions() {
        Collection<CriterionDefinition> result = new ArrayList<CriterionDefinition>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_CRITERION_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CriterionDefinition criterion = new CriterionDefinition();
                criterion.setId(rs.getString(1));
                result.add(criterion);
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
    private static final String CRIT_MET_SUBQUERY = " And qa.id_met ";
    private static final String SUB_CRIT_MET_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, elt.id_stereotype, qa.id_met, qa.valbrute_qamet, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, qa.lignes, t.has_source"
            + " From Element elt, Critere_bline crit"
            + " left outer join Qametrique qa"
            + " on qa.id_bline = crit.id_bline And qa.id_elt = crit.id_elt And qa.id_met = ?, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And elt.id_main_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?"
            + " And elt.id_telt = t.id_telt";
    private static final String SUB_CRIT_MET_WITH_FILTER_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, elt.id_stereotype, qa.id_met, qa.valbrute_qamet, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, qa.lignes, t.has_source"
            + " From Element elt, Critere_bline crit"
            + " left outer join Qametrique qa"
            + " on qa.id_bline = crit.id_bline And qa.id_elt = crit.id_elt And qa.id_met = ?, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And elt.id_main_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?"
            + " And elt.id_telt = t.id_telt";
    private static final String SUB_CRIT_NOMET_REQUEST =
            " Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, elt.id_stereotype, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, t.has_source"
            + " From Element elt, Critere_bline crit, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And elt.id_main_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?"
            + " And elt.id_telt = t.id_telt";
    private static final String EA_CRIT_NOMET_REQUEST =
            " Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, elt.id_stereotype, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, t.has_source"
            + " From Element elt, Critere_bline crit, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And elt.id_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?";
    private static final String EA_CRIT_MET_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, elt.id_stereotype, qa.id_met, qa.valbrute_qamet, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, qa.lignes, t.has_source"
            + " From Element elt, Critere_bline crit"
            + " left outer join Qametrique qa"
            + " on qa.id_bline = crit.id_bline And qa.id_elt = crit.id_elt And qa.id_met = ?, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And elt.id_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?"
            + " And elt.id_telt = t.id_telt";

    /** {@inheritDoc}
     */
    public List<CriterionBean> retrieveCriterionDetailsForElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt) {
        List<CriterionBean> result = (List<CriterionBean>) dataCache.loadFromCache("retrieveCriterionDetailsForEltsMAIN"
                + eltBean.getBaseline().getId() + eltBean.getId()
                + criterionDef.getId() + eltBean.getUsage().getId() + filter
                + typeElt + noteSeuil);
        if (result == null) {
            List<MetriqueDefinitionBean> metList = criterionDef.getMetriquesDefinitions();
            if ((metList != null) && (metList.size() == 1)) {
                result = retrieveCriterionDetailsForElts(EA_CRIT_MET_REQUEST, eltBean, criterionDef, noteSeuil, filter, typeElt);
            } else {
                Map<String, Map<String, MetriqueBean>> metriqueMap = null;
                if (metList.size() > 0) {
                    MetriqueDao metriqueFacade = MetriqueDbmsDao.getInstance();
                    metriqueMap = metriqueFacade.retrieveQametriqueMapFromMetListBline(eltBean.getBaseline().getId(), getMetCritSubQuery(metList));
                }
                result = retrieveCriterionDetailsMetForElts(EA_CRIT_NOMET_REQUEST, eltBean, criterionDef, noteSeuil, filter, typeElt, metriqueMap);
            }
            dataCache.storeToCache(eltBean.getBaseline().getId(), "retrieveCriterionDetailsForEltsMAIN"
                    + eltBean.getBaseline().getId() + eltBean.getId()
                    + criterionDef.getId() + eltBean.getUsage().getId() + filter
                    + typeElt + noteSeuil, result);
        }
        return result;
    }

    private static String appendDateAndOrder(ElementBean eltBean, String request) {
        return request + " order by elt.lib_elt";
    }

    /** {@inheritDoc}
     */
    public List<CriterionBean> retrieveCriterionDetailsForSubElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt) {
        List<CriterionBean> result = (List<CriterionBean>) dataCache.loadFromCache("retrieveCriterionDetailsForSubElts"
                + eltBean.getBaseline().getId() + eltBean.getId()
                + criterionDef.getId() + eltBean.getUsage().getId() + filter
                + typeElt + noteSeuil);
        if (result == null) {
            List<MetriqueDefinitionBean> metList = criterionDef.getMetriquesDefinitions();
            if ((metList != null) && (metList.size() == 1)) {
                String request = SUB_CRIT_MET_REQUEST;
                if (!typeElt.equals("ALL") && !typeElt.equals(ElementType.MET)) {
                    request = SUB_CRIT_MET_WITH_FILTER_REQUEST;
                } else if (!filter.equals("%") && filter.length() > 5) {
                    request = SUB_CRIT_MET_WITH_FILTER_REQUEST;
                }
                result = retrieveCriterionDetailsForElts(request, eltBean, criterionDef, noteSeuil, filter, typeElt);
            } else {
                Map<String, Map<String, MetriqueBean>> metriqueMap = null;
                if (metList.size() > 0) {
                    MetriqueDao metriqueFacade = MetriqueDbmsDao.getInstance();
                    metriqueMap = metriqueFacade.retrieveQametriqueMapFromMetListBline(eltBean.getBaseline().getId(), getMetCritSubQuery(metList));
                }
                result = retrieveCriterionDetailsMetForElts(SUB_CRIT_NOMET_REQUEST, eltBean, criterionDef, noteSeuil, filter, typeElt, metriqueMap);
            }
            dataCache.storeToCache(eltBean.getBaseline().getId(), "retrieveCriterionDetailsForSubElts"
                    + eltBean.getBaseline().getId() + eltBean.getId()
                    + criterionDef.getId() + eltBean.getUsage().getId() + filter
                    + typeElt + noteSeuil, result);
        }
        return result;
    }

    private String getMetCritSubQuery(List<MetriqueDefinitionBean> metList) {
        StringBuffer result = new StringBuffer(CRIT_MET_SUBQUERY);
        Iterator<MetriqueDefinitionBean> i = metList.iterator();
        MetriqueDefinitionBean metBean = null;
        if (metList.size() > 1) {
            int cnt = 0;
            result.append(" in (");
            while (i.hasNext()) {
                metBean = (MetriqueDefinitionBean) i.next();
                if (cnt > 0) {
                    result.append(",");
                }
                result.append("'");
                result.append(metBean.getId());
                result.append("'");
                cnt++;
            }
            result.append(")");
        }
        return result.toString();
    }

    private List<CriterionBean> retrieveCriterionDetailsForElts(
            String query, ElementBean eltBean,
            CriterionDefinition criterionDef, double noteSeuil,
            String filter, String typeElt) {
        List<CriterionBean> result = (List<CriterionBean>) dataCache.loadFromCache("retrieveCriterionDetailsForElts"
                + eltBean.getBaseline().getId() + eltBean.getId()
                + criterionDef.getId() + eltBean.getUsage().getId() + filter
                + typeElt + noteSeuil);
        if (result == null) {
            result = new ArrayList<CriterionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            BaselineBean baseline = eltBean.getBaseline();
            String tmpTypeElt = typeElt;
            if (tmpTypeElt.equals("ALL")) {
                tmpTypeElt = "%";
            }
            String lastElement = "";
            CriterionBean criterion = null;
            Map<String, MetriqueBean> metriques = new HashMap<String, MetriqueBean>();
            List<MetriqueDefinitionBean> metList = criterionDef.getMetriquesDefinitions();
            MetriqueDefinitionBean metBean = (MetriqueDefinitionBean) metList.iterator().next();
            String request = appendDateAndOrder(eltBean, query);
            try {
                logger.debug(request);
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, metBean.getId());
                pstmt.setString(2, baseline.getId());
                pstmt.setString(3, criterionDef.getId());
                pstmt.setDouble(4, noteSeuil);
                pstmt.setString(5, eltBean.getId());
                pstmt.setString(6, filter);
                pstmt.setString(7, tmpTypeElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String actualElement = rs.getString(1);
                    if (!actualElement.equals(lastElement)) {
                        if (criterion != null) {
                            criterion.setMetriques(metriques);
                            result.add(criterion);
                            metriques = new HashMap<String, MetriqueBean>();
                        }

                        criterion = new CriterionBean();
                        criterion.setCriterionDefinition(criterionDef);
                        criterion.setNote(rs.getDouble("note_cribl"));
                        criterion.setTendance(getDouble(rs, "tendance"));
                        criterion.setJustNote(rs.getDouble("just_note_cribl"));
                        String idJust = rs.getString("id_just");
                        if (idJust != null) {
                            criterion.setJustificatif(new JustificatifDbmsDao().retrieveJustificatifById(idJust, connection));
                            criterion.getJustificatif().setNote(criterion.getJustNote());
                        }
                        ElementBean elementBean = new ElementBean();
                        elementBean.setId(rs.getString("id_elt"));
                        elementBean.setTypeElt(rs.getString("id_telt"));
                        elementBean.setLib(rs.getString("lib_elt"));
                        elementBean.setDesc(rs.getString("desc_elt"));
                        elementBean.setStereotype(rs.getString("id_stereotype"));
                        elementBean.setHasSource(rs.getBoolean("has_source"));
                        criterion.setElement(elementBean);
                    }
                    MetriqueBean met = new MetriqueBean();
                    met.setId(rs.getString("id_met"));
                    if (met.getId() != null) {
                        met.setValbrute(rs.getDouble("valbrute_qamet"));
                        String lines = rs.getString("lignes");
                        if (lines != null && lines.length() > 0) {
                            met.setLines(lines.split(","));
                        }
                        metriques.put(met.getId(), met);
                    }
                    lastElement = actualElement;
                }
                if (criterion != null) {
                    criterion.setMetriques(metriques);
                    result.add(criterion);
                }
                dataCache.storeToCache(eltBean.getBaseline().getId(), "retrieveCriterionDetailsForElts"
                        + eltBean.getBaseline().getId() + eltBean.getId()
                        + criterionDef.getId() + eltBean.getUsage().getId()
                        + filter + typeElt + noteSeuil, result);
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    private List<CriterionBean> retrieveCriterionDetailsMetForElts(
            String query, ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt,
            Map<String, Map<String, MetriqueBean>> metMap) {
        List<CriterionBean> result = (List<CriterionBean>) dataCache.loadFromCache("retrieveCriterionDetailsMetForElts"
                + eltBean.getBaseline().getId() + eltBean.getId()
                + criterionDef.getId() + eltBean.getUsage().getId() + filter
                + typeElt + noteSeuil);
        if (result == null) {
            result = new ArrayList<CriterionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            BaselineBean baseline = eltBean.getBaseline();
            String tmpTypeElt = typeElt;
            if (tmpTypeElt.equals("ALL")) {
                tmpTypeElt = "%";
            }
            CriterionBean criterion = null;
            String request = appendDateAndOrder(eltBean, query);
            try {
                logger.debug(request);
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, baseline.getId());
                pstmt.setString(2, criterionDef.getId());
                pstmt.setDouble(3, noteSeuil);
                pstmt.setString(4, eltBean.getId());
                pstmt.setString(5, filter);
                pstmt.setString(6, tmpTypeElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String actualElement = rs.getString("id_elt");
                    criterion = new CriterionBean();
                    criterion.setCriterionDefinition(criterionDef);
                    criterion.setNote(rs.getDouble("note_cribl"));
                    criterion.setTendance(getDouble(rs, "tendance"));
                    criterion.setJustNote(rs.getDouble("just_note_cribl"));
                    String idJust = rs.getString("id_just");
                    if (idJust != null) {
                        criterion.setJustificatif(new JustificatifDbmsDao().retrieveJustificatifById(idJust, connection));
                    }
                    ElementBean elementBean = new ElementBean();
                    elementBean.setId(actualElement);
                    elementBean.setTypeElt(rs.getString("id_telt"));
                    elementBean.setLib(rs.getString("lib_elt"));
                    elementBean.setDesc(rs.getString("desc_elt"));
                    elementBean.setStereotype(rs.getString("id_stereotype"));
                    criterion.setElement(elementBean);
                    elementBean.setHasSource(rs.getBoolean("has_source"));
                    if (metMap != null) {
                        criterion.setMetriques((Map<String, MetriqueBean>) metMap.get(actualElement));
                    }
                    result.add(criterion);
                }
                dataCache.storeToCache(eltBean.getBaseline().getId(), "retrieveCriterionDetailsForElts"
                        + eltBean.getBaseline().getId() + eltBean.getId()
                        + criterionDef.getId() + eltBean.getUsage().getId()
                        + filter + typeElt + noteSeuil, result);
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    /**
     * *********************************************************************************************************************************
     * Acc�s aux crit�res sans les m�triques.
     * **********************************************************************************************************************************
     */
    private static final String SUB_CRIT_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, t.has_source"
            + " From Element elt, Critere_bline crit, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And elt.id_main_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?"
            + " And t.id_telt = elt.id_telt";
    private static final String EA_CRIT_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just, t.has_source"
            + " From Element elt, Critere_bline crit, Type_element t"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?"
            + " And crit.note_cribl < ? And crit.note_cribl > 0"
            + " And crit.id_elt = ?" + " And elt.id_pro = crit.id_pro"
            + " And elt.desc_elt like ?" + " And elt.id_telt like ?"
            + " And t.id_telt = elt.id_telt";

    /** {@inheritDoc}
     */
    public List<CriterionBean> retrieveCriterionDetailsNoMetForElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt) {
        return retrieveCriterionDetailsNoMetForElts(EA_CRIT_REQUEST, eltBean,
                criterionDef, noteSeuil, filter, typeElt);
    }

    /** {@inheritDoc}
     */
    public List<CriterionBean> retrieveCriterionDetailsNoMetForSubElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt) {
        return retrieveCriterionDetailsNoMetForElts(SUB_CRIT_REQUEST, eltBean, criterionDef, noteSeuil, filter, typeElt);
    }

    private List<CriterionBean> retrieveCriterionDetailsNoMetForElts(
            String query, ElementBean eltBean,
            CriterionDefinition criterionDef, double noteSeuil,
            String filter, String typeElt) {
        List<CriterionBean> result = new ArrayList<CriterionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BaselineBean baseline = eltBean.getBaseline();
        logger.info(baseline.getDmaj().toString());

        String tmpTypeElt = typeElt;
        if (tmpTypeElt.equals("ALL")) {
            tmpTypeElt = "%";
        }


        String request = appendDateAndOrder(eltBean, query);
        try {
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, baseline.getId());
            pstmt.setString(2, criterionDef.getId());
            pstmt.setDouble(3, noteSeuil);
            pstmt.setString(4, eltBean.getId());
            pstmt.setString(5, filter);
            pstmt.setString(6, tmpTypeElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CriterionBean criterion = new CriterionBean();
                criterion.setCriterionDefinition(criterionDef);
                criterion.setNote(rs.getDouble("note_cribl"));
                criterion.setTendance(getDouble(rs, "tendance"));
                criterion.setJustNote(rs.getDouble("just_note_cribl"));
                String idJust = rs.getString("id_just");
                if (idJust != null) {
                    criterion.setJustificatif(new JustificatifDbmsDao().retrieveJustificatifById(idJust, connection));
                }
                ElementBean elementBean = new ElementBean();
                elementBean.setId(rs.getString("id_elt"));
                elementBean.setTypeElt(rs.getString("id_telt"));
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setDesc(rs.getString("desc_elt"));
                elementBean.setHasSource(rs.getBoolean("has_source"));
                criterion.setElement(elementBean);
                result.add(criterion);
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

    /** {@inheritDoc}
     */
    public Collection<CriterionBean> retrieveCriterionSummary(
            String idBline, String idFact, String idElt) {
        Collection<CriterionBean> result = (Collection<CriterionBean>) dataCache.loadFromCache("retrieveCriterionSummary"
                + idBline + idElt + idFact);
        if (result == null) {
            result = new ArrayList<CriterionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(CRITERION_SUMMARY);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idFact);
                pstmt.setString(3, idElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    CriterionBean criterion = new CriterionBean();
                    CriterionDefinition criterionDef = retrieveCriterionDefinitionByKey(rs.getString(8));
                    criterion.setCriterionDefinition(criterionDef);
                    criterion.setNote(rs.getDouble(5));
                    criterion.setTendance(getDouble(rs, 6));
                    criterion.setWeight(rs.getDouble(7));
                    ElementBean elementBean = new ElementBean();
                    elementBean.setId(rs.getString(1));
                    elementBean.setTypeElt(rs.getString(2));
                    elementBean.setLib(rs.getString(3));
                    elementBean.setDesc(rs.getString(4));
                    criterion.setElement(elementBean);
                    result.add(criterion);
                }
                dataCache.storeToCache(idBline, "retrieveCriterionSummary"
                        + idBline + idElt + idFact, result);
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();

    private Map<String, Map<String, MetriqueBean>> retrieveAdditionnalMetricMap(String idBline) {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String metList = dynProp.getProperty(Constants.BOTTOMUP_ADDITIONNAL_METRIC_LIST_KEY);

        Map<String, Map<String, MetriqueBean>> result = new HashMap<String, Map<String, MetriqueBean>>();
        if (metList != null && metList.length() > 0) {
            MetriqueDao metricDao = MetriqueDbmsDao.getInstance();
            result = metricDao.retrieveQametriqueFromMetListBline(idBline, metList);
        }

        return result;
    }

    /** {@inheritDoc}
     */
    public List<BottomUpSummaryBean> retrieveCriterionBottomUpSummary(String idBline, String idElt, FilterBean filter) {
        List<BottomUpSummaryBean> result = (List<BottomUpSummaryBean>) dataCache.loadFromCache("retrieveCriterionBottomUpSummary"
                + idBline + idElt + filter.getFilterDesc() + filter.getTypeElt());
        if (result == null) {

            Map<String, Map<String, MetriqueBean>> additionnalMetricMap = retrieveAdditionnalMetricMap(idBline);

            result = new ArrayList<BottomUpSummaryBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            String requestUsingTypeEnd = " And elt.id_telt = ?";
            try {
                if (filter.applyFilterTypeElt()) {
                    pstmt = connection.prepareStatement(CRITERION_BOTTOM_UP_SUMMARY
                            + requestUsingTypeEnd);
                } else {
                    pstmt = connection.prepareStatement(CRITERION_BOTTOM_UP_SUMMARY);
                }
                pstmt.setString(1, idBline);
                pstmt.setString(2, idElt);
                pstmt.setString(3, filter.getFilterDesc());
                if (filter.applyFilterTypeElt()) {
                    pstmt.setString(4, filter.getTypeElt());
                }
                rs = pstmt.executeQuery();
                BottomUpSummaryBean bean = null;
                while (rs.next()) {
                    String actualElement = rs.getString(1);
                    bean = new BottomUpSummaryBean();
                    result.add(bean);
                    ElementBean elementBean = new ElementBean();
                    elementBean.setId(actualElement);
                    elementBean.setTypeElt(rs.getString(2));
                    elementBean.setHasSource(rs.getBoolean(3));
                    elementBean.setLib(rs.getString(4));
                    elementBean.setDesc(rs.getString(5));
                    bean.setElement(elementBean);
                    bean.setNote(rs.getDouble("note1"), 0);
                    bean.setNote(rs.getDouble("note2"), 1);
                    bean.setNote(rs.getDouble("note3"), 2);
                    bean.setNote(rs.getDouble("note4"), 3);
                    bean.setAdditionnalMetricMap(additionnalMetricMap.get(actualElement));
                }
                //Collections.sort(result);
                dataCache.storeToCache(idBline, "retrieveCriterionBottomUpSummary"
                        + idBline + idElt + filter.getFilterDesc()
                        + filter.getTypeElt(), result);
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<BottomUpDetailBean> retrieveCriterionBottomUpDetail(
            String idBline, String idElt, String idPro) {
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
            rs = pstmt.executeQuery();
            BottomUpDetailBean bean = null;
            while (rs.next()) {
                if (bean == null) {
                    bean = new BottomUpDetailBean();
                    ElementBean elementBean = new ElementBean();
                    elementBean.setId(rs.getString(1));
                    elementBean.setLib(rs.getString(2));
                    elementBean.setDesc(rs.getString(3));
                    bean.setElement(elementBean);
                    bean.setCriterions(factCritWeight);
                    result.add(bean);
                }
                String idCrit = rs.getString(4);
                CriterionPerFactorBean crit = bean.lookUp(idCrit);
                if (crit != null) {
                    // Un critere a une valeur non nulle ou n'est pas le seul critere d'un unique facteur.
                    crit.setNote(new Double(rs.getDouble(5)));
                    crit.setJustNote(rs.getDouble(6));
                    String idJust = rs.getString(7);
                    if (idJust != null) {
                        crit.setJustificatif(new JustificatifDbmsDao().retrieveJustificatifById(idJust, connection));
                    }
                    crit.setTendance(getDouble(rs, 8));
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

    private String getCriterionTotalCorrectionsQuery(
            int baseNote, FilterBean filter) {
        StringBuffer query = new StringBuffer("Select Sum(");
        query.append("note").append(baseNote).append(")");
        query.append(" From ELEMENT_BASELINE_INFO ecr");
        if (filter.applyFilter()) {
            query.append(", Element elt");
        }
        query.append(" Where ecr.id_main_elt = ?");
        query.append(" AND ecr.id_bline = ?");
        if (filter.applyFilter()) {
            query.append(" AND elt.id_main_elt=ecr.id_main_elt");
            query.append(" AND elt.id_elt=ecr.id_elt");
            query.append(" AND elt.desc_elt like ?");
            if (filter.applyFilterTypeElt()) {
                query.append(" And elt.id_telt = ?");
            }
        }
        return query.toString();
    }

    /** {@inheritDoc}
     */
    public int retrieveCriterionTotalCorrections(String idBline, String idElt, int baseNote, FilterBean filter) {
        int result = 0;
        Integer cacheResult = (Integer) dataCache.loadFromCache("retrieveCriterionTotalCorrections"
                + idElt + idBline + baseNote + filter.getFilterDesc()
                + filter.getTypeElt());
        if (cacheResult == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            String query = getCriterionTotalCorrectionsQuery(baseNote, filter);
            logger.trace("retrieveCriterionTotalCorrections:" + idBline + ", "
                    + idElt + ", " + baseNote + ", " + filter);
            try {
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                if (filter.applyFilter()) {
                    pstmt.setString(3, filter.getFilterDesc());
                    if (filter.applyFilterTypeElt()) {
                        pstmt.setString(4, filter.getTypeElt());
                    }
                }
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
                dataCache.storeToCache(idBline, "retrieveCriterionTotalCorrections"
                        + idElt + idBline + baseNote + filter.getFilterDesc()
                        + filter.getTypeElt(), new Integer(result));
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
            logger.trace("End retrieveCriterionTotalCorrections");
        } else {
            result = cacheResult.intValue();
        }
        return result;
    }

    private String getCriterionTotalElementsQuery(
            int baseNote, FilterBean filter) {
        StringBuffer query = new StringBuffer("Select count(*)");
        query.append(" From ELEMENT_BASELINE_INFO ecr");
        if (filter.applyFilter()) {
            query.append(", Element elt");
        }
        query.append(" Where ecr.id_main_elt = ?");
        query.append(" AND ecr.id_bline = ?");
        if (baseNote == 1) {
            query.append(" And ecr.note1 > 0");
        } else {
            query.append(" And ecr.note1 = 0");
            if (baseNote == 2) {
                query.append(" And ecr.note2 > 0");
            } else {
                query.append(" And ecr.note2 = 0");
                query.append(" And (ecr.note3 > 0 or ecr.note4 > 0)");
            }
        }
        if (filter.applyFilter()) {
            query.append(" AND elt.id_main_elt=ecr.id_main_elt");
            query.append(" AND elt.id_elt=ecr.id_elt");
            query.append(" AND elt.desc_elt like ?");
            if (filter.applyFilterTypeElt()) {
                query.append(" And elt.id_telt = ?");
            }
        }
        return query.toString();
    }

    /** {@inheritDoc}
     */
    public int retrieveCriterionTotalElements(String idBline, String idElt, int baseNote, FilterBean filter) {
        int result = 0;
        Integer cacheResult = (Integer) dataCache.loadFromCache("retrieveCriterionTotalElements"
                + idElt + idBline + baseNote + filter.getFilterDesc()
                + filter.getTypeElt());
        if (cacheResult == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            String query = getCriterionTotalElementsQuery(baseNote, filter);
            try {
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                if (filter.applyFilter()) {
                    pstmt.setString(3, filter.getFilterDesc());
                    if (filter.applyFilterTypeElt()) {
                        pstmt.setString(4, filter.getTypeElt());
                    }
                }
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getInt(1);
                }
                dataCache.storeToCache(idBline, "retrieveCriterionTotalElements"
                        + idElt + idBline + baseNote + filter.getFilterDesc()
                        + filter.getTypeElt(), new Integer(result));
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        } else {
            result = cacheResult.intValue();
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection<FactorRepartitionBean> retrieveRepartitionByFactor(
            String idBline, String idElt, String idPro,
            String idUsa, String filter, String lastTypeElt) {
        Collection<FactorRepartitionBean> result = (Collection<FactorRepartitionBean>) dataCache.loadFromCache("retrieveRepartitionByFactor"
                + idBline + idElt + idPro + idUsa + filter + lastTypeElt);

        if (result == null) {
            result = new ArrayList<FactorRepartitionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(CRITERION_REPARTITION_BY_FACTOR);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idPro);
                pstmt.setString(3, idBline);
                pstmt.setString(4, idUsa);
                pstmt.setString(5, filter);
                pstmt.setString(6, lastTypeElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    int nb = rs.getInt(1);
                    String factId = rs.getString(2);
                    result.add(new FactorRepartitionBean(factId, nb));
                }
                dataCache.storeToCache(idBline, "retrieveRepartitionByFactor"
                        + idBline + idElt + idPro + idUsa + filter + lastTypeElt, result);
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionByCriterion(
            String idBline, String idElt, String idPro,
            String filter, String lastTypeElt) {
        Collection<CriterionRepartitionBean> result = (Collection<CriterionRepartitionBean>) dataCache.loadFromCache("retrieveRepartitionByCriterion"
                + idBline + idElt + idPro + filter + lastTypeElt);
        if (result == null) {
            result = new ArrayList<CriterionRepartitionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            int max = 0;
            try {
                pstmt = connection.prepareStatement(CRITERION_REPARTITION_BY_CRITERION);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idPro);
                pstmt.setString(3, idBline);
                pstmt.setString(4, filter);
                pstmt.setString(5, lastTypeElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    int nb = rs.getInt(1);
                    String factId = rs.getString(2);
                    result.add(new CriterionRepartitionBean(factId, nb));
                    max += nb;
                }
                purgeRepartitionCollection(result, max / 100);
                dataCache.storeToCache(idBline, "retrieveRepartitionByCriterion"
                        + idBline + idElt + idPro + filter + lastTypeElt, result);
            } catch (SQLException e) {
                logger.error("Error during Criterion retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
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
    public List<CriterionBean> retrieveFacteurSynthese(
            String idElt, String idPro,
            String idBline, String idFac) {
        List<CriterionBean> result = (List<CriterionBean>) dataCache.loadFromCache("retrieveFacteurSynthese"
                + idBline + idElt + idPro + idFac);
        if (result == null) {
            result = new ArrayList<CriterionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                String request = "Select distinct(l.id_crit) as id_crit, l.note_cribl, l.tendance, f.valeur_poids, l.just_note_cribl, l.id_just, critusa.id_telt,"
                        + " critusa.id_usa, elt.id_elt, l.cost, l.CRITERION_COMMENT"
                        + " From Critere_bline l, Poids_Fact_Crit f, Element elt, Critere_Usage critusa, Modele usage"
                        + " Where l.id_elt=?" + " And l.id_bline=?"
                        + " And f.bline_poids=l.id_bline"
                        + " And f.id_crit=l.id_crit" + " And f.id_fact=?"
                        + " And f.id_elt=l.id_elt" + " And f.id_pro=l.id_pro"
                        + " And elt.id_elt=l.id_elt"
                        + " And l.id_crit=critusa.id_crit"
                        + " And critusa.id_usa=elt.id_usa"
                        + " And usage.id_usa=critusa.id_usa"
                        + " order by l.id_crit";

                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                pstmt.setString(3, idFac);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    CriterionBean bean = new CriterionBean();
                    CriterionDefinition def = new CriterionDefinition();
                    def.setId(rs.getString("id_crit"));
                    def.setIdTElt(rs.getString("id_telt"));
                    bean.setCriterionDefinition(def);
                    bean.setNote(rs.getDouble("note_cribl"));
                    bean.setTendance(getDouble(rs, 3));
                    bean.setWeight(rs.getDouble("valeur_poids"));
                    if (rs.getString("id_just") != null) {
                        JustificationBean just = new JustificatifDbmsDao().retrieveJustificatifById(rs.getString("id_just"), connection);
                        just.setNote(rs.getDouble("just_note_cribl"));
                        bean.setJustNote(rs.getDouble("just_note_cribl"));
                        bean.setJustificatif(just);
                    }
                    ElementBean eb = new ElementBean();
                    eb.setId(rs.getString("id_elt"));
                    UsageBean ub = new UsageBean(rs.getString("id_usa"));
                    eb.setUsage(ub);
                    bean.setElement(eb);
                    bean.setCost(rs.getDouble("cost"));
                    bean.setComment(rs.getString("CRITERION_COMMENT"));
                    result.add(bean);
                }
                dataCache.storeToCache(idBline, "retrieveFacteurSynthese"
                        + idBline + idElt + idPro + idFac, result);
            } catch (SQLException e) {
                logger.error("Erreur lors de la recuperation de la synth�se du facteur "
                        + idFac);
                logger.error("id_pro=" + idPro + ", id_bline=" + idBline
                        + ", id_elt=" + idElt, e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Map retrieveCriterionNoteRepartition(String idElt, String idPro,
            String idBline, String idFac) {
        Map result = (Map) dataCache.loadFromCache("retrieveCriterionNoteRepartition"
                + idBline + idElt + idPro + idFac);
        if (result == null) {
            result = new HashMap();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                String request = "Select nr.id_crit, nr.seuil, nr.total"
                        + " From CritereNoteRepartition nr, Poids_Fact_Crit p"
                        + " Where p.bline_poids=?" + " And p.id_fact=?"
                        + " And p.id_elt=?" + " And nr.id_elt=p.id_elt"
                        + " And nr.id_crit=p.id_crit"
                        + " And nr.id_bline=p.bline_poids"
                        + " order by nr.id_crit, nr.total ASC";
// Preparing the request.
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idFac);
                pstmt.setString(3, idElt);
// Executing the request.
                rs = pstmt.executeQuery();
                String lastCritId = "";
                CriterionNoteRepartition lastRepartition = null;
// Loop on result set.
                while (rs.next()) {
                    String actualCritId = rs.getString(1);
                    if (!actualCritId.equals(lastCritId)) {
                        /* The actual result concern a different criterion than the last one.
                         * Creation of a new storage element associated to the new criterion.
                         * Criterion ID initialization.
                         * Add the new criterion storage element to the hashtable result.
                         */
                        lastRepartition = new CriterionNoteRepartition();
                        lastRepartition.setId(actualCritId);
                        result.put(actualCritId, lastRepartition);
                    }
// Add part and value to the criterion storage element.
                    lastRepartition.add(rs.getInt(2), rs.getInt(3));
// Update the last criterion ID.
                    lastCritId = actualCritId;
                }
                dataCache.storeToCache(idBline, "retrieveCriterionNoteRepartition"
                        + idBline + idElt + idPro + idFac, result);
            } catch (SQLException e) {
                logger.error("Erreur lors de la recuperation de la r�partition des notes par crit�re "
                        + idFac);
                logger.error("id_pro=" + idPro + ", id_bline=" + idBline
                        + ", id_elt=" + idElt, e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    protected Collection retrieveCriterionNoteRepartition(String idElt, String idPro, String idBline) {
        Collection result = new ArrayList();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            result = retrieveCriterionNoteRepartition(idElt, idPro, idBline, connection);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private Collection retrieveCriterionNoteRepartition(String idElt, String idPro, String idBline, Connection connection) {
        Collection result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String request = "Select c.id_crit, FLOOR(c.note_cribl), FLOOR(c.just_note_cribl), count(*)"
                    + " From Critere_bline c, Element e, Element e2, Critere_usage cu"
                    + " Where c.id_bline='" + idBline + "'" + " And c.id_pro='"
                    + idPro + "'" + " And e2.id_main_elt='" + idElt + "'"
                    + " And e.id_elt = e2.id_main_elt"
                    + " And e.id_pro = e2.id_pro" + " And e2.id_elt=c.id_elt"
                    + " And e2.id_pro=c.id_pro" + " And cu.id_usa=e.id_usa"
                    + " And cu.id_telt=e2.id_telt" + " And cu.id_crit=c.id_crit"
                    + " And c.note_cribl > 0"
                    + " group by c.id_crit, FLOOR(c.note_cribl), FLOOR(c.just_note_cribl)"
                    + " order by c.id_crit, FLOOR(c.note_cribl), FLOOR(c.just_note_cribl) ASC";
            // Preparing the request.
            pstmt = connection.prepareStatement(request);
            // Executing the request.
            rs = pstmt.executeQuery();
            String lastCritId = "";
            CriterionNoteRepartition lastRepartition = null;
            // Loop on result set.
            while (rs.next()) {
                String actualCritId = rs.getString(1);
                if (!actualCritId.equals(lastCritId)) {
                    /* The actual result concern a different criterion than the last one.
                     * Creation of a new storage element associated to the new criterion.
                     * Criterion ID initialization.
                     * Add the new criterion storage element to the hashtable result.
                     */
                    lastRepartition = new CriterionNoteRepartition();
                    lastRepartition.setId(actualCritId);
                    result.add(lastRepartition);
                }
                int nbElts = rs.getInt(4);
                //le seuil
                int value = rs.getInt(2) - 1;
                int justif = rs.getInt(3);
                if (justif > 0) {
                    value = justif - 1;
                }
                // Add part and value to the criterion storage element.
                lastRepartition.add(value, nbElts);
                // Update the last criterion ID.
                lastCritId = actualCritId;
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la r�partition des notes par crit�re.");
            logger.error("id_pro=" + idPro + ", id_bline=" + idBline
                    + ", id_elt=" + idElt, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public void calculateCriterionNoteRepartition(String idElt, String idPro, String idBline) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            calculateCriterionNoteRepartition(idElt, idPro, idBline, connection);
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la repartition des notes par critere.");
            logger.error("id_pro=" + idPro + ", id_bline=" + idBline
                    + ", id_elt=" + idElt, e);
            JdbcDAOUtils.rollbackConnection(connection);
        } catch (DataAccessException e) {
            logger.error("Erreur lors de la recuperation de la repartition des notes par critere.");
            logger.error("id_pro=" + idPro + ", id_bline=" + idBline
                    + ", id_elt=" + idElt, e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /** {@inheritDoc}
     */
    public void calculateCriterionNoteRepartition(String idElt, String idPro, String idBline, Connection connection) throws DataAccessException {
        Collection noteRepartition = retrieveCriterionNoteRepartition(idElt, idPro, idBline, connection);
        if (noteRepartition != null && !noteRepartition.isEmpty()) {
            Iterator i = noteRepartition.iterator();
            while (i.hasNext()) {
                CriterionNoteRepartition cnr = (CriterionNoteRepartition) i.next();
                for (int idx = 0; idx < 4; idx++) {
                    updateCritereNoteRepartition(idElt, idBline, cnr, idx, connection);
                }
            }
        }
    }
    private static final String CRITERE_NOTE_REPARTITION_SELECT = "Select nr.id_elt, nr.id_bline, nr.id_crit, nr.seuil, nr.total From CritereNoteRepartition nr"
            + " Where nr.id_elt=? And nr.id_bline=? And nr.id_crit=? And nr.seuil=?";
    private static final String CRITERE_NOTE_REPARTITION_INSERT = "INSERT INTO CritereNoteRepartition (id_elt, id_bline, id_crit, seuil, total) VALUES (?, ?, ?, ?, ?)";
    private static final String CRITERE_NOTE_REPARTITION_UPDATE = "UPDATE CritereNoteRepartition SET total=? WHERE id_elt=? AND id_bline=? AND id_crit=? AND seuil=?";

    protected void updateCritereNoteRepartition(String idElt, String idBline,
            CriterionNoteRepartition cnr, int seuil, Connection conn)
            throws DataAccessException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(CRITERE_NOTE_REPARTITION_SELECT);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, cnr.getId());
            pstmt.setInt(4, seuil);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
                pstmt = conn.prepareStatement(CRITERE_NOTE_REPARTITION_UPDATE);
                // Maj des donnees dans la base.
                // Initialisation des parametres de la requete.
                pstmt.setDouble(1, cnr.getValue(seuil));
                pstmt.setString(2, idElt);
                pstmt.setString(3, idBline);
                pstmt.setString(4, cnr.getId());
                pstmt.setInt(5, seuil);
                // Execution de la requete de mise a jour.
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                pstmt = conn.prepareStatement(CRITERE_NOTE_REPARTITION_INSERT);
                // Insertion des donnees dans la base.
                // Initialisation des parametres de la requete.
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                pstmt.setString(3, cnr.getId());
                pstmt.setInt(4, seuil);
                pstmt.setDouble(5, cnr.getValue(seuil));
                // Execution de la requete d'insertion.
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error updating criterenoterepartition id_crit="
                    + cnr.getId(), e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            //connection.setAutoCommit(true);
        }
    }

    /**
     * Get a Double object from a ResultSet and a column index.
     * @param rs the ResultSet
     * @param idx the column index
     * @return a Double object or null
     * @throws SQLException
     */
    private static Double getDouble(ResultSet rs, int idx) throws SQLException {
        Double result = null;
        if (rs != null && idx > 0) {
            double val = rs.getDouble(idx);
            if (!rs.wasNull()) {
                result = new Double(val);
            }
        }
        return result;
    }

    /**
     * Get a Double object from a ResultSet and a column index.
     * @param rs the ResultSet
     * @param cname the column name
     * @return a Double object or null
     * @throws SQLException
     */
    private static Double getDouble(ResultSet rs, String cname) throws SQLException {
        Double result = null;
        if (rs != null && cname != null) {
            double val = rs.getDouble(cname);
            if (!rs.wasNull()) {
                result = new Double(val);
            }
        }
        return result;
    }

    /* Nouveau et Mauvais */
    private static final String NEW_AND_BAD_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, note1 + note2"
            + " From ELEMENT_BASELINE_INFO cb, Element e"
            + " Where id_bline = ?" + " And cb.id_elt = e.id_elt"
            + " And cb.id_main_elt = ?" + " And (note1 > 0 OR note2 > 0)"
            + " And nbbetter = 0" + " And nbworst = 0" + " And nbstable = 0";

    /* Ancien degrade */
    private static final String OLD_AND_WORST_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, nbworst"
            + " From ELEMENT_BASELINE_INFO cb, Element e"
            + " Where id_bline = ?" + " And cb.id_elt = e.id_elt"
            + " And cb.id_main_elt = ?" + " And nbworst > 0"
            + " And nbbetter = 0";

    /* Ancien ameliore */
    private static final String OLD_AND_BETTER_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, nbbetter"
            + " From ELEMENT_BASELINE_INFO cb, Element e"
            + " Where id_bline = ?" + " And cb.id_elt = e.id_elt"
            + " And cb.id_main_elt = ?" + " And nbworst = 0"
            + " And nbbetter > 0";

    /* Ancien ameliore et degrade */
    private static final String OLD_BETTER_AND_WORST_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, nbworst + nbbetter"
            + " From ELEMENT_BASELINE_INFO cb, Element e"
            + " Where id_bline = ?" + " And cb.id_elt = e.id_elt"
            + " And cb.id_main_elt = ?" + " And nbworst > 0"
            + " And nbbetter > 0";

    /* Ancien mauvais et stable */
    private static final String BAD_AND_STABLE_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, nbstable"
            + " From ELEMENT_BASELINE_INFO cb, Element e"
            + " Where id_bline = ?" + " And cb.id_elt = e.id_elt"
            + " And cb.id_main_elt = ?" + " And nbworst = 0"
            + " And nbbetter = 0" + " And nbstable > 0"
            + " And (note1 > 0 OR note2 > 0)";

    /* Ancien et stable */
    private static final String STABLE_QUERY =
            "Select e.id_elt, e.lib_elt, e.desc_elt, nbstable"
            + " From ELEMENT_BASELINE_INFO cb, Element e"
            + " Where id_bline = ?" + " And cb.id_elt = e.id_elt"
            + " And cb.id_main_elt = ?" + " And nbworst = 0"
            + " And nbbetter = 0" + " And nbstable > 0";
    private static final String FILTER_TYPE_SUBQUERY =
            " And id_telt = ";
    private static final String FILTER_DESC_SUBQUERY =
            " And desc_elt like ";
    private static final String ORDER_BY_LIB_ELT_SUBQUERY =
            " Order by e.lib_elt";

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
    
    private static final String RETRIEVE_NEW_AND_BAD_ELEMENTS_WITH_CRITERIONS =
            "SELECT elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.tendance, cb.note_cribl, cb.just_note_cribl"
            + " FROM Critere_bline cb, Element elt"
            + " WHERE cb.tendance is null" + " AND cb.id_bline = ?"
            + " AND cb.id_pro = ?" + " AND elt.id_elt = cb.id_elt "
            + " AND elt.id_main_elt = ? " + " AND cb.note_cribl > 0 " + " AND "
            + " ( (cb.note_cribl < 3 AND cb.just_note_cribl is null) "
            + " OR (cb.just_note_cribl IS NOT NULL AND cb.just_note_cribl < 3) "
            + " ) " + "ORDER BY elt.id_elt";
    private static final String RETRIEVE_OLD_WORST_ELEMENTS_WITH_CRITERIONS =
            "SELECT elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.tendance, cb.note_cribl, cb.just_note_cribl"
            + " FROM Critere_bline cb, Element elt"
            + " WHERE cb.tendance is not null" + " AND cb.id_bline = ?"
            + " AND cb.id_pro = ?" + " AND elt.id_elt = cb.id_elt "
            + " AND elt.id_main_elt = ? "
            + " AND cb.note_cribl > 0 AND tendance > 0" + " AND "
            + " ( (cb.just_note_cribl is null AND cb.tendance > cb.note_cribl) "
            + " OR (cb.just_note_cribl IS NOT NULL AND cb.tendance > cb.just_note_cribl) "
            + " ) " + " AND cb.id_elt in ( "
            + "  select id_elt from element_baseline_info where nbbetter = 0 and nbworst > 0 "
            + " )  " + "ORDER BY elt.id_elt";
    private static final String RETRIEVE_OLD_BETTER_ELEMENTS_WITH_CRITERIONS =
            "SELECT elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.tendance, cb.note_cribl, cb.just_note_cribl"
            + " FROM Critere_bline cb, Element elt"
            + " WHERE cb.tendance is not null" + " AND cb.id_bline = ?"
            + " AND cb.id_pro = ?" + " AND elt.id_elt = cb.id_elt "
            + " AND elt.id_main_elt = ? "
            + " AND cb.note_cribl > 0 AND tendance > 0" + " AND "
            + " ( (cb.just_note_cribl is null AND cb.tendance < cb.note_cribl) "
            + " OR (cb.just_note_cribl IS NOT NULL AND cb.tendance < cb.just_note_cribl) "
            + " ) " + " AND cb.id_elt in ( "
            + "  select id_elt from element_baseline_info where nbbetter > 0 and nbworst = 0 "
            + " )  " + "ORDER BY elt.id_elt";
    private static final String RETRIEVE_OLD_BETTERWORST_ELEMENTS_WITH_CRITERIONS =
            "SELECT elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.tendance, cb.note_cribl, cb.just_note_cribl"
            + " FROM Critere_bline cb, Element elt"
            + " WHERE cb.tendance is not null" + " AND cb.id_bline = ?"
            + " AND cb.id_pro = ?" + " AND elt.id_elt = cb.id_elt "
            + " AND elt.id_main_elt = ? "
            + " AND cb.note_cribl > 0 AND tendance > 0" + " AND "
            + " ( (cb.just_note_cribl is null AND cb.tendance <> cb.note_cribl) "
            + " OR (cb.just_note_cribl IS NOT NULL AND cb.tendance <> cb.just_note_cribl) "
            + " ) " + " AND cb.id_elt in ( "
            + "  select id_elt from element_baseline_info where nbbetter > 0 and nbworst > 0 "
            + " )  " + "ORDER BY elt.id_elt";
    private static final String RETRIEVE_OLD_BADSTABLE_ELEMENTS_WITH_CRITERIONS =
            "SELECT elt.id_elt, elt.lib_elt, elt.desc_elt, cb.id_crit, cb.tendance, cb.note_cribl, cb.just_note_cribl"
            + " FROM Critere_bline cb, Element elt"
            + " WHERE cb.tendance is not null" + " AND cb.id_bline = ?"
            + " AND cb.id_pro = ?" + " AND elt.id_elt = cb.id_elt "
            + " AND elt.id_main_elt = ? "
            + " AND cb.note_cribl > 0 AND tendance > 0" + " AND "
            + " ( (cb.note_cribl < 3 AND cb.just_note_cribl is null AND cb.tendance = cb.note_cribl) "
            + " OR (cb.just_note_cribl IS NOT NULL AND cb.just_note_cribl < 3 AND cb.tendance = cb.just_note_cribl) "
            + " ) " + " AND cb.id_elt in ( "
            + "  select id_elt from element_baseline_info where nbbetter = 0 and nbworst = 0 and nbstable > 0"
            + " )  " + "ORDER BY elt.id_elt";

    private List<BottomUpDetailBean> retrieveEvolutionElementsWithCriterions(String idEa, String idBline, String idPro, String query) {
        List<BottomUpDetailBean> result = new ArrayList<BottomUpDetailBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idPro);
            pstmt.setString(3, idEa);
            rs = pstmt.executeQuery();
            String oldIdElt = null;
            BottomUpDetailBean bean = null;
            List<CriterionPerFactorBean> critList = null;
            while (rs.next()) {
                //cb.id_crit, cb.note_cribl, cb.just_note_cribl, cb.id_just, cb.tendance
                String thisIdElt = rs.getString("id_elt");
                if (!thisIdElt.equals(oldIdElt)) {
                    //ce n'est plus le meme element
                    bean = new BottomUpDetailBean();
                    ElementBean elt = new ElementBean();
                    elt.setId(rs.getString("id_elt"));
                    elt.setLib(rs.getString("lib_elt"));
                    elt.setDesc(rs.getString("desc_elt"));
                    bean.setElement(elt);
                    result.add(bean);
                    critList = new ArrayList<CriterionPerFactorBean>();
                    bean.setCriterions(critList);
                    oldIdElt = thisIdElt;
                }
                CriterionPerFactorBean cpfb = new CriterionPerFactorBean();
                cpfb.setId(rs.getString("id_crit"));
                cpfb.setTendance(rs.getDouble("tendance"));
                double note = rs.getDouble("note_cribl");
                double justNote = rs.getDouble("just_note_cribl");
                if (justNote > 0.0) {
                    cpfb.setNote(justNote);
                } else {
                    cpfb.setNote(note);
                }
                if (critList != null) {
                    critList.add(cpfb);
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la synthese d'evolution: id baseline="
                    + idBline);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<BottomUpDetailBean> retrieveNewAndBadElementsWithCriterions(String idEa, String idBline, String idPro) {
        List<BottomUpDetailBean> result = (List<BottomUpDetailBean>) dataCache.loadFromCache("retrieveNewAndBadElementsWithCriterions"
                + idEa + idBline + idPro);
        if (result == null) {
            result = this.retrieveEvolutionElementsWithCriterions(idEa, idBline, idPro, RETRIEVE_NEW_AND_BAD_ELEMENTS_WITH_CRITERIONS);
            dataCache.storeToCache(idBline, "retrieveNewAndBadElementsWithCriterions"
                    + idEa + idBline + idPro, result);
        }
        return result;
    }

    public List<BottomUpDetailBean> retrieveOldWorstElementsWithCriterions(String idEa, String idBline, String idPro) {
        List<BottomUpDetailBean> result = (List<BottomUpDetailBean>) dataCache.loadFromCache("retrieveOldWorstElementsWithCriterions"
                + idEa + idBline + idPro);
        if (result == null) {
            result = this.retrieveEvolutionElementsWithCriterions(idEa, idBline, idPro, RETRIEVE_OLD_WORST_ELEMENTS_WITH_CRITERIONS);
            dataCache.storeToCache(idBline, "retrieveOldWorstElementsWithCriterions"
                    + idEa + idBline + idPro, result);
        }
        return result;
    }

    public List<BottomUpDetailBean> retrieveOldBetterElementsWithCriterions(String idEa, String idBline, String idPro) {
        List<BottomUpDetailBean> result = (List<BottomUpDetailBean>) dataCache.loadFromCache("retrieveOldBetterElementsWithCriterions"
                + idEa + idBline + idPro);
        if (result == null) {
            result = this.retrieveEvolutionElementsWithCriterions(idEa, idBline, idPro, RETRIEVE_OLD_BETTER_ELEMENTS_WITH_CRITERIONS);
            dataCache.storeToCache(idBline, "retrieveOldBetterElementsWithCriterions"
                    + idEa + idBline + idPro, result);
        }
        return result;
    }

    public List<BottomUpDetailBean> retrieveOldBetterWorstElementsWithCriterions(String idEa, String idBline, String idPro) {
        List<BottomUpDetailBean> result = (List<BottomUpDetailBean>) dataCache.loadFromCache("retrieveOldBetterWorstElementsWithCriterions"
                + idEa + idBline + idPro);
        if (result == null) {
            result = this.retrieveEvolutionElementsWithCriterions(idEa, idBline, idPro, RETRIEVE_OLD_BETTERWORST_ELEMENTS_WITH_CRITERIONS);
            dataCache.storeToCache(idBline, "retrieveOldBetterWorstElementsWithCriterions"
                    + idEa + idBline + idPro, result);
        }
        return result;
    }

    public List<BottomUpDetailBean> retrieveOldBadStableElementsWithCriterions(String idEa, String idBline, String idPro) {
        List<BottomUpDetailBean> result = (List<BottomUpDetailBean>) dataCache.loadFromCache("retrieveOldBadStableElementsWithCriterions"
                + idEa + idBline + idPro);
        if (result == null) {
            result = this.retrieveEvolutionElementsWithCriterions(idEa, idBline, idPro, RETRIEVE_OLD_BADSTABLE_ELEMENTS_WITH_CRITERIONS);
            dataCache.storeToCache(idBline, "retrieveOldBadStableElementsWithCriterions"
                    + idEa + idBline + idPro, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt, String idBline, FilterBean filter) {
        List<ElementEvolutionSummaryBean> result = (List<ElementEvolutionSummaryBean>) dataCache.loadFromCache("retrieveNewAndBadElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createEvolutionQuery(NEW_AND_BAD_QUERY, filter);
            result = retrieveEvolutionSummary(query, idElt, idBline);
            dataCache.storeToCache(idBline, "retrieveNewAndBadElements" + filter
                    + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt, String idBline, FilterBean filter) {
        List<ElementEvolutionSummaryBean> result = (List<ElementEvolutionSummaryBean>) dataCache.loadFromCache("retrieveOldAndWorstElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createEvolutionQuery(OLD_AND_WORST_QUERY, filter);
            result = retrieveEvolutionSummary(query, idElt, idBline);
            dataCache.storeToCache(idBline, "retrieveOldAndWorstElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt, String idBline, FilterBean filter) {
        List<ElementEvolutionSummaryBean> result = (List<ElementEvolutionSummaryBean>) dataCache.loadFromCache("retrieveOldAndBetterElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createEvolutionQuery(OLD_AND_BETTER_QUERY, filter);
            result = retrieveEvolutionSummary(query, idElt, idBline);
            dataCache.storeToCache(idBline, "retrieveOldAndBetterElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt, String idBline, FilterBean filter) {
        List<ElementEvolutionSummaryBean> result = (List<ElementEvolutionSummaryBean>) dataCache.loadFromCache("retrieveOldBetterAndWorstElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createEvolutionQuery(OLD_BETTER_AND_WORST_QUERY, filter);
            result = retrieveEvolutionSummary(query, idElt, idBline);
            dataCache.storeToCache(idBline, "retrieveOldBetterAndWorstElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt, String idBline, FilterBean filter) {
        List<ElementEvolutionSummaryBean> result = (List<ElementEvolutionSummaryBean>) dataCache.loadFromCache("retrieveStableElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createEvolutionQuery(STABLE_QUERY, filter);
            result = retrieveEvolutionSummary(query, idElt, idBline);
            dataCache.storeToCache(idBline, "retrieveStableElements" + filter
                    + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt, String idBline, FilterBean filter) {
        List<ElementEvolutionSummaryBean> result = (List<ElementEvolutionSummaryBean>) dataCache.loadFromCache("retrieveBadAndStableElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createEvolutionQuery(BAD_AND_STABLE_QUERY, filter);
            result = retrieveEvolutionSummary(query, idElt, idBline);
            dataCache.storeToCache(idBline, "retrieveBadAndStableElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    private List<ElementEvolutionSummaryBean> retrieveEvolutionSummary(String query, String idEltPere, String idBline) {
        List<ElementEvolutionSummaryBean> result = new ArrayList<ElementEvolutionSummaryBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idEltPere);
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
                    + idBline);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private String createRepartitionQuery(String query, FilterBean filter) {
        StringBuffer result = new StringBuffer(REPARTITION_SELECT_FROM_SUBQUERY);
        if (filter.applyFilter()) {
            result.append(REPARTITION_FILTER_FROM_SUBQUERY);
        }
        result.append(REPARTITION_WHERE_SUBQUERY);
        if (filter.applyFilter()) {
            result.append(REPARTITION_FILTER_JOIN_SUBQUERY);
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
    public Collection retrieveRepartitionNewAndBadElements(String idElt, String idBline, FilterBean filter) {
        Collection result = (Collection) dataCache.loadFromCache("retrieveRepartitionNewAndBadElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createRepartitionQuery(REPARTITION_CRIT_NEW_AND_BAD_QUERY, filter);
            result = retrieveEvolutionRepartitionByCriterion(idElt, idBline, query);
            dataCache.storeToCache(idBline, "retrieveRepartitionNewAndBadElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection retrieveRepartitionOldAndWorstElements(String idElt, String idBline, FilterBean filter) {
        Collection result = (Collection) dataCache.loadFromCache("retrieveRepartitionOldAndWorstElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createRepartitionQuery(REPARTITION_CRIT_OLD_AND_WORST_QUERY, filter);
            result = retrieveEvolutionRepartitionByCriterion(idElt, idBline, query);
            dataCache.storeToCache(idBline, "retrieveRepartitionOldAndWorstElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection retrieveRepartitionOldAndBetterElements(String idElt, String idBline, FilterBean filter) {
        Collection result = (Collection) dataCache.loadFromCache("retrieveRepartitionOldAndBetterElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createRepartitionQuery(REPARTITION_CRIT_OLD_AND_BETTER_QUERY, filter);
            result = retrieveEvolutionRepartitionByCriterion(idElt, idBline, query);
            dataCache.storeToCache(idBline, "retrieveRepartitionOldAndBetterElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection retrieveRepartitionOldBetterWorstElements(String idElt, String idBline, FilterBean filter) {
        Collection result = (Collection) dataCache.loadFromCache("retrieveRepartitionOldBetterWorstElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createRepartitionQuery(REPARTITION_CRIT_OLD_BETTER_AND_WORST_QUERY, filter);
            result = retrieveEvolutionRepartitionByCriterion(idElt, idBline, query);
            dataCache.storeToCache(idBline, "retrieveRepartitionOldBetterWorstElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection retrieveRepartitionBadStableElements(String idElt, String idBline, FilterBean filter) {
        Collection result = (Collection) dataCache.loadFromCache("retrieveRepartitionBadStableElements"
                + filter + idElt + idBline);
        if (result == null) {
            String query = createRepartitionQuery(REPARTITION_CRIT_BAD_AND_STABLE_QUERY, filter);
            result = retrieveEvolutionRepartitionByCriterion(idElt, idBline, query);
            dataCache.storeToCache(idBline, "retrieveRepartitionBadStableElements"
                    + filter + idElt + idBline, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection retrieveEvolutionRepartitionByCriterion(String idElt, String idBline, String query) {
        Collection result = new ArrayList();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int max = 0;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idElt);
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
    private static final String INSERT_ELEMENT_BASELINE_INFO_START_LINE_QUERY = "Insert into ELEMENT_BASELINE_INFO (id_elt, id_bline, id_main_elt, start_Line, note1, note2, note3, note4) VALUES (?,?,?,?,0,0,0,0)";
    private static final String UPDATE_ELEMENT_BASELINE_INFO_START_LINE_QUERY = "Update ELEMENT_BASELINE_INFO Set start_Line=? Where id_elt=? And id_bline=?";

    /** {@inheritDoc}
     */
    public void initElementBaselineInformation(String idElt, String idBline,
            String idMainElt, int startLine) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(SELECT_ELEMENT_BASELINE_INFO_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
                pstmt = connection.prepareStatement(UPDATE_ELEMENT_BASELINE_INFO_START_LINE_QUERY);
                pstmt.setInt(1, startLine);
                pstmt.setString(2, idElt);
                pstmt.setString(3, idBline);
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                pstmt = connection.prepareStatement(INSERT_ELEMENT_BASELINE_INFO_START_LINE_QUERY);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                pstmt.setString(3, idMainElt);
                pstmt.setInt(4, startLine);
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("initElementBaselineInformation ID_ELT=" + idElt
                    + ", ID_BLINE=" + idBline, e);
            throw new DataAccessException("initElementBaselineInformation ID_ELT="
                    + idElt + ", ID_BLIEN=" + idBline, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String SELECT_ELEMENT_BASELINE_INFO_QUERY = "Select id_elt From ELEMENT_BASELINE_INFO Where id_elt=? And id_bline=?";
    private static final String INSERT_ELEMENT_BASELINE_INFO_QUERY = "Insert into ELEMENT_BASELINE_INFO (id_elt, id_bline, id_main_elt, note1, note2, note3, note4) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_ELEMENT_BASELINE_INFO_QUERY = "Update ELEMENT_BASELINE_INFO Set note1=?, note2=?, note3=?, note4=? Where id_elt=? And id_bline=?";

    /** {@inheritDoc}
     */
    public void updateElementBaselineInformation(String idElt, String idBline, String idMainElt, int[] notes, Connection connection) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(SELECT_ELEMENT_BASELINE_INFO_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
                pstmt = connection.prepareStatement(UPDATE_ELEMENT_BASELINE_INFO_QUERY);
                pstmt.setInt(1, notes[0]);
                pstmt.setInt(2, notes[1]);
                pstmt.setInt(3, notes[2]);
                pstmt.setInt(4, notes[3]);
                pstmt.setString(5, idElt);
                pstmt.setString(6, idBline);
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                pstmt = connection.prepareStatement(INSERT_ELEMENT_BASELINE_INFO_QUERY);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                pstmt.setString(3, idMainElt);
                pstmt.setInt(4, notes[0]);
                pstmt.setInt(5, notes[1]);
                pstmt.setInt(6, notes[2]);
                pstmt.setInt(7, notes[3]);
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("updateElementBaselineInformation ID_ELT=" + idElt
                    + ", ID_BLIEN=" + idBline, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }
    private static final String UPDATE_ELEMENT_BASELINE_INFO_EVOL_BETTER_QUERY = "Update ELEMENT_BASELINE_INFO Set nbbetter=(Select count(note_cribl) From Critere_bline c Where c.id_elt=? and c.id_bline=? and note_cribl>tendance) Where id_elt=? And id_bline=?";
    private static final String UPDATE_ELEMENT_BASELINE_INFO_EVOL_WORST_QUERY = "Update ELEMENT_BASELINE_INFO Set nbworst=(Select count(note_cribl) From Critere_bline c Where c.id_elt=? and c.id_bline=? and note_cribl<tendance) Where id_elt=? And id_bline=?";
    private static final String UPDATE_ELEMENT_BASELINE_INFO_EVOL_STABLE_QUERY = "Update ELEMENT_BASELINE_INFO Set nbstable=(Select count(note_cribl) From Critere_bline c Where c.id_elt=? and c.id_bline=? and note_cribl=tendance) Where id_elt=? And id_bline=?";

    /** {@inheritDoc}
     */
    public void updateElementBaselineInformationEvolution(String idElt, String idBline, Connection connection) {
        updateElementBaselineInformationEvolution(idElt, idBline, UPDATE_ELEMENT_BASELINE_INFO_EVOL_BETTER_QUERY, connection);
        updateElementBaselineInformationEvolution(idElt, idBline, UPDATE_ELEMENT_BASELINE_INFO_EVOL_WORST_QUERY, connection);
        updateElementBaselineInformationEvolution(idElt, idBline, UPDATE_ELEMENT_BASELINE_INFO_EVOL_STABLE_QUERY, connection);
    }

    /** {@inheritDoc}
     */
    public void updateElementBaselineInformationEvolution(String idElt, String idBline, String query, Connection connection) {
        PreparedStatement pstmt = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idElt);
            pstmt.setString(4, idBline);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("updateElementBaselineInformationEvolution ID_ELT="
                    + idElt + ", ID_BLINE=" + idBline, e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }
    private static final String SELECT_CRITERION_QUERY = "SELECT id_crit, id_bline, id_elt From Critere_BLINE Where id_elt=? And id_bline=? And id_crit=?";
    /** Requete d'insertion d'un critere. */
    private static final String INSERT_CRITERION_QUERY = "INSERT INTO Critere_BLINE (id_crit, id_bline, id_pro, id_elt, note_cribl, dinst_cribl, cost)"
            + " VALUES (?, ?, ?, ?, ?, {fn now()}, ?)";
    /** Requete de mise a jour d'un critere. */
    private static final String UPDATE_CRITERION_QUERY = "UPDATE Critere_BLINE SET note_cribl=?, dmaj_cribl={fn now()}, cost=?"
            + " WHERE id_crit=? AND id_bline=? AND id_pro=? AND id_elt=?";

    public void updateCriterion(Collection criterionColl, BaselineBean baseline, ProjectBean projet, String idElt, Connection connection) throws DataAccessException {
        if (criterionColl != null && !criterionColl.isEmpty()) {
            PreparedStatement pstmtSelect = null;
            PreparedStatement pstmtInsert = null;
            PreparedStatement pstmtUpdate = null;
            ResultSet rs = null;
            try {
                connection.setAutoCommit(false);
                pstmtSelect = connection.prepareStatement(SELECT_CRITERION_QUERY);
                pstmtUpdate = connection.prepareStatement(UPDATE_CRITERION_QUERY);
                pstmtInsert = connection.prepareStatement(INSERT_CRITERION_QUERY);
                Iterator i = criterionColl.iterator();
                Critere currentCrit = null;
                while (i.hasNext()) {
                    currentCrit = (Critere) i.next();
                    double valnote = 0;
                    if (currentCrit.getCalculatedNote() > 0) {
                        valnote = currentCrit.getCalculatedNote();
                    }
                    pstmtSelect.setString(1, idElt);
                    pstmtSelect.setString(2, baseline.getId());
                    pstmtSelect.setString(3, currentCrit.getId());
                    rs = pstmtSelect.executeQuery();
                    if (rs.next()) {
                        // Maj des donn�es dans la base.
                        // Initialisation des param�tres de la requ�te.
                        pstmtUpdate.setDouble(1, valnote);
                        pstmtUpdate.setDouble(2, currentCrit.getCost());
                        pstmtUpdate.setString(3, currentCrit.getId());
                        pstmtUpdate.setString(4, baseline.getId());
                        pstmtUpdate.setString(5, projet.getId());
                        pstmtUpdate.setString(6, idElt);
                        // Ex�cution de la requete de mise a jour.
                        pstmtUpdate.addBatch();
                    } else {
                        // Insertion des donnees dans la base.
                        // Initialisation des parametres de la requete.
                        pstmtInsert.setString(1, currentCrit.getId());
                        pstmtInsert.setString(2, baseline.getId());
                        pstmtInsert.setString(3, projet.getId());
                        pstmtInsert.setString(4, idElt);
                        pstmtInsert.setDouble(5, valnote);
                        pstmtInsert.setDouble(6, currentCrit.getCost());
                        // Execution de la requete d'insertion.
                        pstmtInsert.addBatch();
                    }
                    if (currentCrit.getIdJustification() != null) {
                        // Mise a jour du justificatif dans la base.
                        updateCritereJustDataBase(currentCrit, connection, baseline, projet, idElt);
                    }
                    JdbcDAOUtils.closeResultSet(rs);
                }
                pstmtInsert.executeBatch();
                pstmtUpdate.executeBatch();
            } catch (SQLException e) {
                logger.error("Error updating criterion data", e);
                throw new DataAccessException(e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmtSelect);
                JdbcDAOUtils.closePrepareStatement(pstmtInsert);
                JdbcDAOUtils.closePrepareStatement(pstmtUpdate);
                //connection.setAutoCommit(true);
            }
        }
    }
    /** Requete de mise a jour du justificatif d'un critere. */
    private static final String UPDATE_CRITERION_JUST_QUERY = "UPDATE Critere_BLINE SET id_just=?, just_note_cribl=?, dmaj_cribl={fn now()}"
            + " WHERE id_crit=? AND id_bline=? AND id_pro=? AND id_elt=?";

    /** Mise a jour du justificatif pour le critere.
     * @param connection la connexion DB utilisee.
     * @param baseline la baseline actuelle.
     * @param projet le projet.
     * @param id_elt l'identifiant de l'element concerne.
     */
    protected void updateCritereJustDataBase(Critere crit,
            Connection connection, BaselineBean baseline,
            ProjectBean projet, String idElt) throws DataAccessException {
        logger.info("Update Critere: " + crit.getId() + ", "
                + crit.getIdJustification() + ", " + crit.getCalculatedNote()
                + ", " + crit.getNoteJustifiee());
        PreparedStatement updatestmt = null;
        try {
            updatestmt = connection.prepareStatement(UPDATE_CRITERION_JUST_QUERY);
            if ((crit.getIdJustification() != null) && (crit.getNoteJustifiee()
                    > 0.0) && (crit.getCalculatedNote()
                    > crit.getNoteJustifiee())) {
                // La nouvelle note calcul�e est sup�rieure � l'ancienne: pas de note justifi�e.
                updatestmt.setString(1, null);
                updatestmt.setDouble(2, 0.0);
            } else {
                // La nouvelle note calcul�e est inf�rieure ou �gale � l'ancienne: on applique l'ancien justificatif et la note justifi�e.
                updatestmt.setString(1, crit.getIdJustification());
                updatestmt.setDouble(2, crit.getNoteJustifiee());
            }
            updatestmt.setString(3, crit.getId());
            updatestmt.setString(4, baseline.getId());
            updatestmt.setString(5, projet.getId());
            updatestmt.setString(6, idElt);
            // Ex�cution de la requete de mise a jour.
            updatestmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating justif", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(updatestmt);
        }
    }
    /** Requete d'insertion du poids d'un critere dans le calcul d'un facteur. */
    private static final String INSERT_WEIGHT_QUERY = "INSERT INTO Poids_fact_crit (id_fact, id_crit, bline_poids, id_pro, id_elt, valeur_poids, dinst_poids, dapplication_poids)"
            + " VALUES (?, ?, ?, ?, ?, ?, {fn now()}, {fn now()})";
    /** Requete d'insertion du poids d'un critere dans le calcul d'un facteur. */
    private static final String UPDATE_WEIGHT_QUERY = "UPDATE Poids_fact_crit set valeur_poids=?, dmaj_poids={fn now()}"
            + " WHERE id_fact=? AND id_crit=? AND bline_poids=? AND id_pro=? AND id_elt=?";
    /** Requete d'insertion du poids d'un critere dans le calcul d'un facteur. */
    private static final String SELECT_WEIGHT_QUERY = "SELECT bline_poids FROM Poids_fact_crit Where bline_poids=? And id_pro=? And id_fact=? And id_crit=? And id_elt=?";

    /** Mise a jour du poids du critere pour un facteur donne.
     * @param connection la connexion DB utilisee.
     * @param baseline la baseline actuelle.
     * @param projet le projet.
     * @param idElt l'identifiant de l'element concerne.
     * @param idFac l'identifiant du facteur concerne.
     * @param poids le poids du critere pour le facteur.
     */
    public void updateWeight(Critere crit, BaselineBean baseline,
            ProjectBean projet, String idElt,
            String idFac, double poids,
            Connection connection) throws DataAccessException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(SELECT_WEIGHT_QUERY);
            pstmt.setString(1, baseline.getId());
            pstmt.setString(2, projet.getId());
            pstmt.setString(3, idFac);
            pstmt.setString(4, crit.getId());
            pstmt.setString(5, idElt);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                pstmt.close();
                pstmt = connection.prepareStatement(INSERT_WEIGHT_QUERY);
                // Initialisation des parametres de la requete.
                pstmt.setString(1, idFac);
                pstmt.setString(2, crit.getId());
                pstmt.setString(3, baseline.getId());
                pstmt.setString(4, projet.getId());
                pstmt.setString(5, idElt);
                pstmt.setDouble(6, poids);
                // Execution de la requete de mise a jour.
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                pstmt = connection.prepareStatement(UPDATE_WEIGHT_QUERY);
                // Maj des donnees dans la base.
                // Initialisation des parametres de la requete.
                pstmt.setDouble(1, poids);
                pstmt.setString(2, idFac);
                pstmt.setString(3, crit.getId());
                pstmt.setString(4, baseline.getId());
                pstmt.setString(5, projet.getId());
                pstmt.setString(6, idElt);
                // Execution de la requete de mise a jour.
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {

            String reqfailed = "INSERT INTO Poids_fact_crit (id_fact, id_crit, bline_poids, id_pro, id_elt, valeur_poids, dapplication_poids)"
                    + " VALUES (" + idFac + "," + crit.getId() + ","
                    + baseline.getId() + "," + projet + "," + idElt + ","
                    + poids + ", {fn now()})";
            logger.error("ERROR:" + reqfailed);
            logger.error("Error updating Poids_fact_crit", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            //connection.setAutoCommit(true);
        }
    }

    /*
    public static void main(String[] args) {
    Connection conn = JdbcDAOUtils.getConnection(Constants.CAQS_DATASOURCE_KEY);
    BaselineBean blineBean = BaselineAccessFacade.retrieveBaselineAndProjectById("INFOCABLEV1", conn);
    ElementBean eltBean = ElementAccessFacade.retrieveElementById("20040624141936312198300", conn);
    eltBean.setBaseline(blineBean);

    CriterionDefinition criterionDef = CriterionAccessFacade.retrieveCriterionDefinitionByKey("ANTI_RECURSIVITE", eltBean.getUsage().getId(), conn);
    Collection c = CriterionAccessFacade.retrieveCriterionDetailsForSubElts(eltBean, criterionDef, 3, "%", ElementType.MET, conn);
    System.out.println(c);
    }
     */
    public double getMarkForEAToCriterion(String idElt, String idCrit, String idBline) {
        double retour = 0.0;

        String query = "SELECT note_cribl, just_note_cribl FROM critere_bline"
                + " WHERE id_elt = ? AND id_bline = ? AND id_crit = ?";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idCrit);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                double just = rs.getDouble("just_note_cribl");
                double mark = rs.getDouble("note_cribl");
                retour = (just > 0.0) ? just : mark;
            }
        } catch (SQLException e) {
            logger.error("getMarkForEAToCriterion ID_ELT=" + idElt
                    + ", ID_BLIEN=" + idBline + ", ID_CRIT=" + idCrit, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return retour;
    }

    /** {@inheritDoc}
     */
    public double getAverageMetricValueForEA(String idEa, String idBline, String idMet) {
        double retour = 0.0;

        String query = "SELECT VALBRUTE_QAMET FROM qametrique"
                + " WHERE id_bline = ? AND id_met = ? AND id_elt IN ("
                + "	SELECT id_elt FROM element WHERE id_main_elt = ?)";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idMet);
            pstmt.setString(3, idEa);
            rs = pstmt.executeQuery();
            double nbElts = 0;
            double total = 0.0;
            while (rs.next()) {
                total += rs.getDouble("VALBRUTE_QAMET");
                nbElts++;
            }
            retour = total / nbElts;
        } catch (SQLException e) {
            logger.error("getAverageMetricValueForEA ID_EA=" + idEa
                    + ", ID_BLINE=" + idBline + ", ID_MET=" + idMet, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return retour;
    }

    /**
     * @{@inheritDoc}
     */
    public void updateCommentForCriterion(ElementBean elt,
            String idCrit, String comment) throws DataAccessException {
        String query = "UPDATE critere_bline" + " SET criterion_comment = ? "
                + " WHERE id_elt = ? and id_bline = ? AND id_crit = ?";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, comment);
            pstmt.setString(2, elt.getId());
            pstmt.setString(3, elt.getBaseline().getId());
            pstmt.setString(4, idCrit);
            int nb = pstmt.executeUpdate();
            if (nb > 0) {
                dataCache.clearCache(elt.getBaseline().getId());
            } else {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("updateCommentForCriterion idElt=" + elt.getId()
                    + ", ID_BLINE=" + elt.getBaseline().getId() + ", id_crit="
                    + idCrit, e);
            throw new DataAccessException("Nothing updated");
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    public List<CriterionDefinition> retrieveCriterionsByIdLibGoalModel(String id, String lib, String goal, String model, String idLang) {
        StringBuffer query = new StringBuffer("SELECT c.id_crit ");
        StringBuffer fromClause = new StringBuffer(" FROM Critere c");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(c.id_crit) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            fromClause = fromClause.append(" , I18N libI18n ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" libI18n.table_name = 'critere' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLang).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = c.id_crit");
            alreadyOneClause = true;
        }

        if (!"%".equals(goal)) {
            //il y a une recherche sur les objectifs associés
            fromClause = fromClause.append(" , Facteur_critere fc ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" c.id_crit = fc.id_crit ").append(" AND fc.id_fact IN ( ").append(" SELECT DISTINCT id_table FROM I18N ").append(" WHERE table_name = 'facteur' ").append(" AND column_name = 'lib' ").append(" AND id_langue = '").append(idLang).append("' ").append(" AND lower(text) like '").append(goal.toLowerCase()).append("' )");
            alreadyOneClause = true;
        }

        if (!"%".equals(model)) {
            //il y a une recherche sur les objectifs associés
            fromClause = fromClause.append(" , critere_usage cu ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" c.id_crit = cu.id_crit ").append(" AND cu.id_usa IN ( ").append(" SELECT DISTINCT id_table FROM I18N ").append(" WHERE table_name = 'modele' ").append(" AND column_name = 'lib' ").append(" AND id_langue = '").append(idLang).append("' ").append(" AND lower(text) like '").append(model.toLowerCase()).append("' )");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<CriterionDefinition> result = new ArrayList<CriterionDefinition>();
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
                CriterionDefinition newCriterion = new CriterionDefinition();
                newCriterion.setId(rs.getString("id_crit"));
                result.add(newCriterion);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving criterions", e);
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
    public void deleteCriterionBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_CRITERION_QUERY);
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

    /**
     * @{@inheritDoc }
     */
    public void saveCriterionBean(CriterionDefinition criterion) throws DataAccessException {
        PreparedStatement update = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (retrieveCriterionDefinitionByKey(criterion.getId()) == null) {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_CRITERION_QUERY);
                stmt.setString(1, criterion.getId());
                stmt.setTimestamp(2, criterion.getDapplication());
                stmt.setTimestamp(3, criterion.getDinst());
                stmt.setTimestamp(4, criterion.getDmaj());
                stmt.setTimestamp(5, criterion.getDperemption());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } else {
                stmt = connection.prepareStatement(UPDATE_CRITERION_DATE_QUERY);
                stmt.setTimestamp(1, criterion.getDapplication());
                stmt.setTimestamp(2, criterion.getDinst());
                stmt.setTimestamp(3, criterion.getDmaj());
                stmt.setTimestamp(4, criterion.getDperemption());
                stmt.setString(5, criterion.getId());
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
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(update);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public ModelEditorCriterionBean retrieveCriterionByIdWithAssociatedModelsCount(String id) {
        ModelEditorCriterionBean result = this.retrieveModelEditorCriterionBeanByKey(id);
        if (result != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                // Preparation de la requete.
                stmt = connection.prepareStatement(RETRIEVE_NUMBER_OF_ASSOCIATED_MODELS);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    int nb = rs.getInt("nb");
                    result.setNbModelsAssociated(nb);
                }
            } catch (SQLException e) {
                logger.error("Error retrieving criterion", e);
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
     * retrieves a CriterionBean by id, for the model editor
     * @param idCrit id
     * @return criterionBean
     */
    public ModelEditorCriterionBean retrieveModelEditorCriterionBeanByKey(String idCrit) {
        ModelEditorCriterionBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CRITERION_BY_ID_REQUEST);
            pstmt.setString(1, idCrit);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ModelEditorCriterionBean();
                result.setId(rs.getString(1));
                result.setDapplication(rs.getTimestamp("dapplication_crit"));
                result.setDmaj(rs.getTimestamp("dmaj_crit"));
                result.setDperemption(rs.getTimestamp("dperemption_crit"));
                result.setDinst(rs.getTimestamp("dinst_crit"));
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

    /**
     * @{@inheritDoc }
     */
    public void updateCriterionWeightForGoal(String idCrit, String idFact,
            String idUsa, int weight) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(UPDATE_CRITERION_WEIGHT_FOR_GOAL);
            stmt.setInt(1, weight);
            stmt.setString(2, idCrit);
            stmt.setString(3, idFact);
            stmt.setString(4, idUsa);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("Error updating weight", e);
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
    public void updateCriterionTEltForModel(String idCrit,
            String idUsa, String idTelt) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(UPDATE_CRITERION_TELT_FOR_MODEL);
            stmt.setString(1, idTelt);
            stmt.setString(2, idCrit);
            stmt.setString(3, idUsa);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("Error updating weight", e);
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
    public List<CriterionDefinition> retrieveCriterionsNotAssociatedToGoalAndModel(String idUsa, String idFact, String filterId, String filterLib, String idLoc) {
        List<CriterionDefinition> result = new ArrayList<CriterionDefinition>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            String query = "SELECT DISTINCT(id_crit) FROM Critere ";
            if (filterLib != null) {
                query += ", I18n i18n ";
            }
            query += " WHERE id_crit NOT IN " + "  (SELECT DISTINCT(id_crit) "
                    + "     FROM facteur_critere where id_usa = '" + idUsa
                    + "' AND id_fact='" + idFact + "')";
            if (filterId != null) {
                query += " AND lower(id_crit) like '%" + filterId.toLowerCase()
                        + "%'";
            }
            if (filterLib != null) {
                query += " AND i18n.table_name = 'critere' AND i18n.column_name = 'lib' AND lower(i18n.text) like '%"
                        + filterLib.toLowerCase() + "%' AND i18n.id_langue = '"
                        + idLoc + "' AND i18n.id_table=id_crit";
            }
            stmt = connection.prepareStatement(query);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                CriterionDefinition newCriterion = new CriterionDefinition();
                newCriterion.setId(rs.getString("id_crit"));
                result.add(newCriterion);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving criterions", e);
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
    public void associateCriterionToGoalAndModel(String idCrit, String idFact, String idUsa)
            throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(SELECT_CRITERE_USAGE);
            stmt.setString(1, idUsa);
            stmt.setString(2, idCrit);
            rs = stmt.executeQuery();
            int nb = 0;
            if (rs.next()) {
                nb = rs.getInt("nb");
            }
            if (nb == 0) {
                JdbcDAOUtils.closePrepareStatement(stmt);
                // Preparation de la requete.
                stmt = connection.prepareStatement(INSERT_CRITERE_USAGE);
                stmt.setString(1, idUsa);
                stmt.setString(2, idCrit);
                stmt.setString(3, ElementType.EA);
                // Execution de la requete.
                nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing updated");
                }
            }
        } catch (SQLException e) {
            logger.error("Error inserting critere_usage", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
        }

        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(INSERT_FACTEUR_CRITERE);
            stmt.setString(1, idUsa);
            stmt.setString(2, idFact);
            stmt.setString(3, idCrit);
            stmt.setInt(4, 1);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("Error inserting facteur_critere", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public boolean deleteCriterionFromGoalAndModel(String idCrit, String idFact, String idUsa)
            throws DataAccessException {
        boolean retour = false;
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_FACTEUR_CRITERE);
            stmt.setString(1, idUsa);
            stmt.setString(2, idFact);
            stmt.setString(3, idCrit);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error inserting facteur_critere", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }

        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(SELECT_FACTEUR_CRITERE);
            stmt.setString(1, idUsa);
            stmt.setString(2, idCrit);
            rs = stmt.executeQuery();
            int nb = 0;
            if (rs.next()) {
                nb = rs.getInt("nb");
            }
            if (nb == 0) {
                JdbcDAOUtils.closePrepareStatement(stmt);
                // Preparation de la requete.
                stmt = connection.prepareStatement(DELETE_CRITERE_USAGE);
                stmt.setString(1, idUsa);
                stmt.setString(2, idCrit);
                // Execution de la requete.
                nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing deleted");
                } else {
                    retour = true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error deleting critere_usage", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public Map<String, List<FactorDefinitionBean>> retrieveAssociatedModelsAndGoalsForCriterion(String idCrit) {
        Map<String, List<FactorDefinitionBean>> result = new HashMap<String, List<FactorDefinitionBean>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ASSOCIATED_MODELS_GOALS_FOR_CRITERION);
            stmt.setString(1, idCrit);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                String idUsa = rs.getString("id_usa");
                List<FactorDefinitionBean> factors = result.get(idUsa);
                if (factors == null) {
                    factors = new ArrayList<FactorDefinitionBean>();
                    result.put(idUsa, factors);
                }
                FactorDefinitionBean cd = new FactorDefinitionBean();
                cd.setId(rs.getString("id_fact"));
                factors.add(cd);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving models and goals", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
}

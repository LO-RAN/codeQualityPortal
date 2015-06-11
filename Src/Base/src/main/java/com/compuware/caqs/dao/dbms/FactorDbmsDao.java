/*
 * Class.java
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

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.CriterionFactorWeightBean;
import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementFactorBaseline;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Facteur;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorFactorBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class FactorDbmsDao implements FactorDao {

    private static final String RETRIEVE_NB_ASSOCIATED_MODELS = "select count(distinct id_usa) as nb "
            + " from facteur_critere " + " where id_fact = ?";
    private static final String RETRIEVE_FACTOR_BY_ID = " SELECT id_fact, dinst_fact, dapplication_fact, dperemption_fact, dmaj_fact FROM Facteur "
            + " WHERE id_fact = ?";
    private static final String CREATE_FACTOR_QUERY = " INSERT INTO Facteur(id_fact, dinst_fact, dmaj_fact, dapplication_fact, dperemption_fact) "
            + " VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_FACTOR_QUERY = " UPDATE Facteur SET dinst_fact=?, dmaj_fact=?, dapplication_fact=?, dperemption_fact=? "
            + " WHERE id_fact = ?";
    private static final String DELETE_FACTOR_QUERY = " DELETE FROM Facteur "
            + " WHERE id_fact = ?";
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String ALL_FACTOR_REQUEST = "SELECT fac.id_fact FROM Facteur fac order by fac.id_fact";
    private static final String FACTOR_BY_ID_REQUEST = "SELECT facb.id_fac, facb.note_facbl FROM Facteur_Bline facb WHERE facb.id_bline=? AND facb.id_pro=? AND facb.id_fac=? AND facb.id_elt=? order by facb.id_fac";
    private static final String FACTOR_BY_USA_REQUEST = "SELECT fc.id_fact FROM Facteur_Critere fc WHERE fc.id_usa=? order by fc.id_fact";
    private static final String DISTINCT_FACTOR_BY_USA_REQUEST = "SELECT distinct(fc.id_fact) FROM Facteur_Critere fc WHERE fc.id_usa=? order by fc.id_fact";
    private static final String FACTORS_FOR_ELT_BASELINE_REQUEST = "SELECT distinct(facb.id_fac) FROM Facteur_Bline facb WHERE facb.id_bline=? AND facb.id_pro=? AND facb.id_elt=?";
    private static final String FACTOR_BY_ELT_BASELINE_REQUEST = "SELECT facb.id_fac, facb.note_facbl, facb.FACTEUR_COMMENT FROM Facteur_Bline facb WHERE facb.id_bline=? AND facb.id_pro=? AND facb.id_elt=? order by facb.id_fac";
    private static final String FACTORS_BY_ELT_BASELINE_REQUEST = "SELECT id_fac, note_facbl, tendance FROM Facteur_Bline WHERE id_bline=? AND id_elt=?";
    private static final String FACTOR_JUST_BY_ELT_BASELINE_REQUEST = "SELECT facb.id_fac, facb.note_facbl, facb.id_label, facb.FACTEUR_COMMENT, facb.tendance FROM Facteur_Bline facb WHERE facb.id_bline=? AND facb.id_pro=? AND facb.id_elt=? order by facb.id_fac";
    private static final String FACTOR_JUST_BY_ID_ELT_BASELINE_REQUEST = "SELECT facb.id_fac, facb.note_facbl, facb.id_label, facb.FACTEUR_COMMENT FROM Facteur_Bline facb WHERE facb.id_bline=? AND facb.id_pro=? AND facb.id_elt=? AND facb.id_fac=? order by facb.id_fac";
    private static final String FACTOR_SUM_WEIGHT_REQUEST =
            "Select p.id_fact, sum(p.valeur_poids)"
            + " From Poids_Fact_Crit p, Element elt"
            + " Where p.id_elt=elt.id_main_elt" + " And elt.id_elt=?"
            + " And p.bline_poids=?" + " group by p.id_fact";
    private static final String FACTOR_CRIT_DEF_REQUEST =
            "Select DISTINCT p.id_fact, p.id_crit, p.valeur_poids"
            + " From Poids_Fact_Crit p, Element e"
            + " Where p.id_elt=e.id_main_elt" + " And e.id_elt=?"
            + " And p.bline_poids=?" + " Order by p.id_crit, p.id_fact";
    private static final String MODELS_FOR_GOAL_REQUEST = "SELECT distinct(id_usa) FROM Facteur_critere WHERE id_fact=?";

    private static FactorDao singleton = new FactorDbmsDao();
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();

    public static FactorDao getInstance() {
        return FactorDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private FactorDbmsDao() {
    }

    /**
     * @{@inheritDoc }
     */
    public Collection<FactorBean> retrieveAllFactorDefinitions() {
        Collection<FactorBean> result = new ArrayList<FactorBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_FACTOR_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean fact = new FactorBean();
                fact.setId(rs.getString("id_fact"));
                result.add(fact);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
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
    public List<FactorBean> retrieveFactorDefinitionByUsage(String idUsage) {
        List<FactorBean> result = new ArrayList<FactorBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTOR_BY_USA_REQUEST);
            pstmt.setString(1, idUsage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean fact = new FactorBean();
                fact.setId(rs.getString("id_fact"));
                result.add(fact);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
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
    public List<FactorBean> retrieveGoalsListByModel(String idModel) {
        List<FactorBean> result = new ArrayList<FactorBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(DISTINCT_FACTOR_BY_USA_REQUEST);
            pstmt.setString(1, idModel);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean fact = new FactorBean();
                fact.setId(rs.getString("id_fact"));
                result.add(fact);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
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
    public List<FactorBean> retrieveGoalsNotAssociatedToModel(String idModel, String filterId, String filterLib, String idLoc) {
        List<FactorBean> result = new ArrayList<FactorBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT distinct(id_fact) FROM Facteur ";
            if (filterLib != null) {
                query += ", I18n i18n ";
            }
            query += " WHERE id_fact not in "
                    + "   (SELECT id_fact FROM facteur_critere "
                    + "    WHERE id_usa = '" + idModel + "')";
            if (filterId != null) {
                query += " AND lower(id_fact) like '%" + filterId.toLowerCase()
                        + "%'";
            }
            if (filterLib != null) {
                query += " AND i18n.table_name = 'facteur' AND i18n.column_name = 'lib' AND lower(i18n.text) like '%"
                        + filterLib.toLowerCase() + "%' AND i18n.id_langue = '"
                        + idLoc + "' AND i18n.id_table=id_fact";
            }
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean fact = new FactorBean();
                fact.setId(rs.getString("id_fact"));
                result.add(fact);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    public static FactorBean retrieveFactorByKey(String idBline, String idPro, String idFact, String idElt, String idUsa, Connection connection) {
        FactorBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTOR_BY_ID_REQUEST);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idPro);
            pstmt.setString(3, idFact);
            pstmt.setString(4, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new FactorBean();
                result.setId(rs.getString("id_fac"));
                result.setNote(rs.getDouble("note_facbl"));
                CriterionDao criterionFacade = CriterionDbmsDao.getInstance();
                Collection critDefs = criterionFacade.retrieveCriterionDefinitionByFactor(result.getId(), idUsa);
                result.setCriterionsDefs(critDefs);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return result;
    }
    private static final String AVERAGE_FACTOR_MARK_FOR_ELEMENT_QUERY =
            "SELECT AVG(note_facbl) as avgMark FROM facteur_bline"
            + " WHERE id_elt = ? AND id_bline = ?";

    /**
     * @{inheritDoc}
     */
    public double retrieveAverageFactorMarkForElement(String idElt, String idBline) {
        double retour = 0.0;

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(AVERAGE_FACTOR_MARK_FOR_ELEMENT_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                retour = rs.getDouble("avgMark");
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    /** @{@inheritDoc }
     */
    public List<FactorBean> retrieveFactorsByElementBaseline(String idBline, String idPro, String idElt, String idUsa) {
        List<FactorBean> result = (List<FactorBean>) dataCache.loadFromCache("retrieveFactorsByElementBaseline"
                + idBline + idPro + idElt + idUsa);
        if (result == null) {
            result = new ArrayList<FactorBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            CriterionDao criterionFacade = CriterionDbmsDao.getInstance();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(FACTOR_BY_ELT_BASELINE_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idPro);
                pstmt.setString(3, idElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    FactorBean currentFactor = new FactorBean();
                    currentFactor.setId(rs.getString("id_fac"));
                    currentFactor.setNote(rs.getDouble("note_facbl"));
                    currentFactor.setComment(rs.getString("FACTEUR_COMMENT"));
                    Collection critDefs = criterionFacade.retrieveCriterionDefinitionByFactor(currentFactor.getId(), idUsa);
                    currentFactor.setCriterionsDefs(critDefs);
                    result.add(currentFactor);
                }
                dataCache.storeToCache(idBline, "retrieveFactorsByElementBaseline"
                        + idBline + idPro + idElt + idUsa, result);
            } catch (SQLException e) {
                logger.error("Error during Factor retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public FactorBeanCollection retrieveFactorsByElementBaseline(String idBline, String idPro, String idElt) {
        FactorBeanCollection result = (FactorBeanCollection) dataCache.loadFromCache("retrieveFactorsByElementBaseline"
                + idBline + idPro + idElt);
        if (result == null) {
            result = new FactorBeanCollection(idElt, idBline);
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(FACTOR_JUST_BY_ELT_BASELINE_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idPro);
                pstmt.setString(3, idElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    FactorBean currentFactor = new FactorBean();
                    currentFactor.setId(rs.getString("id_fac"));
                    currentFactor.setNote(rs.getDouble("note_facbl"));
                    currentFactor.setComment(rs.getString("FACTEUR_COMMENT"));
                    String idLabel = rs.getString("id_label");
                    if (idLabel != null) {
                        LabelBean label = new LabelBean();
                        label.setId(idLabel);
                        currentFactor.setLabel(label);
                    }
                    currentFactor.setTendance(rs.getDouble("tendance"));
                    result.add(currentFactor);
                }
                dataCache.storeToCache(idBline, "retrieveFactorsByElementBaseline"
                        + idBline + idPro + idElt, result);
            } catch (SQLException e) {
                logger.error("Error during Factor retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public FactorBean retrieveFactorAndJustByIdElementBaseline(String idBline,
            String idPro, String idElt, String idFac) {
        FactorBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTOR_JUST_BY_ID_ELT_BASELINE_REQUEST);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idPro);
            pstmt.setString(3, idElt);
            pstmt.setString(4, idFac);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new FactorBean();
                result.setId(rs.getString("id_fac"));
                result.setNote(rs.getDouble("note_facbl"));
                result.setComment(rs.getString("FACTEUR_COMMENT"));
                String idLabel = rs.getString("id_label");
                if (idLabel != null) {
                    LabelBean label = new LabelBean();
                    label.setId(idLabel);
                    result.setLabel(label);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String FACTOR_JUST_OF_SUBELTS_REQUEST =
            "SELECT facb.id_fac, facb.note_facbl, facb.id_label, elt.id_elt, elt.lib_elt, elt.desc_elt, elt.id_telt, elt.poids_elt"
            + " FROM Facteur_Bline facb, Elt_links l, Element elt"
            + " WHERE facb.id_bline=?" + " AND facb.id_pro=?"
            + " AND l.elt_pere=?" + " AND facb.id_fac=?"
            + " AND l.elt_fils=facb.id_elt" + " AND l.elt_fils=elt.id_elt"
            + " AND facb.id_elt=elt.id_elt" + " AND facb.id_pro=elt.id_pro"
            + " order by facb.id_fac";

    /**
     * @{@inheritDoc }
     */
    public Collection<FactorElementBean> retrieveFactorAndJustForSubElts(String idBline, String idPro, String idElt, String idFac) {
        Collection<FactorElementBean> result = new ArrayList<FactorElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTOR_JUST_OF_SUBELTS_REQUEST);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idPro);
            pstmt.setString(3, idElt);
            pstmt.setString(4, idFac);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorElementBean newFactor = new FactorElementBean();
                newFactor.setId(rs.getString("id_fac"));
                newFactor.setNote(rs.getDouble("note_facbl"));
                String idLabel = rs.getString("id_label");
                if (idLabel != null) {
                    LabelBean label = new LabelBean();
                    label.setId(idLabel);
                    newFactor.setLabel(label);
                }
                ElementBean eltBean = new ElementBean();
                eltBean.setId(rs.getString("id_elt"));
                eltBean.setLib(rs.getString("lib_elt"));
                eltBean.setDesc(rs.getString("desc_elt"));
                eltBean.setTypeElt(rs.getString("id_telt"));
                eltBean.setPoids(rs.getInt("poids_elt"));
                newFactor.setElement(eltBean);
                result.add(newFactor);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private Map<String, Double> retrieveFactorSumWeight(String idBline, String idElt) {
        Map<String, Double> result = new HashMap<String, Double>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTOR_SUM_WEIGHT_REQUEST);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String idFact = rs.getString(1);
                Double sumWeight = new Double(rs.getDouble(2));
                result.put(idFact, sumWeight);
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
    public List<CriterionPerFactorBean> retrieveFactorCritereDef(String idBline, String idElt) {
        List<CriterionPerFactorBean> result = new ArrayList<CriterionPerFactorBean>();
        Map<String, Double> factSumWeight = retrieveFactorSumWeight(idBline, idElt);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTOR_CRIT_DEF_REQUEST);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            String lastCrit = "";
            CriterionPerFactorBean crit = null;
            while (rs.next()) {
                String idFact = rs.getString(1);
                String idCrit = rs.getString(2);
                double weight = rs.getDouble(3);

                if (!idCrit.equals(lastCrit)) {
                    crit = new CriterionPerFactorBean();
                    crit.setId(idCrit);
                    result.add(crit);
                }

                CriterionFactorWeightBean fact = new CriterionFactorWeightBean();
                fact.setId(idFact);
                fact.setWeight(weight);
                fact.setSumWeight((Double) factSumWeight.get(idFact));

                crit.addFactor(fact);

                lastCrit = idCrit;
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
    private static final String ALL_ELT_FACTORS_QUERY =
            "Select elt.id_elt, elt.lib_elt, l.elt_pere, fb.id_bline, fb.id_fac, fb.note_facbl, f.lib_fact"
            + " From facteur_bline fb, element elt, elt_links l, facteur f"
            + " Where fb.id_elt=elt.id_elt" + " And l.type='T'"
            + " And l.elt_fils=elt.id_elt" + " And l.elt_fils=fb.id_elt"
            + " And fb.id_fac = f.id_fact"
            + " order by dinst_elt, fb.id_bline, fb.id_fac";

    public List<ElementFactorBaseline> retrieveAllElementFactor() {
        List<ElementFactorBaseline> result = new ArrayList<ElementFactorBaseline>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, ElementFactorBaseline> existingElements = new HashMap<String, ElementFactorBaseline>();
        try {
            pstmt = connection.prepareStatement(ALL_ELT_FACTORS_QUERY);
            rs = pstmt.executeQuery();
            String lastElt = null;
            String lastBline = null;
            Map<String, FactorBean> factorMap = null;
            FactorBean f = null;
            ElementFactorBaseline elt = null;
            ElementFactorBaseline father = null;
            while (rs.next()) {
                String idElt = rs.getString("id_elt");
                if (!idElt.equals(lastElt)) {
                    elt = (ElementFactorBaseline) existingElements.get(idElt);
                    if (elt == null) {
                        elt = new ElementFactorBaseline();
                        elt.setId(idElt);
                        elt.setLib(rs.getString("lib_elt"));
                        existingElements.put(idElt, elt);
                        result.add(elt);
                    }
                    father = (ElementFactorBaseline) existingElements.get(rs.getString("elt_pere"));
                    if (father != null) {
                        elt.setFather(father);
                        father.addSon(elt);
                    }
                }
                String idBline = rs.getString("id_bline");
                if (!idBline.equals(lastBline)) {
                    factorMap = new HashMap<String, FactorBean>();
                    elt.addFactor(idBline, factorMap);
                }
                f = new FactorBean();
                f.setId(rs.getString("id_fac"));
                f.setLib(rs.getString("lib_fact"));
                f.setNote(rs.getDouble("note_facbl"));
                factorMap.put(f.getId(), f);
                lastBline = idBline;
                lastElt = idElt;
            }
        } catch (SQLException e) {
            logger.error("Error during factor retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String SELECT_FACTEUR_REQUEST = "SELECT id_fac FROM FACTEUR_BLINE Where id_fac = ? AND id_bline = ? AND id_elt = ?";
    private static final String INSERT_FACTEUR_REQUEST = "INSERT INTO Facteur_BLINE (id_fac, id_bline, id_pro, id_elt, note_facbl, dinst_facbl)"
            + " VALUES (?, ?, ?, ?, ?, {fn now()})";
    private static final String UPDATE_FACTEUR_REQUEST = "UPDATE Facteur_BLINE SET note_facbl=?, dmaj_facbl={fn now()}"
            + " WHERE id_fac=? AND id_bline=? AND id_pro=? AND id_elt=?";

    /** Mise a jour des donnees du facteur dans la base de donnees.
     * @param connection la connexion DB utilisee.
     * @param baseline la baseline actuelle.
     * @param projet le projet.
     * @param idElt l'identifiant de l'element concerne.
     */
    public void updateFacteurDataBase(Collection<Facteur> factorColl,
            BaselineBean baseline, ProjectBean projet,
            String idElt, Connection connection) throws DataAccessException {
        if (factorColl != null && factorColl.size() > 0 && connection != null) {

            // Preparation des requetes de mise a jour.
            PreparedStatement selectstmt = null;
            PreparedStatement insertstmt = null;
            PreparedStatement updatestmt = null;
            ResultSet rs = null;
            try {
                connection.setAutoCommit(false);
                selectstmt = connection.prepareStatement(SELECT_FACTEUR_REQUEST);
                insertstmt = connection.prepareStatement(INSERT_FACTEUR_REQUEST);
                updatestmt = connection.prepareStatement(UPDATE_FACTEUR_REQUEST);
                Iterator<Facteur> i = factorColl.iterator();
                Facteur currentFact = null;
                while (i.hasNext()) {
                    currentFact = i.next();
                    if (currentFact.getCalculatedNote() > 0) {
                        selectstmt.setString(1, currentFact.getId());
                        selectstmt.setString(2, baseline.getId());
                        selectstmt.setString(3, idElt);
                        rs = selectstmt.executeQuery();
                        if (!rs.next()) {
                            // Insertion des donnees dans la base.
                            // Initialisation des donnees.
                            insertstmt.setString(1, currentFact.getId());
                            insertstmt.setString(2, baseline.getId());
                            insertstmt.setString(3, projet.getId());
                            insertstmt.setString(4, idElt);
                            insertstmt.setDouble(5, currentFact.getCalculatedNote());
                            // Execution de la requete.
                            insertstmt.addBatch();
                        } else {
                            // Update des donnees dans la base.
                            // Initialisation des donnees.
                            updatestmt.setDouble(1, currentFact.getCalculatedNote());
                            updatestmt.setString(2, currentFact.getId());
                            updatestmt.setString(3, baseline.getId());
                            updatestmt.setString(4, projet.getId());
                            updatestmt.setString(5, idElt);
                            // Execution de la requete.
                            updatestmt.addBatch();
                        }
                        JdbcDAOUtils.closeResultSet(rs);
                    }
                }
                insertstmt.executeBatch();
                updatestmt.executeBatch();
                JdbcDAOUtils.commit(connection);
            } catch (SQLException e) {
                // La mise a jour a echouee.
                JdbcDAOUtils.rollbackConnection(connection);
                throw new DataAccessException(e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(selectstmt);
                JdbcDAOUtils.closePrepareStatement(insertstmt);
                JdbcDAOUtils.closePrepareStatement(updatestmt);
            }
        }
    }
    private static final String RETRIEVE_ELEMENT_FACTOR_BY_USAGE_QUERY =
            "Select distinct f.id_fact" + " From Facteur f, Facteur_Critere fc"
            + " Where fc.id_usa=?" + " And fc.id_fact=f.id_fact"
            + " And fc.id_fact in (SELECT distinct fb.id_fac FROM facteur_bline fb WHERE fb.id_bline = ? And fb.id_elt = ?)"
            + " order by f.id_fact";
    private static final String RETRIEVE_SUB_ELEMENT_FACTOR_QUERY =
            "Select distinct f.id_fact"
            + " From Facteur f, Facteur_bline fb, Elt_links l"
            + " Where fb.id_fac=f.id_fact" + " And fb.id_elt = l.elt_fils"
            + " And fb.id_bline = ?" + " And l.elt_pere = ?"
            + " order by f.id_fact";

    /** @{@inheritDoc }
     */
    public List<FactorBean> retrieveFactorList(ElementBean eltBean) throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        List<FactorBean> result = new ArrayList<FactorBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (eltBean.getTypeElt().equals(ElementType.EA)) {
                pstmt = conn.prepareStatement(RETRIEVE_ELEMENT_FACTOR_BY_USAGE_QUERY);
                UsageBean usageBean = eltBean.getUsage();
                pstmt.setString(1, usageBean.getId());
                pstmt.setString(2, eltBean.getBaseline().getId());
                pstmt.setString(3, eltBean.getId());
            } else {
                pstmt = conn.prepareStatement(RETRIEVE_SUB_ELEMENT_FACTOR_QUERY);
                BaselineBean blineBean = eltBean.getBaseline();
                pstmt.setString(1, blineBean.getId());
                pstmt.setString(2, eltBean.getId());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean factor = new FactorBean();
                factor.setId(rs.getString(1));
                result.add(factor);
            }
        } catch (SQLException e) {
            logger.error("Error during factor retrieve.", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public List<FactorBean> retrieveAllFactorsForElements(List<ElementBean> elements) {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        List<FactorBean> result = new ArrayList<FactorBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        if (!elements.isEmpty()) {
            String query = "Select distinct fc.id_fact"
                    + " From Facteur_Critere fc"
                    + " Where fc.id_usa IN (select distinct id_usa from element where id_elt IN (";
            boolean first = true;
            for (ElementBean elt : elements) {
                if (!first) {
                    query += ", ";
                }
                query += "'" + elt.getId() + "'";
                first = false;
            }
            query += "))";

            try {
                pstmt = conn.prepareStatement(query);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    FactorBean factor = new FactorBean();
                    factor.setId(rs.getString("id_fact"));
                    result.add(factor);
                }
            } catch (SQLException e) {
                logger.error("Error during factor retrieve.", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(conn);
            }
        }
        return result;
    }

    /**
     * @{@inheritDoc}
     */
    public void updateCommentForFactor(ElementBean elt,
            String idFac, String comment) throws DataAccessException {
        String query = "UPDATE facteur_bline" + " SET facteur_comment = ? "
                + " WHERE id_elt = ? and id_bline = ? AND id_fac = ?";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, comment);
            pstmt.setString(2, elt.getId());
            pstmt.setString(3, elt.getBaseline().getId());
            pstmt.setString(4, idFac);
            int nb = pstmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing to update");
            }
        } catch (SQLException e) {
            logger.error("updateCommentForFactor idElt=" + elt.getId()
                    + ", ID_BLINE=" + elt.getBaseline().getId() + ", id_fact="
                    + idFac, e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public List<FactorBean> retrieveFactorsByIdAndLib(String id, String lib, String idLoc) {
        List<FactorBean> result = new ArrayList<FactorBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            StringBuffer query = new StringBuffer("SELECT id_fact ");
            StringBuffer fromClause = new StringBuffer(" FROM Facteur f");
            StringBuffer whereClause = new StringBuffer(" WHERE ");
            boolean alreadyOneClause = false;

            if (!"%".equals(id)) {
                //il y a une recherche sur l'identifiant du critere
                whereClause = whereClause.append(" lower(f.id_fact) like '").append(id.toLowerCase()).append("' ");
                alreadyOneClause = true;
            }

            if (!"%".equals(lib)) {
                //il y a une recherche sur le lib
                fromClause = fromClause.append(" , I18N libI18n ");
                if (alreadyOneClause) {
                    whereClause = whereClause.append(" AND ");
                }
                whereClause = whereClause.append(" libI18n.table_name = 'facteur' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLoc).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = f.id_fact");
                alreadyOneClause = true;
            }

            query.append(fromClause);
            if (alreadyOneClause) {
                query.append(whereClause);
            }
            // Preparation de la requete.
            stmt = connection.prepareStatement(query.toString());
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                FactorBean newFactor = new FactorBean();
                newFactor.setId(rs.getString("id_fact"));
                result.add(newFactor);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving models", e);
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
    public void deleteFactorBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_FACTOR_QUERY);
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
    public void saveFactorBean(FactorBean goal) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (retrieveFactorById(goal.getId(), connection) == null) {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_FACTOR_QUERY);
                stmt.setString(1, goal.getId());
                stmt.setTimestamp(2, goal.getDinst());
                stmt.setTimestamp(3, goal.getDmaj());
                stmt.setTimestamp(4, goal.getDapplication());
                stmt.setTimestamp(5, goal.getDperemption());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } else {
                // Preparation de la requete.
                stmt = connection.prepareStatement(UPDATE_FACTOR_QUERY);
                stmt.setTimestamp(1, goal.getDinst());
                stmt.setTimestamp(2, goal.getDmaj());
                stmt.setTimestamp(3, goal.getDapplication());
                stmt.setTimestamp(4, goal.getDperemption());
                stmt.setString(5, goal.getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing updated");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating/updating goal", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    private FactorBean retrieveFactorById(String id, Connection conn) {
        FactorBean retour = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // Preparation de la requete.
            stmt = conn.prepareStatement(RETRIEVE_FACTOR_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                retour = new FactorBean();
                retour.setId(rs.getString("id_fact"));
            }
        } catch (SQLException e) {
            logger.error("Error creating tool", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public ModelEditorFactorBean retrieveFactorByIdWithAssociatedModelsCount(String id) {
        ModelEditorFactorBean result = this.retrieveModelEditorFactorBeanByKey(id);
        if (result != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                // Preparation de la requete.
                stmt = connection.prepareStatement(RETRIEVE_NB_ASSOCIATED_MODELS);
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
     * retrieves a ModelEditorFactorBean by id, for the model editor
     * @param idCrit id
     * @return criterionBean
     */
    public ModelEditorFactorBean retrieveModelEditorFactorBeanByKey(String idFact) {
        ModelEditorFactorBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_FACTOR_BY_ID);
            pstmt.setString(1, idFact);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ModelEditorFactorBean();
                result.setId(rs.getString(1));
                result.setDapplication(rs.getTimestamp("dapplication_fact"));
                result.setDmaj(rs.getTimestamp("dmaj_fact"));
                result.setDperemption(rs.getTimestamp("dperemption_fact"));
                result.setDinst(rs.getTimestamp("dinst_fact"));
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
    public List<FactorBean> retrieveAllFactorsForElement(String idElt, String idPro, String idBline) {
        List<FactorBean> result = (List<FactorBean>) dataCache.loadFromCache("retrieveAllFactorsForElement"
                + idBline + idPro + idElt);
        if (result == null) {
            result = new ArrayList<FactorBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(FACTORS_FOR_ELT_BASELINE_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idPro);
                pstmt.setString(3, idElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    FactorBean currentFactor = new FactorBean();
                    currentFactor.setId(rs.getString("id_fac"));
                    result.add(currentFactor);
                }
                dataCache.storeToCache(idBline, "retrieveAllFactorsForElement"
                        + idBline + idPro + idElt, result);
            } catch (SQLException e) {
                logger.error("Error during Factor retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public List<UsageBean> retrieveAllModelsAssociatedToGoal(String idGoal) {
        List<UsageBean> result = new ArrayList<UsageBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(MODELS_FOR_GOAL_REQUEST);
            pstmt.setString(1, idGoal);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UsageBean model = new UsageBean();
                model.setId(rs.getString("id_usa"));
                result.add(model);
            }
        } catch (SQLException e) {
            logger.error("Error during Factor retrieve", e);
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
    public List<FactorBean> retrieveGoalsEvolutions(String idEa, String idBline, String idPrevBline) {
        List<FactorBean> retour = new ArrayList<FactorBean>();

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(FACTORS_BY_ELT_BASELINE_REQUEST);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idEa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean goal = new FactorBean();
                goal.setId(rs.getString("id_fac"));
                goal.setNote(rs.getDouble("note_facbl"));
                goal.setTendance(rs.getDouble("tendance"));
                retour.add(goal);
            }
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);

            List<FactorBean> previousBaseline = new ArrayList<FactorBean>();
            pstmt = connection.prepareStatement(FACTORS_BY_ELT_BASELINE_REQUEST);
            pstmt.setString(1, idPrevBline);
            pstmt.setString(2, idEa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                FactorBean goal = new FactorBean();
                goal.setId(rs.getString("id_fac"));
                goal.setNote(rs.getDouble("note_facbl"));
                goal.setTendance(rs.getDouble("tendance"));
                previousBaseline.add(goal);
            }
            this.mergeFactorsLists(retour, previousBaseline);
        } catch (SQLException e) {
            logger.error("Error during Factor evolution retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        
        return retour;
    }

    private void mergeFactorsLists(List<FactorBean> baseline, List<FactorBean> previousBaseline) {
        //on place a 0 la tendance des objectifs dans baseline et non
        //dans previousBaseline. ils sont nouveaux par rapport a previousBaseline
        for(FactorBean goal : baseline) {
            int index = previousBaseline.indexOf(goal);
            if(index == -1) {
                goal.setTendance(0.0);
            } else {
                FactorBean previousGoal = previousBaseline.get(index);
                goal.setTendance(previousGoal.getNote());
            }
        }
    }
}

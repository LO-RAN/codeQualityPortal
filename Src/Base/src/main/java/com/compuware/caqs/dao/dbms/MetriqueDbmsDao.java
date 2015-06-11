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
import java.util.Set;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.DonneesBrutes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.domain.dataschemas.ScatterDataBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.MapUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class MetriqueDbmsDao implements MetriqueDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String METRIQUES_BY_ID_REQUEST = "SELECT id_met FROM Metrique WHERE id_met=?";
    private static final String DELETE_METRIC_QUERY = "DELETE FROM METRIQUE WHERE " +
            " id_met = ?";
    private static final String METRIC_TOOL_BY_ID_REQUEST = "SELECT id_met, outil_met FROM Metrique WHERE id_met=?";
    private static final String METRIQUES_BY_ID_CRIT_REQUEST =
            "SELECT r.id_met, m.outil_met" +
            " FROM Regle r, Metrique m " +
            " WHERE r.id_crit=? AND r.id_usa=? AND r.id_met = m.id_met" +
            " ORDER BY r.id_met";
    private static final String METRIQUES_BY_USA_REQUEST =
            "Select Distinct id_met" + " From Metrique" +
            " Where id_met in (Select r.id_met From Regle r Where r.id_usa=?)" +
            " order by id_met";
    private static final String CREATE_METRIC_QUERY = "INSERT INTO " +
            "METRIQUE(ID_MET, OUTIL_MET, dapplication_met, dinst_met, dmaj_met, dperemption_met)" +
            " VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_METRIC_QUERY = "UPDATE " +
            " METRIQUE SET OUTIL_MET = ?, dapplication_met=?, dinst_met=?, dmaj_met=?, dperemption_met=? " +
            " WHERE ID_MET = ?";
    private static final String CREATE_REGLE_QUERY =
            "INSERT INTO REGLE(id_crit, id_met, id_usa) VALUES(?,?,?)";
    private static final String DELETE_REGLE_QUERY =
            "DELETE FROM REGLE WHERE id_crit=? AND id_met=? AND id_usa=?";
    private static final String DELETE_METRIC_FOR_ELEMENT_AND_BALINE_QUERY =
            "DELETE FROM qametrique WHERE id_met = ? and id_bline = ? and id_elt IN (SELECT id_elt FROM element WHERE id_main_elt = ?)";
    private static final String RETRIEVE_METRIC_WITH_ASSOCIATION_COUNT_BY_ID =
            "SELECT m.id_met, m.dinst_met, m.dmaj_met, m.dapplication_met, " +
            " m.dperemption_met, m.outil_met, count(re.id_met) as nbRegle " +
            " FROM metrique m, regle re" +
            " WHERE m.id_met = ? " +
            " AND m.id_met = re.id_met " +
            " GROUP BY m.id_met, m.dinst_met, m.dmaj_met, m.dapplication_met, " +
            " m.dperemption_met, m.outil_met";
    private static final String RETRIEVE_METRIC_WITHOUT_ASSOCIATION_COUNT_BY_ID =
            "SELECT m.id_met, m.dinst_met, m.dmaj_met, m.dapplication_met, " +
            " m.dperemption_met, m.outil_met " +
            " FROM metrique m" +
            " WHERE m.id_met = ? ";
    private static final String RETRIEVE_ASSOCIATED_MODELS_AND_CRITERIONS_FOR_METRIC =
            "SELECT id_usa, id_crit FROM regle WHERE id_met = ?";

    private static MetriqueDao singleton = new MetriqueDbmsDao();

    public static MetriqueDao getInstance() {
        return MetriqueDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private MetriqueDbmsDao() {
    }

    public static MetriqueDefinitionBean retrieveMetriqueDefinitionByKey(
            String idMet, Connection connection) {
        MetriqueDefinitionBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(METRIQUES_BY_ID_REQUEST);
            pstmt.setString(1, idMet);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new MetriqueDefinitionBean();
                result.setId(rs.getString("id_met"));
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<MetriqueDefinitionBean> retrieveMetriqueDefinitionNotAssociatedToCriterionAndModelByTool(
            String idCrit, String idUsa, String toolId, String filterId, String filterLib, String idLoc) {
        List<MetriqueDefinitionBean> result = new ArrayList<MetriqueDefinitionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT distinct(id_met), outil_met" +
                    " FROM Metrique ";
            if (filterLib != null) {
                query += ", I18n i18n ";
            }

            query += " WHERE id_met NOT IN (SELECT id_met FROM Regle " +
                    " WHERE id_crit='" + idCrit + "' AND id_usa='" + idUsa +
                    "')" +
                    " AND outil_met = '" + toolId + "'";
            if (filterId != null) {
                query += " AND lower(id_met) like '%" + filterId.toLowerCase() +
                        "%'";
            }
            if (filterLib != null) {
                query += " AND i18n.table_name = 'metrique' AND i18n.column_name = 'lib' AND lower(i18n.text) like '%" +
                        filterLib.toLowerCase() + "%' AND i18n.id_langue = '" +
                        idLoc + "' AND i18n.id_table=id_met";
            }

            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MetriqueDefinitionBean metriqueDef = new MetriqueDefinitionBean();
                metriqueDef.setId(rs.getString("id_met"));
                OutilBean ob = new OutilBean();
                ob.setId(rs.getString("outil_met"));
                metriqueDef.setOutil(ob);
                result.add(metriqueDef);
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<MetriqueDefinitionBean> retrieveMetriqueDefinitionByIdCrit(
            String idCrit, String idUsa) {
        List<MetriqueDefinitionBean> result = new ArrayList<MetriqueDefinitionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(METRIQUES_BY_ID_CRIT_REQUEST);
            pstmt.setString(1, idCrit);
            pstmt.setString(2, idUsa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MetriqueDefinitionBean metriqueDef = new MetriqueDefinitionBean();
                metriqueDef.setId(rs.getString(1));
                OutilBean ob = new OutilBean();
                ob.setId(rs.getString("outil_met"));
                metriqueDef.setOutil(ob);
                result.add(metriqueDef);
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<MetriqueDefinitionBean> retrieveMetriqueDefinitionByUsage(
            String idUsa) {
        List<MetriqueDefinitionBean> result = new ArrayList<MetriqueDefinitionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(METRIQUES_BY_USA_REQUEST);
            pstmt.setString(1, idUsa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MetriqueDefinitionBean metriqueDef = new MetriqueDefinitionBean();
                metriqueDef.setId(rs.getString(1));
                result.add(metriqueDef);
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_ALL_METRIQUES_QUERY = "SELECT id_met FROM METRIQUE";

    /** {@inheritDoc}
     */
    public Collection<MetriqueDefinitionBean> retrieveAllMetriques() {
        Collection<MetriqueDefinitionBean> result = new ArrayList<MetriqueDefinitionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_METRIQUES_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MetriqueDefinitionBean metriqueDef = new MetriqueDefinitionBean();
                metriqueDef.setId(rs.getString(1));
                result.add(metriqueDef);
            }
        } catch (SQLException e) {
            logger.error("Error during All Metriques retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public HashMap retrieveAllMetriquesMap() {
        HashMap result = new HashMap();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_METRIQUES_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MetriqueDefinitionBean metriqueDef = new MetriqueDefinitionBean();
                metriqueDef.setId(rs.getString(1));
                result.put(metriqueDef.getId(), metriqueDef);
            }
        } catch (SQLException e) {
            logger.error("Error during All Metriques retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ALL_METRIQUES_REQUEST =
            "Select distinct qa.id_met" + " From Qametrique qa, Element elt" +
            " Where qa.id_bline = ?" + " And qa.id_elt = elt.id_elt" +
            " And elt.id_telt =?";

    /** {@inheritDoc}
     */
    public Map<String, MetriqueDefinitionBean> retrieveMetriqueDefinition(
            String idBline, String idTelt) {
        Map<String, MetriqueDefinitionBean> result = (Map) dataCache.loadFromCache("retrieveMetriqueDefinition" +
                idBline + idTelt);
        if (result == null) {
            result = new HashMap<String, MetriqueDefinitionBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(ALL_METRIQUES_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idTelt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    MetriqueDefinitionBean metriqueDef = new MetriqueDefinitionBean();
                    metriqueDef.setId(rs.getString(1));
                    result.put(metriqueDef.getId(), metriqueDef);
                }
            } catch (SQLException e) {
                logger.error("Error during Metrique retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
            dataCache.storeToCache(idBline, "retrieveMetriqueDefinition" +
                    idBline + idTelt, result);
        }
        return result;
    }
    private static final String QAMETRIQUES_BY_MET_ELT_BLINE =
            "Select qa.id_met, qa.valbrute_qamet" + " From Qametrique qa" +
            " Where qa.id_bline = ?" + " And qa.id_elt = ?" +
            " And qa.id_met = ?";

    /** {@inheritDoc}
     */
    public MetriqueBean retrieveQametriqueFromMetEltBline(
            String idBline, String idMet, String idElt) {
        MetriqueBean result = new MetriqueBean();
        result.setId(idMet);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(QAMETRIQUES_BY_MET_ELT_BLINE);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idElt);
            pstmt.setString(3, idMet);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result.setId(rs.getString(1));
                result.setValbrute(rs.getDouble(2));
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String AVERAGE_QAMETRIQUES_BY_MET_ELT_BLINE =
            "Select AVG(qa.valbrute_qamet) as avgqamet" + " From Qametrique qa" +
            " Where qa.id_bline = ?" +
            " And qa.id_elt in (select id_elt from element where id_pro = ?)" +
            " And qa.id_met = ?";

    /** {@inheritDoc}
     */
    public MetriqueBean retrieveAverageQametriqueFromMetEltBline(
            String idBline, String idMet, String idPro) {
        MetriqueBean result = new MetriqueBean();
        result.setId(idMet);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(AVERAGE_QAMETRIQUES_BY_MET_ELT_BLINE);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idPro);
            pstmt.setString(3, idMet);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result.setId(idMet);
                result.setValbrute(rs.getDouble("avgqamet"));
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String QAMETRIQUES_BY_METLIST_ELT_BLINE =
            "Select qa.id_elt, qa.id_met, qa.valbrute_qamet" +
            " From Qametrique qa" + " Where qa.id_bline = ?" +
            " And qa.id_met IN ";

    /** {@inheritDoc}
     */
    public Map<String, Map<String, MetriqueBean>> retrieveQametriqueFromMetListBline(
            String idBline, String idMetList) {
        Map<String, Map<String, MetriqueBean>> result = new HashMap<String, Map<String, MetriqueBean>>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(QAMETRIQUES_BY_METLIST_ELT_BLINE +
                    "'" + idMetList + "'");
            pstmt.setString(1, idBline);
            rs = pstmt.executeQuery();
            MetriqueBean metricBean = null;
            Map<String, MetriqueBean> currentMetricMap = null;
            while (rs.next()) {
                String idElt = rs.getString("id_elt");
                if (!result.containsKey(idElt)) {
                    result.put(idElt, new HashMap<String, MetriqueBean>());
                }
                currentMetricMap = result.get(idElt);
                metricBean = new MetriqueBean();
                metricBean.setId(rs.getString("id_met"));
                metricBean.setValbrute(rs.getDouble("valbrute_qamet"));
                currentMetricMap.put(metricBean.getId(), metricBean);
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String QAMETRIQUES_BY_METLIST_BLINE =
            "Select qa.id_elt, qa.id_met, qa.valbrute_qamet, qa.lignes" +
            " From Qametrique qa" + " Where qa.id_bline = ?";

    /** {@inheritDoc}
     */
    public Map<String, Map<String, MetriqueBean>> retrieveQametriqueMapFromMetListBline(
            String idBline, String metList) {
        Map<String, Map<String, MetriqueBean>> result = new HashMap<String, Map<String, MetriqueBean>>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String lastElt = "";
        String currentElt = null;
        MetriqueBean met = null;
        Map<String, MetriqueBean> metMap = null;
        String lines = null;
        try {
            logger.debug(QAMETRIQUES_BY_METLIST_BLINE + metList +
                    " order by qa.id_elt");
            pstmt = connection.prepareStatement(QAMETRIQUES_BY_METLIST_BLINE +
                    metList + " order by qa.id_elt");
            pstmt.setString(1, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                currentElt = rs.getString("id_elt");
                if (!currentElt.equals(lastElt) || metMap == null) {
                    metMap = new HashMap<String, MetriqueBean>();
                    result.put(currentElt, metMap);
                }
                met = new MetriqueBean();
                met.setId(rs.getString("id_met"));
                met.setValbrute(rs.getDouble("valbrute_qamet"));
                lines = rs.getString("lignes");
                if (lines != null && lines.length() > 0) {
                    met.setLines(lines.split(","));
                }
                metMap.put(met.getId(), met);
                lastElt = currentElt;
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String SCATTERPLOT_REQUEST = "Select elt.id_elt, elt.lib_elt, elt.desc_elt, qa.id_met, qa.valbrute_qamet" +
            " From Qametrique qa, Element elt" + " Where qa.id_bline = ?" +
            " And qa.id_elt = elt.id_elt" + " And id_main_elt = ?" +
            " And qa.id_met in (?, ?)" + " And elt.id_telt= ?" +
            " order by elt.desc_elt";
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();

    /** {@inheritDoc}
     */
    public List<ScatterDataBean> retrieveScatterPlot(ElementBean ea, String metH, String metV, String idTelt) {
        List<ScatterDataBean> result = (List<ScatterDataBean>) dataCache.loadFromCache("retrieveScatterPlot" +
                ea.getBaseline().getId() + ea.getId() + metH + metV + idTelt);
        if (result == null) {
            result = new ArrayList<ScatterDataBean>();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            double maxMetH = 10.0;
            double maxMetV = 10.0;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                BaselineBean bline = ea.getBaseline();
                StringBuffer request = new StringBuffer(SCATTERPLOT_REQUEST);
                logger.debug("Request=" + request.toString());
                pstmt = connection.prepareStatement(request.toString());
                pstmt.setString(1, bline.getId());
                pstmt.setString(2, ea.getId());
                pstmt.setString(3, metH);
                pstmt.setString(4, metV);
                pstmt.setString(5, idTelt);
                rs = pstmt.executeQuery();
                String lastElt = "";
                ScatterDataBean bean = null;
                while (rs.next()) {
                    String actualIdElt = rs.getString(1);
                    if (!lastElt.equals(actualIdElt)) {
                        if (bean != null) {
                            result.add(bean);
                        }
                        bean = new ScatterDataBean(bline, actualIdElt, rs.getString(2), rs.getString(3));
                    }

                    String idMet = rs.getString(4);
                    double valbrute = rs.getDouble(5);
                    if (idMet.equals(metH)) {
                        if (valbrute > maxMetH) {
                            maxMetH = valbrute;
                        }
                        bean.setMetH("" + valbrute);
                    }
                    if (idMet.equals(metV)) {
                        if (valbrute > maxMetV) {
                            maxMetV = valbrute;
                        }
                        bean.setMetV("" + valbrute);
                    }
                    lastElt = actualIdElt;
                }
            } catch (SQLException e) {
                logger.error("Error during Metrique retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
            addInitValues(result, maxMetH, maxMetV);
            dataCache.storeToCache(ea.getBaseline().getId(), "retrieveScatterPlot" +
                    ea.getBaseline().getId() + ea.getId() + metH + metV + idTelt, result);
        }
        return result;
    }

    private static void addInitValues(List<ScatterDataBean> coll,
            double maxMetH, double maxMetV) {
        ScatterDataBean bean = new ScatterDataBean(null, "MAX", "Max", "Max");
        bean.setMetH("" + maxMetH);
        bean.setMetV("" + maxMetV);
        coll.add(0, bean);
    }
    private static final String METRIQUE_SELECT_FOR_UPDATE = "SELECT id_elt, VALBRUTE_QAMET, LIGNES FROM QAMETRIQUE" +
            " WHERE ID_ELT=? AND ID_BLINE=? AND ID_MET=?";
    private static final String METRIQUE_INSERT = "INSERT INTO QAMETRIQUE (ID_ELT,ID_BLINE,ID_MET,VALBRUTE_QAMET,LIGNES)" +
            " VALUES(?,?,?,?,?)";
    private static final String METRIQUE_UPDATE = "UPDATE QAMETRIQUE SET VALBRUTE_QAMET=?,LIGNES=?" +
            " WHERE ID_ELT=? AND ID_BLINE=? AND ID_MET=?";

    /** {@inheritDoc}
     */
    public void setMetrique(String idElt, String idBline,
            String idMet, double valbrute) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            setMetrique(idElt, idBline, idMet, valbrute, connection, true);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error during Metrique update/insert", e);
            throw new DataAccessException("Error during Metrique update/insert", e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    public void setMetrique(String idElt, String idBline, String idMet,
            double valbrute, Connection connection, boolean doCommit)
            throws DataAccessException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(METRIQUE_SELECT_FOR_UPDATE);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idMet);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
                pstmt = connection.prepareStatement(METRIQUE_UPDATE);
                pstmt.setDouble(1, valbrute);
                pstmt.setString(2, null);
                pstmt.setString(3, idElt);
                pstmt.setString(4, idBline);
                pstmt.setString(5, idMet);
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                pstmt = connection.prepareStatement(METRIQUE_INSERT);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBline);
                pstmt.setString(3, idMet);
                pstmt.setDouble(4, valbrute);
                pstmt.setString(5, null);
                pstmt.executeUpdate();
            }
            if (doCommit) {
                JdbcDAOUtils.commit(connection);
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique update/insert", e);
            throw new DataAccessException("Error during Metrique update/insert", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }

    /** {@inheritDoc}
     */
    public void setMetrique(ElementMetricsBean eltBean, String idBline,
            HashMap existingMetrics, UpdatePolicy updatePolicy) throws DataAccessException {
        setMetrique(eltBean, eltBean.getMetricCollection(), idBline, existingMetrics, updatePolicy);
    }

    /** {@inheritDoc}
     */
    public void setMetrique(ElementBean eltBean, Collection metList,
            String idBline, Map existingMetrics, UpdatePolicy updatePolicy) throws DataAccessException {
        if (eltBean != null && eltBean.getId() != null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmtSelect = null;
            PreparedStatement pstmtUpdate = null;
            PreparedStatement pstmtInsert = null;
            ResultSet rs = null;

            try {
                connection.setAutoCommit(false);
                pstmtSelect = connection.prepareStatement(METRIQUE_SELECT_FOR_UPDATE);
                pstmtUpdate = connection.prepareStatement(METRIQUE_UPDATE);
                pstmtInsert = connection.prepareStatement(METRIQUE_INSERT);
                MetriqueBean metBean = null;
                Iterator i = metList.iterator();
                while (i.hasNext()) {
                    metBean = (MetriqueBean) i.next();
                    if (existingMetrics.get(metBean.getId()) != null) {
                        pstmtSelect.setString(1, eltBean.getId());
                        pstmtSelect.setString(2, idBline);
                        pstmtSelect.setString(3, metBean.getId());
                        rs = pstmtSelect.executeQuery();
                        if (rs.next()) {
                            pstmtUpdate.setDouble(1, getValue(rs.getDouble("VALBRUTE_QAMET"), metBean.getValbrute(), updatePolicy));
                            pstmtUpdate.setString(2, getLines(rs.getString("LIGNES"), metBean.getLinesAsString(',', Constants.MAX_LINES_SIZE), updatePolicy));
                            pstmtUpdate.setString(3, eltBean.getId());
                            pstmtUpdate.setString(4, idBline);
                            pstmtUpdate.setString(5, metBean.getId());
                            pstmtUpdate.addBatch();
                        } else {
                            pstmtInsert.setString(1, eltBean.getId());
                            pstmtInsert.setString(2, idBline);
                            pstmtInsert.setString(3, metBean.getId());
                            pstmtInsert.setDouble(4, metBean.getValbrute());
                            pstmtInsert.setString(5, metBean.getLinesAsString(',', Constants.MAX_LINES_SIZE));
                            pstmtInsert.addBatch();
                        }
                    }
                    else{
                logger.debug("Ignoring value provided for metrics "+metBean.getId()+" as there is no reference to this metrics in the database !");
                    }
                }
                pstmtInsert.executeBatch();
                pstmtUpdate.executeBatch();
                JdbcDAOUtils.commit(connection);
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                JdbcDAOUtils.rollbackConnection(connection);
                logger.error("Error during Metrique update/insert", e);
                throw new DataAccessException("Error during Metrique update/insert", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmtSelect);
                JdbcDAOUtils.closePrepareStatement(pstmtUpdate);
                JdbcDAOUtils.closePrepareStatement(pstmtInsert);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }

    private double getValue(double oldValue, double newValue, UpdatePolicy updatePolicy) {
        // By default, erase the existing value.
        double result = newValue;
        if (updatePolicy != null) {
            if (UpdatePolicy.SUM.equals(updatePolicy)) {
                result = oldValue + newValue;
            } else if (UpdatePolicy.AVG.equals(updatePolicy)) {
                result = (oldValue + newValue) / 2;
            } else if (UpdatePolicy.MIN.equals(updatePolicy)) {
                result = Math.min(oldValue, newValue);
            } else if (UpdatePolicy.MAX.equals(updatePolicy)) {
                result = Math.max(oldValue, newValue);
            }
        }
        return result;
    }

    private String getLines(String oldValue, String newValue, UpdatePolicy updatePolicy) {
        // By default, erase the existing value.
        String result = newValue;
        if (updatePolicy != null && !updatePolicy.ERASE.equals(updatePolicy) &&
                oldValue != null) {
            result = oldValue + newValue;
        }
        return result;
    }

    public void insertMetrics(Map<String, MetriqueBean> metricMap,
            String idBline) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            Set<String> keySet = metricMap.keySet();
            Iterator<String> keyIter = keySet.iterator();
            MetriqueBean currentMetric = null;
            while (keyIter.hasNext()) {
                String idElt = keyIter.next();
                currentMetric = metricMap.get(idElt);
                setMetrique(idElt, idBline, currentMetric.getId(), currentMetric.getValbrute(), connection, false);
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error during Metrique update/insert", e);
            throw new DataAccessException("Error during Metrique update/insert", e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    private void calculeVolumetrie(Map<String, Double> vol) {
        Double allCode = (Double) MapUtils.get(vol, Constants.ALL_CODE_KEYS);
        Double allCodeMet = (Double) vol.get("ALL_CODE_MET");
        Double comment = (Double) MapUtils.get(vol, Constants.COMMENT_KEYS);
        if (comment == null) {
            comment = new Double(0);
        }
        Double complexDest = (Double) vol.get("COMPLEX_DEST");
        if (allCode != null && allCode.doubleValue() != 0 &&
                comment.doubleValue() != 0) {
            double val = comment.doubleValue() / (allCode.doubleValue() +
                    comment.doubleValue()) * 100;
            vol.put("PCT_COMMENTS", new Double(val));
        } else {
            vol.put("PCT_COMMENTS", new Double(0.0));
        }
        if (allCodeMet != null && complexDest != null) {
            double val = complexDest.doubleValue() / allCodeMet.doubleValue() *
                    100;
            vol.put("PCT_COMPLEX_DEST", new Double(val));
        } else {
            vol.put("PCT_COMPLEX_DEST", new Double(0.0));
        }
    }

    /** {@inheritDoc}
     */
    public Map<String, Map<String, Double>> retrieveVolumetryMetricsEvolution(ElementBean eltBean) {
        String sqlRequest = "Select qa.id_met, qa.valbrute_qamet, qa.id_bline " +
                " From Qametrique qa, Baseline b" +
                " Where qa.id_elt=?" + " And qa.id_bline=b.id_bline" +
                " And qa.id_met in ('ALL_CODE', 'NOCL', 'LOC', 'ALL_CODE_MET', 'COMMENTS', 'CLOC', 'COMPLEX_DEST', 'IFPUG')" +
                " And b.dinst_bline <= ?" +
                " And b.dmaj_bline IS NOT NULL" +
                " And b.dpremption_bline IS NULL" +
                " order by qa.id_met";

        Map<String, Map<String, Double>> result = new HashMap<String,  Map<String, Double>>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            pstmt.setTimestamp(2, eltBean.getBaseline().getDinst());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String idBline = rs.getString("id_bline");
                Map<String, Double> blineResult = result.get(idBline);
                if(blineResult==null) {
                    blineResult = new HashMap<String, Double>();
                    result.put(idBline, blineResult);
                }
                blineResult.put(rs.getString("id_met"), rs.getDouble("valbrute_qamet"));
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de l'evolution de la Volumetrie.");
            logger.error("id_bline=" + eltBean.getBaseline().getId() +
                    ", id_elt=" + eltBean.getId(), e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        for(Map<String, Double> blineResult : result.values()) {
            calculeVolumetrie(blineResult);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Map<String, Double> retrieveVolumetryMetrics(ElementBean eltBean, String idBline) {
        String sqlRequest = "Select id_met, valbrute_qamet From Qametrique" +
                " Where id_elt=?" + " And id_bline=?" +
                " And id_met in ('ALL_CODE', 'NOCL', 'LOC', 'ALL_CODE_MET', 'COMMENTS', 'CLOC', 'COMPLEX_DEST', 'IFPUG')" +
                " order by id_met";

        Map<String, Double> result = new HashMap<String, Double>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String idMet = rs.getString(1);
                result.put(idMet, new Double(rs.getDouble(2)));
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la Volumetrie.");
            logger.error("id_bline=" + idBline +
                    ", id_elt=" + eltBean.getId(), e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        calculeVolumetrie(result);
        return result;
    }
    private static final String QAMETRIQUE_LINES_BY_ELT_BLINE_CRIT_QUERY = "Select distinct qa.id_met, qa.lignes From Qametrique qa, Regle r Where qa.id_elt=? And qa.id_bline=? And qa.lignes IS NOT NULL And qa.id_met=r.id_met And r.id_usa=? And r.id_crit=?";
    private static final String QAMETRIQUE_LINES_BY_ELT_BLINE_QUERY = "Select distinct qa.id_met, qa.lignes From Qametrique qa, Regle r Where qa.id_elt=? And qa.id_bline=? And qa.lignes IS NOT NULL And qa.id_met=r.id_met And r.id_usa=?";
    private static final String BETTER_ONLY_METRICS_WHERE_CLAUSE = " And qa.valbrute_qamet > qa.tendance";
    private static final String WORST_ONLY_METRICS_WHERE_CLAUSE = " And (qa.valbrute_qamet < qa.tendance or qa.tendance is null)";
    private static final String CHANGED_ONLY_METRICS_WHERE_CLAUSE = " And (qa.valbrute_qamet <> qa.tendance or qa.tendance is null)";
    private static final String OLD_WORST_ATTR = "oldWorst";
    private static final String OLD_BETTER_ATTR = "oldBetter";
    private static final String OLD_BETTER_WORST_ATTR = "oldBetterWorst";

    private String getTendanceWhereClause(ElementsCategory wanted) {
        String result = "";
        if (ElementsCategory.OLD_BETTER.equals(wanted)) {
            result = BETTER_ONLY_METRICS_WHERE_CLAUSE;
        } else if (ElementsCategory.OLD_WORST.equals(wanted)) {
            result = WORST_ONLY_METRICS_WHERE_CLAUSE;
        } else if (ElementsCategory.OLD_BETTER_WORST.equals(wanted)) {
            result = CHANGED_ONLY_METRICS_WHERE_CLAUSE;
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public Collection<MetriqueBean> retrieveQametriqueLines(String idElt,
            String idBline, String idCrit, String idUsa, ElementsCategory tendance) {
        Collection<MetriqueBean> result = new ArrayList<MetriqueBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (idCrit != null && idUsa != null) {
                pstmt = connection.prepareStatement(QAMETRIQUE_LINES_BY_ELT_BLINE_CRIT_QUERY +
                        getTendanceWhereClause(tendance));
            } else {
                pstmt = connection.prepareStatement(QAMETRIQUE_LINES_BY_ELT_BLINE_QUERY +
                        getTendanceWhereClause(tendance));
            }
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idUsa);
            if (idCrit != null) {
                pstmt.setString(4, idCrit);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MetriqueBean metriqueBean = new MetriqueBean();
                metriqueBean.setId(rs.getString("id_met"));
                String lines = rs.getString("lignes");
                if (lines != null && lines.length() > 0) {
                    metriqueBean.setLines(lines.split(","));
                }
                result.add(metriqueBean);
            }
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String QAMETRIQUE_LINES_UPDATE_QUERY = "Update Qametrique set lignes=? where id_elt=? and id_bline=? and id_met=?";

    /** {@inheritDoc}
     */
    public void saveQametriqueLines(String idElt, String idBline, Collection metrics) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        String lines;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(QAMETRIQUE_LINES_UPDATE_QUERY);
            Iterator i = metrics.iterator();
            MetriqueBean bean;
            while (i.hasNext()) {
                bean = (MetriqueBean) i.next();
                lines = bean.getLinesAsString(',', Constants.MAX_LINES_SIZE);
                pstmt.setString(1, lines);
                pstmt.setString(2, idElt);
                pstmt.setString(3, idBline);
                pstmt.setString(4, bean.getId());
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during Metrique lines update", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String MET_REQUEST = "Select distinct(qa.id_met) from Qametrique qa, Element elt" +
            " Where qa.id_bline = ?" + " And qa.id_elt = elt.id_elt" +
            " And elt.id_main_elt = ?" + " order by qa.id_met";

    public Collection retrieveMetriqueDefinitionCollection(String idEa, String idBline) {
        Collection result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            pstmt = connection.prepareStatement(MET_REQUEST);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idEa);
            rs = pstmt.executeQuery();
            MetriqueBean current = null;
            while (rs.next()) {
                current = new MetriqueBean();
                current.setId(rs.getString(1));
                result.add(current);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des metriques.");
            logger.error("id_bline=" + idBline + ", idEa=" + idEa, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ALL_DATA_REQUEST = "Select elt.id_elt, elt.id_telt, elt.desc_elt, met.id_met, qa.valbrute_qamet" +
            " From Qametrique qa, Element elt, Metrique met" +
            " Where qa.id_bline = ?" + " And qa.id_elt = elt.id_elt" +
            " And elt.id_main_elt = ?" + " And qa.id_met = met.id_met" +
            " order by elt.id_elt, id_met";

    public Collection retrieveAllQametriqueForEa(String idEa, String idBline) {
        Collection result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            pstmt = connection.prepareStatement(ALL_DATA_REQUEST);
            pstmt.setString(1, idBline);
            pstmt.setString(2, idEa);
            rs = pstmt.executeQuery();
            String actualElt = "";
            String oldElt = "";
            DonneesBrutes db = null;
            while (rs.next()) {
                actualElt = rs.getString("id_elt");
                if (!oldElt.equals(actualElt)) {
                    db = new DonneesBrutes(actualElt,
                            rs.getString("id_telt"), rs.getString("desc_elt"));
                    result.add(db);
                }
                db.addMetrique(rs.getString("id_met"),
                        rs.getDouble("valbrute_qamet"));
                oldElt = actualElt;
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation des valeurs brutes.");
            logger.error("id_bline=" + idBline + ", idEa=" + idEa, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    public void updateVolumetrie(String idElt, String idBline,
            Connection connection) throws DataAccessException {
        String calculRequest = "Select id_met, SUM(valbrute_qamet) From Qametrique, Elt_links" +
                " Where elt_pere = ?" + " And id_elt = elt_fils" +
                " And (id_bline = ?" +
                " Or id_bline in (Select child_id_bline From baseline_links where parent_id_bline = ? and child_id_elt = id_elt)" +
                " )" +
                " And id_met in ('ALL_CODE', 'NOCL', 'LOC', 'ALL_CODE_MET', 'COMMENTS', 'CLOC', 'COMPLEX_DEST')" +
                " group by id_met";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(calculRequest);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                setMetrique(idElt, idBline, rs.getString(1), rs.getInt(2), connection, false);
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error updating metrics", e);
            throw new DataAccessException("Error updating figures", e);
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public List<MetriqueDefinitionBean> retrieveMetricsByIdLibTool(String id, String lib, String toolId, String idLang) {
        StringBuffer query = new StringBuffer("SELECT m.id_met, m.outil_met ");
        StringBuffer fromClause = new StringBuffer(" FROM Metrique m");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(m.id_met) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            fromClause = fromClause.append(" , I18N libI18n ");
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" libI18n.table_name = 'metrique' ").append(" AND libI18n.column_name = 'lib' ").append(" AND libI18n.id_langue = '").append(idLang).append("' ").append(" AND lower(libI18n.text) like '").append(lib.toLowerCase()).append("' ").append(" AND libI18n.id_table = m.id_met");
            alreadyOneClause = true;
        }

        if (!"%".equals(toolId)) {
            //il y a une recherche sur les objectifs associés
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" m.outil_met = '").append(toolId).append("' ");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<MetriqueDefinitionBean> result = new ArrayList<MetriqueDefinitionBean>();
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
                MetriqueDefinitionBean metric = new MetriqueDefinitionBean();
                metric.setId(rs.getString("id_met"));
                String outilMet = rs.getString("outil_met");
                if (outilMet != null) {
                    OutilBean outil = new OutilBean();
                    outil.setId(outilMet);
                    metric.setOutil(outil);
                }
                result.add(metric);
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
    public MetriqueDefinitionBean retrieveMetricById(String id) {
        MetriqueDefinitionBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(METRIC_TOOL_BY_ID_REQUEST);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new MetriqueDefinitionBean();
                result.setId(rs.getString("id_met"));
                String outilMet = rs.getString("outil_met");
                if (outilMet != null) {
                    OutilBean outil = new OutilBean();
                    outil.setId(outilMet);
                    result.setOutil(outil);
                }
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
    public void deleteMetricBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_METRIC_QUERY);
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
    public void saveMetricBean(MetriqueDefinitionBean metric) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (retrieveMetriqueDefinitionByKey(metric.getId(), connection) ==
                    null) {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_METRIC_QUERY);
                stmt.setString(1, metric.getId());
                stmt.setString(2, metric.getOutil().getId());
                stmt.setTimestamp(3, metric.getDapplication());
                stmt.setTimestamp(4, metric.getDinst());
                stmt.setTimestamp(5, metric.getDmaj());
                stmt.setTimestamp(6, metric.getDperemption());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } else {
                stmt = connection.prepareStatement(UPDATE_METRIC_QUERY);
                stmt.setString(1, metric.getOutil().getId());
                stmt.setTimestamp(2, metric.getDapplication());
                stmt.setTimestamp(3, metric.getDinst());
                stmt.setTimestamp(4, metric.getDmaj());
                stmt.setTimestamp(5, metric.getDperemption());
                stmt.setString(6, metric.getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing updated");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating tool", e);
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
    public List<String> retrieveCorrectMetricsFromProvidedForModel(List<String> metrics, String idUsa) {
        List<String> retour = new ArrayList<String>();

        if (idUsa != null && !metrics.isEmpty()) {
            String query = "SELECT m.id_met FROM OUTILS_MODELE om, metrique m WHERE om.id_usa = '";
            query += idUsa +
                    "' and om.ID_OUTILS = m.OUTIL_MET and m.ID_MET in (";
            boolean first = true;
            for (String metric : metrics) {
                if (!first) {
                    query += ", ";
                }
                query += "'" + metric + "'";
                first = false;
            }
            query += ")";
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                // Preparation de la requete.
                stmt = connection.prepareStatement(query);
                // Execution de la requete.
                rs = stmt.executeQuery();
                while (rs.next()) {
                    // Parcours du resultat.
                    retour.add(rs.getString("id_met"));
                }
            } catch (SQLException e) {
                logger.error("Error retrieving criterions", e);
            } finally {
                // Fermeture du resultat et de la requete.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public void createRegle(String idMet, String idUsa, String idCrit) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(CREATE_REGLE_QUERY);
            stmt.setString(1, idCrit);
            stmt.setString(2, idMet);
            stmt.setString(3, idUsa);
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
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void deleteRegle(String idMet, String idUsa, String idCrit) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(DELETE_REGLE_QUERY);
            stmt.setString(1, idCrit);
            stmt.setString(2, idMet);
            stmt.setString(3, idUsa);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error creating tool", e);
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
    public void deleteMetricForBaselineAndElement(String idElt, String idMet, String idBline) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(DELETE_METRIC_FOR_ELEMENT_AND_BALINE_QUERY);
            stmt.setString(1, idMet);
            stmt.setString(2, idBline);
            stmt.setString(3, idElt);
            // Execution de la requete.
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting metric", e);
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
    public MetriqueDefinitionBean retrieveMetricWithAssociationCountById(String id) {
        MetriqueDefinitionBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_METRIC_WITH_ASSOCIATION_COUNT_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new MetriqueDefinitionBean();
                result.setId(rs.getString("id_met"));
                result.setDinst(rs.getTimestamp("dinst_met"));
                result.setDmaj(rs.getTimestamp("dmaj_met"));
                result.setDapplication(rs.getTimestamp("dapplication_met"));
                result.setDperemption(rs.getTimestamp("dperemption_met"));
                result.setNbCriterionsAssociated(rs.getInt("nbRegle"));
                String outilMet = rs.getString("outil_met");
                if (outilMet != null) {
                    OutilBean outil = new OutilBean();
                    outil.setId(outilMet);
                    result.setOutil(outil);
                }
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                stmt = connection.prepareStatement(RETRIEVE_METRIC_WITHOUT_ASSOCIATION_COUNT_BY_ID);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = new MetriqueDefinitionBean();
                    result.setId(rs.getString("id_met"));
                    result.setDinst(rs.getTimestamp("dinst_met"));
                    result.setDmaj(rs.getTimestamp("dmaj_met"));
                    result.setDapplication(rs.getTimestamp("dapplication_met"));
                    result.setDperemption(rs.getTimestamp("dperemption_met"));
                    result.setNbCriterionsAssociated(0);
                    String outilMet = rs.getString("outil_met");
                    if (outilMet != null) {
                        OutilBean outil = new OutilBean();
                        outil.setId(outilMet);
                        result.setOutil(outil);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving metric", e);
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
    public Map<String, List<CriterionDefinition>> retrieveAssociatedModelsAndCriterionsForMetric(String idMet) {
        Map<String, List<CriterionDefinition>> result = new HashMap<String, List<CriterionDefinition>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ASSOCIATED_MODELS_AND_CRITERIONS_FOR_METRIC);
            stmt.setString(1, idMet);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                String idUsa = rs.getString("id_usa");
                List<CriterionDefinition> criterions = result.get(idUsa);
                if (criterions == null) {
                    criterions = new ArrayList<CriterionDefinition>();
                    result.put(idUsa, criterions);
                }
                CriterionDefinition cd = new CriterionDefinition();
                cd.setId(rs.getString("id_crit"));
                criterions.add(cd);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving models and criterions", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
}

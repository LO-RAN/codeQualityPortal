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
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.evolutions.EvolutionBaselineBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cwfr-fdubois
 */
public class BaselineDbmsDao implements BaselineDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    public static final String BASELINE_BY_ID_REQUEST = "SELECT id_bline, lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre FROM Baseline WHERE id_bline=?";
    public static final String LAST_BASELINE_REQUEST = "SELECT id_bline, lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre FROM Baseline WHERE pro_blinre=? order by dinst_bline desc";
    private static final String GET_NB_BASELINE_LINK = "SELECT count(child_id_elt) as nb FROM BASELINE_LINKS WHERE parent_id_bline = ? AND child_id_bline = ? AND child_id_elt = ?";
    private static BaselineDao singleton = new BaselineDbmsDao();

    public static BaselineDao getInstance() {
        return BaselineDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private BaselineDbmsDao() {
    }
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();

    /** {@inheritDoc}
     */
    public BaselineBean retrieveBaselineById(java.lang.String id) {
        BaselineBean result = (BaselineBean) dataCache.loadFromCache("BBID" + id);
        if (result == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(BASELINE_BY_ID_REQUEST);
                pstmt.setString(1, id);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = new BaselineBean();
                    result.setId(rs.getString("id_bline"));
                    result.setLib(rs.getString("lib_bline"));
                    result.setDesc(rs.getString("desc_bline"));
                    result.setDinst(rs.getTimestamp("dinst_bline"));
                    result.setDmaj(rs.getTimestamp("dmaj_bline"));
                    dataCache.storeToCache(id, "BBID" + id, result);
                }
            } catch (SQLException e) {
                logger.error("Error during Baseline retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    public BaselineBean retrieveLastBaseline(java.lang.String projectId) {
        BaselineBean result=null;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(LAST_BASELINE_REQUEST);
                pstmt.setString(1, projectId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = new BaselineBean();
                    result.setId(rs.getString("id_bline"));
                    result.setLib(rs.getString("lib_bline"));
                    result.setDesc(rs.getString("desc_bline"));
                    result.setDinst(rs.getTimestamp("dinst_bline"));
                    result.setDmaj(rs.getTimestamp("dmaj_bline"));
                }
            } catch (SQLException e) {
                logger.error("Error during Baseline retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        
        return result;
    }

    
    /** {@inheritDoc}
     */
    public BaselineBean retrieveBaselineAndProjectById(String id) {

        logger.debug("entering retrieveBaselineAndProjectById");

        BaselineBean result = (BaselineBean) dataCache.loadFromCache("BPBID"
                + id);
        if (result == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(BASELINE_BY_ID_REQUEST);
                logger.debug("Request : " + BASELINE_BY_ID_REQUEST);
                pstmt.setString(1, id);

                rs = pstmt.executeQuery();
                logger.debug("id baseline=" + id);
                if (rs.next()) {
                    logger.debug("baseline found");
                    result = new BaselineBean();
                    result.setId(rs.getString("id_bline"));
                    result.setLib(rs.getString("lib_bline"));
                    result.setDesc(rs.getString("desc_bline"));
                    result.setDinst(rs.getTimestamp("dinst_bline"));
                    result.setDmaj(rs.getTimestamp("dmaj_bline"));
                    String projectId = rs.getString("pro_blinre");
                    if (projectId != null) {
                        ProjectDao projectFacade = ProjectDbmsDao.getInstance();
                        result.setProject(projectFacade.retrieveProjectById(projectId));
                    }
                    dataCache.storeToCache(id, "BPBID" + id, result);
                } else {
                    logger.debug("baseline NOT found");
                }

            } catch (SQLException e) {
                logger.error("Error during Baseline retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        return result;
    }
    private static final String BASELINES_WITH_INSTANCIATION_BY_PROJECTID_REQUEST =
            "SELECT id_bline, lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre, DPREMPTION_BLINE "
            + " FROM Baseline " + " WHERE pro_blinre=? "
            + " AND lib_bline IS NOT NULL " + " order by dinst_bline DESC";

    /** {@inheritDoc}
     */
    public Collection<BaselineBean> retrieveBaselinesAndInstanciationByProjectId(String projectId) {
        Collection<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(BASELINES_WITH_INSTANCIATION_BY_PROJECTID_REQUEST);
            pstmt.setString(1, projectId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean newBaseline = new BaselineBean();
                newBaseline.setId(rs.getString("id_bline"));
                newBaseline.setLib(rs.getString("lib_bline"));
                newBaseline.setDesc(rs.getString("desc_bline"));
                newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
                newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
                result.add(newBaseline);
            }
        } catch (SQLException e) {
            logger.error("Error during Baseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String BASELINE_BY_PROJECTID_REQUEST =
            "SELECT id_bline, lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre, DPREMPTION_BLINE "
            + "FROM Baseline " + "WHERE pro_blinre=? " + "AND (lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' AND lib_bline IS NOT NULL) " + "order by dinst_bline DESC";

    /** {@inheritDoc}
     */
    public Collection<BaselineBean> retrieveValidBaselinesByProjectId(String projectId) {
        Collection<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(BASELINE_BY_PROJECTID_REQUEST);
            pstmt.setString(1, projectId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean newBaseline = new BaselineBean();
                newBaseline.setId(rs.getString("id_bline"));
                newBaseline.setLib(rs.getString("lib_bline"));
                newBaseline.setDesc(rs.getString("desc_bline"));
                newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
                newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
                result.add(newBaseline);
            }
        } catch (SQLException e) {
            logger.error("Error during Baseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
    private static final String ALL_TERMINATED_BASELINE_BY_PROJECTID_REQUEST =
            "SELECT b.id_bline, b.lib_bline, b.desc_bline, b.dinst_bline, b.dmaj_bline, b.pro_blinre "
            + "FROM Baseline b " + "WHERE b.pro_blinre=? " + "AND (b.lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' OR b.lib_bline IS NULL) AND b.id_bline NOT IN (SELECT DISTINCT id_bline FROM Caqs_Messages WHERE id_task = '"
            + TaskId.ANALYSING.toString()+"' AND (status = '"+MessageStatus.IN_PROGRESS+"' OR status = '"+MessageStatus.NOT_STARTED+"' ) )"
            + "order by dinst_bline DESC";

    /** {@inheritDoc}
     */
    public Collection<BaselineBean> retrieveAllTerminatedBaselinesByProjectId(String projectId) {
        Collection<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_TERMINATED_BASELINE_BY_PROJECTID_REQUEST);
            pstmt.setString(1, projectId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean newBaseline = new BaselineBean();
                newBaseline.setId(rs.getString("id_bline"));
                newBaseline.setLib(rs.getString("lib_bline"));
                newBaseline.setDesc(rs.getString("desc_bline"));
                newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
                newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
                result.add(newBaseline);
            }
        } catch (SQLException e) {
            logger.error("Error during Baseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private static final String ALL_BASELINE_BY_PROJECTID_REQUEST =
            "SELECT id_bline, lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre "
            + "FROM Baseline " + "WHERE pro_blinre=? " + "AND (lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' OR lib_bline IS NULL)" + "order by dinst_bline DESC";

    /** {@inheritDoc}
     */
    public Collection<BaselineBean> retrieveAllBaselinesByProjectId(String projectId) {
        Collection<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_BASELINE_BY_PROJECTID_REQUEST);
            pstmt.setString(1, projectId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean newBaseline = new BaselineBean();
                newBaseline.setId(rs.getString("id_bline"));
                newBaseline.setLib(rs.getString("lib_bline"));
                newBaseline.setDesc(rs.getString("desc_bline"));
                newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
                newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
                result.add(newBaseline);
            }
        } catch (SQLException e) {
            logger.error("Error during Baseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    /*
    private static final String ALL_BASELINE_WITH_LABEL_BY_PROJECTID_REQUEST =
    "SELECT b.id_bline, b.lib_bline, b.desc_bline, b.dinst_bline, b.dmaj_bline, b.pro_blinre, l.statut_label " +
    " FROM Baseline b, LABELLISATION l " +
    " WHERE pro_blinre = ? " +
    " AND (lib_bline <> '" + Constants.INSTANCIATION_BASELINE_SQL_NAME +
    "' OR lib_bline IS NULL)" +
    " AND l.id_pro = b.pro_blinre" +
    " AND l.id_bline = b.id_bline " +
    " ORDER BY dinst_bline DESC";

    public Collection<BaselineBean> retrieveAllBaselinesByProjectId(String projectId) {
    Collection<BaselineBean> result = new ArrayList<BaselineBean>();
    Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
    pstmt = connection.prepareStatement(ALL_BASELINE_BY_PROJECTID_REQUEST);
    pstmt.setString(1, projectId);
    rs = pstmt.executeQuery();
    while (rs.next()) {
    BaselineBean newBaseline = new BaselineBean();
    newBaseline.setId(rs.getString("id_bline"));
    newBaseline.setLib(rs.getString("lib_bline"));
    newBaseline.setDesc(rs.getString("desc_bline"));
    newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
    newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
    result.add(newBaseline);
    }
    } catch (SQLException e) {
    logger.error("Error during Baseline retrieve", e);
    } finally {
    JdbcDAOUtils.closeResultSet(rs);
    JdbcDAOUtils.closePrepareStatement(pstmt);
    JdbcDAOUtils.closeConnection(connection);
    }
    return result;
    }*/
    private static final String BASELINES_FOR_IDELT_ORDER_DINST_DESC =
            "SELECT distinct(b.id_bline), lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre "
            + " FROM Baseline b, Facteur_bline fb " + " WHERE fb.id_elt = ? "
            + " AND fb.id_bline = b.id_bline " + " AND (lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' AND lib_bline IS NOT NULL) AND DPREMPTION_BLINE IS NULL"
            + " order by dinst_bline DESC";
    private static final String BASELINES_FOR_IDELT_ORDER_DINST_ASC =
            "SELECT distinct(b.id_bline), lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre "
            + " FROM Baseline b, Facteur_bline fb " + " WHERE fb.id_elt = ? "
            + " AND fb.id_bline = b.id_bline " + " AND (lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' AND lib_bline IS NOT NULL) AND DPREMPTION_BLINE IS NULL"
            + " order by dinst_bline ASC";

    /**
     * @{@inheritDoc }
     */
    public BaselineBean getLastBaseline(String idElt) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        BaselineBean result = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(BASELINES_FOR_IDELT_ORDER_DINST_DESC);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new BaselineBean();
                result.setId(rs.getString("id_bline"));
                result.setLib(rs.getString("lib_bline"));
                result.setDesc(rs.getString("desc_bline"));
                result.setDinst(rs.getTimestamp("dinst_bline"));
                result.setDmaj(rs.getTimestamp("dmaj_bline"));
            }
        } catch (SQLException e) {
            logger.error("Error during isLastBaseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }

    /** {@inheritDoc}
     */
    public boolean isLastBaseline(String baselineId, String idElt) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        boolean result = this.isLastBaseline(baselineId, idElt, connection);
        JdbcDAOUtils.closeConnection(connection);
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public boolean isFirstBaseline(String baselineId, String idElt) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        boolean result = false;
        String firstBline = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(BASELINES_FOR_IDELT_ORDER_DINST_ASC);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                firstBline = rs.getString("id_bline");
            }
        } catch (SQLException e) {
            logger.error("Error during isFirstBaseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        result = baselineId.equals(firstBline);
        return result;
    }

    /**
     * 
     * @param childBaseline child baseline id
     * @param parentBaseline parent baseline id
     * @param idElt element's id
     * @param connection connection
     * @return true si c'est une baseline liee, false sinon.
     */
    private boolean isLinkedBaseline(String childBaseline, String parentBaseline, String idElt, Connection connection) {
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(GET_NB_BASELINE_LINK);
            pstmt.setString(1, parentBaseline);
            pstmt.setString(2, childBaseline);
            pstmt.setString(3, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt("nb");
                result = (nb == 1);
            }
        } catch (SQLException e) {
            logger.error("Error during isLastBaseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }

        return result;
    }

    /**
     * @param baselineId identifiant de la baseline
     * @param idElt identifiant de l'element
     * @param connection connection
     * @return true si la baseline est bien la derniere pour l'element, false sinon
     */
    private boolean isLastBaseline(String baselineId, String idElt, Connection connection) {
        boolean result = false;
        String lastBaseline = "";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(BASELINES_FOR_IDELT_ORDER_DINST_DESC);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lastBaseline = rs.getString("id_bline");
            }
            result = baselineId.equals(lastBaseline);
            if (!result) {
                //peut être est-ce une baseline attachee
                result = this.isLinkedBaseline(baselineId, lastBaseline, idElt, connection);
            }
        } catch (SQLException e) {
            logger.error("Error during isLastBaseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }

        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public void peremptBaseline(String bline) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement("UPDATE BASELINE SET DPREMPTION_BLINE = ? WHERE ID_BLINE = ?");
            blstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            blstmt.setString(2, bline);
            blstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("peremptBaseline", e);
            throw new DataAccessException("peremptBaseline", e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String NEXT_BASELINE_FOR_PROJECT_QUERY =
            "Select b.id_bline, b.lib_bline From Baseline b"
            + " Where b.dinst_bline = ("
            + " Select min(bnext.dinst_bline) From Baseline bnext, Baseline bnew"
            + " Where bnext.pro_blinre = bnew.pro_blinre"
            + " And bnew.id_bline = ?" + " And bnext.id_bline <> bnew.id_bline"
            + " And bnext.dinst_bline > bnew.dinst_bline"
            + " And bnext.dmaj_bline is not null" + " And (bnext.lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' OR bnext.lib_bline IS NULL)" + " )" + " And b.pro_blinre = ?";

    /** {@inheritDoc}
     */
    public BaselineBean getNextBaseline(BaselineBean currentBline, ProjectBean pb) throws DataAccessException {
        BaselineBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        ResultSet rs = null;
        try {
            blstmt = connection.prepareStatement(NEXT_BASELINE_FOR_PROJECT_QUERY);
            blstmt.setString(1, currentBline.getId());
            blstmt.setString(2, pb.getId());
            rs = blstmt.executeQuery();
            if (rs.next()) {
                result = new BaselineBean();
                result.setId(rs.getString(1));
                result.setLib(rs.getString(2));
            }
        } catch (SQLException e) {
            logger.error("Error during previous baseline init", e);
            throw new DataAccessException("Error during previous baseline init", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String PREVIOUS_BASELINE_FOR_PROJECT_QUERY =
            "Select b.id_bline, b.lib_bline From Baseline b"
            + " Where b.dinst_bline = ("
            + " Select max(bold.dinst_bline) From Baseline bold, Baseline bnew"
            + " Where bold.pro_blinre = bnew.pro_blinre"
            + " And bnew.id_bline = ?" + " And bold.id_bline <> bnew.id_bline"
            + " And bold.dinst_bline < bnew.dinst_bline"
            + " And bold.dmaj_bline is not null" + " And (bold.lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' OR bold.lib_bline IS NULL)" + " )" + " And b.pro_blinre = ?";

    /** {@inheritDoc}
     */
    public BaselineBean getPreviousBaseline(BaselineBean currentBline) throws DataAccessException {
        BaselineBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        ResultSet rs = null;
        try {
            blstmt = connection.prepareStatement(PREVIOUS_BASELINE_FOR_PROJECT_QUERY);
            blstmt.setString(1, currentBline.getId());
            blstmt.setString(2, currentBline.getProject().getId());
            rs = blstmt.executeQuery();
            if (rs.next()) {
                result = new BaselineBean();
                result.setId(rs.getString(1));
                result.setLib(rs.getString(2));
            }
        } catch (SQLException e) {
            logger.error("Error during previous baseline init", e);
            throw new DataAccessException("Error during previous baseline init", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String PREVIOUS_BASELINE_FOR_ELEMENT_QUERY =
            "Select b.id_bline, b.lib_bline From Baseline b"
            + " Where b.dinst_bline = (" + " 	Select max(bold.dinst_bline)"
            + " 	From Baseline bold, Baseline bnew, Element elt"
            + " 	Where bold.pro_blinre = bnew.pro_blinre"
            + " 	And bnew.id_bline = ?" + " 	And bold.id_bline <> bnew.id_bline"
            + " 	And bold.dinst_bline < bnew.dinst_bline"
            + " 	And bold.dmaj_bline is not null" + " 	And (bold.lib_bline<>'"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME
            + "' OR bold.lib_bline IS NULL)" + " 	And bold.id_bline not in ("
            + " 		Select parent_id_bline From Baseline_links Where child_id_elt = ?"
            + " 	)" + "		And elt.id_elt = ?"
            + "		And bold.dinst_bline > elt.dinst_elt" + " )"
            + " And b.pro_blinre = ?"
            + " And b.dpremption_bline IS NULL";

    /** {@inheritDoc}
     */
    public BaselineBean getPreviousBaseline(BaselineBean currentBline, String idElt) throws DataAccessException {
        BaselineBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        ResultSet rs = null;
        try {
            blstmt = connection.prepareStatement(PREVIOUS_BASELINE_FOR_ELEMENT_QUERY);
            blstmt.setString(1, currentBline.getId());
            blstmt.setString(2, idElt);
            blstmt.setString(3, idElt);
            blstmt.setString(4, currentBline.getProject().getId());
            rs = blstmt.executeQuery();
            if (rs.next()) {
                result = new BaselineBean();
                result.setId(rs.getString(1));
                result.setLib(rs.getString(2));
            }
        } catch (SQLException e) {
            logger.error("Error during previous baseline init", e);
            throw new DataAccessException("Error during previous baseline init", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    public Timestamp getBaselineDmaj(String idBline) {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        java.sql.Timestamp result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String request = "Select dmaj_bline From Baseline"
                    + " Where id_bline = ?";
            pstmt = conn.prepareStatement(request);
            pstmt.setString(1, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getTimestamp(1);
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation de la date de Maj de la baseline "
                    + idBline + ".", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    public void delete(String idBline, String idPro, boolean split) {
        if (split) {
            deleteSplit(idBline, idPro);
        } else {
            delete(idBline, idPro);
        }
    }

    protected void delete(String idBline, String idPro) {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Statement st = null;
        try {
            st = conn.createStatement();
            Collection<String> purgeRequests = new ArrayList<String>();
            //metrics
            String qaMetrique = "DELETE FROM Qametrique where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(qaMetrique);
            //Calculus
            String poidsFactCrit = "DELETE FROM poids_fact_crit where bline_poids = '"
                    + idBline + "'";
            purgeRequests.add(poidsFactCrit);
            String critereNoteRepartitionBline = "DELETE FROM CRITERENOTEREPARTITION where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(critereNoteRepartitionBline);
            String critereBline = "DELETE FROM critere_bline where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(critereBline);
            String facteurBline = "DELETE FROM facteur_bline where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(facteurBline);
            //Architecture
            String linkEltBline = "DELETE FROM link_elt_bline where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(linkEltBline);
            String eltBlineInfo = "DELETE FROM Element_Baseline_Info where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(eltBlineInfo);
            String volumetry = "DELETE FROM Volumetry where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(volumetry);
            String copyPaste = "DELETE FROM Copy_Paste_Ref where id_bline = '"
                    + idBline + "'";
            purgeRequests.add(copyPaste);
            String actionPlancriterion = "DELETE FROM Action_Plan_Criterion WHERE id_action_plan IN "
                    + "(SELECT id_action_plan FROM Action_Plan WHERE id_bline = '"
                    + idBline + "')";
            purgeRequests.add(actionPlancriterion);
            String actionPlan = "DELETE FROM Action_Plan WHERE id_bline = '"
                    + idBline + "'";
            purgeRequests.add(actionPlan);

            Iterator<String> i = purgeRequests.iterator();
            while (i.hasNext()) {
                String request = i.next();
                logger.info("Request : " + request);
                boolean returnValue = st.execute(request);
                logger.info("Result : " + returnValue);
            }

            st.execute("DELETE FROM link_real where id_bline = '" + idBline
                    + "'");
            st.execute("DELETE FROM baseline_links where parent_id_bline = '"
                    + idBline + "'");
            st.execute("DELETE FROM baseline where id_bline ='" + idBline + "'");
            //st.execute("DELETE FROM elt_links where elt_fils in (Select distinct id_elt FROM element where id_elt not in (Select distinct id_elt From qametrique Where id_pro = '" + idPro + "') and id_telt in ('CLS','MET') and id_pro = '" + idPro + "')");
            //st.execute("DELETE FROM element where id_elt not in (Select distinct id_elt From qametrique Where id_pro = '" + idPro + "') and id_telt in ('CLS','MET') and id_pro = '" + idPro + "'");
        } catch (SQLException e) {
            logger.error("Error during baseline purge", e);
        } finally {
            JdbcDAOUtils.closeStatement(st);
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    protected void deleteSplit(String idBline, String idPro) {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Statement statementDeleteBaseline = null;
        try {
            for (int n = 0; n < 10; n++) {
                Statement st = conn.createStatement();
                Collection<String> purgeRequests = new ArrayList<String>();
                //metrics
                String qaMetrique = "DELETE FROM Qametrique where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(qaMetrique);
                //Calculus
                String poidsFactCrit = "DELETE FROM poids_fact_crit where bline_poids = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(poidsFactCrit);
                String critereNoteRepartitionBline = "DELETE FROM CRITERENOTEREPARTITION where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(critereNoteRepartitionBline);
                String critereBline = "DELETE FROM critere_bline where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(critereBline);
                String facteurBline = "DELETE FROM facteur_bline where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(facteurBline);
                //Architecture
                String linkEltBline = "DELETE FROM link_elt_bline where id_bline = '"
                        + idBline + "' And (elt_from_id like '%" + n
                        + "' Or elt_to_id like '%" + n + "')";
                purgeRequests.add(linkEltBline);
                String eltBlineInfo = "DELETE FROM Element_Baseline_Info where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(eltBlineInfo);
                String volumetry = "DELETE FROM Volumetry where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(volumetry);
                String copyPaste = "DELETE FROM Copy_Paste_Ref where id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(copyPaste);
                String actionPlancriterion = "DELETE FROM Action_Plan_Criterion WHERE id_action_plan IN "
                        + "(SELECT id_action_plan FROM Action_Plan WHERE id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "')";
                purgeRequests.add(actionPlancriterion);
                String actionPlan = "DELETE FROM Action_Plan WHERE id_bline = '"
                        + idBline + "' And id_elt like '%" + n + "'";
                purgeRequests.add(actionPlan);

                Iterator<String> i = purgeRequests.iterator();
                while (i.hasNext()) {
                    String request = i.next();
                    logger.info("Request : " + request);
                    boolean returnValue = st.execute(request);
                    logger.info("Result : " + returnValue);
                }

            }
            statementDeleteBaseline = conn.createStatement();
            statementDeleteBaseline.execute("DELETE FROM link_real where id_bline = '"
                    + idBline + "'");
            statementDeleteBaseline.execute("DELETE FROM baseline where id_bline ='"
                    + idBline + "'");
            statementDeleteBaseline.execute("DELETE FROM elt_links where elt_fils in (Select distinct id_elt FROM element where id_elt not in (Select distinct id_elt From qametrique Where id_pro = '"
                    + idPro + "') and id_telt in ('CLS','MET') and id_pro = '"
                    + idPro + "')");
            statementDeleteBaseline.execute("DELETE FROM element where id_elt not in (Select distinct id_elt From qametrique Where id_pro = '"
                    + idPro + "') and id_telt in ('CLS','MET') and id_pro = '"
                    + idPro + "'");
        } catch (SQLException e) {
            logger.error("Error during baseline purge", e);
        } finally {
            JdbcDAOUtils.closeStatement(statementDeleteBaseline);
            JdbcDAOUtils.closeConnection(conn);
        }
    }
    private static final String ALL_PREVIOUS_BASELINE_QUERY =
            "SELECT b.id_bline, b.lib_bline, b.desc_bline, b.dinst_bline, b.dmaj_bline, b.pro_blinre, coalesce(\"nb_crit\",0) as nb_crit "
            + " FROM Baseline b " + " LEFT OUTER JOIN "
            + "   (select ap.id_elt, ap.id_bline, count(id_crit) as \"nb_crit\" "
            + "    FROM Action_plan_criterion apc, Action_plan ap "
            + "     WHERE ap.id_action_plan = apc.id_action_plan "
            + "     GROUP BY ap.id_elt, ap.id_bline) nbap on nbap.id_elt = ? and nbap.id_bline = b.id_bline "
            + " WHERE dmaj_bline is not null AND dmaj_bline < ? And lib_bline <> '"
            + Constants.INSTANCIATION_BASELINE_SQL_NAME + "' "
            + " AND pro_blinre = ? AND b.id_bline IN (SELECT DISTINCT id_bline FROM facteur_bline WHERE id_elt = ?) "
            + " ORDER BY dinst_bline desc";

    /**
     * @{@inheritDoc }
     */
    public List<EvolutionBaselineBean> retrieveAllPreviousValidBaseline(String idEa, BaselineBean currentBaseline) {
        List<EvolutionBaselineBean> result = (List<EvolutionBaselineBean>) dataCache.loadFromCache("previousBaselinesFor"
                + idEa + currentBaseline.getId());
        if (result == null) {
            result = new ArrayList<EvolutionBaselineBean>();
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(ALL_PREVIOUS_BASELINE_QUERY);
                pstmt.setString(1, idEa);
                pstmt.setTimestamp(2, currentBaseline.getDmaj());
                pstmt.setString(3, currentBaseline.getProject().getId());
                pstmt.setString(4, idEa);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    EvolutionBaselineBean newBaseline = new EvolutionBaselineBean();
                    newBaseline.setId(rs.getString("id_bline"));
                    newBaseline.setLib(rs.getString("lib_bline"));
                    newBaseline.setDesc(rs.getString("desc_bline"));
                    newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
                    newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
                    newBaseline.setNbCriterionsInActionsPlan(rs.getInt("nb_crit"));
                    result.add(newBaseline);
                }
                dataCache.storeToCache(currentBaseline.getId(), "previousBaselinesFor"
                        + idEa + currentBaseline.getId(), result);
            } catch (SQLException e) {
                logger.error("Error during Baseline retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    private static final String ALL_BASELINE_QUERY = "SELECT id_bline, lib_bline, desc_bline, dinst_bline, dmaj_bline, pro_blinre FROM Baseline WHERE dmaj_bline is not null And lib_bline <> 'BaseLine d''Instanciation' order by dinst_bline";

    public List<BaselineBean> retrieveAllValidBaseline() {
        List<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_BASELINE_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean newBaseline = new BaselineBean();
                newBaseline.setId(rs.getString("id_bline"));
                newBaseline.setLib(rs.getString("lib_bline"));
                newBaseline.setDesc(rs.getString("desc_bline"));
                newBaseline.setDinst(rs.getTimestamp("dinst_bline"));
                newBaseline.setDmaj(rs.getTimestamp("dmaj_bline"));
                result.add(newBaseline);
            }
        } catch (SQLException e) {
            logger.error("Error during Baseline retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_BASELINE_LINK_QUERY =
            "Select child_id_bline From Baseline_links"
            + " Where child_id_elt = ?" + " And parent_id_bline = ?";

    /** {@inheritDoc}
     */
    public String retrieveLinkedBaseline(String idElt, String idBline)
            throws DataAccessException {
        String result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_BASELINE_LINK_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("child_id_bline");
            }
        } catch (SQLException e) {
            logger.error("Error during Baseline link retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String IS_BASELINE_ATTACHED_QUERY =
            "Select b.id_bline, b.lib_bline From Baseline_links, Baseline b"
            + " Where child_id_bline = ?" + " And PARENT_ID_BLINE = b.id_bline";

    public List<BaselineBean> isBaselineAttachedToOtherBaseline(String idBline) {
        List<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(IS_BASELINE_ATTACHED_QUERY);
            pstmt.setString(1, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean bb = new BaselineBean();
                bb.setId(rs.getString("id_bline"));
                bb.setLib(rs.getString("lib_bline"));
                result.add(bb);
            }
        } catch (SQLException e) {
            logger.error("Error during isBaselineAttachedToOtherBaseline", e);
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
    public boolean update(String baselineName, String baselineId, boolean changeDmaj) throws DataAccessException {
        boolean result = false;
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Statement statement = null;
        try {
            statement = conn.createStatement();
            String baseLineQuery = "UPDATE BASELINE SET ";
            if (changeDmaj) {
                baseLineQuery += "DMAJ_BLINE={fn now()},";
            }
            //only UNIX Extraction will give the last base line name
            baseLineQuery += "LIB_BLINE='" + getBaselineName(baselineName)
                    + "' WHERE ID_BLINE = '" + baselineId + "'";
            logger.info("updating baseline : " + baseLineQuery);
            statement.execute(baseLineQuery);
            dataCache.clearCache(baselineId);
            result = true;
        } catch (SQLException e) {
            logger.error("Error updating baseline", e);
            throw new DataAccessException("Error updating baseline: "
                    + e.getMessage(), e);
        } finally {
            JdbcDAOUtils.closeStatement(statement);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    public BaselineBean create(String projectId, String forcedId) throws DataAccessException {
        BaselineBean result = null;
        String id = IDCreator.getID();
        String baseLineQuery = "";
        if (forcedId != null) {
            id = forcedId;
        }
        baseLineQuery = "INSERT INTO BASELINE (ID_BLINE,PRO_BLINRE,DINST_BLINE) VALUES('"
                + id.toUpperCase() + "','" + projectId.toUpperCase()
                + "',{fn now()})";

        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(baseLineQuery);
            result = new BaselineBean();
            result.setId(id.toUpperCase());
            ProjectBean pb = new ProjectBean();
            pb.setId(projectId.toUpperCase());
            result.setProject(pb);
        } catch (SQLException e) {
            logger.error("Error creating baseline", e);
            throw new DataAccessException("Error creating baseline: "
                    + e.getMessage(), e);
        } finally {
            JdbcDAOUtils.closeStatement(statement);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    // dupplicated in bonita workflow environment
    // @see com.compuware.caqs.workflow.common.SuccessMessageHook
    private String getBaselineName(String name) {
        String result = name;
        if (result.matches("^[0-9]+$") && result.length() >= 12) {
            try {
                DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                Date d;
                d = df.parse(result.substring(0, 12));
                df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                result = df.format(d);
            } catch (ParseException e) {
                logger.error("Error getting baseline name", e);
            }
        }
        // replace all single quotes that would break the SQL update command
        return result.replaceAll("'", " ");
    }

    /**
     * @{@inheritDoc }
     */
    public BaselineBean getLastRealBaseline(String idElt) throws DataAccessException {
        BaselineBean bb = this.getLastBaseline(idElt);
        if (bb != null) {
            String realIdBline = this.retrieveLinkedBaseline(idElt, bb.getId());
            if (realIdBline != null) {
                bb = this.retrieveBaselineAndProjectById(realIdBline);
            }
        }
        return bb;
    }
}

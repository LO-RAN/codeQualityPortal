/*
 * ElementDbmsDao.java
 *
 * Created on 23 janvier 2004, 12:25
 */
package com.compuware.caqs.dao.dbms;

// Imports for DBMS transactions.
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.treemap.TreeMapElementBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.dao.util.QueryUtil;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementRetrieveFilterBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteElement;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class ElementDbmsDao implements ElementDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    /** Query to retrieve element data by ID. */
    private static final String ELEMENT_BY_ID_REQUEST =
            "SELECT id_elt, lib_elt, desc_elt, id_telt, id_pro, info2, id_usa"
            + " FROM Element e" + " WHERE id_elt = ?";
    private static final String APPLICATION_ENTITY_BY_ELEMENT_ID_REQUEST =
            "SELECT id_elt, lib_elt, desc_elt, id_telt, id_pro, info2"
            + " FROM Element e" + " WHERE id_elt = ("
            + "   SELECT id_main_elt FROM Element WHERE id_elt = ?)";
    private static final String RETRIEVE_NOT_PEREMPTED_ELEMENT_BY_ID_REQUEST =
            "SELECT e.id_elt, lib_elt, desc_elt, id_telt, id_pro, info2, e.id_usa,"
            + "   m.APU_LIMIT_MEDIUM, m.APU_LIMIT_LONG"
            + " FROM Element e, Modele m, Droits d" + " WHERE e.id_elt = ?"
            + " And e.dperemption IS NULL" + " And e.id_elt = d.id_elt"
            + " And d.id_profil_user = ?"
            + " And ((e.id_usa = m.id_usa AND e.id_usa IS NOT NULL) OR (e.id_usa IS NULL))";
    private static final String FATHER_REQUEST = "SELECT elt_pere FROM Elt_links WHERE elt_fils=? AND type = 'T'";
    /** Query used to retrieve subelements from a given element in a tree structure
     * with project, subproject or EA elements. No atomic elements.
     */
    private static final String SUB_ELEMENTS_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.id_usa"
            + " From Element elt, Elt_links l" + " Where elt.id_elt=l.elt_fils"
            + " And l.elt_pere=?" + " And l.type='T'" + " AND ("
            + " elt.dperemption is null OR " + " elt.dperemption > ?" + " )";
    private static final String LINK_SELECT_FOR_UPDATE = "SELECT elt_pere, elt_fils FROM Elt_links"
            + " WHERE elt_pere=?" + " AND elt_fils=?";
    private static final String RETRIEVE_EAS_USING_DIALECTE = "SELECT id_elt, lib_elt, id_pro FROM element "
            + " WHERE id_dialecte = ? AND id_telt = '" + ElementType.EA + "'";
    private static final String RETRIEVE_EAS_USING_MODEL = "SELECT id_elt, lib_elt, id_pro FROM element "
            + " WHERE id_usa = ? AND id_telt = '" + ElementType.EA + "'";
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    private static ElementDao singleton = new ElementDbmsDao();

    public static ElementDao getInstance() {
        return ElementDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private ElementDbmsDao() {
    }

    /** {@inheritDoc}
     */
    public void createBaseLine(String idPro, java.sql.Date dateInst) {
        String insertBline = "INSERT INTO BASELINE (ID_BLINE,LIB_BLINE,DESC_BLINE,PRO_BLINRE,DINST_BLINE, DMAJ_BLINE) VALUES (?,?,?,?,?,?)";
        String idBline = IDCreator.getID();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(insertBline);
            blstmt.setString(1, idBline);
            blstmt.setString(2, "BaseLine d'Instanciation");
            blstmt.setString(3, "BaseLine d'Instanciation");
            blstmt.setString(4, idPro);
            blstmt.setDate(5, dateInst);
            blstmt.setDate(6, dateInst);

            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Aucune Baseline creee :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    public void updateProject(String idPro, String libPro, String descPro, java.sql.Date dateMaj) {
        String updatePro = "UPDATE PROJET SET LIB_PRO=?, DESC_PRO=?, DMAJ_PRO=? WHERE ID_PRO=?";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        try {
            eltstmt = connection.prepareStatement(updatePro);
            eltstmt.setString(1, libPro);
            eltstmt.setString(2, descPro);
            eltstmt.setDate(3, dateMaj);
            eltstmt.setString(4, idPro);
            eltstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Aucun projet mis � jour :" + e.toString());
        } finally {
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /** {@inheritDoc}
     */
    public void generateProject(String idPro, String libPro, String descPro,
            String usagePro, int isObject, java.sql.Date dateInst,
            java.sql.Date dateMaj) {
        String insertPro = "INSERT INTO PROJET (ID_PRO, LIB_PRO, DESC_PRO, TYPE, DINST_PRO, DMAJ_PRO) VALUES (?,?,?,?,?,?)";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        try {
            eltstmt = connection.prepareStatement(insertPro);
            eltstmt.setString(1, idPro);
            eltstmt.setString(2, libPro);
            eltstmt.setString(3, descPro);
            eltstmt.setInt(4, isObject);
            eltstmt.setDate(5, dateInst);
            eltstmt.setDate(6, dateMaj);

            eltstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Aucun projet cree :" + e.toString());
        } finally {
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveNotPeremptedElementById(String id, String userId) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by id: " + id);
        try {
            pstmt = connection.prepareStatement(RETRIEVE_NOT_PEREMPTED_ELEMENT_BY_ID_REQUEST);
            pstmt.setString(1, id);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                result.setTypeElt(rs.getString("id_telt"));
                result.setDialecte(DialecteDbmsDao.retrieveDialecteByElementId(id, connection));
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectDao projectFacade = ProjectDbmsDao.getInstance();
                    result.setProject(projectFacade.retrieveProjectById(idPro));
                }
                result.setInfo2(rs.getString("info2"));
                String idUsa = rs.getString("id_usa");
                if (idUsa != null) {
                    UsageBean usage = new UsageBean();
                    usage.setId(idUsa);
                    usage.setLowerLimitLongRun(rs.getDouble("APU_LIMIT_LONG"));
                    usage.setLowerLimitMediumRun(rs.getDouble("APU_LIMIT_MEDIUM"));
                    result.setUsage(usage);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveElementById(String id) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by id: " + id);
        try {
            pstmt = connection.prepareStatement(ELEMENT_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                result.setTypeElt(rs.getString("id_telt"));
                result.setDialecte(DialecteDbmsDao.retrieveDialecteByElementId(id, connection));
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectDao projectFacade = ProjectDbmsDao.getInstance();
                    result.setProject(projectFacade.retrieveProjectById(idPro));
                }
                result.setInfo2(rs.getString("info2"));
                String idUsa = rs.getString("id_usa");
                if (idUsa != null) {
                    UsageBean usage = new UsageBean();
                    usage.setId(idUsa);
                    result.setUsage(usage);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveApplicationEntityByElementId(String id) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by id: " + id);
        try {
            pstmt = connection.prepareStatement(APPLICATION_ENTITY_BY_ELEMENT_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                result.setTypeElt(rs.getString("id_telt"));
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectDao projectFacade = ProjectDbmsDao.getInstance();
                    result.setProject(projectFacade.retrieveProjectById(idPro));
                }
                result.setInfo2(rs.getString("info2"));
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveElementByDesc(String desc, String idEA) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by desc: " + desc);
        try {
            pstmt = connection.prepareStatement(ELEMENT_SELECT_BY_DESC);
            pstmt.setString(1, desc);
            pstmt.setString(2, idEA);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                result.setTypeElt(rs.getString("id_telt"));
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveElementByLib(String lib, String idEa) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by desc: " + lib);
        try {
            pstmt = connection.prepareStatement(ELEMENT_SELECT_BY_LIB);
            pstmt.setString(1, lib);
            pstmt.setString(2, idEa);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                //result.setTypeElt(rs.getString("id_telt"));
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveFatherElement(String id) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        ElementBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by id: " + id);
        try {
            pstmt = connection.prepareStatement(FATHER_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("elt_pere"));
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String MAIN_ELEMENT_BY_ID_REQUEST =
            "SELECT main.id_elt, main.lib_elt, main.desc_elt, main.id_telt, main.id_pro, main.id_usa"
            + " FROM Element main, Element elt" + " WHERE elt.id_elt = ?"
            + " AND main.id_elt = elt.id_main_elt";

    /** {@inheritDoc}
     */
    public ElementBean retrieveMainElement(String id) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by id: " + id);
        try {
            pstmt = connection.prepareStatement(MAIN_ELEMENT_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                result.setTypeElt(rs.getString("id_telt"));
                DialecteBean db = DialecteDbmsDao.retrieveDialecteByElementId(result.getId(), connection);
                result.setDialecte(db);
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectDao projectFacade = ProjectDbmsDao.getInstance();
                    result.setProject(projectFacade.retrieveProjectById(idPro));
                }
                String idUsa = rs.getString("id_usa");
                if (idUsa != null) {
                    UsageBean usage = new UsageBean();
                    usage.setId(idUsa);
                    result.setUsage(usage);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Main Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ALL_DATA_ELEMENT_BY_ID_REQUEST = "SELECT LIB_ELT,DESC_ELT,ID_TELT,DINST_ELT,DMAJ_ELT,ID_PRO,STREAM_ELT,PVOBNAME,VOBMOUNTPOINT,MAKEFILE_DIR,SOURCE_DIR,BIN_DIR,PERIODIC_DIR,LIB,ID_USA,ID_DIALECTE,POIDS_ELT,FILEPATH,LINEPOS,INFO1,INFO2,SCM_REPOSITORY,SCM_MODULE,PROJECT_FILE_PATH from ELEMENT where ID_ELT =?";

    /** {@inheritDoc}
     */
    public ElementBean retrieveAllElementDataById(String id) {
        ElementBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve element by id: " + id);
        try {
            pstmt = connection.prepareStatement(ALL_DATA_ELEMENT_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ElementBean();
                result.setId(id);
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
                result.setTypeElt(rs.getString("id_telt"));
                result.setDinst(rs.getTimestamp("dinst_elt"));
                result.setDmaj(rs.getTimestamp("dmaj_elt"));
                result.setStreamElt(rs.getString("stream_elt"));
                result.setPVobName(rs.getString("pvobname"));
                result.setVobMountPoint(rs.getString("vobmountpoint"));
                result.setMakefileDir(rs.getString("makefile_dir"));
                result.setSourceDir(rs.getString("source_dir"));
                result.setBinDir(rs.getString("bin_dir"));
                result.setPeriodicDir(rs.getString("periodic_dir"));
                result.setLibraries(rs.getString("lib"));
                result.setPoids(rs.getInt("poids_elt"));
                result.setScmRepository(rs.getString("scm_repository"));
                result.setScmModule(rs.getString("scm_module"));
                result.setProjectFilePath(rs.getString("project_file_path"));
                result.setInfo1(rs.getString("info1"));
                result.setInfo2(rs.getString("info2"));
                ProjectBean prjBean = new ProjectBean();
                prjBean.setId(rs.getString("id_pro"));
                result.setProject(prjBean);
                String idDialecte = rs.getString("id_dialecte");
                if (idDialecte != null) {
                    result.setDialecte(DialecteDbmsDao.retrieveDialecteById(idDialecte, connection));
                }
                String idUsa = rs.getString("id_usa");
                result.setFilePath(rs.getString("filePath"));
                result.setLinePos(rs.getInt("linePos"));
                if (idUsa != null) {
                    result.setUsage(UsageDbmsDao.retrieveUsageById(idUsa, connection));
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String PROJECT_RETRIEVE_QUERY =
            "Select id_elt, lib_elt, id_pro" + " From Element"
            + " Where id_telt='" + ElementType.PRJ + "'"
            + " And dperemption is null" + " order by lib_elt";

    /** {@inheritDoc}
     */
    public Collection<ElementBean> retrieveProjects() {
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(PROJECT_RETRIEVE_QUERY);
            rs = pstmt.executeQuery();
            ElementBean elementBean = null;
            while (rs.next()) {
                elementBean = new ElementBean();
                elementBean.setId(rs.getString("id_elt"));
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setTypeElt(ElementType.PRJ);
                ProjectBean prj = new ProjectBean();
                prj.setId(rs.getString("id_pro"));
                elementBean.setProject(prj);
                result.add(elementBean);
            }
        } catch (SQLException e) {
            logger.error("Error during project retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ELEMENTS_FOR_USER_RETRIEVE_QUERY =
            "Select e.id_elt, e.lib_elt, e.id_pro" + " From Element e, Droits d"
            + " Where e.id_telt=?" + " And e.dperemption is null"
            + " And e.id_elt = d.id_elt"
            + " And d.id_profil_user = ? order by e.lib_elt";

    /** {@inheritDoc}
     */
    public Collection<ElementBean> retrieveElements(String idUser, String idTelt) {
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ELEMENTS_FOR_USER_RETRIEVE_QUERY);
            pstmt.setString(1, idTelt);
            pstmt.setString(2, idUser);
            rs = pstmt.executeQuery();
            ElementBean elementBean = null;
            while (rs.next()) {
                elementBean = new ElementBean();
                elementBean.setId(rs.getString("id_elt"));
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setTypeElt(ElementType.PRJ);
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectBean prj = new ProjectBean();
                    prj.setId(idPro);
                    elementBean.setProject(prj);
                }
                result.add(elementBean);
            }
        } catch (SQLException e) {
            logger.error("Error during project retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private Timestamp getTimestamp(Timestamp t) {
        Timestamp result = t;
        if (result == null) {
            result = new Timestamp(new Date().getTime());
        }
        return result;
    }
    private static final String ELEMENT_BY_TYPE_REQUEST = "SELECT id_elt, lib_elt, desc_elt, id_usa, id_stereotype, id_dialecte, info1, info2, source_dir"
            + " FROM Element" + " WHERE id_pro=?" + " AND id_telt=?"
            + " AND dinst_elt < ?"
            + " AND (dperemption IS NULL OR dperemption > ?)";

    /** {@inheritDoc}
     */
    public Collection<ElementBean> retrieveElementByType(String idPro,
            String idTelt, Timestamp dmajBline) {
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ProjectDao projectDao = ProjectDbmsDao.getInstance();
        ProjectBean currentProject = projectDao.retrieveProjectById(idPro);
        try {
            pstmt = connection.prepareStatement(ELEMENT_BY_TYPE_REQUEST);
            pstmt.setString(1, idPro);
            pstmt.setString(2, idTelt);
            pstmt.setTimestamp(3, getTimestamp(dmajBline));
            pstmt.setTimestamp(4, getTimestamp(dmajBline));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementBean elementBean = new ElementBean();
                elementBean.setId(rs.getString("id_elt"));
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setDesc(rs.getString("desc_elt"));
                elementBean.setInfo1(rs.getString("info1"));
                elementBean.setInfo2(rs.getString("info2"));
                elementBean.setSourceDir(rs.getString("source_dir"));
                elementBean.setTypeElt(idTelt);
                String idUsa = rs.getString("id_usa");
                if (idUsa != null) {
                    UsageBean usage = new UsageBean();
                    usage.setId(idUsa);
                    elementBean.setUsage(usage);
                }
                elementBean.setStereotype(rs.getString("id_stereotype"));
                String idDialecte = rs.getString("id_dialecte");
                if (idDialecte != null) {
                    elementBean.setDialecte(DialecteDbmsDao.retrieveDialecteById(idDialecte, connection));
                }
                elementBean.setProject(currentProject);
                result.add(elementBean);
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
    public Collection<ElementBean> retrieveElementByType(String idPro, String idTelt) {
        return retrieveElementByType(idPro, idTelt, new Timestamp(new Date().getTime()));
    }
    private static final String SUB_ELEMENTS_BY_TYPE_REQUEST =
            "Select elt.id_elt, elt.lib_elt, elt.desc_elt" + " From Element elt"
            + " Where elt.id_main_elt=?" + " And elt.id_telt=?"
            + " AND (dperemption IS NULL)";

    /** {@inheritDoc}
     */
    public Collection<ElementBean> retrieveSubElementByType(String idElt,
            String idTelt) {
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(SUB_ELEMENTS_BY_TYPE_REQUEST);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idTelt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementBean elementBean = new ElementBean();
                elementBean.setId(rs.getString("id_elt"));
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setDesc(rs.getString("desc_elt"));
                result.add(elementBean);
            }
        } catch (SQLException e) {
            logger.error("Error during Sub element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String EXISTING_ELEMENTS_QUERY =
            "Select elt.id_elt, elt.lib_elt, elt.desc_elt, elt.id_telt, elt.id_pro"
            + " From Element elt" + " Where elt.id_main_elt = ?"
            + " And elt.dperemption IS NULL";

    /** {@inheritDoc}
     */
    public Collection<ElementBean> retrieveExistingElement(String idMainElt) {
        return retrieveLinkedElement(idMainElt, EXISTING_ELEMENTS_QUERY);
    }
    /**
     * requete pour recuperer la note moyenne d'un element a tous les objectifs
     */
    private static final String RETRIEVE_MARK_FOR_GOAL =
            "SELECT note_facbl" + " FROM facteur_bline" + " WHERE id_elt = ?"
            + " AND id_bline = ?" + " AND id_fac = ?";

    /**
     * Renvoie la note moyenne de chaque objectif d'un element.
     * @param idElt l'element pour lequel recuperer la note moyenne
     * @param idFac identifiant de l'objectif
     * @param connection connection a la base
     * @return la note moyenne
     */
    private double retrieveGoalMark(TreeMapElementBean elt, String idFac,
            Connection connection) {
        double result = 0.0;
        BaselineBean baseline = null;
        try {
            baseline = BaselineDbmsDao.getInstance().getLastRealBaseline(elt.getId());
        } catch (DataAccessException ex) {
            logger.error("Error while retrieving last real baseline", ex);
        }
        if (baseline != null) {
            elt.setBaseline(baseline);

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(RETRIEVE_MARK_FOR_GOAL);
                pstmt.setString(1, elt.getId());
                pstmt.setString(2, baseline.getId());
                pstmt.setString(3, idFac);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getDouble("note_facbl");
                }
            } catch (SQLException e) {
                logger.error("Error during retrieve goal mark", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
        }
        return result;
    }
    /**
     * requete pour recuperer la note moyenne d'un element a tous les objectifs
     */
    private static final String RETRIEVE_AVERAGE_MARK =
            "SELECT AVG(note_facbl) as moyenne" + " FROM facteur_bline"
            + " WHERE id_elt = ?" + " AND id_bline = ?";

    private static final String RETRIEVE_AVERAGE_MARK_ATTACHED_BASELINE =
            "SELECT AVG(note_facbl) as moyenne" + " FROM facteur_bline"
            + " WHERE id_elt = ?" + " AND id_bline in (select child_id_bline from BASELINE_LINKS where PARENT_ID_BLINE = ?)";

    /**
     * Renvoie la note moyenne de chaque objectif d'un element.
     * @param elt l'element pour lequel recuperer la note moyenne
     * @param connection connection a la base
     * @return la note moyenne
     */
    private double retrieveAverageGoalsMark(TreeMapElementBean elt, Connection connection) {
        double result = 0.0;
        BaselineBean baseline = null;
        try {
            baseline = BaselineDbmsDao.getInstance().getLastRealBaseline(elt.getId());
        } catch (DataAccessException ex) {
            logger.error("Error while retrieving last real baseline", ex);
        }
        if (baseline != null) {
            elt.setBaseline(baseline);

            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(RETRIEVE_AVERAGE_MARK);
                pstmt.setString(1, elt.getId());
                pstmt.setString(2, baseline.getId());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getDouble("moyenne");
                }
            } catch (SQLException e) {
                logger.error("Error during retrieve average mark", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
            if(result == 0.0) {
                try {
                    pstmt = connection.prepareStatement(RETRIEVE_AVERAGE_MARK_ATTACHED_BASELINE);
                    pstmt.setString(1, elt.getId());
                    pstmt.setString(2, baseline.getId());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        result = rs.getDouble("moyenne");
                    }
                } catch (SQLException e) {
                    logger.error("Error during retrieve average mark", e);
                } finally {
                    JdbcDAOUtils.closeResultSet(rs);
                    JdbcDAOUtils.closePrepareStatement(pstmt);
                }
            }
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public TreeMapElementBean retrieveMarkedTree(
            String root, String leafType, String idFac, String idUser) {
        TreeMapElementBean retour = null;
        ElementBean eb = this.retrieveElementById(root);
        if (eb != null) {
            retour = new TreeMapElementBean(eb);
            retrieveMarkedLinkedElement(retour, leafType, idFac, idUser);
        }
        return retour;
    }

    /** {@inheritDoc}
     */
    public TreeMapElementBean retrieveProjectMarkedTree(
            String root, BaselineBean baseline, String idFac, String idUser) {
        TreeMapElementBean retour = (TreeMapElementBean) dataCache.loadFromCache("retrieveProjectMarkedTree"
                + baseline.getId() + idFac + root + idUser);
        if (retour == null) {
            ElementBean eb = this.retrieveElementById(root);
            if (eb != null) {
                retour = new TreeMapElementBean(eb);
                retrieveProjectMarkedLinkedElement(retour, baseline, idFac, idUser);
            }
            dataCache.storeToCache(baseline.getId(), "retrieveProjectMarkedTree"
                    + baseline.getId() + idFac + root + idUser, retour);
        }
        return retour;
    }

    /**
     * Retrieve elements linked to the given one with the average mark.
     * @param elt the given element.
     * @param idBLine baseline id
     * @param idFac l'identifiant de l'objectif pour lequel recuperer la note ou
     * Constants.ALL_FACTORS pour la note moyenne de tous les objectifs
     * @param idUser identifiant de l'utilisateur accredite
     */
    private void retrieveProjectMarkedLinkedElement(TreeMapElementBean elt,
            BaselineBean baseline, String idFac, String idUser) {
        List<TreeMapElementBean> result = new ArrayList<TreeMapElementBean>();
        double totalMark = 0.0;
        int nbChildren = 0;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(PROJECT_ARBO_QUERY_ONE_LEVEL);
            pstmt.setString(1, elt.getProject().getId());
            pstmt.setString(2, elt.getId());
            pstmt.setString(3, baseline.getId());
            pstmt.setString(4, idUser);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String elementId = rs.getString("id_elt");
                TreeMapElementBean elementBean = new TreeMapElementBean(elementId);
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setDesc(rs.getString("desc_elt"));
                elementBean.setTypeElt(rs.getString("id_telt"));
                ProjectBean pb = new ProjectBean();
                pb.setId(rs.getString("id_pro"));
                elementBean.setProject(pb);
                if (!elementBean.getTypeElt().equals(ElementType.EA)) {
                    this.retrieveProjectMarkedLinkedElement(elementBean, baseline, idFac, idUser);
                } else {
                    double mark = 0.0;
                    if (Constants.ALL_FACTORS.equals(idFac)) {
                        mark = this.retrieveAverageGoalsMark(elementBean, connection);
                    } else {
                        mark = this.retrieveGoalMark(elementBean, idFac, connection);
                    }
                    elementBean.setMark(mark);
                }
                if (elementBean.getMark() > 0.0) {
                    totalMark += elementBean.getMark();
                    nbChildren++;
                    result.add(elementBean);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Sub element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        if (nbChildren > 0) {
            totalMark /= nbChildren;
        }
        elt.setMark(totalMark);
        elt.setChildren(result);
    }

    /**
     * Retrieve elements linked to the given one with the average mark.
     * @param elt the given element.
     * @param leafType le type de l'element feuille (apres lequel on ne
     * recherche plus)
     * @param idFac l'identifiant de l'objectif pour lequel recuperer la note ou
     * Constants.ALL_FACTORS pour la note moyenne de tous les objectifs
     * @param idUser identifiant de l'utilisateur accredite
     */
    private void retrieveMarkedLinkedElement(TreeMapElementBean elt,
            String leafType, String idFac, String idUser) {
        List<TreeMapElementBean> result = new ArrayList<TreeMapElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_CHILDREN_ELEMENTS_WITH_USER);
            pstmt.setString(1, elt.getId());
            pstmt.setString(2, idUser);
            pstmt.setTimestamp(3, new Timestamp((new Date()).getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String elementId = rs.getString("id_elt");
                TreeMapElementBean elementBean = new TreeMapElementBean(elementId);
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setDesc(rs.getString("desc_elt"));
                elementBean.setTypeElt(rs.getString("id_telt"));
                if (!elementBean.getTypeElt().equals(leafType)) {
                    this.retrieveMarkedLinkedElement(elementBean, leafType, idFac, idUser);
                } else {
                    ProjectBean pb = new ProjectBean();
                    pb.setId(rs.getString("id_pro"));
                    elementBean.setProject(pb);
                    double mark = 0.0;
                    if (Constants.ALL_FACTORS.equals(idFac)) {
                        mark = this.retrieveAverageGoalsMark(elementBean, connection);
                    } else {
                        mark = this.retrieveGoalMark(elementBean, idFac, connection);
                    }
                    elementBean.setMark(mark);
                }
                if (elementBean.getMark() > 0.0) {
                    result.add(elementBean);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Sub element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        double totalMark = 0.0;
        int nbChildren = 0;
        for (TreeMapElementBean child : result) {
            totalMark += child.getMark();
            nbChildren++;
        }
        if (nbChildren > 0) {
            totalMark /= nbChildren;
        }
        elt.setMark(totalMark);
        elt.setChildren(result);
    }
    private static final String SUB_ELEMENTS_QUERY =
            "Select elt.id_elt, elt.lib_elt, elt.desc_elt, elt.id_telt, elt.id_pro"
            + " From Element elt, Elt_links l" + " Where elt.id_elt=l.elt_fils"
            + " And l.elt_pere=?" + " And elt.dperemption IS NULL"
            + " And l.TYPE != '" + Constants.SYMBOLIC_LINK_TYPE + "'";
    private static final String SUPER_ELEMENTS_QUERY =
            "Select elt.id_elt, elt.lib_elt, elt.desc_elt, elt.id_telt, elt.id_pro"
            + " From Element elt, Elt_links l" + " Where elt.id_elt=l.elt_pere"
            + " And l.elt_fils=?" + " And elt.dperemption IS NULL"
            + " And l.TYPE != '" + Constants.SYMBOLIC_LINK_TYPE + "'";

    /** {@inheritDoc}
     */
    public Collection<ElementBean> retrieveSubElement(String idElt) {
        return retrieveLinkedElement(idElt, SUB_ELEMENTS_QUERY);
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveSuperElement(String idElt) {
        ElementBean retour = null;
        Collection<ElementBean> list = this.retrieveLinkedElement(idElt, SUPER_ELEMENTS_QUERY);
        if (list != null && !list.isEmpty()) {
            retour = list.iterator().next();
        }
        return retour;
    }

    /**
     * Retrieve elements linked to the given one.
     * @param idElt the given element.
     * @param query the query to apply to retrieve lined elements.
     * @return elements linked to the given one.
     */
    private Collection<ElementBean> retrieveLinkedElement(String idElt, String query) {
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementBean elementBean = new ElementBean();
                elementBean.setId(rs.getString("id_elt"));
                elementBean.setLib(rs.getString("lib_elt"));
                elementBean.setDesc(rs.getString("desc_elt"));
                elementBean.setTypeElt(rs.getString("id_telt"));
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectBean pb = new ProjectBean();
                    pb.setId(idPro);
                    elementBean.setProject(pb);
                }
                result.add(elementBean);
            }
        } catch (SQLException e) {
            logger.error("Error during Sub element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * Recherche les sous elements d'une entite applicative pour la gestion des stereotypes.
     * @param idEa L'identifiant de l'entite applicative mere.
     * @param filter Le filtre sur les elements recherches.
     * @return la collection des sous elements correspondant aux criteres de recherche.
     */
    public Collection<ElementBean> retrieveSubElementForStereotypeMgmt(
            String idEa, ElementRetrieveFilterBean filter) {
        Collection<ElementBean> result = new ArrayList<ElementBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        StringBuffer query = new StringBuffer("SELECT id_elt, desc_elt, id_stereotype FROM Element");
        query.append(" WHERE id_main_elt = '").append(idEa).append("'");
        if (filter.getDescElt() != null && filter.getDescElt().length() > 0
                && !filter.getDescElt().equals("%")) {
            query.append(" AND desc_elt LIKE '").append(filter.getDescElt()).append("'");
        }
        if (filter.getTypeElt() != null
                && !filter.getTypeElt().equals(ElementType.ALL)) {
            query.append(" AND id_telt = '").append(filter.getTypeElt()).append("'");
        }
        if (filter.getIdStereotype() != null && filter.getIdStereotype().length()
                > 0) {
            query.append(" AND id_stereotype = '").append(filter.getIdStereotype()).append("'");
        } else if (filter.getEmptyOnly()) {
            query.append(" AND id_stereotype IS NULL");
        }
        query.append(" ORDER BY desc_elt");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementBean elementBean = new ElementBean();
                elementBean.setId(rs.getString(1));
                elementBean.setDesc(rs.getString(2));
                result.add(elementBean);
            }
        } catch (SQLException e) {
            logger.error("Error during Sub element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ELEMENT_STEREOTYPE_UPDATE = "UPDATE Element SET id_stereotype = ? WHERE id_elt = ?";

    /**
     * Set a stereotype for an element collection.
     * @param elementCollection the given element collection.
     * @param idStereotype the given stereotype.
     * @param connection the DBMS connection used.
     */
    public void setElementStereotype(List<String> elementCollection,
            String idStereotype) {
        if (elementCollection != null && elementCollection.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement stmt = null;
            try {
                connection.setAutoCommit(false);
                stmt = connection.prepareStatement(ELEMENT_STEREOTYPE_UPDATE);
                for (String currentElt : elementCollection) {
                    if (idStereotype != null && idStereotype.length() > 0) {
                        stmt.setString(1, idStereotype);
                    } else {
                        stmt.setNull(1, Types.VARCHAR);
                    }
                    stmt.setString(2, currentElt);
                    stmt.addBatch();
                }
                stmt.executeBatch();
                JdbcDAOUtils.commit(connection);
            } catch (SQLException e) {
                logger.error("Error during stereotype element set", e);
                JdbcDAOUtils.rollbackConnection(connection);
            } finally {
                JdbcDAOUtils.closeStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }

    /** {@inheritDoc}
     */
    public List<ElementBean> retrieveSubElements(String idElt,
            Timestamp dmajBline) {
        List<ElementBean> result = new ArrayList<ElementBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            pstmt = connection.prepareStatement(SUB_ELEMENTS_REQUEST);
            pstmt.setString(1, idElt);
            pstmt.setTimestamp(2, dmajBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementBean elementBean = new ElementBean();
                elementBean.setId(rs.getString(1));
                elementBean.setTypeElt(rs.getString(2));
                if (elementBean.getTypeElt().equals(ElementType.EA)) {
                    String idUsa = rs.getString(3);
                    UsageBean usage = new UsageBean(idUsa);
                    elementBean.setUsage(usage);
                }
                result.add(elementBean);
            }
        } catch (SQLException e) {
            logger.error("Error during Sub element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<ElementBean> retrieveRecursiveSubElements(String idElt,
            BaselineBean baseline) {
        List<ElementBean> result = new ArrayList<ElementBean>();
        List<ElementBean> l = retrieveSubElements(idElt, baseline.getDmaj());
        for (ElementBean elt : l) {
            if (elt.getTypeElt().equals(ElementType.EA)) {
                result.add(elt);
            } else {
                result.addAll(retrieveRecursiveSubElements(elt.getId(), baseline));
            }
        }
        return result;
    }
    /**
     * Query used to retrieve created elements for a given EA and a given type.
     */
    private static final String TOTAL_ELEMENTS_REQUEST =
            "Select elt.id_telt, count(elt.id_elt)" + " From Element elt"
            + " Where elt.dinst_elt <= ?" + " And elt.id_main_elt = ?"
            + " And (" + " elt.dperemption is null OR " + " elt.dperemption > ?"
            + " )" + " Group by elt.id_telt";

    /** {@inheritDoc}
     */
    public Map<String, Integer> retrieveTotalElements(Timestamp dinstBline,
            Timestamp dmajBline, String idElt) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(TOTAL_ELEMENTS_REQUEST);
            pstmt.setTimestamp(1, dmajBline);
            pstmt.setString(2, idElt);
            pstmt.setTimestamp(3, dmajBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString(1), new Integer(rs.getInt(2)));
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
     * Query used to retrieve created elements for a given EA and a given type.
     */
    private static final String CREATED_ELEMENTS_REQUEST =
            "Select elt.id_telt, count(elt.id_elt)" + " From Element elt"
            + " Where elt.dinst_elt >= ?" + " And elt.id_main_elt = ?"
            + " And elt.dinst_elt <= ?" + " And ("
            + " elt.dperemption is null OR " + " elt.dperemption > ?" + " )"
            + " Group by elt.id_telt";

    /** {@inheritDoc}
     */
    public Map<String, Integer> retrieveCreatedElements(Timestamp dinstBline,
            Timestamp dmajBline, String idElt) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(CREATED_ELEMENTS_REQUEST);
            pstmt.setTimestamp(1, dinstBline);
            pstmt.setString(2, idElt);
            pstmt.setTimestamp(3, dmajBline);
            pstmt.setTimestamp(4, dmajBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString(1), new Integer(rs.getInt(2)));
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
     * Query used to retrieve deleted elements for a given EA and a given type.
     */
    private static final String DELETED_ELEMENTS_REQUEST =
            "Select elt.id_telt, count(elt.id_elt)" + " From Element elt"
            + " Where elt.dinst_elt < ?" + " And elt.id_main_elt = ?"
            + " And elt.dperemption is not null" + " And elt.dperemption >= ?"
            + " And elt.dperemption <= ?" + " Group by elt.id_telt";

    /** {@inheritDoc}
     */
    public Map<String, Integer> retrieveDeletedElements(Timestamp dinstBline,
            Timestamp dmajBline, String idElt) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(DELETED_ELEMENTS_REQUEST);
            pstmt.setTimestamp(1, dinstBline);
            pstmt.setString(2, idElt);
            pstmt.setTimestamp(3, dinstBline);
            pstmt.setTimestamp(4, dmajBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString(1), new Integer(rs.getInt(2)));
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
    private static final String ELEMENT_VOLUMETRY_SELECT = "SELECT id_telt From Volumetry Where id_elt = ? And id_telt = ? And id_bline = ?";
    private static final String ELEMENT_VOLUMETRY_INSERT = "INSERT INTO Volumetry (id_elt, id_telt, id_bline, total, created, deleted) VALUES (?,?,?,?,?,?)";
    private static final String ELEMENT_VOLUMETRY_UPDATE = "UPDATE Volumetry SET total = ?, created = ?, deleted = ? WHERE id_elt = ? AND id_telt = ? AND id_bline = ?";

    public void setElementVolumetry(Collection<Volumetry> volumetryCollection,
            String idBline) throws DataAccessException {

        if (volumetryCollection != null && volumetryCollection.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmtSelect = null;
            PreparedStatement pstmtInsert = null;
            PreparedStatement pstmtUpdate = null;
            ResultSet rs = null;
            try {
                connection.setAutoCommit(false);
                pstmtSelect = connection.prepareStatement(ELEMENT_VOLUMETRY_SELECT);
                pstmtUpdate = connection.prepareStatement(ELEMENT_VOLUMETRY_UPDATE);
                pstmtInsert = connection.prepareStatement(ELEMENT_VOLUMETRY_INSERT);
                Iterator<Volumetry> i = volumetryCollection.iterator();
                Volumetry currentVol = null;
                while (i.hasNext()) {
                    currentVol = (Volumetry) i.next();
                    pstmtSelect.setString(1, currentVol.getIdElt());
                    pstmtSelect.setString(2, currentVol.getIdTElt());
                    pstmtSelect.setString(3, idBline);
                    rs = pstmtSelect.executeQuery();
                    if (rs.next()) {
                        pstmtUpdate.setInt(1, currentVol.getTotal());
                        pstmtUpdate.setInt(2, currentVol.getCreated());
                        pstmtUpdate.setInt(3, currentVol.getDeleted());
                        pstmtUpdate.setString(4, currentVol.getIdElt());
                        pstmtUpdate.setString(5, currentVol.getIdTElt());
                        pstmtUpdate.setString(6, idBline);
                        pstmtUpdate.addBatch();
                    } else {
                        pstmtInsert.setString(1, currentVol.getIdElt());
                        pstmtInsert.setString(2, currentVol.getIdTElt());
                        pstmtInsert.setString(3, idBline);
                        pstmtInsert.setInt(4, currentVol.getTotal());
                        pstmtInsert.setInt(5, currentVol.getCreated());
                        pstmtInsert.setInt(6, currentVol.getDeleted());
                        pstmtInsert.addBatch();
                    }
                    JdbcDAOUtils.closeResultSet(rs);
                }
                pstmtInsert.executeBatch();
                pstmtUpdate.executeBatch();
            } catch (SQLException e) {
                logger.error("Error updating criterion data", e);
                throw new DataAccessException(e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmtSelect);
                JdbcDAOUtils.closePrepareStatement(pstmtInsert);
                JdbcDAOUtils.closePrepareStatement(pstmtUpdate);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    private static final String RETRIEVE_ALL_ENTITY_APPLICATION_FOR_PROJECT_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.id_pro, e.id_telt, e.id_usa, e.id_dialecte, m.APU_LIMIT_MEDIUM, m.APU_LIMIT_LONG "
            + " FROM ELEMENT e, modele m" + " WHERE e.id_telt = '"
            + ElementType.EA + "' " + " AND e.dperemption IS NULL"
            + " AND e.id_pro = ?" + " AND e.id_usa = m.id_usa";

    /**
     * @{@inheritDoc }
     */
    public List<ElementBean> retrieveAllApplicationEntitiesForProject(ElementBean project) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        List<ElementBean> retour = new ArrayList<ElementBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_ENTITY_APPLICATION_FOR_PROJECT_QUERY);
            pstmt.setString(1, project.getProject().getId());
            rs = pstmt.executeQuery();
            retour = this.getElementBeanListFromResultSet(rs);
        } catch (SQLException e) {
            logger.error("Error during Volumetry retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    private List<ElementBean> getElementBeanListFromResultSet(ResultSet rs) throws SQLException {
        List<ElementBean> retour = new ArrayList<ElementBean>();
        if (rs != null) {
            while (rs.next()) {
                ElementBean ea = new ElementBean();
                ea.setId(rs.getString("id_elt"));
                ea.setLib(rs.getString("lib_elt"));
                ea.setTypeElt(rs.getString("id_telt"));

                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectBean pb = new ProjectBean();
                    pb.setId(idPro);
                    ea.setProject(pb);
                }

                String idUsa = rs.getString("id_usa");
                if (idUsa != null) {
                    UsageBean ub = new UsageBean(idUsa);
                    ub.setLowerLimitLongRun(rs.getDouble("APU_LIMIT_LONG"));
                    ub.setLowerLimitMediumRun(rs.getDouble("APU_LIMIT_MEDIUM"));
                    ea.setUsage(ub);
                }

                String idDialecte = rs.getString("id_dialecte");
                if (idDialecte != null) {
                    DialecteBean db = DialecteDbmsDao.getInstance().retrieveDialecteByElementId(ea.getId());
                    ea.setDialecte(db);
                }

                retour.add(ea);
            }
        }
        return retour;
    }
    private static final String RETRIEVE_ALL_ENTITY_APPLICATION_FOR_PROJECT_AND_BASELINE_QUERY =
            "SELECT distinct e.id_elt, e.lib_elt, e.id_pro, e.id_telt, e.id_usa, e.id_dialecte, m.APU_LIMIT_LONG, m.APU_LIMIT_MEDIUM "
            + "FROM ELEMENT e, modele m, element_baseline_info ebi WHERE e.id_telt = '"
            + ElementType.EA + "' AND e.dperemption IS NULL "
            + "AND e.id_pro = ? AND e.id_usa = m.id_usa "
            + " AND e.id_elt = ebi.id_elt AND (ebi.id_bline = ? OR "
            + " ebi.id_bline in (select child_id_bline from BASELINE_LINKS where PARENT_ID_BLINE = ?))";

    /**
     * @{@inheritDoc }
     */
    public List<ElementBean> retrieveAllApplicationEntitiesForProjectAndBaseline(ProjectBean project, BaselineBean baseline) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        List<ElementBean> retour = new ArrayList<ElementBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_ENTITY_APPLICATION_FOR_PROJECT_AND_BASELINE_QUERY);
            pstmt.setString(1, project.getId());
            pstmt.setString(2, baseline.getId());
            pstmt.setString(3, baseline.getId());
            rs = pstmt.executeQuery();
            retour = this.getElementBeanListFromResultSet(rs);
        } catch (SQLException e) {
            logger.error("Error during Volumetry retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    private String createEndQuery(List<ElementBean> eas) {
        String retour = "";
        boolean first = true;
        for (ElementBean ea : eas) {
            if (!first) {
                retour += " OR ";
            }
            retour += " ( ";
            retour += "v.id_elt = '" + ea.getId() + "' ";
            retour += "AND v.id_bline = '" + ea.getBaseline().getId() + "' ";
            retour += " ) ";
            first = false;
        }
        return retour;
    }

    /** {@inheritDoc}
     */
    public int retrieveGlobalNumberOfElements(List<ElementBean> eas,
            boolean onlyFileElements) {
        int result = 0;
        Connection connection = JdbcDAOUtils.getConnection(this,
                Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "Select SUM(v.total) as total" + " From Volumetry v";
            if (onlyFileElements) {
                query += ", type_element te";
            }
            query += " Where ( ";
            query += this.createEndQuery(eas);
            query += ")";
            if (onlyFileElements) {
                query += "And v.id_telt = te.id_telt ";
                query += " And te.is_file = 1";
            }

            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("total");
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

    /** {@inheritDoc}
     */
    public int retrieveGlobalIFPUG(List<ElementBean> eas) {
        int result = 0;
        Connection connection = JdbcDAOUtils.getConnection(this,
                Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "Select SUM(v.valbrute_qamet) as total"
                    + " From QAMetrique v";
            query += " Where v.id_met = 'IFPUG' AND ( ";
            query += this.createEndQuery(eas);
            query += ")";

            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt("total");
            }
        } catch (SQLException e) {
            logger.error("Error during global ifpug retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<Volumetry> retrieveFileElementsVolumetry(
            List<ElementBean> eas,
            boolean onlyFileElements) {
        List<Volumetry> result = new ArrayList<Volumetry>();
        Connection connection = JdbcDAOUtils.getConnection(this,
                Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "Select v.id_elt, v.id_telt, v.total, v.created, v.deleted"
                    + " From Volumetry v";
            if (onlyFileElements) {
                query += ", type_element te";
            }
            query += " Where ( ";
            query += this.createEndQuery(eas);
            query += ")";
            if (onlyFileElements) {
                query += " And v.id_telt = te.id_telt ";
                query += " And te.is_file = 1";
            }
            pstmt = connection.prepareStatement(query);
            Volumetry currentVolumetry = null;
            rs = pstmt.executeQuery();
            while (rs.next()) {
                currentVolumetry = new Volumetry();
                currentVolumetry.setIdElt(rs.getString("id_elt"));
                currentVolumetry.setIdTElt(rs.getString("id_telt"));
                currentVolumetry.setTotal(rs.getInt("total"));
                currentVolumetry.setCreated(rs.getInt("created"));
                currentVolumetry.setDeleted(rs.getInt("deleted"));
                result.add(currentVolumetry);
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

    /**
     * Query used to retrieve element volumetry for a given EA and a given baseline.
     */
    private static final String RETRIEVE_ELEMENT_VOLUMETRY_BY_TYPE_QUERY =
            "Select id_telt, total, created, deleted" + " From Volumetry"
            + " Where id_elt = ?" + " And id_bline = ?";

    /** {@inheritDoc}
     */
    public List<Volumetry> retrieveVolumetry(String idElt, String idBline) {
        List<Volumetry> result = new ArrayList<Volumetry>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ELEMENT_VOLUMETRY_BY_TYPE_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            Volumetry currentVolumetry = null;
            rs = pstmt.executeQuery();
            while (rs.next()) {
                currentVolumetry = new Volumetry();
                currentVolumetry.setIdElt(idElt);
                currentVolumetry.setIdTElt(rs.getString("id_telt"));
                currentVolumetry.setTotal(rs.getInt("total"));
                currentVolumetry.setCreated(rs.getInt("created"));
                currentVolumetry.setDeleted(rs.getInt("deleted"));
                result.add(currentVolumetry);
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

    /** {@inheritDoc}
     */
    public boolean delete(String idElt, String telt) {
        boolean retour = false;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        //on prend les fils
        String getChildrenEltsQuery = "SELECT e.id_elt, e.id_telt "
                + "FROM ELT_LINKS l, ELEMENT e " + "WHERE l.elt_pere = ? "
                + "AND type = 'T' " + "AND e.id_elt = l.elt_fils";
        String deleteAllEAChildren = "UPDATE ELEMENT SET DPEREMPTION={fn now()} WHERE ID_ELT "
                + "IN (select id_elt from element where id_main_elt=?)";
        String deleteElt = "UPDATE ELEMENT SET DPEREMPTION={fn now()} WHERE ID_ELT=?";

        PreparedStatement pstmt = null;
        PreparedStatement childrenStmt = null;
        try {
            if (!ElementType.EA.equals(telt)) {
                childrenStmt = connection.prepareStatement(getChildrenEltsQuery);
                childrenStmt.setString(1, idElt);
                ResultSet rs = childrenStmt.executeQuery();
                while (rs.next()) {
                    String idF = rs.getString("id_elt");
                    String t = rs.getString("id_telt");
                    this.delete(idF, t);
                }
            } else {
                childrenStmt = connection.prepareStatement(deleteAllEAChildren);
                childrenStmt.setString(1, idElt);
                childrenStmt.executeUpdate();
            }
            pstmt = connection.prepareStatement(deleteElt);
            pstmt.setString(1, idElt);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
            retour = true;
        } catch (SQLException e) {
            logger.error(e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
    private static final String ELEMENT_SELECT_FOR_UPDATE = "SELECT id_elt FROM Element"
            + " WHERE ID_ELT=?";
    private static final String ELEMENT_INSERT =
            "INSERT INTO ELEMENT (ID_ELT,LIB_ELT,DESC_ELT,ID_TELT,DINST_ELT,STREAM_ELT,PVOBNAME,VOBMOUNTPOINT,MAKEFILE_DIR,SOURCE_DIR,BIN_DIR,PERIODIC_DIR,LIB,ID_USA,ID_DIALECTE,POIDS_ELT,ID_PRO,INFO1,INFO2,SCM_REPOSITORY,SCM_MODULE,PROJECT_FILE_PATH)"
            + " VALUES(?,?,?,?,{fn now()},?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String ELEMENT_UPDATE =
            "UPDATE ELEMENT SET LIB_ELT=?,DESC_ELT=?,DMAJ_ELT={fn now()},STREAM_ELT=?,PVOBNAME=?,VOBMOUNTPOINT=?,MAKEFILE_DIR=?,SOURCE_DIR=?,BIN_DIR=?,PERIODIC_DIR=?,LIB=?,ID_USA=?,ID_DIALECTE=?,POIDS_ELT=?,INFO1=?,INFO2=?,SCM_REPOSITORY=?,SCM_MODULE=?,PROJECT_FILE_PATH=?"
            + " WHERE ID_ELT=?";

    /** {@inheritDoc}
     */
    public void setElement(ElementBean eltBean) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(ELEMENT_SELECT_FOR_UPDATE);
            pstmt.setString(1, eltBean.getId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
                pstmt = connection.prepareStatement(ELEMENT_UPDATE);
                pstmt.setString(1, eltBean.getLib());
                pstmt.setString(2, eltBean.getDesc());
                pstmt.setString(3, eltBean.getStreamElt());
                pstmt.setString(4, eltBean.getPVobName());
                pstmt.setString(5, eltBean.getVobMountPoint());
                pstmt.setString(6, eltBean.getMakefileDir());
                pstmt.setString(7, eltBean.getSourceDir());
                pstmt.setString(8, eltBean.getBinDir());
                pstmt.setString(9, eltBean.getPeriodicDir());
                pstmt.setString(10, eltBean.getLibraries());
                UsageBean usage = eltBean.getUsage();
                if (usage != null) {
                    pstmt.setString(11, usage.getId());
                } else {
                    pstmt.setNull(11, java.sql.Types.VARCHAR);
                }
                DialecteBean dialecte = eltBean.getDialecte();
                if (dialecte != null) {
                    pstmt.setString(12, dialecte.getId());
                } else {
                    pstmt.setNull(12, java.sql.Types.VARCHAR);
                }
                pstmt.setInt(13, eltBean.getPoids());
                pstmt.setString(14, eltBean.getInfo1());
                pstmt.setString(15, eltBean.getInfo2());
                pstmt.setString(16, eltBean.getScmRepository());
                pstmt.setString(17, eltBean.getScmModule());
                pstmt.setString(18, eltBean.getProjectFilePath());

                pstmt.setString(19, eltBean.getId());
                pstmt.executeUpdate();
            } else {
                pstmt.close();
                pstmt = connection.prepareStatement(ELEMENT_INSERT);
                pstmt.setString(1, eltBean.getId());
                pstmt.setString(2, eltBean.getLib());
                pstmt.setString(3, eltBean.getDesc());
                pstmt.setString(4, eltBean.getTypeElt());
                pstmt.setString(5, eltBean.getStreamElt());
                pstmt.setString(6, eltBean.getPVobName());
                pstmt.setString(7, eltBean.getVobMountPoint());
                pstmt.setString(8, eltBean.getMakefileDir());
                pstmt.setString(9, eltBean.getSourceDir());
                pstmt.setString(10, eltBean.getBinDir());
                pstmt.setString(11, eltBean.getPeriodicDir());
                pstmt.setString(12, eltBean.getLibraries());
                UsageBean usage = eltBean.getUsage();
                if (usage != null) {
                    pstmt.setString(13, usage.getId());
                } else {
                    pstmt.setNull(13, java.sql.Types.VARCHAR);
                }
                DialecteBean dialecte = eltBean.getDialecte();
                if (dialecte != null) {
                    pstmt.setString(14, dialecte.getId());
                } else {
                    pstmt.setNull(14, java.sql.Types.VARCHAR);
                }
                pstmt.setInt(15, eltBean.getPoids());
                if (eltBean.getProject() != null) {
                    pstmt.setString(16, eltBean.getProject().getId());
                }
                pstmt.setString(17, eltBean.getInfo1());
                pstmt.setString(18, eltBean.getInfo2());
                pstmt.setString(19, eltBean.getScmRepository());
                pstmt.setString(20, eltBean.getScmModule());
                pstmt.setString(21, eltBean.getProjectFilePath());
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
            dataCache.clearCache(eltBean.getId());
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /** {@inheritDoc}
     */
    public void deleteUsersRights(String idElt) {
        String deleteRights = "DELETE FROM DROITS WHERE ID_ELT=?";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(deleteRights);
            pstmt.setString(1, idElt);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during user right delete", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    /** {@inheritDoc}
     */
    public void updateUsersRights(String idElt, String userList) {
        String updateRights = "INSERT INTO DROITS (ID_ELT,ID_PROFIL_USER,TYPE_ACCES) VALUES (?,?,?)";
        java.util.StringTokenizer stUserList = new java.util.StringTokenizer(userList, ",");
        String accessType = "U"; // pour l'instant on ne gere pas les profils uniquement les users

        // on efface les droits associe a l'elt pour les remettre moins couteux que erreur + update !!!
        this.deleteUsersRights(idElt);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(updateRights);
            while (stUserList.hasMoreTokens()) {
                String user = stUserList.nextToken();
                pstmt.setString(1, idElt);
                pstmt.setString(2, user.trim());
                pstmt.setString(3, accessType);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during user right update", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    private boolean userHasRightsForElement(String idElt, String idUser) {
        boolean retour = false;
        String count = "SELECT count(ID_ELT) FROM DROITS WHERE ID_ELT = ? AND ID_PROFIL_USER = ?";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(count);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idUser);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt(1);
                retour = (nb > 0);
            }
        } catch (SQLException e) {
            logger.error("Error during user right update", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    /** {@inheritDoc}
     */
    public void updateChildrenRights(String idFather, String userList,
            String userId, boolean canModifyDomains,
            boolean canModifyAllProjets) {
        Collection<ElementBean> subElts = retrieveSubElement(idFather);
        if (subElts != null) {
            ElementBean eltBean = null;
            Iterator<ElementBean> i = subElts.iterator();
            while (i.hasNext()) {
                eltBean = i.next();
                if (ElementType.DOMAIN.equals(eltBean.getTypeElt())) {
                    if (!canModifyDomains) {
                        continue;
                    }
                }
                if (ElementType.PRJ.equals(eltBean.getTypeElt())
                        && !canModifyAllProjets) {
                    if (!this.userHasRightsForElement(eltBean.getId(), userId)) {
                        continue;
                    }
                }

                this.updateUsersRights(eltBean.getId(), userList);

                if (!ElementType.EA.equals(eltBean.getTypeElt())) {
                    // on met a jour recursivement tous les fils
                    this.updateChildrenRights(eltBean.getId(), userList,
                            userId, canModifyDomains,
                            canModifyAllProjets);
                }
            }
        }
    }
    private static final String ELEMENT_SELECT_BY_LIB = "SELECT id_elt, lib_elt, desc_elt FROM Element"
            + " WHERE LIB_ELT = ? AND ID_MAIN_ELT = ? AND DPEREMPTION IS NULL";
    private static final String ELEMENT_SELECT_BY_DESC = "SELECT id_elt, lib_elt, desc_elt, id_telt FROM Element"
            + " WHERE DESC_ELT = ? AND ID_MAIN_ELT = ? AND DPEREMPTION IS NULL";
    private static final String ELEMENT_INSERT_DESC =
            "INSERT INTO ELEMENT (ID_ELT,LIB_ELT,DESC_ELT,ID_TELT,DINST_ELT,ID_PRO,ID_MAIN_ELT, FILEPATH)"
            + " VALUES(?,?,?,?,{fn now()},?,?,?)";

    /** {@inheritDoc}
     */
    public ElementBean retrieveUnknownElement(String eltDesc, String eaId,
            boolean createIfNotFound) {
        ElementBean result = new ElementBean();
        result.setDesc(eltDesc);
        result = retrieveUnknownElement(result, eaId, createIfNotFound);
        return result;
    }

    /** {@inheritDoc}
     */
    public ElementBean retrieveUnknownElement(ElementBean eltBean, String eaId,
            boolean createIfNotFound) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementBean result = eltBean;
        try {
            connection.setAutoCommit(false);
            if (eltBean.getDesc() != null && eltBean.getDesc().length() > 0) {
                pstmt = connection.prepareStatement(ELEMENT_SELECT_BY_DESC);
                pstmt.setString(1, eltBean.getDesc());
            } else {
                pstmt = connection.prepareStatement(ELEMENT_SELECT_BY_LIB);
                pstmt.setString(1, eltBean.getLib());
            }
            pstmt.setString(2, eaId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result.setId(rs.getString("id_elt"));
            } else if (createIfNotFound) {
                pstmt.close();
                result.setId(IDCreator.getID());
                pstmt = connection.prepareStatement(ELEMENT_INSERT_DESC);
                pstmt.setString(1, result.getId());
                pstmt.setString(2, eltBean.getLib());
                pstmt.setString(3, eltBean.getDesc());
                pstmt.setString(4, eltBean.getTypeElt());
                pstmt.setString(5, eltBean.getProject().getId());
                pstmt.setString(6, eaId);
                pstmt.setString(7, eltBean.getFilePath());
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during Metrique retrieve", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public void setTreeElementLink(ElementBean eltBean, String fatherId) {
        setElementLink(eltBean, fatherId, TREE_LINK);
    }

    /** {@inheritDoc}
     */
    public void setLeafElementLink(ElementBean eltBean, String fatherId) {
        setElementLink(eltBean, fatherId, LEAF_LINK);
    }
    private static final String LINK_INSERT =
            "INSERT INTO Elt_links (elt_pere, elt_fils, type, dinst_links, dapplication_links)"
            + " VALUES(?, ?, ?, {fn now()}, {fn now()})";

    private void setElementLink(ElementBean eltBean, String fatherId,
            String linkType) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(LINK_SELECT_FOR_UPDATE);
            pstmt.setString(1, fatherId);
            pstmt.setString(2, eltBean.getId());
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                pstmt.close();
                pstmt = connection.prepareStatement(LINK_INSERT);
                pstmt.setString(1, fatherId);
                pstmt.setString(2, eltBean.getId());
                pstmt.setString(3, linkType);
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during link update", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String SET_OUT_OF_DATE_QUERY = "Update Element Set DPEREMPTION = {fn now()} Where id_elt = ?";

    /** {@inheritDoc}
     */
    public void setOutOfDateElements(Collection<ElementBean> eltBeanColl, List<String> typeList) {
        if (eltBeanColl != null && eltBeanColl.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            try {
                connection.setAutoCommit(false);
                pstmt = connection.prepareStatement(SET_OUT_OF_DATE_QUERY);
                ElementBean eltBean = null;
                Iterator<ElementBean> i = eltBeanColl.iterator();
                while (i.hasNext()) {
                    eltBean = i.next();
                    if (typeList.contains(eltBean.getTypeElt())) {
                        pstmt.setString(1, eltBean.getId());
                        pstmt.addBatch();
                    }
                }
                pstmt.executeBatch();
                JdbcDAOUtils.commit(connection);
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Error during Metrique retrieve", e);
                JdbcDAOUtils.rollbackConnection(connection);
            } finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    private static final String ALL_PROJECT_ARBO_QUERY =
            "Select /*+ FIRST_ROWS */ id_elt, id_telt, lib_elt, dperemption, poids_elt, elt_pere"
            + " From Element, Elt_links" + " Where id_elt = elt_fils"
            + " And type = 'T'" + " order by dinst_elt";

    /** {@inheritDoc}
     */
    public List<ElementLinked> retrieveProjectArboElements() {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        HashMap<String, String> peremptedElts = new HashMap<String, String>();
        try {
            pstmt = connection.prepareStatement(ALL_PROJECT_ARBO_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setPoids(rs.getInt("poids_elt"));
                ElementBean pere = new ElementBean();
                pere.setId(rs.getString("elt_pere"));
                bean.setFather(pere);
                if ((rs.getDate("dperemption") != null) || (peremptedElts.get(bean.getFather().getId())
                        != null)) {
                    peremptedElts.put(bean.getId(), bean.getId());
                } else {
                    result.add(bean);
                }
            }

        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ALL_CHILDREN_ELEMENTS_WITH_USER =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.id_pro, elt.desc_elt, l.type, elt.id_usa"
            + " From Element elt, Elt_links l, Droits d"
            + " Where elt.id_elt = l.elt_fils" + " And l.elt_pere=?"
            + " And (l.type = 'T' OR l.type = 'S')"
            + " And elt.id_elt = d.id_elt" + " And d.id_profil_user = ?"
            + " AND (" + " elt.dperemption is null OR " + " elt.dperemption > ?"
            + " )";

    /** {@inheritDoc}
     */
    public List<ElementLinked> retrieveAllChildrenElements(String idEltPere,
            String idUser) {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        try {
            pstmt = connection.prepareStatement(ALL_CHILDREN_ELEMENTS_WITH_USER);
            pstmt.setString(1, idEltPere);
            pstmt.setString(2, idUser);
            pstmt.setTimestamp(3, new Timestamp((new Date()).getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setDesc(rs.getString("desc_elt"));
                String idPro = rs.getString("id_pro");
                if (idPro != null) {
                    ProjectBean prj = new ProjectBean();
                    prj.setId(idPro);
                    bean.setProject(prj);
                }
                String idUsa = rs.getString("id_usa");
                if (idUsa != null) {
                    bean.setUsage(new UsageBean(idUsa));
                }
                bean.setLinkType(rs.getString("type"));
                result.add(bean);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ALL_CHILDREN_ELEMENTS =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.id_pro, elt.desc_elt, l.type"
            + " From Element elt, Elt_links l"
            + " Where elt.id_elt = l.elt_fils" + " And l.elt_pere=?"
            + " And (l.type = 'T' OR l.type = '" + Constants.SYMBOLIC_LINK_TYPE
            + "')" + " AND (" + " elt.dperemption is null OR "
            + " elt.dperemption > ?" + " )";

    /** {@inheritDoc}
     */
    public List<ElementLinked> retrieveAllChildrenElements(String idEltPere) {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        try {
            pstmt = connection.prepareStatement(ALL_CHILDREN_ELEMENTS);
            pstmt.setString(1, idEltPere);
            pstmt.setTimestamp(2, new Timestamp((new Date()).getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setDesc(rs.getString("desc_elt"));
                ProjectBean prj = new ProjectBean();
                prj.setId(rs.getString("id_pro"));
                bean.setProject(prj);
                bean.setLinkType(rs.getString("type"));
                result.add(bean);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_CHILDREN_ELEMENT =
            "Select e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, e.id_pro, e.id_dialecte, e.poids_elt, e.id_usa"
            + " From Element e, elt_links el, Droits d"
            + " Where e.id_elt = el.elt_fils" + " And e.id_elt = d.id_elt"
            + " And (el.type = 'T' OR el.type = 'S')"
            + " And d.id_profil_user = ?" + " And el.elt_pere = ?"
            + " And e.dperemption is null";
    private static final String RETRIEVE_CHILDREN_ELEMENT_WITHOUT_USER =
            "Select e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, e.id_pro, e.id_dialecte, e.poids_elt, e.id_usa"
            + " From Element e, elt_links el" + " Where e.id_elt = el.elt_fils"
            + " And (el.type = 'T' OR el.type = 'S')" + " And el.elt_pere = ?"
            + " And e.dperemption is null";

    private void retrieveAllElementsForTypeBelongingToParentByUser(
            String idDomain, String idUser, String idTelt,
            List<ElementBean> retour, Connection conn) {
        PreparedStatement eltstmt = null;
        ResultSet rs = null;

        try {
            if (idUser != null) {
                eltstmt = conn.prepareStatement(RETRIEVE_CHILDREN_ELEMENT);
                eltstmt.setString(1, idUser);
                eltstmt.setString(2, idDomain);
            } else {
                eltstmt = conn.prepareStatement(RETRIEVE_CHILDREN_ELEMENT_WITHOUT_USER);
                eltstmt.setString(1, idDomain);
            }
            rs = eltstmt.executeQuery();
            while (rs.next()) {
                String eltFils = rs.getString("id_elt");
                String libFils = rs.getString("lib_elt");
                String teltFils = rs.getString("id_telt");
                if (!idTelt.equals(teltFils)) {
                    this.retrieveAllElementsForTypeBelongingToParentByUser(eltFils, idUser, idTelt, retour, conn);
                } else if (idTelt.equals(teltFils)) {
                    ElementBean eb = new ElementBean();
                    eb.setId(eltFils);
                    eb.setLib(libFils);
                    eb.setTypeElt(teltFils);
                    ProjectBean pb = new ProjectBean();
                    pb.setId(rs.getString("id_pro"));
                    eb.setProject(pb);
                    eb.setDesc(rs.getString("desc_elt"));
                    eb.setPoids(rs.getInt("poids_elt"));
                    String idDialecte = rs.getString("id_dialecte");
                    if (idDialecte != null) {
                        DialecteBean dial = DialecteDbmsDao.retrieveDialecteById(idDialecte, conn);
                        eb.setDialecte(dial);
                    }
                    String idUsa = rs.getString("id_usa");
                    if (idUsa != null) {
                        UsageBean ub = new UsageBean();
                        ub.setId(idUsa);
                        eb.setUsage(ub);
                    }
                    retour.add(eb);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving children elements for idDomain = "
                    + idDomain, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
        }
    }

    /** {@inheritDoc}
     */
    public List<ElementBean> retrieveAllElementsForTypeBelongingToParentByUser(
            String idDomain, String idUser,
            String idTelt) {
        List<ElementBean> retour = new ArrayList<ElementBean>();

        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        this.retrieveAllElementsForTypeBelongingToParentByUser(idDomain, idUser, idTelt, retour, conn);
        JdbcDAOUtils.closeConnection(conn);

        return retour;
    }
    private static final String ALL_PROJECT_ARBO_BY_USER_QUERY =
            "Select /*+ FIRST_ROWS */ elt.id_elt, elt.id_telt, elt.lib_elt, elt.dperemption, elt.poids_elt, l.elt_pere, l.type"
            + " From Element elt, Elt_links l, Droits d"
            + " Where elt.id_elt = l.elt_fils"
            + " And (l.type = 'T' OR l.type = 'S')"
            + " And elt.id_elt = d.id_elt" + " And d.id_profil_user = ?"
            + " order by elt.dinst_elt";

    /** {@inheritDoc}
     */
    public List<ElementLinked> retrieveProjectArboElements(String idUser) {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        HashMap<String, String> peremptedElts = new HashMap<String, String>();
        try {
            pstmt = connection.prepareStatement(ALL_PROJECT_ARBO_BY_USER_QUERY);
            pstmt.setString(1, idUser);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setPoids(rs.getInt("poids_elt"));
                bean.setLinkType(rs.getString("type"));
                ElementBean pere = new ElementBean();
                pere.setId(rs.getString("elt_pere"));
                bean.setFather(pere);
                if ((rs.getDate("dperemption") != null) || (peremptedElts.get(bean.getFather().getId())
                        != null)) {
                    peremptedElts.put(bean.getId(), bean.getId());
                } else {
                    result.add(bean);
                }
            }

        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String PROJECT_ARBO_QUERY_ONE_LEVEL =
            "Select e.id_elt, id_telt, lib_elt, e.id_pro, dperemption, poids_elt, elt_pere, e.desc_elt"
            + " From Element e, Elt_links l, Droits d, Baseline b"
            + " Where e.id_elt = l.elt_fils" + " And id_pro = ?"
            + " And l.elt_pere = ?" + " And type = 'T'" + " And b.id_bline = ?"
            + " And dinst_elt < b.dinst_bline"
            + " And (dperemption IS NULL OR dperemption > b.dinst_bline)"
            + " And e.id_elt = d.id_elt" + " And d.id_profil_user = ?"
            + " order by dinst_elt";

    /** {@inheritDoc}
     */
    public List<ElementLinked> retrieveProjectArboElementsOneLevel(String idPro,
            String idBline, String idElt, String idUser) {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        //HashMap<String, String> peremptedElts = new HashMap<String, String>();
        try {
            pstmt = connection.prepareStatement(PROJECT_ARBO_QUERY_ONE_LEVEL);
            pstmt.setString(1, idPro);
            pstmt.setString(2, idElt);
            pstmt.setString(3, idBline);
            pstmt.setString(4, idUser);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setDesc(rs.getString("desc_elt"));
                bean.setPoids(rs.getInt("poids_elt"));
                ElementBean pere = new ElementBean();
                pere.setId(rs.getString("elt_pere"));
                bean.setFather(pere);
                result.add(bean);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String PROJECT_ARBO_QUERY =
            "Select e.id_elt, id_telt, lib_elt, dperemption, poids_elt, elt_pere"
            + " From Element e, Elt_links, Droits d, Baseline b"
            + " Where e.id_elt = elt_fils" + " And type = 'T'"
            + " And id_pro = ?" + " And e.id_elt = d.id_elt"
            + " And b.id_bline = ?" + " And dinst_elt < b.dinst_bline"
            + " And (dperemption IS NULL OR dperemption > b.dinst_bline)"
            + " And d.id_profil_user = ?" + " order by dinst_elt";

    /** {@inheritDoc}
     */
    public List<ElementLinked> retrieveProjectArboElements(String idPro,
            String idBline, String idUser) {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        HashMap<String, String> peremptedElts = new HashMap<String, String>();
        try {
            pstmt = connection.prepareStatement(PROJECT_ARBO_QUERY);
            pstmt.setString(1, idPro);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idUser);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setPoids(rs.getInt("poids_elt"));
                ElementBean pere = new ElementBean();
                pere.setId(rs.getString("elt_pere"));
                bean.setFather(pere);
                if ((rs.getDate("dperemption") != null) || (peremptedElts.get(bean.getFather().getId())
                        != null)) {
                    peremptedElts.put(bean.getId(), bean.getId());
                } else {
                    result.add(bean);
                }
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String PROJECT_ARBO_BY_USER_QUERY =
            "Select e.id_elt, id_telt, lib_elt, dperemption, poids_elt, elt_pere"
            + " From Element e, Elt_links, Droits d"
            + " Where e.id_elt = elt_fils" + " And type = 'T'"
            + " And id_pro = ?" + " And e.id_elt = d.id_elt"
            + " And dperemption IS NULL" + " And d.id_profil_user = ?"
            + " order by dinst_elt";

    /**
     * @{@inheritDoc }
     */
    public ElementLinked retrieveProjectArboElements(String idPro,
            String idUser) throws DataAccessException {
        ElementLinked result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        HashMap<String, ElementLinked> elementMap = new HashMap<String, ElementLinked>();
        try {
            pstmt = connection.prepareStatement(PROJECT_ARBO_BY_USER_QUERY);
            pstmt.setString(1, idPro);
            pstmt.setString(2, idUser);
            rs = pstmt.executeQuery();
            ElementLinked father = null;
            List<ElementLinked> listeTemporaire = new ArrayList<ElementLinked>();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setLib(rs.getString("lib_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                bean.setPoids(rs.getInt("poids_elt"));
                ElementBean pere = new ElementBean();
                pere.setId(rs.getString("elt_pere"));
                bean.setFather(pere);
                father = elementMap.get(bean.getFather().getId());
                if (father != null) {
                    father.addChild(bean);
                } else if (!ElementType.PRJ.equals(bean.getTypeElt())) {
                    listeTemporaire.add(bean);
                }
                elementMap.put(bean.getId(), bean);
                if (ElementType.PRJ.equals(bean.getTypeElt())) {
                    result = bean;
                }
            }
            if (!listeTemporaire.isEmpty()) {
                for (ElementLinked link : listeTemporaire) {
                    father = elementMap.get(link.getFather().getId());
                    if (father != null) {
                        father.addChild(link);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving project tree", e);
            throw new DataAccessException("Error retrieving project tree", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_SUB_ELEMENT_TYPES_QUERY = "Select distinct(id_telt) From Element Where id_main_elt = ?";

    public List<ElementType> retrieveSubElementTypes(String idMainElt) {
        List<ElementType> result = new ArrayList<ElementType>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementType type = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_SUB_ELEMENT_TYPES_QUERY);
            pstmt.setString(1, idMainElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                type = new ElementType();
                type.setId(rs.getString(1));
                result.add(type);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String SUBELEMENT_LINKS_QUERY =
            "Select distinct elt_pere, id_elt, pkg.coeff_pack, fils.id_stereotype, id_telt, desc_elt, dinst_elt"
            + " From Elt_Links lnk, Element fils, Package pkg"
            + " Where lnk.elt_fils=fils.id_elt" + " And lnk.type='L'"
            + " And (fils.dperemption is null Or fils.dperemption > ?)"
            + " And fils.dinst_elt < ?" + " And ("
            + " (fils.id_pack is null And pkg.id_pack='O')" + " Or"
            + " (fils.id_pack is not null And fils.id_pack = pkg.id_pack)"
            + " )" + " And fils.id_main_elt=?" + " order by fils.desc_elt";

    public List<ElementLinked> retrieveSubElementLinks(String idMainElt,
            Timestamp dmajBline) {
        List<ElementLinked> result = new ArrayList<ElementLinked>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ElementLinked bean;
        try {
            pstmt = connection.prepareStatement(SUBELEMENT_LINKS_QUERY);
            pstmt.setTimestamp(1, dmajBline);
            pstmt.setTimestamp(2, dmajBline);
            pstmt.setString(3, idMainElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new ElementLinked();
                bean.setId(rs.getString("id_elt"));
                bean.setTypeElt(rs.getString("id_telt"));
                int coefPack = rs.getInt("coeff_pack");
                if (rs.wasNull()) {
                    coefPack = 1;
                }

                bean.setPoids(coefPack);
                ElementBean pere = new ElementBean();
                pere.setId(rs.getString("elt_pere"));
                bean.setFather(pere);
                bean.setDesc(rs.getString("desc_elt"));
                result.add(bean);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String ELEMENT_HAS_BASELINE_QUERY =
            "Select count(id_elt)" + " From Facteur_bline" + " Where id_elt=?";

    public boolean elementHasBaselines(String idElt) {
        boolean retour = false;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ELEMENT_HAS_BASELINE_QUERY);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
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
    private static final String MOVE_ELEMENT_QUERY =
            "Update ELT_LINKS" + " set ELT_PERE=?" + " Where ELT_FILS=?";

    public void moveElement(String son, String father) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(MOVE_ELEMENT_QUERY);
            pstmt.setString(1, father);
            pstmt.setString(2, son);
            pstmt.executeUpdate();
            dataCache.clearCache(son);
        } catch (SQLException e) {
            logger.error("Error during Element update", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String UPDATE_ELEMENT_DATE_QUERY =
            "UPDATE ELEMENT SET DMAJ_ELT = {fn now()} WHERE ID_ELT = ?";

    public void updateElementDate(String idElt) throws DataAccessException {
        //update EA MAJ DATE
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            //Loading Ok. Update MAJ date of EA.
            pstmt = connection.prepareStatement(UPDATE_ELEMENT_DATE_QUERY);
            pstmt.setString(1, idElt);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error updating element", e);
            throw new DataAccessException("Error updating element", e);
        } finally {
            JdbcDAOUtils.closeStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        //end update EA MAJ DATE
    }

    public void removeRightsToChildren(String idFather, String[] rights,
            String userId, boolean canModifyDomains,
            boolean canModifyAllProjets) {
        Collection<ElementBean> subElts = retrieveSubElement(idFather);
        if (subElts != null) {
            ElementBean eltBean = null;
            Iterator<ElementBean> i = subElts.iterator();
            while (i.hasNext()) {
                eltBean = i.next();
                if (ElementType.DOMAIN.equals(eltBean.getTypeElt())) {
                    if (!canModifyDomains) {
                        continue;
                    }
                }
                if (ElementType.PRJ.equals(eltBean.getTypeElt())
                        && !canModifyAllProjets) {
                    if (!this.userHasRightsForElement(eltBean.getId(), userId)) {
                        continue;
                    }
                }

                this.removeUsersRights(eltBean.getId(), rights);

                if (!eltBean.getTypeElt().equals(ElementType.EA)) {
                    // on met a jour recursivement tous les fils
                    this.removeRightsToChildren(eltBean.getId(), rights,
                            userId, canModifyDomains, canModifyAllProjets);
                }
            }
        }
    }

    public void removeUsersRights(String idElt, String[] rights) {
        String updateRights = "DELETE FROM DROITS WHERE ID_ELT = ? AND ID_PROFIL_USER = ?";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(updateRights);
            for (int i = 0; i < rights.length; i++) {
                pstmt.setString(1, idElt);
                pstmt.setString(2, rights[i]);
                pstmt.addBatch();

            }
            pstmt.executeBatch();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during user right remove", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }
    private static final String DELETE_COPY_PASTE = "Delete From Copy_Paste_Ref Where id_bline = ?";
    private static final String COPY_PASTE_INSERT = "INSERT INTO Copy_Paste_Ref (id_copy, id_bline, id_elt, line, length) VALUES (?,?,?,?,?)";

    public void insertCopyPaste(List<CopyPasteBean> copyPasteList,
            String idBline) throws DataAccessException {
        if (copyPasteList != null && copyPasteList.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            
            PreparedStatement pstmtInsert = null;
            PreparedStatement pstmtDelete = null;
            try {
                connection.setAutoCommit(false);

                pstmtDelete = connection.prepareStatement(DELETE_COPY_PASTE);
                pstmtDelete.setString(1, idBline);
                pstmtDelete.executeUpdate();
                JdbcDAOUtils.commit(connection);
                pstmtDelete.close();

                pstmtInsert = connection.prepareStatement(COPY_PASTE_INSERT);
                Iterator<CopyPasteBean> i = copyPasteList.iterator();
                CopyPasteBean currentCopy = null;
                while (i.hasNext()) {
                    currentCopy = (CopyPasteBean) i.next();
                    List<CopyPasteElement> elements = currentCopy.getElements();
                    Iterator<CopyPasteElement> eltIter = elements.iterator();
                    while (eltIter.hasNext()) {
                        CopyPasteElement currentElement = eltIter.next();
                        pstmtInsert.setString(1, currentCopy.getId());
                        pstmtInsert.setString(2, idBline);
                        pstmtInsert.setString(3, currentElement.getIdElt());
                        pstmtInsert.setInt(4, currentElement.getLine());
                        pstmtInsert.setInt(5, currentCopy.getLines());
                        pstmtInsert.addBatch();

                        //logger.debug("INSERT INTO Copy_Paste_Ref (id_copy, id_bline, id_elt, line, length) VALUES ('"+currentCopy.getId()+"','"+idBline+"','"+currentElement.getIdElt()+"',"+currentElement.getLine()+","+currentCopy.getLines()+");");
                    }
                    
                }
                pstmtInsert.executeBatch();
                JdbcDAOUtils.commit(connection);
            } catch (SQLException e) {
                logger.error("Error setting copy paste"+ e);
                
                //throw new DataAccessException(e);
            } finally {
                JdbcDAOUtils.closePrepareStatement(pstmtInsert);
                JdbcDAOUtils.closePrepareStatement(pstmtDelete);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

    }
    private static final String COPY_PASTE_RETRIEVE_QUERY =
            " Select id_copy, e.id_elt, e.desc_elt, line, length"
            + " From copy_paste_ref cp, element e" + " Where id_copy in ("
            + "Select id_copy" + " From copy_paste_ref" + " Where id_elt = ?"
            + " And id_bline = ?" + " )" + " And id_bline = ?"
            + " And cp.id_elt = e.id_elt" + " Order by id_copy";

    public List<CopyPasteBean> retrieveCopyPaste(String idElt,
            String idBline) throws DataAccessException {
        List<CopyPasteBean> result = new ArrayList<CopyPasteBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        CopyPasteBean currentCopy = null;
        CopyPasteElement currentElement = null;
        try {
            pstmt = connection.prepareStatement(COPY_PASTE_RETRIEVE_QUERY);
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            pstmt.setString(3, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String idCopy = rs.getString("id_copy");
                if (currentCopy == null || !idCopy.equals(currentCopy.getId())) {
                    currentCopy = new CopyPasteBean();
                    currentCopy.setId(idCopy);
                    currentCopy.setLines(rs.getInt("length"));
                    result.add(currentCopy);
                }
                currentElement = new CopyPasteElement();
                currentElement.setIdElt(rs.getString("id_elt"));
                currentElement.setDescElt(rs.getString("desc_elt"));
                currentElement.setLine(rs.getInt("line"));
                currentCopy.addElement(currentElement);
            }
        } catch (SQLException e) {
            logger.error("Error during Copy/Paste retrieve", e);
            throw new DataAccessException("Error during Copy/Paste retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String COPY_PASTE_ELEMENT_RETRIEVE_QUERY =
            " Select id_copy, e.id_elt, e.desc_elt, line, length"
            + " From copy_paste_ref cp, element e" + " Where id_copy = ?"
            + " And cp.id_elt = ?" + " And id_bline = ?" + " And line = ?"
            + " And cp.id_elt = e.id_elt" + " Order by id_copy";

    public CopyPasteBean retrieveCopyPaste(String idCopy, String idElt,
            String idBline, int line) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        CopyPasteBean currentCopy = null;
        CopyPasteElement currentElement = null;
        try {
            pstmt = connection.prepareStatement(COPY_PASTE_ELEMENT_RETRIEVE_QUERY);
            pstmt.setString(1, idCopy);
            pstmt.setString(2, idElt);
            pstmt.setString(3, idBline);
            pstmt.setInt(4, line);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                currentCopy = new CopyPasteBean();
                currentCopy.setId(idCopy);
                currentCopy.setLines(rs.getInt("length"));
                currentElement = new CopyPasteElement();
                currentElement.setIdElt(rs.getString("id_elt"));
                currentElement.setDescElt(rs.getString("desc_elt"));
                currentElement.setLine(rs.getInt("line"));
                currentCopy.addElement(currentElement);
            }
        } catch (SQLException e) {
            logger.error("Error during Copy/Paste retrieve", e);
            throw new DataAccessException("Error during Copy/Paste retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return currentCopy;
    }

    /** {@inheritDoc}
     */
    public List<ElementType> retrieveAllElementTypes() {
        List<ElementType> retour = new ArrayList<ElementType>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT ID_TELT FROM TYPE_ELEMENT";
            pstmt = connection.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementType et = new ElementType(rs.getString("ID_TELT"));
                retour.add(et);
            }
        } catch (SQLException e) {
            logger.error("Error during element type retrieving", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    /*	/** {@inheritDoc}

     */
    public List<ElementType> retrieveAllElementTypesForApplicationEntity(
            String idEa) {
        List<ElementType> retour = new ArrayList<ElementType>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "select distinct id_telt " + " from element e "
                    + " where id_main_elt = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, idEa);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ElementType et = new ElementType(rs.getString("ID_TELT"));
                retour.add(et);
            }
        } catch (SQLException e) {
            logger.error("Error during element type retrieving", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
    private static final String PROJECT_SUB_ELT_QUERY =
            "Select id_elt, id_telt, lib_elt,stream_elt, id_dialecte,DPEREMPTION,PVOBNAME,VOBMOUNTPOINT,makefile_dir,source_dir,bin_dir,periodic_dir,LIB, info1, info2"
            + " From Element e" + " Where id_pro = ?"
            + " And DPEREMPTION IS NULL" + " And id_telt = ?"
            + " And id_elt IN @INCLAUSE@" + " order by lib_elt";
    private static final String ALL_PROJECT_SUB_ELT_REQUEST =
            "Select id_elt, id_telt, lib_elt,stream_elt, id_dialecte,DPEREMPTION,PVOBNAME,VOBMOUNTPOINT,makefile_dir,source_dir,bin_dir,periodic_dir,LIB, info1, info2"
            + " From Element e" + " Where id_pro = ?"
            + " And DPEREMPTION IS NULL" + " And id_telt = ?"
            + " order by lib_elt";

    /**
     * @{@inheritDoc}
     */
    public List<EA> retrieveProjectSubElements(String projectId,
            String[] eaArray, DataFileType wantedType)
            throws DataAccessException {
        List<EA> result = new ArrayList<EA>();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        QueryUtil queryUtil = QueryUtil.getInstance();
        String query = ALL_PROJECT_SUB_ELT_REQUEST;
        if (eaArray != null) {
            query = queryUtil.replaceInClause(PROJECT_SUB_ELT_QUERY, eaArray);
        }
        try {
            eltstmt = conn.prepareStatement(query);
            eltstmt.setString(1, projectId);
            eltstmt.setString(2, wantedType.getName());
            rs = eltstmt.executeQuery();
            EA currentEa = null;
            while (rs.next()) {
                currentEa = new EA();
                currentEa.setId(rs.getString("id_elt"));
                DataFileType type = DataFileType.fromName(rs.getString(2));
                currentEa.setTypeElt(type.getName());
                currentEa.setLib(rs.getString("lib_elt"));
                currentEa.setInfo1(rs.getString("info1"));
                currentEa.setInfo2(rs.getString("info2"));

                DialecteBean dialecte = DialecteDbmsDao.retrieveDialecteById(rs.getString(5), conn);
                currentEa.setDialecte(dialecte);

                currentEa.setSourceDir(rs.getString("source_dir"));
                currentEa.setTargetDirectory(currentEa.getSourceDir());
                currentEa.setLibraries(rs.getString("lib"));

                result.add(currentEa);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving project sub elements", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    private static final String RETRIEVE_PARENT_ELEMENT_WITHOUT_ENTRYPOINT =
            "Select id_elt, lib_elt" + " From Element e, elt_links el"
            + " Where e.id_elt=el.elt_pere" + " And elt_fils = ?"
            + " And el.elt_pere != 'ENTRYPOINT'" + " And el.type = 'T'";

    /**
     * renvoie le chemin depuis la racine vers l'element envoye en parametre
     * @param idElt identifiant de l'element
     * @param col la colonne utilisee pour representer le chemin : id_elt ou lib_elt
     * @param conn connection a la bdd
     * @return le chemin
     */
    private String retrieveParentPath(String idElt, String col,
            Connection conn) {
        String retour = null;
        PreparedStatement eltstmt = null;
        ResultSet rs = null;

        try {
            eltstmt = conn.prepareStatement(RETRIEVE_PARENT_ELEMENT_WITHOUT_ENTRYPOINT);
            eltstmt.setString(1, idElt);
            rs = eltstmt.executeQuery();
            if (rs.next()) {
                String eltPere = rs.getString("id_elt");
                retour = this.retrieveParentPath(eltPere, col, conn);
                if (retour != null) {
                    retour += Constants.ELEMENT_PATH_SEPARATOR
                            + rs.getString(col);
                } else {
                    retour = Constants.ELEMENT_PATH_SEPARATOR
                            + rs.getString(col);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving father element for idElt = "
                    + idElt, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
        }
        return retour;
    }

    /**
     * @{@inheritDoc}
     */
    public String retrieveParentPathByLib(String idElt) {
        String retour = (String) dataCache.loadFromCache("parentPathByLib"
                + idElt);
        if (retour == null) {
            Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            retour = this.retrieveParentPath(idElt, "lib_elt", conn);
            if (retour == null) {
                retour = Constants.ELEMENT_PATH_SEPARATOR;
            }
            JdbcDAOUtils.closeConnection(conn);
            dataCache.storeToCache(idElt, "parentPathByLib" + idElt, retour);
        }
        return retour;
    }

    /**
     * @{@inheritDoc}
     */
    public String retrieveParentPathById(String idElt) {
        String retour = "";
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        retour = this.retrieveParentPath(idElt, "id_elt", conn);
        if (retour == null) {
            retour = ElementType.ENTRYPOINT;
        }
        JdbcDAOUtils.closeConnection(conn);
        return retour;
    }
    /**
     * requete retournant le nombre de liens existants pour un couple pere/fils
     */
    private static final String RETRIEVE_NB_LINK_QUERY = "SELECT count(elt_pere) as nb"
            + " FROM elt_links" + " WHERE elt_pere = ?" + " AND elt_fils = ?";
    /**
     * requete inserant un nouveau lien symbolique
     */
    private static final String INSERT_SYMLINK_QUERY = "INSERT INTO "
            + " elt_links(elt_pere, elt_fils, dinst_links, type)"
            + " VALUES(?, ?, ?, ?)";

    /**
     * @{@inheritDoc}
     */
    public MessagesCodes createSymbolicLink(String fatherId, String childId) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        try {
            eltstmt = conn.prepareStatement(RETRIEVE_NB_LINK_QUERY);
            eltstmt.setString(1, fatherId);
            eltstmt.setString(2, childId);
            rs = eltstmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt("nb");
                if (nb == 0) {
                    JdbcDAOUtils.closeResultSet(rs);
                    JdbcDAOUtils.closePrepareStatement(eltstmt);
                    eltstmt = conn.prepareStatement(INSERT_SYMLINK_QUERY);
                    eltstmt.setString(1, fatherId);
                    eltstmt.setString(2, childId);
                    eltstmt.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
                    eltstmt.setString(4, Constants.SYMBOLIC_LINK_TYPE);
                    eltstmt.executeUpdate();
                } else {
                    retour = MessagesCodes.SYMLINK_ALREADY_EXISTS;
                }
            }
        } catch (SQLException e) {
            logger.error("Error while creating symlink fatherId = " + fatherId
                    + ", childId = " + childId, e);
            retour = MessagesCodes.DATABASE_ERROR;
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return retour;
    }
    /**
     * requete supprimant un lien symbolique
     */
    private static final String DELETE_SYMLINK_QUERY = "DELETE FROM elt_links "
            + " WHERE elt_pere = ?" + " AND elt_fils = ?" + " AND type = 'S'";

    /**
     * @{@inheritDoc}
     */
    public void deleteSymbolicLink(String fatherId, String childId)
            throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        try {
            eltstmt = conn.prepareStatement(DELETE_SYMLINK_QUERY);
            eltstmt.setString(1, fatherId);
            eltstmt.setString(2, childId);
            int nb = eltstmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("No symbolic link to delete");
            }
        } catch (SQLException e) {
            logger.error("Error while creating symlink fatherId = " + fatherId
                    + ", childId = " + childId, e);
            throw new DataAccessException(e.getMessage());
        } finally {
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    private List<ElementLinked> createElementLinkedListFromResultSet(ResultSet rs, List<ElementLinked> liste)
            throws SQLException {
        while (rs.next()) {
            ElementLinked eltBean = new ElementLinked();
            eltBean.setId(rs.getString("id_elt1"));
            eltBean.setLib(rs.getString("lib_elt1"));
            eltBean.setTypeElt(rs.getString("telt1"));
            eltBean.setDperemption(rs.getTimestamp("dper1"));
            ElementBean pere = new ElementBean();
            pere.setId(rs.getString("id_elt2"));
            pere.setLib(rs.getString("lib_elt2"));
            pere.setTypeElt(rs.getString("telt2"));
            eltBean.setFather(pere);
            liste.add(eltBean);
        }
        return liste;
    }
    /**
     * Renvoie tous les elements perimes dont le pere n'est pas perime et qui ne
     * sont pas flagges comme a supprimer
     */
    private static final String RETRIEVE_ALL_PEREMPTED_ROOT_ELTS_QUERY =
            "select elt.id_elt as id_elt1, elt.lib_elt as lib_elt1, elt.id_telt as telt1, elt.DPEREMPTION as dper1, "
            + " elt2.id_elt as id_elt2, elt2.id_telt as telt2, elt2.lib_elt as lib_elt2 "
            + " FROM element elt, elt_links l, element elt2"
            + " WHERE elt.dperemption IS NOT NULL "
            + " AND elt.id_main_elt IS NULL " + " AND elt.id_elt = l.elt_fils "
            + " AND l.elt_pere = elt2.id_elt " + " AND elt2.DPEREMPTION IS NULL"
            + " AND elt.id_elt NOT IN ( SELECT id_elt FROM caqs_messages WHERE id_task = ?)";

    /**
     * @{@inheritDoc}
     */
    public List<ElementLinked> retrieveAllPeremptedRootsElements() {
        List<ElementLinked> retour = new ArrayList<ElementLinked>();

        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        try {
            eltstmt = conn.prepareStatement(RETRIEVE_ALL_PEREMPTED_ROOT_ELTS_QUERY);
            eltstmt.setString(1, TaskId.DELETE_ELEMENTS.toString());
            rs = eltstmt.executeQuery();
            this.createElementLinkedListFromResultSet(rs, retour);
        } catch (SQLException e) {
            logger.error("Error while retrieving all perempted roots elements", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }

        return retour;
    }
    /**
     * Renvoie tous les elements perimes dont le pere n'est pas perime et qui ne
     * sont pas flagges comme a supprimer
     */
    private static final String RETRIEVE_ALL_PEREMPTED_ROOT_ELTS_WITH_USER_QUERY =
            "select elt.id_elt as id_elt1, elt.lib_elt as lib_elt1, elt.id_telt as telt1, elt.DPEREMPTION as dper1, "
            + " elt2.id_elt as id_elt2, elt2.id_telt as telt2, elt2.lib_elt as lib_elt2 "
            + " FROM element elt, elt_links l, element elt2"
            + " WHERE elt.dperemption IS NOT NULL "
            + " AND elt.id_main_elt IS NULL " + " AND elt.id_elt = l.elt_fils "
            + " AND l.elt_pere = elt2.id_elt " + " AND elt2.DPEREMPTION IS NULL"
            + " AND elt.id_elt NOT IN ( SELECT id_elt FROM caqs_messages WHERE id_task = ?)"
            + " AND elt.id_elt in (SELECT id_elt  FROM droits WHERE id_profil_user = ?)";

    /**
     * @{@inheritDoc}
     */
    public List<ElementLinked> retrieveAllPeremptedRootsElements(String userId) {
        List<ElementLinked> retour = new ArrayList<ElementLinked>();

        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        try {
            eltstmt = conn.prepareStatement(RETRIEVE_ALL_PEREMPTED_ROOT_ELTS_WITH_USER_QUERY);
            eltstmt.setString(1, TaskId.DELETE_ELEMENTS.toString());
            eltstmt.setString(2, userId);
            rs = eltstmt.executeQuery();
            this.createElementLinkedListFromResultSet(rs, retour);
        } catch (SQLException e) {
            logger.error("Error while retrieving all perempted roots elements", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }

        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public List<ElementBean> retrieveAllPeremptedSubElements(String idElt) {
        List<ElementBean> retour = new ArrayList<ElementBean>();

        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        try {
            eltstmt = conn.prepareStatement(SELECT_ALL_PEREMPTED_CHILDREN);
            eltstmt.setString(1, idElt);
            rs = eltstmt.executeQuery();
            while (rs.next()) {
                ElementLinked eltBean = new ElementLinked();
                eltBean.setId(rs.getString("id_elt"));
                retour.add(eltBean);
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving all perempted sub elements for "
                    + idElt, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return retour;
    }

    private void restoreAllPeremptedSubElements(String idElt,
            PreparedStatement updateStmt,
            PreparedStatement selectStmt) throws DataAccessException {
        ResultSet rs = null;
        try {
            updateStmt.setString(1, idElt);
            updateStmt.addBatch();
            selectStmt.setString(1, idElt);
            rs = selectStmt.executeQuery();
            while (rs.next()) {
                String childIdElt = rs.getString("id_elt");
                this.restoreAllPeremptedSubElements(childIdElt, updateStmt,
                        selectStmt);
            }
        } catch (SQLException e) {
            logger.error("error while restoring elements", e);
            throw new DataAccessException(e.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
        }
    }
    private static final String RESTORE_PEREMPTED_ELEMENT_QUERY = "UPDATE ELEMENT"
            + " SET DPEREMPTION = NULL " + " WHERE ID_ELT = ?";
    private static final String SELECT_ALL_PEREMPTED_CHILDREN = "SELECT elt.ID_ELT "
            + " FROM ELEMENT elt, ELT_LINKS l"
            + " WHERE elt.DPEREMPTION IS NOT NULL "
            + " AND elt.id_elt = l.ELT_FILS " + " AND l.ELT_PERE = ? ";

    /**
     * @{@inheritDoc }
     */
    public void restoreAllPeremptedElementsTree(String idElt)
            throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement updateStmt = null;
        PreparedStatement selectStmt = null;
        try {
            selectStmt = conn.prepareStatement(SELECT_ALL_PEREMPTED_CHILDREN);
            updateStmt = conn.prepareStatement(RESTORE_PEREMPTED_ELEMENT_QUERY);
            this.restoreAllPeremptedSubElements(idElt, updateStmt, selectStmt);
            updateStmt.executeBatch();
        } catch (SQLException exc) {
            logger.error("Error while restoring perempted elements", exc);
            throw new DataAccessException(exc.getMessage());
        } finally {
            JdbcDAOUtils.closePrepareStatement(selectStmt);
            JdbcDAOUtils.closePrepareStatement(updateStmt);
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    private void executeDeleteQuery(String query, String idElt, Connection conn)
            throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, idElt);
            stmt.executeUpdate();
        } catch (SQLException exc) {
            logger.error("Error while restoring perempted elements", exc);
            throw new DataAccessException(exc.getMessage());
        } finally {
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void deletePeremptedElement(String idElt) throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            conn.setAutoCommit(false);
            //on commence par supprimer le lien vers le pere
            String query = "DELETE FROM ELT_LINKS WHERE ELT_FILS = ?";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression des elements fils
            query = "DELETE FROM ELT_LINKS WHERE ELT_PERE = ?";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression des elements fils de l'ea
            query = "DELETE FROM ELT_LINKS WHERE ELT_FILS IN (SELECT ID_ELT FROM ELEMENT WHERE ID_MAIN_ELT = ?)";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression des archi_link
            query = "DELETE FROM ARCHI_LINK WHERE ID_PROJ = ?";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression des droits
            query = "DELETE FROM DROITS WHERE ID_ELT = ?";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression des �l�ments fils
            query = "DELETE FROM ELEMENT WHERE ID_MAIN_ELT = ?";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression de l'�l�ment
            query = "DELETE FROM ELEMENT WHERE ID_ELT = ?";
            this.executeDeleteQuery(query, idElt, conn);
            JdbcDAOUtils.commit(conn);
            conn.setAutoCommit(true);
        } catch (DataAccessException exc) {
            JdbcDAOUtils.rollbackConnection(conn);
            throw exc;
        } catch (SQLException exc) {
            logger.error("Error while deleting perempted elements", exc);
            JdbcDAOUtils.rollbackConnection(conn);
            throw new DataAccessException(exc.getMessage());
        } finally {
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void deleteProjectAnalysises(String idElt) throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            ElementBean eltBean = this.retrieveElementById(idElt);
            String idPro = eltBean.getProject().getId();
            conn.setAutoCommit(false);
            //on commence par les plans d'action
            String query = "DELETE FROM ACTION_PLAN_CRITERION WHERE ID_ELT IN"
                    + " (SELECT ID_ELT FROM ELEMENT WHERE ID_PRO = ?)";
            this.executeDeleteQuery(query, idPro, conn);
            query = "DELETE FROM ACTION_PLAN WHERE ID_ELT IN"
                    + " (SELECT ID_ELT FROM ELEMENT WHERE ID_PRO = ?)";
            this.executeDeleteQuery(query, idPro, conn);
            //suppression des archi_link
            query = "DELETE FROM ARCHI_LINK WHERE ID_PROJ IN"
                    + "(SELECT DISTINCT ID_ELT FROM ELEMENT WHERE ID_PRO = ? AND ID_TELT = 'EA')";
            this.executeDeleteQuery(query, idPro, conn);
            //suppression des elements fils
            query = "DELETE FROM ELEMENT WHERE ID_MAIN_ELT = ?";
            this.executeDeleteQuery(query, idElt, conn);
            //suppression de l'element
            query = "DELETE FROM ELEMENT WHERE ID_ELT = ?";
            this.executeDeleteQuery(query, idElt, conn);
            JdbcDAOUtils.commit(conn);
            conn.setAutoCommit(true);
        } catch (DataAccessException exc) {
            JdbcDAOUtils.rollbackConnection(conn);
            throw exc;
        } catch (SQLException exc) {
            logger.error("Error while deleting perempted elements", exc);
            JdbcDAOUtils.rollbackConnection(conn);
            throw new DataAccessException(exc.getMessage());
        } finally {
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    private void retrieveAllApplicattionEntitiesBy(Map<String, List<ElementBean>> result, ResultSet rs) throws SQLException {
        while (rs.next()) {
            // Parcours du resultat.
            String idPro = rs.getString("id_pro");
            List<ElementBean> elements = result.get(idPro);
            if (elements == null) {
                elements = new ArrayList<ElementBean>();
                result.put(idPro, elements);
            }
            ElementBean cd = new ElementBean();
            cd.setId(rs.getString("id_elt"));
            cd.setLib(rs.getString("lib_elt"));
            if (idPro != null) {
                ProjectDao projectFacade = ProjectDbmsDao.getInstance();
                cd.setProject(projectFacade.retrieveProjectById(idPro));
            }
            elements.add(cd);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public Map<String, List<ElementBean>> retrieveAllApplicationEntitiesForModel(String idUsa) {
        Map<String, List<ElementBean>> result = new HashMap<String, List<ElementBean>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_EAS_USING_MODEL);
            stmt.setString(1, idUsa);
            // Execution de la requete.
            rs = stmt.executeQuery();
            this.retrieveAllApplicattionEntitiesBy(result, rs);
        } catch (SQLException e) {
            logger.error("Error retrieving eas for model", e);
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
    public Map<String, List<ElementBean>> retrieveAllApplicationEntitiesForDialecte(String idDialecte) {
        Map<String, List<ElementBean>> result = new HashMap<String, List<ElementBean>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_EAS_USING_DIALECTE);
            stmt.setString(1, idDialecte);
            // Execution de la requete.
            rs = stmt.executeQuery();
            this.retrieveAllApplicattionEntitiesBy(result, rs);
        } catch (SQLException e) {
            logger.error("Error retrieving eas for dialecte", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
}

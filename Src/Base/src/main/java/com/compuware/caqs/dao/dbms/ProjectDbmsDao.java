/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:22
 */

package com.compuware.caqs.dao.dbms;

// Imports for DBMS transactions.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class ProjectDbmsDao implements ProjectDao {
    
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    
    private static final String PROJECT_BY_ID_REQUEST = "SELECT id_pro, lib_pro, desc_pro FROM Projet WHERE id_pro=?";
    
    //private static String PROJECT_BY_USAGEID_REQUEST = "SELECT id_pro, lib_pro, desc_pro, usa_pro FROM Projet WHERE id_usa=?";
    
    private static ProjectDao singleton = new ProjectDbmsDao();
    
    public static ProjectDao getInstance() {
    	return ProjectDbmsDao.singleton;
    }
    
    /** Creates a new instance of Class */
    private ProjectDbmsDao() {
    }
    
    /* (non-Javadoc)
	 * @see com.compuware.caqs.dao.dbms.ProjectDao#retrieveProjectById(java.lang.String)
	 */
    public ProjectBean retrieveProjectById(java.lang.String id) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        ProjectBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(PROJECT_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ProjectBean();
                result.setId(rs.getString("id_pro"));
                result.setLib(rs.getString("lib_pro"));
                result.setDesc(rs.getString("desc_pro"));
            }
        }
        catch (SQLException e) {
            logger.error("Error during project retrieve by Id",e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
    private static final String PROJECT_ALL_REQUEST = "SELECT p.id_pro, lib_pro, desc_pro " +
    		"FROM Projet p, Element e " +
    		"WHERE p.id_pro=e.id_pro " +
    		"and e.id_telt='PRJ' " +
    		"and e.dperemption IS NULL " +
    		"ORDER BY lib_pro";
    
    /** {@inheritDoc}
	 */
    public Collection<ProjectBean> retrieveAllProject() {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Collection<ProjectBean> result = new ArrayList<ProjectBean>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(PROJECT_ALL_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ProjectBean prj = new ProjectBean();
                prj.setId(rs.getString("id_pro"));
                prj.setLib(rs.getString("lib_pro"));
                prj.setDesc(rs.getString("desc_pro"));
                result.add(prj);
            }
        }
        catch (SQLException e) {
            logger.error("Error during all project retrieve",e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
    private static final String MODULE_PROJECT_QUERY =
    	"SELECT p.id_pro, p.lib_pro, p.desc_pro"
    	+ " FROM Projet p, Element e"
		+ " WHERE p.id_pro = e.id_pro"
		+ " And e.id_elt = ?";
    
    /** {@inheritDoc}
	 */
    public ProjectBean retrieveModuleProject(java.lang.String idElt) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        ProjectBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(MODULE_PROJECT_QUERY);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new ProjectBean();
                result.setId(rs.getString("id_pro"));
                result.setLib(rs.getString("lib_pro"));
                result.setDesc(rs.getString("desc_pro"));
            }
        }
        catch (SQLException e) {
            logger.error("Error during module project retrieve",e);
            throw new DataAccessException("Error during module project retrieve",e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
    private static final String PROJECT_ELEMENT_ID_QUERY =
    	"Select id_elt, lib_elt, desc_elt From Element"
    	+ " Where id_pro = ?"
    	+ " And id_telt = 'PRJ'";
    
    /** {@inheritDoc}
	 */
    public ElementBean retrieveProjectElementBean(java.lang.String projectId) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        ElementBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(PROJECT_ELEMENT_ID_QUERY);
            pstmt.setString(1, projectId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	result = new ElementBean();
                result.setId(rs.getString("id_elt"));
                result.setLib(rs.getString("lib_elt"));
                result.setDesc(rs.getString("desc_elt"));
            }
        }
        catch (SQLException e) {
            logger.error("Error during project element retrieve",e);
            throw new DataAccessException("Error during project element retrieve",e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
    
    /** {@inheritDoc}
	 */
    public String retrieveProjectElementId(java.lang.String projectId) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(PROJECT_ELEMENT_ID_QUERY);
            pstmt.setString(1, projectId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
            	result = rs.getString("id_elt");
            }
        }
        catch (SQLException e) {
            logger.error("Error during project element retrieve",e);
            throw new DataAccessException("Error during project element retrieve",e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
}

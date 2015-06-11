package com.compuware.carscode.dbms;

import com.compuware.toolbox.dbms.JdbcDAOUtils;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 5 janv. 2006
 * Time: 11:40:14
 * To change this template use File | Settings | File Templates.
 */
public class ProjectDao {

    private static final String RETRIEVE_ALL_PROJECTS_REQUEST = "SELECT DISTINCT id_pro,lib_elt from ELEMENT where ID_PRO <>'ENTRYPOINT' AND ELEMENT.DPEREMPTION is null and ID_TELT='PRJ' order by lib_elt";

    /**
     * Retrieve all active project from CAQS database.
     * @return all active project from CAQS database as a list of ProjectBean.
     */
    public List<ProjectBean> retrieveAllProject() {
        List<ProjectBean> result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // Recupération d'une connection à la base de données..
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DS);
        try {
            pstmt = conn.prepareStatement(RETRIEVE_ALL_PROJECTS_REQUEST);
            // Exécution de la recherche.
            rs = pstmt.executeQuery();
            result = createProjectList(rs);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    private static final String RETRIEVE_PROJECT_BY_USER_QUERY =
    	"SELECT DISTINCT id_pro,lib_elt"
    	+ " FROM ELEMENT elt, DROITS d"
    	+ " WHERE elt.ID_PRO <> 'ENTRYPOINT'"
		+ " AND elt.DPEREMPTION is null"
		+ " AND elt.ID_TELT = 'PRJ'"
		+ " AND elt.ID_ELT = d.ID_ELT"
		+ " AND d.ID_PROFIL_USER = ?"
		+ " ORDER BY lib_elt";
    
    /**
     * Retrieve all active project for the given user from the CAQS database.
     * @param idUser the current user ID.
     * @return all active project fot the current user from CAQS database as a list of ProjectBean.
     */
    public List<ProjectBean> retrieveProjectByUser(String idUser) {
        List<ProjectBean> result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // Recupération d'une connection à la base de données..
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DS);
        try {
            pstmt = conn.prepareStatement(RETRIEVE_PROJECT_BY_USER_QUERY);
            pstmt.setString(1, idUser.trim());
            // Exécution de la recherche.
            rs = pstmt.executeQuery();
            result = createProjectList(rs);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    
    private List<ProjectBean> createProjectList(ResultSet rs) throws SQLException {
    	List<ProjectBean> result = new ArrayList<ProjectBean>();
    	if (rs != null) {
    		ProjectBean projectBean = null;
	        // Parcours du résultat.
	        while (rs.next()) {
	            // On crée un instance de projet par ligne récupérée.
	            projectBean = new ProjectBean();
	            projectBean.setId(rs.getString("id_pro"));
	            projectBean.setLib(rs.getString("lib_elt"));
	            result.add(projectBean);
	        }
    	}
    	return result;
    }
    
}

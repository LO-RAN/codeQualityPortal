package com.compuware.carscode.dbms;

import com.compuware.toolbox.dbms.JdbcDAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 5 janv. 2006
 * Time: 11:40:14
 * To change this template use File | Settings | File Templates.
 */
public class ProjectDao {

    private static final String RETRIEVE_ALL_PROJECTS_REQUEST = "SELECT DISTINCT id_pro,lib_elt from ELEMENT where ID_PRO <>'ENTRYPOINT' AND ELEMENT.DPEREMPTION is null and ID_TELT='PRJ'";

    /**
     * Retrieve all active project from CAQS database.
     * @return all active project from CAQS database as a list of ProjectBean.
     */
    public HashMap retrieveAllProject() {
        HashMap result = new HashMap();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // Recupération d'une connection à la base de données..
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DS);
        try {
            pstmt = conn.prepareStatement(RETRIEVE_ALL_PROJECTS_REQUEST);
            // Exécution de la recherche.
            rs = pstmt.executeQuery();
            // Parcours du résultat.
            while (rs.next()) {
                // On crée un instance de projet par ligne récupérée.
                ProjectBean projectBean = new ProjectBean();
                projectBean.setId(rs.getString("id_pro"));
                projectBean.setLib(rs.getString("lib_elt"));
                result.put(projectBean.getId(), projectBean);
            }
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

}

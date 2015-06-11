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
import java.util.ArrayList;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.LabelDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.Label;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.LabelResume;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class LabelDbmsDao implements LabelDao {
    
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    
    private static final String LABEL_BY_ID_REQUEST = "SELECT id_label, lib_label, desc_label, id_pro, id_bline, statut_label, cuser_label, dinst_label, dmaj_label, label_link FROM Labellisation WHERE id_label=?";
    
    private static final String LABEL_BY_ELEMENTID_REQUEST = "Select id_label, lib_label, desc_label, statut_label, cuser_label, dinst_label, dmaj_label, label_link"
                                                            + " From Labellisation l"
                                                            + " Where l.id_pro=?"
                                                            + " And l.id_bline=?"
                                                            + " And l.id_label in ("
                                                                    + " Select distinct f.id_label From Facteur_Bline f"
                                                                    + " Where f.id_elt=?"
                                                            + " )";
    
    private static LabelDao singleton = new LabelDbmsDao();
    
    /** Creates a new instance of Class */
    private LabelDbmsDao() {
    }
    
    public static LabelDao getInstance() {
    	return singleton;
    }
    
    /** {@inheritDoc}
	 */
    public LabelBean retrieveLabelById(String id) {
        LabelBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String link = null;
        try {
            pstmt = connection.prepareStatement(LABEL_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new LabelBean();
                result.setId(rs.getString("id_label"));
                result.setLib(rs.getString("lib_label"));
                result.setDesc(rs.getString("desc_label"));
                result.setDinst(rs.getTimestamp("dinst_label"));
                result.setDmaj(rs.getTimestamp("dmaj_label"));
                result.setStatut(rs.getString("statut_label"));
                result.setUser(rs.getString("cuser_label"));
                String idBline = rs.getString("id_bline");
                if (idBline != null) {
                    BaselineDao baselineFacade = BaselineDbmsDao.getInstance();
                    BaselineBean baseline = baselineFacade.retrieveBaselineAndProjectById(idBline);
                    result.setBaseline(baseline);
                }
                link = rs.getString("label_link");
            }
        }
        catch (SQLException e) {
            logger.error("Error during Label retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        
        if (link != null) {
            LabelBean labelLink = retrieveLabelById(link);
            result.setLabelLink(labelLink);
        }
        
        return result;
    }
    
    /** {@inheritDoc}
	 */
    public LabelBean retrieveLabelById(BaselineBean baseline, java.lang.String id) {
        LabelBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String link = null;
        try {
            pstmt = connection.prepareStatement(LABEL_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new LabelBean();
                result.setId(rs.getString("id_label"));
                result.setLib(rs.getString("lib_label"));
                result.setDesc(rs.getString("desc_label"));
                result.setDinst(rs.getTimestamp("dinst_label"));
                result.setDmaj(rs.getTimestamp("dmaj_label"));
                result.setStatut(rs.getString("statut_label"));
                result.setUser(rs.getString("cuser_label"));
                result.setBaseline(baseline);
                link = rs.getString("label_link");
            }
        }
        catch (SQLException e) {
            logger.error("Error during Label retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        if (link != null) {
            LabelBean labelLink = retrieveLabelById(link);
            result.setLabelLink(labelLink);
        }
        
        return result;
    }
    
    /** {@inheritDoc}
	 */
    public LabelBean retrieveLabelByElement(ElementBean eltBean) {
        LabelBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String link = null;
        try {
            pstmt = connection.prepareStatement(LABEL_BY_ELEMENTID_REQUEST);
            BaselineBean baseline = eltBean.getBaseline();
            ProjectBean project = baseline.getProject();
            pstmt.setString(1, project.getId());
            pstmt.setString(2, baseline.getId());
            pstmt.setString(3, eltBean.getId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new LabelBean();
                result.setId(rs.getString("id_label"));
                result.setLib(rs.getString("lib_label"));
                result.setDesc(rs.getString("desc_label"));
                result.setDinst(rs.getTimestamp("dinst_label"));
                result.setDmaj(rs.getTimestamp("dmaj_label"));
                result.setStatut(rs.getString("statut_label"));
                result.setUser(rs.getString("cuser_label"));
                result.setBaseline(baseline);
                link = rs.getString("label_link");
            }
        }
        catch (SQLException e) {
            logger.error("Error during Label retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        
        if (link != null && result!=null) {
            LabelBean labelLink = retrieveLabelById(link);
            result.setLabelLink(labelLink);
        }
        
        return result;
    }
    
    public void setLabel(LabelBean labelBean, ElementBean eltBean) throws DataAccessException {
        String selectRequest = "Select lib_label"
            + " From Labellisation"
            + " Where id_label=?";

	    String insertRequest = "INSERT INTO Labellisation (id_label, id_pro, id_bline, lib_label, desc_label, statut_label, cuser_label, dinst_label, label_link)"
	            + " VALUES (?, ?, ?, ?, ?, ?, ?, {fn now()}, ?)";
	
	    String updateRequest = "UPDATE Labellisation"
	            + " SET lib_label=?, desc_label=?, dmaj_label={fn now()}, statut_label=?, cuser_label=?"
	            + " WHERE id_label=?";
	
	    Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        conn.setAutoCommit(false);
	        pstmt = conn.prepareStatement(selectRequest);
	        pstmt.setString(1, labelBean.getId());
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
				pstmt.close();
				pstmt = conn.prepareStatement(updateRequest);
				pstmt.setString(1, labelBean.getLib());
				pstmt.setString(2, labelBean.getDesc());
				pstmt.setString(3, labelBean.getStatut());
				pstmt.setString(4, labelBean.getUser());
				pstmt.setString(5, labelBean.getId());
				pstmt.executeUpdate();
			} else {
				pstmt.close();
				pstmt = conn.prepareStatement(insertRequest);
				pstmt.setString(1, labelBean.getId());
				pstmt.setString(2, eltBean.getBaseline().getProject().getId());
				pstmt.setString(3, eltBean.getBaseline().getId());
				pstmt.setString(4, labelBean.getLib());
				pstmt.setString(5, labelBean.getDesc());
				pstmt.setString(6, labelBean.getStatut());
				pstmt.setString(7, labelBean.getUser());
				if (labelBean.getLabelLink() != null) {
					pstmt.setString(8, labelBean.getLabelLink().getId());
				}
				else {
					pstmt.setNull(8, java.sql.Types.VARCHAR);
				}
				pstmt.executeUpdate();
			}
		    updateElement(eltBean, labelBean, false, conn);
	    }
	    catch (SQLException e) {
	        throw new DataAccessException("Error updating label", e);
	    }
	    finally {
	        JdbcDAOUtils.closeResultSet(rs);
	        JdbcDAOUtils.closePrepareStatement(pstmt);
	        JdbcDAOUtils.closeConnection(conn);
	    }
    }
    	
	public void performInsert(LabelBean label) throws DataAccessException {
        String request = "INSERT INTO Labellisation (id_label, id_pro, id_bline, lib_label, desc_label, statut_label, cuser_label, dinst_label)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, {fn now()})";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        if (connection != null) {
            PreparedStatement pstmt = null;
            try{
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, label.getId());
                pstmt.setString(2, label.getProjet().getId());
                pstmt.setString(3, label.getBaseline().getId());
                pstmt.setString(4, label.getLib());
                pstmt.setString(5, label.getDesc());
                pstmt.setString(6, label.getStatut());
                pstmt.setString(7, label.getUser());
                pstmt.executeUpdate();
            }
            catch(SQLException e){
                logger.error("Error during label insert", e);
                throw new DataAccessException("Error during label insert", e);
            }
            finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    
    public void performUpdate(LabelBean label) throws DataAccessException {
        String request = "UPDATE Labellisation"
                    + " SET lib_label=?, desc_label=?, dmaj_label={fn now()}, statut_label=?, cuser_label=?"
                    + " WHERE id_label=?";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        if (connection != null) {
            PreparedStatement pstmt = null;
            try{
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, label.getLib());
                pstmt.setString(2, label.getDesc());
                pstmt.setString(3, label.getStatut());
                pstmt.setString(4, label.getUser());
                pstmt.setString(5, label.getId());
                pstmt.executeUpdate();
                JdbcDAOUtils.commit(connection);
            }
            catch(SQLException e){
                logger.error("Error during label update", e);
                throw new DataAccessException("Error during label update", e);
            }
            finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
    
    public void updateElement(ElementBean eltBean,
            LabelBean labelBean, boolean onlyEmpty,
            Connection existingConnection) throws DataAccessException {
        String request = "UPDATE Facteur_bline"
                    + " SET id_label=?"
                    + " WHERE id_elt=?"
                    + " AND id_bline=?";
        
        if (onlyEmpty) {
            request += " AND id_label is null";
        }

        Connection connection = existingConnection;
        if (connection != null) {
            PreparedStatement pstmt = null;
            try{
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, labelBean.getId());
                pstmt.setString(2, eltBean.getId());
                pstmt.setString(3, labelBean.getBaseline().getId());
                pstmt.executeUpdate();
                JdbcDAOUtils.commit(connection);
            }
            catch(SQLException e){
                throw new DataAccessException(e);
            }
            finally {
                JdbcDAOUtils.closePrepareStatement(pstmt);
            }
            if (!"DEMAND".equals(labelBean.getStatut())) {
                propageLabel(eltBean, labelBean, connection);
            }
        }
    }
    
    /** Requête de récupération des sous-éléments. */
    private static final String SUB_ELT_REQUEST = "Select id_elt, id_telt From Element, Elt_links"
                                                + " Where id_elt = elt_fils"
                                                + " And elt_pere = ?"
                                                + " And id_pro = ?"
                                                + " And dperemption is null"
                                                + " And dinst_elt < ?"
                                                + " And type='T'";
    
    private void propageLabel(ElementBean eltBean, LabelBean labelBean,
            Connection existingConnection) throws DataAccessException {
        Connection connection = existingConnection;
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        try {
            // Préparation de la requête de récupération avec les informations de l'élément.
            eltstmt = connection.prepareStatement(SUB_ELT_REQUEST);
            eltstmt.setString(1, eltBean.getId());
            eltstmt.setString(2, labelBean.getProjet().getId());
            eltstmt.setTimestamp(3, labelBean.getBaseline().getDmaj());
            // Exécution de la requête.
            rs = eltstmt.executeQuery();
            ElementBean currentElt = null;
            // Parcours du résultat de la requête.
            while (rs.next()) {
            	currentElt = new ElementBean();
                // Identifiant du sous-élément.
                eltBean.setId(rs.getString(1));
                // Type du sous-élément.
                String type = rs.getString(2);
                if (type != null && !type.equals(ElementType.MET) && !type.equals(ElementType.CLS)) {
                    this.updateElement(currentElt, labelBean, true, connection);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e.toString());
            throw new DataAccessException(e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
        }
    }    
    
    public Label retrieveLinkedLabel(String idLabel, String idPro,
            String idBline) {
    	Label result = null;
        if (idLabel != null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            String request = "Select lib_label, desc_label, statut_label, cuser_label, dinst_label, dmaj_label, label_link"
                                + " From Labellisation"
                                + " Where id_label=?";
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try{
                pstmt = connection.prepareStatement(request);
                pstmt.setString(1, idLabel);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                	result = new Label(idLabel, rs.getString(1), idPro, idBline, rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5), rs.getDate(6), rs.getString(7));
                }
            }
            catch(Exception e){
                logger.error(e.toString());
            }
            finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
	            JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
        
    private static final String LINK_LABEL_QUERY = "Update Labellisation Set label_link=? Where id_label=?";

    public void linkLabels(String idLabelOld, String idLabelNew)
            throws DataAccessException {
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(LINK_LABEL_QUERY);
            pstmt.setString(1, idLabelOld);
            pstmt.setString(2, idLabelNew);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(conn);
        }
        catch(SQLException e){
            throw new DataAccessException("Erreur lors de la liaison des labels "+idLabelOld+" et "+idLabelNew, e);
        }
        finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
        	JdbcDAOUtils.rollbackConnection(conn);
            JdbcDAOUtils.closeConnection(conn);
        }
    }

    private static final String RETRIEVE_ALL_QUERY = "Select distinct(label.id_label), label.lib_label, label.dinst_label, pro.id_pro, pro.lib_pro, label.cuser_label, bline.id_bline, bline.lib_bline, elt.id_elt, elt.lib_elt, elt.id_telt"
        + " From Labellisation label, Facteur_bline facb, Facteur fac, Element elt, Projet pro, Baseline bline, Droits dr"
        + " Where label.statut_label= ?"
        + " And dr.id_profil_user = ?"
        + " And facb.id_pro=label.id_pro"
        + " And facb.id_bline=label.id_bline"
        + " And facb.id_label=label.id_label"
        + " And elt.id_elt=dr.id_elt"
        + " And elt.id_elt=facb.id_elt"
        + " And elt.id_pro=label.id_pro"
        + " And bline.id_bline=label.id_bline"
        + " And fac.id_fact=facb.id_fac"
        + " And pro.id_pro=label.id_pro"
        + " order by pro.id_pro, label.cuser_label, bline.id_bline";
    
    public List<LabelResume> retrieveAllLabels(String req, String userId) throws DataAccessException {
        List<LabelResume> result = new ArrayList<LabelResume>();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            pstmt = conn.prepareStatement(RETRIEVE_ALL_QUERY);
            pstmt.setString(1, req);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(new LabelResume(rs.getString(1), rs.getString(2), rs.getDate(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)));
            }
        }
        catch(SQLException e){
            logger.error("Erreur lors de la recuperation des Labels de type "+req, e);
            throw new DataAccessException("Erreur lors de la recuperation des Labels de type "+req, e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }
    
}

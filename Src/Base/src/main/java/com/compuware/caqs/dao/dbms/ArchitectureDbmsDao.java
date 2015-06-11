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
import java.util.Map;

import com.compuware.caqs.business.load.ElementOfArchitecture;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.ArchitectureDao;
import com.compuware.caqs.domain.dataschemas.LinkInfos;
import com.compuware.caqs.domain.dataschemas.callsto.CallBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 *
 */
public class ArchitectureDbmsDao implements ArchitectureDao {

    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);

    private static ArchitectureDao singleton = new ArchitectureDbmsDao();
    
    public static ArchitectureDao getInstance() {
    	return singleton;
    }
    
    private ArchitectureDbmsDao() {
    }
	
    
    private static final String ARCHI_REQUEST = "SELECT /*+ ALL_ROWS ORDERED USE_NL(leb, lr, elt2, elt1) */"
        + " distinct elt1.ID_ELT, elt1.LIB_ELT, elt1.DESC_ELT, pkg1.LIB_PACK,"
        + " elt2.ID_ELT, elt2.LIB_ELT, elt2.DESC_ELT, pkg2.LIB_PACK"
        + " FROM LINK_ELT_BLINE leb, LINK_REAL lr, Element elt2, Package pkg2, Element elt1, Package pkg1"
        + " WHERE lr.ID_BLINE=?"
        + " AND lr.ID_PROJ=?"
        + " AND lr.STATE=?"
        + " AND lr.ID_LINK=leb.REAL_LINK_ID"
        + " AND lr.ID_BLINE=leb.ID_BLINE"
        + " AND leb.ELT_FROM_ID=elt1.ID_ELT"
        + " AND elt1.ID_PACK=pkg1.ID_PACK"
        + " AND leb.ELT_TO_ID=elt2.ID_ELT"
        + " AND elt2.ID_PACK=pkg2.ID_PACK";

    private static final String COPIER_COLLER_REQUEST = "SELECT /*+ ALL_ROWS ORDERED USE_NL(leb, lr, elt2, elt1) */"
        + " distinct elt1.ID_ELT, elt1.LIB_ELT, elt1.DESC_ELT,"
        + " elt2.ID_ELT, elt2.LIB_ELT, elt2.DESC_ELT"
        + " FROM LINK_ELT_BLINE leb, LINK_REAL lr, Element elt2, Element elt1"
        + " WHERE lr.ID_BLINE=?"
        + " AND lr.ID_LINK=leb.REAL_LINK_ID"
        + " AND lr.ID_BLINE=leb.ID_BLINE"
        + " AND leb.ELT_FROM_ID=elt1.ID_ELT"
        + " AND ((leb.ELT_FROM_ID=? AND leb.ELT_TO_ID=elt2.ID_ELT)"
            + " OR (leb.ELT_TO_ID=? AND leb.ELT_FROM_ID=elt2.ID_ELT))"
        + " AND lr.STATE=?";
    
	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.interfaces.ArchitectureDao#retrieveLinks(java.lang.String, java.lang.String, int)
	 */
	public Collection retrieveLinks(String idElt, String idBline, int state) {
        Collection result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (state == 20) {
                pstmt = conn.prepareStatement(COPIER_COLLER_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idElt);
                pstmt.setString(3, idElt);
                pstmt.setInt(4, state);
            }
            else {
                pstmt = conn.prepareStatement(ARCHI_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idElt);
                pstmt.setInt(3, state);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkInfos li;
                if (state == 20) {
                    li = new LinkInfos(rs.getString(1), rs.getString(2), rs.getString(3), "", rs.getString(4), rs.getString(5), rs.getString(6), "");
                }
                else {
                    li = new LinkInfos(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                }
                result.add(li);
            }
        }
        catch(SQLException e){
            mLog.error("Erreur lors de la recuperation des Liens d'architecture.");
            mLog.error("id_bline="+idBline+", id_elt="+idElt+", state="+state, e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
	}

    private static final String INSERT_RECURSIVITY_QUERY = "INSERT INTO QAMETRIQUE (ID_ELT,ID_MET,ID_BLINE,VALBRUTE_QAMET) values (?,'RECURSIVITE',?,1)";
    private static final String INSERT_VALID_RELATION_QUERY = "INSERT INTO LINK_ELT_BLINE (LINK_ID,ELT_FROM_ID,ELT_TO_ID,ID_BLINE,TYPE) values (?,?,?,?,'CALLSTO')";

    /* (non-Javadoc)
	 * @see com.compuware.caqs.dao.interfaces.ArchitectureDao#insertCalls(Collection<CallBean>, java.lang.String)
	 */
	public void insertCalls(Collection<CallBean> callColl, String idBline) throws DataAccessException {
		if (callColl != null && callColl.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement recursivityPstmt = null;
            PreparedStatement callPstmt = null;
            Map<String, String> recursivityFound = new HashMap<String, String>();
            CallBean currentCall = null;
            try {
	            connection.setAutoCommit(false);
            	recursivityPstmt = connection.prepareStatement(INSERT_RECURSIVITY_QUERY);
            	callPstmt = connection.prepareStatement(INSERT_VALID_RELATION_QUERY);
            	
            	Iterator<CallBean> callIter = callColl.iterator();
            	while (callIter.hasNext()) {
            		currentCall = callIter.next();
            		callPstmt.setString(1, IDCreator.getID());
            		callPstmt.setString(2, currentCall.getIdFrom());
            		callPstmt.setString(3, currentCall.getIdTo());
            		callPstmt.setString(4, idBline);
            		callPstmt.addBatch();
                    if (currentCall.getIdFrom() != null && currentCall.getIdFrom().equals(currentCall.getIdTo())
                    		&& !recursivityFound.containsKey(currentCall.getIdFrom())) {
                    	recursivityPstmt.setString(1, currentCall.getIdFrom());
                    	recursivityPstmt.setString(2, idBline);
                    	recursivityPstmt.addBatch();
                    	recursivityFound.put(currentCall.getIdFrom(), currentCall.getIdFrom());
                    }
            	}
            	callPstmt.executeBatch();
            	recursivityPstmt.executeBatch();
	            JdbcDAOUtils.commit(connection);
	            connection.setAutoCommit(true);
            }
            catch(SQLException e){
                mLog.error("Error insert element calls for baseline " + idBline, e);
                JdbcDAOUtils.rollbackConnection(connection);
                throw new DataAccessException("Error insert element calls for baseline " + idBline, e);
            }
            finally {
                JdbcDAOUtils.closePrepareStatement(recursivityPstmt);
                JdbcDAOUtils.closePrepareStatement(callPstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
		}
	}

    private static final String RETRIEVE_ARCHITECTURE_ELEMENT_QUERY =
    	"SELECT ELEMENT.ID_ELT,"
        + " ELEMENT.DESC_ELT, ELEMENT.ID_PACK FROM ELEMENT"
        + " WHERE ELEMENT.ID_MAIN_ELT = ?"
        + " And DPEREMPTION IS NULL";        

    private static final String RETRIEVE_ARCHITECTURE_ELEMENT_BY_TYPE_QUERY =
    	RETRIEVE_ARCHITECTURE_ELEMENT_QUERY
        + " And ELEMENT.ID_TELT = ?";        

	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.interfaces.ArchitectureDao#retrieveArchitectureElement(java.lang.String, com.compuware.caqs.business.load.db.DataFileType)
	 */
	public Map<String, ElementOfArchitecture> retrieveArchitectureElement(
			String idModule, DataFileType typeElement)
			throws DataAccessException {
		Map<String, ElementOfArchitecture> childrenHash = new HashMap<String, ElementOfArchitecture>();        
        
		String query = null;
        if (typeElement != null) {
        	query = RETRIEVE_ARCHITECTURE_ELEMENT_BY_TYPE_QUERY;
    	 }
        else {
        	query = RETRIEVE_ARCHITECTURE_ELEMENT_QUERY;
        }
        
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement sta = null;
        ResultSet rs = null;
        try{
            sta = connection.prepareStatement(query);
            sta.setString(1, idModule);
            if (typeElement != null) {
            	sta.setString(2, typeElement.getName());
            }
            rs = sta.executeQuery();
            while(rs.next()){
                String id = rs.getString("id_elt");
                String desc = rs.getString("desc_elt");
                String idModArchi = rs.getString("id_pack");                
                childrenHash.put(desc , new ElementOfArchitecture(id,desc,idModArchi) );
            }
        }
        catch(SQLException e){
            mLog.error("Error getting module architecture children", e);
            throw new DataAccessException("Error getting module architecture children", e);
        }
        finally{
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(sta);
            JdbcDAOUtils.closeConnection(connection);
        }
                
        return childrenHash;
    }

}

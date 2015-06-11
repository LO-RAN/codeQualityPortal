/*
 * RealLinkCreator.java
 * Created on 6 novembre 2002, 11:54
 */

package com.compuware.caqs.business.load.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class RealLinkCreator{// extends DbConnect{

    // déclaration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("Architecture");
    
    //real link types
    public static final int REAL_LINK_ANTI = 0;
    public static final int REAL_LINK_OK = 1;
    public static final int REAL_LINK_NOTEXPECTED = 2;
        
    protected String m_eaId;
    protected String m_baseLineId;
    
    //number of calls by types
    protected int m_goodCalls = 0;
    protected int m_nonPrevuCalls = 0;        
    protected int m_archiLinkCount = 0;
    protected int m_contreSensCalls = 0;
    
    protected Map m_elementPackageHash = new HashMap();
    
    /** Creates a new instance of RealLinkCreator */
    public RealLinkCreator(String eaId, String baseLineId) {
        this.m_eaId = eaId;
        this.m_baseLineId = baseLineId;
    }
    
    public void execute() throws LoaderException {
        RealLinkCreator.logger.info("Start : RealLinkCreation");
        
        //Start DB Connection.
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        
        try {
	        //0. Delete all Real links for ea baseline;
	        this.deleteAllRealLinks(conn);
	        
	        //1. get the ELEMENTS/PACKAGE ASSIGNEMENT
	        this.getElementPackageAssignement(conn);
	        
	        //2. Get the architecture links count
	        this.countArchiLink(conn);
	        
	        //3. Create the real links for ea/baseline
	        this.createRealLinks(conn);
	        
	        //4. update baseline metrics
	        this.updateMetrics(conn);
        }
        catch (DataAccessException e) {
        	logger.error("Error creating real links", e);
        	throw new LoaderException("Error creating real links", e);
        }
        finally {
	        //Close the connection
	        JdbcDAOUtils.closeConnection(conn);
        }
        RealLinkCreator.logger.info("End : RealLinkCreation");
    }
    
    protected void createRealLinks(Connection conn){
        PreparedStatement sta = null;
        PreparedStatement sta2 = null;
        PreparedStatement sta3 = null;
        ResultSet rs = null;
        //2. get LINK_ELT_BLINE
        try{
            //get real relations between methods
            String selectElementsQuery = "SELECT LINK_ID,ELT_FROM_ID, ELT_TO_ID FROM LINK_ELT_BLINE WHERE ID_BLINE=? AND TYPE='CALLSTO'";
            sta = conn.prepareStatement(selectElementsQuery);
            RealLinkCreator.logger.debug("SELECT LINK_ID,ELT_FROM_ID, ELT_TO_ID FROM LINK_ELT_BLINE WHERE ID_BLINE='"+this.m_baseLineId+"' AND TYPE='CALLSTO'");
            sta.setString(1, this.m_baseLineId);
            rs = sta.executeQuery();
            
            while(rs.next()){
                String idLinkEltBline = rs.getString(1);
                String fromEltId = rs.getString(2);
                String toEltId = rs.getString(3);
                String fromPackageId = (String) this.m_elementPackageHash.get(fromEltId);
                String toPackageId = (String) this.m_elementPackageHash.get(toEltId);
                if ( (fromPackageId!=null)&&(toPackageId!=null) && (fromPackageId.compareTo(toPackageId)!=0) ){
                    int state = this.getRealLinkState(fromPackageId, toPackageId, conn);
                    
                    //try to find if it exist
                    String realLinkId = this.getRealLinkId(fromPackageId, toPackageId, state, conn);
                    
                    if(realLinkId == null){
                        realLinkId = IDCreator.getID();
                        String insertRealLinkQuery = "INSERT INTO LINK_REAL (ID_LINK,ID_FROM,ID_TO,ID_PROJ,ID_BLINE,STATE) values(?,?,?,?,?,?)";
                        sta2 = conn.prepareStatement(insertRealLinkQuery);
                        sta2.setString(1, realLinkId);
                        sta2.setString(2, fromPackageId);
                        sta2.setString(3, toPackageId);
                        sta2.setString(4, this.m_eaId);
                        sta2.setString(5, this.m_baseLineId);
                        sta2.setInt(6, state);
                        RealLinkCreator.logger.debug("INSERT INTO LINK_REAL (ID_LINK,ID_FROM,ID_TO,ID_PROJ,ID_BLINE,STATE) values('"+realLinkId+"','"+fromPackageId+"','"+toPackageId+"','"+this.m_eaId+"','"+this.m_baseLineId+"',"+state+")");
                        sta2.executeUpdate();
                        JdbcDAOUtils.closePrepareStatement(sta2);
                    }
                    
                    //count links
                    switch (state){
                        case RealLinkCreator.REAL_LINK_ANTI: this.m_contreSensCalls++;
                        break;
                        case RealLinkCreator.REAL_LINK_OK: this.m_goodCalls++;
                        break;
                        case RealLinkCreator.REAL_LINK_NOTEXPECTED: this.m_nonPrevuCalls++;
                        break;
                        default :
                            RealLinkCreator.logger.info("Unknown Real Link state : " + state);
                    }
                    //end CountLinks
                    
                    String updateLinkEltBlineQuery = "UPDATE LINK_ELT_BLINE SET REAL_LINK_ID=? where LINK_ID=?";
                    sta3 = conn.prepareStatement(updateLinkEltBlineQuery);
                    sta3.setString(1, realLinkId);
                    sta3.setString(2, idLinkEltBline);
                    RealLinkCreator.logger.debug("UPDATE LINK_ELT_BLINE SET REAL_LINK_ID='"+realLinkId+"' where LINK_ID='"+idLinkEltBline+"'");
                    sta3.executeUpdate();
                    JdbcDAOUtils.closePrepareStatement(sta3);
                }
                else{
                    RealLinkCreator.logger.debug("fromEltId/fromPackageId " +fromEltId+"/"+toEltId);
                    RealLinkCreator.logger.debug("fromPackageId/fromPackageId " +fromPackageId+"/"+toPackageId);
                }
            }
        }
        catch(Exception e){
            RealLinkCreator.logger.error(e.toString());
            JdbcDAOUtils.rollbackConnection(conn);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(sta2);
            JdbcDAOUtils.closePrepareStatement(sta3);
            JdbcDAOUtils.closePrepareStatement(sta);
        }
        //end 2.
    }
    
    protected String getRealLinkId(String fromPackageId,String toPackageId,int state, Connection conn){
        String realLinkId = null;
        PreparedStatement realLinkSta = null;
        ResultSet rsRealLink = null;
        try{
            String realLinExistsQuery = "SELECT ID_LINK FROM LINK_REAL WHERE ID_FROM=? AND ID_TO=? AND ID_PROJ=? AND ID_BLINE=? AND STATE=?";
            realLinkSta = conn.prepareStatement(realLinExistsQuery);
            realLinkSta.setString(1, fromPackageId);
            realLinkSta.setString(2, toPackageId);
            realLinkSta.setString(3, this.m_eaId);
            realLinkSta.setString(4, this.m_baseLineId);
            realLinkSta.setInt(5, state);
            rsRealLink = realLinkSta.executeQuery();
            if(rsRealLink.next()){
                realLinkId = rsRealLink.getString(1);
            }
        }
        catch(Exception e){
            RealLinkCreator.logger.error(e.toString());
        }
        finally {
            JdbcDAOUtils.closeResultSet(rsRealLink);
            JdbcDAOUtils.closePrepareStatement(realLinkSta);
        }
        return realLinkId;
    }
    
    protected int getRealLinkState(String fromPackageId, String toPackageId, Connection conn){
        int state=RealLinkCreator.REAL_LINK_NOTEXPECTED;
        PreparedStatement sta1 = null;
        ResultSet rs1 = null;
        PreparedStatement sta11 = null;
        ResultSet rs11 = null;
        
        try{
            //find real link state
            String isArchiLinkQuery = "SELECT count(ID_LINK) FROM ARCHI_LINK WHERE ID_FROM=? AND ID_TO=?";
            sta1 = conn.prepareStatement(isArchiLinkQuery);
            sta1.setString(1, fromPackageId);
            sta1.setString(2, toPackageId);
            rs1 = sta1.executeQuery();
            boolean edgeExists=false;
            while(rs1.next()){
                int numberOfArchiLink = rs1.getInt(1);
                if (numberOfArchiLink > 0){
                    edgeExists=true;
                }
            }
            //count archiLink
            
            if(edgeExists){
                state=RealLinkCreator.REAL_LINK_OK;
            }
            else{
                String isAntiArchiLinkQuery = "SELECT count(ID_LINK) FROM ARCHI_LINK WHERE ID_FROM=? AND ID_TO=?";
                sta11 = conn.prepareStatement(isAntiArchiLinkQuery);
                sta11.setString(1, toPackageId);
                sta11.setString(2, fromPackageId);
                rs11 = sta11.executeQuery();
                boolean AntiEdgeExists = false;
                while(rs11.next()){
                    int countAntiLink = rs11.getInt(1);
                    if (countAntiLink>0){
                        AntiEdgeExists = true;
                    }
                }
                if(AntiEdgeExists){
                    state=RealLinkCreator.REAL_LINK_ANTI;
                }
            }
        }
        catch(Exception e){
            RealLinkCreator.logger.error(e.toString());
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs1);
            JdbcDAOUtils.closePrepareStatement(sta1);
            JdbcDAOUtils.closeResultSet(rs11);
            JdbcDAOUtils.closePrepareStatement(sta11);
        }
        return state;
        //End find real link state
    }
    
    protected void updateMetrics(Connection conn) throws DataAccessException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
        metriqueDao.setMetrique(this.m_eaId, this.m_baseLineId, "LIENS_CS", this.m_contreSensCalls, conn, true);
        metriqueDao.setMetrique(this.m_eaId, this.m_baseLineId, "LIENS_NP", this.m_nonPrevuCalls, conn, true);
    }
    
    protected void countArchiLink(Connection conn){
        this.m_archiLinkCount = 0;
        PreparedStatement sta0 = null;
        ResultSet rs0 = null;
        try{
            String selectArchiLinkQuery = "SELECT count(ID_LINK) FROM ARCHI_LINK WHERE ID_PROJ =?";
            sta0 = conn.prepareStatement(selectArchiLinkQuery);
            sta0.setString(1, this.m_eaId);
            RealLinkCreator.logger.debug("SELECT count(ID_LINK) FROM ARCHI_LINK WHERE ID_PROJ ='"+ this.m_eaId+"'");
            rs0 = sta0.executeQuery();
            if (rs0.next()){
                this.m_archiLinkCount = rs0.getInt(1);
            }
            RealLinkCreator.logger.info("Number of archi_link : " + this.m_archiLinkCount);
        }
        catch(Exception e){
            RealLinkCreator.logger.error(e.toString());
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs0);
            JdbcDAOUtils.closePrepareStatement(sta0);
        }
    }
    
    protected void deleteAllRealLinks(Connection conn){
        //delete all real links
        PreparedStatement statement0 = null;
        try{
            //Loading Ok. Update MAJ date of EA.
            //type 20 is used for Cut/Paste Links.
            String deleteAllQuery = "DELETE LINK_REAL WHERE ID_BLINE=? AND ID_PROJ=? AND STATE<20";
            statement0 = conn.prepareStatement(deleteAllQuery);
            statement0.setString(1, this.m_baseLineId);
            statement0.setString(2, this.m_eaId);
            RealLinkCreator.logger.debug("DELETE LINK_REAL WHERE ID_BLINE='"+this.m_baseLineId+"' AND ID_PROJ='"+this.m_eaId+"' AND STATE<20");
            statement0.executeUpdate();
        }
        catch(Exception e){
            RealLinkCreator.logger.error(e.toString());
            JdbcDAOUtils.rollbackConnection(conn);
        }
        finally {
            JdbcDAOUtils.closePrepareStatement(statement0);
        }
    }
    
    protected void getElementPackageAssignement(Connection conn){
        //gets package assigned to element
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try{
            //Loading Ok. Update MAJ date of EA.
            String selectRequest = "SELECT elt.ID_ELT, elt.ID_PACK FROM Element elt, PACKAGE pkg WHERE pkg.ID_PROJ=? AND elt.ID_PACK=pkg.ID_PACK";
            pstmt = conn.prepareStatement(selectRequest);
            //String selectPackageQuery = "SELECT ID_PACK FROM PACKAGE WHERE ID_PROJ='"+this.m_eaId+"'";
            RealLinkCreator.logger.debug("SELECT elt.ID_ELT, elt.ID_PACK FROM Element elt, PACKAGE pkg WHERE pkg.ID_PROJ="+this.m_eaId+" AND elt.ID_PACK=pkg.ID_PACK");
            pstmt.setString(1, this.m_eaId);
            rs = pstmt.executeQuery();
            while(rs.next()){
                String idElt = rs.getString(1);
                String idPack = rs.getString(2);
                this.m_elementPackageHash.put(idElt,idPack);
            }
        }
        catch(Exception e){
            RealLinkCreator.logger.error(e.toString());
            JdbcDAOUtils.rollbackConnection(conn);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }    
}

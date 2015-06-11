/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:25
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
import com.compuware.caqs.dao.interfaces.FactorEvolutionDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.CriterionEvolutionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorEvolutionBean;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;

/**
 *
 * @author  cwfr-fdubois
 */
public class FactorEvolutionDbmsDao implements FactorEvolutionDao {
    
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    
    private static FactorEvolutionDbmsDao singleton = new FactorEvolutionDbmsDao();
    
    public static FactorEvolutionDbmsDao getInstance() {
    	return singleton;
    }
    
    /** Creates a new instance of Class */
    private FactorEvolutionDbmsDao() {
    }

    /* (non-Javadoc)
	 * @see com.compuware.caqs.dao.dbms.FactorEvolutionDao#retrieveFactorEvolution(com.compuware.caqs.domain.dataschemas.ElementBean)
	 */
    public Collection<FactorBean> retrieveFactorEvolution(ElementBean eltBean, BaselineBean bline) {
        Collection<FactorBean> result = new ArrayList<FactorBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sqlRequest = "Select facb.id_fac, facb.note_facbl, facb.tendance From Facteur_bline facb"
                             + " Where facb.id_elt=?"
                             + " And facb.id_bline=?"
                             + " order by facb.id_fac";
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            pstmt.setString(2, eltBean.getBaseline().getId());
            rs = pstmt.executeQuery();
            FactorBean factorBean = null;
            while (rs.next()) {
                String idFac = rs.getString(1);
                double noteFacbl = rs.getDouble(2);
            	factorBean = new FactorBean();
            	factorBean.setId(idFac);
            	factorBean.setNote(noteFacbl);
            	double tendance = rs.getDouble(3);
            	if (!rs.wasNull()) {
            		factorBean.setTendance(new Double (tendance));
            	}
                result.add(factorBean);
            }
        }
        catch(SQLException e){
            logger.error("Erreur lors de la recuperation de l'evolution des facteurs:");
            logger.error("id_elt="+eltBean.getId(), e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }

    /* (non-Javadoc)
	 * @see com.compuware.caqs.dao.dbms.FactorEvolutionDao#retrieveFactorEvolution(com.compuware.caqs.domain.dataschemas.ElementBean)
	 */
    public Collection<FactorEvolutionBean> retrieveFactorEvolution(ElementBean eltBean) {
        Collection<FactorEvolutionBean> result = new ArrayList<FactorEvolutionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sqlRequest = "Select facb.id_bline, facb.id_fac, facb.note_facbl, bline.dinst_bline From Facteur_bline facb, Baseline bline"
                             + " Where facb.id_elt=?"
                             + " And facb.id_bline=bline.id_bline"
                             + " And bline.dinst_bline <= ?"
                             + " And bline.dmaj_bline IS NOT NULL"
                             + " And bline.dpremption_bline IS NULL"
                             + " order by facb.id_fac, bline.dinst_bline";
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            pstmt.setTimestamp(2, eltBean.getBaseline().getDinst());
            rs = pstmt.executeQuery();
            String actualIdFac = "";
            FactorEvolutionBean actualBean = null;
            while (rs.next()) {
                String idBline = rs.getString(1);
                String idFac = rs.getString(2);
                double noteFacBl = rs.getDouble(3);
                if (!idFac.equals(actualIdFac)) {
                    if (actualBean != null) {
                        result.add(actualBean);
                    }
                    actualBean = new FactorEvolutionBean(idFac);
                }
                actualBean.addEntry(idBline, new Double(noteFacBl));
                actualIdFac = idFac;
            }
            result.add(actualBean);
        }
        catch(SQLException e){
            logger.error("Erreur lors de la recuperation de l'evolution des facteurs:");
            logger.error("id_elt="+eltBean.getId(), e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }    

    /* (non-Javadoc)
	 * @see com.compuware.caqs.dao.dbms.FactorEvolutionDao#retrieveEvolutionMinAvg(com.compuware.caqs.domain.dataschemas.ElementBean)
	 */
    public List<FactorEvolutionBean> retrieveEvolutionMinAvg(ElementBean eltBean) {
        List<FactorEvolutionBean> result = new ArrayList<FactorEvolutionBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sqlRequest = "Select facb.id_bline, bline.dinst_bline, min(facb.note_facbl), avg(facb.note_facbl)"
                                + " From Facteur_bline facb, Baseline bline"
                                + " Where facb.id_elt=?"
                                + " And facb.id_bline=bline.id_bline"
                                + " And bline.dmaj_bline IS NOT NULL"
                                + " And bline.dpremption_bline IS NULL"
                                + " group by facb.id_bline, bline.dinst_bline"
                                + " order by bline.dinst_bline";
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            rs = pstmt.executeQuery();
            FactorEvolutionBean minFactor = new FactorEvolutionBean("MIN");;
            FactorEvolutionBean avgFactor = new FactorEvolutionBean("AVG");;
            while (rs.next()) {
                String idBline = rs.getString(1);
                double minScore = rs.getDouble(3);
                double avgScore = rs.getDouble(4);
                minFactor.addEntry(idBline, new Double(minScore));
                avgFactor.addEntry(idBline, new Double(avgScore));
            }
            result.add(minFactor);
            result.add(avgFactor);
        }
        catch(SQLException e){
            logger.error("Erreur lors de la recuperation de l'evolution des facteurs:");
            logger.error("id_elt="+eltBean.getId(), e);
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
    public Collection retrieveCriterionEvolution(ElementBean eltBean) {
    	Collection result = new ArrayList();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sqlRequest = "Select cb.id_bline, cb.id_crit, cb.note_cribl, cb.just_note_cribl, bline.dinst_bline"
                             + " From Critere_bline cb, Baseline bline"
                             + " Where cb.id_elt=?"
                             + " And cb.id_bline=bline.id_bline"
                             + " And bline.dinst_bline <= ?"
                             + " And bline.dmaj_bline IS NOT NULL"
                             + " And bline.dpremption_bline IS NULL"
                             + " order by cb.id_crit, bline.dinst_bline";
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            pstmt.setTimestamp(2, eltBean.getBaseline().getDinst());
            rs = pstmt.executeQuery();
            String actualIdCrit = "";
            CriterionEvolutionBean actualBean = null;
            while (rs.next()) {
                String idBline = rs.getString(1);
                String idCrit = rs.getString(2);
                double noteCriBl = rs.getDouble(4);
                if (noteCriBl == 0) {
                	noteCriBl = rs.getDouble(3);
                }
                if (!idCrit.equals(actualIdCrit)) {
                    if (actualBean != null) {
                        result.add(actualBean);
                    }
                    actualBean = new CriterionEvolutionBean(idCrit);
                }
                actualBean.addEntry(idBline, new Double(noteCriBl));
                actualIdCrit = idCrit;
            }
            result.add(actualBean);
        }
        catch(SQLException e){
            logger.error("Erreur lors de la recuperation de l'evolution des facteurs:");
            logger.error("id_elt="+eltBean.getId(), e);
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
    public List<BaselineBean> retrieveBaselines(ElementBean eltBean) {
    	List<BaselineBean> result = new ArrayList<BaselineBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            String sqlRequest = "Select distinct facb.id_bline, bline.dinst_bline, bline.lib_bline, bline.dmaj_bline From Facteur_bline facb, Baseline bline"
                             + " Where facb.id_elt=?"
                             + " And facb.id_bline=bline.id_bline"
                             + " And bline.dinst_bline <= ?"
                             + " And bline.dmaj_bline IS NOT NULL"
                             + " And bline.dpremption_bline IS NULL"
                             + " order by bline.dinst_bline";
            pstmt = connection.prepareStatement(sqlRequest);
            pstmt.setString(1, eltBean.getId());
            pstmt.setTimestamp(2, eltBean.getBaseline().getDinst());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                BaselineBean b = new BaselineBean();
                b.setId(rs.getString("id_bline"));
                b.setLib(rs.getString("lib_bline"));
                b.setDinst(rs.getTimestamp("dinst_bline"));
                b.setDmaj(rs.getTimestamp("dmaj_bline"));
                result.add(b);
            }
        }
        catch(SQLException e){
            logger.error("Erreur lors de la recuperation des baselines:");
            logger.error("id_elt="+eltBean.getId(), e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

        return result;
    }    
    
    
}

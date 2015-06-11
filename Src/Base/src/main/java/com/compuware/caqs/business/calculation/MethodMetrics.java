/*
 * MethodMetrics.java
 *
 * Created on 18 mai 2004, 14:54
 */
package com.compuware.caqs.business.calculation;

import com.compuware.caqs.domain.dataschemas.DialecteBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.compuware.caqs.business.calculation.exception.CalculationException;
import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CalculationDao;
import com.compuware.caqs.dao.interfaces.DialecteDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.domain.calculation.quality.RuleSet;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.MapUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author  cwfr-fdubois
 */
public class MethodMetrics {

    /** Requete de recuperation des sous-elements du programme. */
    private static final String SUB_ELT_REQUEST = "Select distinct(fils.id_elt), fils.id_stereotype From Element fils"
            + " Where fils.id_main_elt = ?" + " And fils.id_telt = ?"
            + " And (fils.dperemption is null Or fils.dperemption > ?)"
            + " And fils.dinst_elt < ?" + " order by fils.id_elt";
    private Logger logger = null;

    /** Creates a new instance of CopyPaste */
    public MethodMetrics(Logger logger) {
        this.logger = logger;
    }

    public void perform(Baseline baseline) throws CalculationException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            connection.setAutoCommit(false);
            DaoFactory daoFactory = DaoFactory.getInstance();
            ElementDao elementFacade = daoFactory.getElementDao();
            Collection<ElementBean> eaColl = elementFacade.retrieveElementByType(baseline.getProject().getId(), ElementType.EA, baseline.getDmaj());
            Iterator<ElementBean> i = eaColl.iterator();
            while (i.hasNext()) {
                ElementBean ea = (ElementBean) i.next();
                ea.setBaseline(baseline);
                perform(ea, baseline.getProject(), baseline, connection);
            }
            JdbcDAOUtils.commit(connection);
        } catch (DataAccessException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during metric calculation", e);
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during metric calculation", e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    public void perform(ElementBean ea, ProjectBean projet, Baseline baseline) throws CalculationException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            connection.setAutoCommit(false);
            perform(ea, projet, baseline, connection);
            JdbcDAOUtils.commit(connection);
        } catch (DataAccessException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during metric calculation", e);
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during metric calculation", e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    public void init(ElementBean ea) {
        retrieveModel(ea);
    }

    public void perform(ElementBean ea, ProjectBean projet, Baseline baseline, Connection connection) throws SQLException, DataAccessException {
        logger.info("Calcul metriques EA: " + ea.getId());
        UsageCalculator calculator = this.getCalculator(ea.getUsage());
        List<Leaf> allMethods = retrieveAllMethods(ea.getId(), projet, baseline, ea.getUsage(), connection);
        this.calculeCopierColler(ea.getId(), baseline, allMethods, calculator, connection);
        this.calculeComplexDest(ea, baseline, allMethods, ea.getUsage(), connection);
        this.calculeNbElements(ea, baseline);
        JdbcDAOUtils.commit(connection);
    }

    private UsageBean retrieveModel(ElementBean ea) {
        UsageBean result = ea.getUsage();
        if (result == null) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            UsageDao usageDao = daoFactory.getUsageDao();
            result = usageDao.retrieveUsageByElementId(ea.getId());
        }
        ea.setUsage(result);
        return result;
    }

    private UsageCalculator getCalculator(UsageBean usage) {
        UsageCalculator result = new UsageCalculator();
        result.init("method-" + usage.getId().toLowerCase() + ".xml");
        return result;
    }

    /**
     * Methode d'initialisation des methodes du programme.
     */
    public List<Leaf> retrieveAllMethods(String idElt, ProjectBean projet, Baseline baseline, UsageBean usage, Connection connection) throws DataAccessException {
        return retrieveAllElements(idElt, projet, baseline, usage, connection, ElementType.MET);
    }

    /**
     * Methode d'initialisation des methodes du programme.
     */
    public List<Leaf> retrieveAllElements(String idElt, ProjectBean projet, Baseline baseline, UsageBean usage, Connection connection, String idTelt) throws DataAccessException {
        List<Leaf> result = new ArrayList<Leaf>();
        PreparedStatement metstmt = null;
        ResultSet rs = null;
        try {

            DaoFactory daoFactory = DaoFactory.getInstance();
            CalculationDao calculationDao = daoFactory.getCalculationDao();
            Map<String, Map<String, ValuedObject>> metricMap = calculationDao.retrieveMetrics(idElt, baseline.getId());

            // Preparation de la requete.
            metstmt = connection.prepareStatement(SUB_ELT_REQUEST);
            // Initialisation des parametres de la requete.
            metstmt.setString(1, idElt);
            metstmt.setString(2, idTelt);
            metstmt.setTimestamp(3, baseline.getDmaj());
            metstmt.setTimestamp(4, baseline.getDmaj());
            // Execution de la requete.
            rs = metstmt.executeQuery();
            Leaf elt = null;
            // Parcours du resultat de la requete.
            while (rs.next()) {
                String metIdElt = rs.getString(1);
                String metStereotype = rs.getString(2);
                // Creation d'une nouvelle methode a partir des donnees resultat.
                // Ajout au resultat de l'appel.
                if (idTelt.equals(ElementType.MET)) {
                    elt = new Methode(metIdElt, idTelt, 1, metStereotype, projet, baseline, usage);
                } else {
                    elt = new Leaf(metIdElt, idTelt, 1, metStereotype, projet, baseline, usage);
                }
                elt.setMetrics(metricMap.get(metIdElt));
                result.add(elt);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving methods", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(metstmt);
        }
        return result;
    }

    public void calculeComplexDest(ElementBean elt, Baseline baseline, List<Leaf> methods, UsageBean usage, Connection connection) throws DataAccessException, SQLException {
        if (elt.getDialecte() == null) {
            DialecteDao dialecteDao = DaoFactory.getInstance().getDialecteDao();
            DialecteBean db = dialecteDao.retrieveDialecteByElementId(elt.getId());
            elt.setDialecte(db);
        }

        UsageCalculator calculator = getUsageCalculator(usage);
        double globalAllCode = 0.0;
        double complexDest = 0.0;
        // Parcours des methodes.
        String complexityMetric = null;
        Properties caqsProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        if (elt.getDialecte() != null) {
            complexityMetric = caqsProp.getProperty("compl."
                    + elt.getDialecte().getLangage().getId());
        }

        if (complexityMetric == null) {
            complexityMetric = "PLOG";
        }

        Iterator<Leaf> i = methods.iterator();
        while (i.hasNext()) {
            // Extraction de la methode.
            Methode method = (Methode) i.next();
            Qametrique allCode = (Qametrique) MapUtils.get(method.getMetriques(),
                    Constants.ALL_CODE_KEYS);
            if (allCode != null) {
                globalAllCode += allCode.getValbrute();
                double plog = calculator.eval(usage.getId(), complexityMetric,
                        method.getMetriques(), RuleSet.STEREOTYPE_ALL);
                if (plog == 0) {
                    plog = calculator.eval(usage.getId(), "COMPLEXITY", method.getMetriques(), RuleSet.STEREOTYPE_ALL);
                }
                if (plog == 1) {
                    complexDest += allCode.getValbrute();
                }
            }
        }
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
        if (globalAllCode > 0.0) {
            metriqueDao.setMetrique(elt.getId(), baseline.getId(), "ALL_CODE_MET", globalAllCode, connection, true);
        }
        if (complexDest >= 0.0) {
            metriqueDao.setMetrique(elt.getId(), baseline.getId(), "COMPLEX_DEST", complexDest, connection, true);
        }
        JdbcDAOUtils.commit(connection);
    }

    public void calculeIFPUG(ElementBean elt, Baseline baseline, ProjectBean projet, UsageBean usage) throws CalculationException {
        logger.debug("Calcul de l'IFPUG");
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            metriqueDao.deleteMetricForBaselineAndElement(elt.getId(), "IFPUG", baseline.getId());
            UsageCalculator calculator = getUsageCalculator(usage);
            String ifpugTelt = calculator.getUsageIFPUGElementType(usage.getId());
            List<Leaf> allIFPUGElements = retrieveAllElements(elt.getId(), projet, baseline, usage, connection, ifpugTelt);
            double ifpugSum = 0.0;
            if (allIFPUGElements != null && !allIFPUGElements.isEmpty()) {
                Iterator<Leaf> i = allIFPUGElements.iterator();
                while (i.hasNext()) {
                    // Extraction de la methode.
                    Leaf element = i.next();
                    double ifpug = calculator.evalIFPUG(usage.getId(), element.getMetriques());
                    metriqueDao.setMetrique(element.getId(), baseline.getId(), "IFPUG", ifpug, connection, true);
                    ifpugSum += ifpug;
                }
            }
            metriqueDao.setMetrique(elt.getId(), baseline.getId(), "IFPUG", ifpugSum, connection, true);
            JdbcDAOUtils.commit(connection);
        } catch (DataAccessException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during metric calculation", e);
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during metric calculation", e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    private String getIdCopierColler(String idElt, Baseline baseline, Connection connection) throws DataAccessException {
        String request = "Select id_link From Link_real" + " Where id_proj=?"
                + " And id_bline=?" + " And state=20";

        String result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(request);
            pstmt.setString(1, idElt);
            pstmt.setString(2, baseline.getId());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting copy/paste links", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return result;
    }

    public void calculeCopierColler(String idElt, Baseline baseline, List<Leaf> methods, UsageCalculator calculator, Connection connection) throws DataAccessException, SQLException {
        // Recuperation de l'identifiant pour le copier-coller.
        String idLinkProg = getIdCopierColler(idElt, baseline, connection);
        if (idLinkProg == null) {
            idLinkProg = IDCreator.getID();
        }
        updateCopierColler(idElt, baseline, idLinkProg, connection);

        logger.debug("Calcul du COPIER-COLLER");
        int size = methods.size();
        // Parcours des methodes.
        for (int i = 0; i < methods.size(); i++) {
            // Extraction de la methode.
            Methode method = (Methode) methods.get(i);
            // Calcul du Copier-Coller
            logger.debug("(" + (i + 1) + "/" + size + "), MET:" + method.getId());
            method.calculeCopierColler(connection, idLinkProg, methods, calculator, i
                    + 1);
        }
        JdbcDAOUtils.commit(connection);
    }

    private void updateCopierColler(String idElt, Baseline baseline, String idLinkProg, Connection connection) throws DataAccessException {
        String selectRequest = "Select id_link From LINK_REAL Where id_link=?";
        String insertRequest = "Insert into LINK_REAL (id_link, id_proj, id_bline, state) VALUES (?, ?, ?, 20)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(selectRequest);
            pstmt.setString(1, idLinkProg);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
            } else {
                pstmt.close();
                pstmt = connection.prepareStatement(insertRequest);
                pstmt.setString(1, idLinkProg);
                pstmt.setString(2, idElt);
                pstmt.setString(3, baseline.getId());
                pstmt.executeUpdate();
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            throw new DataAccessException("Error updating copy-paste", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }

    private void calculeNbElements(ElementBean eltBean, Baseline baseline) throws DataAccessException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();

        Map<String, Integer> nbTotal = elementFacade.retrieveTotalElements(baseline.getDinst(), baseline.getDmaj(), eltBean.getId());
        Map<String, Integer> nbCreated = elementFacade.retrieveCreatedElements(baseline.getDinst(), baseline.getDmaj(), eltBean.getId());
        Map<String, Integer> nbDeleted = elementFacade.retrieveDeletedElements(baseline.getDinst(), baseline.getDmaj(), eltBean.getId());
        Collection<Volumetry> volumetryColl = new ArrayList<Volumetry>();
        Volumetry volumetryBean = null;
        if (nbTotal != null) {
            Set<String> keySet = nbTotal.keySet();
            if (keySet != null) {
                String currentKey = null;
                Iterator<String> i = keySet.iterator();
                while (i.hasNext()) {
                    currentKey = i.next();
                    volumetryBean = new Volumetry();
                    volumetryBean.setIdElt(eltBean.getId());
                    volumetryBean.setIdTElt(currentKey);
                    volumetryBean.setTotal(nbTotal.get(currentKey));
                    volumetryBean.setCreated(nbCreated.get(currentKey));
                    volumetryBean.setDeleted(nbDeleted.get(currentKey));
                    volumetryColl.add(volumetryBean);
                    //on retire des elements supprimes pour n'avoir ensuite que ceux entierement supprimes
                    nbDeleted.remove(currentKey);
                }
            }
        }
        if (nbDeleted != null) {
            //precedemment, nous avons mis a jour les volumetries des types d'elements
            //presents avant ou non et presents maintenant
            //dans le cas par exemple du php 5, on peut avoir une analyse avec des CLS
            //maj du referentiel ==> suppression de toutes les CLS (php 5 ==> php 4)
            //on doit donc aussi mettre a jours les volumetries des types d'elements qui n'existent plus
            //tous les elements dans la map sont ceux entierement supprimes
            for (Map.Entry<String, Integer> deleted : nbDeleted.entrySet()) {
                volumetryBean = new Volumetry();
                volumetryBean.setIdElt(eltBean.getId());
                volumetryBean.setIdTElt(deleted.getKey());
                volumetryBean.setTotal(0);
                volumetryBean.setCreated(0);
                volumetryBean.setDeleted(deleted.getValue());
                volumetryColl.add(volumetryBean);
            }
        }
        if (!volumetryColl.isEmpty()) {
            elementFacade.setElementVolumetry(volumetryColl, baseline.getId());
        }
    }

    private UsageCalculator getUsageCalculator(UsageBean usage) {
        UsageCalculator result = new UsageCalculator();
        result.init("method-" + usage.getId().toLowerCase() + ".xml");
        return result;
    }
}

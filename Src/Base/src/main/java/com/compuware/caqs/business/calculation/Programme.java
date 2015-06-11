/*
 * Programme.java
 *
 * Created on 3 octobre 2002, 11:24
 */
package com.compuware.caqs.business.calculation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CalculationDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.calcul.Facteur;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * Define a project module.
 * @author  cwfr-fdubois
 */
public class Programme extends Leaf {

    /** Cree une nouvelle instance de Programme. */
    public Programme(String idElt, double poids, String stereotype,
            ProjectBean projet, Baseline baseline) {
        // Initialisation des attributs.
        super(idElt, ElementType.EA, poids, stereotype, projet, baseline);
        this.mIdMainElt = idElt;
    }

    public void initUsage() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsageDao usageDao = daoFactory.getUsageDao();
        this.mUsage = usageDao.retrieveUsageByElementId(mIdElt);
        Map<String, Map<String, Double>> facdefs = usageDao.retrieveFactorDefinition(mUsage.getId());
        this.mUsage.setFactorDefinition(facdefs);
        // Initialisation du crit�re.
        setCriterionIds(usageDao.retrieveCriterionsForElement(this.mUsage.getId(), ElementType.EA));
    }

    public void init(ICalculationConfig config) throws DataAccessException {
        LoggerManager.pushContexte("EA(" + mIdElt + ")");
        logger.debug("Initialisation");
        initUsage();
        this.mIsMainContainer = true;

        DaoFactory daoFactory = DaoFactory.getInstance();
        CalculationDao calculationDao = daoFactory.getCalculationDao();
        Map<String, Map<String, ValuedObject>> metricMap = null;
        Map<String, Map<String, Critere>> criterionMap = null;
        if (!config.needCriterionCalculation()) {
            // Does not need calculation => retrieve existing criterion
            criterionMap = calculationDao.retrieveCriterion(mIdMainElt, mBaseline.getId());
        }
        // Need calculation => retrieve metrics
        metricMap = calculationDao.retrieveMetrics(mIdMainElt, mBaseline.getId());
        this.mMetriques = metricMap.get(mIdMainElt);

        Map<String, Map<String, Critere>> critJustifMap = calculationDao.getCritereJustificatifs(mIdMainElt, mBaseline);

        Collection<String> factorColl = calculationDao.retrieveFactors(mUsage.getId());
        ElementDao elementDao = daoFactory.getElementDao();
        List<ElementLinked> links = elementDao.retrieveSubElementLinks(mIdMainElt, mBaseline.getDmaj());
        UsageDao usageDao = daoFactory.getUsageDao();
        Map<String, String> criterionElementTypeMap = usageDao.retrieveCriterionElementTypeMap(mUsage.getId());
        initTree(links, metricMap, criterionMap, factorColl, criterionElementTypeMap);
        super.init(config, critJustifMap);
        LoggerManager.popContexte();
    }

    private void initTree(List<ElementLinked> links,
            Map<String, Map<String, ValuedObject>> metricMap,
            Map<String, Map<String, Critere>> criterionMap,
            Collection<String> factorColl,
            Map<String, String> criterionElementTypeMap) throws DataAccessException {
        Map<String, Collection<String>> criterionSubEltsMap = new HashMap<String, Collection<String>>();
        Map<String, Leaf> tree = new HashMap<String, Leaf>();
        tree.put(this.mIdElt, this);
        if (criterionMap != null && criterionMap.containsKey(this.getId())) {
            mCriterions = criterionMap.get(this.getId());
        }
        if (links != null) {
            Iterator<ElementLinked> linkIter = links.iterator();
            ElementLinked currentLink = null;
            Leaf currentLeaf = null;
            Leaf parentLeaf = null;
            while (linkIter.hasNext()) {
                currentLink = linkIter.next();

                if (criterionSubEltsMap.get(currentLink.getTypeElt()) == null) {
                    criterionSubEltsMap.put(currentLink.getTypeElt(), getCriterionsForElement(currentLink.getTypeElt()));
                }

                parentLeaf = tree.get(currentLink.getFather().getId());
                if (parentLeaf != null) {
                    currentLeaf = tree.get(currentLink.getId());
                    if (currentLeaf == null) {
                        currentLeaf = new Leaf(currentLink.getId(), currentLink.getTypeElt(), currentLink.getPoids(), null, mProjet, mBaseline, mUsage);
                        currentLeaf.setLogger(logger);
                        currentLeaf.setCriterionIds(criterionSubEltsMap.get(currentLink.getTypeElt()));
                        currentLeaf.setMainElt(getMainElt());
                        currentLeaf.setDescElt(currentLink.getDesc());
                        currentLeaf.setCriterionElementTypeMap(criterionElementTypeMap);
                        if (metricMap != null &&
                                metricMap.containsKey(currentLink.getId())) {
                            currentLeaf.setMetrics(metricMap.get(currentLink.getId()));
                        }
                        if (criterionMap != null &&
                                criterionMap.containsKey(currentLink.getId())) {
                            currentLeaf.mCriterions = criterionMap.get(currentLink.getId());
                        }
                        initFactorMap(currentLeaf, factorColl);
                        parentLeaf.mSubElt.add(currentLeaf);
                        tree.put(currentLink.getId(), currentLeaf);
                    }
                } else {
                    logger.error("parent not found for element:" +
                            currentLeaf.toString());
                    throw new DataAccessException("parent not found for element:" +
                            currentLeaf.toString());
                }
            }
        }
        initFactorMap(this, factorColl);
    }

    private void initFactorMap(Leaf currentLeaf, Collection<String> factorColl) {
        Map<String, Facteur> factorMap = currentLeaf.getFacteurs();
        Iterator<String> factorIter = factorColl.iterator();
        while (factorIter.hasNext()) {
            String idFact = factorIter.next();
            factorMap.put(idFact, new Facteur(idFact, currentLeaf.getPoids()));
        }
    }

    public void calculate(ICalculationConfig config) throws DataAccessException {
        super.calculate(getUsageCalculator(), config);
    }

    private UsageCalculator getUsageCalculator() {
        UsageCalculator result = new UsageCalculator();
        result.init("method-" + this.mUsage.getId().toLowerCase() + ".xml");
        return result;
    }

    @Override
    protected boolean isTreeStructureElement() {
        return true;
    }

    public void performUpdate(ICalculationConfig config) throws CaqsException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            super.performUpdate(connection, config);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DataAccessException("Error during calculation database update", e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }
}

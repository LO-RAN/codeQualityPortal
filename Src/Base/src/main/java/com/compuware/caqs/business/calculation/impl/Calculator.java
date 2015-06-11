/**
 * 
 */
package com.compuware.caqs.business.calculation.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.calculation.Baseline;
import com.compuware.caqs.business.calculation.Element;
import com.compuware.caqs.business.calculation.ICalculator;
import com.compuware.caqs.business.calculation.MethodMetrics;
import com.compuware.caqs.business.calculation.Programme;
import com.compuware.caqs.business.calculation.exception.CalculationException;
import com.compuware.caqs.business.calculation.exception.CalculationPostConditionException;
import com.compuware.caqs.business.calculation.exception.CalculationPreConditionException;
import com.compuware.caqs.business.calculation.exception.NoMetricFoundException;
import com.compuware.caqs.business.util.AnalysisFileLogger;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.CalculationDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.domain.dataschemas.calcul.ModuleBaselineAssoc;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import org.apache.log4j.Logger;

/**
 * Implement a calculator.
 * @author cwfr-fdubois
 *
 */
public class Calculator implements ICalculator {

    protected static Logger logger;
    /** The dao factory for database access. */
    private DaoFactory daoFactory = DaoFactory.getInstance();
    /** The current project. */
    private ProjectBean project = null;
    /** The corresponding project element ID. */
    private String projectElementId = null;
    /** The current baseline. */
    private Baseline baseline = null;
    /** The ea list to calculate. */
    private List<ModuleBaselineAssoc> eaList = null;
    /** The ea/baseline association map. */
    private Map<String, String> eaBaselineAssocMap = null;
    /** The calculation configuration. */
    private ICalculationConfig config = null;

    public void calculate() throws CalculationException, CalculationPostConditionException {
        try {
            if (config.needCalculation() && eaList != null && !eaList.isEmpty()) {
                Iterator<ModuleBaselineAssoc> moduleIter = this.eaList.iterator();
                while (moduleIter.hasNext()) {
                    ModuleBaselineAssoc module = moduleIter.next();
                    calculateModule(module);
                    this.removeResultsNotInQualityModelAnymore(module);
                }
            }
            consolidate();
        } finally {
            AnalysisFileLogger.shutdownHierarchy(logger);
        }
    }

    private void removeResultsNotInQualityModelAnymore(ModuleBaselineAssoc module) {
        if(module != null) {
            this.daoFactory.getCalculationDao().removeResultsNotInQualityModelAnymore(module.getBaseline().getId(),
                    module.getElement().getUsage().getId(), module.getElement().getId());
        }
    }

    private void calculateModule(ModuleBaselineAssoc module) throws CalculationException {
        try {
            Baseline elementBaseline = createBaseline(module.getBaseline().getId(), module.getElement().getId());
            MethodMetrics mm = new MethodMetrics(logger);
            mm.init(module.getElement());
            if (this.config.needMetricCalculation()) {
                // Calcul des metriques.
                mm.perform(module.getElement(), this.project, elementBaseline);
            }
            mm.calculeIFPUG(module.getElement(), baseline, this.project, module.getElement().getUsage());
            if (module.getElement() != null) {
                Programme pgm = new Programme(module.getElement().getId(), 1, null, this.project, elementBaseline);
                pgm.setLogger(logger);
                pgm.init(this.config);
                pgm.calculate(this.config);
                pgm.performUpdate(this.config);
            }
        } catch (CaqsException e) {
            throw new CalculationException(e);
        }
    }

    private void consolidate() throws CalculationException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        try {
            // Aggregation des facteurs.
            logger.info("Recherche des elements pour le projet " + this.project +
                    " et la baseline " + baseline + ".");
            Baseline eltBline = new Baseline(this.baseline);
            eltBline.setPreviousBaseline(this.baseline.getPreviousBaseline());
            Element elt = new Element(this.projectElementId, 1, this.project, eltBline);
            elt.setLogger(logger);
            elt.init(this.eaBaselineAssocMap, connection);
            elt.calculate(connection);
            elt.performUpdate(connection, this.config);
            JdbcDAOUtils.commit(connection);
            elt.print();
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error commiting changes", e);
        } catch (DataAccessException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException("Error during database communication", e);
        } catch (CaqsException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new CalculationException(e);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    public void init(AnalysisConfig pAnalysisParameters, ICalculationConfig pConfig)
            throws CalculationPreConditionException {
        logger = AnalysisFileLogger.createLogger(pAnalysisParameters.getBaselineId());
        logger.info(pAnalysisParameters.toString());
        logger.info(pConfig.toString());
        try {
            checkInitParameter(pAnalysisParameters, pConfig);
            initAttributes(pAnalysisParameters, pConfig);
            checkCalculationPrerequisites();
        } catch (CalculationPreConditionException e) {
            logger.error("Calculation init error", e);
            AnalysisFileLogger.shutdownHierarchy(logger);
            throw e;
        }
    }

    private void checkInitParameter(AnalysisConfig pAnalysisParameters, ICalculationConfig pConfig)
            throws CalculationPreConditionException {
        if (pAnalysisParameters.getProjectId() == null) {
            throw new CalculationPreConditionException("Missing project for calculation");
        }
        if (pAnalysisParameters.getBaselineId() == null) {
            throw new CalculationPreConditionException("Missing baseline for calculation");
        }
        if (pConfig == null) {
            throw new CalculationPreConditionException("Missing calculation configuration");
        }
    }

    private void initAttributes(AnalysisConfig pAnalysisParameters, ICalculationConfig pConfig)
            throws CalculationPreConditionException {
        this.config = pConfig;
        try {
            ProjectDao projectDao = daoFactory.getProjectDao();
            this.project = projectDao.retrieveProjectById(pAnalysisParameters.getProjectId());
            this.projectElementId = projectDao.retrieveProjectElementId(this.project.getId());
            this.baseline = this.createBaseline(pAnalysisParameters.getBaselineId(), this.projectElementId);
            String[] validEaArray = pAnalysisParameters.getModuleArray();
            if (validEaArray == null) {
                // Every modules selected
                validEaArray = createModuleArrayFromProject(this.project.getId());
            }
            CalculationDao calculationDao = daoFactory.getCalculationDao();
            calculationDao.createModuleBaselineAssoc(pAnalysisParameters.getProjectId(), this.baseline, validEaArray);
            eaBaselineAssocMap = calculationDao.retrieveModuleBaselineMap(pAnalysisParameters.getBaselineId());
            this.eaList = retrieveModuleBaselineAssociations(pAnalysisParameters.getBaselineId(), validEaArray);
        } catch (DataAccessException e) {
            throw new CalculationPreConditionException(e);
        }
    }

    private Baseline createBaseline(String baselineId, String idElt) throws DataAccessException {
        BaselineDao baselineFacade = daoFactory.getBaselineDao();
        BaselineBean currentBaseline = baselineFacade.retrieveBaselineAndProjectById(baselineId);
        Baseline result = new Baseline(currentBaseline);
        BaselineBean previousBaseline = baselineFacade.getPreviousBaseline(result, idElt);
        if (previousBaseline != null) {
            result.setPreviousBaseline(new Baseline(previousBaseline));
        }
        if (result.getDmaj() == null) {
            java.util.Date now = new java.util.Date();
            result.setDmaj(new Timestamp(now.getTime()));
        }
        return result;
    }

    private String[] createModuleArrayFromProject(String projectId) throws DataAccessException {
        ElementDao elementFacade = daoFactory.getElementDao();
        Collection<ElementBean> eaColl = elementFacade.retrieveElementByType(projectId, ElementType.EA, baseline.getDmaj());
        CalculationDao calculationDao = daoFactory.getCalculationDao();
        eaBaselineAssocMap = calculationDao.retrieveModuleBaselineMap(baseline.getId());
        String[] result = new String[eaColl.size() - eaBaselineAssocMap.size()];
        int idx = 0;
        Iterator<ElementBean> eaIter = eaColl.iterator();
        while (eaIter.hasNext()) {
            ElementBean ea = eaIter.next();
            if (!eaBaselineAssocMap.containsKey(ea.getId())) {
                result[idx] = ea.getId();
                idx++;
            }
        }
        return result;
    }

    private void checkCalculationPrerequisites() throws CalculationPreConditionException {
        CalculationDao calculationDao = daoFactory.getCalculationDao();
        try {
            boolean metricExistsForBaseline = calculationDao.metricExistsForBaseline(this.baseline);
            if (!metricExistsForBaseline) {
                throw new NoMetricFoundException("No metric found for baseline " +
                        this.baseline.getId());
            }
        } catch (DataAccessException e) {
            throw new CalculationPreConditionException("Error during metric check", e);
        }
    }

    /**
     * Retrieve all modules associated with the given baseline or with a related one that
     * are inside the given module ID array.
     * @param baselineId the baseline ID.
     * @return the module/baseline association list found.
     * @throws DataAccessException
     */
    public List<ModuleBaselineAssoc> retrieveModuleBaselineAssociations(
            String baselineId, String[] eaArray) throws DataAccessException {
        List<ModuleBaselineAssoc> result = new ArrayList<ModuleBaselineAssoc>();
        ModuleBaselineAssoc assoc = null;
        ElementBean element = null;
        BaselineBean projectBaseline = new BaselineBean();
        projectBaseline.setId(baselineId);
        BaselineBean currentBaseline = null;
        for (int i = 0; i < eaArray.length; i++) {
            element = new ElementBean();
            element.setId(eaArray[i]);
            String baselineAssoc = eaBaselineAssocMap.get(eaArray[i]);
            if (baselineAssoc != null) {
                currentBaseline = new BaselineBean();
                currentBaseline.setId(baselineId);
            } else {
                currentBaseline = projectBaseline;
            }
            assoc = new ModuleBaselineAssoc(element, currentBaseline);
            result.add(assoc);
        }
        return result;
    }
}

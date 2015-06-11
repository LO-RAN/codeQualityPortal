package com.compuware.caqs.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.business.report.Reporter;
import com.compuware.caqs.business.util.AnalysisFileLogger;
import com.compuware.caqs.business.util.AntExecutor;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.dbms.DataAccessCache;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.domain.dataschemas.calcul.impl.CalculationConfig;
import com.compuware.caqs.domain.dataschemas.evolutions.EvolutionBaselineBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.service.process.CalculationSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import org.apache.log4j.Logger;

public class BaselineSvc {

    private static final BaselineSvc instance = new BaselineSvc();
    private BaselineDao baselineDao = DaoFactory.getInstance().getBaselineDao();

    private BaselineSvc() {
    }

    public static BaselineSvc getInstance() {
        return instance;
    }
    protected static Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();

    public BaselineBean retrieveBaselineById(java.lang.String id) {
        return this.baselineDao.retrieveBaselineById(id);
    }

    public java.sql.Timestamp getBaselineDmaj(String idBline) throws CaqsException {
        return this.baselineDao.getBaselineDmaj(idBline);
    }

    /**
     * Renvoie toutes les baselines, sauf la baseline d'instantiation (les baselines
     * dont l'analyse a échouée sont inclues) et celles en cours de calcul
     * @param idPro
     * @return
     */
    public Collection<BaselineBean> retrieveAllTerminatedBaselinesByProjectId(String projectId) {
        return this.baselineDao.retrieveAllTerminatedBaselinesByProjectId(projectId);
    }

    /**
     * Renvoie toutes les baselines, sauf la baseline d'instantiation (les baselines
     * dont l'analyse a échouée sont inclues)
     * @param idPro
     * @return
     */
    public Collection<BaselineBean> retrieveAllBaselineByProjectId(String projectId) {
        return this.baselineDao.retrieveAllBaselinesByProjectId(projectId);
    }

    public BaselineBean getPreviousBaseline(BaselineBean currentBline) {
        BaselineBean result = null;
        try {
            result = this.baselineDao.getPreviousBaseline(currentBline);
        } catch (DataAccessException e) {
            result = null;
            logger.error("getPreviousBaseline", e);
        }
        return result;
    }

    public BaselineBean getPreviousBaseline(BaselineBean currentBline, String idElt) {
        BaselineBean result = null;
        try {
            result = this.baselineDao.getPreviousBaseline(currentBline, idElt);
        } catch (DataAccessException e) {
            result = null;
            logger.error("getPreviousBaseline", e);
        }
        return result;
    }

    public void purgeBaseline(String idBline, String idPro, boolean split) throws CaqsException {
        BaselineBean bline = this.baselineDao.retrieveBaselineById(idBline);
        if (bline.getLib() != null) {
            //on met a jour les tendances
            //on commence par perimer la bline
            this.baselineDao.peremptBaseline(idBline);

            //et on met a jour la tendance
            ProjectBean pb = new ProjectBean();
            pb.setId(idPro);
            BaselineBean bb = new BaselineBean();
            bb.setId(idBline);
            BaselineBean nextBline = this.baselineDao.getNextBaseline(bb, pb);
            if (nextBline != null) {
                //s'il y a une baseline apres celle-ci, il faut la recalculer
                //pour mettre a jour les tendances
                ICalculationConfig calculationConfig = new CalculationConfig();
                calculationConfig.setNeedConsolidation(false);
                calculationConfig.setNeedCriterionCalculation(false);
                calculationConfig.setNeedGoalCalculation(false);
                calculationConfig.setNeedMetricCalculation(false);
                calculationConfig.setNeedTrendUpdate(true);
                AnalysisConfig analysisParameters = new AnalysisConfig();
                analysisParameters.setProjectId(idPro);
                analysisParameters.setBaselineId(nextBline.getId());
                String[] selectedEa = null;
                List<ElementBean> eas = ElementSvc.getInstance().retrieveAllApplicationEntitiesForProjectAndBaseline(pb, nextBline);
                if (!eas.isEmpty()) {
                    selectedEa = new String[eas.size()];
                    int i = 0;
                    for (ElementBean ea : eas) {
                        selectedEa[i] = ea.getId();
                        i++;
                    }
                }
                analysisParameters.setModuleArray(selectedEa);
                CalculationSvc.getInstance().calculate(analysisParameters, calculationConfig);
                dataCache.clearCache(nextBline.getId());
            }
            //fin mise a jour des tendances
        }
        this.baselineDao.delete(idBline, idPro, split);

        ElementDao elementDao = DaoFactory.getInstance().getElementDao();
        Collection<ElementBean> eaColl = elementDao.retrieveElementByType(idPro, ElementType.EA);
        try {
            if (eaColl != null) {
                ElementBean currentEa = null;
                Iterator<ElementBean> eaIter = eaColl.iterator();
                while (eaIter.hasNext()) {
                    currentEa = eaIter.next();
                    cleanEaDirectories(currentEa, idBline);
                }
            }
        } finally {
            ImageUtil.getInstance().clearKiviatImage(idBline);
        }
        dataCache.clearCache(idBline);
    }
    private static final String CLEAN_EA_DIRS = "cleanEaDirectories";

    private void cleanEaDirectories(ElementBean eltBean, String idBline) throws CaqsException {
        Logger analysisLogger = AnalysisFileLogger.createLogger(idBline);
        AntExecutor antExecutor = new AntExecutor(analysisLogger);
        try {
            antExecutor.processAntScript(eltBean, CLEAN_EA_DIRS, getCleanProperties(eltBean, idBline));
        } catch (IOException e) {
            analysisLogger.error("Error cleaning ea directories for: "
                    + eltBean.getLib(), e);
            throw new CaqsException("Error cleaning ea directories for: "
                    + eltBean.getLib(), e);
        }
    }
    private static final String REPORT_PATH_KEY = "REPORT_PATH";
    private static final String DATA_PATH_KEY = "DATA_PATH";

    private Properties getCleanProperties(ElementBean eltBean, String idBline) {
        Properties result = new Properties();
        BaselineBean blineBean = new BaselineBean();
        blineBean.setId(idBline);
        eltBean.setBaseline(blineBean);
        String reportPath = Reporter.getReportPath(eltBean, eltBean.getBaseline().getId());
        result.put(REPORT_PATH_KEY, reportPath);
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String dataPath = dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY);
        dataPath = new File(dataPath).getAbsolutePath();
        String pathWithBaseline = ElementBean.getHtmlSrcDir(idBline, eltBean, eltBean.getProject(), dataPath);
        result.put(DATA_PATH_KEY, pathWithBaseline);
        return result;
    }

    public AnalysisResult update(String baselineName, String baselineId, boolean changeDmaj) {
        AnalysisResult result = null;
        Logger analysisLogger = AnalysisFileLogger.createLogger(baselineId);
        analysisLogger.info("Update baseline: ID=" + baselineId + ", NAME="
                + baselineName + ", Update date=" + changeDmaj);
        try {
            this.baselineDao.update(baselineName, baselineId, changeDmaj);
            result = getSuccessResultString(baselineId);
        } catch (CaqsException e) {
            result = getErrorResultString(e);
            analysisLogger.error("Error during baseline update", e);
        }
        return result;
    }

    public AnalysisResult create(String projectId, String forcedId) {
        AnalysisResult result = null;
        Logger analysisLogger = null;
        if (forcedId != null && forcedId.length() > 0) {
            analysisLogger = AnalysisFileLogger.createLogger(forcedId);
            analysisLogger.info("Create baseline with ID=" + forcedId
                    + " for project with ID=" + projectId);
        }
        try {
            BaselineBean bb = this.baselineDao.create(projectId, forcedId);
            if (bb != null) {
                result = getSuccessResultString(bb.getId());
            } else {
                result = getErrorResultString("Impossible to create a the baseline for project Id:"
                        + projectId + " and given Id:" + forcedId);
            }
        } catch (CaqsException e) {
            result = getErrorResultString(e);
        }
        return result;
    }

    private AnalysisResult getSuccessResultString(String baselineId) {
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        result.addParam("baselineId", baselineId);
        return result;
    }

    private AnalysisResult getErrorResultString(Exception e) {
        return getErrorResultString(e.toString());
    }

    private AnalysisResult getErrorResultString(String appendString) {
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(false);
        result.setMessage(appendString);
        return result;
    }

    public AnalysisResult delete() {
        return getErrorResultString("not implemented");
    }

    public void purge(String projectId, String baselineId) throws CaqsException {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();

        boolean split = false;
        String splitString = dynProp.getProperty("baseline.purge.split");
        if (splitString != null) {
            split = Boolean.getBoolean(splitString);
        }

        purgeBaseline(baselineId, projectId, split);
        EvolutionSvc.getInstance().clearBaselineEvolutionDatas(baselineId);
    }

    public List<BaselineBean> isBaselineAttachedToOtherBaseline(String idBline) {
        return this.baselineDao.isBaselineAttachedToOtherBaseline(idBline);
    }

    /**
     * @param baselineId identifiant de la baseline
     * @param elt element
     * @return true si la baseline est bien la derniere pour l'element, false sinon
     */
    public boolean isLastBaseline(String baselineId, ElementBean elt) {
        return this.baselineDao.isLastBaseline(baselineId, elt.getId());
    }

    /**
     *
     * @param baselineId
     * @param idElt
     * @return true si l'identifiant de baseline envoye correspond a la premiere
     * baseline de l'element, false sinon
     */
    public boolean isFirstBaseline(String baselineId, String idElt) {
        return this.baselineDao.isFirstBaseline(baselineId, idElt);
    }

    /**
     * returns the real baseline. returns real baseline (idBline or linked baseline if there is one)
     * @param elt the element for which the real baseline has to be returned
     * @param tentative baseline id
     * @return returns real baseline (idBline or linked baseline if there is one)
     */
    public BaselineBean getRealBaseline(ElementBean elt, String idBline) {
        BaselineBean bb = null;
        try {
            String realIdBline = this.baselineDao.retrieveLinkedBaseline(elt.getId(), idBline);
            bb = new BaselineBean();
            if (realIdBline == null) {
                bb.setId(idBline);
            } else {
                bb.setId(realIdBline);
            }
        } catch (CaqsException ce) {
            logger.error("Error while retrieving last baseline for element (id:"
                    + elt.getId() + ")", ce);
        }
        return bb;
    }

    /**
     * returns last baseline. returns last baseline or last linked baseline if there is one
     * @param elt the element for which the last baseline has to be returned
     * @return the last baseline for the element
     */
    public BaselineBean getLastBaseline(ElementBean elt) {
        BaselineBean bb = null;
        try {
            bb = this.baselineDao.getLastRealBaseline(elt.getId());
        } catch (CaqsException ce) {
            logger.error("Error while retrieving last baseline for element (id:"
                    + elt.getId() + ")", ce);
        }
        return bb;
    }

    /**
     * returns baseline. returns baseline or linked baseline if there is one
     * @param elt the element for which the baseline has to be returned
     * @param idBline hte baseline id to retrieve
     * @return the last baseline for the element
     */
    public BaselineBean getRealBaselineForEA(ElementBean elt, String idBline) {
        BaselineBean bb = this.baselineDao.retrieveBaselineAndProjectById(idBline);
        try {
            if (bb != null) {
                String realIdBline = this.baselineDao.retrieveLinkedBaseline(elt.getId(), bb.getId());
                if (realIdBline != null) {
                    bb = this.baselineDao.retrieveBaselineAndProjectById(realIdBline);
                }
            }
        } catch (CaqsException ce) {
            logger.error("Error while retrieving last baseline for element (id:"
                    + elt.getId() + ")", ce);
        }
        return bb;
    }

    /**
     * Renvoie toutes les baselines, sauf la baseline d'instantiation et les
     * baselines dont l'analyse a échouée
     * @param idPro l'identifiant du projet
     * @return
     */
    public Collection<BaselineBean> retrieveValidBaselinesByProjectId(String idPro) {
        return this.baselineDao.retrieveValidBaselinesByProjectId(idPro);
    }

    /**
     * @param projectId identifiant du projet
     * @return la liste des baselines valides plus la baseline d'instanciation pour
     * un projet
     */
    public Collection<BaselineBean> retrieveBaselinesAndInstanciationByProjectId(String projectId) {
        return this.baselineDao.retrieveBaselinesAndInstanciationByProjectId(projectId);
    }

    /**
     * recupere toutes les baselines valides anterieures a celle en parametre
     * @param idEa id elt
     * @param currentBaseline baseline
     * @return baselines anterieures
     */
    public List<EvolutionBaselineBean> retrieveAllPreviousValidBaseline(String idEa, BaselineBean currentBaseline) {
        return this.baselineDao.retrieveAllPreviousValidBaseline(idEa, currentBaseline);
    }
}

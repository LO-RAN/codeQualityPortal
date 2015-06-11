/**
 * 
 */
package com.compuware.caqs.service.process;

import com.compuware.caqs.service.TaskSvc;
import com.compuware.caqs.util.RemoteTaskUtils;
import java.io.File;
import java.io.IOException;

import java.util.Properties;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.dbms.DataAccessCache;
import com.compuware.caqs.domain.dataschemas.calcul.impl.CalculationConfig;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.caqs.service.report.ReportSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.business.calculation.ICalculator;
import com.compuware.caqs.business.calculation.impl.Calculator;
import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.EvolutionSvc;

/**
 * Service associated to calculation process.
 * @author cwfr-fdubois
 *
 */
public class CalculationSvc {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final CalculationSvc instance = new CalculationSvc();

    private CalculationSvc() {
    }

    public static CalculationSvc getInstance() {
        return instance;
    }

    public boolean calculate(ElementBean eltBean, String baselineId, String idUser,
            String projectId, boolean calculateMetrics, boolean showMsg) {
        boolean success = false;
        String idMessCalcul = null;
        if (showMsg) {
            idMessCalcul = MessagesSvc.getInstance().addMessageWithStatus(TaskId.COMPUTING, eltBean.getId(),
                    baselineId, idUser, null, null, MessageStatus.IN_PROGRESS);
            MessagesSvc.getInstance().setMessagePercentage(idMessCalcul, -1);
        }
        clearCache(eltBean, baselineId, projectId);
        try {
            CalculationSvc calculationSvc = CalculationSvc.getInstance();
            ICalculationConfig calculationConfig = new CalculationConfig();
            calculationConfig.setNeedMetricCalculation(calculateMetrics);
            calculationConfig.setNeedCriterionCalculation(true);
            calculationConfig.setNeedTrendUpdate(true);
            calculationSvc.calculate(eltBean, calculationConfig);
            success = true;
        } catch (Exception e) {
            logger.error("Error during compute", e);
            success = false;
        } finally {
            clearCache(eltBean, baselineId, projectId);
            if (eltBean != null) {
                // On fait un re-calcul...
                deleteExistingReportFile(eltBean);
            }

            if (showMsg) {
                if (success) {
                    MessagesSvc.getInstance().setMessageTaskStatus(idMessCalcul, MessageStatus.COMPLETED);
                    if (eltBean != null) {
                        TaskSvc.getInstance().updateElementAsRecomputed(eltBean.getId(), baselineId);
                    }
                } else {
                    MessagesSvc.getInstance().setMessageTaskStatus(idMessCalcul, MessageStatus.FAILED);
                }
            }
        }
        return success;
    }

    public void launchRemoteCalculation(ElementBean eltBean, String idUser, boolean calculateMetrics, HttpServletRequest request) {
        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String computeServer = prop.getProperty(Constants.CAQS_COMPUTE_ADDRESS);
        boolean doLocalRecompute = false;
        if (computeServer != null && !"".equals(computeServer)) {
            String sUrl = computeServer + request.getContextPath() + "/calcul?";
            sUrl += "id_ea=" + eltBean.getId();
            sUrl += "&projectId=" + eltBean.getProject().getId();
            sUrl += "&baselineId=" + eltBean.getBaseline().getId();
            sUrl += "&libElt=" + eltBean.getLib();
            sUrl += "&libPrj=" + eltBean.getProject().getLib();
            sUrl += "&metrics=" + calculateMetrics;
            sUrl += "&id_user=" + idUser;
            sUrl += "&showMsg=true";
            doLocalRecompute = !RemoteTaskUtils.callRemoteTask(sUrl);
        } else {
            doLocalRecompute = true;
        }

        if (doLocalRecompute) {
            String baselineId = eltBean.getBaseline().getId();
            String projectId = eltBean.getBaseline().getProject().getId();
            this.calculate(eltBean, baselineId, idUser, projectId,
                    calculateMetrics, true);
        }
    }

    private void clearCache(ElementBean eltBean, String idBline, String idPro) {
        DataAccessCache dataCache = DataAccessCache.getInstance();
        dataCache.clearCache(idBline);
        dataCache.clearCache(idPro);
        if (eltBean != null) {
            dataCache.clearCache(eltBean.getId());
        }
        EvolutionSvc.getInstance().clearBaselineEvolutionDatas(idBline);
    }

    private void deleteExistingReportFile(ElementBean eltBean) {
        String destination = ReportSvc.getReportPath(eltBean);

        File destDir = new File(destination);
        if (destDir.exists()) {
            try {
                FileTools.rdelete(destDir);
            } catch (IOException e) {
                logger.error("Error deleting report directory " + destination, e);
            }
        }
    }

    /**
     * Re-Calculate the given element
     * @param eltBean the given element linked with project and baseline.
     */
    public void calculate(ElementBean eltBean, ICalculationConfig config) throws CaqsException {
        AnalysisConfig analysisParameters = new AnalysisConfig();
        analysisParameters.setProjectId(eltBean.getProject().getId());
        analysisParameters.setBaselineId(eltBean.getBaseline().getId());
        String[] selectedEa = null;
        if (eltBean.getTypeElt() != null && "EA".equals(eltBean.getTypeElt())) {
            //Current element is a module, just select it for calculation.
            //Else every modules will be calculated.
            selectedEa = new String[1];
            selectedEa[0] = eltBean.getId();
        }
        analysisParameters.setModuleArray(selectedEa);
        calculate(analysisParameters, config);
    }

    /**
     * Re-Calculate the given element
     * @param idPro project ID.
     * @param idBline baseline ID.
     */
    public void calculate(AnalysisConfig analysisParameters, ICalculationConfig config) throws CaqsException {
        ICalculator calculator = new Calculator();
        try {
            calculator.init(analysisParameters, config);
            calculator.calculate();
        } finally {
            ImageUtil.getInstance().clearKiviatImage(analysisParameters.getBaselineId());
        }
    }
}

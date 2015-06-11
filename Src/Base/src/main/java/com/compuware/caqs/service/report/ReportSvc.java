package com.compuware.caqs.service.report;

import com.compuware.caqs.business.report.DomainXlsReport;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.business.report.Reporter;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.RemoteTaskUtils;
import com.compuware.toolbox.exception.SystemIOException;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.struts.util.MessageResources;

public class ReportSvc {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final ReportSvc instance = new ReportSvc();

    private ReportSvc() {
    }

    public static ReportSvc getInstance() {
        return instance;
    }
    private Reporter reporter = new Reporter();

    public void forwardToReportGeneratorServer(ElementBean eltBean, String idUser, HttpServletRequest request) {
        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String reportGeneratorServer = prop.getProperty(Constants.CAQS_REPORT_GENERATOR_ADDRESS);
        boolean doLocalGeneration = false;
        Locale loc = RequestUtil.getLocale(request);
        String language = loc.getLanguage();
        if (reportGeneratorServer != null && !"".equals(reportGeneratorServer)) {
            String sUrl = reportGeneratorServer + request.getContextPath()
                    + "/report?";
            sUrl += "id_ea=" + eltBean.getId();
            sUrl += "&id_pro=" + eltBean.getProject().getId();
            sUrl += "&id_bline=" + eltBean.getBaseline().getId();
            sUrl += "&id_user=" + idUser;
            sUrl += "&language=" + language;
            sUrl += "&showMsg=true";
            doLocalGeneration = !RemoteTaskUtils.callRemoteTask(sUrl);
        } else {
            doLocalGeneration = true;
        }

        if (doLocalGeneration) {
            if (eltBean != null) {
                this.generateReportFor(eltBean, RequestUtil.getConnectedUserId(request),
                        loc, RequestUtil.getResources(request), true);
            }
        }
    }

    public boolean generateReportFor(ElementBean eltBean, String userId, Locale loc, MessageResources resources, boolean showMessages) {
        boolean success = false;
        String configDir = CaqsConfigUtil.getCaqsConfigDir();
        String idGeneratingReportMsg = MessagesSvc.getInstance().retrieveNotFinishedMessageId(eltBean.getId(), eltBean.getBaseline().getId(), TaskId.GENERATING_REPORT);
        if (showMessages) {
            if (idGeneratingReportMsg == null) {
                idGeneratingReportMsg = MessagesSvc.getInstance().addMessageWithStatus(TaskId.GENERATING_REPORT, eltBean.getId(),
                        eltBean.getBaseline().getId(), userId, null, null, MessageStatus.IN_PROGRESS);
            }
            MessagesSvc.getInstance().setMessagePercentage(idGeneratingReportMsg, 1);
            MessagesSvc.getInstance().setInProgressMessageToStep(idGeneratingReportMsg, "caqs.process.step.reportstarted");
        }
        try {
            File f = this.generateReport(eltBean, configDir, true, loc, resources, idGeneratingReportMsg);
            if (f != null && f.exists()) {
                success = true;
            }
        } catch (Exception e) {
            logger.error("Exception during report generation : "
                    + e.getMessage());
        } finally {
            if (showMessages) {
                if (success) {
                    MessagesSvc.getInstance().setMessageTaskStatus(idGeneratingReportMsg, MessageStatus.COMPLETED);
                } else {
                    MessagesSvc.getInstance().setMessageTaskStatus(idGeneratingReportMsg, MessageStatus.FAILED);
                }
            }
        }
        return success;
    }

    public File generateReport(ElementBean eltBean, String configDir, boolean forceRegeneration, Locale loc, MessageResources resources, String idMessage) throws IOException, SystemIOException {
        return reporter.generateReport(eltBean, configDir, forceRegeneration, loc, resources, idMessage);
    }

    public File getReportFile(ElementBean eltBean, String idBline, Locale loc) {
        return reporter.getReportFile(eltBean, idBline, loc);
    }

    public static String getReportPath(ElementBean eltBean) {
        return Reporter.getReportPath(eltBean, eltBean.getBaseline().getId());
    }

    public Collection<ElementBean> retrieveAllEa(String idPro, BaselineBean baselineBean) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        Collection<ElementBean> result = elementFacade.retrieveElementByType(idPro, ElementType.EA, baselineBean.getDmaj());
        return result;
    }

    public Workbook generateDomainExcelSynthesis(String domainId, String userId, Locale locale, MessageResources resources) {
        DomainXlsReport re = new DomainXlsReport(domainId, userId, locale, resources);
        return re.generateWorkbook();
    }
}

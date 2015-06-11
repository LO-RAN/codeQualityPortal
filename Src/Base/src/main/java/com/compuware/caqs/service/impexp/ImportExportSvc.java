package com.compuware.caqs.service.impexp;

import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.presentation.util.CaqsUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.InternationalizationSvc;
import com.compuware.caqs.service.ModelSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import com.compuware.caqs.util.RemoteTaskUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.compuware.caqs.business.util.AntExecutor;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.SystemCallResult;
import com.compuware.caqs.exception.CaqsRuntimeException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class ImportExportSvc {

    private static final ImportExportSvc instance = new ImportExportSvc();
    private static Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();

    private ImportExportSvc() {
    }

    public static ImportExportSvc getInstance() {
        return instance;
    }
    public static final String EXPORT_DIR_KEY = "export.dir";
    public static final String IMPORT_DIR_KEY = "import.dir";
    public static final String EXPORT_PROJECT_GOAL = "exportProject";
    public static final String EXPORT_BASELINE_GOAL = "exportBaseline";
    public static final String EXPORT_MODELE_GOAL = "exportModele";
    public static final String IMPORT = "import";

    /**
     * Export data associated to the given project.
     * @param idPro the project id.
     * @return
     * @throws CaqsRuntimeException
     */
    public File exportProject(String idPro) throws CaqsRuntimeException {
        File result = null;
        result = export(idPro, null, EXPORT_PROJECT_GOAL);
        return result;
    }

    /**
     * Export data associated to the given project and baseline.
     * @param idPro the project id.
     * @param idBline the baseline id.
     * @return
     * @throws CaqsRuntimeException
     */
    public File exportBaseline(String idPro, String idBline) throws CaqsRuntimeException {
        File result = null;
        result = export(idPro, idBline, EXPORT_BASELINE_GOAL);
        return result;
    }

    public File export(String idPro, String idBline, String goal) throws CaqsRuntimeException {
        File result = null;
        // Retrieve the project definition
        DaoFactory daoFactory = DaoFactory.getInstance();
        ProjectDao projectFacade = daoFactory.getProjectDao();
        ProjectBean projectBean = projectFacade.retrieveProjectById(idPro);
        if (projectBean != null) {
            SystemCallResult cmdResult = process(getProperties(projectBean, idBline), goal);
            if (cmdResult.getResultCode() == 0) {
                result = retrieveFile(projectBean, idBline);
            }
        }
        return result;
    }

    public boolean exportProject(String idPro, String libPro, String idUser) {
        boolean success = false;
        File zipResultFile = null;
        if (idPro != null) {
            List<String> l = new ArrayList<String>();
            l.add(Constants.MESSAGES_LIBPRJ_INFO1 + libPro);
            String idMsg = MessagesSvc.getInstance().addMessageWithStatus(TaskId.EXPORT_PROJECT, null, null,
                    idUser, l, idPro, MessageStatus.IN_PROGRESS);
            MessagesSvc.getInstance().setMessagePercentage(idMsg, -1);

            try {
                zipResultFile = exportProject(idPro);
                if (zipResultFile != null && zipResultFile.exists() &&
                        zipResultFile.isFile()) {
                    success = true;
                }
            } catch (CaqsRuntimeException e) {
                logger.error("Error during project import/export", e);
            } finally {
                if (success) {
                    MessagesSvc.getInstance().setMessageTaskStatus(idMsg, MessageStatus.COMPLETED);
                } else {
                    MessagesSvc.getInstance().setMessageTaskStatus(idMsg, MessageStatus.FAILED);
                }
            }
        }
        return success;
    }

    public void launchRemoteExportProject(String idPro, String libPro, String idUser, HttpServletRequest request) {
        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String computeServer = prop.getProperty(Constants.CAQS_EXPORT_ADDRESS);
        boolean doLocalProjectExport = false;
        if (computeServer != null && !"".equals(computeServer)) {
            String sUrl = computeServer + request.getContextPath() + "/export?";
            sUrl += "type=project";
            sUrl += "&idPro=" + idPro;
            sUrl += "&libPro=" + libPro;
            sUrl += "&idUser=" + idUser;
            doLocalProjectExport = !RemoteTaskUtils.callRemoteTask(sUrl);
        } else {
            doLocalProjectExport = true;
        }

        if (doLocalProjectExport) {
            this.exportProject(idPro, libPro, idUser);
        }
    }

    public void launchRemoteExportModel(String idUsa, String idUser, HttpServletRequest request) {
        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String computeServer = prop.getProperty(Constants.CAQS_EXPORT_ADDRESS);
        boolean doLocalModelExport = false;
        Locale loc = RequestUtil.getLocale(request);
        if (computeServer != null && !"".equals(computeServer)) {
            String sUrl = computeServer + request.getContextPath() + "/export?";
            sUrl += "type=modele";
            sUrl += "&idUsa=" + idUsa;
            sUrl += "&language=" + loc.getLanguage();
            sUrl += "&idUser=" + idUser;
            doLocalModelExport = !RemoteTaskUtils.callRemoteTask(sUrl);
        } else {
            doLocalModelExport = true;
        }

        if (doLocalModelExport) {
            this.exportModel(idUsa, idUser, RequestUtil.getLocale(request));
        }
    }

    public boolean exportModel(String idUsa, String idUser, Locale loc) {
        boolean success = false;

        File zipResultFile = null;
        if (idUsa != null) {
            String libUsa = "";
            UsageBean ub = ModelSvc.getInstance().retrieveUsageById(idUsa);
            if (ub != null) {
                libUsa = ub.getLib(loc);
            }
            List<String> l = new ArrayList<String>();
            l.add(Constants.MESSAGES_ARGS_INFO1 + libUsa);
            String idMsg = MessagesSvc.getInstance().addMessageWithStatus(TaskId.EXPORT_MODEL, null, null,
                    idUser, l, idUsa, MessageStatus.IN_PROGRESS);
            MessagesSvc.getInstance().setMessagePercentage(idMsg, -1);

            try {
                zipResultFile = exportModele(idUsa);
                if (zipResultFile != null && zipResultFile.exists() &&
                        zipResultFile.isFile()) {
                    success = true;
                }
            } catch (CaqsRuntimeException e) {
                logger.error("Error during import/export", e);
            } finally {
                if (success) {
                    MessagesSvc.getInstance().setMessageTaskStatus(idMsg, MessageStatus.COMPLETED);
                } else {
                    MessagesSvc.getInstance().setMessageTaskStatus(idMsg, MessageStatus.FAILED);
                }
            }
        }
        return success;
    }

    public boolean importZip(String target, String fileName, String idUser) {
        String idMsg = null;
        boolean success = false;

        File f = new File(fileName);
        if (f != null && f.exists()) {

            List<String> l = new ArrayList<String>();
            l.add(Constants.MESSAGES_ARGS_INFO1 + f.getName());
            TaskId ti = TaskId.IMPORT_MODEL;
            if (target.equals("Project")) {
                ti = TaskId.IMPORT_PROJECT;
            }
            idMsg = MessagesSvc.getInstance().addMessageWithStatus(ti, null, null,
                    idUser, l, f.getName(), MessageStatus.IN_PROGRESS);
            MessagesSvc.getInstance().setMessagePercentage(idMsg, -1);

            try {
                int retour = ImportExportSvc.getInstance().importData(f, target);
                success = (retour == 0);
            } catch (CaqsRuntimeException e) {
                logger.error("import zip file error", e);
            }
        }

        if (idMsg != null) {
            if (success) {
                MessagesSvc.getInstance().setMessageTaskStatus(idMsg, MessageStatus.COMPLETED);
                if ("Modele".equals(target)) {
                    InternationalizationSvc.getInstance().initResourceBundles();
                }
            } else {
                MessagesSvc.getInstance().setMessageTaskStatus(idMsg, MessageStatus.FAILED);
            }
        }
        return success;
    }

    public void launchRemoteImportZip(String target, String fileName, String idUser, HttpServletRequest request) {
        Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String computeServer = prop.getProperty(Constants.CAQS_IMPORT_ADDRESS);
        boolean doLocalImportZip = false;
        if (computeServer != null && !"".equals(computeServer)) {
            String sUrl = computeServer + request.getContextPath() + "/import?";
            sUrl += "target=" + target;
            sUrl += "&fileName=" + fileName;
            sUrl += "&idUser=" + idUser;
            doLocalImportZip = !RemoteTaskUtils.callRemoteTask(sUrl);
        } else {
            doLocalImportZip = true;
        }

        if (doLocalImportZip) {
            this.importZip(target, fileName, idUser);
        }
    }

    /**
     * Export data associated to the given usage.
     * @param idUsa the usage id.
     */
    public File exportModele(String idUsa) throws CaqsRuntimeException {
        File result = null;
        SystemCallResult cmdResult = process(getModeleProperties(idUsa), EXPORT_MODELE_GOAL);
        if (cmdResult.getResultCode() == 0) {
            result = retrieveModeleFile(idUsa);
        }
        return result;
    }

    private SystemCallResult process(Properties prop, String goal) throws CaqsRuntimeException {
        SystemCallResult result = new SystemCallResult();
        AntExecutor command = new AntExecutor(logger);
        // Extract source code from EA's source directory.
        try {
            result = command.processAntScript(goal, prop);
        } catch (IOException e) {
            logger.error("Error during import/export", e);
            throw new CaqsRuntimeException("Error during import/export", e);
        }
        return result;
    }

    private String getRootDir() {
        return new File(dynProp.getProperty(Constants.CAQS_ALLDATAS_DIR)).getAbsolutePath();
    }

    private String getExportDestinationDir(ProjectBean projectBean) {
        String destDir = getRootDir();
        destDir = FileTools.concatPath(destDir, dynProp.getProperty(EXPORT_DIR_KEY));
        destDir = FileTools.concatPath(destDir, projectBean.getLib());
        return destDir;
    }

    private String getExportDestinationDir(String subDir) {
        String destDir = getRootDir();
        destDir = FileTools.concatPath(destDir, dynProp.getProperty(EXPORT_DIR_KEY));
        destDir = FileTools.concatPath(destDir, subDir);
        return destDir;
    }

    public String getImportDestinationDir() {
        String destDir = getRootDir();
        destDir = FileTools.concatPath(destDir, dynProp.getProperty(IMPORT_DIR_KEY));
        return destDir;
    }

    private String getFileName(ProjectBean projectBean, String idBline) {
        StringBuffer result = new StringBuffer(projectBean.getLib());
        if (idBline != null) {
            result.append('-').append(idBline);
        }
        result.append(".zip");
        return result.toString();
    }

    private Properties getProperties(ProjectBean projectBean, String idBline) {
        Properties prop = new Properties();
        if (idBline != null) {
            prop.put("ID_BLINE", idBline);
        }
        prop.put("ID_PRO", projectBean.getId());
        prop.put("LIB_PRO", projectBean.getLib());
        prop.put("DEST_DIR", getExportDestinationDir(projectBean));

        prop.put("GlobalDir", getRootDir());
        return prop;
    }

    private Properties getModeleProperties(String idUsa) {
        Properties prop = new Properties();
        prop.put("ID_USA", idUsa);
        prop.put("DEST_DIR", getExportDestinationDir(idUsa));
        prop.put("GlobalDir", getRootDir());
        return prop;
    }

    public File retrieveProjectFile(String idPro, String idBline) {
        File result = null;
        // Retrieve the project definition
        DaoFactory daoFactory = DaoFactory.getInstance();
        ProjectDao projectFacade = daoFactory.getProjectDao();
        ProjectBean projectBean = projectFacade.retrieveProjectById(idPro);
        result = this.retrieveFile(projectBean, idBline);
        return result;
    }

    private File retrieveFile(ProjectBean projectBean, String idBline) {
        File result = null;
        File dir = new File(getExportDestinationDir(projectBean));
        result = new File(dir, getFileName(projectBean, idBline));
        if (!result.exists()) {
            result = null;
        }
        return result;
    }

    public File retrieveModeleFile(String idUsa) {
        File result = null;
        File dir = new File(getExportDestinationDir(idUsa));
        result = new File(dir, idUsa + ".zip");
        if (!result.exists()) {
            result = null;
        }
        return result;
    }

    public int importData(File fileToImport, String target) throws CaqsRuntimeException {
        Properties prop = new Properties();
        prop.put("DEST_DIR", fileToImport.getParentFile().getAbsolutePath());
        prop.put("DEST_ZIP_FILE", fileToImport.getName());
        prop.put("GlobalDir", new File(dynProp.getProperty(Constants.CAQS_ALLDATAS_DIR)).getAbsolutePath());
        return process(prop, IMPORT + target).getResultCode();
    }

    public File createFile(String destDir, String fileName, InputStream input) throws IOException {
        File destZipFile = new File(destDir, fileName);
        if (destZipFile.exists()) {
            destZipFile.delete();
        }
        File dest = new File(destDir);
        dest.mkdirs();
        destZipFile.createNewFile();
        FileOutputStream writer = new FileOutputStream(destZipFile);
        byte[] bytes = new byte[2048];
        while (input.read(bytes, 0, 2048) != -1) {
            writer.write(bytes);
        }
        writer.close();
        return destZipFile;
    }
}

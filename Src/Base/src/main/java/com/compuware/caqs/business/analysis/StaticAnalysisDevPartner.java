/*
 * StaticAnalysisDevPartner.java
 * @author  cwfr-fxalbouy
 * Created on 8 novembre 2002, 10:42
 */
package com.compuware.caqs.business.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.SystemIO;
import com.compuware.toolbox.exception.SystemIOException;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.filter.RegexpFilter;
import com.compuware.toolbox.io.filter.RegexpTools;
import com.compuware.toolbox.util.StringUtils;
import org.openide.util.Exceptions;

public class StaticAnalysisDevPartner extends StaticAnalysis {

    //devpartner
    protected String mDevPartnerBatchPath;
    private File slnFile = null;

    /**
     * Creates a new instance of StaticAnalysisDevPartner
     */
    public StaticAnalysisDevPartner() {
        super();
        logger.info("Tool is Devpartner");
    }

    @Override
    protected void initSpecific(Properties dbProps) {
        //devpartner
        String caqsHome = CaqsConfigUtil.getCaqsHome();
        this.mDevPartnerBatchPath = FileTools.concatPath(caqsHome, Constants.DEVPARTNER_PLUGIN_BATCH_CMD);
    }

    protected boolean isAnalysisPossible(EA curEA, Properties dynProp) {
        boolean result = true;

        if (StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "vb_VB")
                || StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cs_C#")) {

            // if not the right version
            if (( isOldVersionRequired(curEA, dynProp) != dynProp.getProperty(CODE_REVIEW_VERSION_KEY).equals("7") )) {
                // give notification and leave silently
                logger.info("DevPartner is not configured for " + curEA.getDialecte().getLib() + " on this system; ignoring request for EA : " + curEA.getLib() + " !");
                result = false;
            }
        } else {
            logger.info("DevPartner Studio can only be used on VB or C#; ignoring request for EA: " + curEA.getLib() + " !");
            result = false;
        }
        return result;
    }

    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        if (isAnalysisPossible(curEA, dynProp)) {


            File configFile = createConfigurationFile(curEA);
            if (configFile != null) {

                String cmd;
                if (curEA.getDialecte().getId().equals("vb_VB6")) {
                    cmd = dynProp.getProperty(CODE_REVIEW_6_CMD_KEY);
                } else {
                    cmd = dynProp.getProperty(CODE_REVIEW_CMD_KEY);
                }

                String codeReviewCmd = cmd + " /F " + configFile.getAbsolutePath() + " " + extractVisualStudioOption(curEA);
                logger.info("Executing " + codeReviewCmd);
                try {
                    SystemIO.exec(codeReviewCmd, this.logger, null);
                }
                catch (SystemIOException e) {
                    throw new AnalysisException("Error during devpartner studio analysis", e);
                }
            } else {
                throw new AnalysisException("Impossible to find the configFile");
            }
        }
    }

    @Override
    protected AnalysisResult analysisCheck(EA curEA) {
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        if (isAnalysisPossible(curEA, CaqsConfigUtil.getCaqsGlobalConfigProperties())) {
            // new release of MicroFocus DevPartner Studio creates a file with a ".dpmdb" suffix,
            // even if we ask for a ".mdb" suffix in the config file !
            // => thus we check for this file, and if it exists, copy it to the result.mdb file we are expecting.
            File dpmdbfile = new File(FileTools.concatPath(curEA.getTargetDirectory(), "\\devpartnerstudio\\result.dpmdb"));
            File mdbfile = new File(FileTools.concatPath(curEA.getTargetDirectory(), "\\devpartnerstudio\\result.mdb"));
            if (dpmdbfile.exists()) {
                try {
                    FileTools.copy(dpmdbfile, mdbfile);
                }
                catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

            result.setSuccess(checkForExistingNonEmptyBinaryFile(FileTools.concatPath(curEA.getTargetDirectory(), "\\devpartnerstudio\\result.mdb")));
        }
        return result;
    }
    private static final String CODE_REVIEW_CMD_KEY = "devpartner.codereview.cmd";
    private static final String CODE_REVIEW_6_CMD_KEY = "devpartner.codereview6.cmd";
    private static final String CODE_REVIEW_VERSION_KEY = "devpartner.codereview.version";

    private boolean isOldVersionRequired(EA curEA, Properties dynProp) {
        // DevPartner 7 can deal with VB6 and up to VS 2003 VB and C# dialects
        if (curEA.getDialecte().getId().equals("vb_VB6")
                || curEA.getDialecte().getId().equals("vb_VB.Net")
                || curEA.getDialecte().getId().equals("cs_C#.Net")) {
            return true;
        } else {
            return false;
        }

    }

    private void setProjectFile(EA curEA, String pattern) {

        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        // Retrieve all EA's data from database.
        ElementBean ea = elementFacade.retrieveAllElementDataById(curEA.getId());
        String srcDirStr = FileTools.concatPath(ea.getFullSourceDirPath(), "/src/");

        String vbpPath = ea.getProjectFilePath();
        if (vbpPath != null && vbpPath.length() > 0) {
            slnFile = new File(FileTools.concatPath(srcDirStr, vbpPath));
            logger.info("Looking for file:" + slnFile.getAbsolutePath());
        }

        if (slnFile == null || !slnFile.exists()) {
            logger.info("No given project file, searching one");
            File srcDir = new File(srcDirStr);
            RegexpFilter filter = new RegexpFilter();
            filter.setAcceptDirectory(true);

            filter.setRegexp(pattern);
            List projectFiles = RegexpTools.searchFiles(srcDir, filter);
            if (projectFiles != null && !projectFiles.isEmpty()) {
                slnFile = (File) projectFiles.get(0);
            }
        }
    }

    private boolean createVBConfigFile(EA curEA, File configFile) throws AnalysisException {
        boolean retour = false;

        setProjectFile(curEA, ".*\\.vbp");

        if (slnFile != null && slnFile.exists()) {
            try {
                File targetDir = new File(FileTools.concatPath(curEA.getTargetDirectory(), "\\devpartnerstudio\\"));
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                File mdbFile = new File(targetDir, "result.mdb");
                if (mdbFile.exists()) {
                    mdbFile.delete();
                }
                PrintWriter writer = new PrintWriter(new FileWriter(configFile));
                writer.println("[PROJECT]");
                writer.println("VBCOMPILER=6");
                writer.println("PROJECT=" + slnFile.getAbsolutePath());
                writer.println("SESSIONFILE=" + mdbFile.getAbsolutePath());
                writer.println("SUMMARYFILE=" + targetDir.getAbsolutePath() + "\\result.htm");
                writer.println("RULESET=All Rules");
                writer.println("METRICS=1");
                writer.println("UNCALLED=1");
                writer.println("CROSSREF=1");
                writer.println("SKIPNAMING=0");
                writer.println("[MODULES]");
                writer.flush();
                writer.close();
                retour = true;
            }
            catch (FileNotFoundException e) {
                throw new AnalysisException("Error creating VB config file", e);
            }
            catch (IOException e) {
                throw new AnalysisException("Error creating VB config file", e);
            }
        } else {
            throw new AnalysisException("Project file not found: " + ( slnFile == null ? "null" : slnFile.getAbsolutePath() ));
        }
        return retour;
    }

    private boolean createDotNetConfigFile(EA curEA, File configFile) throws AnalysisException {
        boolean retour = false;

        setProjectFile(curEA, ".*\\.sln");

        if (slnFile != null && slnFile.exists()) {
            try {
                File targetDir = new File(FileTools.concatPath(curEA.getTargetDirectory(), "\\devpartnerstudio\\"));
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                
                // added specific check for .dpmdb file created by new MicroFocus DevPartner Studio
                File mdbFile = new File(targetDir, "result.dpmdb");
                if (mdbFile.exists()) {
                    mdbFile.delete();
                }

                mdbFile = new File(targetDir, "result.mdb");
                if (mdbFile.exists()) {
                    mdbFile.delete();
                }

                PrintWriter writer = new PrintWriter(new FileWriter(configFile));
                writer.println("[SOLUTION]");
                writer.println("SOLUTION=" + slnFile.getAbsolutePath());
                writer.println("RULESET=All Rules");
                writer.println("UNCALLED=1");
                writer.println("METRICS=1");
                writer.println("NAMING=1");
                writer.println("UseVSNETNaming=1");
                writer.println("AnalyzeNamespaces=1");
                writer.println("AnalyzeClasses=1");
                writer.println("AnalyzeInterfaces=1");
                writer.println("AnalyzeDelegates=1");
                writer.println("AnalyzeEvents=1");
                writer.println("AnalyzeEnums=1");
                writer.println("AnalyzeStructs=1");
                writer.println("AnalyzeParameters=1");
                writer.println("AnalyzeMethods=1");
                writer.println("AnalyzeVars=1");
                writer.println("AnalyzePublicsOnly=1");
                writer.println("UseCamelOnLocals=1");
                writer.println("Dictionary=0");
                writer.println("CodeReviewVersion=8.2 Build (1)");
                writer.print("SESSIONFILE=");
                writer.println(targetDir.getAbsolutePath() + "\\result.mdb");
                writer.print("SUMMARYFILE=");
                writer.println(targetDir.getAbsolutePath() + "\\result.htm");
                writer.flush();
                writer.close();
                retour = true;
            }
            catch (FileNotFoundException e) {
                throw new AnalysisException("Error creating config file", e);
            }
            catch (IOException e) {
                throw new AnalysisException("Error creating config file", e);
            }
        } else {
            throw new AnalysisException("Solution file not found: " + ( slnFile == null ? "null" : slnFile.getAbsolutePath() ));
        }
        return retour;
    }

    private File createConfigurationFile(EA curEA) throws AnalysisException {



        File configFile = new File(curEA.getTargetDirectory() + "/src/codeReviewConfig.crb");
        if (curEA.getDialecte().getId().contains(".Net")) {
            if (!this.createDotNetConfigFile(curEA, configFile)) {
                configFile = null;
            }
        } else {
            if (!this.createVBConfigFile(curEA, configFile)) {
                configFile = null;
            }
        }
        return configFile;
    }

    private String extractVisualStudioOption(EA curEA) throws AnalysisException {
        String result = "";

        if (slnFile != null && slnFile.exists()) {
            try {
                FileReader freader = new FileReader(slnFile);
                BufferedReader breader = new BufferedReader(freader);
                String line = breader.readLine();
                while (line != null && result.length() == 0) {
                    if (line.endsWith("Format Version 9.00")) {
                        result = "/vs \"8.0\"";
                    } else if (line.endsWith("Format Version 8.00")) {
                        result = "/vs \"7.1\"";
                    } else if (line.endsWith("Format Version 10.00")) {
                        result = "/vs \"9.0\"";
                    } else if (line.endsWith("Format Version 11.00")) {
                        result = "/vs \"10.0\"";
                    } else {
                        line = breader.readLine();
                    }
                }
                breader.close();
                freader.close();
            }
            catch (FileNotFoundException e) {
                throw new AnalysisException("File not found: " + slnFile.getAbsolutePath(), e);
            }
            catch (IOException e) {
                throw new AnalysisException("Error during file read", e);
            }
        }
        return result;
    }
    private static final String CLASS_METRIC_XML_FILE = "/devpartnerstudio/classMetrics.xml";
    private static final String METHOD_METRIC_XML_FILE = "/devpartnerstudio/methodMetrics.xml";
    private static final String CALLS_TO_FILE = "/devpartnerstudio/callsto.csv";

    private void consolidateResults(EA curEA) throws LoaderException {
        File reportPath = new File(curEA.getTargetDirectory() + "/devpartnerstudio/");
        if (!reportPath.exists()) {
            reportPath.mkdirs();
        }
        String reportDirectoryPath = reportPath.getAbsolutePath();
        if (!reportDirectoryPath.endsWith("/") && !reportDirectoryPath.endsWith("\\")) {
            reportDirectoryPath += "/";
        }

        String cmd =
                Constants.OS_SPECIFIC_DOUBLE_QUOTE + this.mDevPartnerBatchPath + Constants.OS_SPECIFIC_DOUBLE_QUOTE
                + " " + Constants.OS_SPECIFIC_DOUBLE_QUOTE + FileTools.concatPath(curEA.getTargetDirectory(), "/src/") + Constants.OS_SPECIFIC_DOUBLE_QUOTE
                + " " + Constants.OS_SPECIFIC_DOUBLE_QUOTE + reportDirectoryPath + Constants.OS_SPECIFIC_DOUBLE_QUOTE
                + " " + curEA.getDialecte().getId();
        logger.info("Starting DevPartner Analysis : " + cmd);

        try {
            SystemIO.exec(cmd, this.logger, null);
        }
        catch (SystemIOException e) {
            throw new LoaderException("Error consolidating devpartner studio results", e);
        }
    }

    @Override
    protected void loadData(EA curEA) throws LoaderException {
        //load Data
        if (isAnalysisPossible(curEA, CaqsConfigUtil.getCaqsGlobalConfigProperties())) {
            consolidateResults(curEA);

            ProjectBean projectBean = new ProjectBean();
            projectBean.setId(this.config.getProjectId());

            BaselineBean baselineBean = new BaselineBean();
            baselineBean.setId(this.config.getBaselineId());

            ElementBean eaElt = new ElementBean();
            eaElt.setId(curEA.getId());

            FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
            loader.setMainTool(this.config.isMasterTool());

            loader.load(new DataFile(DataFileType.CLS, curEA.getTargetDirectory() + CLASS_METRIC_XML_FILE, true));
            loader.load(new DataFile(DataFileType.MET, curEA.getTargetDirectory() + METHOD_METRIC_XML_FILE, false));

            File callsToFile = new File(curEA.getTargetDirectory() + CALLS_TO_FILE);
            if (callsToFile.exists()) {
                new ArchitectureLoader(curEA, this.config.getBaselineId(), curEA.getTargetDirectory() + CALLS_TO_FILE);
            }
        }
        //END load Data
    }
}

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;

import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.ErrorFileUtil;
import com.compuware.caqs.util.FileUtils;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.StringUtils;

public class StaticAnalysisDevEntreprise extends StaticAnalysisAntGeneric {

    private static final String TOOLS_OUTPUT_PATH = File.separator + "deventreprisereports";
    private static final String XMLFILENAME = TOOLS_OUTPUT_PATH + File.separator + "report.xml";
    private static final String CALLS_TO_FILE = TOOLS_OUTPUT_PATH + File.separator + "callsto.csv";
    private static final String CSV_DIAG_FILE_BASENAME = TOOLS_OUTPUT_PATH + File.separator + "Diagnostics";
    private static final String ERROR_FILE_BASENAME = TOOLS_OUTPUT_PATH + File.separator + "program-errors-";
    private static final int DIAGNOSTIC_CODE_IDX = 5;
    private static final int DIAGNOSTIC_PROGRAM_IDX = 1;
    private static final int DIAGNOSTIC_TEXT_IDX = 6;
    private static final String UNABLE_TO_OPEN_INCLUDE = "I007";
    protected String mDevEntrepriseBatchPath = "";

    /** Creates a new instance of StaticAnalysisDevEntreprise */
    public StaticAnalysisDevEntreprise() {
        super();
        logger.info("Tool is DevEntreprise");
    }

    @Override
    protected AnalysisResult analysisCheck(EA curEA) {
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        if (isAnalysisPossible(curEA)) {
            String collectionNames = curEA.getInfo1();
            if (collectionNames != null && collectionNames.length() > 0) {
                String[] collectionNameArray = collectionNames.substring(12).split(",");
                for (int i = 0; i < collectionNameArray.length; i++) {
                    File errorFile = new File(curEA.getTargetDirectory() + ERROR_FILE_BASENAME + collectionNameArray[i] + ".txt");
                    File csvDiagFile = new File(curEA.getTargetDirectory() + CSV_DIAG_FILE_BASENAME + collectionNameArray[i] + ".txt");
                    writeErrorFromDiagnostic(curEA, errorFile, csvDiagFile);
                }
                File xml = new File(curEA.getTargetDirectory() + XMLFILENAME);
                if (!xml.exists()) {
                    result.setSuccess(false);
                    result.setMessage("Report file not generated");
                }
            }
        }
        return result;
    }

    private boolean writeErrorFromDiagnostic(EA curEA, File errorFile, File csvDiagFile) {
        StringBuilder errorLines = new StringBuilder();
        if (errorFile.exists()) {
            ErrorFileUtil.writeError(this.config.getBaselineId(), "analysis", errorFile, logger);
        }
        if (csvDiagFile.exists()) {
            try {
                CSVReader diagnostic = new CSVReader(new FileReader(csvDiagFile), '\t');
                String[] currentLine = null;
                while ((currentLine = diagnostic.readNext()) != null) {
                    if (currentLine[DIAGNOSTIC_CODE_IDX] != null && (currentLine[DIAGNOSTIC_CODE_IDX].startsWith("E")
                            || currentLine[DIAGNOSTIC_CODE_IDX].startsWith(UNABLE_TO_OPEN_INCLUDE))) {
                        errorLines.append(currentLine[DIAGNOSTIC_CODE_IDX]).append(';');
                        errorLines.append(currentLine[DIAGNOSTIC_PROGRAM_IDX]).append(';');
                        errorLines.append(currentLine[DIAGNOSTIC_TEXT_IDX]).append(";\n");
                    }
                }
            } catch (IOException e) {
                logger.error("Error extracting error from diagnostic", e);
            }
        } else {
            errorLines.append("Diagnostic " + csvDiagFile.getName() + " does not exist.");
        }
        ErrorFileUtil.writeError(this.config.getBaselineId(), "analysis", errorLines.toString());
        return errorLines.length() > 0;
    }

    @Override
    protected void initSpecific(Properties dbProps) {
        String caqsHome = CaqsConfigUtil.getCaqsHome();
        this.mDevEntrepriseBatchPath = FileTools.concatPath(caqsHome, Constants.DEVENTREPRISE_PLUGIN_BATCH_CMD);
    }

    @Override
    protected void loadData(EA curEA) throws LoaderException {
        if (isAnalysisPossible(curEA)) {
            DataFile clsFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + getXmlFileReportPath(), true);

            ProjectBean projectBean = new ProjectBean();
            projectBean.setId(this.config.getProjectId());

            BaselineBean baselineBean = new BaselineBean();
            baselineBean.setId(this.config.getBaselineId());

            ElementBean eaElt = new ElementBean();
            eaElt.setId(curEA.getId());

            FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
            loader.setMainTool(this.config.isMasterTool());

            loader.load(clsFile);

            File callsToFile = new File(curEA.getTargetDirectory() + CALLS_TO_FILE);
            if (callsToFile.exists()) {
                curEA.setProject(projectBean);
                new ArchitectureLoader(curEA, this.config.getBaselineId(), curEA.getTargetDirectory() + CALLS_TO_FILE);
            }
        }
    }

    protected boolean isAnalysisPossible(EA curEA) {
        boolean result = true;

        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cobol")) {
            logger.info("DevEntreprise can only be used on Cobol; ignoring request for EA: " + curEA.getLib() + " !");
            result = false;
        }
        return result;
    }

    protected String getXmlFileReportPath() {
        return XMLFILENAME;
    }

    @Override
    protected String getAntTarget() {
        return "deventrepriseAnalysis";
    }

    @Override
    protected void postToolAnalysis() {
    }
}

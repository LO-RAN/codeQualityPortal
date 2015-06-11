package com.compuware.carscode.plugin.deventreprise.dataschemas;

import com.compuware.carscode.plugin.deventreprise.util.ReturnCodes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.carscode.plugin.deventreprise.dao.DaoFactory;
import com.compuware.carscode.plugin.deventreprise.dao.DevEntrepriseDao;
import com.compuware.carscode.plugin.deventreprise.util.Constants;
import com.compuware.carscode.plugin.deventreprise.util.CsvReader;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

public class Program extends CobolSource {

    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");

    private static final String NB_ALPHANUMERIC_TO_NUMERIC = "ALPHANUMERIC-TO-NUMERIC";
    private static final String NB_ALPHANUMERIC_TO_ALPHABETIC = "ALPHANUMIC-TO-ALPHA";
    private static final String NB_ALPHABETIC_TO_NUMERIC = "ALPHA-TO-NUMERIC";
    private static final String NB_NUMERIC_TO_ALPHABETIC = "NUMERIC-TO-ALPHA";
    private static final String NB_SIGNED_TO_UNSIGNED = "SIGNED-TO-UNSIGNED";
    private static final String MAX_DATA_ITEM_LENGTH = "MAX-VAR-SIZE";
    //private static final String LOC = "TOTAL-LOC";
    private static final String NB_LST_COMPLEX_COMPUTE = "COMPLEX-COMPUTE";//LST-COMPLEX-COMPUTE
    private static final String NB_CONDITIONS_NOT_USED = "CONDITION-NOT-USED";
    private static final String NB_LOWER_PRECISION_MOVES = "TRUNCATION-DURING-MOVE";//"LOWER-PRECISION";
    private static final String NB_ARITHMETIC_OVERFLOWS = "ARITHMETIC-OVERFLOW";
    //private static final String NB_LINES_PROC_DIV = "PROCEDURE-LOC";
    private static final String IDENTIFIER_TOO_LONG = "IDENTIFIER_TOO_LONG";
    private static final String LEARNING_FAILED_METRIC = "LEARNING_FAILED";
    private Diagnostic diagnostic = null;
    private int uniqueOperators = 0;
    private int uniqueOperands = 0;
    private int totalOperators = 0;
    private int totalOperands = 0;
    private int nbLinesProcDiv = 0;
    private double difficulty = 0;
    private double effort = 0;
    private List<Procedure> procedures;

    private List<String> links;

    public Program(String name, String collection, CobolPackage cp) {
        super(name, collection, cp);
        likeName = name;
        this.procedures = new ArrayList<Procedure>();
    }

    private ReturnCodes waitForFinishedLearning(DevEntrepriseDao dao) {
        ReturnCodes retour = ReturnCodes.LEARNING_FAILED;
        String status = dao.getLearningStatus(this.id);
        if (!"Complete".equals(status) && !"Failed".equals(status)) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                retour = ReturnCodes.LEARNING_FAILED;
            }
        }
        if (status.equals("Complete")) {
            retour = ReturnCodes.NO_ERROR;
        }
        else if (status.equals("Failed")) {
            retour = ReturnCodes.LEARNING_FAILED;
        }
        else {
            retour = ReturnCodes.LEARNING_PENDING;
        }
        return retour;
    }

    public ReturnCodes fillProperties(CsvReader halstead, CsvReader mccabe, CsvReader diagnostic, List<Copy> copies, String xmlFile) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        DevEntrepriseDao dao = DaoFactory.getInstance().getDevPartnerDao();
        List<Violation> l = null;
        this.id = dao.getIdFromDatabase(this.name, this.collection);
        if (this.id != 0) {
            logger.debug("Check for learning for program " + this.name);
            retour = this.waitForFinishedLearning(dao);
            if (retour == ReturnCodes.NO_ERROR) {
                logger.debug("Extracting Halstead metrics...");
                this.extractInformationsFromHasltead(halstead);
                logger.debug("Extracting McCabe metrics...");
                Thread procAnalysisThread = this.extractInformationsFromMcCabe(mccabe);
                logger.debug("Extracting Diagnostic metrics...");
                this.extractInformationsFromDiagnostic(diagnostic);
                logger.debug("Querying database for metrics...");
                this.variables = dao.getNumericVariablesMore18Digits(this.id, this.getLikeName());
                Map<String, List<Violation>> results = dao.getNbMoveProblems(this.id, this.name);
                l = results.get(DevEntrepriseDao.ALPHANUMERIC_TO_NUMERIC);
                this.setViolation(Program.NB_ALPHANUMERIC_TO_NUMERIC, l, copies);
                l = results.get(DevEntrepriseDao.ALPHANUMERIC_TO_ALPHABETIC);
                this.setViolation(Program.NB_ALPHANUMERIC_TO_ALPHABETIC, l, copies);
                l = results.get(DevEntrepriseDao.ALPHABETIC_TO_NUMERIC);
                this.setViolation(Program.NB_ALPHABETIC_TO_NUMERIC, l, copies);
                l = results.get(DevEntrepriseDao.NUMERIC_TO_ALPHABETIC);
                this.setViolation(Program.NB_NUMERIC_TO_ALPHABETIC, l, copies);
                l = results.get(DevEntrepriseDao.SIGNED_TO_UNSIGNED);
                this.setViolation(Program.NB_SIGNED_TO_UNSIGNED, l, copies);

                int maxDataItemLength = dao.getMaxDataItemLength(this.id, this.name);
                this.violations.put(Program.MAX_DATA_ITEM_LENGTH, new Integer(maxDataItemLength));

                this.setViolation(Program.NB_CONDITIONS_NOT_USED, dao.getNbConditionsNotUsed(this.id, this.name), copies);
                this.setViolation(Program.NB_LOWER_PRECISION_MOVES, dao.getNbLowerPrecisionMoves(this.id, this.name), copies);
                this.setViolation(Program.NB_ARITHMETIC_OVERFLOWS, dao.getNbArithmeticOverflow(this.id, this.name), copies);
                this.setViolation(Program.NB_LST_COMPLEX_COMPUTE, dao.getLstComplexCompute(this.id, this.name), copies);
                this.setViolation(Program.IDENTIFIER_TOO_LONG, dao.getIdentiferTooLong(this.id, this.name), copies);
                logger.debug("Extracting metrics from xml definition...");
                dao.getMetricsFromXML(xmlFile, this, copies);

                logger.debug("Extract dependencies...");
                links = dao.getProgramDependencies(this.id);

                try {
                    procAnalysisThread.join();
                }
                catch (InterruptedException e) {
                    logger.error("Procedure analysis interupted", e);
                }
            }
            else if (ReturnCodes.LEARNING_FAILED.equals(retour)) {
                this.addViolation(Program.LEARNING_FAILED_METRIC, new Violation("", this.name));
            }
        }
        return retour;
    }

    public void setLearningFailed() {
        this.addViolation(Program.LEARNING_FAILED_METRIC, new Violation("", this.name));
    }

    public String getElementType() {
        return "FILE";//"PROGRAM";
    }

    public String getName() {
        return name;
    }

    public String toString() {
        StringBuffer retour = new StringBuffer();
        retour.append("  <elt type=\"" + this.getElementType() + "\" name=\"" +
                this.getCobolPackage().getCobolPackage() + "." + this.getName() +
                "\" filepath=\"" + this.getCobolPackage().getCobolPackagePath() + "/" + this.getElementName() + ".CBL\">\n");
        retour.append("    <metric id=\"COBOL_UNIQUE_OPERATORS\" value=\"" + this.uniqueOperators + "\" />\n");
        retour.append("    <metric id=\"COBOL_UNIQUE_OPERANDS\" value=\"" + this.uniqueOperands + "\" />\n");
        retour.append("    <metric id=\"COBOL_TOTAL_OPERATORS\" value=\"" + this.totalOperators + "\" />\n");
        retour.append("    <metric id=\"COBOL_TOTAL_OPERANDS\" value=\"" + this.totalOperands + "\" />\n");
        retour.append("    <metric id=\"COBOL_DIFFICULTY\" value=\"" + this.difficulty + "\" />\n");
        retour.append("    <metric id=\"COBOL_EFFORT\" value=\"" + this.effort + "\" />\n");
        retour.append("    <metric id=\"PROCEDURE-LOC\" value=\"" + this.nbLinesProcDiv + "\" />\n");
        retour.append("    <metric id=\"PROCEDURE-PARAGRAPHS\" value=\"" + this.procedures.size() + "\" />\n");
        if (this.variables != null && this.variables.size() > 0) {
            List<Variable> l = new ArrayList<Variable>();
            for (Variable v : this.variables) {
                if (v.isMoreThan18Digits()) {
                    l.add(v);
                }
            }
            if (!l.isEmpty()) {
                retour.append("    <metric id=\"NUMERIC-VAR-TOO-BIG\" value=\"" + l.size() + "\" lines=\"");
                boolean first = true;
                for (Variable v : l) {
                    if (!first) {
                        retour.append(",");
                    }
                    retour.append(v.getLine());
                    first = false;
                }
                retour.append("\" />\n");
            }
        }
        retour.append(super.toStringBuffer());
        if (this.diagnostic != null) {
            retour.append(this.diagnostic.toString());
        }
        retour.append("  </elt>\n");
        if (this.procedures != null) {
            for (Procedure p : this.procedures) {
                retour.append(p.toString());
            }
        }
        return retour.toString();
    }

    public String getLinksAsCsv(Map<String, String> idDescMap, char separator) {
        StringBuilder result = new StringBuilder();
        if (links != null) {
            for (String linkId: links) {
                if (idDescMap.containsKey(linkId)) {
                    result.append(this.getCobolPackage().getCobolPackage()).append(".").append(this.getName());
                    result.append(separator);
                    result.append(idDescMap.get(linkId));
                    result.append(separator).append('\n');
                }
            }
        }
        return result.toString();
    }

    public int getNbLinesProcDiv() {
        return nbLinesProcDiv;
    }

    public void setNbLinesProcDiv(int nbLinesProcDiv) {
        this.nbLinesProcDiv = nbLinesProcDiv;
    }

    private Thread extractInformationsFromMcCabe(CsvReader mccabe) {
        ProcedureAnalyzer procAnalyzer = new ProcedureAnalyzer(this, mccabe);
        Thread t = new Thread(procAnalyzer);
        t.start();
        return t;
    }

    private class ProcedureAnalyzer implements Runnable {

        Program program;
        CsvReader mccabe;

        public ProcedureAnalyzer(Program program, CsvReader mccabe) {
            this.program = program;
            this.mccabe = mccabe;
        }

        public void run() {
            List<Map<String, String>> lines = mccabe.getAllLinesFor("Program", getElementName().toUpperCase() + ".CBL");
            if (lines != null && !lines.isEmpty()) {
                try {
                    for (Iterator<Map<String, String>> it = lines.iterator(); it.hasNext();) {
                        Map<String, String> line = it.next();
                        FileReader fr = new FileReader(getParentDirectory().getAbsolutePath()+File.separator+getElementName().toUpperCase() + ".CBL");
                        BufferedReader br = new BufferedReader(fr, 2048);
                        //une nouvelle procédure
                        Procedure p = new Procedure(line, program);
                        logger.debug(" -> Procedure " + p.getName() + "...");
                        p.analyze(br);
                        if (p.getLoc() > 0) {
                            /* The procedure is included in the program source file.
                             * The metrics have been calculated. */
                            procedures.add(p);
                        }
                        br.close();
                        fr.close();
                    }
                }
                catch (IOException e) {
                    logger.error("Error reading the source file", e);
                }
            }
        }
        
    }

    private void extractInformationsFromDiagnostic(CsvReader diagnostic) {
        this.diagnostic = new Diagnostic(diagnostic, this.getElementName());
    }

    public void extractInformationsFromHasltead(CsvReader halstead) {
        Map<String, String> line = halstead.getUniqueLinesFor("Program", name.toUpperCase() + ".CBL");
        if (line != null) {
            String s = line.get(Constants.UNIQUE_OPERATORS);
            uniqueOperators = (new Integer(s)).intValue();
            s = line.get(Constants.UNIQUE_OPERANDS);
            uniqueOperands = (new Integer(s)).intValue();
            s = line.get(Constants.TOTAL_OPERATORS);
            totalOperators = (new Integer(s)).intValue();
            s = line.get(Constants.TOTAL_OPERANDS);
            totalOperands = (new Integer(s)).intValue();
            s = line.get(Constants.DIFFICULTY);
            difficulty = (new Double(s)).doubleValue();
            s = line.get(Constants.EFFORT);
            effort = (new Double(s)).doubleValue();
        }
        else {
            logger.debug("Searching " + name.toUpperCase() + ".CBL");
        }
    }

    @Override
    public Map<String, Object> getGlobalParams() {
        Map<String, Object> retour = super.getCobolSourceGlobalParams();
        return retour;
    }
}

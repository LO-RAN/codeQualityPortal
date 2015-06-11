package com.compuware.carscode.plugin.deventreprise;

import com.compuware.carscode.plugin.deventreprise.util.ReturnCodes;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.PropertiesReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.compuware.carscode.plugin.deventreprise.dao.DaoFactory;
import com.compuware.carscode.plugin.deventreprise.dao.DevEntrepriseDao;
import com.compuware.carscode.plugin.deventreprise.dataschemas.CobolPackage;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Copy;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Program;
import com.compuware.carscode.plugin.deventreprise.util.CobolFilenameFilter;
import com.compuware.carscode.plugin.deventreprise.util.CsvReader;
import com.compuware.carscode.plugin.deventreprise.util.launchcollector.LaunchCollectorCommandExec;
import com.compuware.toolbox.io.CommandExec;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DevEntrepriseAnalyzer {

    private static final String DIAGNOSTIC = "Diagnostics";
    private static final String HALSTEAD = "Halstead";
    private static final String MC_CABE = "McCabe";
    private static final String CONFIG_FILENAME = "config.properties";
    private static final String RUN_COLLECTOR_BATCH = "runCollector.bat";
    private static final String MAX_LEARNING_FAILED_KEY = "learning.maxfailed";
    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");
    private List<Program> programs = null;
    private List<Copy> copies = null;
    private Properties dynProp;
    private List<CobolPackage> programsDirs;
    private List<CobolPackage> includesDirs;
    private String reportDirectory;
    private String pluginPath;
    private String srcDirectory;
    private int maxNbLearningFailed = 0;
    private List<Program> programLearningFailedList = new ArrayList<Program>();

    public DevEntrepriseAnalyzer(String reportDirectory, String pluginPath,
            String srcDirectory) {
        this.programs = new ArrayList<Program>();
        this.copies = new ArrayList<Copy>();
        this.reportDirectory = reportDirectory;
        this.pluginPath = pluginPath;
        this.srcDirectory = srcDirectory;

        String configPath = pluginPath + File.separator + CONFIG_FILENAME;
        this.dynProp = PropertiesReader.getProperties(configPath, this, false);
        String sMaxFailed = dynProp.getProperty(MAX_LEARNING_FAILED_KEY);
        if (sMaxFailed != null && sMaxFailed.length() > 0) {
            this.maxNbLearningFailed = Integer.parseInt(sMaxFailed);
        }
    }

    public ReturnCodes consolidateResults() {
        ReturnCodes retour = ReturnCodes.ERROR_WRITING_RESULTS;
        try {
            logger.info("XML report generation...");
            File f = new File(this.reportDirectory);
            if (!f.exists()) {
                f.mkdirs();
            }
            if (f.isDirectory()) {
                File f2 = new File(f, "report.xml");
                OutputStreamWriter owriter = new OutputStreamWriter(new FileOutputStream(f2), "utf-8");
                PrintWriter writer = new PrintWriter(owriter);
                writer.println("<Result>");
                writer.println(this.toXMLOutput());
                writer.println("</Result>");
                writer.flush();
                writer.close();
                retour = ReturnCodes.NO_ERROR;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("DevEntrepriseAnalyzer:consolidateResults:"
                    + e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("DevEntrepriseAnalyzer:consolidateResults:"
                    + e.getMessage());
        }
        return retour;
    }

    private Map<String, Program> createMap(List<Program> programs) {
        Map<String, Program> result = new HashMap<String, Program>();
        if (programs != null) {
            for (Program p : programs) {
                result.put(p.getName(), p);
            }
        }
        return result;
    }

    private String dateToString(Date lastSearchTime) {
        String result = "";
        if (lastSearchTime != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = df.format(lastSearchTime);
        }
        return result;
    }

    private ReturnCodes exportLinks(Map<String, String> idDescMap) {
        ReturnCodes retour = ReturnCodes.ERROR_WRITING_RESULTS;
        try {
            logger.info("CSV dependencies report generation...");
            File f = new File(this.reportDirectory);
            if (!f.exists()) {
                f.mkdirs();
            }
            if (f.isDirectory()) {
                File f2 = new File(f, "callsto.csv");
                OutputStreamWriter owriter = new OutputStreamWriter(new FileOutputStream(f2), "utf-8");
                PrintWriter writer = new PrintWriter(owriter);
                writer.println(linksToCsv(idDescMap));
                writer.flush();
                writer.close();
                retour = ReturnCodes.NO_ERROR;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("DevEntrepriseAnalyzer:consolidateResults:"
                    + e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error("DevEntrepriseAnalyzer:consolidateResults:"
                    + e.getMessage());
        }
        return retour;
    }

    private Map<String, String> extractIdDescMap() {
        Map<String, String> result = new HashMap<String, String>();
        for (Program p : this.programs) {
            result.put("" + p.getId(), p.getElementDesc());
        }
        for (Copy c : this.copies) {
            result.put("" + c.getId(), c.getElementDesc());
        }
        return result;
    }

    private String linksToCsv(Map<String, String> idDescMap) {
        StringBuilder result = new StringBuilder();
        for (Program p : this.programs) {
            result.append(p.getLinksAsCsv(idDescMap, ';'));
        }
        return result.toString();
    }

    private ReturnCodes launchProgramAnalyser(String collection) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        int processResult = 0;

        String includesForCmd = "";
        boolean first = true;
        for (CobolPackage include : this.includesDirs) {
            if (!first) {
                includesForCmd += ",";
            }
            includesForCmd += "\""
                    + include.getCobolPackageDirectory().getAbsolutePath()
                    + "\"";
            first = false;
        }

        for (CobolPackage source : this.programsDirs) {
            String cmd = this.dynProp.getProperty("metadata.installPath");
            cmd += "/BINW/xtract.exe";
            cmd += " \"" + source.getCobolPackageDirectory().getAbsolutePath()
                    + "/*\"";
            cmd += " -c " + includesForCmd;
            cmd += " -h \"" + this.reportDirectory + File.separator + HALSTEAD
                    + collection + ".txt\"";
            cmd += " -m \"" + this.reportDirectory + File.separator + MC_CABE
                    + collection + ".txt\"";
            cmd += " -r \"" + this.reportDirectory + File.separator + DIAGNOSTIC
                    + collection + ".txt\" -x \"Tab\"";
            System.out.println("commande de lancement xtract.exe : " + cmd);
            // Launching the collect.
            processResult = CommandExec.exec(cmd, this.getSystemEnv(), new File(pluginPath), logger);
            if (processResult != 0) {
                retour = ReturnCodes.ERROR_PROGRAM_ANALYSER;
            }
        }
        return retour;
    }

    private ReturnCodes launchCollector(String pluginPath, String collection) {
        String cmd = pluginPath + File.separator + RUN_COLLECTOR_BATCH + " ";
        if (this.dynProp.getProperty("metadataServer.deleteColl", "0").equals("1")) {
            cmd += "-d ";
        }
        cmd += this.dynProp.getProperty("metadataServer") + " ";
        cmd += this.dynProp.getProperty("metadataServer.port") + " ";
        cmd += collection;

        // Launching the collect.
        return LaunchCollectorCommandExec.exec(cmd, this.getSystemEnv(), new File(pluginPath), logger);
    }

    private ReturnCodes copyStructureToSrcDir(String cobolPath, List<CobolPackage> dirsList) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        CobolPackage cp = new CobolPackage(cobolPath, this.srcDirectory);
        File fDownloadDir = new File(this.dynProp.getProperty("metadata.installPath"), "Download");
        if (fDownloadDir != null && fDownloadDir.exists()
                && fDownloadDir.isDirectory()) {
            String[] dirs = cobolPath.split("\\.");
            if (dirs != null) {
                File fSrcDir = new File(this.srcDirectory);
                if (fSrcDir != null) {
                    String thisDownloadDirPath = fDownloadDir.getAbsolutePath();
                    for (int i = 0; i < dirs.length; i++) {
                        fSrcDir = new File(fSrcDir, dirs[i]);
                        thisDownloadDirPath += File.separator + dirs[i];
                    }
                    if (!fSrcDir.exists()) {
                        fSrcDir.mkdirs();
                    }
                    cp.setCobolPackageDirectory(fSrcDir);
                    dirsList.add(cp);

                    File downloadPath = new File(thisDownloadDirPath);
                    if (downloadPath != null) {
                        File[] list = downloadPath.listFiles();
                        try {
                            FileTools.copy(list, fSrcDir);
                        } catch (IOException e) {
                            logger.error("Error while copying source files", e);
                            retour = ReturnCodes.IMPOSSIBLE_TO_COPY_DOWNLOAD;
                        }
                    }

                } else {
                    retour = ReturnCodes.SRC_DIR_NOT_ACCESSIBLE;
                }
            }
        } else {
            logger.error("DevEntreprise plugin not accessible : "
                    + this.dynProp.getProperty("metadata.installPath"));
            retour = ReturnCodes.DEVENTREPRISE_DOWNLOAD_DIR_NOT_ACCESSIBLE;
        }
        return retour;
    }

    private ReturnCodes copyAllFiles(String collectionName, DevEntrepriseDao dao) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        List<String> downloadedSourceDirs = dao.retrieveAllSourcesForCollection(collectionName);
        List<String> filters = dao.retrieveAllProgramMemberNames(collectionName);
        this.programsDirs = new ArrayList<CobolPackage>();
        for (String s : downloadedSourceDirs) {
            retour = this.copyStructureToSrcDir(s, this.programsDirs);
            if (retour != ReturnCodes.NO_ERROR) {
                break;
            }
        }
        if (retour == ReturnCodes.NO_ERROR) {
            List<String> downloadedIncludesDirs = dao.retrieveAllIncludesForCollection(collectionName);
            this.includesDirs = new ArrayList<CobolPackage>();
            for (String s : downloadedIncludesDirs) {
                retour = this.copyStructureToSrcDir(s, this.includesDirs);
                if (retour != ReturnCodes.NO_ERROR) {
                    break;
                }
            }
        }

        return retour;
    }

    private ReturnCodes analyseCollection(String collectionName, DevEntrepriseDao dao, String pluginDirectory) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        logger.info("Analyze collections " + collectionName);

        File f = new File(this.reportDirectory);
        if (f != null && !f.exists()) {
            f.mkdirs();
        }

        logger.info("Collect...");
        Date lastSearchTime = new Date(0);
        retour = this.launchCollector(this.pluginPath, collectionName);
        System.out.println("result from runcollection : " + retour.getCode());
        if (retour == ReturnCodes.NO_ERROR) {
            retour = this.copyAllFiles(collectionName, dao);
            if (retour == ReturnCodes.NO_ERROR) {
                for (CobolPackage cp : this.programsDirs) {
                    this.programs.addAll(this.getProgramsNames(cp, collectionName));
                }
                for (CobolPackage cp : this.includesDirs) {
                    this.copies.addAll(this.getCopiesNames(cp, collectionName));
                }
                logger.info("Program Analyzer...");
                retour = this.launchProgramAnalyser(collectionName);
                if (retour == ReturnCodes.NO_ERROR) {
                    File fHasltead = new File(reportDirectory, HALSTEAD
                            + collectionName + ".txt");
                    CsvReader halstead = new CsvReader(fHasltead.getAbsolutePath(), "\\t");
                    File fMcCabe = new File(reportDirectory, MC_CABE
                            + collectionName + ".txt");
                    CsvReader mccabe = new CsvReader(fMcCabe.getAbsolutePath(), "\\t");
                    File fDiagnostic = new File(reportDirectory, DIAGNOSTIC
                            + collectionName + ".txt");
                    CsvReader diagnostic = new CsvReader(fDiagnostic.getAbsolutePath(), "\\t");

                    logger.info("Metadata Database Analyzer...");
                    Map<String, Program> programMap = createMap(programs);

                    while (!programMap.isEmpty() && (ReturnCodes.NO_ERROR.equals(retour)
                            || ReturnCodes.LEARNING_FAILED.equals(retour))) {
                        Date tmp = new Date();
                        List<String> newCompletedPrograms = dao.retrieveLastCompletedPrograms(collectionName, dateToString(lastSearchTime));
                        lastSearchTime = tmp;
                        if (newCompletedPrograms.isEmpty()) {
                            int nbPending = dao.countLearningProgramsWithStatus(collectionName, "Pending");
                            if (nbPending > 0) {
                                try {
                                    logger.info("No new program completed, "
                                            + nbPending
                                            + " programs pending, wait for a minute for a new check...");
                                    Thread.sleep(60000);
                                } catch (InterruptedException ex) {
                                    logger.error("Error during thread sleep...", ex);
                                }
                            } else {
                                // No more pending programs, set others as failed.
                                Iterator<Program> i = programMap.values().iterator();
                                while (i.hasNext()) {
                                    Program p = i.next();
                                    p.setLearningFailed();
                                    this.programLearningFailedList.add(p);
                                    i.remove();
                                    logger.info("Program " + p.getName()
                                            + " -> Failed.");
                                }
                                break;
                            }
                        } else {
                            Iterator<String> i = newCompletedPrograms.iterator();
                            while (i.hasNext()
                                    && (ReturnCodes.NO_ERROR.equals(retour)
                                    || ReturnCodes.LEARNING_FAILED.equals(retour))) {
                                String pname = i.next();
                                Program p = programMap.get(pname);
                                if (p != null) {
                                    retour = p.fillProperties(halstead, mccabe, diagnostic, copies, pluginDirectory
                                            + File.separator + "definitions.xml");
                                    programMap.remove(pname);
                                    if (!ReturnCodes.NO_ERROR.equals(retour)) {
                                        break;
                                    }
                                    this.getPropertiesFromFile(p);
                                    logger.info("Program " + p.getName()
                                            + " -> Done.");
                                }
                            }
                        }
                    }
                    if ((retour == ReturnCodes.NO_ERROR) || (retour
                            == ReturnCodes.LEARNING_FAILED)) {
                        for (Copy c : copies) {
                            c.fillProperties(halstead, mccabe, diagnostic, copies);
                            this.getPropertiesForCopy(c);
                            logger.info("Copy " + c.getName() + " -> Done.");
                        }
                    }
                    if ((retour == ReturnCodes.NO_ERROR) || (retour
                            == ReturnCodes.LEARNING_FAILED)) {
                        if (this.programLearningFailedList.size()
                                > maxNbLearningFailed) {
                            retour = ReturnCodes.LEARNING_FAILED;
                        } else {
                            retour = ReturnCodes.NO_ERROR;
                        }
                    }
                }
            }
        }
        return retour;
    }

    private ReturnCodes launchAnalysis(String collectionsName, String pluginDirectory) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        String[] collections = collectionsName.split(",");

        DevEntrepriseDao dao = DaoFactory.getInstance().getDevPartnerDao();
        retour = dao.setConnection(this.dynProp);
        if (retour == ReturnCodes.NO_ERROR) {
            //on lance le collector et on copie les fichiers
            for (int i = 0; (i < collections.length) && (retour
                    == ReturnCodes.NO_ERROR); i++) {
                retour = this.analyseCollection(collections[i], dao, pluginDirectory);
            }
            dao.closeConnection();
            if (retour == ReturnCodes.NO_ERROR) {
                retour = this.consolidateResults();
            }
            if (retour == ReturnCodes.NO_ERROR) {
                Map<String, String> idDescMap = extractIdDescMap();
                retour = this.exportLinks(idDescMap);
            }
        }
        if (!this.programLearningFailedList.isEmpty()) {
            saveErrors(collectionsName);
        }
        return retour;
    }

    private void saveErrors(String collectionsName) {
        File reportDir = new File(this.reportDirectory);
        if (reportDir.exists()) {
            reportDir.mkdirs();
        }
        if (reportDir.exists()) {
            File errorFile = new File(reportDir, "program-errors-"
                    + collectionsName + ".txt");
            try {
                FileWriter fw = new FileWriter(errorFile);
                BufferedWriter bw = new BufferedWriter(fw);
                for (Program p : this.programLearningFailedList) {
                    bw.append(p.getName()).append(": Learning FAILED");
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (IOException e) {
                logger.error("Error saving errors to file", e);
            }
        }
    }

    private void getPropertiesFromFile(Program p) {
        int loc = 0;
        boolean hasTabs = false;
        File f = new File(p.getParentDirectory(), p.getElementName() + ".CBL");
        if (f != null && f.exists()) {
            FileReader fr;
            try {
                fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line = null;
                boolean inProcDiv = false;
                int locProcDiv = 0;

                Pattern pattern = Pattern.compile("\\d*\\s*PROCEDURE\\s*DIVISION.\\s*\\d*");

                while ((line = br.readLine()) != null) {
                    loc++;
                    if (line.contains("\t")) {
                        hasTabs = true;
                    }
                    if (inProcDiv) {
                        locProcDiv++;
                    } else {
                        Matcher m = pattern.matcher(line);
                        if (m.matches()) {
                            inProcDiv = true;
                        }
                    }
                }
                p.setNbLinesProcDiv(locProcDiv);
                br.close();
                fr.close();
            } catch (FileNotFoundException e) {
                logger.error("DevEntrepriseAnalyzer:getPropertiesFromFile:"
                        + e.getMessage());
            } catch (IOException e) {
                logger.error("DevEntrepriseAnalyzer:getPropertiesFromFile:"
                        + e.getMessage());
            }

        }
        p.setHasTabs(hasTabs);
        p.setLoc(loc);
    }

    private void getPropertiesForCopy(Copy c) {
        int loc = 0;
        boolean hasTabs = false;
        File f = new File(c.getParentDirectory(), c.getElementName() + ".CBL");
        if (f != null && f.exists()) {
            FileReader fr;
            try {
                fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);
                String line = null;

                while ((line = br.readLine()) != null) {
                    loc++;
                    if (line.contains("\t")) {
                        hasTabs = true;
                    }
                }
                br.close();
                fr.close();
            } catch (FileNotFoundException e) {
                logger.error("DevEntrepriseAnalyzer:getPropertiesFromFile:"
                        + e.getMessage());
            } catch (IOException e) {
                logger.error("DevEntrepriseAnalyzer:getPropertiesFromFile:"
                        + e.getMessage());
            }

        }
        c.setHasTabs(hasTabs);
        c.setLoc(loc);
    }

    public String toXMLOutput() {
        StringBuffer sb = new StringBuffer();
        for (Program p : programs) {
            sb.append(p.toString());
        }
        for (Copy c : copies) {
            sb.append(c.toString());
        }
        return sb.toString();
    }

    public List<Program> getProgramsNames(CobolPackage cp, String collection) {
        List<Program> retour = new ArrayList<Program>();

        if (cp.getCobolPackageDirectory() != null
                && cp.getCobolPackageDirectory().isDirectory()) {
            String[] children = cp.getCobolPackageDirectory().list(new CobolFilenameFilter());
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    String programName = children[i].substring(0, children[i].lastIndexOf("."));
                    Program p = new Program(programName, collection, cp);
                    retour.add(p);
                }
            }
        }
        return retour;
    }

    public List<Copy> getCopiesNames(CobolPackage cp, String collection) {
        List<Copy> retour = new ArrayList<Copy>();

        if (cp.getCobolPackageDirectory() != null
                && cp.getCobolPackageDirectory().isDirectory()) {
            String[] children = cp.getCobolPackageDirectory().list(new CobolFilenameFilter());
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    String programName = children[i].substring(0, children[i].lastIndexOf("."));
                    Copy p = new Copy(programName, collection, cp);
                    retour.add(p);
                }
            }
        }
        return retour;
    }

    private String[] getSystemEnv() {
        Map<String, String> envMap = System.getenv();
        String[] result = new String[envMap.size()];
        Set<String> keySet = envMap.keySet();
        Iterator<String> iter = keySet.iterator();
        String key = null;
        int i = 0;
        while (iter.hasNext()) {
            key = iter.next();
            result[i++] = key + "=" + envMap.get(key);
        }
        return result;
    }

    public static void main(String[] args) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        if (args.length == 4) {
            String collectionName = args[0];
            DevEntrepriseAnalyzer analyser = new DevEntrepriseAnalyzer(args[2], args[3], args[1]);
            retour = analyser.launchAnalysis(collectionName, args[3]);
        } else {
            logger.error("DevEntrepriseAnalyser usage :");
            logger.error("[Collection name] [Source directory] [XML Results directory] [plugin path]");
            logger.error("The result directory should also contain the following files :");
            logger.error("diag.txt, Halstead.txt, McCabe.txt");
            retour = ReturnCodes.BAD_PARAMETERS;
        }
        logger.error("Analysis return code: " + retour.toString());
        System.exit(retour.getCode());
    }

    /*public static void main(String[] args) {
    // renault
    //D:/Analyses/renault/Cobol/src
    //D:/Analyses/renault/Cobol/deventreprisereports
    //D:/Analyses/renault/Cobol/deventreprisereports
    String collectionName = "TEST";
    DevEntrepriseAnalyzer analyser = new DevEntrepriseAnalyzer("D:/Analyses/pdaprod_v2/deventreprisereports",
    "D:/CAQS/Traitements/deventreprise/plugin",
    "D:/Analyses/pdaprod_v2/src");
    ReturnCodes retour = analyser.launchAnalysis(collectionName);
    System.exit(retour.getCode());
    }*/
}

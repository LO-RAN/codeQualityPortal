

package com.compuware.carscode.plugin.advisor;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.compuware.advisor.application.commandLine.CommandLine;
import com.compuware.advisor.framework.command.CommandFailedException;
import com.compuware.advisor.framework.command.CommandSyntaxException;
import com.compuware.advisor.framework.command.PredictionFailedException;
import com.compuware.advisor.plugin.ruleValidation.plugability.RulesPlugIn;
import com.compuware.advisor.plugin.view.validation.codeValidation.summary.CodeValidationExporter;
import com.compuware.advisor.plugin.view.validation.codeValidation.summary.CodeValidationSumTaskListener;
import com.compuware.advisor.plugin.view.validation.codeValidation.summary.CodeValidationSummary;
import com.compuware.advisor.plugin.view.validation.codeValidation.summary.CodeValidationSummaryTask;
import com.compuware.advisor.util.global.UIManager;


public class OptimalAdvisorAnalyser {

    private Logger logger = Logger.getLogger("StaticAnalysis");

    /* Constantes pour l'analyse OptimalAdvisor. */
    public static final String OPTIMAL_ADVISOR_HOME_KEY = "advisor.home";

    public static final String OPTIMAL_ADVISOR_RULESET_OPTION_KEY = "ruleset";
    public static final String OPTIMAL_ADVISOR_RULESET_SELECTED_OPTION_KEY = "rulesetSelected";

    public static final String OPTIMAL_ADVISOR_RULES_ID_KEY = "advisor.rulesid";
    public static final String OPTIMAL_ADVISOR_RULES_SELECTED_ID_KEY = "advisor.rulesselected";

    public static final String OPTIMAL_ADVISOR_INPUTPATH_OPTION_KEY = "inputPath";
    public static final String OPTIMAL_ADVISOR_SOURCEPATH_OPTION_KEY = "sourcepath";

    public OptimalAdvisorAnalyser() {
    }

    public boolean analyse(String sourceDir, String destDir, String destFileName, String propertiesFile) {
        boolean retour = true;
        logger.info("début de l'analyse optimaladvisor");

        //System.loadLibrary("CLMGR");

        logger.debug("srcDir="+sourceDir);
        logger.debug("destDir="+destDir);

        CommandLine cl = new CommandLine();
        Locale loc = new Locale("EN", "EN");

        try {
            cl.init();

            Properties prop = readProperties(propertiesFile);
            cl.getOptions().setString(OPTIMAL_ADVISOR_RULESET_OPTION_KEY, prop.getProperty(OPTIMAL_ADVISOR_RULES_ID_KEY));
            cl.getOptions().setString(OPTIMAL_ADVISOR_RULESET_SELECTED_OPTION_KEY, prop.getProperty(OPTIMAL_ADVISOR_RULES_SELECTED_ID_KEY));
            
            java.util.ArrayList al = new java.util.ArrayList();

            File bytecodeDir = new File(sourceDir + File.separator + "bin");
            if (bytecodeDir.exists() && "true".equalsIgnoreCase(prop.getProperty("usebytecode"))) {
                cl.getOptions().setString("bytecodepath", bytecodeDir.getAbsolutePath());
                cl.getOptions().setBoolean("usesourceparser", false);
            	al.add("newjava");
            	al.add(sourceDir + File.separator + "bin");
            }
            else {
                cl.getOptions().setString("bytecodepath", bytecodeDir.getAbsolutePath());
                cl.getOptions().setBoolean("usesourceparser", true);
            	al.add("newjava");
            	al.add(sourceDir + File.separator + "src");
            }
            cl.getPlugInManager().getCommandCenter().interpretCommand(al, System.err, System.err);
        } catch (CommandSyntaxException ex) {
            retour = false;
            logger.error("Illegal command: " + ex.getMessage());
        } catch (PredictionFailedException ex) {
            retour = false;
            logger.error(ex.getMessage());
        } catch (CommandFailedException ex) {
            logger.error("Command failed: " + ex.getMessage());
        }
        if (retour == true) {
            ResourceBundle resource = ResourceBundle.getBundle("com.compuware.advisor.plugin.view.validation.codeValidation.Resources", loc);

            CodeValidationExporter codeValidationExporter = new CodeValidationExporter(cl.getPlugInManager(), resource, cl.getOptions());
            CodeValidationSummary cvs = new CodeValidationSummary(cl.getPlugInManager(), resource, null, cl.getOptions(), new UIManager(null));
            RulesPlugIn jrules = (RulesPlugIn) cl.getPlugInManager().getPlugIn(com.compuware.advisor.plugin.jrules.JRulesPlugIn.class);

            cl.getOptions().setString(OPTIMAL_ADVISOR_INPUTPATH_OPTION_KEY, sourceDir + File.separator + "src");
            cl.getOptions().setString(OPTIMAL_ADVISOR_SOURCEPATH_OPTION_KEY, sourceDir + File.separator + "src");
            CodeValidationSumTaskListener callback = cvs;
            String msgGeneratingHtml = resource.getString("msgGeneratingHtml");
            CodeValidationSummaryTask task = new CodeValidationSummaryTask(msgGeneratingHtml, jrules, codeValidationExporter, callback, new UIManager(null));
            jrules.runRules(task);

            logger.info("Export xml rule validation...");
            String value = codeValidationExporter.generateXMLRuleValidation();
            logger.info("Formating xml...");
            value = formatXml(value);
            java.io.File file = null;
            try {
                File destDirFile = new File(destDir);
                if (!destDirFile.exists()) {
                    destDirFile.mkdirs();
                }
                file = new File(destDirFile.getAbsolutePath(), destFileName);
                java.io.FileWriter fw = new java.io.FileWriter(file);
                java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
                bw.write(value);
                bw.newLine();
                bw.close();
                fw.close();
            } catch (Exception e) {
                retour = false;
                logger.error(e);
            }
        }
        return retour;
    }
    
    private String formatXml(String value) {
        StringBuffer result = new StringBuffer("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><data><details>");
        int idxDetail = value.indexOf("<details>");
        int idxEndDetail = value.lastIndexOf("</details>");
        String tmpViolations = value.substring(idxDetail + 9, idxEndDetail);
        tmpViolations = tmpViolations.replaceAll("<", "&lt;");
        tmpViolations = tmpViolations.replaceAll("&lt;violation", "<violation");
        result.append(tmpViolations);
        result.append("</details></data>");
        return result.toString();
    }

    private Properties readProperties(String fileName) {
        Properties prop = new Properties();
        try {
            File f = new File(fileName);
            if (f.exists()) {
                prop.load(new FileInputStream(f));
            }
            else {
                prop.load(this.getClass().getResourceAsStream(fileName));
            }
        } catch (java.io.IOException io) {
            logger.error("### PropertiesReader, IO Exception, could not find the file "+fileName+" ###");
            logger.error("### PropertiesReader, IO Exception: " + io.getMessage());
        }
        return prop;
    }

    /** args[0] == Source Dir
     * args[1] == Destination Dir
     * args[2] == Destination file name
     * args[3] == Properties file
     */
    public static void main(String[] args) {
    	try {
    		OptimalAdvisorAnalyser aoa = new OptimalAdvisorAnalyser();
    		aoa.analyse(args[0], args[1], args[2], args[3]);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}

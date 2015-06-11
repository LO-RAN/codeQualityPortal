package com.compuware.caqs.business.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.analysis.SystemCallResult;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.StreamStringSearchKey;
import com.compuware.caqs.util.SystemIO;
import com.compuware.caqs.util.SystemIOResult;
import com.compuware.toolbox.exception.SystemIOException;
import com.compuware.toolbox.io.FileTools;
import org.apache.log4j.Logger;

/**
 * @author cwfr-fdubois
 */
public class AntExecutor {

    /** Result code if the ant target does not exists. */
    private static final int TARGET_DOES_NOT_EXIST = -2;

	/** The JNDI key for ant classpath access. */
    public static final String ANT_CLASSPATH_ENV_KEY = "ant.classpath";

    /** The Java path key for ant execution. */
    private static final String ANT_JAVA_PATH_KEY = "JAVA_PATH";
	/** The JNDI key for ant jvm options access. */
    private static final String ANT_JVM_OPTIONS_KEY = "ant.jvm.options";

    /** The logger. */
    private Logger logger;

    /**
     * Constructor using a logger.
     * @param t the logger to use.
     */
    public AntExecutor(Logger t) {
    	this.logger = t;
    }

    /**
     * Set the logger for the ant execution.
     * @param logger the logger to use.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /** Execute a script Ant located in the given ea.
     * @param ea The ea element.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @throws java.io.IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult processAntScript(ElementBean ea, String goal, Properties propList) throws java.io.IOException {
    	File propFile = saveJavaProperties(ea.getFullSourceDirPath(), propList, false);
    	SystemCallResult result = doProcessAntScript(getBuildDir(ea), goal, propList, propFile, new File("."));
    	if (result != null && result.getResultCode() == TARGET_DOES_NOT_EXIST) {
        	result = doProcessAntScript(getBuildDir(), goal, propList, propFile, new File("."));
    	}
        propFile.delete();
    	return result;
    }

    /** Execute a script Ant located in the given ea.
     * @param ea The ea element.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @param workingDir The working directory.
     * @throws java.io.IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult processAntScript(ElementBean ea, String goal, Properties propList, File workingDir) throws java.io.IOException {
    	File propFile = saveJavaProperties(ea.getFullSourceDirPath(), propList, false);
    	SystemCallResult result = doProcessAntScript(getBuildDir(ea), goal, propList, propFile, workingDir);
    	if (result != null && result.getResultCode() == TARGET_DOES_NOT_EXIST) {
        	result = doProcessAntScript(getBuildDir(), goal, propList, propFile, workingDir);
    	}
        propFile.delete();
    	return result;
    }

    /** Execute a script Ant located in the default source directory.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @throws java.io.IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult processAntScript(String goal, Properties propList) throws java.io.IOException {
    	return processAntScript(getBuildDir(), goal, propList);
    }

    /** Execute a script Ant located in the given source directory.
     * @param srcDir The source directory where the build.xml Ant file is located.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @throws java.io.IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult processAntScript(String srcDir, String goal, Properties propList) throws java.io.IOException {
    	return processAntScript(srcDir, goal, propList, new File("."));
    }

	/** Execute a script Ant located in the default source directory.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @param workingDir The working directory.
     * @throws java.io.IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult processAntScript(String goal, Properties propList, File workingDir) throws java.io.IOException {
    	return processAntScript(getBuildDir(), goal, propList, workingDir);
    }

	/** Execute a script Ant located in the given source directory.
     * @param srcDir The source directory where the build.xml Ant file is located.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @param workingDir The working directory.
     * @throws java.io.IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult processAntScript(String srcDir, String goal, Properties propList, File workingDir) throws java.io.IOException {
    	File propFile = saveJavaProperties(srcDir, propList, true);
    	SystemCallResult result = doProcessAntScript(srcDir, goal, propList, propFile, workingDir);
    	propFile.delete();
    	return result;
    }

    /** Execute a script Ant located in the given source directory.
     * @param srcDir The source directory where the build.xml Ant file is located.
     * @param goal The ant goal to execute. If null, then execute the default goal.
     * @param propList Properties to add to the ant command line.
     * @param propFile the ant properties file to set.
     * @param workingDir The working directory.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult  doProcessAntScript(String srcDir, String goal, Properties propList, File propFile, File workingDir) {
    	SystemCallResult  result = new SystemCallResult();

        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();

        // Get a valid classpath to execute ant.
        String antClassPath = dynProp.getProperty(ANT_CLASSPATH_ENV_KEY, System.getProperty("java.class.path", "."));

        String javaCmd = getJavaCommand(propList);

        // Execute the Ant command on the build.xml file located into the source directory.
        logger.info("Running ant command...");
        logger.debug("AntExecutor.processAntScript: command executed:");
        String cmd = javaCmd + " -cp " + Constants.OS_SPECIFIC_DOUBLE_QUOTE + antClassPath + Constants.OS_SPECIFIC_DOUBLE_QUOTE
        		+ " " + dynProp.getProperty(AntExecutor.ANT_JVM_OPTIONS_KEY)
        		+ " -Dant.home="+getBuildDir()
        		+ " org.apache.tools.ant.Main -propertyfile "+Constants.OS_SPECIFIC_DOUBLE_QUOTE+propFile.getAbsolutePath()+Constants.OS_SPECIFIC_DOUBLE_QUOTE
        		+ " -buildfile "+srcDir+"/build.xml"
        		+ " "+getGoalFromParameter(goal);
        logger.debug(cmd);

        SystemIOResult cmdResult = null;
        try {
            cmdResult = SystemIO.exec(cmd, getSystemEnv(), workingDir, logger, getSearchString());
        }
        catch (SystemIOException ex) {
            logger.error("Error during Ant call", ex);
        }
        result.setResultCode(cmdResult.getResultCode());
        if (cmdResult.getStringSearchResult() != null) {
            if (cmdResult.getStringSearchResult().get(StreamStringSearchKey.ANT_TASK_NOT_FOUND) != null
                && cmdResult.getStringSearchResult().get(StreamStringSearchKey.ANT_TASK_NOT_FOUND)) {
                logger.info("Target not found, returning -2");
                result.setResultCode(TARGET_DOES_NOT_EXIST);
            }
            // recherche d'erreur dans la sortie standard du processus
            else if (cmdResult.getStringSearchResult().get(StreamStringSearchKey.ERROR) != null
                    && cmdResult.getStringSearchResult().get(StreamStringSearchKey.ERROR)) {
                    logger.error("Found error, return -1");
                    result.setResultCode(-1);
            }
        }
        return result;
    }

    /**
     * Get the strings to search map for ant result.
     * @return the strings to search map for ant result.
     */
    private Map<StreamStringSearchKey, String[]> getSearchString() {
        Map<StreamStringSearchKey, String[]> result = new HashMap<StreamStringSearchKey, String[]>();
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        result.put(StreamStringSearchKey.ANT_TASK_NOT_FOUND , new String[] {dynProp.getProperty(Constants.ANT_BUILD_NOT_FOUND_ERROR_KEY)});
        result.put(StreamStringSearchKey.ERROR , dynProp.getProperty(Constants.ANT_ERROR_KEY).split(","));
        return result;
    }

    /**
     * Get the java command according to the given properties.
     * @param prop the environment properties.
     * @return the java command according to the given properties.
     */
    private String getJavaCommand(Properties prop) {
    	String result = prop.getProperty(AntExecutor.ANT_JAVA_PATH_KEY);
    	if (result == null) {
    		result = "";
    	}
    	else {
    		result += "/bin/";
    	}
		result += "java";
    	return result;
    }

    /**
     * Get the system environment properties as a String[] of key=value.
     * @return the system environment properties as a String[] of key=value.
     */
    private String[] getSystemEnv() {
    	Map<String, String> envMap = System.getenv();
    	String[] result = new String[envMap.size()];
    	Set<String> keySet = envMap.keySet();
    	Iterator<String> iter = keySet.iterator();
    	String key = null;
    	int i = 0;
    	while (iter.hasNext()) {
    		key = iter.next();
    		result[i++] = key+"="+envMap.get(key);
    	}
    	return result;
    }

    /**
     * Save the properties to a file in the given source directory.
     * If temporary, then a temporary file is created like build-xxx.properties.
     * Else a build.properties is created.
     * @param srcDir the source directory to create the file in.
     * @param propList a properties list.
     * @param temporary true if the file file should be created temporay.
     * @return the created properties file.
     * @throws IOException Thrown if an IOException occurs during properties file creation.
     */
    private File saveJavaProperties(String srcDir, Properties propList, boolean temporary) throws IOException {
        File result = null;
        File destDir = null;
        if (srcDir != null) {
          destDir = new File(srcDir);
        }
        else {
          destDir = new File(getBuildDir());
          temporary = true;
        }
        if (!destDir.exists()) {
        	destDir.mkdirs();
        }
        if (temporary) {
        	result = File.createTempFile("build-", ".properties", destDir);
        }
        else {
        	result = new File(destDir, "build.properties");
        }
        propList.store(new FileOutputStream(result), null);
        return result;
    }

    /**
     * Return the goal string for the ant command.
     * @param goal the goal to execute.
     * @return the goal string to concat to the ant command (may be empty if the parameter is null).
     */
    private String getGoalFromParameter(String goal) {
        String result = "";
        if (goal != null) {
            result = goal;
        }
        return result;
    }

    /**
     * Get the ant build directory for to the given module.
     * @param ea the given module.
     * @return the ant build directory for to the given module.
     */
    private String getBuildDir(ElementBean ea) {
        String buildDir;
        File buildFile = new File(FileTools.concatPath(ea.getFullSourceDirPath(), "build.xml"));
        if (buildFile.exists()) {
            buildDir =  ea.getFullSourceDirPath();
        }
        else {
            buildDir = getBuildDir();
        }
        return buildDir;
    }

    /**
     * Retrieve the default Ant build directory i.e. [CAQS_HOME]/traitements/ant
     * @return the default Ant build directory.
     */
    public String getBuildDir() {
        String caqsHome = CaqsConfigUtil.getCaqsHome();
        return FileTools.concatPath(caqsHome, Constants.ANT_DEFAULT_BUILD_DIR);
    }

}

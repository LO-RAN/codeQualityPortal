package com.compuware.caqs.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.compuware.toolbox.exception.SystemIOException;
import org.apache.log4j.Logger;

/**
 * @author cwfr-fdubois
 *
 */
public class SystemIO {

    /**
     * Execute the given command using the system exec.
     * @param command the command to execute.
     * @param logger the logger to log into.
     * @param searchString the search string map.
     * @return the result of the system execution.
     * @throws com.compuware.toolbox.exception.SystemIOException an exception occured during the system execution.
     */
	public static SystemIOResult exec(String command, Logger logger, Map<StreamStringSearchKey, String[]> searchString) throws SystemIOException {
        return exec(command, null, null, logger, searchString);
    }

    /**
     * Execute the given command using the system exec.
     * @param command the command to execute.
     * @param systemEnv the system environment variable to use.
     * @param workingDir the working directory.
     * @param logger the logger to log into.
     * @param searchString the search string map.
     * @return the result of the system execution.
     * @throws com.compuware.toolbox.exception.SystemIOException an exception occured during the system execution.
     */
	public static SystemIOResult exec(String command, String[] systemEnv, File workingDir, Logger logger, Map<StreamStringSearchKey, String[]> searchString) throws SystemIOException {
		SystemIOResult result = new SystemIOResult();
		java.lang.Process p = null;
        try {
            p = Runtime.getRuntime().exec(command, systemEnv, workingDir);

            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), StreamType.ERROR, logger, searchString);
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), StreamType.OUTPUT, logger, searchString);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            try {
                if (p.waitFor() != 0) {
                	logger.info("Fin");
                	logger.info("Process end with exit value : " + p.exitValue());
                }
            }
            catch (InterruptedException e) {
            	throw new SystemIOException("Error executing process: ", e);
            }
            result.setResultCode(p.exitValue());
            result.addStringSearchResult(outputGobbler.getSearchStringResult());
            result.addStringSearchResult(errorGobbler.getSearchStringResult());
        }
        catch(IOException e){
        	throw new SystemIOException("Error executing process: ", e);
        }
        return result;
    }
}

package com.compuware.toolbox.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class CommandExec {

	public static int exec(String command, Logger logger) {
		return exec(command, null, null, logger);
    }

	public static int exec(String command, String[] env, File dir, Logger logger) {
		int result = 1;
		java.lang.Process p = null;
        try{
            p = Runtime.getRuntime().exec(command, env, dir);
            StreamGobbler out = new StreamGobbler(p.getInputStream(), StreamType.OUTPUT, logger);
            StreamGobbler err = new StreamGobbler(p.getErrorStream(), StreamType.ERROR, logger);
            out.start();
            err.start();

            try {
                if (p.waitFor() != 0) {
                	logger.info("Fin");
                	logger.info("Process end with exit value : " + p.exitValue());
                }
            }
            catch (InterruptedException e) {
            	logger.error("Error executing process: ", e);
            }
            result = p.exitValue();
        }
        catch(IOException e){
        	logger.error("Error executing process: ", e);
        }
        return result;
    }

}

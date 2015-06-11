/*
 * LoggerManager.java
 *
 * Created on 23 mars 2004, 16:30
 */

package com.compuware.toolbox.util.logging;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author  cwfr-fdubois
 */
public class LoggerManager {
    
    /** Creates a new instance of LoggerManager */
    public LoggerManager() {
    }
    
    public static void configure(String configFilePath) {
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            PropertyConfigurator.configure(configFilePath);
        }
    }
    
    public static Logger getLogger(String key) {
        Logger log = LogManager.getLogger(key);
        if (log == null) {
            log = Logger.getLogger(key);
        }
        return log;
    }
    
    public static void pushContexte(String ctx) {
        NDC.push(ctx);
    }
    
    public static String popContexte() {
        String result = NDC.pop();
        return result;
    }

    public static void removeContexte() {
        NDC.remove();
    }
}

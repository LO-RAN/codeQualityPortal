/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.business.util;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.PropertiesReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.RootLogger;
import org.openide.util.Exceptions;

/**
 *
 * @author cwfr-fdubois
 */
public class AnalysisFileLogger extends Hierarchy {

    public AnalysisFileLogger(Logger rootLogger) {
        super(rootLogger);
    }

    public static Logger createLogger(String baselineId) {
        AnalysisFileLogger loggerManager = new AnalysisFileLogger(new RootLogger(Level.DEBUG));
        String configDir = CaqsConfigUtil.getCaqsConfigDir();
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        PropertyConfigurator configurator = new PropertyConfigurator();
        Properties prop = PropertiesReader.getProperties(configDir + "/log4j-analysis.properties", Hierarchy.class);
        prop.setProperty("log4j.appender.staticanalysis.File", dynProp.getProperty(Constants.LOG4J_LOGGER_DIR_KEY) + "/analysis-" + baselineId + ".txt");
        configurator.doConfigure(prop, loggerManager);
        return loggerManager.getLogger("Analysis");
    }

    public static void shutdownHierarchy(Logger logger) {
        if (logger != null) {
            logger.getLoggerRepository().shutdown();
        }
    }

}

package com.compuware.caqs.domain.dataschemas.formatter;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.io.PropertiesReader;

public class FormatterFactory {

    //logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
	
    private Properties formatterConfig = null;
    
    private static FormatterFactory singleton = new FormatterFactory();
    
    public static FormatterFactory getInstance() {
    	return singleton;
    }
    
    private FormatterFactory() {
		formatterConfig = PropertiesReader.getProperties("/com/compuware/caqs/dataschemas/formatter/config.properties", this, true);
    }
    
	public Formatter getFormatter(String formatterKey) {
		Formatter result = null;
		String formatterClassName = formatterConfig.getProperty("formatter." + formatterKey);
		if (formatterClassName != null) {
			Class cls;
			try {
				cls = Class.forName(formatterClassName);
				if (cls != null) {
					result = (Formatter)cls.newInstance();
				}
			} catch (ClassNotFoundException e) {
				logger.error("Error creating a new formatter", e);
			} catch (InstantiationException e) {
				logger.error("Error creating a new formatter", e);
			} catch (IllegalAccessException e) {
				logger.error("Error creating a new formatter", e);
			}
		}
		return result;
	}
}

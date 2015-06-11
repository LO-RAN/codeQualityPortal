/**
 * 
 */
package com.compuware.caqs.business.parser.config;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.compuware.toolbox.io.PropertiesReader;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class AbstractConfigurationFactory {

	/**
	 * Get the configuration factory as specified in the parser.properties file.
	 * @return the configuration factory.
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static AbstractConfigurationFactory getFactory() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Properties prop = PropertiesReader.getProperties("/com/compuware/caqs/business/parser/parser.properties", AbstractConfigurationFactory.class);
		AbstractConfigurationFactory result = null;
		if (prop != null) {
			String className = prop.getProperty("configFactory");
			if (className != null) {
				result = (AbstractConfigurationFactory)Class.forName(className).newInstance();
			}
		}
		return result;
	}

	/**
	 * Create a parser configuration.
	 * @param sourceFileList the source files to parse.
	 * @param resultFile the result file for metrics.
	 * @param callsToFile the result file for calls.
	 * @param configFile the configuration file.
	 * @return the new parser configuration.
	 */
	public abstract IParserConfiguration createConfig(
			List<File> sourceFileList,
			File resultFile,
			File callsToFile,
			File configFile);
	
}

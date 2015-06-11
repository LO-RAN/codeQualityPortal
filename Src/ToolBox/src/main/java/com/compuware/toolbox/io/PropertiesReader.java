package com.compuware.toolbox.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 30 nov. 2005
 * Time: 10:05:28
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesReader {

    /** The logger used. */
	static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("Ihm");

    private static HashMap propertiesMap = new HashMap();

    protected PropertiesReader(){}

    /**
     * Get a Properties definition from a filePath.
     * @param fileName the properties file path.
     * @param obj the referer.
     * @param useCache to use a cache or not.
     * @return the Properties definition found.
     */
    public static Properties getProperties(String fileName, Object obj, boolean useCache){
        Properties result = null;
        if (useCache) {
            result = getProperties(fileName, obj);
        }
        else {
            result = readPropertiesFromFile(fileName, obj);
        }
        return result;
    }

    /**
     * Get a Properties definition from a filePath.
     * Read the file once, use cache after.
     * @param fileName the properties file path.
     * @param obj the referer.
     * @return the Properties definition found.
     */
    public static Properties getProperties(String fileName, Object obj){
        if (propertiesMap.get(fileName) == null) {
            synchronized(PropertiesReader.class) {
                if (propertiesMap.get(fileName) == null) {
                    Properties prop = readPropertiesFromFile(fileName, obj);
                    if (!prop.isEmpty()) {
                        propertiesMap.put(fileName, prop);
                    }
                }
            }
        }
        return (Properties)propertiesMap.get(fileName);
    }

    /**
     * Read a Properties file from a filePath.
     * @param fileName the properties file path.
     * @param obj the referer.
     * @return the Properties definition found.
     */
    private static Properties readPropertiesFromFile(String fileName, Object obj) {
        Properties prop = new Properties();
        try {
            File f = new File(fileName);
            if (f.exists()) {
                prop.load(new FileInputStream(f));
            }
            else {
                prop.load(obj.getClass().getResourceAsStream(fileName));
            }
        } catch (java.io.IOException io) {
            logger.error("### PropertiesReader, IO Exception, could not find the file "+fileName+" ###");
            logger.error("### PropertiesReader, IO Exception: " + io.getMessage());
        }
        return prop;
    }
    
    /**
     * Get a Properties definition from a filePath.
     * @param fileName the properties file path.
     * @param cls the referer class.
     * @param useCache to use a cache or not.
     * @return the Properties definition found.
     */
    public static Properties getProperties(String fileName, Class cls, boolean useCache){
        Properties result = null;
        if (useCache) {
            result = getProperties(fileName, cls);
        }
        else {
            result = readPropertiesFromFile(fileName, cls);
        }
        return result;
    }

    /**
     * Get a Properties definition from a filePath.
     * Read the file once, use cache after.
     * @param fileName the properties file path.
     * @param cls the referer class.
     * @return the Properties definition found.
     */
    public static Properties getProperties(String fileName, Class cls){
        if (propertiesMap.get(fileName) == null) {
            synchronized(PropertiesReader.class) {
                if (propertiesMap.get(fileName) == null) {
                    Properties prop = readPropertiesFromFile(fileName, cls);
                    if (!prop.isEmpty()) {
                        propertiesMap.put(fileName, prop);
                    }
                }
            }
        }
        return (Properties)propertiesMap.get(fileName);
    }

    /**
     * Read a Properties file from a filePath.
     * @param fileName the properties file path.
     * @param cls the referer class.
     * @return the Properties definition found.
     */
    private static Properties readPropertiesFromFile(String fileName, Class cls) {
        Properties prop = new Properties();
        try {
            File f = new File(fileName);
            if (f.exists()) {
                prop.load(new FileInputStream(f));
            }
            else {
                prop.load(cls.getResourceAsStream(fileName));
            }
        } catch (java.io.IOException io) {
            logger.error("### PropertiesReader, IO Exception, could not find the file "+fileName+" ###");
            logger.error("### PropertiesReader, IO Exception: " + io.getMessage());
        }
        return prop;
    }    
}

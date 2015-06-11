package com.compuware.caqs.util;

import java.util.Properties;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.JndiReader;
import com.compuware.toolbox.io.PropertiesReader;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 1 dec. 2005
 * Time: 16:57:01
 * To change this template use File | Settings | File Templates.
 */
public class CaqsConfigUtil {

    public static String getCaqsHome() {
        return new File((String)JndiReader.getValue(Constants.CAQS_HOME_KEY, Constants.CAQS_HOME_DEFAULT)).getAbsolutePath() ;
    }

    public static String getCaqsConfigDir() {
        StringBuffer result = new StringBuffer(getCaqsHome());
        result.append(Constants.CAQS_CONFIG_DIR_VALUE);
    	return result.toString();
    }

    public static Properties getCaqsConfigProperties(Object from) {
        return PropertiesReader.getProperties(Constants.CONFIG_FILE_PATH, from);
    }

    public static String getLocalizedCaqsProperty(Properties prop, String key) {
        String caqsHome = CaqsConfigUtil.getCaqsHome();
        String result = FileTools.concatPath(caqsHome, prop.getProperty(key));
        return result;
    }

    public static String getLocalizedCaqsFile(String relativeFilePath) {
        String caqsHome = CaqsConfigUtil.getCaqsHome();
        String result = FileTools.concatPath(caqsHome, relativeFilePath);
        return result;
    }

    public static Properties getCaqsGlobalConfigProperties() {
        String dynamicPropFileName = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_DYNAMIC_CONFIG_FILE_PATH);
        return PropertiesReader.getProperties(dynamicPropFileName, CaqsConfigUtil.class, false);
    }
}

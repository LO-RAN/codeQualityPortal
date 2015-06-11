/*
 * EA.java
 *
 * Created on 9 septembre 2002, 13:57
 *
 * @author  fxa
 */

package com.compuware.caqs.domain.dataschemas.analysis;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;

public class EA extends ElementBean {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2841894377653504385L;

	// d�claration du logger
    static protected Logger logger = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    
    private String targetDirectory = null;
    
    public void setTargetDirectory(String directory){
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String basePath=null;
        try {
            basePath = new File(dynProp.getProperty(Constants.SRC_BASE_PATH)).getCanonicalPath();
        } catch (IOException ex) {
         		logger.error("Can't get full path : "+ex.getMessage());
        }
    	this.targetDirectory = directory;
        if (basePath != null) {
        	targetDirectory = FileTools.concatPath(basePath, targetDirectory);
        }
        File target = new File(this.targetDirectory);
        
        if(!target.exists()){
            target.mkdir();
        }        
    }
    
    public String getTargetDirectory(){
        return this.suppressEndingBackOrFowardSlash(this.targetDirectory);
    }
    
    public String getSourceDir(){
        return this.suppressEndingBackOrFowardSlash(super.getSourceDir());
    }
    
    private String suppressEndingBackOrFowardSlash(String aString){
        String returnValue = aString;
        if(aString != null && (aString.endsWith("\\") || aString.endsWith("/"))) {
            returnValue = aString.substring(0,aString.length()-1);
        }
        return returnValue;
    }
}

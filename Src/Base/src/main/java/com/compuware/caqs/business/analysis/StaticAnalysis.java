/*
 * StaticAnalisys.java
 * @author  fxa
 * Created on 9 septembre 2002, 12:27
 */

package com.compuware.caqs.business.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.analysis.exception.AnalysisPreConditionException;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.util.AnalysisFileLogger;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import com.compuware.toolbox.util.xml.ParserUtil;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public abstract class StaticAnalysis {
    //logger
	//protected static final Logger logger = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
	protected Logger logger = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    //project information
    protected String mCcBaseLineName="";

    protected StaticAnalysisConfig config = null;

    //directories
    protected String mTargetDirectory;
    protected String mXmlTempDirectory;
    protected String mWebDataAbsolutePath;

    protected String errorString = "";

    /** Creates a new instance of StaticAnalisys */
    public void init(StaticAnalysisConfig pConfig) {
        this.config = pConfig;
        logger = AnalysisFileLogger.createLogger(this.config.getBaselineId());
        logger.debug("Starting a new StaticAnalysis for tool: " + this.config.getToolId());
        logger.debug("Parameters are :");
        logger.debug("  - Project id = " + this.config.getProjectId());
        logger.debug("  - Baseline id = " + this.config.getBaselineId());
        logger.debug("  - Analysis = " +this.config.isAnalyse());
        logger.debug("  - Load data = " + this.config.isLoad());
        logger.debug("  - Master = " + this.config.isMasterTool());
    }

    //override foreach tool for specifics initialisations.
    protected abstract void initSpecific(Properties dbProps) throws AnalysisPreConditionException;
    //override for the analysis and report creation
    protected abstract void toolAnalysis(EA curEA) throws AnalysisException;
    //override for analysis check
    protected abstract AnalysisResult analysisCheck(EA curEA);
    //override for loading the data
    protected abstract void loadData(EA curEA) throws LoaderException;

    public final AnalysisResult execute(String[] eaArray, ResourceBundle resources){
    	logger.info("Starting execution of static analysis");

        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementDao = daoFactory.getElementDao();
        AnalysisResult result = new AnalysisResult();

        try {
	        logger.info("  Step 1/7 initialisation");
	        init();

            logger.info("  Step 2/7 getting project EA");
            //get project ea
            Collection<EA> eaColl = elementDao.retrieveProjectSubElements(this.config.getProjectId(), eaArray, DataFileType.EA);

            logger.info("  Step 3/7 for each project EA" + eaColl.size());
            if (eaColl.size() > 0){
                result.setSuccess(true);
                result.setMessage("");

                Iterator<EA> eaIter = eaColl.iterator();
                while (eaIter.hasNext() && result.isSuccess()) {
                    EA curEA = eaIter.next();
                	logger.info("  Step 4/7 extracting source for EA");
                    //analysis and / or load ?
                    if(this.config.isAnalyse()){
                    	logger.info("  Step 5/7 processing EA");
                        //Analysis
                        toolAnalysis(curEA);
                        result = analysisCheck(curEA);
                        if (result != null && !result.isSuccess()) {
                        	break;
                        }
                    }
                    //load data
                    if(this.config.isLoad()){
                    	logger.info("  Step 6/7 loading EA Data");
                        loadData(curEA);
                        logger.info("  Step 7/7 Updating EA MAJ Date");
                        elementDao.updateElementDate(curEA.getId());
                    }
                }
            }
            else{
                throw new AnalysisException("No EA found for this project");
            }
        }
        catch (CaqsException e) {
        	result.setSuccess(false);
        	result.setMessage(e.getMessage());
        }
        finally {
	        logger.info("End of static Analysis with status ok = " + result.isSuccess());
	        if(result.isSuccess()) {
	            //no error
	            result.addParam("baselineName", this.mCcBaseLineName);
	        } else {
                    logger.error("Error during static analysis : "+result.getMessage());
                }
            AnalysisFileLogger.shutdownHierarchy(logger);
        }
        return result;
    }

    protected void init() throws AnalysisPreConditionException {
    	logger.debug("Initializing a new StaticAnalysis");
        boolean ok = true;

        Properties dbProps = CaqsConfigUtil.getCaqsConfigProperties(this);

        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();

        //directories
        this.mTargetDirectory = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.TARGET_DIRECTORY_KEY);
        this.mXmlTempDirectory = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.XML_TEMP_DIRECTORY_KEY);
        this.mWebDataAbsolutePath = dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY);

        if (mTargetDirectory == null || mXmlTempDirectory == null || mWebDataAbsolutePath == null) {
            logger.error("Can't read the properties file. Make sure conf.txt is in the CLASSPATH");
            throw new AnalysisPreConditionException("Can't read the properties file. Make sure conf.txt is in the CLASSPATH");
        }
        //calls overrided specific initialisations.
        initSpecific(dbProps);

        logger.debug("End of initialisation StaticAnalysis status ok = "+ ok);
    }

    protected boolean checkForExistingNonEmptyFile(String filePath) {
    	boolean result = false;
    	if (filePath != null) {
    		File f = new File(filePath);
    		if (f.exists()) {
    			try {
    				FileReader freader = new FileReader(f);
    				BufferedReader breader = new BufferedReader(freader);
    				int cnt = 0;
    				while ((breader.readLine() != null) && (cnt < 2)) {
    					cnt++;
    				}
    				if (cnt < 2) {
    					result = true;
    				}
    			}
    			catch (FileNotFoundException e) {
    				logger.error("File not found " + filePath);
    			}
    			catch (IOException e) {
    				logger.error("Error reading " + filePath, e);
    			}
    		}
    	}
    	if (!result) {
    		logger.debug("Not found or empty file: " + filePath);
    	}
    	return result;
    }

    protected boolean checkForExistingNonEmptyBinaryFile(String filePath) {
    	boolean result = false;
    	if (filePath != null) {
    		File f = new File(filePath);
    		result = f.exists() && (f.length() > 0);
    	}
    	if (!result) {
    		logger.debug("Not found or empty file: " + filePath);
    	}
    	return result;
    }

    protected boolean checkForWellFormedXmlFile(String filePath) {
    	boolean result = false;
    	if (filePath != null) {
    		File f = new File(filePath);
    		if (f.exists()) {
    	    	Document xmlDocument = ParserUtil.parseDefinition(f);
    	        if (xmlDocument != null) {
    	        	result = true;
    	        }
    		}
    	}
    	if (!result) {
    		logger.debug("Non well formed file: " + filePath);
    	}
    	return result;
    }

}

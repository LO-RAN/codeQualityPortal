/*
 * SourceManager.java
 *
 * Created on 14 juin 2004, 10:36
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import com.compuware.caqs.business.util.AntExecutor;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.AnalysisConfig;
import com.compuware.caqs.domain.dataschemas.analysis.SystemCallResult;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.apache.log4j.Logger;

/**
 * Manage the source code extraction.
 * @author  cwfr-fdubois
 */
public class SourceManager {

    /** The default build goal key. */
    public static final String DEFAULT_ACTION = null;
    /** The build goal key. */
    public static final String BUILD_ACTION = "build";

    Logger logger = null;

    /** Creates a new instance of SourceManager */
    public SourceManager() {
    	this.logger = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    }

    /** Creates a new instance of SourceManager */
    public SourceManager(Logger logger) {
    	this.logger = logger;
    }

    /** Manage source code for a given program.
     * @param idEa The source to manage program ID.
     * @param target the ant target.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult manageEa(String idEa, String target) throws IOException, CaqsException {
    	DaoFactory daoFactory = DaoFactory.getInstance(); 
        ProjectDao projectDao = daoFactory.getProjectDao();
    	ProjectBean projectBean = projectDao.retrieveModuleProject(idEa);
    	return manageEa(idEa, null, projectBean, target, new Properties(), new File("."));
    }

    /** Manage source code for a given program.
     * @param idEa The source to manage program ID.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult manageEa(String idEa, String baselineId, ProjectBean projectBean, String goal, Properties parameters) throws IOException {
    	return manageEa(idEa, baselineId, projectBean, goal, parameters, new File("."));
    }

    /** Manage source code for a given program.
     * @param idEa The source to manage program ID.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult manageEa(String idEa, String baselineId, ProjectBean projectBean, String goal, Properties parameters, File workingDir) throws IOException {
    	SystemCallResult result = null;
    	DaoFactory daoFactory = DaoFactory.getInstance(); 
        ElementDao elementFacade = daoFactory.getElementDao(); 
        // Retrieve all EA's data from database.
        ElementBean ea = elementFacade.retrieveAllElementDataById(idEa);
        if (ea != null) {
            AntExecutor command = new AntExecutor(this.logger);
            // Extract source code from EA's source directory.
            result = command.processAntScript(ea, goal, getProperties(ea, baselineId, projectBean, goal, parameters), workingDir);
        }
        // Return the execution result code.
        return result;
    }

    private String getJavaPath(String idDialecte) {
    	String result = null;
    	String jdkVersion = null;
    	if (idDialecte.startsWith("java_jdk")) {
    		jdkVersion = idDialecte.substring(8);
    	}
    	if (jdkVersion != null) {
            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
            result = dynProp.getProperty("java.path." + jdkVersion);
    	}
    	return result;
    }

    private Properties getProperties(ElementBean ea, String baselineId, ProjectBean projectBean, String goal, Properties parameters) {
        Properties prop = new Properties();
        if (baselineId != null) {
        	prop.put("baselineId", baselineId);
        }
        if (ea != null) {
	        prop.put("id_elt", ea.getId());
	        prop.put("lib_elt", ea.getLib());
	        prop.put("SRC_DIR", ea.getFullSourceDirPath());
	        if (ea.getDialecte() != null) {
	        	prop.put("DIALECTE", ea.getDialecte().getId());
	        	if (goal.equalsIgnoreCase("exportDesignViolation")
                        && ea.getDialecte().getLangage() != null
                        && ea.getDialecte().getLangage().getId() != null
                        && ea.getDialecte().getLangage().getId().equalsIgnoreCase("java")) {
	        		String javaPath = getJavaPath(ea.getDialecte().getId());
	        		if (javaPath != null) {
	        			prop.put("JAVA_PATH", javaPath);
	        		}
	        	}
	        }
	        if (ea.getInfo1() != null) {
	        	prop.put("INFO1", ea.getInfo1());
	        }
	        if (ea.getInfo2() != null) {
	        	prop.put("INFO2", ea.getInfo2());
	        }
	        if (ea.getScmRepository() != null) {
	        	prop.put("SCM_REPOSITORY", ea.getScmRepository());
	        }
	        if (ea.getScmModule() != null) {
	        	prop.put("SCM_MODULE", ea.getScmModule());
	        }

            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
	        if (goal != null && dynProp.getProperty(goal+".dest.dir") != null) {
	            prop.put("DEST_DIR", ElementBean.getHtmlSrcDir(baselineId, ea, projectBean, new File(dynProp.getProperty(goal+".dest.dir")).getAbsolutePath()));
	        }
        }
        if (projectBean != null) {
            prop.put("id_pro", projectBean.getId());
            prop.put("lib_pro", projectBean.getLib());
        }
        if (parameters != null) {
        	prop.putAll(parameters);
        }
        return prop;
    }

    /** Manage source code for a given project.
     * @param projectId The source to manage project ID.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult manage(AnalysisConfig config, String goal, boolean prjTarget, Properties parameters) throws IOException {
    	SystemCallResult result;
    	if (prjTarget) {
    		result = managePrj(config.getProjectId(), config.getBaselineId(), goal, parameters);
    	}
    	else {
    		result = manage(config, goal, parameters);
    	}
    	return result;
    }

    /** Manage source code for a given project.
     * @param projectId The source to manage project ID.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult managePrj(String projectId, String baselineId, String goal, Properties parameters) throws IOException {
    	SystemCallResult result = null;
    	// Retrieve the project definition
        DaoFactory daoFactory = DaoFactory.getInstance();
        ProjectDao projectFacade = daoFactory.getProjectDao();
        ProjectBean projectBean = projectFacade.retrieveProjectById(projectId);
        if (projectBean != null) {
            AntExecutor command = new AntExecutor(this.logger);
            // Extract source code from EA's source directory.
            result = command.processAntScript(goal, getProperties(null, baselineId, projectBean, goal, parameters));
        }
        return result;
    }

    /** Manage source code for a given project.
     * @param projectId The source to manage project ID.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult manage(String projectId, String baselineId, String goal, Properties parameters) throws IOException {
    	SystemCallResult result = new SystemCallResult();
        // Retrieve the project definition
        DaoFactory daoFactory = DaoFactory.getInstance();
        ProjectDao projectFacade = daoFactory.getProjectDao();
        ProjectBean projectBean = projectFacade.retrieveProjectById(projectId);
        BaselineDao baselineDao = daoFactory.getBaselineDao();
        Timestamp dmajBline = baselineDao.getBaselineDmaj(baselineId);
        // Retrieve all EA of the project.
    	  ElementDao elementFacade = daoFactory.getElementDao();
        Collection<ElementBean> eaColl = elementFacade.retrieveElementByType(projectId, ElementType.EA, dmajBline);
        Iterator<ElementBean> i = eaColl.iterator();
        // For each EA
        while (i.hasNext() && result != null && result.getResultCode() == 0) {
            ElementBean ea = i.next();
            // Extract EA source code.
            result = manageEa(ea.getId(), baselineId, projectBean, goal, parameters);
        }
        return result;
    }

    /** Manage source code for a given project.
     * @param projectId The source to manage project ID.
     * @throws IOException Thrown if an IOException occurs during the script execution.
     * @return the script execution return code value: default is 0.
     */
    public SystemCallResult manage(AnalysisConfig config, String goal, Properties parameters) throws IOException {
    	SystemCallResult result = null;
if (
           config.getModuleArray() != null
				&& config.getModuleArray().length > 0
        && ( 
                config.getModuleOptionArray() == null 
            || (   config.getModuleOptionArray() != null
				        && config.getModuleOptionArray().length == config.getModuleArray().length
               )
           )
   ) {
		        // Retrieve the project definition
		        DaoFactory daoFactory = DaoFactory.getInstance();
		        ProjectDao projectFacade = daoFactory.getProjectDao();
		        ProjectBean projectBean = projectFacade.retrieveProjectById(config.getProjectId());
		        String[] moduleArray = config.getModuleArray();
		        String[] moduleOptionArray = config.getModuleOptionArray();
		        // For each EA
		        for (int i = 0; i < moduleArray.length; i++) {
		        	parameters.remove("EA_OPTION");
                    if(config.getModuleOptionArray() != null){
		        	parameters.put("EA_OPTION", moduleOptionArray[i]);
                    }
		            result = manageEa(moduleArray[i], config.getBaselineId(), projectBean, goal, parameters);
		        }
    		}
    		else {
    			result = manage(config.getProjectId(), config.getBaselineId(), goal, parameters);
    		}
    	
        return result;
    }

}

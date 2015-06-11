/*
 * StaticAnalysisDevPartnerRuleByRule.java
 *
 * Created on July 13, 2004, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlCopyPasteFileLoader;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.StringUtils;

public class StaticAnalysisOptimalAdvisor extends StaticAnalysis{
    protected String optimalAdvisorHome = null;
    
    /** Creates a new instance of StaticAnalysisOptimalAdvisor */
    public StaticAnalysisOptimalAdvisor() {
        super();
        logger.info("Tool is OptimalAdvisor");
    }
    
    protected void initSpecific(Properties dbProps){
        String caqsHome = CaqsConfigUtil.getCaqsHome();
        this.optimalAdvisorHome = FileTools.concatPath(caqsHome, Constants.OPTIMAL_ADVISOR_HOME_KEY);
    }

    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("OptimalAdvisor Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            // On ignore l'analyse...
        }
        else{
        	DaoFactory daoFactory = DaoFactory.getInstance();
        	ProjectDao projectDao = daoFactory.getProjectDao();
	        ProjectBean projectBean = projectDao.retrieveProjectById(this.config.getProjectId());
        	
        	SourceManager srcManager = new SourceManager(this.logger);
        	try {
                logger.info("Start OptimalAdvisor Analysis");
        		srcManager.manageEa(curEA.getId(), this.config.getBaselineId(), projectBean, "exportDesignViolation", null);
        		srcManager.manageEa(curEA.getId(), this.config.getBaselineId(), projectBean, "exportLinks", null);

                logger.info("Consolidate OptimalAdvisor Results");
                AnalyseOptimalAdvisor aoa = new AnalyseOptimalAdvisor(this.logger);
                aoa.conlidate(curEA.getTargetDirectory(), curEA.getDialecte().getId());
        	}
        	catch (IOException e) {
        		throw new AnalysisException("Error during OptimalAdvisor analysis", e);
        	}
        }
    }

	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
	    	result.setSuccess(checkForWellFormedXmlFile(curEA.getTargetDirectory() + ADPXMLFILENAME)
	    		&& checkForWellFormedXmlFile(curEA.getTargetDirectory() + AnalyseOptimalAdvisor.XMLFILENAME));
        }
    	return result;
    }
    
    private static final String ADPXMLFILENAME = File.separator + "optimaladvisorreports" + File.separator + "traited-xmlmetrics.xml";
    private static final String CALLS_TO_FILE = "/optimaladvisorreports/dependency.csv";
    private static final String CPD_FILE = "/optimaladvisorreports/cpd.xml";
    
    @Override
    protected void loadData(EA curEA) throws LoaderException {
        //load Data
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("OptimalAdvisor Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            // On ignore le chargement...
        }
        else{
            logger.info("Loading OptilamAdvisor result files:");
            DataFile pkgFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + ADPXMLFILENAME, true);
            DataFile clsFile = new DataFile(DataFileType.CLS, curEA.getTargetDirectory() + AnalyseOptimalAdvisor.XMLFILENAME, true);
	
	        ProjectBean projectBean = new ProjectBean();
	        projectBean.setId(this.config.getProjectId());
	        
	        BaselineBean baselineBean = new BaselineBean();
	        baselineBean.setId(this.config.getBaselineId());
	        
	        ElementBean eaElt = new ElementBean();
	    	eaElt.setId(curEA.getId());
	    	eaElt.setSourceDir(curEA.getTargetDirectory());
	        
	        FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);

            logger.info("    - Package file");
        	loader.load(pkgFile);
            logger.info("    - Class file");
        	loader.load(clsFile);
	        File callsToFile = new File(curEA.getTargetDirectory() + CALLS_TO_FILE);
	        if (callsToFile.exists()) {
                logger.info("    - Calls to file");
	        	curEA.setProject(projectBean);
	        	new ArchitectureLoader(curEA, this.config.getBaselineId(), curEA.getTargetDirectory() + CALLS_TO_FILE);
	        }
	        File cpdToFile = new File(curEA.getTargetDirectory() + CPD_FILE);
	        if (cpdToFile.exists()) {
                logger.info("    - Copy/Paste file");
	        	XmlCopyPasteFileLoader copyPasteloader = new XmlCopyPasteFileLoader(eaElt, projectBean, baselineBean);
	        	copyPasteloader.setBaseDir(curEA.getTargetDirectory() + "/src/");
	        	DataFile cpdFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + CPD_FILE, true);
	        	copyPasteloader.load(cpdFile);
	        }
        }
        //END load Data
    }

}

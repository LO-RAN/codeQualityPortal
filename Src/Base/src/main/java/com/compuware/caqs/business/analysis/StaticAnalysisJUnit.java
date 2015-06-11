/*
 * StaticAnalysisJUnit.java
 *
 * Created on July 13, 2004, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

/**
 *
 * @author  cwfr-fdubois
 */
public class StaticAnalysisJUnit extends StaticAnalysis {

    private static final String JUNIT_XMLFILENAME = File.separator + "junitreports" + File.separator + "report.xml";
    private static final String COV_XMLFILENAME = File.separator + "junitreports" + File.separator + "cov-report.xml";
	
    /** Creates a new instance of StaticAnalysisJUnit */
    public StaticAnalysisJUnit() {
        super() ;
        logger.info("Tool is JUnit");
    }
    
    @Override
    protected void initSpecific(Properties dbProps){
    }
    
    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        // Done using continuous build process (Maven for example.
    }

	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")) {
            logger.info("JUnit can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;

        }
		return result;
	}

    @Override
    protected AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
    	if (isAnalysisPossible(curEA)) {
        	File fjunit = new File(curEA.getTargetDirectory() + JUNIT_XMLFILENAME);
        	File fcov = new File(curEA.getTargetDirectory() + COV_XMLFILENAME);
        	
        	if (fjunit.exists()) {
        		result.setSuccess(checkForWellFormedXmlFile(curEA.getTargetDirectory() + JUNIT_XMLFILENAME));
        	}
        	if (fcov.exists()) {
        		result.setSuccess(result.isSuccess() && checkForWellFormedXmlFile(curEA.getTargetDirectory() + COV_XMLFILENAME));
        	}
        }
    	return result;
    }

    @Override
    protected void loadData(EA curEA) throws LoaderException {
        //load Data
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("JUnit Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            // On ignore le chargement...
        }
        else {
        	File fjunit = new File(curEA.getTargetDirectory() + JUNIT_XMLFILENAME);
        	File fcov = new File(curEA.getTargetDirectory() + COV_XMLFILENAME);

        	if (fjunit.exists() || fcov.exists()) {
		        ProjectBean projectBean = new ProjectBean();
		        projectBean.setId(this.config.getProjectId());
		        
		        BaselineBean baselineBean = new BaselineBean();
		        baselineBean.setId(this.config.getBaselineId());
		        
		        ElementBean eaElt = new ElementBean();
		    	eaElt.setId(curEA.getId());
		    	eaElt.setSourceDir(curEA.getTargetDirectory());
		        
		        FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);

	        	if (fcov.exists()) {
		            DataFile covFile = new DataFile(DataFileType.MET, curEA.getTargetDirectory() + COV_XMLFILENAME, true);
			    	loader.load(covFile);
	        	}

	        	if (fjunit.exists()) {
		        	DataFile junitFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + JUNIT_XMLFILENAME, true);
		        	loader.setMainTool(true);
		        	loader.load(junitFile);
	        	}
        	}
        }
        //END load Data
    }
    
}

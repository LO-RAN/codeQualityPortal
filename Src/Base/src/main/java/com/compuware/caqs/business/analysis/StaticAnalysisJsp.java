/*
 * StaticAnalysisJsp.java
 *
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.Properties;

import com.compuware.caqs.business.load.db.ArchitectureLoader;
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

public class StaticAnalysisJsp extends StaticAnalysisAntGeneric {
    
    /** Creates a new instance of StaticAnalysisOptimalAdvisor */
    public StaticAnalysisJsp() {
        super();
        logger.info("Tool is JspAnalyzer");
    }

    @Override
    protected void initSpecific(Properties dbProps){
    }
    
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysisAntGeneric#getXmlFileReportPath()
	 */
	@Override
	protected String getXmlFileReportPath() {
		// not used... overwriten...
    	return null;
	}
    
	@Override
	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
        if( !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")) {
            logger.info("JspAnalyzer can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "jspAnalysis";
	}
	
	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        if(isAnalysisPossible(curEA)) {
        	// TODO
        }
    	return result;
    }
    
    private static final String RESULT_XML_FILE = File.separator + "jspreports" + File.separator + "report.xml";
    private static final String CALLS_TO_FILE = File.separator + "jspreports" + File.separator + "report.csv";
    
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysis#loadData(com.compuware.caqs.business.load.EA)
	 */
	@Override
    protected void loadData(EA curEA) throws LoaderException {
        boolean ok = true;
        //where are the reports to load ?
        //load Data
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("OptimalAdvisor Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            // On ignore le chargement...
            ok = true;
        }
        else{
        	File jspXmlFile = new File(curEA.getTargetDirectory() + RESULT_XML_FILE);
        	if (jspXmlFile.exists()) {
	            DataFile jspLoadFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + RESULT_XML_FILE, true);
		
		        ProjectBean projectBean = new ProjectBean();
		        projectBean.setId(this.config.getProjectId());
		        
		        BaselineBean baselineBean = new BaselineBean();
		        baselineBean.setId(this.config.getBaselineId());
		        
		        ElementBean eaElt = new ElementBean();
		    	eaElt.setId(curEA.getId());
		    	eaElt.setSourceDir(curEA.getTargetDirectory());
		        
		        FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);

		        loader.setMainTool(true);
	        	loader.load(jspLoadFile);
		        File callsToFile = new File(curEA.getTargetDirectory() + CALLS_TO_FILE);
		        if (callsToFile.exists()) {
		        	curEA.setProject(projectBean);
		        	new ArchitectureLoader(curEA, this.config.getBaselineId(), curEA.getTargetDirectory() + CALLS_TO_FILE);
		        }
        	}
        }
        //END load Data
    }

    @Override
    protected void postToolAnalysis() {
    }
}

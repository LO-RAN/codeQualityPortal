/*
 * StaticAnalysisPMcCabe.java
 *
 * Created on July 31, 2007, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.Properties;

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

public class StaticAnalysisPMcCabe extends StaticAnalysisAntGeneric {
    
    private static final String FILE_XMLFILENAME = File.separator + "pmccabereports" + File.separator + "fileReport.xml";
    private static final String FCT_XMLFILENAME = File.separator + "pmccabereports" + File.separator + "fctReport.xml";

    /** Creates a new instance of StaticAnalysisPMcCabe */
    public StaticAnalysisPMcCabe() {
        super();
        logger.info("Tool is PMcCabe");
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
    protected void initSpecific(Properties dbProps){
    }
    
	@Override
	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
        if( !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cpp") 
    		&& !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "ansic")
    		&& !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "proc") ) {
            logger.info("PMcCabe Analysis can only be used on C/C++/Pro*C; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "pmccabeAnalysis";
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysis#loadData(com.compuware.caqs.business.load.EA)
	 */
	@Override
	protected void loadData(EA curEA) throws LoaderException {
        if (isAnalysisPossible(curEA)) {
            DataFile fileFile = new DataFile(DataFileType.CLS, curEA.getTargetDirectory() + FILE_XMLFILENAME, true);
            DataFile fctFile = new DataFile(DataFileType.MET, curEA.getTargetDirectory() + FCT_XMLFILENAME, true);
        	
	        ProjectBean projectBean = new ProjectBean();
	        projectBean.setId(this.config.getProjectId());
	        
	        BaselineBean baselineBean = new BaselineBean();
	        baselineBean.setId(this.config.getBaselineId());
	        
	        ElementBean eaElt = new ElementBean();
	    	eaElt.setId(curEA.getId());
	        
	        FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
	        loader.setMainTool(this.config.isMasterTool());

        	loader.load(fileFile);
        	loader.load(fctFile);
        }
    }

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysis#analysisCheck(com.compuware.caqs.business.load.EA)
	 */
	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        if (isAnalysisPossible(curEA)) {
            result.setSuccess(checkForWellFormedXmlFile(curEA.getTargetDirectory() + FILE_XMLFILENAME)
                && checkForWellFormedXmlFile(curEA.getTargetDirectory() + FCT_XMLFILENAME));
        }
		return result;
	}	

    @Override
    protected void postToolAnalysis() {
    }
}

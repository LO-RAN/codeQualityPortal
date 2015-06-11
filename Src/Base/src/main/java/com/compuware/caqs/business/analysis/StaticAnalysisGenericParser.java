package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.Properties;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;

public class StaticAnalysisGenericParser extends StaticAnalysisAntGeneric {
	private static final String TOOLS_OUTPUT_PATH = File.separator + "genericparserreports";
    
    /** Creates a new instance of StaticAnalysisDevEntreprise */
	public StaticAnalysisGenericParser() {
		super();
        logger.info("Tool is GenericParser");
	}
	
	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
    	if (isAnalysisPossible(curEA)) {
            String resultFile = this.getResultFileName(curEA);
    		File xml = new File(resultFile);
    		if(!xml.exists()) {
    			result.setSuccess(false);
    		}
    	}
    	return result;
	}

	@Override
	protected void initSpecific(Properties dbProps) {
    }

    private String getResultFileName(EA curEA) {
        return curEA.getTargetDirectory() + TOOLS_OUTPUT_PATH + File.separator +
                    curEA.getDialecte().getId()+"Result.xml";
    }

	@Override
	protected void loadData(EA curEA) throws LoaderException {
        if (isAnalysisPossible(curEA)) {
            DataFile clsFile = new DataFile(DataFileType.ALL,
                    this.getResultFileName(curEA), true);
        	
	        ProjectBean projectBean = new ProjectBean();
	        projectBean.setId(this.config.getProjectId());
	        
	        BaselineBean baselineBean = new BaselineBean();
	        baselineBean.setId(this.config.getBaselineId());
	        
	        ElementBean eaElt = new ElementBean();
	    	eaElt.setId(curEA.getId());
	        
	        FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
	        loader.setMainTool(this.config.isMasterTool());

	        loader.load(clsFile);
        }
	}

	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;

        File targetDir = new File(FileTools.concatPath(curEA.getFullSourceDirPath(), "/src/"));
        if(targetDir==null || !targetDir.exists() || targetDir.isFile()) {
        	result = false;
        }
        if (result) {
            String languagePath = FileTools.concatPath(CaqsConfigUtil.getCaqsHome(), Constants.GENERIC_PARSER_LANGAGE_PATH_KEY);
            if (languagePath != null && curEA.getDialecte() != null && curEA.getDialecte().getLangage() != null) {
                File languageFile = new File (languagePath, "" + curEA.getDialecte().getLangage().getId().toLowerCase() + ".xml");
                result = languageFile.exists();
            }
            else {
                result = false;
            }
        }
		return result;
	}
	
	protected String getXmlFileReportPath() {
    	return null;
	}

	@Override
	protected String getAntTarget() {
		return "genericparserAnalysis";
	}


    @Override
    protected void postToolAnalysis() {
    }
}

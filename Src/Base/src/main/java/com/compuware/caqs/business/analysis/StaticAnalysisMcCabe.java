/*
 * StaticAnalysisMcCabe.java
 * @author  cwfr-fxalbouy
 * Created on  8 novembre 2002, 10:43
 */
package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.DbLoader3;
import com.compuware.caqs.business.load.db.LoaderConfig;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.RealLinkCreator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.SystemIO;
import com.compuware.toolbox.exception.SystemIOException;
import com.compuware.toolbox.util.StringUtils;

public class StaticAnalysisMcCabe extends StaticAnalysis {
	
	public static final String TOOL_ID = "mccabe";
	
    //mccabe
    protected String mccabeScriptPath;
    protected String mccabeScriptConfigurationFilePath;
    protected String callsToScript;

    /** Creates a new instance of StaticAnalysisMcCabe */
    public StaticAnalysisMcCabe() {
        super();
        logger.info("Tool is McCabe");
    }

    @Override
    protected void initSpecific(Properties dbProps){
        //mccabe
        this.mccabeScriptPath = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.MCCABE_SCRIPT_PATH_KEY);
        this.mccabeScriptConfigurationFilePath = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.MCCABE_SCRIPT_CONFIG_PATH_KEY);
        this.callsToScript = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.CALLS_TO_SCRIPT_PATH_KEY);
    }

    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        String cmd="";

        if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            cmd="perl "+ this.mccabeScriptPath +" " + this.mccabeScriptConfigurationFilePath + " " + curEA.getTargetDirectory() + " " +  curEA.getDialecte() + " "+  curEA.getId() + " " + curEA.getTargetDirectory()+File.separator+"src;"+ curEA.getTargetDirectory()+File.separator+"jsp" + " " + curEA.getTargetDirectory()+File.separator+"bin";
        }
        else if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "vb")){
            cmd="perl "+ this.mccabeScriptPath +" " + this.mccabeScriptConfigurationFilePath + " " + curEA.getTargetDirectory() + " " + curEA.getDialecte() + " "+  curEA.getId() + " " + curEA.getTargetDirectory()+File.separator+"src";
        }
        else if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "csharp")){
            cmd="perl "+ this.mccabeScriptPath +" " + this.mccabeScriptConfigurationFilePath + " " + curEA.getTargetDirectory() + " " + curEA.getDialecte() + " "+  curEA.getId() + " " + curEA.getTargetDirectory()+File.separator+"src";
        }
        // too bad we have to hard code these things :-(
        else if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cpp")){
            cmd="perl "+ this.mccabeScriptPath +" " + this.mccabeScriptConfigurationFilePath + " " + curEA.getTargetDirectory() + " " + curEA.getDialecte() + " "+  curEA.getId() + " " + curEA.getTargetDirectory()+File.separator+"src";
        }
        else if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "ansic")){
            cmd="perl "+ this.mccabeScriptPath +" " + this.mccabeScriptConfigurationFilePath + " " + curEA.getTargetDirectory() + " " + curEA.getDialecte() + " "+  curEA.getId() + " " + curEA.getTargetDirectory()+File.separator+"src";
        }
        else if(StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cobol")){
            cmd="perl "+ this.mccabeScriptPath +" " + this.mccabeScriptConfigurationFilePath + " " + curEA.getTargetDirectory() + " " + curEA.getDialecte() + " "+  curEA.getId() + " " + curEA.getTargetDirectory()+File.separator+"src" + " " + curEA.getLibraries();
        }
        logger.info("Starting McCabe Analysis : " + cmd);
        cmd+="\n";

        try {
			SystemIO.exec(cmd, this.logger, null);
		}
        catch (SystemIOException e) {
        	throw new AnalysisException("Error during McCabe analysis", e);
		}

        //End McCabe Analysis
    }

	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(checkForExistingNonEmptyFile(curEA.getTargetDirectory() + File.separator + MCCABE_REPORTS + File.separator + "ALLCLASSESMETRICS.CSV"));
    	if (result.isSuccess()) {
    		result.setSuccess(checkForExistingNonEmptyFile(curEA.getTargetDirectory() + File.separator + MCCABE_REPORTS + File.separator + "traitedALLMODULESMETRICS.CSV"));
    	}
    	return result;
    }
    
    @Override
    protected void loadData(EA curEA) throws LoaderException {
        boolean ok = true;
        //load Data
        Collection<DataFile> files = new ArrayList<DataFile>();
        files.add(new DataFile(DataFileType.EA,curEA.getTargetDirectory()+File.separator+MCCABE_REPORTS+File.separator+"ALLPROGRAMMETRICS.CSV", false));
        files.add(new DataFile(DataFileType.CLS,curEA.getTargetDirectory()+File.separator+MCCABE_REPORTS+File.separator+"ALLCLASSESMETRICS.CSV", true));

        files.add(new DataFile(DataFileType.MET,curEA.getTargetDirectory()+File.separator+MCCABE_REPORTS+File.separator+"traitedALLMODULESMETRICS.CSV", false));

        LoaderConfig config = new LoaderConfig(this.config.getProjectId(), curEA, this.config.getBaselineId());
        config.setPeremptElementsIfNotUsed(this.config.isMasterTool());
        config.setCreateIfDoesNotExists(this.config.isMasterTool());
        config.setUpdateMetricIfAlreadyExist(false);
        config.setFilePathAndLineActive(false);

        DbLoader3 loader = new DbLoader3(config, files);
        loader.execute();
        //END load Data

        //loading Architecture
        //architecture isn't done for cobol!
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cobol")){
            new ArchitectureLoader(curEA,this.config.getBaselineId(),curEA.getTargetDirectory()+File.separator+MCCABE_REPORTS+File.separator+"CALLSTO.MODIFIED.CSV");
            //end loading Architecture
            //creating real links
            RealLinkCreator rlc = new RealLinkCreator(curEA.getId(),this.config.getBaselineId());
            rlc.execute();
            //end
        }
    }

    public static final String MCCABE_REPORTS = "reports";


}

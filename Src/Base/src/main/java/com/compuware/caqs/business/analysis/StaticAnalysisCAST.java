/*
 * StaticAnalysisCAST.java
 *
 * Created on 21 janvier 2005, 15:01
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.DbLoader;
import com.compuware.caqs.business.load.db.LoaderConfig;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.RealLinkCreator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;


/**
 * @author cwfr-dzysman
 */
public class StaticAnalysisCAST extends StaticAnalysis {
    //mccabe
    protected String castScriptConfigurationFilePath = null;
    protected String callsToScript = null;


    /**
     * Creates a new instance of StaticAnalysisCAST
     */
    public StaticAnalysisCAST() {
        super();
        logger.info("Tool is CAST");
    }

    protected void initSpecific(Properties dbProps) {
        this.castScriptConfigurationFilePath = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.CAST_SCRIPT_CONFIG_PATH_KEY);
        this.callsToScript = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.CALLS_TO_SCRIPT_PATH_KEY);
    }


    protected void toolAnalysis(EA curEA) {
    }

    @Override
    protected AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(checkForExistingNonEmptyFile(curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "ALLCLASSESMETRICS.CSV"));
    	if (result.isSuccess()) {
    		result.setSuccess(checkForExistingNonEmptyFile(curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "traitedALLMODULESMETRICS.CSV"));
    	}
    	return result;
    }
    
    protected void loadData(EA curEA) throws LoaderException {
        //load Data 
        Collection<DataFile> files = new ArrayList<DataFile>();
        files.add(new DataFile(DataFileType.EA, curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "ALLPROGRAMMETRICS.CSV", false));
        files.add(new DataFile(DataFileType.CLS, curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "ALLCLASSESMETRICS.CSV", true));
        //files.add(new DataFile(DataFileType.CLS,curEA.getTargetDirectory()+File.separator+"reports"+File.separator+"INTERFACES.CSV"));
        files.add(new DataFile(DataFileType.MET, curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "traitedALLMODULESMETRICS.CSV", false));
        files.add(new DataFile(DataFileType.LINKS_INTERFACEMETHODS, curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "INTERFACESMETHODS.CSV", false));
        files.add(new DataFile(DataFileType.LINKS_CLASSESMETHODS, curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "CLASSMETHODS.CSV", false));
        //files.add(new DataFile(DataFileType.LINKS_INHERITANCE, curEA.getTargetDirectory()+File.separator+"reports"+File.separator+"COMPUWARE_INHER_LINKS.csv"));
        
        LoaderConfig config = new LoaderConfig(this.config.getProjectId(), curEA, this.config.getBaselineId());
        config.setPeremptElementsIfNotUsed(this.config.isMasterTool());
        config.setCreateIfDoesNotExists(this.config.isMasterTool());
        config.setUpdateMetricIfAlreadyExist(false);
        config.setFilePathAndLineActive(false);

        DbLoader loader = new DbLoader(config);
        loader.execute(files);
        //END load Data
        
        //loading Architecture
        new ArchitectureLoader(curEA, this.config.getBaselineId(), curEA.getTargetDirectory() + File.separator + "CAST_reports" + File.separator + "CALLSTO.CSV");
        //end loading Architecture
        //creating real links
        RealLinkCreator rlc = new RealLinkCreator(curEA.getId(), this.config.getBaselineId());
        rlc.execute();
        //end
    }


}

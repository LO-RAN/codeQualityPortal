/*
 * StaticAnalysisPQC.java
 *
 * @author  cwfr-lizac
 * @version
 * 
 * Created on february 14, 2005, 03:40 PM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.DbLoader3;
import com.compuware.caqs.business.load.db.LoaderConfig;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.parsers.pqc.Parser;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.analysis.EA;

/**
 *
 * @author  cwfr-lizac
 */
public class StaticAnalysisPQC extends StaticAnalysis {
	
	private static final String PQC_REPORT_DIR 	= "PQCreports";
	private static final String PQC_SRC_DIR 		= "src";
	private static final String PQC_REPORT_CSV 	= "pqcOut.csv";
	
    //PQC
    protected String pqcDataFile;
    
    private File reportDirectory;
    private String sourcesPath;    
    
    public StaticAnalysisPQC() {
        super();
        logger.info("Tool is PQC");
    }
    
    protected void initSpecific(Properties dbProps){
        //checkstyle
        this.pqcDataFile = dbProps.getProperty(Constants.PQC_DATA_FILE_KEY);
    }
    
    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        if(curEA.getDialecte().getId().substring(0,7).compareToIgnoreCase("pacbase")!=0){
            logger.info("PQC can only be used on Pacbase; ignoring request for EA: "+curEA.getLib()+" !");
        }
        else{
            //creates report directory
            this.reportDirectory = new File(curEA.getTargetDirectory()+File.separator+StaticAnalysisPQC.PQC_REPORT_DIR);
            this.reportDirectory.mkdir();
            logger.info("Created report directory : "+reportDirectory.getPath());
            
            this.sourcesPath = curEA.getTargetDirectory()+File.separator+StaticAnalysisPQC.PQC_SRC_DIR;

            // process pqc data file to make CSV
            String outputFile=reportDirectory.getPath()+File.separator+StaticAnalysisPQC.PQC_REPORT_CSV;
            String inputFile=sourcesPath+File.separator+pqcDataFile;

            logger.info("Processing : "+inputFile+" to produce "+outputFile);
            
            // actually transform PQC generated file into CSV
    		Parser p=new Parser(inputFile,outputFile);
    		p.generateCSV();
        }
    }

	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        this.reportDirectory = new File(curEA.getTargetDirectory()+File.separator+StaticAnalysisPQC.PQC_REPORT_DIR);
        String pqcCsvOutputFile = reportDirectory.getPath()+File.separator+StaticAnalysisPQC.PQC_REPORT_CSV;
    	result.setSuccess(checkForExistingNonEmptyFile(pqcCsvOutputFile));
        return result;
    }
    
    @Override
    protected void loadData(EA curEA) throws LoaderException {
        //load Data
        Collection<DataFile> files = new ArrayList<DataFile>();
        this.reportDirectory = new File(curEA.getTargetDirectory()+File.separator+StaticAnalysisPQC.PQC_REPORT_DIR);
        String pqcCsvOutputFile = reportDirectory.getPath()+File.separator+StaticAnalysisPQC.PQC_REPORT_CSV;
        files.add(new DataFile(DataFileType.CLS, pqcCsvOutputFile, true));
        
        LoaderConfig config = new LoaderConfig(this.config.getProjectId(), curEA, this.config.getBaselineId());
        config.setPeremptElementsIfNotUsed(this.config.isMasterTool());
        config.setCreateIfDoesNotExists(this.config.isMasterTool());
        config.setUpdateMetricIfAlreadyExist(false);
        config.setFilePathAndLineActive(false);

        DbLoader3 loader = new DbLoader3(config, files);
        loader.execute();
        //END load Data
    }
}

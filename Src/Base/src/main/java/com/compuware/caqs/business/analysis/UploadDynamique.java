/*
 * UploadDynamique.java
 * Created on 10 octobre 2002, 17:26
 */

package com.compuware.caqs.business.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.DbLoader3;
import com.compuware.caqs.business.load.db.LoaderConfig;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.CommandExec;

import org.apache.log4j.Logger;

/**
 *
 * @author  fxa
 */
public class UploadDynamique {

    // declaration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("UploadDynamique");

    protected String projectId;
    protected String baseLineId;
    protected String sourceFile;
    protected String eaId;
    protected String dialectId;
    protected String devPartnerScriptModifCsv;
    protected String devEnterpriseCobolScriptModifCsv;

    /** Creates a new instance of UploadDynamique */
    public UploadDynamique(String projectId, String baseLineId,String sourceFile,String eaId,String dialectId, boolean master) throws LoaderException {
        this.projectId = projectId;
        this.baseLineId = baseLineId;
        this.sourceFile = sourceFile;
        this.eaId = eaId;
        this.dialectId = dialectId.toUpperCase();

        Properties dbProps = CaqsConfigUtil.getCaqsConfigProperties(this);
        try {
            this.devPartnerScriptModifCsv = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.DEVPARTNER_SCRIPT_MODIF_CSV_KEY);
            this.devEnterpriseCobolScriptModifCsv = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.DEVENTREPRISE_COBOL_SCRIPT_MODIF_CSV_KEY);
            if ((devPartnerScriptModifCsv == null) || (devEnterpriseCobolScriptModifCsv == null)) {
                UploadDynamique.logger.error("Can't read the properties file. " +
                "Make sure configPortailQualite.conf is in the CLASSPATH");
            }
        }
        catch (Exception e) {
            UploadDynamique.logger.error("Can't read the properties file. " +
            "Make sure configPortailQualite.conf is in the CLASSPATH");
        }

        Collection<DataFile> files = new ArrayList<DataFile>();
        String cmd ="";
        if ( this.dialectId.startsWith("VB") ){
            cmd ="perl " + this.devPartnerScriptModifCsv + " " + this.sourceFile + " " + this.sourceFile+".modified";
            this.execute(cmd);
            files.add(new DataFile(DataFileType.MET, this.sourceFile+".modified", false));
        }
        else if ( this.dialectId.startsWith("JAVA") ){
            com.compuware.caqs.business.load.ConvertDevPartner cdp = new com.compuware.caqs.business.load.ConvertDevPartner();
            cdp.convertion(this.sourceFile,this.sourceFile+".modified");
            files.add(new DataFile(DataFileType.MET, this.sourceFile+".modified", false));
        }
        else if ( this.dialectId.startsWith("COBOL") ){
            //the file separator must be ;
            // have to create a perl script to take file name as prog name and add it to proc names.
            cmd ="perl " + this.devEnterpriseCobolScriptModifCsv + " " + this.sourceFile + " " + this.sourceFile+".modified";
            this.execute(cmd);
            files.add(new DataFile(DataFileType.MET, this.sourceFile, false));
        }
        else{
            UploadDynamique.logger.error("UNKNOWN DIALECT !!!" + this.dialectId);
        }

        EA ea = new EA();
        ea.setId(this.eaId);
        ProjectBean project = new ProjectBean();
        project.setId(this.projectId);
        ea.setProject(project);
        LoaderConfig config = new LoaderConfig(this.projectId, ea, this.baseLineId);
        config.setPeremptElementsIfNotUsed(master);
        config.setCreateIfDoesNotExists(master);
        config.setUpdateMetricIfAlreadyExist(true);
        config.setFilePathAndLineActive(false);

        DbLoader3 loader = new DbLoader3(config, files);
        loader.execute();
    }

    private void execute(String cmd){
        try{
        	UploadDynamique.logger.info(cmd);
        	CommandExec.exec(cmd, UploadDynamique.logger);
        }
        catch(Exception e){
        	UploadDynamique.logger.error("Error executing dynamic metrics upload", e);
        }
    }
}

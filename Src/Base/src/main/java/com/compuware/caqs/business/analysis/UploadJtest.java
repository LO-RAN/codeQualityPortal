/*
 * UploadDynamique.java
 * Created on 10 octobre 2002, 17:26
 */

package com.compuware.caqs.business.analysis;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.DbLoader3;
import com.compuware.caqs.business.load.db.LoaderConfig;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;

/**
 *
 * @author  fxa
 */
public class UploadJtest {

    // déclaration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("UploadDynamique");
    
    protected String projectId;
    protected String baseLineId;
    protected String sourceFile;
    protected String eaId;
    protected String dialectId;
    protected String devPartnerScriptModifCsv;
    
    /** Creates a new instance of UploadDynamique */
    public UploadJtest(String projectId, String baseLineId, String sourceFile, String eaId, String dialectId) throws LoaderException {
        this.projectId = projectId;
        this.baseLineId = baseLineId;
        this.sourceFile = sourceFile;
        this.eaId = eaId;
        this.dialectId = dialectId;
        
        Collection<DataFile> files = new ArrayList<DataFile>();
        files.add(new DataFile(DataFileType.CLS, this.sourceFile, true));

        EA ea = new EA();
        ea.setId(this.eaId);
        ProjectBean project = new ProjectBean();
        project.setId(this.projectId);
        ea.setProject(project);

        LoaderConfig config = new LoaderConfig(this.projectId, ea, this.baseLineId);
        config.setPeremptElementsIfNotUsed(false);
        config.setCreateIfDoesNotExists(true);
        config.setUpdateMetricIfAlreadyExist(true);
        config.setFilePathAndLineActive(false);

        DbLoader3 loader = new DbLoader3(config, files);
        loader.execute();
    }
}

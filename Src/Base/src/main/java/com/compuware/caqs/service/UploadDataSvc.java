package com.compuware.caqs.service;

import java.io.File;
import java.util.List;

import com.compuware.caqs.business.analysis.UploadDynamique;
import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.DataLoader;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.actions.upload.uploaders.AbstractCaqsUploader;
import com.compuware.caqs.service.upload.UploadFileType;
import com.compuware.toolbox.util.logging.LoggerManager;

public class UploadDataSvc {

    private static final UploadDataSvc instance = new UploadDataSvc();

    private UploadDataSvc() {
    }

    public static UploadDataSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger("Ihm");

    /**
     * Upload the file data.
     * @param uploader the data uploader.
     * @throws CaqsException the load failed or no data has been extracted.
     */
    public void uploadLoadedMetricsList(AbstractCaqsUploader uploader) throws CaqsException {
        uploader.loadData();
        if (uploader.isMaster()) {
            BaselineSvc baselineSvc = BaselineSvc.getInstance();
            ElementBean ea = uploader.getApplicationEntity();
            baselineSvc.update(ea.getBaseline().getLib(), ea.getBaseline().getId(), true);
        }
    }
}
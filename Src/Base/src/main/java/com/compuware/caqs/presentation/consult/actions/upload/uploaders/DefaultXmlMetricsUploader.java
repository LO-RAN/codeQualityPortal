/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.consult.actions.upload.uploaders;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.LoaderException;
import java.io.File;

import com.compuware.caqs.business.load.db.DataLoader;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.XmlFileLoader;

/**
 * The default CSV reader is able to read CSV files created like:
 * ElementName;ElementType;MetricId;...;
 * @author cwfr-fdubois
 */
public class DefaultXmlMetricsUploader extends AbstractCaqsUploader {

    /**
     * Default Constructor.
     */
    public DefaultXmlMetricsUploader() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCorrectExport(File f) {
        boolean result = true;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean extractMetrics() {
        boolean retour = false;
        DataFile file = new DataFile(DataFileType.CLS, this.uploadedFile.getAbsolutePath(), true);
        FileLoader loader = new XmlFileLoader(this.ea, this.ea.getProject(), this.ea.getBaseline());
        loader.setMainTool(this.isMaster());
        logger.info("Load data");
        try {
            loader.load(file);
            retour = true;
        } catch (LoaderException ex) {
            logger.error("erreur upload", ex);
        }
        return retour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataLoader getLoader() {
        DataLoader result = new XmlFileLoader(this.ea, this.ea.getProject(), this.ea.getBaseline());
        result.setMainTool(this.master);
        return result;
    }
}

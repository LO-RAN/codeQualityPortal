/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.consult.actions.upload.uploaders;

import java.io.File;

import com.compuware.caqs.business.analysis.UploadDynamique;
import com.compuware.caqs.exception.CaqsException;

/**
 * The Calls to CSV reader is able to read CSV files created like:
 * ElementFrom;ElementTo
 * @author cwfr-fdubois
 */
public class DevPartnerCovUploader extends AbstractCaqsUploader {

    /**
     * Default Constructor: separator is ;.
     */
    public DevPartnerCovUploader() {
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
        return true;
    }

    /**
     * {@inheritDoc}
     * @throws CaqsException
     */
    @Override
    public void loadData() throws CaqsException {
        new UploadDynamique(ea.getProject().getId(), ea.getBaseline().getId(), this.uploadedFile.getPath(), ea.getId(), ea.getDialecte().getId(), master);
    }

}

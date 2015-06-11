/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.consult.actions.upload.uploaders;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;
import com.compuware.caqs.business.load.db.ArchitectureLoader;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.exception.CaqsException;


/**
 * The Calls to CSV reader is able to read CSV files created like:
 * ElementFrom;ElementTo
 * @author cwfr-fdubois
 */
public class CsvCallsUploader extends AbstractCaqsUploader {

    /** The number of columns in the CSV. */
    private static final int COLUMN_NUMBER = 2;

    /** The CSV separator. */
    private String csvSeparator = ";";

    /**
     * Default Constructor: separator is ;.
     */
    public CsvCallsUploader() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCorrectExport(File f) {
        boolean result = false;
        try {
            CSVReader reader = new CSVReader(new FileReader(f));
            String[] line = reader.readNext();
            if (line != null && line.length != COLUMN_NUMBER) {
                result = true;
            }
            reader.close();
        }
        catch (IOException e) {
            logger.error("Error reading the uploaded file as a CSV file", e);
        }
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
     * Get the CSV separator.
     * @return the CSV separator.
     */
    public String getCsvSeparator() {
        return csvSeparator;
    }

	/**
     * Set the CSV separator value.
	 * @param csvSeparator The CSV separator to set.
	 */
    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    /**
     * {@inheritDoc}
     * @throws CaqsException
     */
    @Override
    public void loadData() throws CaqsException {
        EA pea = new EA();
        pea.setId(ea.getId());
        pea.setProject(ea.getProject());
        new ArchitectureLoader(pea, ea.getBaseline().getId(), this.uploadedFile.getPath());
    }

}

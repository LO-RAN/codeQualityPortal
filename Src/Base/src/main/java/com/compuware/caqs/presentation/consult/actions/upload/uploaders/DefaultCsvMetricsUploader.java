/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.consult.actions.upload.uploaders;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;


/**
 * The default CSV reader is able to read CSV files created like:
 * ElementName;ElementType;MetricId;...;
 * @author cwfr-fdubois
 */
public class DefaultCsvMetricsUploader extends AbstractCaqsUploader {

    /** The minimum number of columns in the CSV. */
    private static final int MIN_COLUMN_NUMBER = 3;

    /** The CSV separator. */
    private String csvSeparator = ";";

    /**
     * Default Constructor: separator is ;.
     */
    public DefaultCsvMetricsUploader() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCorrectExport(File f) {
        boolean result = false;
        try {
            CSVReader reader = new CSVReader(new FileReader(f), this.csvSeparator.charAt(0));
            String[] line = reader.readNext();
            if (line != null && line.length >= MIN_COLUMN_NUMBER) {
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
        boolean result = false;
        try {
            CSVReader reader = new CSVReader(new FileReader(this.uploadedFile));
            String[] firstLine = reader.readNext();
            if (firstLine != null && firstLine.length >= MIN_COLUMN_NUMBER) {
                String[] line = reader.readNext();
                while (line != null) {
                    if (line.length >= MIN_COLUMN_NUMBER) {
                        for (int i = 0; i < line.length; i++) {
                            extractMetrics(firstLine, line);
                        }
                    }
                    line = reader.readNext();
                }
            }
            reader.close();
        }
        catch (IOException e) {
            logger.error("Error reading the uploaded file as a CSV file", e);
        }
        return result;
    }

    /**
     * Extract element and metric data from the given CSV line and the given CSV header.
     * @param header the CSV header (first line of the file).
     * @param line the CSV line.
     */
    private void extractMetrics(String[] header, String[] line) {
        if (line.length == header.length) {
            for (int i = 2; i < line.length; i++) {
                addMetric(header[i], line[0], line[1], Double.parseDouble(line[i]), false);
            }
        }
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

}

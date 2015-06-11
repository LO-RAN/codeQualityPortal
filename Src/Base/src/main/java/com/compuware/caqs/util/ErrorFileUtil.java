/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.util;

import java.io.File;
import java.io.IOException;

import com.compuware.toolbox.io.FileTools;

import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-fdubois
 */
public class ErrorFileUtil {

    /** The error directory : CaqsHome/Errors. */
    private static final String ERROR_DIR = CaqsConfigUtil.getCaqsHome() + "/Errors";

    /**
     * Append the content fo the given file to the error file.
     * @param baselineId the baseline ID.
     * @param type the message type.
     * @param errorFile the file to copy.
     * @param logger the logger.
     */
    public static void writeError(String baselineId, String type, File errorFile, Logger logger) {
        try {
            FileTools.copy(errorFile, getErrorFile(baselineId, type), true);
        }
        catch (IOException ex) {
            logger.error("Error copying content of the file " + errorFile.getName(), ex);
        }
    }

    /**
     * Write the error message to the error file.
     * @param baselineId the baseline ID.
     * @param type the message type.
     * @param message the message to write.
     */
    public static void writeError(String baselineId, String type, String message) {
        FileUtils.append(ERROR_DIR, "error-" + type + '-' + baselineId + ".txt", message);
    }

    /**
     * Get the error file according to the given baseline ID and type.
     * @param baselineId the baseline ID.
     * @param type the error type.
     * @return the error file according to the given baseline ID and type.
     */
    public static File getErrorFile(String baselineId, String type) {
        return new File(ERROR_DIR, "error-" + type + '-' + baselineId + ".txt");
    }
}

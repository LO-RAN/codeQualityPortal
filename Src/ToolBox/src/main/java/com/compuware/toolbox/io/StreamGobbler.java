/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.toolbox.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-fdubois
 */
public class StreamGobbler extends Thread {

    /** The input stream to read. */
    private InputStream is;

    /** The current thread stream type. */
    private StreamType type = StreamType.OUTPUT;

    /** The current thread logger. */
    private Logger logger;

    /**
     * Constructor.
     * @param is the input stream.
     * @param type the stream type.
     * @param logger the current logger.
     */
    public StreamGobbler(InputStream is, StreamType type, Logger logger) {
        this.is = is;
        this.type = type;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ((line = br.readLine()) != null) {
                log(line);
            }
        }
        catch (IOException ioe) {
            logger.error("Error reading stream", ioe);
        }
    }

    /**
     * Log the given line according to the stream type.
     * @param line the line to log.
     */
    private void log(String line) {
        if (StreamType.ERROR.equals(this.type)) {
            logger.error(line);
        }
        else {
            logger.info(line);
        }
    }

}

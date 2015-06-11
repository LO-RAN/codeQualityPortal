/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-fdubois
 */
public class StreamGobbler extends Thread {

    /** The input stream to read. */
    private InputStream is;

    /** The stream type. */
    private StreamType type;

    /** The logger for the stream. */
    private Logger logger;

    /** The string to search map. */
    private Map<StreamStringSearchKey, String[]> searchString;

    /** The string to search result. */
    private Map<StreamStringSearchKey, Boolean> searchStringResult = new HashMap<StreamStringSearchKey, Boolean>();

    /**
     * Constructor.
     * @param is the input stream.
     * @param type the type of the stream.
     * @param logger the logger.
     * @param searchString the search string map.
     */
    StreamGobbler(InputStream is, StreamType type, Logger logger, Map<StreamStringSearchKey, String[]> searchString) {
        this.is = is;
        this.type = type;
        this.logger = logger;
        this.searchString = searchString;
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
                if (searchString != null) {
                    updateSearchStringResultFromLine(line);
                }
            }
        }
        catch (IOException ioe) {
            logger.error("Error reading stream", ioe);
        }
    }

    /**
     * Update the search string result from the given line.
     * @param line the given line to analyze.
     */
    private void updateSearchStringResultFromLine(String line) {
        if (searchString != null) {
            for (StreamStringSearchKey searchStringKey: searchString.keySet()) {
                updateSearchStringResultFromLine(searchStringKey, line);
            }
        }
    }

    /**
     * Update the search string result from the given line and search key.
     * @param searchStringKey the search key.
     * @param line the given line to analyze.
     */
    private void updateSearchStringResultFromLine(StreamStringSearchKey searchStringKey, String line) {
        String[] keyWords = searchString.get(searchStringKey);
    	if (keyWords != null && line != null && keyWords.length > 0) {
    		Pattern p;
    		Matcher m;
    		for (int i = 0; i < keyWords.length; i++) {
    			p = Pattern.compile(keyWords[i], Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    			m = p.matcher(line);
    			if (m.find()) {
                    searchStringResult.put(searchStringKey, true);
    				break;
    			}
    		}
    	}
    }

    /**
     * Log the given line according to the stream type.
     * @param line the line to log.
     */
    private void log(String line) {
        if (type.equals(StreamType.ERROR)) {
            logger.error(line);
        }
        else {
            logger.info(line);
        }
    }

    /**
     * Get the search string result.
     * @return the search string result.
     */
    public Map<StreamStringSearchKey, Boolean> getSearchStringResult() {
        return searchStringResult;
    }

}

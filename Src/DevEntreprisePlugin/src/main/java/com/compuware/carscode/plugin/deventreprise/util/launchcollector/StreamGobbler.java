package com.compuware.carscode.plugin.deventreprise.util.launchcollector;

import com.compuware.carscode.plugin.deventreprise.util.ReturnCodes;
import com.compuware.toolbox.io.StreamType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 *
 * @author cwfr-fdubois
 */
public class StreamGobbler extends Thread {

    /** The input stream to read. */
    private InputStream is;

    private ReturnCodes detectedError;

    /**
     * Constructor.
     * @param is the input stream.
     * @param type the stream type.
     * @param logger the current logger.
     */
    public StreamGobbler(InputStream is) {
        this.is = is;
        this.detectedError = ReturnCodes.NO_ERROR;
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
                System.out.println(line);
                if(line.contains("ERROR Unable to Connect to host")) {
                    this.detectedError = ReturnCodes.NO_CONNECTION;
                }
            }
        }
        catch (IOException ioe) {
            this.detectedError = ReturnCodes.GENERIC_ERROR;
            System.out.println(ioe.getMessage());
        }
    }

    public ReturnCodes getDetectedError() {
        return this.detectedError;
    }
}

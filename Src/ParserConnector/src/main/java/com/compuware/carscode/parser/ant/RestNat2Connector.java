/**
 *
 */
package com.compuware.carscode.parser.ant;

import com.compuware.carscode.parser.util.mail.MessagesProcessor;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;

/**
 * @author cwfr-lizac
 *
 */
public class RestNat2Connector extends AbstractConnector {

    static final Logger logger = Logger.getLogger(RestNat2Connector.class.getName());
    private File destDir = null;
    private String options = "";
    private String command = "";
    private File logFile = null;
    private File resultFile = null;
    private File codeFile = null;
    private String frequency = "10000";
    private String maxWait = "3600000";

    /**
     * @param destDir The destination directory for attachments.
     */
    public void setDestDir(String theDestDir) {
        this.destDir = new File(theDestDir);
    }

    /**
     * @param options The options to set.
     */
    public void setOptions(String options) {
        this.options = options;
    }

    /**
     * @param command The command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @param logFile The logFile to set.
     */
    public void setLogFile(String logFile) {
        this.logFile = new File(logFile);
    }

    /**
     * @param resultFile The resultFile to set.
     */
    public void setResultFile(String resultFile) {
        this.resultFile = new File(resultFile);
    }

    /**
     * @param codeFile The codeFile to set.
     */
    public void setCodeFile(String codeFile) {
        this.codeFile = new File(codeFile);
    }

    public void setFrequency(String theFrequency) {
        this.frequency = theFrequency;
    }

    public void setMaxWait(String theMaxWait) {
        this.maxWait = theMaxWait;
    }

    protected void checkParameters() throws BuildException {
        if (destDir == null) {
            throw new BuildException("A valid destination directory is required.", getLocation());
        }
        if (command == null) {
            throw new BuildException("A valid command is required.", getLocation());
        }
        if (logFile == null || logFile.isDirectory()) {
            throw new BuildException("A valid log file is required.", getLocation());
        }
        if (resultFile == null || resultFile.isDirectory()) {
            throw new BuildException("A valid result file is required.", getLocation());
        }
        if (codeFile == null || codeFile.isDirectory()) {
            throw new BuildException("A valid code file is required.", getLocation());
        }
    }

    protected void processAnalysis() {

        // execute what we believe should be a CFT command triggering a remote RestNat analysis
        String cmd = getCommand();
        File dir = logFile.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        exec(cmd, logFile);

        // Check result once in "freq" MILLIseconds
        int freq = Integer.parseInt(frequency);
        int wait=Integer.parseInt(maxWait);
        
        // wait until both files are available or timeout occured
        while ((!resultFile.exists() | !codeFile.exists()) & wait>0) {
            logger.log(Level.INFO, "Going to sleep for " + freq / 1000 + " seconds ("+wait/1000+" seconds remaining before abortion)...");
            try {
                Thread.sleep(freq); // sleep for freq milliseconds
                wait -= freq;
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }


    }

    private String getCommand() {
        StringBuilder cmd = new StringBuilder(this.command);
        if (this.options != null && this.options.length() > 0) {
            cmd.append(' ').append(this.options);
        }
        return cmd.toString();
    }
}

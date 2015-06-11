/**
 * 
 */
package com.compuware.carscode.parser.ant;

import com.compuware.carscode.parser.util.mail.MessagesProcessor;
import java.io.File;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;

/**
 * @author cwfr-lizac
 *
 */
public class RestNatConnector extends AbstractConnector {

    static final Logger logger = Logger.getLogger(RestNatConnector.class.getName());
    private File destDir = null;
    private String mailhost = null;
    private int    port=0;
    private String user = null;
    private String password = null;
    private String mbox = "INBOX";
    private String frequency = "10000";
    private String options = "";
    private String beforeCommand = "";
    private String afterCommand = "";
    private String prefix = "RESTNAT:";
    private File destFile = null;
    private String protocol = "imaps";

    /**
     * @param destDir The destination directory for attachments.
     */
    public void setDestDir(String theDestDir) {
        this.destDir = new File(theDestDir);
    }

    public void setMailhost(String theHost) {
        this.mailhost = theHost;
    }

    public void setPort(int thePort) {
        this.port = thePort;
    }

    public void setUser(String theUser) {
        this.user = theUser;
    }

    public void setPassword(String thePassword) {
        this.password = thePassword;
    }

    public void setMbox(String theMbox) {
        this.mbox = theMbox;
    }

    public void setFrequency(String theFrequency) {
        this.frequency = theFrequency;
    }

    public void setPrefix(String thePrefix) {
        this.prefix = thePrefix;
    }

    public void setProtocol(String theProtocol) {
        this.protocol = theProtocol;
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
    public void setBeforeCommand(String command) {
        this.beforeCommand = command;
    }

    /**
     * @param command The command to set.
     */
    public void setAfterCommand(String command) {
        this.afterCommand = command;
    }
	/**
	 * @param destFile The destFile to set.
	 */
	public void setDestFile(String destFile) {
		this.destFile = new File(destFile);
	}

    protected void checkParameters() throws BuildException {
        if (destDir == null) {
            throw new BuildException("A valid destination directory is required.", getLocation());
        }
        if (mailhost == null) {
            throw new BuildException("A valid mail host name is required.", getLocation());
        }
        if (port == 0) {
            throw new BuildException("A valid port number is required.", getLocation());
        }
        if (user == null) {
            throw new BuildException("A valid user name is required.", getLocation());
        }
        if (password == null) {
            throw new BuildException("A valid user password is required.", getLocation());
        }
        if (protocol == null) {
            throw new BuildException("A valid protocol (ex: \"imap\" or \"imaps\") is required.", getLocation());
        }
        if (beforeCommand == null) {
            throw new BuildException("A valid beforeCommand is required.", getLocation());
        }
        if (afterCommand == null) {
            throw new BuildException("A valid afterCommand is required.", getLocation());
        }
        if (destFile == null || destFile.isDirectory()) {
            throw new BuildException("A valid destination file is required.", getLocation());
        }
    }

    protected void processAnalysis() {

        // execute what we believe should be a CFT command triggering a remote RestNat analysis
        String cmd = getBeforeCommand();
        File dir = destDir;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        exec(cmd, destFile);


        // then wait for email with results
        MessagesProcessor mp = new MessagesProcessor(dir);
        mp.checkNewMessages(mailhost, port, user, password, mbox, frequency, prefix, protocol);

        // execute a command after the mail has been processed
        // (typically used to get archive containing source files related to the analysed library)
        exec(afterCommand, destFile);

    }

    private String getBeforeCommand() {
        StringBuilder cmd = new StringBuilder(this.beforeCommand);
        if (this.options != null && this.options.length() > 0) {
            cmd.append(' ').append(this.options);
        }
        return cmd.toString();
    }
}

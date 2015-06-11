/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

/**
 * @author cwfr-fdubois
 *
 */
public class PmccabeConnector extends AbstractConnector {

    private List srcFilesets = new ArrayList();
    private File destFile = null;
    private String options = "-t";
    private String command = "pmccabe";
    
    public void addSource(FileSet fileset) {
        srcFilesets.add(fileset);
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
	 * @param destFile The destFile to set.
	 */
	public void setDestFile(String destFile) {
		this.destFile = new File(destFile);
	}

	protected void checkParameters() throws BuildException {
        if (srcFilesets == null || srcFilesets.isEmpty()) {
            throw new BuildException("A fileset is required.", getLocation());
        }
        if (destFile == null || destFile.isDirectory()) {
            throw new BuildException("A valid destination file is required.", getLocation());
        }
    }
    
    protected void processAnalysis() {
    	String cmd = getCommand();
    	File destDir = destFile.getParentFile();
    	if (!destDir.exists()) {
    		destDir.mkdirs();
    	}
    	exec(cmd, destFile);
    }
    
    private String getCommand() {
    	StringBuffer cmd = new StringBuffer(this.command);
    	if (this.options != null && this.options.length() > 0) {
    		cmd.append(' ').append(this.options);
    	}
    	cmd.append(getCommandFilePathList(this.srcFilesets, " "));
        return cmd.toString();
    }
	    
}

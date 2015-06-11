/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

/**
 * @author cwfr-fdubois
 *
 */
public class SplintConnector extends AbstractConnector {

    private List srcFilesets = new ArrayList();
    private List libFilesets = new ArrayList();
    private String options = null;
    private String destFile = null;
    private String command = "splint";
    
    public void addSource(FileSet fileset) {
        srcFilesets.add(fileset);
    }
    
    public void addLib(FileSet fileset) {
        libFilesets.add(fileset);
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
		this.destFile = destFile;
	}

	protected void checkParameters() throws BuildException {
        if (srcFilesets == null || srcFilesets.isEmpty()) {
            throw new BuildException("A source fileset is required.", getLocation());
        }
        if (libFilesets == null || libFilesets.isEmpty()) {
            throw new BuildException("A library fileset is required.", getLocation());
        }
        if (destFile == null || destFile.length() < 1) {
            throw new BuildException("A destination file is required.", getLocation());
        }
    }
    
    protected void processAnalysis() {
    	StringBuffer cmd = new StringBuffer(this.command);
    	if (this.options != null && this.options.length() > 0) {
    		cmd.append(' ').append(this.options);
    	}
    	cmd.append(" +csv ").append(destFile);
    	cmd.append(getCommandFilePathList(this.libFilesets, " -I", true));
    	cmd.append(getCommandFilePathList(this.srcFilesets, " "));
        exec(cmd.toString(), null);
    }
    
}

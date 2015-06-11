/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import org.apache.tools.ant.BuildException;

/**
 * @author cwfr-fdubois
 *
 */
public class FlawFinderConnector extends AbstractConnector {

	public static final String OS_NAME = System.getProperty("os.name");
	
	private File srcDir = null;
    private File destDir = null;
    private String options = "";
    private String flawFinderPath = "";
    private String command = "python";
    
	/**
	 * @param options The options to set.
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * @param flawFinderPath The flawFinderPath to set.
	 */
	public void setFlawFinderPath(String flawFinderPath) {
		this.flawFinderPath = flawFinderPath;
	}

	/**
	 * @param srcDir The srcDir to set.
	 */
	public void setSrcDir(String srcDir) {
		this.srcDir = new File(srcDir);
	}

	/**
	 * @param command The command to set.
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @param destDir The destDir to set.
	 */
	public void setDestDir(String destDir) {
		this.destDir = new File(destDir);
	}

	protected void checkParameters() throws BuildException {
        if (srcDir == null || !srcDir.exists()) {
            throw new BuildException("A valid source directory is required.", getLocation());
        }
        if (destDir == null) {
            throw new BuildException("A valid destination directory is required.", getLocation());
        }
        if (flawFinderPath == null || flawFinderPath.length() < 1) {
            throw new BuildException("the path for FlawFinder is required.", getLocation());
        }
    }
    
    protected void processAnalysis() {
    	StringBuffer cmd = new StringBuffer(this.command);
    	cmd.append(' ').append(this.flawFinderPath);
    	if (this.options != null && this.options.length() > 0) {
    		cmd.append(' ').append(this.options);
    	}
    	cmd.append(' ').append(srcDir.getAbsolutePath());

		if (!this.destDir.exists()) {
			this.destDir.mkdirs();
		}
		File ffout = new File(destDir, "ffout.csv");
		exec(cmd.toString(), ffout);
    }
	    
}

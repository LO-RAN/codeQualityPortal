/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;

/**
 * @author cwfr-lizac
 *
 */
public class CppCheckConnector extends AbstractConnector {

	public static final String OS_NAME = System.getProperty("os.name");
	
	private File srcDir = null;
    private File destDir = null;
    private String command = "cppcheck -f --xml --enable=all";
    
	/**
	 * @param options The options to set.
	 */
	public void setCmd(String command) {
		this.command = command;
	}


	/**
	 * @param srcDir The srcDir to set.
	 */
	public void setSrcDir(String srcDir) {
		this.srcDir = new File(srcDir);
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
        if (command == null) {
            throw new BuildException("A valid command is required.", getLocation());
        }
    }
    
    protected void processAnalysis() {
    	StringBuffer cmd = new StringBuffer(this.command);
    	cmd.append(' ').append(srcDir.getAbsolutePath());

		if (!this.destDir.exists()) {
			this.destDir.mkdirs();
		}
		File outputFile = new File(destDir, "cppcheck.xml");
		exec(cmd.toString(), outputFile);
    }
	    
	/**
	 * Execute the given command and write the standard output to the given output file.
	 * @param command the command to execute.
	 * @param outputFile the output file.
	 * @param caller the calling ant task.
	 * @return the execution return code.
	 */
	public int exec(String command, File outputFile) throws BuildException {
		Process p = null;
		try{
			log("Executing the following command: " + command.toString());
			p = Runtime.getRuntime().exec(command);

                        // cppcheck writes its xml results on error channel rather than standard output...
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), outputFile);
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), null);
			errorGobbler.start();
			outputGobbler.start();

			try {
				if (p.waitFor() != 0) {
					log("Process end with exit value : " + p.exitValue() );
				}
			}
			catch (InterruptedException e) {
				throw new BuildException("Error executing process: " + e.toString(), getLocation());
			}

		}
		catch(IOException e){
			throw new BuildException("Error executing process: " + e.toString(), getLocation());
		}
		catch (Exception e) {
			throw new BuildException("Error executing process: " + e.toString(), getLocation());
		}
		return p.exitValue();
	}
}

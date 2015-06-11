/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.DirSet;

/**
 * @author cwfr-lizac
 *
 */
public class PhpcpdConnector extends AbstractConnector {

    private String 	srcPath = "";
    private String 	options = "";
    private String 	command = "php";
    private String 	scriptPath = "";
    private String 	outputFile = null;
    private List 	includeDirs = new ArrayList();

   private static final String OS_NAME = System.getProperty("os.name");
      
   private static final String OS_SPECIFIC_DOUBLE_QUOTE = OS_NAME.compareToIgnoreCase("WINDOWS") >= 0 ? "\"" : "";
    
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
	 * @param options The script path to set.
	 */
	public void setScriptPath(String s) {
		this.scriptPath = s;
	}
	
	/**
	 * @param options The src path to set.
	 */
	public void setSrcPath(String s) {
		this.srcPath = s;
	}
	
	/**
	 * @param options The output file to use.
	 */
	public void setOutputFile(String s) {
		this.outputFile = s;
	}
	
	protected void checkParameters() throws BuildException {
		File f = new File(this.outputFile);
        if (outputFile == null || f.isDirectory()) {
            throw new BuildException("A valid destination file is required.", getLocation());
        }
    }
    
    protected void processAnalysis() {
    	String cmd = getCommand();
    	System.out.println("commande : "+cmd);
    	exec(cmd, null);
    }
    
    public void addDirset(DirSet dirset) {
    	includeDirs.add(dirset);
    }
    
    private String getCommand() {
    	StringBuffer cmd = new StringBuffer(this.command);
    	if (this.options != null && this.options.length() > 0) {
    		cmd.append(' ').append(this.options);
    	}
    	cmd.append(" -d include_path="+OS_SPECIFIC_DOUBLE_QUOTE+".");
    	for(Iterator it=this.includeDirs.iterator(); it.hasNext(); ) {
    		DirSet ds = (DirSet) it.next();
    		String root = ds.getDir(this.getProject()).getAbsolutePath();
    		DirectoryScanner scanner = ds.getDirectoryScanner(this.getProject());
    		String[] dirs = scanner.getIncludedDirectories();
    		for(int i=0; i<dirs.length; i++) {
    			cmd.append(File.pathSeparator).append(root).append(File.separatorChar).append(dirs[i]);
    		}
    	}
    	cmd.append(OS_SPECIFIC_DOUBLE_QUOTE);
        
    	if (this.scriptPath != null && this.scriptPath.length() > 0) {
    		cmd.append(" -f ").append(this.scriptPath);
    	}
    	cmd.append(" -- --log-pmd="+this.outputFile+"");
    	
    	cmd.append(' ').append(this.srcPath);
        return cmd.toString();
    }
	    
}

package com.compuware.carscode.parser.ant;

import org.apache.tools.ant.BuildException;

public class GenericParserConnector extends AbstractConnector {
	private String fileList;
	private String resultsDir;
	private String srcDir;
	private String scriptPath;
	private String language;
    private String genericparserlanguagepath;
	
	
	protected void checkParameters() throws BuildException {
		if (resultsDir == null || "".equals(resultsDir)) {
            throw new BuildException("A directory to store results is required.", getLocation());
        }
        if (srcDir == null || "".equals(srcDir)) {
            throw new BuildException("A source directory is required.", getLocation());
        }
        if (language == null || "".equals(language)) {
            throw new BuildException("A language is required.", getLocation());
        }
	}

	protected void processAnalysis() throws BuildException {
		StringBuffer cmd = new StringBuffer(this.scriptPath);
		String fileListArg = this.srcDir;
        String callsToArg = "";
        if(this.fileList!=null && !"".equals(this.fileList)) {
    		String[] args = this.fileList.split(";");
    		if(args!=null && args.length>0) {
                String fileListPrefix = "fileList=";
                String callsToPrefix = "callsTo=";
    			for(int i=0; i<args.length; i++) {
    				if(args[i].startsWith(fileListPrefix)) {
    					fileListArg += ","+args[i].substring(fileListPrefix.length());
    				}
    				if(args[i].startsWith(callsToPrefix)) {
    					callsToArg = args[i].substring(callsToPrefix.length());
    				}
    			}
    		}
    	}
    	cmd.append(' ').append(fileListArg);
    	cmd.append(' ').append(this.resultsDir);
    	cmd.append(' ').append(this.language);
   		cmd.append(' ').append(this.srcDir);
        cmd.append(' ').append(this.genericparserlanguagepath);
    	cmd.append(' ').append(callsToArg);
		exec(cmd.toString(), null);
	}

	public void setFileList(String fl) {
		this.fileList = fl;
	}

	public void setResultsDir(String resultsDir) {
		this.resultsDir = resultsDir;
	}

	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
	}

	public void setLanguage(String l) {
		this.language = l;
	}

	public void setGenericparserlanguagepath(String l) {
		this.genericparserlanguagepath = l;
	}
}

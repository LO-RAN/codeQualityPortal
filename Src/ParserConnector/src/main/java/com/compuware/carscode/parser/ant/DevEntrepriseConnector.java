package com.compuware.carscode.parser.ant;

import org.apache.tools.ant.BuildException;

public class DevEntrepriseConnector extends AbstractConnector {
	private String collectionName;
	private String resultsDir;
	private String srcDir;
	private String scriptPath;
	private String sqlserverconfig;
	
	
	protected void checkParameters() throws BuildException {
		if (collectionName == null || "".equals(collectionName)) {
            throw new BuildException("At least a collection name is required.", getLocation());
        }
        if (resultsDir == null || "".equals(resultsDir)) {
            throw new BuildException("A directory to store results is required.", getLocation());
        }
        if (srcDir == null || "".equals(srcDir)) {
            throw new BuildException("A source directory is required.", getLocation());
        }
        if (sqlserverconfig == null || "".equals(sqlserverconfig)) {
            throw new BuildException("A config file for sqlserver connection is required.", getLocation());
        }
	}

	protected void processAnalysis() throws BuildException {
		StringBuffer cmd = new StringBuffer(this.scriptPath);
		String collections = "";
    	String infos1 = this.collectionName;
    	if(infos1!=null && !"".equals(infos1)) {
    		String[] args = infos1.split(";");
    		if(args!=null && args.length>0) {
                String collectionPrefix = "collections=";
    			for(int i=0; i<args.length; i++) {
    				if(args[i].startsWith(collectionPrefix)) {
    					collections = args[i].substring(collectionPrefix.length());
    					break;
    				}
    			}
    		}
    	}
    	cmd.append(' ').append(collections);
   		cmd.append(' ').append(this.srcDir);
    	cmd.append(' ').append(this.resultsDir);
    	cmd.append(' ').append(this.sqlserverconfig);
		exec(cmd.toString(), null);
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
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

	public void setSqlserverconfig(String sqlserverconfig) {
		this.sqlserverconfig = sqlserverconfig;
	}

}

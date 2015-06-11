package com.compuware.carscode.ant.taskdef;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class RedirectOutputTask extends Task {
	
	private File output;
	
	public void setOutput(File fileOutput) {
		output = fileOutput;
	}
	
    public void execute() throws BuildException {
        if (output == null) {
            throw new BuildException("The output attribute must be present.", getLocation());
        }
        try {
	        FileOutputStream fout = new FileOutputStream(output);
	        System.setOut(new PrintStream(fout));
        }
        catch (FileNotFoundException e) {
            throw new BuildException("The output cannot be created.", getLocation());
        }
    }
	
}

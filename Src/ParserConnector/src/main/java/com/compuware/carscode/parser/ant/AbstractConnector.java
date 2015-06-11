/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class AbstractConnector extends Task {

	/**
	 * Execute the parser connector ant task.
	 * @throws BuildException when an error occurs during analysis.
	 */
	public void execute() throws BuildException {
		checkParameters();
		processAnalysis();
	}

	/**
	 * Check task parameters validity.
	 * @throws BuildException
	 */
	protected abstract void checkParameters() throws BuildException;

	/**
	 * Process the code analysis.
	 * @throws BuildException
	 */
	protected abstract void processAnalysis() throws BuildException;

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
			log("Executing command: " + command.toString());
			p = Runtime.getRuntime().exec(command);

			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), null);            
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), outputFile);
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

	/**
	 * Get the command parameters list from a given fileset.
	 * Each parameter is separated with the given separator.
	 * @param srcFilesets the fileset to inspect.
	 * @param separator the separator between generated parameters.
	 */
	protected String getCommandFilePathList(List srcFilesets, String separator) {
		return this.getCommandFilePathList(srcFilesets, separator, false);
	}

	/**
	 * Get the command parameters list from a given fileset.
	 * Each parameter is separated with the given separator.
	 * @param srcFilesets the fileset to inspect.
	 * @param separator the separator between generated parameters.
	 * @param directoryTarget specify if the target list is a directory list or not.
	 */
	protected String getCommandFilePathList(List srcFilesets, String separator, boolean directoryTarget) {
		StringBuffer cmd = new StringBuffer();
		Iterator srcIterator = srcFilesets.iterator();
		FileSet fs = null;

		while (srcIterator.hasNext()) {
			fs = (FileSet)srcIterator.next();
			DirectoryScanner ds = fs.getDirectoryScanner(getProject());
			String[] includedFiles = null;
			if (directoryTarget) {
				includedFiles = ds.getIncludedDirectories();
			}
			else {
				includedFiles = ds.getIncludedFiles();
			}
			File base  = ds.getBasedir();
			File currentFile = null;
			for(int i = 0; i < includedFiles.length; i++) {
				currentFile = new File(base, includedFiles[i]);
				cmd.append(separator).append(currentFile.getAbsolutePath());
			}
		}
		return cmd.toString();
	}

	/*
    private static String extractFileType(String fileName) {
    	return fileName.substring(fileName.lastIndexOf('.'));
    }
	 */

	class StreamGobbler extends Thread
	{
		InputStream is;
		File outputFile;

		StreamGobbler(InputStream is, File type)
		{
			this.is = is;
			this.outputFile = type;
		}

		public void run()
		{
			try
			{
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String inputLine;
				if (outputFile != null) {
					FileWriter fw = new FileWriter(outputFile);
					BufferedWriter bw = new BufferedWriter(fw);
					while ((inputLine = br.readLine()) != null) {
						bw.append(inputLine).append('\n');
					}
					bw.flush();
					bw.close();
				}
				else {
					while ((inputLine = br.readLine()) != null) {
						log(inputLine);
					}
				}
				br.close();
			} catch (IOException ioe)
			{
				ioe.printStackTrace();  
			}
		}
	}
}

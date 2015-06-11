package com.compuware.carscode.ant.taskdef;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import code2html.Code2HTML;

/**
 * Created by cwfr-fdubois Date: 28 nov. 2005 Time:
 * 17:48:45 To change this template use File | Settings | File Templates.
 */
public class Code2HtmlTask extends Task {

	private File destDir;
	private File modesDir;
	private List filesets = new ArrayList();

	/**
	 * @param fileset : input files to process
	 */
	public void addFileset(FileSet fileset) {
		filesets.add(fileset);
	}

	/**
	 * @param dest : root directory where produced html files will be created
	 */
	public void setDest(File dest) {
		destDir = dest;
	}

	/**
	 * @param modes : directory where the syntax modes are stored
	 */
	public void setModes(File modes) {
		modesDir = modes;
	}

	public void execute() throws BuildException {
		if (filesets.isEmpty()) {
			throw new BuildException("A fileset is needed.", getLocation());
		}

		if (destDir == null) {
			throw new BuildException("The dest attribute must be present.",
					getLocation());
		}
		if (modesDir == null) {
			throw new BuildException("The modes attribute must be present.",
					getLocation());
		}
		processCode2Html();
		
		getLocation();
	}

	private void processCode2Html() {
		if (destDir.isDirectory()) {
				Iterator srcIterator = this.filesets.iterator();
				FileSet fs = null;
				while (srcIterator.hasNext()) {
					fs = (FileSet) srcIterator.next();

					DirectoryScanner ds = fs.getDirectoryScanner(getProject());
					String[] includedFiles = ds.getIncludedFiles();
					File base = ds.getBasedir();
					File src = null;

					String srcFile = "";
					String destFile = "";
					int count=0;

					for (int i = 0; i < includedFiles.length; i++) {
						src = new File(base, includedFiles[i]);

						String destDirPath =   destDir.getAbsolutePath() 
						                     + getFilePath(  src.getAbsolutePath()
						                    		       , base.getAbsolutePath()
						                      	          );
						createDirIfNotExist(destDirPath);

						srcFile = src.getAbsolutePath();
						
						destFile = destDirPath + File.separator + toHtmlFileName(src.getName());

						/*
						 * to cope with potential spaces in directory or file
						 * names : - we escape space characters on Unix like
						 * systems; - we enclose in quotes on windows systems; -
						 * we start the subprocess from a shell (itself
						 * depending on host system) to get the proper
						 * interpretation of command line arguments.
						 */
						
						
						Code2HTML.processFile( modesDir.getAbsolutePath()
								              ,srcFile.replaceAll("\\s",Constants.OS_SPECIFIC_SPACE)
								              ,destFile.replaceAll("\\s",Constants.OS_SPECIFIC_SPACE)
								             );
						count++;

				}
					System.out.println("Processed "+ count +" files");
			}
		}
	}

	private String toHtmlFileName(String fileName) {
		String result = fileName + ".html";
		return result;
	}

	private String getFilePath(String srcAbsolutePath, String srcBasePath) {
		String result = srcAbsolutePath.substring(srcBasePath.length());
		result = result.substring(0, result.lastIndexOf(File.separator));
		return result;
	}

	private void createDirIfNotExist(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}

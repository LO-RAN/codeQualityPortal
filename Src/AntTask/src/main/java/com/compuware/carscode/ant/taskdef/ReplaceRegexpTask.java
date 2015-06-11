package com.compuware.carscode.ant.taskdef;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 28 nov. 2005
 * Time: 17:48:45
 * To change this template use File | Settings | File Templates.
 */
public class ReplaceRegexpTask extends Task {

    private List filesets = new ArrayList();
    private String regexp = null;
    private String value = null;
    private boolean multilineRegexp = false;
    
    private FileUtils fileUtils = FileUtils.newFileUtils();
    
    public void addFileset(FileSet fileset) {
        filesets.add(fileset);
    }    
    
    /**
	 * @return Returns the regexp.
	 */
	public String getRegexp() {
		return regexp;
	}

	/**
	 * @param regexp The regexp to set.
	 */
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isMultiline() {
		return multilineRegexp;
	}

	public void setMultiline(boolean multilineRegexp) {
		this.multilineRegexp = multilineRegexp;
	}

	public void execute() throws BuildException {
        if (filesets.isEmpty()) {
            throw new BuildException("A fileset is needed.", getLocation());
        }

        processReplace();
    }

    private void processReplace() {
        try {
        	Iterator srcIterator = this.filesets.iterator();
        	FileSet fs = null;
        	while (srcIterator.hasNext()) {
        		fs = (FileSet)srcIterator.next();
        		
        		DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                String[] includedFiles = ds.getIncludedFiles();
                File base  = ds.getBasedir();
                File src = null;
                Reader reader = null;
                Writer writer = null;
                Pattern regexpPattern = initPattern();
                for(int i = 0; i < includedFiles.length; i++) {
                    src = new File(base, includedFiles[i]);
        		
                    File temp = fileUtils.createTempFile("rep", ".tmp", fileUtils.getParentFile(src)); 
                    reader = new FileReader(src);
                    writer = new FileWriter(temp);
                    BufferedReader br = new BufferedReader(reader);
                    BufferedWriter bw = new BufferedWriter(writer);
                    String buf = FileUtils.readFully(br);
                    if (buf == null) {
                    	buf = "";
                    }
                    
                    String newString = replaceAll(buf, regexpPattern); 
                    
                    bw.write(newString, 0, newString.length());
                    bw.flush();
                    bw.close();
                    br.close();
                	fileUtils.rename(temp, src);
                }            		
        	}
        } catch (IOException e) {
            throw new BuildException("exec failed "+e.toString());
        }
    }
    
    private Pattern initPattern() {
    	int options = Pattern.CASE_INSENSITIVE;
    	if (this.multilineRegexp) {
    		options = options | Pattern.DOTALL | Pattern.MULTILINE;
    	}
    	return Pattern.compile(this.regexp, options);
    }
    
    private String replaceAll(String content, Pattern regexpPattern) {
    	String result = "";
    	if (content.length() > 0) {
    		Matcher m = regexpPattern.matcher(content);
    		if (m.find()) {
    			result = m.replaceAll(this.value);
    		}
    		else {
    			result = content;
    			//this.log(content);
    			this.log(this.regexp + " does not match");
    		}
    	}
    	return result;
    }

}

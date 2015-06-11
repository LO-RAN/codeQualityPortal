/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

import com.compuware.carscode.parser.bean.MetricBean;
import com.compuware.carscode.parser.writer.CsvLinkWriter;
import com.compuware.carscode.parser.writer.ElementMetricWriter;
import com.compuware.carscode.parser.writer.XmlWriter;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class AbstractParser extends AbstractFormatter {

    protected List srcFilesets = new ArrayList();
    protected File linkDestFile = null;
    protected String baseDir = null;
    
    public void addSource(FileSet fileset) {
        srcFilesets.add(fileset);
    }
	
	/**
	 * @param baseDir the baseDir to set
	 */
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * @param destFile The link dest file to set.
	 */
	public void setLinkDestFile(String linkDestFile) {
		this.linkDestFile = new File(linkDestFile);
	}
	
	/**
	 * Execute the parser connector ant task.
	 * @throws BuildException when an error occurs during analysis.
	 */
	public void execute() throws BuildException {
		checkCommonParameters();
		checkParameters();
		processAnalysis();
	}
	
	protected void checkCommonParameters() throws BuildException {
        if (srcFilesets == null || srcFilesets.isEmpty()) {
            throw new BuildException("A valid source fileset is required.", getLocation());
        }
        if (destFile == null || destFile.isDirectory()) {
            throw new BuildException("A valid destination file is required.", getLocation());
        }
        if (linkDestFile == null || linkDestFile.isDirectory()) {
            throw new BuildException("A valid destination file for links is required.", getLocation());
        }
        if (baseDir == null) {
            throw new BuildException("A valid base directory is required.", getLocation());
        }
    }

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.ant.AbstractParser#processAnalysis()
	 */
	protected void processAnalysis() throws BuildException {
		Map data = extractData();
		try {
			printData(data);
			printLinks(data);
		}
		catch (IOException e) {
            throw new BuildException("Error during xml writing:" + e.toString(), getLocation());
		}
	}
	
	protected void printData(Map data) throws IOException {
		ElementMetricWriter writer = new XmlWriter();
		writer.print(data, destFile);
	}
	
	protected void printLinks(Map data) throws IOException {
		ElementMetricWriter writer = new CsvLinkWriter();
		writer.print(data, linkDestFile);
	}
	
	protected MetricBean createMetric(String id, double value, String line) {
		MetricBean metricBean = new MetricBean();
		metricBean = new MetricBean();
		metricBean.setId(id.toUpperCase());
		metricBean.setValue(value);
		metricBean.setLines(line);
		return metricBean;
	}
	
	protected int[] extractLineIndexes(String content) {
		Pattern linePattern = Pattern.compile("^.*$", Pattern.MULTILINE);
		int[] result = new int[linePattern.split(content).length + 1];
		Matcher m = linePattern.matcher(content);
		int i = 0;
		while(m.find()) {
			result[i++] = m.start() + 1;
		}
		return result;
	}
    	
	protected int getLine(int[] lineIndexes, int offset) {
		int result = Arrays.binarySearch(lineIndexes, offset);
		if (result < 0) {
			result = Math.abs(result - 1);
		}
		return result;
	}
}

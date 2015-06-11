/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;

import au.com.bytecode.opencsv.CSVReader;

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;
import com.compuware.carscode.parser.bean.IMetricBean;
import com.compuware.carscode.parser.bean.MetricBean;

/**
 * @author cwfr-fdubois
 *
 */
public class SplintFormatter extends AbstractFormatter {

    private String srcDirPath = null;

    public void setSrcDirPath(String srcDirPath) {
        this.srcDirPath = srcDirPath.replaceAll("\\\\", "/");
    }

	protected void checkParameters() throws BuildException {
    }
    
    protected Map extractData() {
    	Map result = new HashMap();
		try {
			CSVReader csvReader = new CSVReader(new FileReader(srcFile), ',');
			List resultList = csvReader.readAll();
			Iterator resultIter = resultList.iterator();
			if (resultIter.hasNext()) {
				// Skip the header line...
				resultIter.next();
			}
			String[] line = null;
			while (resultIter.hasNext()) {
				line = (String[])resultIter.next();
				if (line.length >= 6) {
					extractLineData(line, result);
				}
			}
		}
		catch (FileNotFoundException e) {
			throw new BuildException("A valid source file is required.", getLocation());
		}
		catch (IOException e) {
			throw new BuildException("Error reading the source file: " + e.toString(), getLocation());
		}
		return result;
    }
    
    private static final int FLAG_IDX = 2;
    private static final int FILENAME_IDX = 4;
    private static final int LINE_IDX = 5;
    
    private void extractLineData(String[] line, Map existingData) {
    	String metricName = "SPLINT_" + line[FLAG_IDX];
    	String fileName = getFileName(getRelativePath(line[FILENAME_IDX], srcDirPath));

    	String lineNumber = line[LINE_IDX];
    	
        if (metricName != null) {
        	metricName = metricName.toUpperCase();
        	IElementBean currentElement = (IElementBean)existingData.get(fileName);
        	if (currentElement == null) {
        		currentElement = new ElementBean();
        		currentElement.setDescElt(fileName);
        		currentElement.setTypeElt("FILE");
        		currentElement.setMetricMap(new HashMap());
        		existingData.put(currentElement.getDescElt(), currentElement);
        	}
        	Map metricMap = currentElement.getMetricMap();
        	IMetricBean metricBean = (IMetricBean)metricMap.get(metricName);
        	if (metricBean == null) {
        		metricBean = new MetricBean();
        		metricBean.setId(metricName);
        		metricMap.put(metricName, metricBean);
        	}
    		metricBean.incValue();
    		metricBean.addLine(lineNumber);
        }
    }    
}

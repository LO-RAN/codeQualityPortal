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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class FlawFinderFormatter extends AbstractFormatter {

	public static final String OS_NAME = System.getProperty("os.name");
    private String srcDirPath = null;

    public void setSrcDirPath(String srcDirPath) {
        this.srcDirPath = srcDirPath.replaceAll("\\\\", "/");
    }
	
	protected void checkParameters() throws BuildException {
    }
    
    protected Map extractData() {
    	Map result = new HashMap();
		if (srcFile.exists()) {
			try {
				CSVReader csvReader = new CSVReader(new FileReader(srcFile), ':');
				List resultList = csvReader.readAll();
				Iterator resultIter = resultList.iterator();
				String[] line = null;
				while (resultIter.hasNext()) {
					line = (String[])resultIter.next();
					if (line.length >= 3 && !line[0].startsWith("Examining ")) {
						extractLineData(line, result);
					}
				}
			}
			catch (FileNotFoundException e) {
				log("Error reading flawfinder result file: " + e.toString());
			}
			catch (IOException e) {
				log("Error reading flawfinder result file: " + e.toString());
			}
		}
		return result;
    }
    
    private void extractLineData(String[] line, Map existingData) {
    	int columnIdx = 0;
    	String filePath = line[columnIdx++];
        if (OS_NAME.compareToIgnoreCase("WINDOWS") >= 0) {
    		filePath += ':' + line[columnIdx++];
    	}
    	String fileName = getFileName(getRelativePath(filePath, srcDirPath));
    	String lineNumber = line[columnIdx++];
    	String sevCatFunc = line[columnIdx++];
    	
    	String metricName = null;

    	// sevCatFunc format: [severity] (category) function
    	Pattern p = Pattern.compile("\\[([0-9])\\]\\s*\\(([a-z]+)\\)\\s*([a-z]+)");
        Matcher m = p.matcher(sevCatFunc);
        if (m.find() && m.groupCount() == 3) {
        	String severity = m.group(1);
        	String category = m.group(2);
        	String functionName = m.group(3);
        	metricName = severity + '_' + category + '_' + functionName;
        }
        
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
        		metricBean.setId(metricName.toUpperCase());
        		metricMap.put(metricName, metricBean);
        	}
    		metricBean.incValue();
    		metricBean.addLine(lineNumber);
        }
    }
    
}

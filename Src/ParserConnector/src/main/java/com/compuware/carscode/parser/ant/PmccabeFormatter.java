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
public class PmccabeFormatter extends AbstractFormatter {

    private String columnHeader = null;
    private char separator = '\t';
    private String srcPath = null;

    /**
     * @param columnHeader The columnHeader to set.
     */
    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    /**
     * @param separator The separator to set.
     */
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    /**
     * @param srcPath The source directory path to set.
     */
    public void setSrcPath(String srcPath) {
        if (srcPath != null) {
            this.srcPath = srcPath.replaceAll("\\\\", "/");
        }
    }

    protected void checkParameters() throws BuildException {
        if (columnHeader == null || columnHeader.length() < 1) {
            throw new BuildException("A valid column header is required.", getLocation());
        }
    }

    protected Map extractData() {
        Map result = new HashMap();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(srcFile), separator);
            List resultList = csvReader.readAll();
            Iterator resultIter = resultList.iterator();
            String[] line = null;
            String[] columnNames = columnHeader.split(";");
            IElementBean currentElt = null;
            while (resultIter.hasNext()) {
                line = (String[]) resultIter.next();
                if (line.length >= columnNames.length) {
                    currentElt = extractLineData(line, columnNames);
                    result.put(currentElt.getDescElt(), currentElt);

                    // if it was a method
                    if (currentElt.getDescElt().contains("::")) {
                        // create/update parent class
                        IElementBean classElt = new ElementBean();
                        classElt.setDescElt(currentElt.getDescElt().substring(0, currentElt.getDescElt().lastIndexOf("::")));
                        classElt.setDescParent(getFileName(currentElt.getFilePath()));
                        classElt.setTypeElt("CLS");

                        result.put(classElt.getDescElt(), classElt);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            throw new BuildException("A valid source file is required.", getLocation());
        } catch (IOException e) {
            throw new BuildException("Error reading the source file: " + e.toString(), getLocation());
        }
        return result;
    }

    private IElementBean extractLineData(String[] line, String[] columnNames) {

        IElementBean result = null;
        Map metricMap = new HashMap();
        int columnIdx = 0;
        String metricName = null;
        IMetricBean met = null;
        for (int i = 0; i < columnNames.length - 1; i++) {
            // get the metric name from the given column header
            metricName = "PMCCABE_" + columnNames[i].toUpperCase();
            // Find the next non empty value
            columnIdx = skipToNextValue(line, columnIdx);
            String value = line[columnIdx++];
            if (value != null) {
                met = new MetricBean();
                met.setId(metricName);
                met.setValue(Double.parseDouble(value));
                metricMap.put(met.getId(), met);
            }
        }

        columnIdx = skipToNextValue(line, columnIdx);
        String fileInfo = line[columnIdx++];

        if (fileInfo != null) {
            String filePath = null;
            String fileName = null;
            result = new ElementBean();

            // looking for something like : filename(linenumber): functionName
            Pattern p = Pattern.compile("(.*)\\((.*)\\):\\s*(.*)");
            Matcher m = p.matcher(fileInfo);
            if (m.find() && m.groupCount() == 3) {
                filePath = getRelativePath(m.group(1), srcPath);

                fileName = getFileName(filePath);
                String functionName = m.group(3);
                functionName = functionName.replaceAll("&", "&amp;");
                functionName = functionName.replaceAll("<", "&lt;");
                functionName = functionName.replaceAll(">", "&gt;");
                result.setDescElt(fileName + '.' + functionName);

                // is it a method ?
                if (functionName.contains("::")) {
                    // it's parent is the class
                    result.setDescParent(fileName + '.' + functionName.substring(0, functionName.lastIndexOf("::")));
                } else {
                    //it's a function; its parent is the file
                    result.setDescParent(fileName);
                }

                result.setTypeElt("MET");
            } else {
                filePath = getRelativePath(fileInfo.trim(), srcPath);
                fileName = getFileName(filePath);
                //result = new ElementBean();
                result.setDescElt(fileName);
                result.setTypeElt("FILE");
            }
            result.setFilePath(filePath);
            result.setMetricMap(metricMap);
        }
        return result;
    }

    private int skipToNextValue(String[] line, int currentIdx) {
        int result = currentIdx;
        String value = null;
        // Find the next non empty value
        while ((result < line.length) && (value == null || value.length() < 1)) {
            value = line[result++];
        }
        return result - 1;
    }
}

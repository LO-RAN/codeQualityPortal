/**
 * 
 */
package com.compuware.carscode.parser.ant;

import au.com.bytecode.opencsv.CSVReader;
import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;
import com.compuware.carscode.parser.bean.IMetricBean;
import com.compuware.carscode.parser.bean.MetricBean;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;

/**
 * @author cwfr-lizac
 *
 */
public class CodeAnalyzerProFormatter extends AbstractFormatter {

    private String columnHeader = null;
    private char separator = ',';
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
            // skip the first 2 header lines
            resultIter.next();
            resultIter.next();
            while (resultIter.hasNext()) {
                line = (String[]) resultIter.next();
                // skip lines concerning "unknown" files
                if (line.length >= 2) {
                    currentElt = extractLineData(line, columnNames);
                    if (currentElt != null) {
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
            }
        } catch (FileNotFoundException e) {
            throw new BuildException("A valid source file is required.", getLocation());
        } catch (IOException e) {
            throw new BuildException("Error reading the source file: " + e.toString(), getLocation());
        }
        return result;
    }

    private IElementBean extractLineData(String[] line, String[] columnNames) {
        // skip non files (namely "Dir" lines)
        if (line[1].equals("C++")) {

            // first column contains the absolute file path
            String filePath = getRelativePath(line[0].trim(), srcPath);
            String fileName = getFileName(filePath);
            String fnName = line[2];
            // cope with potential "xml breaking" characters
            fnName = fnName.replaceAll("&", "&amp;");
            fnName = fnName.replaceAll("<", "&lt;");
            fnName = fnName.replaceAll(">", "&gt;");

            IElementBean result = new ElementBean();

            result.setFilePath(filePath);
            // if there is no function name
            if ("".equals(fnName)) {
                // we're dealing with the file element
                result.setDescElt(fileName);
                result.setTypeElt("FILE");
            } else {
                result.setDescElt(fileName + '.' + fnName);
                result.setTypeElt("MET");

                // is it a method ?
                if (fnName.contains("::")) {
                    // it's parent is the class
                    result.setDescParent(fileName + '.' + fnName.substring(0, fnName.lastIndexOf("::")));
                } else {
                    //it's a function; its parent is the file
                    result.setDescParent(fileName);
                }
            }

            Map metricMap = new HashMap();
            String metricName = null;
            IMetricBean met = null;

            for (int i = 3; i < line.length; i++) {
                // get the metric name from the given column header
                metricName = "CAP_" + columnNames[i].toUpperCase();
                // Find the next non empty value
                String value = line[i];
                if (value != null) {
                    met = new MetricBean();
                    met.setId(metricName);
                    // if is is a percentage
                    if (value.contains("%")) {
                        // consider only the numeric value
                        value = value.substring(0, value.indexOf("%"));
                    }
                    met.setValue(Double.parseDouble(value));
                    metricMap.put(met.getId(), met);
                }
            }




            result.setMetricMap(metricMap);

            return result;
        } else {
            return null;
        }
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

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
public class RestNatFormatter extends AbstractFormatter {

    private String columnHeader = null;
    private char separator = ',';
    private int firstMetricColumn = 3;

    /**
     * @param columnHeader The columnHeader to set.
     */
    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    /**
     * @param firstMetricColumn The first Metric Column to set.
     */
    public void setFirstMetricColumn(String column) {
        this.firstMetricColumn = Integer.parseInt(column);
    }

    /**
     * @param separator The separator to set.
     */
    public void setSeparator(char separator) {
        this.separator = separator;
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
            // skip the first (header) line
            resultIter.next();
            while (resultIter.hasNext()) {
                line = (String[]) resultIter.next();
                currentElt = extractLineData(line, columnNames);
                if (currentElt != null) {
                    result.put(currentElt.getDescElt(), currentElt);
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

        IElementBean result = new ElementBean();

        String name = line[0] + "." + line[1];
        // if we are dealing with units
        if (firstMetricColumn > 3) {
            result.setDescElt(name + "." + line[2].replaceAll("\\.", "_"));
            result.setTypeElt("UNIT");
            result.setDescParent(name);
            result.setFilePath(result.getDescParent() + ".natural");
        } else { // objects
            result.setDescElt(name);
            result.setTypeElt("OBJECT");
            result.setFilePath(result.getDescElt() + ".natural");
        }


        Map metricMap = new HashMap();
        String metricName = null;
        IMetricBean met = null;

        for (int i = firstMetricColumn; i < (line.length-1); i++) {
            // get the metric name from the given column header
            metricName = "NAT_" + columnNames[i].toUpperCase();
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
    }
}

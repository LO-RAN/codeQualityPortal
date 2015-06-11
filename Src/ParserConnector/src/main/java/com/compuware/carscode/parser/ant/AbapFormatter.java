/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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
public class AbapFormatter extends AbstractFormatter {

    protected void checkParameters() throws BuildException {
    }

    protected Map extractData() {
        Map result = new HashMap();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(srcFile), ';');
            List resultList = csvReader.readAll();
            Iterator resultIter = resultList.iterator();
            if (resultIter.hasNext()) {
                // Skip the header line...
                resultIter.next();
            }
            String[] line = null;
            while (resultIter.hasNext()) {
                line = (String[]) resultIter.next();
                if (line != null && line.length >= 8 && !line[0].startsWith("Examining ")) {
                    extractLineData(line, result);
                } else {
                    log("Cannot format line: " + Arrays.deepToString(line));
                }
            }
        } catch (FileNotFoundException e) {
            throw new BuildException("A valid source file is required.", getLocation());
        } catch (IOException e) {
            throw new BuildException("Error reading the source file: " + e.toString(), getLocation());
        }
        return result;
    }
    private static final int METRIC_IDX = 0;
    private static final int OBJECTNAME_IDX = 5;
    private static final int ALTOBJECTNAME_IDX = 3;
    private static final int OBJECTTYPE_IDX = 4;
    private static final int PARENTOBJECTNAME_IDX = 1;
    private static final int LINE_IDX = 6;

    private void extractLineData(String[] line, Map existingData) {
        String metricName = line[METRIC_IDX];
        String objectName = line[OBJECTNAME_IDX];
        String objectType = getObjectType(line[OBJECTTYPE_IDX]);
        String lineNumber = line[LINE_IDX];
        String parentName = line[PARENTOBJECTNAME_IDX];

        if (objectName == null || objectName.length() == 0) {
            objectName = line[ALTOBJECTNAME_IDX];
        }

        String currentEltDesc = parentName != null ? parentName + '.' + objectName : objectName;

        IElementBean currentElement = (IElementBean) existingData.get(currentEltDesc);
        if (currentElement == null) {
            currentElement = new ElementBean();
            currentElement.setDescElt(currentEltDesc);
            currentElement.setTypeElt(objectType);
            if (parentName != null) {
                currentElement.setDescParent(parentName);
                // create the parent if it does not exist yet
                IElementBean parentElement = (IElementBean) existingData.get(parentName);
                if (parentElement == null) {
                    parentElement = new ElementBean();
                    parentElement.setDescElt(parentName);
                    parentElement.setTypeElt(objectType);
                    parentElement.setMetricMap(new HashMap());
                    existingData.put(parentElement.getDescElt(), parentElement);
                }
            }
            currentElement.setMetricMap(new HashMap());
            existingData.put(currentElement.getDescElt(), currentElement);
        }
        if (metricName != null && metricName.trim().length() > 0) {
            metricName = metricName.toUpperCase();
            // Getting element metrics
            Map metricMap = currentElement.getMetricMap();
            // Retrieving already existing metric with the given name
            IMetricBean metricBean = (IMetricBean) metricMap.get(metricName);
            if (metricBean == null) {
                // No metric exists for the given name in the current element,
                // Create it and add it in the element metrics.
                metricBean = new MetricBean();
                metricBean.setId(metricName);
                metricMap.put(metricName, metricBean);
            }
            // Increment the number of violation for the given metric.
            metricBean.incValue();
            // Add the violation line number.
            metricBean.addLine(lineNumber);
        }

    }
    private static final Map TYPE_ASSOC;

    static {
        TYPE_ASSOC = new HashMap();
        TYPE_ASSOC.put("CLAS", "CLS");
        TYPE_ASSOC.put("FUGR", "CLS");
        TYPE_ASSOC.put("PROG", "PROG");
        TYPE_ASSOC.put("METH", "MET");
    	TYPE_ASSOC.put("DYNP", "PROG");
    }

    private String getObjectType(String objectType) {
        String result = "CLS";
        if (objectType != null) {
            result = (String) TYPE_ASSOC.get(objectType);
        }
        return result;
    }
}

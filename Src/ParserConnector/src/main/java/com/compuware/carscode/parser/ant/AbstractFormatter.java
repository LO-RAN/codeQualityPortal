/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;
import com.compuware.carscode.parser.bean.IMetricBean;
import com.compuware.carscode.parser.bean.MetricBean;
import com.compuware.carscode.parser.writer.ElementMetricWriter;
import com.compuware.carscode.parser.writer.XmlWriter;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class AbstractFormatter extends Task {

    protected File srcFile = null;
    protected File destFile = null;

    /**
     * @param srcFile The srcFile to set.
     */
    public void setSrcFile(String srcFile) {
        this.srcFile = new File(srcFile);
    }

    /**
     * @param destFile The destFile to set.
     */
    public void setDestFile(String destFile) {
        this.destFile = new File(destFile);
    }

    public void execute() throws BuildException {
        checkCommonParameters();
        checkParameters();
        processFormat();
    }

    protected void checkCommonParameters() throws BuildException {
        if (srcFile == null || srcFile.isDirectory() || !srcFile.exists()) {
            throw new BuildException("A valid source file is required.", getLocation());
        }
        if (destFile == null || destFile.isDirectory()) {
            throw new BuildException("A valid destination file is required.", getLocation());
        }
    }

    protected abstract void checkParameters() throws BuildException;

    protected void processFormat() throws BuildException {
        if (this.srcFile != null) {
            log("Formatting file " + this.srcFile.getAbsolutePath());
        }
        Map data = extractData();
        ElementMetricWriter xmlWriter = new XmlWriter();
        try {
            xmlWriter.print(data, destFile);
        } catch (IOException e) {
            throw new BuildException("Error during xml writing:" + e.toString(), getLocation());
        }
    }

    protected abstract Map extractData();

    protected String getFileName(String filePath) {
        String result = null;
        if (filePath != null) {
            result = filePath;
            result = result.replaceAll("\\\\", "/");
            result = result.replaceAll("\\.", "_");
            result = result.replaceAll("/", ".");
        }
        return result;
    }

    protected String getRelativePath(String filePath, String srcDir) {
        String result = filePath.replaceAll("\\\\", "/");
        result = result.replace(srcDir, "");
        if (result.startsWith("/")) {
            result = result.substring(1);
        }
        return result;
    }

    protected String formatId(String name) {
        String result = name;
        if (result != null) {
            result = result.trim();
            result = result.replaceAll("/", "");
            result = result.replaceAll(" ", "_");
            result = result.replaceAll("\\_+", "_");
            result = result.replaceAll("\\.", "");
            result = result.replaceAll("\\(", "");
            result = result.replaceAll("\\)", "");
            result = result.replaceAll("%", "PCT");
            result = result.toUpperCase();
        }
        return result;
    }

    protected IElementBean getElementOrCreateIt(String name, Map elementMap, String parent, String idTelt) {
        IElementBean result = null;
        if (elementMap != null) {
            if (elementMap.containsKey(name)) {
                result = (IElementBean) elementMap.get(name);
            } else {
                result = new ElementBean();
                result.setDescElt(name);
                result.setTypeElt(idTelt);
                result.setDescParent(parent);
                elementMap.put(name, result);
            }
        }
        return result;
    }

    protected void addMetric(String name, Map metricMap, double value, String line) {
        IMetricBean currentMetric = null;
        if (metricMap != null) {
            if (metricMap.containsKey(name)) {
                currentMetric = (IMetricBean) metricMap.get(name);
                currentMetric.incValue();
                currentMetric.addLine(line);
            } else {
                currentMetric = new MetricBean();
                currentMetric.setId(name.toUpperCase());
                currentMetric.setValue(value);
                currentMetric.setLines(line);
                metricMap.put(name, currentMetric);
            }
        }
    }

    protected void addMetric(String name, Map metricMap, String value, String line) {
        if (value != null && value.matches("[0-9,\\.]+")) {
            double doubleValue = Double.parseDouble(value);
            addMetric(name, metricMap, doubleValue, line);
        }
    }

    protected List getFileList(List srcFilesets) {
        List result = new ArrayList();
        Iterator srcIterator = srcFilesets.iterator();
        FileSet fs = null;

        while (srcIterator.hasNext()) {
            fs = (FileSet) srcIterator.next();
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            String[] includedFiles = null;
            includedFiles = ds.getIncludedFiles();

            File base = ds.getBasedir();
            for (int i = 0; i < includedFiles.length; i++) {
                result.add(new File(base, includedFiles[i]));
            }
        }
        return result;
    }
}

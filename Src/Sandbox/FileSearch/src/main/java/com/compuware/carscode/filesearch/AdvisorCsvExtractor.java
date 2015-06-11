package com.compuware.carscode.filesearch;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXException;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import com.compuware.carscode.filesearch.bean.ViolationBean;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 16 févr. 2006
 * Time: 17:19:24
 * To change this template use File | Settings | File Templates.
 */
public class AdvisorCsvExtractor {

    private static final String VIOLATION_TAG = "violation";
    private static final String FILENAME_PROPERTY = "file";
    private static final String DESCRIPTION_TAG = "description";
    private static final String ERROR_LINE_PROPERTY = "line";

    private String sourceError;
    private String baseDir;
    private String errorLib;

    public AdvisorCsvExtractor(String baseDir, String sourceError, String errorLib) {
        this.sourceError = sourceError;
        this.errorLib = errorLib;
        this.baseDir = baseDir.replaceAll("/", ".");
        this.baseDir = this.baseDir.replaceAll("\\\\", ".");
        if (!this.baseDir.endsWith(".")) {
            this.baseDir = this.baseDir + ".";
        }
    }

    public void setSourceError(String sourceError) {
        this.sourceError = sourceError;
    }

    public String getErrorLib() {
        return errorLib;
    }

    public void setErrorLib(String errorLib) {
        this.errorLib = errorLib;
    }

    /**
     * Parse the xml file to extract an Xml document.
     * @param file the xml file path.
     * @return the Xml document.
     */
    private Document parseDefinition(String file) {
        Document xmlDocument = null;
        DOMParser usageParser= new DOMParser();
        try {
            usageParser.parse(file);
            xmlDocument = usageParser.getDocument();
        }
        catch (SAXException e) {
            System.out.println(e.getMessage());
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
        return xmlDocument;
    }

    private void extract(Node file, HashMap violations) {
        NamedNodeMap fattributes = file.getAttributes();
        String fileName = (String) fattributes.getNamedItem(FILENAME_PROPERTY).getNodeValue();
        String line = (String) fattributes.getNamedItem(ERROR_LINE_PROPERTY).getNodeValue();
        String description = (String) fattributes.getNamedItem(DESCRIPTION_TAG).getNodeValue();
        if (description.startsWith(sourceError)) {
            fileName = fileName.replaceAll("/", ".");
            fileName = fileName.replaceAll("\\\\", ".");
            fileName = fileName.substring(baseDir.length());
            ViolationBean violationBean = (ViolationBean)violations.get(fileName);
            if (violationBean == null) {
                violationBean = new ViolationBean(fileName);
                violations.put(fileName, violationBean);
            }
            violationBean.addViolation(line);
        }
    }

    /**
     * Initialize the the parser from an xml config file.
     * @param filePath the xml config file path.
     */
    public void extract(String filePath, PrintWriter out) throws IOException {
        Document xmlDocument = parseDefinition(filePath);
        HashMap violations = new HashMap();
        if (xmlDocument != null) {
            NodeList fileList = xmlDocument.getElementsByTagName(VIOLATION_TAG);
            if (fileList != null) {
                for (int i = 0; i < fileList.getLength(); i++) {
                    Node file = fileList.item(i);
                    extract(file, violations);
                }
            }
        }
        printViolations(violations, out);
    }

    private void printViolations(HashMap violations, PrintWriter out) {
        Set violationSet = violations.keySet();
        Iterator i = violationSet.iterator();
        while (i.hasNext()) {
            String key = (String)i.next();
            ViolationBean violationBean = (ViolationBean)violations.get(key);
            violationBean.print(errorLib, out);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: AdvisorCsvExtractor xmlPath baseDir srcName errorLib outputFile");
        }

        AdvisorCsvExtractor extractor = new AdvisorCsvExtractor(args[1],args[2],args[3]);
        PrintWriter writer = null;
        File out = new File(args[4]);
        writer = new PrintWriter(new FileWriter(out));
        extractor.extract(args[0], writer);
        writer.flush();
    }
}

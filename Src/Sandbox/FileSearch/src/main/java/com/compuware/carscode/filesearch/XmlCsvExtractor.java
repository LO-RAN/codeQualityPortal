package com.compuware.carscode.filesearch;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 15 févr. 2006
 * Time: 15:01:41
 * To change this template use File | Settings | File Templates.
 */
public class XmlCsvExtractor {
    private static final String FILE_TAG = "file";
    private static final String FILENAME_PROPERTY = "name";
    private static final String ERROR_LINE_PROPERTY = "line";
    private static final String ERROR_SOURCE_PROPERTY = "source";

    private String sourceError;
    private String baseDir;
    private String errorLib;

    public XmlCsvExtractor(String baseDir, String sourceError, String errorLib) {
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

    private void extract(Node file, PrintWriter out) {
        NamedNodeMap fattributes = file.getAttributes();
        String fileName = (String) fattributes.getNamedItem(FILENAME_PROPERTY).getNodeValue();
        NodeList errorList = file.getChildNodes();
        StringBuffer result = new StringBuffer();
        int idx = 0;
        if (errorList != null) {
            for (int i = 0; i < errorList.getLength(); i++) {
                Node error = errorList.item(i);
                NamedNodeMap attributes = error.getAttributes();
                if (attributes != null) {
                    String currentSourceError = (String) attributes.getNamedItem(ERROR_SOURCE_PROPERTY).getNodeValue();
                    String line = (String) attributes.getNamedItem(ERROR_LINE_PROPERTY).getNodeValue();
                    if (currentSourceError.endsWith(sourceError)) {
                        if (idx > 0) {
                            result.append(",");
                        }
                        result.append(line);
                        idx++;
                    }
                }
            }
            if (idx > 0) {
                result.append(";").append(errorLib).append(";").append(idx);
                fileName = fileName.replaceAll("/", ".");
                fileName = fileName.replaceAll("\\\\", ".");
                result.insert(0, " line ");
                result.insert(0, fileName.substring(baseDir.length()));
                out.println(result.toString());
            }
        }
    }

    /**
     * Initialize the the parser from an xml config file.
     * @param filePath the xml config file path.
     */
    public void extract(String filePath, PrintWriter out) throws IOException {
        Document xmlDocument = parseDefinition(filePath);
        if (xmlDocument != null) {
            NodeList fileList = xmlDocument.getElementsByTagName(FILE_TAG);
            if (fileList != null) {
                for (int i = 0; i < fileList.getLength(); i++) {
                    Node file = fileList.item(i);
                    extract(file, out);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: XmlCsvExtractor xmlPath baseDir srcName errorLib outputFile");
        }

        XmlCsvExtractor extractor = new XmlCsvExtractor(args[1],args[2],args[3]);
        PrintWriter writer = null;
        File out = new File(args[4]);
        writer = new PrintWriter(new FileWriter(out));
        extractor.extract(args[0], writer);
        writer.flush();
    }

}

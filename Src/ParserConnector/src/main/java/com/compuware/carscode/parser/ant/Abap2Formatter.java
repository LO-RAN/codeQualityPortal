/**
 * 
 */
package com.compuware.carscode.parser.ant;

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;
import com.compuware.carscode.parser.bean.IMetricBean;
import com.compuware.carscode.parser.bean.MetricBean;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * @author cwfr-lizac
 *
 */
public class Abap2Formatter extends AbstractFormatter {

    private static final String ELEMENT_TAG = "elt";
    private static final String METRIC_TAG = "met";
    private static final String SOURCE_TAG = "source";
    private static final String NAME_ATTR = "name";
    private static final String SUBNAME_ATTR = "ssobjname";
    private static final String TYPE_ATTR = "ssobjtype";
    private static final String LINE_ATTR = "line";
    private static final String ID_ATTR = "id";
    private static final String CODE_ATTR = "cpde";
    private static final String SRC_EXT = ".abap";
    private String srcPath = null;

    /**
     * @param srcPath The source directory path to set.
     */
    public void setSrcPath(String srcPath) {
        if (srcPath != null) {
            this.srcPath = srcPath.replaceAll("\\\\", "/");
        }
    }

    protected void checkParameters() throws BuildException {
    }

    protected Map extractData() {
        return extractData(srcFile);
    }

    protected Map extractData(File src) {
        Map result = null;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        log("Extracting " + src.getAbsolutePath());
        try {
            SAXParser parser = factory.newSAXParser();

            XmlFileHandler manager = new XmlFileHandler(srcPath);
            parser.parse(src, manager);
            result = manager.getData();
        } catch (ParserConfigurationException e) {
            log("Abap : consolidateResults : " + e);
        } catch (SAXException e) {
            log("Abap : consolidateResults : " + e);
        } catch (IOException e) {
            log("Abap : consolidateResults : " + e);
        }
        return result;
    }

    private class XmlFileHandler extends DefaultHandler {

        private Map data = new HashMap();
        private IElementBean currentElt = null;
        private String srcPath = null;
        private String currentElementName = null;
        private File codeFile = null;
        private FileWriter fstream = null;
        private BufferedWriter out = null;

        private boolean isSrc=false;

        public XmlFileHandler(String srcPath) {
            this.srcPath = srcPath;
        }

        public Map getData() {
            return this.data;
        }

        public void startDocument() throws SAXException {
        }

        public void endDocument() throws SAXException {
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (ELEMENT_TAG.equals(qName) && (attributes != null)) {
                if (attributes != null) {
                    currentElementName = attributes.getValue(NAME_ATTR);
                }
            } else if (METRIC_TAG.equals(qName)) {
                createMetric(attributes);
            } else if (SOURCE_TAG.equals(qName)) {
                isSrc=true;

                codeFile = new File(srcPath + File.separator + File.separator + currentElementName + SRC_EXT);
                try {
                    fstream = new FileWriter(codeFile);
                    out = new BufferedWriter(fstream);
                } catch (IOException ex) {
                    log("Abap : Creating source file : " + ex);
                }
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (SOURCE_TAG.equals(qName)) {
                isSrc=false;
                try {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException ex) {
                    log("Abap : Closing source file : " + ex);
                }

            }
        }

        // we expect the source code to be included in CDATA
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (isSrc) {
                String cdata = new String(ch, start, length);
                try {
                    if (null != out) {
                        out.write(cdata);
                    }
                } catch (IOException ex) {
                    log("Abap : Writing to source file : " + ex);
                }
            }

        }

        private void createMetric(Attributes attributes) {
            if (attributes != null) {

                String name = attributes.getValue(NAME_ATTR);
                if (attributes.getValue(SUBNAME_ATTR).equals(name)) {

                    String metricId = attributes.getValue(ID_ATTR) + "_" + attributes.getValue(CODE_ATTR);
                    String metricLine = attributes.getValue(LINE_ATTR);

                    IElementBean currentElement = (IElementBean) data.get(name);

                    if (currentElement == null) {
                        currentElement = new ElementBean();
                        currentElement.setDescElt(name);
                        currentElement.setFilePath(name + SRC_EXT);
                        currentElement.setTypeElt(attributes.getValue(TYPE_ATTR));
                        currentElement.setMetricMap(new HashMap());
                        data.put(currentElement.getDescElt(), currentElement);
                    }

                    Map metricMap = currentElement.getMetricMap();
                    IMetricBean metricBean = (IMetricBean) metricMap.get(metricId);
                    if (metricBean == null) {
                        metricBean = new MetricBean();
                        metricBean.setId(metricId.toUpperCase());
                        metricMap.put(metricId, metricBean);
                    }
                    metricBean.incValue();
                    metricBean.addLine(metricLine);
                }
            }
        }
    }
}

/**
 * 
 */
package com.compuware.carscode.parser.ant;

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;
import com.compuware.carscode.parser.bean.IMetricBean;
import com.compuware.carscode.parser.bean.MetricBean;
import java.io.File;
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
public class FlexPMDFormatter extends AbstractFormatter {

    private static final String SOURCE_TAG = "violation";
    private static final String CLASS_PATH_ATTR = "class";
    private static final String PACKAGE_PATH_ATTR = "package";
    private static final String PROBLEM_ID_ATTR = "rule";
    private static final String PROBLEM_LINE_ATTR = "beginline";
    private static final String PREFIX = "FLEX_";
    private String srcDirPath = null;

    public void setSrcDirPath(String srcDirPath) {
        this.srcDirPath = srcDirPath.replaceAll("\\\\", "/");
    }

    protected void checkParameters() throws BuildException {
        if (srcDirPath == null || srcDirPath.length() < 1) {
            throw new BuildException("A valid source directory path is required.", getLocation());
        }
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

            XmlFileHandler manager = new XmlFileHandler(srcDirPath);
            parser.parse(src, manager);
            result = manager.getData();
        } catch (ParserConfigurationException e) {
            log("FlexPMD : consolidateResults : " + e);
        } catch (SAXException e) {
            log("FlexPMD : consolidateResults : " + e);
        } catch (IOException e) {
            log("FlexPMD : consolidateResults : " + e);
        }
        return result;
    }

    private class XmlFileHandler extends DefaultHandler {

        private Map data = new HashMap();
        private IElementBean currentElt = null;
        private String srcPath = null;

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
            if (SOURCE_TAG.equals(qName) && (attributes != null)) {
                createElement(attributes);
            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        private void createElement(Attributes attributes) {
            if (attributes != null) {
                String sourcePath = attributes.getValue(PACKAGE_PATH_ATTR)+"/"+attributes.getValue(CLASS_PATH_ATTR);
                if (!"".equals(sourcePath)) {
                    String filePath = getRelativePath(sourcePath, srcPath);
                    String fileName = getFileName(filePath).replaceFirst("_.*", "");

                    String metricId = attributes.getValue(PROBLEM_ID_ATTR);
                    int pos=metricId.lastIndexOf(".");
                    if(pos>=0 && (pos+1)<metricId.length()){
                        metricId=metricId.substring(pos+1);
                        }
                    metricId=PREFIX + metricId;

                    String metricLine = attributes.getValue(PROBLEM_LINE_ATTR);

                    IElementBean currentElement = (IElementBean) data.get(fileName);
                    if (currentElement == null) {
                        currentElement = new ElementBean();
                        currentElement.setDescElt(fileName);
	    		currentElement.setFilePath(filePath);
                        currentElement.setTypeElt("CLS");
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

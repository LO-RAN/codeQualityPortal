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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author cwfr-lizac
 *
 */
public class RestNat2Formatter extends AbstractFormatter {

    private static final String SRC_TAG = "SRC";
    private static final String QUNI_TAG = "QUNI";
    private static final String ANO_TAG = "ANO";
    private static final String BLOCK_TAG = "BLOCK";
    private static final String QOBJ_ATTR = "QOBJ";
    private static final String NAME_ATTR = "NAME";
    private static final String TYPE_ATTR = "TYPE";
    private static final String UNI_ATTR = "UNI";
    private static final String N_ATTR = "N";
    private static final String LB_ATTR = "LB";
    private static final String V_ATTR = "V";
    private static final String PREFIX = "NAT_";
    private String libraryName = null;
    private String srcDirPath = null;

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public void setSrcDirPath(String srcDirPath) {
        this.srcDirPath = srcDirPath.replaceAll("\\\\", "/");
    }

    protected void checkParameters() throws BuildException {
        if (libraryName == null || libraryName.length() < 1) {
            throw new BuildException("A valid library name is required.", getLocation());
        }
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

            RestNat2Formatter.XmlFileHandler manager = new RestNat2Formatter.XmlFileHandler(srcDirPath);
            parser.parse(src, manager);
            result = manager.getData();
        } catch (ParserConfigurationException e) {
            log("RestNat : consolidateResults : " + e);
        } catch (SAXException e) {
            log("RestNat : consolidateResults : " + e);
        } catch (IOException e) {
            log("RestNat : consolidateResults : " + e);
        }
        return result;
    }

    private class XmlFileHandler extends DefaultHandler {

        private Map data = new HashMap();
        private IElementBean currentObject = null;
        private IElementBean currentUnit = null;
        private IMetricBean currentAno = null;
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
            if (SRC_TAG.equals(qName) && (attributes.getValue(NAME_ATTR) != null)) {
                processObject(attributes);
            } else if (SRC_TAG.equals(qName) && (attributes.getValue(QOBJ_ATTR) != null)) {
                processObjectMetric(attributes);
            } else if (QUNI_TAG.equals(qName)) {
                processUnit(attributes);
            } else if (ANO_TAG.equals(qName) && (attributes.getValue(N_ATTR) != null)) {
                processObjectAno(attributes);
            } else if (BLOCK_TAG.equals(qName)) {
                processBlock(attributes);
            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        private void processObject(Attributes attributes) {
            // process only actual code objects (those with type P, N, S or H)
            // and ignore others (data objects)
            if ("PNSH".indexOf(attributes.getValue(TYPE_ATTR)) >= 0) {
                String objectName = libraryName + "." + attributes.getValue(NAME_ATTR);
                String sourcePath = attributes.getValue(NAME_ATTR) + ".NS" + attributes.getValue(TYPE_ATTR);
                String filePath = libraryName + "/" + getRelativePath(sourcePath, srcPath);
                currentObject = (IElementBean) data.get(objectName);
                if (currentObject == null) {
                    currentObject = new ElementBean();
                    currentObject.setDescElt(objectName);
                    currentObject.setFilePath(filePath);
                    currentObject.setTypeElt("OBJECT");
                    currentObject.setMetricMap(new HashMap());
                    data.put(objectName, currentObject);
                }
            }
        }

        private void processObjectMetric(Attributes attributes) {

            String metricId = formatId(attributes.getValue(QOBJ_ATTR));
            metricId = PREFIX + metricId;

            Map metricMap = currentObject.getMetricMap();
            IMetricBean metricBean = (IMetricBean) metricMap.get(metricId);
            if (metricBean == null) {
                metricBean = new MetricBean();
                metricBean.setId(metricId);
                metricBean.setValue(Double.parseDouble(attributes.getValue(V_ATTR)));
                metricMap.put(metricId, metricBean);
            }
        }

        private void processObjectAno(Attributes attributes) {

            String metricId = formatId(attributes.getValue(N_ATTR));
            metricId = PREFIX + metricId;

            Map metricMap = currentObject.getMetricMap();
            currentAno = (IMetricBean) metricMap.get(metricId);
            if (currentAno == null) {
                currentAno = new MetricBean();
                currentAno.setId(metricId);
                metricMap.put(metricId, currentAno);
            }
        }

        private void processBlock(Attributes attributes) {
            if (currentAno != null) {
                currentAno.addLine(attributes.getValue(LB_ATTR));
                currentAno.incValue();
            }
        }

        private void processUnit(Attributes attributes) {

            String unitName = currentObject.getDescElt() + "." + attributes.getValue(UNI_ATTR);

            currentUnit = (IElementBean) data.get(unitName);
            if (currentUnit == null) {
                currentUnit = new ElementBean();
                currentUnit.setDescElt(unitName);
                currentUnit.setFilePath(currentObject.getFilePath());
                currentUnit.setDescParent(currentObject.getDescElt());
                currentUnit.setTypeElt("UNIT");
                currentUnit.setMetricMap(new HashMap());
                data.put(unitName, currentUnit);
            }

            String metricId = formatId(attributes.getValue(N_ATTR));
            metricId = PREFIX + metricId;

            Map metricMap = currentUnit.getMetricMap();
            IMetricBean metricBean = (IMetricBean) metricMap.get(metricId);
            if (metricBean == null) {
                metricBean = new MetricBean();
                metricBean.setId(metricId);
                metricBean.setValue(Double.parseDouble(attributes.getValue(V_ATTR)));
                metricMap.put(metricId, metricBean);
            }
        }
    }
}

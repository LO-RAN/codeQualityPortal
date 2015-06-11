package com.compuware.caqs.business.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import org.apache.log4j.Logger;

public class AnalyseOptimalAdvisor {

    private Logger logger = null;
    public static final String XMLFILENAME = File.separator + "optimaladvisorreports" + File.separator + "report.xml";

    public AnalyseOptimalAdvisor(Logger logger) {
        this.logger = logger;
    }

    private void consolidateResults(File clsFile, final String srcPath) throws AnalysisException {
        SAXParserFactory fabrique = SAXParserFactory.newInstance();

        try {
            SAXParser parseur = fabrique.newSAXParser();
            File fichier = new File(clsFile.getAbsolutePath());

            XmlFileHandler gestionnaire = new XmlFileHandler();
            parseur.parse(fichier, gestionnaire);

            Map<String, ElementMetricsBean> classMap = gestionnaire.getClassMap();
            if (classMap != null) {
                writeResult(classMap.values(), srcPath);
            }
        } catch (ParserConfigurationException e) {
            logger.error("result consolidation",e);
            throw new AnalysisException("Error during OptimalAdvisor result consolidation", e);
        } catch (SAXException e) {
            logger.error("result consolidation",e);
            throw new AnalysisException("Error during OptimalAdvisor result consolidation", e);
        } catch (IOException e) {
            logger.error("result consolidation",e);
            throw new AnalysisException("Error during OptimalAdvisor result consolidation", e);
        }
    }

    private void writeResult(Collection<ElementMetricsBean> classColl, String reportPath) throws AnalysisException {
        BufferedWriter bw = null;
        try {
            File sortie = new File(reportPath + AnalyseOptimalAdvisor.XMLFILENAME);
            FileWriter fw = new FileWriter(sortie);
            bw = new BufferedWriter(fw);
            bw.write("<Result>");
            ElementMetricsBean cls = null;
            MetriqueBean met = null;
            Collection<MetriqueBean> metricColl = null;
            Iterator<MetriqueBean> metIt = null;
            Iterator<ElementMetricsBean> clsIt = classColl.iterator();
            while (clsIt.hasNext()) {
                cls = clsIt.next();
                bw.write("<elt type=\"CLS\" name=\"");
                bw.write(cls.getDesc() + "\">");
                bw.newLine();
                metricColl = cls.getMetricCollection();
                metIt = metricColl.iterator();
                while (metIt.hasNext()) {
                    met = (MetriqueBean) metIt.next();
                    bw.write("<metric id=\"");
                    bw.write(met.getId().toUpperCase());
                    bw.write("\" value=\"");
                    bw.write("" + met.getLines().size());
                    bw.write("\" lines=\"");
                    bw.write(met.getLinesAsString(',', Constants.MAX_LINES_SIZE));
                    bw.write("\" />");
                    bw.newLine();
                }
                bw.write("</elt>");
                bw.newLine();
            }
            bw.write("</Result>");
            bw.close();
            fw.close();
        } catch (IOException exc) {
            throw new AnalysisException("Error writing OptimalAdvisor consolidated results", exc);
        }
    }

    public void conlidate(String targetDirectory, String idDialecte) throws AnalysisException {
        File file = new File(targetDirectory + File.separator + Constants.OPTIMAL_ADVISOR_REPORT_DIR_KEY + File.separator + Constants.OPTIMAL_ADVISOR_REPORT_XML_FILE_NAME_KEY);
        //maintenant, on jdomise le tout.
        if (file.exists()) {
            consolidateResults(file, targetDirectory);
        } else {
            throw new AnalysisException("OptimalAdvisor file " + file.getAbsolutePath() + " not found");
        }
    }

    private class XmlFileHandler extends DefaultHandler {

        private static final String CLASS_TAG = "Class";
        // Microfocus CodeReview format
        private static final String VIOLATION_TAG = "violation";
        private static final String VIOLATION_DESCRIPTION = "Description";
        private static final String VIOLATION_LINENO = "LineNo";
        private ElementMetricsBean currentElementBean = null;
        private MetriqueBean currentMetric = null;
        private Map<String, ElementMetricsBean> classMap = new HashMap<String, ElementMetricsBean>();
        private String currentQName = null;
        private String currentMetricName = "";
        private String currentViolationLine = "";
        boolean lookForData = false;

        public void startDocument() throws SAXException {
        }

        public void endDocument() throws SAXException {
        }

        public void startElement(String uri,
                String localName,
                String qName,
                Attributes attributes) throws SAXException {
            // Compuware OptimalAdvisor format (up to version 4.3)
            if (CLASS_TAG.equals(qName)) {
                currentElementBean = classMap.get(attributes.getValue("name"));
                if (currentElementBean == null) {
                    currentElementBean = new ElementMetricsBean();
                    currentElementBean.setDesc(attributes.getValue("name"));
                    classMap.put(currentElementBean.getDesc(), currentElementBean);
                }
            }

            // Microfocus CodeReview format (from version 4.5)
            if (VIOLATION_TAG.equals(qName)) {
                // check for the existence of a "class" attribute which exists only in the new format
                if (null !=attributes.getValue("class")) {
                    currentElementBean = classMap.get(attributes.getValue("class"));
                    if (currentElementBean == null) {
                        currentElementBean = new ElementMetricsBean();
                        currentElementBean.setDesc(attributes.getValue("class"));
                        classMap.put(currentElementBean.getDesc(), currentElementBean);
                    }
                    currentMetricName = extractMetricName(attributes.getValue("description"));
                    MetriqueBean met = currentElementBean.getMetrique(currentMetricName);
                    if (met == null) {
                        met = new MetriqueBean();
                        met.setId(currentMetricName);
                        currentElementBean.addMetrique(met);
                    }
                    met.addLine(attributes.getValue("line"));
                }
            } else if (VIOLATION_DESCRIPTION.equals(qName)) {
                currentQName = qName;
                lookForData = true;
            } else if (VIOLATION_LINENO.equals(qName)) {
                currentQName = qName;
                lookForData = true;
            }
        }

        public void endElement(String uri,
                String localName,
                String qName) throws SAXException {
            if (VIOLATION_DESCRIPTION.equals(this.currentQName)) {
                if (currentMetricName != null) {
                    MetriqueBean met = currentElementBean.getMetrique(currentMetricName);
                    if (met == null) {
                        met = new MetriqueBean();
                        met.setId(currentMetricName);
                        currentElementBean.addMetrique(met);
                    }
                    currentMetric = met;
                    currentMetricName = "";
                    lookForData = false;
                    currentQName = null;
                }
            }
            if (VIOLATION_LINENO.equals(this.currentQName)) {
                if (currentViolationLine != null) {
                    currentMetric.addLine(currentViolationLine);
                }
                currentViolationLine = "";
                lookForData = false;
                currentQName = null;
            }
        }

        public void characters(char[] text, int start, int length)
                throws SAXException {
            if (lookForData) {
                String data = new String(text, start, length);
                if (VIOLATION_DESCRIPTION.equals(this.currentQName)) {
                    String currentViolationDescription = data;
                    if (currentViolationDescription != null) {
                        currentMetricName = currentMetricName + extractMetricName(currentViolationDescription);
                    }
                    if (data.contains(":")) {
                        lookForData = false;
                    }
                } else if (VIOLATION_LINENO.equals(this.currentQName)) {
                    currentViolationLine = currentViolationLine + data;
                }
            }
        }

        private String extractMetricName(String value) {
            String result = null;
            int startIdx = 0;
            if (value != null && value.length() > 0) {
                if (value.startsWith("\"")) {
                    startIdx = 1;
                }
                if (value.contains(":")) {
                    result = value.substring(startIdx, value.indexOf(':'));
                } else {
                    result = value.substring(startIdx);
                }
            }
            return result;
        }

        public Map<String, ElementMetricsBean> getClassMap() {
            return this.classMap;
        }
    };
}

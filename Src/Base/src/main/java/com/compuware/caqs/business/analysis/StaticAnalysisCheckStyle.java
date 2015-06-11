/*
 * StaticAnalysisDevPartnerRuleByRule.java
 *
 * Created on July 13, 2004, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.StringUtils;

/**
 *
 * @author  cwfr-fxalbouy
 */
public class StaticAnalysisCheckStyle extends StaticAnalysisAntGeneric {

    private static final String XMLFILENAME = File.separator + "checkstylereports" + File.separator + "report.xml";
	
    /** Creates a new instance */
    public StaticAnalysisCheckStyle() {
        super() ;
        logger.info("Tool is CheckStyle");
    }
    
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysisAntGeneric#getXmlFileReportPath()
	 */
	@Override
	protected String getXmlFileReportPath() {
    	return XMLFILENAME;
	}

    protected void toolAnalysis(EA curEA) throws AnalysisException {
        logger.info("Start Checkstyle Analysis");
        super.toolAnalysis(curEA);
        
        if (isAnalysisPossible(curEA)) {
            //execute checkstyle cmd
            String checkstyleOutputFile = curEA.getTargetDirectory()+File.separator+"checkstylereports"+File.separator+"checkstyleOut.xml";
            logger.info("Consolidate Checkstyle results");
            consolidateResults(new File(checkstyleOutputFile), curEA.getTargetDirectory());
        }
    }

    private static final String FILE_TAG = "file";
    private static final String ERROR_TAG = "error";
    
    private Collection<ElementMetricsBean> classColl = new ArrayList<ElementMetricsBean>();
    private ElementMetricsBean lastElt = null;
    
    private void createNewEltBean(String srcPath, String filePath) {
    	ElementMetricsBean result = null;
    	String desc = getJavaClassName(FileTools.concatPath(srcPath, "/src"), filePath);
        result = new ElementMetricsBean();
        result.setDesc(desc);
        classColl.add(result);
        lastElt = result;
    }
    
    private void addViolation(Attributes attributes) {
    	String violationDesc = attributes.getValue(VIOLATION_DESC_ATTR);
        String idMet = violationDesc.substring(violationDesc.lastIndexOf('.') + 1); 
        String violationLine = attributes.getValue(VIOLATION_LINE_ATTR);
        MetriqueBean met = lastElt.getMetrique(idMet);
        if (met == null) {
        	met = new MetriqueBean();
        	met.setId(idMet);
        	lastElt.addMetrique(met);
        }
        met.addLine(violationLine);
    }
    
    public void consolidateResults(File clsFile, final String srcPath) throws AnalysisException {
    	SAXParserFactory fabrique = SAXParserFactory.newInstance();
    	
    	try {
			SAXParser parseur = fabrique.newSAXParser();
			File fichier = new File(clsFile.getAbsolutePath());
			
	    	DefaultHandler gestionnaire = new DefaultHandler(){
	    		public void startDocument() throws SAXException {
	    			
	    		}
	    		public void endDocument() throws SAXException {
	    			
	    		}
	    		public void startElement(String uri,
	    				String localName,
	    				String qName,
	    				Attributes attributes)
	    		throws SAXException{
	    			if(FILE_TAG.equals(qName)) {
	    				createNewEltBean(srcPath,attributes.getValue("name"));
	    			} else if(ERROR_TAG.equals(qName)) {
	    				addViolation(attributes);
	    			}
	    		}
	    		public void endElement(String uri,
	    				String localName,
	    				String qName)
	    		throws SAXException{
	    	
	    		}
	    		public void characters(char[] ch,
	    				int start,
	    				int length)
	    		throws SAXException{
	    			   	
	    		}
	    	};
	    	parseur.parse(fichier, gestionnaire);
		}
    	catch (ParserConfigurationException e) {
			throw new AnalysisException("Error during Checkstyle result consolidation", e);
		}
    	catch (SAXException e) {
			throw new AnalysisException("Error during Checkstyle result consolidation", e);
		}
    	catch (IOException e) {
			throw new AnalysisException("Error during Checkstyle result consolidation", e);
		}
        writeResult(classColl, srcPath);
    }
    
    private void writeResult(Collection<ElementMetricsBean> pClassColl, String pReportPath) throws AnalysisException {
        BufferedWriter bw = null;
        try {
            File sortie = new File(pReportPath + XMLFILENAME);
            FileWriter fw = new FileWriter(sortie);
            bw = new BufferedWriter(fw);
            bw.write("<Result>");
            ElementMetricsBean cls = null;
            MetriqueBean met = null;
            Collection<MetriqueBean> metricColl = null;
            Iterator<MetriqueBean> metIt = null;
            Iterator<ElementMetricsBean> clsIt = pClassColl.iterator();
            while (clsIt.hasNext()) {
                cls = clsIt.next();
                bw.write("<elt type=\"CLS\" name=\"");
                bw.write(cls.getDesc() + "\">");
                bw.newLine();
                metricColl = cls.getMetricCollection();
                metIt = metricColl.iterator();
                while (metIt.hasNext()) {
                	met = metIt.next();
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
        }
        catch (java.io.IOException exc) {
            throw new AnalysisException("Error writing the Checkstyle consolidated result", exc);
        }
    }
    
    private static final String VIOLATION_DESC_ATTR = "source";
    private static final String VIOLATION_LINE_ATTR = "line";
    
    private String getJavaClassName(String srcPath, String fullPath) {
        String result;
        result = fullPath.substring(srcPath.length());
        result = result.replaceAll("/", ".");
        result = result.replaceAll("\\\\", ".");
        result = result.replaceAll("\\\\", ".");
        result = result.substring(0, result.lastIndexOf("."));
        if (result.startsWith(".")) {
            result = result.substring(1);
        }
        return result;
    }    
    
	@Override
	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
		if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("Checkstyle Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "checkstyle";
	}


    @Override
    protected void postToolAnalysis() {
    }
}

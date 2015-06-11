/**
 * 
 */
package com.compuware.carscode.parser.ant;

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

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;

/**
 * @author cwfr-fdubois
 *
 */
public class JdtFormatter extends AbstractFormatter {

	private static final String SOURCE_TAG = "source";
	private static final String SOURCE_PATH_ATTR = "path";

	private static final String PROBLEM_TAG = "problem";
	private static final String PROBLEM_ID_ATTR = "id";
	private static final String PROBLEM_LINE_ATTR = "line";

	private static final String CLASS_TYPE = "CLS";
	
	private static final String PREFIX = "JDT-";
	
	// some JDT metrics names are longer than 64 characters
	// which is the size limit in the DB (METRIQUE.ID_MET).
	private static final int ID_MET_LENGTH = 64;
	
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
			log("JDT : consolidateResults : "+e);
		} catch (SAXException e) {
			log("JDT : consolidateResults : "+e);
		} catch (IOException e) {
			log("JDT : consolidateResults : "+e);
		}
		return result;
    }
    
    private class XmlFileHandler extends DefaultHandler {
	
    	private Map data = new HashMap();
    	private IElementBean currentClass = null;
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

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
			if(SOURCE_TAG.equals(qName)) {
		    	if (attributes != null) {
		    		createClass(attributes);
		    	}
			}
			else if (PROBLEM_TAG.equals(qName)) {
				createMetric(attributes);
			}
		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException{
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException{
		}

		private void createClass(Attributes attributes) {
	    	if (attributes != null) {
	        	currentClass = new ElementBean();
	        	String filePath = getRelativePath(attributes.getValue(SOURCE_PATH_ATTR), srcPath);
	    		currentClass.setDescElt(formatClassName(filePath));
	    		currentClass.setTypeElt(CLASS_TYPE);
	    		currentClass.setFilePath(filePath);
	    		currentClass.setMetricMap(new HashMap());
	        	data.put(currentClass.getDescElt(), currentClass);
	    	}
	    }

		private void createMetric(Attributes attributes) {
	    	if (attributes != null) {
	    		String metricId = PREFIX+attributes.getValue(PROBLEM_ID_ATTR);
	    		String metricLine = attributes.getValue(PROBLEM_LINE_ATTR);
	    		// make sure name is no longer than 
	    		// the size limit in the DB (METRIQUE.ID_MET).
	    		if(metricId.length()>ID_MET_LENGTH){
	    			metricId=metricId.substring(0, ID_MET_LENGTH);
	    		}
	    		addMetric(metricId, currentClass.getMetricMap(), 1, metricLine);
	    	}
	    }

	    private String formatClassName(String filePath) {
	    	String result = filePath.substring(0, filePath.lastIndexOf(".java"));
	    	result = result.replaceAll("\\\\", ".");
	    	result = result.replaceAll("/", ".");
	    	return result;
	    }
	    
    }
    
}

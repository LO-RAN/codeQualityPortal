/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.IElementBean;

/**
 * @author cwfr-fdubois
 *
 */
public class DPJCovFormatter extends AbstractFormatter {

	private static final String METHOD_TYPE = "MET";

	private static final String CLASS_TAG = "class";
	private static final String CLASS_NAME_ATTR = "name";

	private static final String METHOD_TAG = "method";
	private static final String METHOD_NAME_ATTR = "name";
	private static final String METHOD_PCTCOVERED_ATTR = "pctCovered";
	private static final String METHOD_NB_LINES_NOT_EXEC = "nbrLinesNotExecuted";

	private static final String DP_PCT_COVERED_UNIT = "DP_PCT_COVERED_UNIT";
	private static final String DP_NB_LINES_NOT_EXEC_UNIT = "DP_NB_LINES_NOT_EXEC_UNIT";

    private List srcFilesets = new ArrayList();

    public void addSource(FileSet fileset) {
        srcFilesets.add(fileset);
    }
    
	protected void checkParameters() throws BuildException {
        if (srcFilesets == null || srcFilesets.isEmpty()) {
            throw new BuildException("A source fileset is required.", getLocation());
        }
    }
	
	protected void checkCommonParameters() throws BuildException {
        if (destFile == null || destFile.isDirectory()) {
            throw new BuildException("A valid destination file is required.", getLocation());
        }
    }
    
	protected Map extractData() {
		Map result = new HashMap();
		List srcFileList = getFileList(srcFilesets);
		Iterator srcFileIter = srcFileList.iterator();
		while (srcFileIter.hasNext()) {
			result.putAll(extractData((File)srcFileIter.next()));
		}
		return result;
	}
	
	protected Map extractData(File src) {
		Map result = null;
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	log("Extracting " + src.getAbsolutePath());
    	try {
			SAXParser parser = factory.newSAXParser();
			
			XmlFileHandler manager = new XmlFileHandler();
	    	parser.parse(src, manager);
	    	result = manager.getData();
		} catch (ParserConfigurationException e) {
			log("JPDCov : consolidateResults : "+e);
		} catch (SAXException e) {
			log("JPDCov : consolidateResults : "+e);
		} catch (IOException e) {
			log("JPDCov : consolidateResults : "+e);
		}
		return result;
    }
    
    private class XmlFileHandler extends DefaultHandler {
	
    	private Map data = new HashMap();
    	private String currentClassName = null;

		public Map getData() {
			return this.data;
		}
		
		public void startDocument() throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
			if(CLASS_TAG.equals(qName)) {
		    	if (attributes != null) {
		        	currentClassName = attributes.getValue(CLASS_NAME_ATTR);
		    	}
			}
			else if (METHOD_TAG.equals(qName)) {
				createMethod(attributes);
			}
		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException{
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException{
		}

		private void createMethod(Attributes attributes) {
	    	if (attributes != null) {
	        	IElementBean method = null;
	    		method = new ElementBean();
	    		method.setDescElt(currentClassName + '.' + formatMethodName(attributes.getValue(METHOD_NAME_ATTR)));
	    		method.setTypeElt(METHOD_TYPE);
	    		method.setMetricMap(new HashMap());
	    		addMetric(DP_PCT_COVERED_UNIT, method.getMetricMap(), attributes.getValue(METHOD_PCTCOVERED_ATTR), null);
	    		addMetric(DP_NB_LINES_NOT_EXEC_UNIT, method.getMetricMap(), attributes.getValue(METHOD_NB_LINES_NOT_EXEC), null);
	        	data.put(method.getDescElt(), method);
	    	}
	    }

	    private String formatMethodName(String methodName) {
	    	String result = methodName.replaceAll(" ", "");
	    	result = result.replaceAll("\\([a-z\\.]*\\.", "(");
	    	result = result.replaceAll(",[a-z\\.]*\\.", ",");
	    	return result;
	    }
	    
    }
    
}

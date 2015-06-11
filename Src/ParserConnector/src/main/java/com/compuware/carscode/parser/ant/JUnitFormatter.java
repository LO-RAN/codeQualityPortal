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
public class JUnitFormatter extends AbstractFormatter {

	private static final String TESTSUITE_TYPE = "TESTSUITE";
	private static final String TESTCASE_CLASS_TYPE = "TESTCASECLASS";
	private static final String TESTCASE_TYPE = "TESTCASE";

	private static final String TESTSUITE_TAG = "testsuite";
	private static final String TESTSUITE_NAME_ATTR = "name";
	private static final String TESTSUITE_TESTS_ATTR = "tests";
	private static final String TESTSUITE_FAILURES_ATTR = "failures";

	private static final String TESTCASE_TAG = "testcase";
	private static final String TESTCASE_CLASSNAME_ATTR = "classname";
	private static final String TESTCASE_NAME_ATTR = "name";

	private static final String TESTCASE_FAILURE_TAG = "failure";
	
	private static final String TESTSUITE_TESTS_ID = "JUNIT_NBTESTS";
	private static final String TESTCASECLASS_TESTS_ID = "JUNIT_NBTESTS";
	private static final String TESTSUITE_FAILURES_ID = "JUNIT_NBFAILURES";
	
	private static final String TESTCASE_FAILURE_ID = "JUNIT_NBFAILURES";

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
    	
    	try {
			SAXParser parser = factory.newSAXParser();
			
			XmlFileHandler manager = new XmlFileHandler();
	    	parser.parse(src, manager);
	    	result = manager.getData(); 
		} catch (ParserConfigurationException e) {
			log("JUnit : consolidateResults : "+e);
		} catch (SAXException e) {
			log("JUnit : consolidateResults : "+e);
		} catch (IOException e) {
			log("JUnit : consolidateResults : "+e);
		}
		return result;
    }
	
    private class XmlFileHandler extends DefaultHandler {
    	private Map data = new HashMap();
    	private IElementBean currentTestSuite = null;
    	private IElementBean currentTestCase = null;

		private boolean failureDetected = false;
		private String failureTrace = null;
		
		public Map getData() {
			return this.data;
		}
		
		public void startDocument() throws SAXException {
		}

		public void endDocument() throws SAXException {
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
			if(TESTSUITE_TAG.equals(qName)) {
				createTestSuite(attributes);
			}
			else if (TESTCASE_TAG.equals(qName)) {
				createTestCase(attributes);
			}
			else if (TESTCASE_FAILURE_TAG.equals(qName)) {
				failureDetected = true;
				failureTrace = "";
			}
		}
		
		public void endElement(String uri, String localName, String qName) throws SAXException{
			if (TESTCASE_FAILURE_TAG.equals(qName)) {
				addFailure(failureTrace);
				failureDetected = false;
				failureTrace = null;
			}
		}
		
		public void characters(char[] text, int start, int length) throws SAXException {
   			if (failureDetected) {
   				failureTrace += new String(text, start, length);
   			}
   	    }
		
	    private void createTestSuite(Attributes attributes) {
	    	if (attributes != null) {
	        	IElementBean testSuite = null;
	    		testSuite = new ElementBean();
	    		testSuite.setDescElt(attributes.getValue(TESTSUITE_NAME_ATTR));
	    		testSuite.setTypeElt(TESTSUITE_TYPE);
	    		testSuite.setMetricMap(new HashMap());
	    		Map metricMap = testSuite.getMetricMap();
	    		addMetric(TESTSUITE_TESTS_ID, metricMap, attributes.getValue(TESTSUITE_TESTS_ATTR), null);
	    		addMetric(TESTSUITE_FAILURES_ID, metricMap, attributes.getValue(TESTSUITE_FAILURES_ATTR), null);
	        	data.put(testSuite.getDescElt(), testSuite);
	        	currentTestSuite = testSuite;
	    	}
	    }
	    
	    private void createTestCase(Attributes attributes) {
	    	if (attributes != null) {
	        	IElementBean testCaseClass = (IElementBean)data.get(attributes.getValue(TESTCASE_CLASSNAME_ATTR));
	        	if (testCaseClass != null && TESTSUITE_TYPE.equals(testCaseClass.getTypeElt())) {
	        		testCaseClass = null;
	        	}
	        	if (testCaseClass == null) {
		        	testCaseClass = new ElementBean();
		        	testCaseClass.setDescElt(attributes.getValue(TESTCASE_CLASSNAME_ATTR));
		        	if (!testCaseClass.getDescElt().equals(currentTestSuite.getDescElt())) {
		        		testCaseClass.setDescParent(currentTestSuite.getDescElt());
		        	}
		        	testCaseClass.setTypeElt(TESTCASE_CLASS_TYPE);
		        	testCaseClass.setMetricMap(new HashMap());
		        	data.put(testCaseClass.getDescElt(), testCaseClass);
	        	}
	        	addMetric(TESTCASECLASS_TESTS_ID, testCaseClass.getMetricMap(), 1.0, null);
	        	
	        	IElementBean testCase = null;
	    		testCase = new ElementBean();
	    		testCase.setDescElt(testCaseClass.getDescElt() + '.' + attributes.getValue(TESTCASE_NAME_ATTR));
	    		testCase.setTypeElt(TESTCASE_TYPE);
	    		testCase.setDescParent(testCaseClass.getDescElt());
	    		testCase.setFilePath(extractFilePath(testCaseClass.getDescElt()));
	    		testCase.setMetricMap(new HashMap());
	    		addMetric(TESTCASE_FAILURE_ID, testCase.getMetricMap(), 0.0, null);
	        	data.put(testCase.getDescElt(), testCase);
	        	currentTestCase = testCase;
	    	}
	    }
	    
	    private void addFailure(String failureTrace) {
	    	if (currentTestCase != null) {
	    		Map metricMap = currentTestCase.getMetricMap();
	    		addMetric(TESTCASE_FAILURE_ID, metricMap, 1.0, extractLine(failureTrace));
	    	}
	    }
	    
	    private String extractFilePath(String desc) {
	    	String result = null;
	    	if (desc != null) {
	    		result = desc.replaceAll("\\.", "/");
	    		result += ".java";
	    	}
	    	return result;
	    }
	    
	    private String extractLine(String trace) {
	    	String result = null;
	    	if (trace != null) {
	    		int startIdx = trace.lastIndexOf(':');
	    		if (startIdx > 0) {
	    			int endIdx = trace.indexOf(')', startIdx + 1);
	    			if (endIdx > startIdx + 1) {
	    				result = trace.substring(startIdx + 1, endIdx);
	    			}
	    		}
	    	}
	    	return result;
	    }
    }	
    
}

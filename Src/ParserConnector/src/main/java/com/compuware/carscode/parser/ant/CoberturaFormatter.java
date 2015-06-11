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
 * @author cwfr-lizac
 *
 */
public class CoberturaFormatter extends AbstractFormatter {

	private static final String METHOD_TYPE = "MET";

	private static final String CLASS_TAG = "class";
	private static final String CLASS_NAME_ATTR = "name";

	private static final String METHOD_TAG = "method";
	private static final String METHOD_NAME_ATTR = "name";
	private static final String METHOD_SIGNATURE_ATTR = "signature";
	private static final String METHOD_PCTCOVERED_ATTR = "branch-rate";

	private static final String PCT_COVERED_UNIT = "PCT_COVERED_UNIT";

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
			log("Cobertura : consolidateResults : "+e);
		} catch (SAXException e) {
			log("Cobertura : consolidateResults : "+e);
		} catch (IOException e) {
			log("Cobertura : consolidateResults : "+e);
		}
		return result;
    }
    
    private class XmlFileHandler extends DefaultHandler {
	
    	private Map data = new HashMap();
    	private String currentClassName = null;

		public Map getData() {
			return this.data;
		}
		
        public org.xml.sax.InputSource resolveEntity(String publicId, String systemId)
                throws org.xml.sax.SAXException, java.io.IOException {
            log("Ignoring: " + publicId + ", " + systemId);
            return new org.xml.sax.InputSource(new java.io.StringReader(""));
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
	    		method.setDescElt(currentClassName + '.' 
                                          + formatMethodName(attributes.getValue(METHOD_NAME_ATTR))
                                          +"("+formatMethodSignature(attributes.getValue(METHOD_SIGNATURE_ATTR))+")");
	    		method.setTypeElt(METHOD_TYPE);
	    		method.setMetricMap(new HashMap());
	    		addMetric(PCT_COVERED_UNIT, method.getMetricMap(), attributes.getValue(METHOD_PCTCOVERED_ATTR), null);
	        	data.put(method.getDescElt(), method);
	    	}
	    }

	    private String formatMethodName(String methodName) {
	    	String result = methodName.replaceAll(" ", "");
	    	result = result.replaceAll("\\([a-z\\.]*\\.", "(");
	    	result = result.replaceAll(",[a-z\\.]*\\.", ",");
	    	return result;
	    }

            private String formatMethodSignature(String signature) {
	    	String result = signature.replaceAll(";\\).*", "");
                result = result.replaceAll("\\(L", "");
                result = result.replaceAll(";L", ",");
                result = result.replaceAll(";", "");

                String[] params=result.split(",");
                result="";
                
                               
                for(int i = 0; i < params.length; i++){
                    String param=params[i];
                    if(! result.equals("")){
                        result+=",";
                    }
                    result+=param.replaceAll(".*\\/","");
                        }

	    	return result;
	    }
	    
    }
    
}

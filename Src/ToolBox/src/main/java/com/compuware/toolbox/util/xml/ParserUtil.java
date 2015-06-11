package com.compuware.toolbox.util.xml;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.File;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ParserUtil {

	public ParserUtil() {
		super();
	}

    /**
     * Parse the xml file to extract an Xml document.
     * @param file the xml file path.
     * @return the Xml document.
     */
    public static Document parseDefinition(String file) {
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

    /**
     * Parse the xml file to extract an Xml document.
     * @param file the xml file path.
     * @return the Xml document.
     */
    public static Document parseDefinition(File file) {
        Document xmlDocument = null;
        DOMParser usageParser= new DOMParser();
        try {
            usageParser.parse(file.getAbsolutePath());
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

}

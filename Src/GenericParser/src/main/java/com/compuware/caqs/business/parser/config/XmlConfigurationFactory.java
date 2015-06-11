/**
 * 
 */
package com.compuware.caqs.business.parser.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compuware.caqs.business.parser.Detector;
import com.compuware.caqs.business.parser.bean.CommentUseStrategyEnum;
import com.compuware.caqs.business.parser.bean.CompareRegexpEntry;
import com.compuware.caqs.business.parser.bean.CountTypeEnum;
import com.compuware.caqs.business.parser.bean.EndOfSectionTypeEnum;
import com.compuware.caqs.business.parser.bean.RegexpEntry;
import com.compuware.caqs.business.parser.bean.Section;
import com.compuware.caqs.business.parser.config.impl.ParserConfiguration;

/**
 * @author cwfr-fdubois
 *
 */
public class XmlConfigurationFactory extends AbstractConfigurationFactory {
	
	Logger logger = Logger.getLogger("StaticAnalysis");

	@Override
	public IParserConfiguration createConfig(List<File> sourceFileList,
			File resultFile, File callsToFile, File configFile) {
		IParserConfiguration result = new ParserConfiguration();
		result.setSourceFileList(sourceFileList);
		result.setResultFile(resultFile);
		result.setCallsToFile(callsToFile);
		if (configFile != null) {
	    	SAXParserFactory saxFactory = SAXParserFactory.newInstance();
	    	
	    	try {
				SAXParser parseur = saxFactory.newSAXParser();

		    	XmlFileHandler xmlManager = new XmlFileHandler(result);
		    	parseur.parse(configFile, xmlManager);
		    	
			}
	    	catch (ParserConfigurationException e) {
				logger.error("Parser configuration creation failed", e);
			}
	    	catch (SAXException e) {
				logger.error("Parser configuration creation failed", e);
			}
	    	catch (IOException e) {
				logger.error("Parser configuration creation failed", e);
			}
		}
		return result;
	}
	
	/**
	 * Xml file handler.
	 * @author cwfr-fdubois
	 *
	 */
    private class XmlFileHandler extends DefaultHandler {
    	
        private static final String COMMENT_TAG = "comment";
        private static final String EMPTYLINE_TAG = "emptyLine";
        private static final String SECTION_TAG = "section";
        private static final String DETECTOR_TAG = "detector";
        private static final String DETECTOR_RULE_TAG = "rule";
        
        private IParserConfiguration config = null;
        private Detector currentDetector = null;
        
        private Section currentSection = null;
        
        public XmlFileHandler(IParserConfiguration config) {
        	this.config = config;
        }
        
		public void startDocument() throws SAXException {
		}
		
		public void endDocument() throws SAXException {
		}
		
		public void startElement(String uri,
				String localName,
				String qName,
				Attributes attributes) throws SAXException{
			if(COMMENT_TAG.equals(qName)) {
				extractComment(attributes);
			}
			else if(EMPTYLINE_TAG.equals(qName)) {
				extractEmptyLine(attributes);
			}
			else if(SECTION_TAG.equals(qName)) {
				extractSection(attributes);
			}
			else if(DETECTOR_TAG.equals(qName)) {
				extractDetector(attributes);
			}
			else if(DETECTOR_RULE_TAG.equals(qName)) {
				extractDetectorRule(attributes);
			}
		}
		
		public void endElement(String uri,
				String localName,
				String qName) throws SAXException{
			if(SECTION_TAG.equals(qName)) {
				if (currentSection != null && currentSection.getParentSection() != null) {
					currentSection = currentSection.getParentSection();
				}
				else {
					this.config.addSection(currentSection);
					currentSection = null;
				}
			}
			else if(DETECTOR_TAG.equals(qName)) {
				this.config.addDetector(currentDetector);
			}
		}
		
		public void characters(char[] text, int start, int length)
	     throws SAXException {
	    }		
		
		private void extractComment(Attributes attributes) {
			int flags = 0;
			if (attributes.getValue("dotAll") != null && attributes.getValue("dotAll").equalsIgnoreCase("true")) {
				flags = flags | Pattern.DOTALL;
			}
			if (attributes.getValue("casesensitive") != null && attributes.getValue("casesensitive").equalsIgnoreCase("false")) {
				flags = flags | Pattern.CASE_INSENSITIVE;
			}
			this.config.addCommentPattern(attributes.getValue("regexp"), flags);
		}
		
		private void extractEmptyLine(Attributes attributes) {
			int flags = 0;
			if (attributes.getValue("dotAll") != null && attributes.getValue("dotAll").equalsIgnoreCase("true")) {
				flags = flags | Pattern.DOTALL;
			}
			if (attributes.getValue("casesensitive") != null && attributes.getValue("casesensitive").equalsIgnoreCase("false")) {
				flags = flags | Pattern.CASE_INSENSITIVE;
			}
			this.config.addEmptyLinePattern(attributes.getValue("regexp"), flags | Pattern.MULTILINE);
		}
		
		private void extractSection(Attributes attributes) {
			Section newSection = new Section();
			newSection.setName(attributes.getValue("name"));
			int flags = 0;
			if (attributes.getValue("dotAll") != null && attributes.getValue("dotAll").equalsIgnoreCase("true")) {
				flags = flags | Pattern.DOTALL;
			}
			if (attributes.getValue("casesensitive") != null && attributes.getValue("casesensitive").equalsIgnoreCase("false")) {
				flags = flags | Pattern.CASE_INSENSITIVE;
			}
			if (attributes.getValue("multiline") != null && attributes.getValue("multiline").equalsIgnoreCase("true")) {
				flags = flags | Pattern.MULTILINE;
			}
			newSection.setRegexp(attributes.getValue("regexp"), flags);
			newSection.setNameGroup(Integer.parseInt(attributes.getValue("nameGroup")));
			newSection.setOpenBlockGroup(Integer.parseInt(attributes.getValue("openBlockGroup")));
			newSection.setClosingBlock(attributes.getValue("closingBlock"));
			newSection.setEndsWith(EndOfSectionTypeEnum.valueOf(attributes.getValue("endsWith")));
			if (currentSection != null) {
				currentSection.addSubSection(newSection);
				newSection.setParentSection(currentSection);
			}
			currentSection = newSection;
		}

		private void extractDetector(Attributes attributes) {
			currentDetector = new Detector();
			currentDetector.setId(attributes.getValue("id"));
			if (attributes.getValue("commentUseStrategy") != null) {
				currentDetector.setCommentUseStrategy(CommentUseStrategyEnum.valueOf(attributes.getValue("commentUseStrategy")));
			}
			if (attributes.getValue("target") != null) {
				currentDetector.setTarget(attributes.getValue("target"));
			}
		}

		private void extractDetectorRule(Attributes attributes) {
			int flags = 0;
			if (attributes.getValue("dotAll") != null && attributes.getValue("dotAll").equalsIgnoreCase("true")) {
				flags = flags | Pattern.DOTALL;
			}
			if (attributes.getValue("casesensitive") != null && attributes.getValue("casesensitive").equalsIgnoreCase("false")) {
				flags = flags | Pattern.CASE_INSENSITIVE;
			}
			if (attributes.getValue("multiline") != null && attributes.getValue("multiline").equalsIgnoreCase("true")) {
				flags = flags | Pattern.MULTILINE;
			}
			int group = 0;
			if (attributes.getValue("group") != null) {
				group = Integer.parseInt(attributes.getValue("group"));
			}
			RegexpEntry regexpEntry = null;
			if (attributes.getValue("compareRegexp") != null) {
				boolean compareEquals = true;
				if (attributes.getValue("compareType") != null && !attributes.getValue("compareType").equalsIgnoreCase("equals")) {
					compareEquals = false;
				}
				regexpEntry = new CompareRegexpEntry(attributes.getValue("regexp"), flags, group, attributes.getValue("compareRegexp"), compareEquals);
			}
			else {
				regexpEntry = new RegexpEntry(attributes.getValue("regexp"), flags, group);
			}
			if (attributes.getValue("countType") != null) {
				regexpEntry.setCountType(CountTypeEnum.valueOf(attributes.getValue("countType")));
			}
			regexpEntry.setEmptyLinePatternList(this.config.getEmptyLinePatternList());
			currentDetector.addRegexpEntry(regexpEntry);
		}
		
		
	}

}

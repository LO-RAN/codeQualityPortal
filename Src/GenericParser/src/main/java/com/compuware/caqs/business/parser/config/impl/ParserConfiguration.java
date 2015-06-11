/**
 * 
 */
package com.compuware.caqs.business.parser.config.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.compuware.caqs.business.parser.Detector;
import com.compuware.caqs.business.parser.bean.Section;
import com.compuware.caqs.business.parser.config.IParserConfiguration;


/**
 * @author cwfr-fdubois
 *
 */
public class ParserConfiguration implements IParserConfiguration {

	/** The source file list to parse. */
	List<File> sourceFileList = new ArrayList<File>();
	
	/** The xml result file. */
	File resultFile = null;
	
	/** The csv callsto file. */
	File callsToFile = null;
	
	/** The detector list. */
	List<Detector> detectorList = new ArrayList<Detector>();
	
	/** A list of comment pattern. */
	List<Pattern> commentPatternList = new ArrayList<Pattern>();

	/** A list of empty line pattern. */
	List<Pattern> emptyLinePatternList = new ArrayList<Pattern>();
	
	/** The section list. */
	List<Section> sectionList = new ArrayList<Section>();

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#getSourceFileList()
	 */
	public List<File> getSourceFileList() {
		return sourceFileList;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#setSourceFileList(java.util.List)
	 */
	public void setSourceFileList(List<File> sourceFileList) {
		this.sourceFileList = sourceFileList;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#addSource(java.io.File)
	 */
	public void addSource(File newSource) {
		if (newSource != null) {
			if (!newSource.isDirectory()) {
				this.sourceFileList.add(newSource);
			}
			else {
				addSource(newSource.listFiles());
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#addSource(java.io.File[])
	 */
	public void addSource(File[] dirContent) {
		if (dirContent != null) {
			File currentFile = null;
			for (int i = 0; i < dirContent.length; i++) {
				currentFile = dirContent[i];
				if (currentFile.isDirectory()) {
					addSource(currentFile);
				}
				else {
					this.sourceFileList.add(currentFile);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#getResultFile()
	 */
	public File getResultFile() {
		return resultFile;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#setResultFile(java.io.File)
	 */
	public void setResultFile(File resultFile) {
		this.resultFile = resultFile;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#getCallsToFile()
	 */
	public File getCallsToFile() {
		return callsToFile;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#setCallsToFile(java.io.File)
	 */
	public void setCallsToFile(File callsToFile) {
		this.callsToFile = callsToFile;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#getDetectorList()
	 */
	public List<Detector> getDetectorList() {
		return detectorList;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#setDetectorList(java.util.List)
	 */
	public void setDetectorList(List<Detector> detectorList) {
		this.detectorList = detectorList;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#getCommentPatternList()
	 */
	public List<Pattern> getCommentPatternList() {
		return commentPatternList;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#setCommentPatternList(java.util.List)
	 */
	public void setCommentPatternList(List<Pattern> commentPatternList) {
		this.commentPatternList = commentPatternList;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#addCommentPattern(java.lang.String, int)
	 */
	public void addCommentPattern(String regexp, int flags) {
		this.commentPatternList.add(Pattern.compile(regexp, flags));
	}
	
	public List<Pattern> getEmptyLinePatternList() {
		return this.emptyLinePatternList;
	}

	public void setEmptyLinePatternList(List<Pattern> emptyLinePatternList) {
		this.emptyLinePatternList = emptyLinePatternList;
	}
	
	public void addEmptyLinePattern(String regexp, int flags) {
		this.emptyLinePatternList.add(Pattern.compile(regexp, flags));
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#addDetector(com.compuware.caqs.business.parser.Detector)
	 */
	public void addDetector(Detector d) {
		this.detectorList.add(d);
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#getSectionList()
	 */
	public List<Section> getSectionList() {
		return sectionList;
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#setSectionList(java.util.List<com.compuware.caqs.business.parser.bean.Section>)
	 */
	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.parser.config.IParserConfiguration#addSectionList(com.compuware.caqs.business.parser.bean.Section)
	 */
	public void addSection(Section section) {
		this.sectionList.add(section);
	}
}

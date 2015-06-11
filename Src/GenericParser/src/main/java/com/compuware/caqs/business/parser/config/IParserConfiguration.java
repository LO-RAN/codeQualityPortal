package com.compuware.caqs.business.parser.config;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import com.compuware.caqs.business.parser.Detector;
import com.compuware.caqs.business.parser.bean.Section;

public interface IParserConfiguration {

	/**
	 * @return the sourceFileList
	 */
	public abstract List<File> getSourceFileList();

	/**
	 * @param sourceFileList the sourceFileList to set
	 */
	public abstract void setSourceFileList(List<File> sourceFileList);

	/**
	 * Add a source file or a directory content to the list.
	 * @param newSource the source file or directory.
	 */
	public abstract void addSource(File newSource);
	
	/**
	 * Add a directory content to the list.
	 * @param dirContent an array of files.
	 */
	public abstract void addSource(File[] dirContent);
	
	/**
	 * @return the resultFile
	 */
	public abstract File getResultFile();

	/**
	 * @param resultFile the resultFile to set
	 */
	public abstract void setResultFile(File resultFile);

	/**
	 * @return the callsToFile
	 */
	public abstract File getCallsToFile();

	/**
	 * @param callsToFile the callsToFile to set
	 */
	public abstract void setCallsToFile(File callsToFile);

	/**
	 * @return the detectorList
	 */
	public abstract List<Detector> getDetectorList();

	/**
	 * @param detectorList the detectorList to set
	 */
	public abstract void setDetectorList(List<Detector> detectorList);

	/**
	 * @return the commentPatternList
	 */
	public abstract List<Pattern> getCommentPatternList();

	/**
	 * @param commentPatternList the commentPatternList to set
	 */
	public abstract void setCommentPatternList(List<Pattern> commentPatternList);

	/**
	 * Add a new comment pattern to the list.
	 * @param regexp the comment regexp.
	 * @param flags the regexp flags.
	 */
	public abstract void addCommentPattern(String regexp, int flags);

	/**
	 * @return the emptyLinePatternList
	 */
	public abstract List<Pattern> getEmptyLinePatternList();

	/**
	 * @param emptyLinePatternList the emptyLinePatternList to set
	 */
	public abstract void setEmptyLinePatternList(List<Pattern> emptyLinePatternList);

	/**
	 * Add a new empty line pattern to the list.
	 * @param regexp the empty line regexp.
	 * @param flags the regexp flags.
	 */
	public abstract void addEmptyLinePattern(String regexp, int flags);

	/**
	 * Add a new detector to the list.
	 * @param d the new detector.
	 */
	public abstract void addDetector(Detector d);

	/**
	 * @return the sectionList
	 */
	public abstract List<Section> getSectionList();

	/**
	 * @param sectionList the sectionList to set
	 */
	public abstract void setSectionList(List<Section> sectionList);
	
	/**
	 * Add a new section to the list.
	 * @param section the new section to add.
	 */
	public abstract void addSection(Section section);
	
}
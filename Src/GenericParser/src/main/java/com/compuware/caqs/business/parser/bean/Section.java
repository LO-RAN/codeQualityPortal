/**
 * 
 */
package com.compuware.caqs.business.parser.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cwfr-fdubois
 *
 */
public class Section {

	/** The section name. */
	private String name = null;
	
	/** The group used to extract the name of the found section. */
	private int nameGroup = 0;
	
	/** The group of the opening block char or string. */
	private int openBlockGroup = 0;
	
	/** The closing block to look for. */
	private String closingBlock = "";
	
	/** The type of end to look for. */
	private EndOfSectionTypeEnum endsWith = EndOfSectionTypeEnum.END_OF_FILE;
	
	/** The regexp used to find the section. */
	private Pattern regexp = null;
	
	/** The subsection list. */
	private List<Section> subSectionList = new ArrayList<Section>();

	/** The parent section. */
	private Section parentSection = null;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameGroup
	 */
	public int getNameGroup() {
		return nameGroup;
	}

	/**
	 * @param nameGroup the nameGroup to set
	 */
	public void setNameGroup(int nameGroup) {
		this.nameGroup = nameGroup;
	}

	/**
	 * @return the openBlockGroup
	 */
	public int getOpenBlockGroup() {
		return openBlockGroup;
	}

	/**
	 * @param openBlockGroup the openBlockGroup to set
	 */
	public void setOpenBlockGroup(int openBlockGroup) {
		this.openBlockGroup = openBlockGroup;
	}

	/**
	 * @return the closingBlock
	 */
	public String getClosingBlock() {
		return closingBlock;
	}

	/**
	 * @param closingBlock the closingBlock to set
	 */
	public void setClosingBlock(String closingBlock) {
		this.closingBlock = closingBlock;
	}

	/**
	 * @return the endsWith
	 */
	public EndOfSectionTypeEnum getEndsWith() {
		return endsWith;
	}

	/**
	 * @param endsWith the endsWith to set
	 */
	public void setEndsWith(EndOfSectionTypeEnum endsWith) {
		this.endsWith = endsWith;
	}

	/**
	 * @return the regexp
	 */
	public Pattern getRegexp() {
		return regexp;
	}

	/**
	 * @param regexp the regexp to set
	 */
	public void setRegexp(Pattern regexp) {
		this.regexp = regexp;
	}

	/**
	 * @param regexp the regexp to set
	 */
	public void setRegexp(String regexp, int flags) {
		this.regexp = Pattern.compile(regexp, flags);
	}

	/**
	 * @return the subSectionList
	 */
	public List<Section> getSubSectionList() {
		return subSectionList;
	}

	/**
	 * @param subSectionList the subSectionList to set
	 */
	public void setSubSectionList(List<Section> subSectionList) {
		this.subSectionList = subSectionList;
	}
	
	/**
	 * Add a new subsection to the list.
	 * @param subSection the subsection to add.
	 */
	public void addSubSection(Section subSection) {
		if (subSection != null) {
			this.subSectionList.add(subSection);
		}
	}
	
	/**
	 * @return the parentSection
	 */
	public Section getParentSection() {
		return parentSection;
	}

	/**
	 * @param parentSection the parentSection to set
	 */
	public void setParentSection(Section parentSection) {
		this.parentSection = parentSection;
	}

	/**
	 * Extract the different sections from the given source.
	 * @param fileName the current file name.
	 * @param content the source content.
	 * @return the list of sections found.
	 */
	public List<IElementBean> extractSections(String fileName, String content, String parentDesc) {
		List<IElementBean> result = new ArrayList<IElementBean>();
		if (this.regexp != null) {
			int start = 0;
			Matcher m = this.regexp.matcher(content);
			IElementBean currentElement = null;
			while (start < content.length() && m.find(start)) {
				if (EndOfSectionTypeEnum.OTHER_SECTION.equals(this.endsWith) && currentElement != null) {
					currentElement.setEndIdx(m.start() - 1);
				}
				currentElement = new ElementBean();
				currentElement.setDescElt(getDesc(m.group(this.nameGroup), parentDesc));
				if (currentElement.getDescElt() == null || currentElement.getDescElt().length() < 1) {
					currentElement.setDescElt(extractDescFromFileName(fileName));
				}
				currentElement.setStartIdx(m.start());
				currentElement.setTypeElt(this.name);
				currentElement.setDescParent(parentDesc);
				result.add(currentElement);
				if (EndOfSectionTypeEnum.END_OF_FILE.equals(this.endsWith)) {
					currentElement.setEndIdx(content.length() - 1);
					start = content.length();
				}
				else {
					start = m.end() + 1;
				}
			}
			if (EndOfSectionTypeEnum.OTHER_SECTION.equals(this.endsWith) && currentElement != null) {
				currentElement.setEndIdx(content.length() - 1);
			}
			if (this.subSectionList != null && this.subSectionList.size() > 0) {
				List<IElementBean> subElementList = new ArrayList<IElementBean>();
				Iterator<Section> sectionIter = this.subSectionList.iterator();
				while (sectionIter.hasNext()) {
					Iterator<IElementBean> elementIter = result.iterator(); 
					Section currentSection = sectionIter.next();
					while(elementIter.hasNext()) {
						currentElement = elementIter.next();
						subElementList.addAll(currentSection.extractSections(fileName, content.substring(currentElement.getStartIdx(), currentElement.getEndIdx()), currentElement.getDescElt()));
					}
				}
				result.addAll(subElementList);
			}
		}
		return result;
	}

	private String getDesc(String group, String parentDesc) {
		String result = group;
		if (parentDesc != null) {
			result = parentDesc + '.' + group;
		}
		return result;
	}

	private String extractDescFromFileName(String fileName) {
		String result = fileName;
		if (result != null && result.length() > 0) {
			result = result.substring(0, result.lastIndexOf('.'));
			result = result.replaceAll("\\\\", "/");
			result = result.replaceAll("/", ".");
		}
		return result;
	}
}

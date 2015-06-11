/**
 * 
 */
package com.compuware.caqs.business.parser.bean;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cwfr-fdubois
 *
 */
public class RegexpEntry {

	/** The regexp used for this entry. */
	Pattern regexp = null;
	
	/** The group used to extract data. */
	int group = 0;
	
	/** The count type. */
	CountTypeEnum countType = CountTypeEnum.COUNT_DETECT;
	
	/** The empty line pattern list. */
	List<Pattern> emptyLinePatternList = null;

	/** Constructor.
	 * @param regexp the regexp to use.
	 */
	public RegexpEntry(String regexp) {
		this.regexp = Pattern.compile(regexp);
	}
	
	/** Constructor.
	 * @param regexp the regexp to use.
	 * @param flags the regexp flags to use.
	 * @param group the group used to extract data.
	 */
	public RegexpEntry(String regexp, int flags, int group) {
		this.regexp = Pattern.compile(regexp, flags);
		this.group = group;
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
	public void setRegexp(String regexp) {
		this.regexp = Pattern.compile(regexp);
	}

	/**
	 * @return the group
	 */
	public int getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(int group) {
		this.group = group;
	}
	
	/**
	 * @return the countType
	 */
	public CountTypeEnum getCountType() {
		return countType;
	}

	/**
	 * @param countType the countType to set
	 */
	public void setCountType(CountTypeEnum countType) {
		this.countType = countType;
	}
	
	/**
	 * @param emptyLinePatternList the emptyLinePatternList to set
	 */
	public void setEmptyLinePatternList(List<Pattern> emptyLinePatternList) {
		this.emptyLinePatternList = emptyLinePatternList;
	}

	public void check(String sourceCode, int[] lineIndexes, IMetricBean result) {
		if (sourceCode != null) {
			Matcher m = regexp.matcher(sourceCode);
			while (m.find()) {
				if (m.groupCount() >= this.group) {
					if (CountTypeEnum.COUNT_DETECT.equals(this.countType)) {
						result.addViolation(m.start(this.group), lineIndexes);
					}
					else {
						result.incValue(countLines(m.group(group)));
					}
				}
			}
		}
	}
	
	protected int countLines(String content) {
		int result = 0;
		String tmp = content;
		if (tmp != null && tmp.length() > 0) {
			if (CountTypeEnum.COUNT_NON_EMPTY_LINES.equals(this.countType)) {
				tmp = clearEmptyLines(tmp);
				tmp = Pattern.compile("\\n[\\n\\s]*\\n").matcher(tmp).replaceAll("\n");
			}
			result = tmp.split("\\n").length;
		}
		return result;
	}
	
	private String clearEmptyLines(String source) {
		String result = source;
		if (result != null && this.emptyLinePatternList != null) {
			Iterator<Pattern> regexpIter = this.emptyLinePatternList.iterator();
			Matcher m = null;
			while (regexpIter.hasNext()) {
				m = regexpIter.next().matcher(result);
				result = m.replaceAll("");
			}
		}
		return result;
	}
	
}

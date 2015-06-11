/**
 * 
 */
package com.compuware.caqs.business.parser.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cwfr-fdubois
 *
 */
public class CompareRegexpEntry extends RegexpEntry {

	/** The regexp to compare with. */
	private Pattern compareRegexp = null;
	
	/** Compare with equals or notEqual. */
	private boolean compareEquals = true;
	
	/**
	 * Constructor.
	 * @param regexp the detection regexp
	 * @param flags the regexp flags to use.
	 * @param group the group used to extract data from  the detection result.
	 * @param compareRegexp the regexp used to check the extracted data.
	 */
	public CompareRegexpEntry(String regexp, int flags, int group, String compareRegexp, boolean compareEquals) {
		super(regexp, flags, group);
		this.compareRegexp = Pattern.compile(compareRegexp, this.regexp.flags());
		this.compareEquals = compareEquals;
	}

	/**
	 * @return the compareRegexp
	 */
	public Pattern getCompareRegexp() {
		return compareRegexp;
	}

	/**
	 * @param compareRegexp the compareRegexp to set
	 */
	public void setCompareRegexp(String compareRegexp) {
		this.compareRegexp = Pattern.compile(compareRegexp, this.regexp.flags());
	}
	
	@Override
	public void check(String sourceCode, int[] lineIndexes, IMetricBean result) {
		if (sourceCode != null) {
			Matcher m = regexp.matcher(sourceCode);
			while (m.find()) {
				if (m.groupCount() >= this.group) {
					if (matches(m.group(group))) {
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
	}
	
	private boolean matches(String str) {
		boolean result = false;
		if (this.compareEquals) {
			result = this.compareRegexp.matcher(str).matches();
		}
		else {
			result = !this.compareRegexp.matcher(str).matches();
		}
		return result;
	}
}

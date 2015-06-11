/**
 * 
 */
package com.compuware.caqs.business.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.business.parser.bean.CommentUseStrategyEnum;
import com.compuware.caqs.business.parser.bean.IMetricBean;
import com.compuware.caqs.business.parser.bean.MetricBean;
import com.compuware.caqs.business.parser.bean.RegexpEntry;

/**
 * @author cwfr-fdubois
 *
 */
public class Detector {

	/** The violation id. */
	String id = null;
	
	/** The list of regexp used to detect defects. */
	List<RegexpEntry> regexpEntryList = new ArrayList<RegexpEntry>();

	/** The strategy used for comments. */
	CommentUseStrategyEnum commentUseStrategy = CommentUseStrategyEnum.KEEP_COMMENTS;
	
	String target = null;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the regexpEntryList
	 */
	public List<RegexpEntry> getRegexpEntryList() {
		return regexpEntryList;
	}

	/**
	 * @param regexpEntryList the regexpEntryList to set
	 */
	public void setRegexpEntryList(List<RegexpEntry> regexpEntryList) {
		this.regexpEntryList = regexpEntryList;
	}
	
	/**
	 * Add a new regexp entry to the list.
	 * @param regexpEntry the new regexp entry.
	 */
	public void addRegexpEntry(RegexpEntry regexpEntry) {
		if (regexpEntry != null) {
			if (this.regexpEntryList == null) {
				this.regexpEntryList = new ArrayList<RegexpEntry>();
			}
			this.regexpEntryList.add(regexpEntry);
		}
	}
	
	/**
	 * @return the commentUseStrategy
	 */
	public CommentUseStrategyEnum getCommentUseStrategy() {
		return commentUseStrategy;
	}

	/**
	 * @param commentUseStrategy the commentUseStrategy to set
	 */
	public void setCommentUseStrategy(CommentUseStrategyEnum commentUseStrategy) {
		this.commentUseStrategy = commentUseStrategy;
	}

	/**
	 * Inspect the code to detect rule violations.
	 * @param sourceCode the source code content.
	 * @param lineIndexes the line indexes.
	 * @return rule violations detected.
	 */
	public IMetricBean inspect(String sourceCode, String sourceCodeWithoutComments, String commentsOnly, int startIdx, int endIdx, int[] lineIndexes) {
		IMetricBean result = new MetricBean(this.id);
		if (this.regexpEntryList != null && !this.regexpEntryList.isEmpty()) {
			String usedSource = getSourceToUse(sourceCode, sourceCodeWithoutComments, commentsOnly);
			Iterator<RegexpEntry> regexpIter = this.regexpEntryList.iterator();
			RegexpEntry currentEntry = null;
			while(regexpIter.hasNext()) {
				currentEntry = regexpIter.next();
				currentEntry.check(usedSource.substring(startIdx, endIdx), lineIndexes, result);
			}
		}
		return result;
	}
	
	private String getSourceToUse(String sourceCode, String sourceCodeWithoutComments, String commentsOnly) {
		String usedSource = sourceCode;
		if (CommentUseStrategyEnum.CLEAR_COMMENTS.equals(this.commentUseStrategy)) {
			usedSource = sourceCodeWithoutComments;
		}
		else if (CommentUseStrategyEnum.COMMENTS_ONLY.equals(this.commentUseStrategy)) {
			usedSource = commentsOnly;
		}
		return usedSource;
	}

	public void setTarget(String value) {
		this.target = value;
	}
	
	public boolean useDetector(String type) {
		boolean result = true;
		if (this.target != null && type != null && !this.target.equalsIgnoreCase(type)) {
			result = false;
		}
		return result;
	}
}

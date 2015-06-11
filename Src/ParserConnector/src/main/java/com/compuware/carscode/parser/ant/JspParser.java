/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

import com.compuware.carscode.parser.bean.ElementBean;
import com.compuware.carscode.parser.bean.MetricBean;
import com.compuware.carscode.parser.util.FileUtil;
import com.compuware.carscode.parser.util.links.LinkExtractor;
import com.compuware.carscode.parser.util.links.WebLinkExtractorfactory;

/**
 * @author cwfr-fdubois
 *
 */
public class JspParser extends AbstractParser {

	Map links = null;
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.ant.AbstractParser#checkParameters()
	 */
	protected void checkParameters() throws BuildException {
	}

	protected Map extractData() {
		Map result = new HashMap();
		try {
			List linkExtractors = getLinkExtractorList();
			Map linkMapping = extractMappings(linkExtractors);
			links = extractLinks(linkExtractors);
			consolidateLinks(links, linkMapping);
			
			List srcFileList = getFileList(srcFilesets);
			Iterator srcFileIter = srcFileList.iterator();
			ElementBean currentJsp = null;
			while (srcFileIter.hasNext()) {
				currentJsp = analyze((File)srcFileIter.next(), linkMapping);
				result.put(currentJsp.getDescElt(), currentJsp);
			}
		}
		catch (IOException e) {
			throw new BuildException("Error analyzing Jsp", e);
		}
		return result;
	}

	private void consolidateLinks(Map links, Map linkMapping) {
		if (links != null && linkMapping != null) {
			Set entrySet = links.entrySet();
			Iterator entryIter = entrySet.iterator();
			Map.Entry entry = null;
			Object currentMappedLink = null;
			while(entryIter.hasNext()) {
				entry = (Map.Entry)entryIter.next();
				currentMappedLink = linkMapping.get((String)entry.getValue());
				if (currentMappedLink != null) {
					entry.setValue(currentMappedLink);
				}
			}
		}
	}
	
	private static final Pattern JSP_COMMENT_PATTERN = Pattern.compile("<%--.*?--%>", Pattern.DOTALL);
	private static final Pattern JAVA_COMMENT_PATTERN = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
	private static final Pattern JAVA_INLINE_COMMENT_PATTERN = Pattern.compile("//[^\\n]+\\n", Pattern.DOTALL);
	private static final Pattern HTML_COMMENT_PATTERN = Pattern.compile("<!--.*?-->", Pattern.DOTALL);
	private static final Pattern SCRIPTLET_PATTERN = Pattern.compile("<%([^@=-].*?)%>", Pattern.DOTALL);
	
	private static final Pattern JSP_TAGLIB_PATTERN = Pattern.compile("<%[ ]*@[ ]*taglib[ ]+.*?prefix=\"([^\"]+)\".*?%>", Pattern.DOTALL);

	private static final Pattern EXPRESSION_PATTERN = Pattern.compile("<%=.+?%>", Pattern.DOTALL);
	private static final Pattern JSP_FORWARD_PATTERN = Pattern.compile("<jsp:forward[ ]+.+?/>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	private static final Pattern JSP_INCLUDE_ACTION_PATTERN = Pattern.compile("<jsp:include[ ]+.+?/>", Pattern.CASE_INSENSITIVE);
	private static final Pattern JSP_STARIMPORT_PATTERN = Pattern.compile("<%@[ ]*page[ ]+.*?import=[\"'].*?\\*.*?[\"']", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);

	private static final Pattern STYLE_TAG_PATTERN = Pattern.compile("<STYLE[ ]+.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);

	private static final Pattern SCRIPT_WITHOUT_LANGAGE_PATTERN = Pattern.compile("<SCRIPT[\\s]*>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	private static final Pattern SCRIPT_LINES_PATTERN = Pattern.compile("<SCRIPT[^>]*>.*?</SCRIPT>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);

	private static final Pattern[] STYLE_ATTR_PATTERN = {
		Pattern.compile("<[^>]+?[ ]+(style)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(color)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<(font)[ ]+.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(bgcolor)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(align)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(valign)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(cellspacing)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(cellpadding)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE),
		Pattern.compile("<[^>]+?[ ]+(border)=.+?>", Pattern.DOTALL|Pattern.CASE_INSENSITIVE)
	};
	
	private static final Pattern CLASS_ATTR_PATTERN = Pattern.compile("<[^>]+?[ ]+(class=).+?>", Pattern.DOTALL);
	private static final String[] CLASS_ATTR_EXCLUDE = new String[]{"jsp:useBean", "form", "table", "p", "body", "tr", "td", "span", "div"};
	
	private static final String JSP_TYPE = "JSP";
	
	private static final String JSP_LOC_ID = "JSP-LOC";
	private static final String JSP_CLOC_ID = "JSP-CLOC";
	private static final String JSP_HTMLCLOC_ID = "JSP-HTMLCOMMENT";
	private static final String JSP_NB_SCRIPTLET_LINES_ID = "JSP-NBSCRIPTLET";
	
	private static final String JSP_NBEXPRESSIONS_ID = "JSP-NBEXPRESSION";
	private static final String JSP_NBFORWARD_ID = "JSP-NBFORWARD";
	private static final String JSP_NBINCLUDE_ACTION_ID = "JSP-NBACTIONINCLUDE";
	private static final String JSP_NBSTARIMPORT_ID = "JSP-NBSTAR_IMPORT";
	private static final String JSP_NBSTYLE_TAG_ID = "JSP-NBSTYLETAG";
	private static final String JSP_NBSTYLE_ATTR_ID = "JSP-NBSTYLEATTR";
	private static final String JSP_NBCLASS_ATTR_ID = "JSP-NBCLASSATTRIBUTE";
	
	private static final String JSP_SCRIPT_WITHOUT_LANGAGE_ID = "JSP-NBSCRIPT-WITHOUT-LANGUAGE";
	private static final String JSP_NBSCRIPT_LINES_ID = "JSP-NBSCRIPT";

	private static final String JSP_NBCUSTOMTAG_ID = "JSP-NBCUSTOMTAG";
	
	private ElementBean analyze(File currentJsp, Map linkMapping) {
		ElementBean eltBean = new ElementBean();
		eltBean.setFilePath(getFilePath(currentJsp.getAbsolutePath()));
		eltBean.setDescElt(getName(eltBean.getFilePath()));
		eltBean.setTypeElt(JSP_TYPE);
		eltBean.setMetricMap(new HashMap());
		try {
			String content = FileUtil.extractContent(currentJsp, "utf-8");
			if (content == null  || content.length() == 0) {
				content = FileUtil.extractContent(currentJsp, "iso8859-2");
			}
			
			int[] lineIndexes = extractLineIndexes(content);
			
			Matcher m = JSP_COMMENT_PATTERN.matcher(content);
			eltBean.setMetric(countLines(m, JSP_CLOC_ID));

			m = JAVA_COMMENT_PATTERN.matcher(content);
			eltBean.setMetric(countLines(m, JSP_CLOC_ID));

			m = JAVA_INLINE_COMMENT_PATTERN.matcher(content);
			eltBean.setMetric(countLines(m, JSP_CLOC_ID));
			
			m = HTML_COMMENT_PATTERN.matcher(content);
			eltBean.setMetric(countLines(m, JSP_HTMLCLOC_ID));
			m = SCRIPTLET_PATTERN.matcher(content);
			eltBean.setMetric(countLoc(m, JSP_NB_SCRIPTLET_LINES_ID));
	
			eltBean.setMetric(countLoc(content, JSP_LOC_ID));
			
			m = EXPRESSION_PATTERN.matcher(content);
			eltBean.setMetric(count(m, JSP_NBEXPRESSIONS_ID, lineIndexes, true));
			m = JSP_FORWARD_PATTERN.matcher(content);
			eltBean.setMetric(count(m, JSP_NBFORWARD_ID, lineIndexes, true));
			m = JSP_INCLUDE_ACTION_PATTERN.matcher(content);
			eltBean.setMetric(count(m, JSP_NBINCLUDE_ACTION_ID, lineIndexes, true));
			m = JSP_STARIMPORT_PATTERN.matcher(content);
			eltBean.setMetric(count(m, JSP_NBSTARIMPORT_ID, lineIndexes, true));
			m = STYLE_TAG_PATTERN.matcher(content);
			eltBean.setMetric(count(m, JSP_NBSTYLE_TAG_ID, lineIndexes, true));
			
			m = SCRIPT_LINES_PATTERN.matcher(content);
			eltBean.setMetric(countLines(m, JSP_NBSCRIPT_LINES_ID));
			m = SCRIPT_WITHOUT_LANGAGE_PATTERN.matcher(content);
			eltBean.setMetric(count(m, JSP_SCRIPT_WITHOUT_LANGAGE_ID, lineIndexes, true));
			
			count(eltBean, content, JSP_NBSTYLE_ATTR_ID, STYLE_ATTR_PATTERN, 1, lineIndexes, true);
			countCustomTagUse(eltBean, content, lineIndexes);
			
			m = CLASS_ATTR_PATTERN.matcher(content);
			eltBean.setMetric(count(m, 1, CLASS_ATTR_EXCLUDE, JSP_NBCLASS_ATTR_ID, lineIndexes, true));
			
			extractLinks(eltBean, content, linkMapping);
		}
		catch (IOException e) {
			throw new BuildException("Error analyzing Jsp " + currentJsp.getAbsolutePath(), e);
		}
	    return eltBean;
	}

	private void count(ElementBean result, String content, String idMet, Pattern[] checks, int group, int[] lineIndexes, boolean addLines) {
		for (int i = 0; i < checks.length; i++) {
			result.setMetric(count(checks[i].matcher(content), group, idMet, lineIndexes, addLines));
		}
	}

	private void countCustomTagUse(ElementBean result, String content, int[] lineIndexes) {
		List customTagPrefixes = extractCustomTagPrefixes(content);
		if (customTagPrefixes != null && customTagPrefixes.size() > 0) {
			Iterator prefixIter = customTagPrefixes.iterator();
			String prefix = null;
			Pattern customtagPattern = null;
			while (prefixIter.hasNext()) {
				prefix = (String)prefixIter.next();
				customtagPattern = Pattern.compile("<" + prefix + ":");
				result.setMetric(count(customtagPattern.matcher(content), JSP_NBCUSTOMTAG_ID, lineIndexes, false));
			}
		}
	}
	
	private List extractCustomTagPrefixes(String content) {
		List result = new ArrayList();
		Matcher m = JSP_TAGLIB_PATTERN.matcher(content);
		while (m.find()) {
			result.add(m.group(1));
		}
		return result;
	}
	
	private MetricBean countLines(Matcher m, String idMet) {
		int nbLines = 0;
		String tmp = null;
		while (m.find()) {
			tmp = m.group();
			tmp = tmp.replaceAll("\\n[\\s\\n]*\\n", "\n");
			nbLines = nbLines + tmp.split("\\n").length;
		}
		return createMetric(idMet, nbLines, null);
	}

	private MetricBean count(Matcher m, String idMet, int[] lineIndexes, boolean addLines) {
		return count(m, 0, null, idMet, lineIndexes, addLines);
	}	
	
	private MetricBean count(Matcher m, int group, String idMet, int[] lineIndexes, boolean addLines) {
		return count(m, group, null, idMet, lineIndexes, addLines);
	}	
	
	private MetricBean count(Matcher m, int group, String[] exclusion, String idMet, int[] lineIndexes, boolean addLines) {
		int nb = 0;
		int line = 0;
		StringBuilder lines = new StringBuilder();
		while (m.find()) {
			if (!excluded(m.group(), exclusion)) {
				if (addLines) {
					if (nb > 0) {
						lines.append(',');
					}
					line = getLine(lineIndexes, m.start(group));
					lines.append(line);
				}
				nb++;
			}
		}
		return createMetric(idMet, nb, lines.toString());
	}
	
	private boolean excluded(String s, String[] exclusion) {
		boolean result = false;
		if (exclusion != null && exclusion.length > 0) {
			for (int i = 0; !result && (i < exclusion.length); i++) {
				result = s.contains(exclusion[i]);
			}
		}
		return result;
	}
	
	protected int getLine(int[] lineIndexes, int offset) {
		int result = Arrays.binarySearch(lineIndexes, offset);
		if (result < 0) {
			result = - result - 1;
		}
		return result;
	}
	
	private MetricBean countLoc(String content, String idMet) {
		MetricBean result = null;
		if (content != null) {
			String tmp = JSP_COMMENT_PATTERN.matcher(content).replaceAll("");
			tmp = tmp.replaceAll("/\\*.*?\\*/", "");
			tmp = tmp.replaceAll("//[^\\n]*\\n", "\n");
			tmp = tmp.replaceAll("\\n[\\s\\n]+", "\n");
			int nbLines = countNonEmptyLines(tmp.split("\\n"));
			result = createMetric(idMet, nbLines, null);
		}
		return result;
	}
	
	private MetricBean countLoc(Matcher m, String idMet) {
		int nbLines = 0;
		String tmp = null;
		while (m.find()) {
			tmp = m.group(1);
			tmp = tmp.replaceAll("/\\*.*?\\*/", "");
			tmp = tmp.replaceAll("//[^\\n]*\\n", "\n");
			tmp = tmp.replaceAll("\\n[\\s\\n]*\\n", "\n");
			nbLines = nbLines + countNonEmptyLines(tmp.split("\\n"));
		}
		return createMetric(idMet, nbLines, null);
	}
	
	private int countNonEmptyLines(String[] lines) {
		int result = 0;
		if (lines != null) {
			String tmp = null;
			for (int i = 0; i < lines.length; i++) {
				tmp = lines[i];
				if (tmp != null && !tmp.matches("[\\s]*")) {
					result++;
				}
			}
		}
		return result;
	}
	
	private String getFilePath(String fullPath) {
		String result = fullPath;
		if (baseDir.endsWith("/") || baseDir.endsWith("\\")) {
			result = result.substring(baseDir.length() - 1);
		}
		else {
			result = result.substring(baseDir.length());
		}
		result = result.replaceAll("\\\\", "/");
		return result;
	}
	
    protected String getName(String filePath) {
    	String result = null;
    	if (filePath != null) {
    		result = filePath.substring(0, filePath.lastIndexOf('.'));
            result = result.replaceAll("\\\\", "/");
            result = result.replaceAll("/", ".");
            if (result.startsWith(".")) {
            	result = result.substring(1);
            }
    		result += '_' + filePath.substring(filePath.lastIndexOf('.') + 1);
    	}
    	return result;
    }
    
	private static final Pattern[] LINK_PATTERNS = {
		Pattern.compile("<a[ ]+.*?href=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<c:url[ ]+.*?value=[\"'][/]?(.*?)[\"']", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<form[ ]+.*?action=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<html:form[ ]+.*?action=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<html:form[ ]+.*?type=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<%@[ ]*page[ ]+.*?import=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE),
		Pattern.compile("<jsp:useBean[ ]+.*?class=[\"'](.*?)[\"']", Pattern.CASE_INSENSITIVE)
	};
	
	private static final Pattern[] LINK_PREPARE_PATTERN = {
		JSP_COMMENT_PATTERN,
		SCRIPTLET_PATTERN,
		EXPRESSION_PATTERN,
		HTML_COMMENT_PATTERN,
		Pattern.compile("<c:out[ ]+.*?>", Pattern.CASE_INSENSITIVE)
	};
	
	private void extractLinks(ElementBean eltBean, String content, Map linkMapping) {
		if (content != null) {
			String tmp = content;
			for (int i = 0; i < LINK_PREPARE_PATTERN.length; i++) {
				tmp = LINK_PREPARE_PATTERN[i].matcher(tmp).replaceAll("");
			}
			for (int i = 0; i < LINK_PATTERNS.length; i++) {
				addLinks(eltBean, LINK_PATTERNS[i].matcher(tmp), linkMapping);
			}
			
		}
	}
	
	private void addLinks(ElementBean eltBean, Matcher m, Map linkMapping) {
		while (m.find() && m.groupCount() >= 1) {
			eltBean.addLink(consolidateLinkDesc(m.group(1), linkMapping));
		}
	}
	
	private static final Pattern SERVLET_PATTERN = Pattern.compile(".*?[/]?servlet/(.*?)");
	
	private List consolidateLinkDesc(String linkDesc, Map linkMapping) {
		List result = new ArrayList();
		String tmpLink = linkDesc;
		if (tmpLink != null) {
			Matcher m = SERVLET_PATTERN.matcher(tmpLink);
			if (m.find()) {
				tmpLink = m.group(1);
			}
			int urlParamIdx = tmpLink.indexOf('?');
			if (urlParamIdx > 0) {
				tmpLink = tmpLink.substring(0, urlParamIdx);
			}
			Object mappedLink = linkMapping.get(tmpLink);
			if (mappedLink != null) {
				if (List.class.isInstance(mappedLink)) {
					result.addAll((List)mappedLink);
				}
				else {
					result.add(mappedLink);
				}
			}
			else {
				result.add(tmpLink);
			}
		}
		return result;
	}
	
	private List configFileList = new ArrayList();
	
	public void addConfig(FileSet fileset) {
		configFileList.add(fileset);
    }
	
	private List getLinkExtractorList() {
		List result = new ArrayList();
		List fileList = this.getFileList(this.configFileList);
		Iterator fileIter = fileList.iterator();
		File  currentFile = null;
		WebLinkExtractorfactory linkExtractorfactory = WebLinkExtractorfactory.getInstance();
		LinkExtractor currentLinkExtractor = null;
		while (fileIter.hasNext()) {
			currentFile = (File)fileIter.next();
			currentLinkExtractor = linkExtractorfactory.getLinkExtractor(currentFile);
			if (currentLinkExtractor != null) { 
				result.add(currentLinkExtractor);
			}
		}
		return result;
	}
	
	private Map extractMappings(List linkExtractorList) throws IOException {
		Map result = new HashMap();
		Iterator linkIter = linkExtractorList.iterator();
		LinkExtractor currentExtractor = null;
		while (linkIter.hasNext()) {
			currentExtractor = (LinkExtractor)linkIter.next();
			result.putAll(currentExtractor.extractMapping());
		}
		return result;
	}
	
	private Map extractLinks(List linkExtractorList) throws IOException {
		Map result = new HashMap();
		Iterator linkIter = linkExtractorList.iterator();
		LinkExtractor currentExtractor = null;
		while (linkIter.hasNext()) {
			currentExtractor = (LinkExtractor)linkIter.next();
			result.putAll(currentExtractor.extractLinks());
		}
		return result;
	}
	
	protected void printLinks(Map data) throws IOException {
		Map allLinks = new HashMap(data);
		allLinks.put("links", links);
		super.printLinks(allLinks);
	}
	
	/*
	public static void main (String[] args) {
		JspParser jspParser = new JspParser();
		jspParser.setBaseDir("D:\\Analyses\\LaPosteEnseigne\\Lot1\\delpdti\\Ens9\\src\\");
		jspParser.analyze(new File("D:\\Analyses\\LaPosteEnseigne\\Lot1\\delpdti\\Ens9\\src\\jsp\\gestiondureferentielsitepilote\\error.jsp"), new HashMap());
	}
	*/
	
}

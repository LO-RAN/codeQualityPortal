package com.compuware.carscode.parser.jsp.jasper;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jasper.compiler.Node;
import org.apache.jasper.compiler.Node.Nodes;


public class LinkExtractor {

	private static final String NBEXCEPTION = "NBEXCEPTION";
	private static final String NBSTYLEATTR = "NBSTYLEATTR";
	private static final String NBSTYLETAG = "NBSTYLETAG";
	private static final String NBCLASSATTRIBUTE = "NBCLASSATTRIBUTE";
	private static final String NBJSPFORWARD = "NBJSPFORWARD";
	private static final String NBSCRIPT = "NBSCRIPT";
	private static final String NBHARDCODEDTEXT = "NBHARDCODEDTEXT";
	private static final String NBEXPRESSION = "NBEXPRESSION";
	private static final String NBSCRIPTLET = "NBSCRIPTLET";
	private static final String HTMLCOMMENT = "HTMLCOMMENT";
	private static final String CLOC = "CLOC";
	private static final String LOC = "LOC";

	private Collection metricColl = new ArrayList();
	
	public void extract(Nodes n) {
    	Nodes nodes = n.getRoot().getBody();
    	int nbScriptletLines = countLines(nodes, Node.Scriptlet.class);
	}

	private void addMetric(String id, int value) {
		metricColl.add(new MetricBean(id, value));
	}
	
    private ParserUtil parserUtil = ParserUtil.getInstance();
    
    private static final String JSP_FORWARD_REGEXP = "<JSP:FORWARD";
    private static final String HTML_LINK_REGEXP = "<[a|A] href=[\"|'](.*)-->";
    private static final String HARDCODED_REGEXP = "[>]?([\\w\\s&;:-]*[\\w]+[\\w\\s&;:-]*)</";        

    private static final String[] SCRIPT_REGEXP = {"<SCRIPT[ >]?", "</SCRIPT>"};
    private static final String[] STYLE_TAG_REGEXP = {"<B>", "<I>", "<FONT[^>]*>", "<U>", "<CENTER>"};
    private static final String[] STYLE_ATTR_REGEXP = {"<[a-zA-Z =\"0-9%]+ style=", "<[a-zA-Z =\"0-9%]+ font=", "<[a-zA-Z =\"0-9%]+ size=", "<[a-zA-Z =\"0-9%]+ color=", "<[a-zA-Z =\"0-9%]+ face=", "<[a-zA-Z =\"0-9%]+ [v]?align=", "<[a-zA-Z =\"0-9%]+ bgcolor="};
    
    
    public void print(String jspUri, PrintWriter writer) {
    	String fileDesc = jspUri.substring(1, jspUri.lastIndexOf('.'));
    	fileDesc = fileDesc.replaceAll("/", ".");
    	writer.append("<class name=\"").append(fileDesc).append("\">\n");
    	MetricBean bean;
    	Iterator i = metricColl.iterator();
    	while (i.hasNext()) {
    		bean = (MetricBean)i.next();
    		printMetric(bean, writer);
    	}
    	writer.println("</class>");
    }
    
    private void printMetric(MetricBean bean, PrintWriter writer) {
    	writer.append("\t<metric id=\"").append(bean.getId()).append("\" value=\"" + bean.getValue()).append("\" />\n");
    }
    
    private int countLines(Nodes nodes, Class cls, String regexpStart, String regexpEnd) {
    	int result = 0;
    	Collection nodeColl = parserUtil.retrieveNodeFromClass(nodes, cls);
    	Node current;
    	String text;
    	boolean waitForEnd = false;
    	if (nodeColl != null) {
    		Iterator it = nodeColl.iterator();
    		while (it.hasNext()) {
    			current = (Node)it.next();
    			text = current.getText();
    			if (text != null && text.length() > 0) {
    		    	int startIdx = 0;
    		    	int endIdx;
    				Pattern sp = Pattern.compile(regexpStart, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    				Matcher sm = sp.matcher(text);
    				Pattern ep = Pattern.compile(regexpEnd, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    				Matcher em = ep.matcher(text);
    				while (sm.find(startIdx) || waitForEnd) {
    					if (!waitForEnd) {
    						startIdx = sm.end();
    					}
        				if (em.find(startIdx)) {
        					endIdx = em.start();
        					result += countLines(text.substring(startIdx, endIdx));
            				startIdx = em.end();
        					waitForEnd = false;
        				}
        				else {
        					result += countLines(text.substring(startIdx));
        					waitForEnd = true;
        					break;
        				}
    				}
    			}
    		}
    	}
    	return result;
    }
    
    private int countLines(Nodes nodes, Class cls, String[] regexp) {
    	int result = 0;
    	Collection nodeColl = parserUtil.retrieveNodeFromClass(nodes, cls);
    	Node current;
    	String text;
    	if (nodeColl != null) {
    		Iterator it = nodeColl.iterator();
    		while (it.hasNext()) {
    			current = (Node)it.next();
    			text = current.getText();
    			if (text != null && text.length() > 0) {
    				int cnt = 0;
    				for (int i = 0; i < regexp.length; i++) {
    					cnt = countLines(current.getText(), regexp[i]);
    					result += cnt;
    				}
    			}
    		}
    	}
    	return result;
    }
    
    private int countLines(Collection nodeColl, String regexp) {
    	int result = 0;
    	Node current;
    	String text;
    	if (nodeColl != null) {
    		Iterator it = nodeColl.iterator();
    		while (it.hasNext()) {
    			current = (Node)it.next();
    			text = current.getText();
    			if (text != null && text.length() > 0) {
    				result += countLines(current.getText(), regexp);
    			}
    		}
    	}
    	return result;
    }
    
    private int countLines(Nodes nodes, Class cls, String regexp) {
    	Collection nodeColl = parserUtil.retrieveNodeFromClass(nodes, cls);
    	return countLines(nodeColl, regexp);
    }
    
    private int countLines(String text, String regexp) {
    	int result = 0;
    	String tmp;
		Pattern p = Pattern.compile(regexp, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		while (m.find()) {
			if (m.groupCount() > 0) {
				tmp = m.group(1);
			}
			else {
				tmp = m.group();
			}
			result += countLines(tmp);
		}
		return result;
    }
    
    private int countLines(Nodes nodes, Class cls) {
    	Collection tmp = parserUtil.retrieveNodeFromClass(nodes, cls);
    	return countLines(tmp);
    }
    
    private int countLines(Collection nodeColl) {
    	int result = 0;
    	Node current;
    	if (nodeColl != null) {
    		Iterator it = nodeColl.iterator();
    		while (it.hasNext()) {
    			current = (Node)it.next();
    			result += countLines(current.getText());
    		}
    	}
    	return result;
    }
    
    private int countLines(String text) {
    	int result = 0;
    	if (text != null && text.length() > 0) {
    		String[] tmp = text.split("\n");
    		String val;
    		for (int i = 0; i < tmp.length; i++) {
    			val = tmp[i];
    			if (!val.matches("[\\s]*")) {
    				result++;
    			}
    		}
    	}
    	return result;
    }
        	
}

package com.compuware.carscode.parser.jsp.jasper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.jasper.compiler.Node;

public class ParserUtil {

	private static final ParserUtil singleton = new ParserUtil();
	
	private ParserUtil() {
	}
	
	public static ParserUtil getInstance() {
		return singleton;
	}
	
	public Collection retrieveNodeFromClass(Node.Nodes root, Class cls) {
		Collection result = new ArrayList();
    	Node current;
		if (root != null && cls != null) {
	    	for (int i = 0; i < root.size(); i++) {
	    		current = root.getNode(i);
	    		if (cls.equals(current.getClass())) {
	    			result.add(current);
	    		}
	    		if (current.getBody() != null && current.getBody().size() > 0) {
	    			result.addAll(retrieveNodeFromClass(current.getBody(), cls));
	    		}
	    	}
		}
		return result;
	}
	
	public Collection retrieveNodeFromClass(Node.Nodes root, Collection clsColl) {
		Collection result = new ArrayList();
    	Node current;
		if (root != null && clsColl != null) {
	    	for (int i = 0; i < root.size(); i++) {
	    		current = root.getNode(i);
	    		if (isNodeFromClass(current, clsColl)) {
	    			result.add(current);
	    		}
	    	}
		}
		return result;
	}
	
	public boolean isNodeFromClass(Node node, Collection clsColl) {
		boolean result = false;
		Class current;
		if (node != null && clsColl != null) {
			Iterator it = clsColl.iterator();
	    	while (it.hasNext() && !result) {
	    		current = (Class)it.next();
	    		if (node.getClass() == current) {
	    			result = true;
	    		}
	    	}
		}
		return result;
	}

	public Collection retrieveExceptionsFromNodes(Node.Nodes root) {
		Collection result = new ArrayList();
    	Node current;
		if (root != null) {
	    	for (int i = 0; i < root.size(); i++) {
	    		current = root.getNode(i);
    			result.addAll(current.getExceptionList());
	    	}
		}
		return result;
	}
	
	
}

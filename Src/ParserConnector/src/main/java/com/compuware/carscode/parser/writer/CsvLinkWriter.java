package com.compuware.carscode.parser.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.compuware.carscode.parser.bean.IElementBean;

/** 
 * @author cwfr-fdubois
 *
 */
public class CsvLinkWriter implements ElementMetricWriter {

	public void print(Map data, File out) throws IOException {
		if (data != null && !data.isEmpty() && out != null) {
			print(data.values(), out);
		}
	}

	public void print(Collection data, File out) throws IOException {
		if (data != null && !data.isEmpty() && out != null) {
			List orderedData = new ArrayList(data);
			FileWriter fw = new FileWriter(out);
			BufferedWriter bw = new BufferedWriter(fw);
			Iterator eltIter = orderedData.iterator();
			Object currentElt = null;
			while (eltIter.hasNext()) {
				currentElt = eltIter.next();
				if (IElementBean.class.isInstance(currentElt)) {
					print((IElementBean)currentElt, bw);
				}
				else {
					print((Map)currentElt, bw);
				}
			}
			bw.flush();
			bw.close();
		}
	}

	protected void print(IElementBean elt, BufferedWriter bw) throws IOException {
		if (elt != null) {
			Set links = elt.getLinks();
			if (links != null && links.size() > 0) {
				Iterator linkIter = links.iterator();
				String currentLink = null;
				while (linkIter.hasNext()) {
					currentLink = (String)linkIter.next();
					printLink(elt, currentLink, bw);
				}
			}
		}
	}
	
	protected void print(Map links, BufferedWriter bw) throws IOException {
		if (links != null) {
			Set keySet = links.keySet();
			if (keySet != null && keySet.size() > 0) {
				Iterator keyIter = keySet.iterator();
				String currentKey = null;
				while (keyIter.hasNext()) {
					currentKey = (String)keyIter.next();
					Object link = links.get(currentKey);
					if (List.class.isInstance(link)) {
						printLink(currentKey, (List)link, bw);
					}
					else {
						printLink(currentKey, (String)link, bw);
					}
				}
			}
		}
	}
	
	protected void printLink(IElementBean elt, String link, BufferedWriter bw) throws IOException {
		bw.append(elt.getDescElt()).append(';').append(link).append('\n');
	}
	
	protected void printLink(String from, String to, BufferedWriter bw) throws IOException {
		bw.append(from).append(';').append(to).append('\n');
	}
	
	protected void printLink(String from, List to, BufferedWriter bw) throws IOException {
		if (to != null && to.size() > 0) {
			Iterator toIter = to.iterator();
			while (toIter.hasNext()) {
				printLink(from, (String)toIter.next(), bw);
			}
		}
	}
	
}

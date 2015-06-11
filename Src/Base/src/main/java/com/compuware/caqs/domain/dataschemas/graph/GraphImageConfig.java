/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.graph;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

/**
 * @author cwfr-fdubois
 *
 */
public class GraphImageConfig {

	private String title = null;
	private Locale locale = null;
	private MessageResources resources = null;
	private int width = 0;
	private int height = 0;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	/**
	 * @return the resources
	 */
	public MessageResources getResources() {
		return resources;
	}
	/**
	 * @param resources the resources to set
	 */
	public void setResources(MessageResources resources) {
		this.resources = resources;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
}

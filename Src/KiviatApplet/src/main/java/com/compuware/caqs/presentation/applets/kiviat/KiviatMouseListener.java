/**
 * 
 */
package com.compuware.caqs.presentation.applets.kiviat;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.ChartEntity;


/**
 * @author cwfr-fdubois
 *
 */
public class KiviatMouseListener implements ChartMouseListener {
	
	private JApplet applet;

	/**
	 * @return Returns the container.
	 */
	public JApplet getApplet() {
		return applet;
	}

	/**
	 * @param container The container to set.
	 */
	public void setApplet(JApplet applet) {
		this.applet = applet;
	}

	
	public void chartMouseClicked(ChartMouseEvent arg0) {
		System.out.println("Mouse clicked");
		ChartEntity entity = arg0.getEntity();	
		String urlText = entity.getURLText();
		try {
			URL url = new URL(applet.getCodeBase() + urlText);
			System.out.println("URL: "+applet.getCodeBase() + urlText);
			applet.getAppletContext().showDocument(url, "_parent");
		}
		catch (MalformedURLException e) {
			// Nothing...
			System.out.println("Bad URL: "+urlText);
		}
	}

	public void chartMouseMoved(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
}

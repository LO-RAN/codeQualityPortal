/**
 * 
 */
package com.compuware.caqs.business.chart.urls;

import java.io.Serializable;
import java.net.URLEncoder;

import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

/**
 * @author cwfr-fdubois
 *
 */
public class KiviatURLGenerator implements CategoryURLGenerator, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8482360572943880885L;
	
	private String urlBase = null;
	private String parameterName = null;
	
	public KiviatURLGenerator(String urlBase, String parameterName) {
		this.urlBase = urlBase;
		this.parameterName = parameterName;
	}
	
    /**
     * Generates a URL for a particular item within a series.
     *
     * @param dataset  the dataset.
     * @param series  the series index (zero-based).
     * @param category  the category index (zero-based).
     *
     * @return The generated URL.
     */
    public String generateURL(CategoryDataset dataset, int series, 
                              int category) {
        String url = this.urlBase;
        boolean firstParameter = url.indexOf("?") == -1;
        url += firstParameter ? "?" : "&amp;";
        
        String catId = "" + (category + 3);
        url += this.parameterName + "=" 
            + URLEncoder.encode(catId);
        return url;
    }

}

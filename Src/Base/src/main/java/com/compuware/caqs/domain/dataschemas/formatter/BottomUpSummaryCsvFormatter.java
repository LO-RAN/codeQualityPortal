package com.compuware.caqs.domain.dataschemas.formatter;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.apache.struts.util.MessageResources;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.util.CaqsConfigUtil;

public class BottomUpSummaryCsvFormatter implements Formatter {

	private String[] metArray = null;
	
	public BottomUpSummaryCsvFormatter() {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String metList = dynProp.getProperty(Constants.BOTTOMUP_ADDITIONNAL_METRIC_LIST_KEY);
        if (metList != null && metList.length() > 0) {
        	this.metArray = metList.split(",");
        }
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.util.formater.Formater#format(java.util.Collection, java.io.PrintWriter)
	 */
	public void format(Collection c, PrintWriter writer, MessageResources resources, Locale loc) {
		if (c != null && writer != null) {
			printHeader(writer, resources, loc);
			Iterator i = c.iterator();
			while (i.hasNext()) {
				format(i.next(), writer, resources, loc);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.util.formater.Formater#format(java.lang.Object, java.io.PrintWriter)
	 */
	public void format(Object o, PrintWriter writer, MessageResources resources, Locale loc) {
		if (o instanceof BottomUpSummaryBean) {
			format((BottomUpSummaryBean)o, writer);
		}
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.util.formater.Formater#format(java.lang.Object, java.io.PrintWriter)
	 */
	private void format(BottomUpSummaryBean summaryBean, PrintWriter writer) {
		if (summaryBean != null && writer != null) {
			writer.append(summaryBean.getElement().getDesc()).append(Constants.CSV_SEPARATOR);
			writer.append("" + summaryBean.getNote(0)).append(Constants.CSV_SEPARATOR);
			writer.append("" + summaryBean.getNote(1)).append(Constants.CSV_SEPARATOR);
			writer.append("" + summaryBean.getNote(2)).append(Constants.CSV_SEPARATOR);
			writer.append("" + summaryBean.getNote(3)).append(Constants.CSV_SEPARATOR);
			printAdditionnalMetrics(summaryBean, writer);
			writer.append(Constants.CR);
		}
	}
	
	private void printHeader(PrintWriter writer, MessageResources resources, Locale loc) {
		writer.append(resources.getMessage(loc, "caqs.bottomup.nom")).append(Constants.CSV_SEPARATOR);
		writer.append(resources.getMessage(loc, "caqs.bottomup.nbde", "1")).append(Constants.CSV_SEPARATOR);
		writer.append(resources.getMessage(loc, "caqs.bottomup.nbde", "2")).append(Constants.CSV_SEPARATOR);
		writer.append(resources.getMessage(loc, "caqs.bottomup.nbde", "3")).append(Constants.CSV_SEPARATOR);
		writer.append(resources.getMessage(loc, "caqs.bottomup.nbde", "4")).append(Constants.CSV_SEPARATOR);
        if (metArray != null) {
        	for (int i = 0; i < metArray.length; i++) {
    			writer.append("" + metArray[i]).append(Constants.CSV_SEPARATOR);
        	}
        }
		writer.append(Constants.CR);
	}
	
	private void printAdditionnalMetrics(BottomUpSummaryBean summaryBean, PrintWriter writer) {
        if (metArray != null) {
        	for (int i = 0; i < metArray.length; i++) {
    			writer.append("" + summaryBean.getAdditionnalMetric(metArray[i])).append(Constants.CSV_SEPARATOR);
        	}
        }
	}
	
}

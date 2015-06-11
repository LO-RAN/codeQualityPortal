/**
 * 
 */
package com.compuware.caqs.parser.pmd.renderer;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.pmd.IRuleViolation;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.renderers.AbstractRenderer;
import net.sourceforge.pmd.util.StringUtil;

import com.compuware.caqs.parser.pmd.bean.MetricBean;

/**
 * @author cwfr-fdubois
 *
 */
public class XmlRenderer extends AbstractRenderer {


    public void render(Writer writer, Report report) throws IOException {

        StringBuffer buf = new StringBuffer();
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + PMD.EOL + createVersionAttr() + createTimestampAttr() + createTimeElapsedAttr(report) + '>' + PMD.EOL);
        String filename = null;
        String className = null;

        Map metricMap = null;
        MetricBean currentMetric = null;
        // rule violations
        for (Iterator i = report.iterator(); i.hasNext();) {
            buf.setLength(0);
            IRuleViolation rv = (IRuleViolation) i.next();
            if (!rv.getFilename().equals(filename)) { // New File
                if (filename != null) {// Not first file ?
                	buf.append(printMetrics(metricMap));
                    buf.append("</elt>").append(PMD.EOL);
                }
                metricMap = new HashMap();
                className = getClassName(rv);
                buf.append("<elt name=\"");
                StringUtil.appendXmlEscaped(buf, className);
                buf.append("\" type='CLS'>").append(PMD.EOL);
            }
            
            if (metricMap.containsKey(rv.getRule().getName())) {
            	currentMetric = (MetricBean)metricMap.get(rv.getRule().getName());
            }
            else {
            	currentMetric = new MetricBean();
            	currentMetric.setId(rv.getRule().getName());
            	metricMap.put(rv.getRule().getName(), currentMetric);
            }

            currentMetric.addLine(rv.getBeginLine());
            currentMetric.incValue();
            
            filename = rv.getFilename(); 
            
            writer.write(buf.toString());
        }
        if (filename != null) { // Not first file ?
        	writer.write(printMetrics(metricMap));
            writer.write("</elt>");
            writer.write(PMD.EOL);
        }

        // errors
        for (Iterator i = report.errors(); i.hasNext();) {
            buf.setLength(0);
            Report.ProcessingError pe = (Report.ProcessingError) i.next();
            buf.append("<error ").append("filename=\"");
            StringUtil.appendXmlEscaped(buf, pe.getFile());
            buf.append("\" msg=\"");
            StringUtil.appendXmlEscaped(buf, pe.getMsg());
            buf.append("\"/>").append(PMD.EOL);
            writer.write(buf.toString());
        }

        writer.write("</Result>");
    }
    
    private String getClassName(IRuleViolation rv) {
    	String result = rv.getFilename();
    	result = result.replaceAll("\\\\", ".");
    	result = result.replaceAll("/", ".");
    	result = result.substring(0, result.lastIndexOf(".java"));
    	return result;
    }

    private String printMetrics(Map metricMap) {
    	StringBuffer result = new StringBuffer();
    	if (metricMap != null) {
    		Collection metricColl = metricMap.values();
    		Iterator i = metricColl.iterator();
    		MetricBean metricBean = null;
    		while (i.hasNext()) {
    			metricBean = (MetricBean)i.next();
    			result.append(printMetric(metricBean));
    		}
    	}
    	return result.toString();
    }
    
    private String printMetric(MetricBean metricBean) {
    	StringBuffer result = new StringBuffer();
    	result.append("<metric id=\"").append(metricBean.getId().toUpperCase()).append('"');
    	result.append(" value=\"").append(metricBean.getValue()).append('"');
    	result.append(" lines=\"").append(metricBean.getLine()).append('"');
    	result.append(" />").append(PMD.EOL);
    	return result.toString();
    }
    
    private String createVersionAttr() {
        return "<Result version=\"" + PMD.VERSION + "\"";
    }

    private String createTimestampAttr() {
        return " timestamp=\"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Date()) + "\"";
    }

    private String createTimeElapsedAttr(Report rpt) {
        Report.ReadableDuration d = new Report.ReadableDuration(rpt.getElapsedTimeInMillis());
        return " elapsedTime=\"" + d.getTime() + "\"";
    }

}

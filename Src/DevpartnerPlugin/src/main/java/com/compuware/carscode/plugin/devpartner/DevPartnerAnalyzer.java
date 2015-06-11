/*
 * StaticAnalysisDevPartner.java
 * @author  cwfr-fxalbouy
 * Created on 8 novembre 2002, 10:42
 */

package com.compuware.carscode.plugin.devpartner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.compuware.carscode.plugin.devpartner.bean.Call;
import com.compuware.carscode.plugin.devpartner.bean.ElementBean;
import com.compuware.carscode.plugin.devpartner.bean.MetricBean;
import com.compuware.carscode.plugin.devpartner.dao.DaoFactory;
import com.compuware.carscode.plugin.devpartner.dao.DevPartnerDao;
import com.compuware.toolbox.util.logging.LoggerManager;



/**
 * Exporte les métriques/anomalies détectées par Devpartner Studio
 * vers un format XML.
 * @author cwfr-fdubois
 *
 */
public class DevPartnerAnalyzer {
    
    //logger
    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");
    
    private DevPartnerDao dao = null;

    /** Creates a new instance of DevPartnerAnalyzer */
    public DevPartnerAnalyzer() {
        super();
    }
    
    public boolean consolidateResults(String fileMdbPath, String srcDir, String destination, String dialect) {
    	boolean retour = false;
    	DaoFactory daofactory = DaoFactory.getInstance();
    	dao = daofactory.getDevPartnerDao(dialect);
    	if(dao!=null) {
    		dao.setSourceDirectory(srcDir);
    		try {
    			Map metrics = dao.retrieveModuleSummary(fileMdbPath);
    			metrics = mergeMetrics(metrics, dao.retrieveModuleMetrics(fileMdbPath));
    			metrics = mergeMetrics(metrics, dao.retrieveModuleRuleDetections(fileMdbPath));
    			saveDetections(metrics, destination + "classMetrics.xml", "CLS");
    			metrics = dao.retrieveProcedureSummary(fileMdbPath);
    			metrics = mergeMetrics(metrics, dao.retrieveProcedureMetrics(fileMdbPath));
    			saveDetections(metrics, destination + "methodMetrics.xml", "MET");
    			Collection callsTo = dao.retrieveCallsTo(fileMdbPath);
    			saveCallsTo(callsTo, destination + "callsto.csv");
    			retour = true;
    		} catch (ClassNotFoundException e) {
    			logger.error(e);
    		} catch (SQLException e) {
    			logger.error(e);
    		} catch (FileNotFoundException e) {
    			logger.error(e);
    		} catch (IOException e) {
    			logger.error(e);
    		}
    	} else {
    		logger.error("Dialect not supported.");
    	}
    	return retour;
    }
    
    private Map mergeMetrics(Map existingMetrics, Map newMetrics) {
    	Map result = existingMetrics;
    	Iterator i = newMetrics.keySet().iterator();
    	String key = null;
    	ElementBean currentElement = null;
    	Map metMap = null;
    	while (i.hasNext()) {
    		key = (String)i.next();
    		if (!result.containsKey(key)) {
    			result.put(key, newMetrics.get(key));
    		}
    		else {
    			currentElement = (ElementBean)result.get(key);
    			metMap = currentElement.getMetricMap();
    			metMap.putAll(((ElementBean)newMetrics.get(key)).getMetricMap());
    		}
    	}
    	return result;
    }
    
    private void saveDetections(Map elementMap, String destination, String tagName) throws FileNotFoundException, IOException {
    	File f = new File(destination);
    	OutputStreamWriter owriter = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
    	PrintWriter writer = new PrintWriter(owriter); 
    	writer.println("<Result>");
    	Set elements = elementMap.keySet();
    	List eltList = new ArrayList(elements);
    	Collections.sort(eltList);
    	Iterator i = eltList.iterator();
    	String eltKey = null;
    	ElementBean currentElement = null;
    	int in=0;
    	while (i.hasNext()) {
    		in++;
    		eltKey = (String)i.next();
    		currentElement = (ElementBean)elementMap.get(eltKey);
    		saveDetections(currentElement, tagName, writer);
    	}
    	writer.println("</Result>");
    	writer.flush();
    	writer.close();
    }
    
    private void saveDetections(ElementBean currentElement, String tagName, PrintWriter writer) {
    	writer.write("<elt type=\""+tagName+"\" name=\"");
    	String name = currentElement.getName();
    	writer.write(name);
    	if (currentElement.getFilePath() != null) {
	    	writer.write("\" filepath=\"");
	    	writer.write(currentElement.getFilePath());
    	}
    	writer.println("\">");
    	if(currentElement.getParentName() != null) {
    		writer.print("    <parent name=\"");
    		writer.print(currentElement.getParentName());
    		writer.println("\" />");
    	}
    	Iterator i = currentElement.getMetricMap().values().iterator();
    	MetricBean detection = null;
    	while (i.hasNext()) {
    		detection = (MetricBean)i.next();
    		saveDetection(detection, writer);
    	}
    	writer.write("</elt>\n");
    }
    
    private void saveDetection(MetricBean detection, PrintWriter writer) {
    	writer.write("    <metric id=\"");
    	String prefix = "DP-";
    	if(dao != null && !detection.getId().startsWith("Count") && !detection.getId().startsWith("Line")) {
    		prefix = dao.getMetricPrefix();
    	}
    	writer.write(detection.getId(prefix));
    	writer.write("\"");
    	writer.write(" value=\"");
    	writer.write(""+detection.getValue());
    	writer.write("\"");
    	writer.write(" lines=\"");
    	writer.write(detection.getLine());
    	writer.write("\"");
    	writer.println(" />");
    }
    
    private void saveCallsTo(Collection callsToCollection, String destination) throws FileNotFoundException, IOException {
    	if (callsToCollection != null) {
        	File f = new File(destination);
        	OutputStreamWriter owriter = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
        	PrintWriter writer = new PrintWriter(owriter); 
        	Iterator i = callsToCollection.iterator();
        	Call current = null;
        	while (i.hasNext()) {
        		current = (Call)i.next();
        		writer.append(current.getFrom()).append(';').append(current.getTo());
        		writer.println();
        	}
        	writer.flush();
        	writer.close();
    	}
    }
    
    public static void main(String[] args) {
    	int retour = 0;
    	DevPartnerAnalyzer analyser = new DevPartnerAnalyzer();
    	if(args.length==4) {
    		if(!analyser.consolidateResults(args[0], args[1], args[2], args[3])) {
    			logger.error("Error while consolidating results.");
    			retour = -1;
    		}
    	} else {
    		logger.error("DevPartnerAnalyser usage :");
    		logger.error("[DPMDB file] [Sources directory] [Target results directory] [Dialect]");
    		retour = -1;
    	}
    	System.exit(retour);
    }
    
}

/*
 * StaticAnalysisDevPartnerRuleByRule.java
 *
 * Created on July 13, 2004, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.SystemIO;
import com.compuware.toolbox.exception.SystemIOException;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.StringUtils;
import com.compuware.toolbox.util.xml.ParserUtil;

/**
 *
 * @author  cwfr-fdubois
 */
public class StaticAnalysisCSMetricGeneration extends StaticAnalysis {
    
    /** Creates a new instance of StaticAnalysisCSMetricGeneration */
    public StaticAnalysisCSMetricGeneration() {
        super();
        logger.info("Tool is CSMetricGeneration");
    }
    
    @Override
    protected void initSpecific(Properties dbProps){
        //csMetricGeneration
    }
    
    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("CS Metric generation can only be used on Java; ignoring analysis request for EA: "+curEA.getLib()+" !");
        }
        else{
            //creates report directory
            File reportDirectory = new File(curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS);
            reportDirectory.mkdir();

            //execute csMetricGeneration cmd
            String csMetricGenerationCmd = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CS_METRIC_GENERATION_CMD_KEY);
            String sourcesPath = curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + "src" + FileTools.FILE_SEPARATOR_SLASH_STR;
            String cmd = csMetricGenerationCmd + " " + sourcesPath + " " + reportDirectory.getAbsolutePath();
            logger.info("Starting csMetricGeneration Analysis : " + cmd);

            try {
				SystemIO.exec(cmd, this.logger, null);
			}
            catch (SystemIOException e) {
				throw new AnalysisException(e);
			}
        	generatePackageMetrics(curEA);
        }
    }

	@Override
    protected  AnalysisResult analysisCheck(EA curEA) {
    	AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
		if (StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")) {
    		result.setSuccess(checkForWellFormedXmlFile(curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_CLS_FILE));
	    	if (result.isSuccess()) {
	    		result.setSuccess(checkForWellFormedXmlFile(curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_MET_FILE));
	    	}
		}
    	return result;
    }
    
    private static final String ELT_TAG = "elt";
    private static final String PARENT_TAG = "parent";
    private static final String NAME_ATTR = "name";
    
    protected void generatePackageMetrics(EA curEA) throws AnalysisException {
    	File classMetricFile = new File(curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_CLS_FILE);
    	File packageMetricFile = new File(curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_PKG_FILE);
    	if (classMetricFile.exists()) {
	    	Document xmlDocument = ParserUtil.parseDefinition(classMetricFile.getAbsolutePath());
	        if (xmlDocument != null) {
	            NodeList classList = xmlDocument.getElementsByTagName(ELT_TAG);
	            if (classList != null) {
	            	Map pkgMap = new HashMap();
	            	Node cls = null;
	                for (int i = 0; i < classList.getLength(); i++) {
	                	cls = classList.item(i);
	                	addPackage(cls, pkgMap);
	                }
		            try {
						generatePackageFile(pkgMap, packageMetricFile);
					}
		            catch (IOException e) {
		            	throw new AnalysisException("Error during package metric generation", e);
					}
	            }
	        }
    	}
    }
    
    protected void addPackage(Node elt, Map pkgMap) {
    	if (elt != null) {
    		NodeList classChildrenNodes = elt.getChildNodes();
    		if (classChildrenNodes != null) {
            	Node pkg = null;
            	ElementMetricsBean currentPkg = null;
                MetriqueBean met = null;
                for (int i = 0; i < classChildrenNodes.getLength(); i++) {
                	pkg = classChildrenNodes.item(i);
                	if (PARENT_TAG.equals(pkg.getNodeName())) {
                        NamedNodeMap fattributes = pkg.getAttributes();
                        String pkgName = (String) fattributes.getNamedItem(NAME_ATTR).getNodeValue();
                        currentPkg = (ElementMetricsBean)pkgMap.get(pkgName);
                        if (currentPkg == null) {
                        	currentPkg = new ElementMetricsBean();
                        	currentPkg.setDesc(pkgName);
                        	pkgMap.put(pkgName, currentPkg);
                        	addMissingPackages(pkgName, pkgMap);
                        }
                        met = currentPkg.getMetrique("NB_CLASS");
                        if (met == null) {
	                        met = new MetriqueBean();
	                        met.setId("NB_CLASS");
	                        met.setValbrute(1);
                        }
                        currentPkg.addAndIncMetrique(met);
                		break;
                	}
                }
			}
		}
    }
    
    protected void generatePackageFile(Map pkgMap, File pkgFile) throws IOException {
    	List keyList = new ArrayList(pkgMap.keySet());
    	Collections.sort(keyList);
    	FileWriter fw = new FileWriter(pkgFile);
    	PrintWriter pw = new PrintWriter(fw);
        pw.println("<Result>");
    	Iterator i = keyList.iterator();
    	String pkgDesc = null;
    	ElementMetricsBean currentPkg = null;
    	Collection metricColl = null;
    	MetriqueBean met = null;
    	while (i.hasNext()) {
    		pkgDesc = (String)i.next();
    		currentPkg = (ElementMetricsBean)pkgMap.get(pkgDesc);
    		pw.write("<elt type=\"PKG\" name=\"");
    		pw.write(currentPkg.getDesc() + "\" lib=\"" + currentPkg.getDesc() + "\">");
    		pw.println();
    		printParent(currentPkg.getDesc(), pw);
            metricColl = currentPkg.getMetricCollection();
            Iterator metIt = metricColl.iterator();
            while (metIt.hasNext()) {
            	met = (MetriqueBean)metIt.next();
            	pw.write("<metric id=\"");
            	pw.write(met.getId().toUpperCase());
            	pw.write("\" value=\"");
            	pw.write("" + met.getValbrute());
            	pw.write("\" />");
            	pw.println();
            }
            pw.write("</elt>");
            pw.println();
        }
        pw.println("</Result>");
        pw.flush();
        pw.close();
    }
    
    private void addMissingPackages(String pkgName, Map pkgMap) {
    	if (pkgName != null && pkgName.length() > 0) {
    		String tmp = pkgName;
    		int lastDotIdx;
    		ElementMetricsBean currentPkg = null;
            while ((lastDotIdx = tmp.lastIndexOf('.')) > 0) {
    			tmp = tmp.substring(0, lastDotIdx);
    			if (pkgMap.get(tmp) == null) {
    				currentPkg = new ElementMetricsBean();
                	currentPkg.setDesc(tmp);
                	pkgMap.put(tmp, currentPkg);
    			}
    			else {
    				// Package exists, so upper exists...
    				break;
    			}
    		}
    	}
    }
    
    private void printParent(String desc, PrintWriter pw) {
    	if (desc != null && desc.length() > 0) {
    		int dotIdx = desc.lastIndexOf('.');
    		if (dotIdx > 0) {
	    		String parentName = desc.substring(0, dotIdx);
				pw.write("<parent name=\"");
				pw.write(parentName + "\" />");
				pw.println();
    		}
    	}
    }
    
    @Override
    protected void loadData(EA curEA) throws LoaderException {
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("CS Metric generation can only be used on Java; ignoring loading request for EA: "+curEA.getLib()+" !");
        }
        else{

	        //where are the reports to load ?
	        DataFile pkgFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_PKG_FILE, true);
	        DataFile clsFile = new DataFile(DataFileType.CLS, curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_CLS_FILE, true);
	        DataFile metFile = new DataFile(DataFileType.MET, curEA.getTargetDirectory() + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS + FileTools.FILE_SEPARATOR_SLASH_STR + Constants.CS_METRIC_GENERATION_REPORTS_MET_FILE, false);
	
	        ProjectBean projectBean = new ProjectBean();
	        projectBean.setId(this.config.getProjectId());

	        BaselineBean baselineBean = new BaselineBean();
	        baselineBean.setId(this.config.getBaselineId());
	        
	        ElementBean eaElt = new ElementBean();
	    	eaElt.setId(curEA.getId());
	        
	        //load Data
	        FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
	        loader.setMainTool(true);

	        loader.load(pkgFile);
        	loader.load(clsFile);
        	loader.load(metFile);
	        //END load Data
        }
    }
    
}

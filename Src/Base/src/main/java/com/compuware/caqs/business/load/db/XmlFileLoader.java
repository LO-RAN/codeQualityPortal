package com.compuware.caqs.business.load.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.util.StringUtils;
import com.compuware.toolbox.util.xml.ParserUtil;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe de chargement des donn�es issues d'un fichier pivot XML en base de donn�es.
 * @author cwfr-fdubois
 *
 */
public class XmlFileLoader extends AbstractFileLoader {
	
    //logger
    static private Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    /**
     * Create a new instance of XmlFileLoader.
     * @param superElement the current EA.
     * @param projectBean the current project.
     * @param baselineBean the current baseline.
     */
    public XmlFileLoader(ElementBean superElement, ProjectBean projectBean, BaselineBean baselineBean) {
    	super(superElement, projectBean, baselineBean);
	}

	/* The tag used to identify an element in the XML result file. */
    private static final String ELT_TAG = "elt";
	/* The tag used to identify a parent element in the XML result file. */
    private static final String PARENT_TAG = "parent";

    /**
     * Load an XML result file in the database.
     * @param eltFile the element XML result file to load.
     */
    @Override
    protected void loadEltData(File eltFile) throws LoaderException {
    	Document xmlDocument = ParserUtil.parseDefinition(eltFile.getAbsolutePath());
        if (xmlDocument != null) {
            NodeList metList = xmlDocument.getElementsByTagName(ELT_TAG);
            if (metList != null) {
                List<ElementMetricsBean> eltMetList = new ArrayList<ElementMetricsBean>();
                Node elt = null;
                ElementMetricsBean eltMetBean = null;
                logger.debug("Loading elements:"+metList.getLength());
                logger.debug("main tool:"+mainTool);
                for (int i = 0; i < metList.getLength(); i++) {
                    elt = metList.item(i);
                    eltMetBean = extractElement(elt);
                    eltMetBean.setParentName(extractParentName(elt));
                    eltMetList.add(eltMetBean);
                }
                loadData(eltMetList, UpdatePolicy.ERASE);
            }
            else {
            	logger.error("No element found");
        		throw new LoaderException("caqs.load.error.emptyfile", "Metric file is empty");
            }
        }
        else {
        	logger.error("File not found or parsing failed : "+eltFile.getAbsolutePath());
    		throw new LoaderException("caqs.load.error.filenotfoundorparsingfailed", "Metric file not found or parsing failed");
        }
    }

    /** The element description attribute name. */
    private static final String ELT_DESC_ATTR = "name";
    /** The element lib attribute name. */
    private static final String ELT_LIB_ATTR = "lib";
    /** The element type attribute name. */
    private static final String ELT_TYPE_ATTR = "type";
    /** The element file path. */
    private static final String ELT_FILE_PATH_ATTR = "filepath";
    /** The element starting line. */
    private static final String ELT_STARTLINE_ATTR = "startLine";

    /**
     * Extract the element informations from the given XML result node.
     * @param elt the current XML node.
     * @param typeElt the element type required.
     * @return the element and its associated metrics.
     */
    private ElementMetricsBean extractElement(Node elt) {
    	ElementMetricsBean result = null;
    	if (elt != null) {
    		result = new ElementMetricsBean();
            NamedNodeMap fattributes = elt.getAttributes();
            String desc = fattributes.getNamedItem(ELT_DESC_ATTR).getNodeValue();
    		result.setDesc(desc.replaceAll(" ", ""));
            String lib = null;
            if (fattributes.getNamedItem(ELT_LIB_ATTR) != null) {
                lib = fattributes.getNamedItem(ELT_LIB_ATTR).getNodeValue();
            }
            else {
            	lib = StringUtils.getStringBetween(result.getDesc(), '.', '(', 64);
            }
    		result.setLib(lib);
            String type = fattributes.getNamedItem(ELT_TYPE_ATTR).getNodeValue();
    		result.setTypeElt(type);
    		if (fattributes.getNamedItem(ELT_FILE_PATH_ATTR) != null) {
    			String filePath = fattributes.getNamedItem(ELT_FILE_PATH_ATTR).getNodeValue();
    			result.setFilePath(filePath);
    		}
    		if (fattributes.getNamedItem(ELT_STARTLINE_ATTR) != null) {
    			String startLine = fattributes.getNamedItem(ELT_STARTLINE_ATTR).getNodeValue();
    			if (startLine != null && startLine.matches("[0-9]+")) {
    				result.setLinePos(Integer.parseInt(startLine));
    			}
    		}
    		result.setProject(project);
    		result.setMetricCollection(extractMetrics(elt));
    	}
    	return result;
    }

    private static final String METRIC_ID_ATTR = "id";
    private static final String METRIC_VALUE_ATTR = "value";
    private static final String METRIC_LINES_ATTR = "lines";
    private static final String METRIC_NODE_NAME = "metric";

    /**
     * Extrait les m�triques � partir de l'�l�ment XML donn�.
     * @param elt l'�l�ment XML en cours.
     * @return la collection de m�triques.
     */
    private Collection<MetriqueBean> extractMetrics(Node elt) {
    	Collection<MetriqueBean> result = new ArrayList<MetriqueBean>();
    	MetriqueBean met = null;
    	String id = null;
    	String valueStr = null;
    	String linesStr = null;
        NodeList metricList = elt.getChildNodes();
        if (metricList != null) {
            for (int i = 0; i < metricList.getLength(); i++) {
                Node metric = metricList.item(i);
                if (metric.getNodeName().equalsIgnoreCase(METRIC_NODE_NAME)) {
	                NamedNodeMap attributes = metric.getAttributes();
	                if (attributes != null) {
	                    met = new MetriqueBean();
	                    id = attributes.getNamedItem(METRIC_ID_ATTR).getNodeValue();
	                    valueStr = attributes.getNamedItem(METRIC_VALUE_ATTR).getNodeValue();
	                    if (attributes.getNamedItem(METRIC_LINES_ATTR) != null) {
	                    	linesStr = attributes.getNamedItem(METRIC_LINES_ATTR).getNodeValue();
	                    }
	                    else {
	                    	linesStr = null;
	                    }
	                    met.setId(IDCreator.formatId(id));
	                    met.setValbrute(Double.parseDouble(valueStr.trim()));
	                    if (linesStr != null) {
                                // remove potentially harming white spaces 
                                linesStr=linesStr.replaceAll("\\s", "");
	                    	met.setLines(linesStr.split(","));
	                    }
	                    result.add(met);
	                }
                }
            }
        }
    	return result;
    }

    /**
     * Extrait la description du parent � partir de l'�l�ment donn�.
     * @param elt l'�l�ment XML method en cours.
     * @return le parent trouv�.
     */
    private String extractParentName(Node elt) {
    	String result = null;
        NodeList subList = elt.getChildNodes();
        if (subList != null) {
            for (int i = 0; i < subList.getLength() && result == null; i++) {
                Node subElt = subList.item(i);
                if (subElt.getNodeName().equalsIgnoreCase(PARENT_TAG)) {
	                NamedNodeMap attributes = subElt.getAttributes();
	                if (attributes != null) {
	                    result = attributes.getNamedItem(ELT_DESC_ATTR).getNodeValue();
	                }
                }
            }
        }
     	return result;
    }

}

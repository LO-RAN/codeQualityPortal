/**
 * 
 */
package com.compuware.caqs.business.load.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteElement;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;

/**
 * Classe de chargement des données issues d'un fichier pivot XML en base de données.
 * @author cwfr-fdubois
 *
 */
public class XmlCopyPasteFileLoader extends AbstractFileLoader {
	
    //logger
    static private Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    /** The base directory. */
    private String baseDir = null;
    
    /**
     * Create a new instance of XmlFileLoader.
     * @param superElement the current EA.
     * @param projectBean the current project.
     * @param baselineBean the current baseline.
     */
    public XmlCopyPasteFileLoader(ElementBean superElement, ProjectBean projectBean, BaselineBean baselineBean) {
    	super(superElement, projectBean, baselineBean);
	}
    
	/**
	 * Load the data from the given DataFile.
	 * @param file the XML result file.
	 */
    public void load(DataFile file) throws LoaderException {
    	logger.debug("Loading:"+file.getPath());
		if (file != null) {
			File f = new File(file.getPath());
			loadEltData(f);
		}
	}

    /**
	 * @param baseDir the baseDir to set
	 */
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
     * Load the data from the given DataFile list.
	 * @param fileList a list of XML result file.
     */
	public void load(List<DataFile> fileList) throws LoaderException {
		if (fileList != null) {
			DataFile f = null;
			Iterator<DataFile> i = fileList.iterator();
			while (i.hasNext()) {
				f = (DataFile)i.next();
				load(f);
			}
		}
	}

    /**
     * Load an XML result file in the database.
     * @param eltFile the element XML result file to load.
     */
    protected void loadEltData(File eltFile) throws LoaderException {
    	try {
        	DaoFactory daoFactory = DaoFactory.getInstance(); 
        	ElementDao elementDao = daoFactory.getElementDao();
        	MetriqueDao metricDao = daoFactory.getMetriqueDao();
        	Collection<ElementBean> eltColl = elementDao.retrieveExistingElement(superElement.getId());
        	Map<String, ElementBean> eltMap = createMap(eltColl);
        	
        	SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

	    	XmlFileHandler handler = new XmlFileHandler(eltMap, baseDir);
	    	parser.parse(eltFile, handler);
	    	
	    	Map<String, MetriqueBean> metricMap = handler.getMetricMap();
	    	List<CopyPasteBean> copyPasteList = handler.getCopyPasteList();
	    	if (metricMap != null && copyPasteList != null) {
	    		metricDao.insertMetrics(metricMap, this.baseline.getId());
	    		elementDao.insertCopyPaste(copyPasteList, this.baseline.getId());
	    	}
    	}
    	catch (ParserConfigurationException e) {
    		throw new LoaderException("Error loading data", e);
		}
    	catch (SAXException e) {
    		throw new LoaderException("Error loading data", e);
		}
    	catch (IOException e) {
    		throw new LoaderException("Error loading data", e);
		}
    	catch (DataAccessException e) {
    		throw new LoaderException("Error loading data", e);
    	}
    }	
    
    private class XmlFileHandler extends DefaultHandler {
    	
        private static final String DUPLICATION_TAG = "duplication";
        private static final String DUPLICATION_LINES_ATTR = "lines";
        
        private static final String FILE_TAG = "file";
        private static final String FILE_LINE_ATTR = "line";
        private static final String FILE_PATH_ATTR = "path";
        
        private List<CopyPasteBean> copyPasteList = new ArrayList<CopyPasteBean>();
        private Map<String, MetriqueBean> metricMap = new HashMap<String, MetriqueBean>();
    	private CopyPasteBean currentDupplication = null;
    	
    	private Map<String,ElementBean> eltMap = null;
    	private String baseDir = null;
    	
    	public XmlFileHandler(Map<String,ElementBean> eltMap, String baseDir) {
    		this.eltMap = eltMap;
    		this.baseDir = baseDir;
    	}
    	
    	public List<CopyPasteBean> getCopyPasteList() {
    		return this.copyPasteList;
    	}
    	
    	public Map<String, MetriqueBean> getMetricMap() {
    		return this.metricMap;
    	}
    	
		public void startDocument() throws SAXException {
		}
		
		public void endDocument() throws SAXException {
		}
		
		public void startElement(String uri,
				String localName,
				String qName,
				Attributes attributes) throws SAXException{
			if(DUPLICATION_TAG.equals(qName)) {
				currentDupplication = new CopyPasteBean();
				currentDupplication.setId(IDCreator.getID());
				currentDupplication.setLines(Integer.parseInt(attributes.getValue(DUPLICATION_LINES_ATTR)));
				this.copyPasteList.add(currentDupplication);
			}
			else if(FILE_TAG.equals(qName)) {
				ElementBean eltBean = this.eltMap.get(extractElementDescription(attributes.getValue(FILE_PATH_ATTR), this.baseDir));
				if (eltBean != null) {
					CopyPasteElement elt = new CopyPasteElement();
					elt.setIdElt(eltBean.getId());
					elt.setLine(Integer.parseInt(attributes.getValue(FILE_LINE_ATTR)));
					this.currentDupplication.addElement(elt);
					MetriqueBean copyMetric = null;
					if (!this.metricMap.containsKey(eltBean.getId())) {
						copyMetric = new MetriqueBean();
						copyMetric.setId("COPY_PASTE");
						copyMetric.addLine("" + elt.getLine());
						this.metricMap.put(eltBean.getId(), copyMetric);
					}
					copyMetric = this.metricMap.get(eltBean.getId());
					if (copyMetric != null) {
						copyMetric.setValbrute(copyMetric.getValbrute() + 1);
					}
				}
			}
		}
		
		public void endElement(String uri,
				String localName,
				String qName) throws SAXException{
		}
		
		public void characters(char[] text, int start, int length)
	     throws SAXException {
	    }	
		
		private String extractElementDescription(String pFilePath,
                String pBaseDir) {
			String result = pFilePath.substring(pBaseDir.length() - 1);
			result = result.replaceAll("\\\\", "/");
			result = result.substring(0, result.indexOf('.'));
			result = result.replaceAll("/", ".");
			if (result.startsWith(".")) {
				result = result.substring(1);
			}
			return result;
		}
		
	};  
    
}

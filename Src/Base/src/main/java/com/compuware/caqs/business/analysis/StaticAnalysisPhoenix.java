package com.compuware.caqs.business.analysis;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ParagraphBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.StringUtils;

public class StaticAnalysisPhoenix extends StaticAnalysisMcCabe {

	public StaticAnalysisPhoenix() {
		super();
        logger.info("Tool is Phoenix");
	}

	@Override
    protected void loadData(EA curEA) throws LoaderException {
        boolean ok = true;
        if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cobol")){
            logger.info("Phoenix Analysis can only be used on Cobol; ignoring request for EA: "+curEA.getLib()+" !");
            // On ignore le chargement...
            ok = true;
        }
        else{
	        super.loadData(curEA);
	        try {
	        	Map progParagraphsMap = loadParagraph(curEA);
				ok = loadMetricLines(curEA, progParagraphsMap);
			}
	        catch (IOException e) {
				throw new LoaderException("Error during Phoenix data load", e);
			}
	        catch (DataAccessException e) {
	        	throw new LoaderException("Error during Phoenix data insert into database", e);
			}
        }
    }
	
    protected static final String PHOENIX_DIR = "phoenix";
    protected static final String METRICS_DIR = "metrics";
    protected static final String PERFORM_DIR = "performs";
    
    protected Map loadParagraph(EA curEA) throws IOException{
        File fileDir = new File(curEA.getTargetDirectory()+File.separator+PHOENIX_DIR+File.separator+PERFORM_DIR+File.separator);
        File[] files = fileDir.listFiles();
        File current;
        Map progParagraphs = new HashMap();
        if (files != null) {
        	for (int i = 0; i < files.length; i++) {
        		current = files[i];
        		progParagraphs.put(getProgName(current), loadParagraphs(curEA, current));
        	}
        }
        return progParagraphs;
    }
	
    private String getProgName(File f) {
    	String result = null;
    	if (f != null) {
    		result = f.getName().substring(0, f.getName().indexOf('.'));
    	}
    	return result;
    }
    
    protected List loadParagraphs(EA curEA, File current) throws IOException{
		List paragraphs = new ArrayList();
    	if (current.exists()) {
    		String progName = getProgName(current);
    		CSVReader reader = new CSVReader(new FileReader(current), ';');
    	    String [] nextLine;
    	    String paragraph;
    	    ParagraphBean bean;
    	    while ((nextLine = reader.readNext()) != null) {
    	        // nextLine[] is an array of values from the line
    	    	if ("PARAG".equals(nextLine[0])) {
	    	    	paragraph = nextLine[2];
	    	    	if (paragraph == null || paragraph.length() < 1) {
	    	    		paragraph = "FIRST";
	    	    	}
	    	    	bean = new ParagraphBean();
	    	    	bean.setName(progName + "." + paragraph);
	    	    	bean.setDebut(nextLine[4]);
	    	    	bean.setFin(nextLine[6]);
	    	    	bean.addMetric("LOC", nextLine[12]);
	    	    	bean.addMetric("VG", nextLine[13]);
	    	    	paragraphs.add(bean);
    	    	}
    	    }
    	    bean = new ParagraphBean();
    	    bean.setName(progName);
    	    bean.setDebut(0);
    	    bean.setFin(Integer.MAX_VALUE);
    	    paragraphs.add(paragraphs.size(), bean);
    	    reader.close();
    	}
    	return paragraphs;
    }
    
    protected boolean loadMetricLines(EA curEA, Map progParagraphsMap) throws DataAccessException, IOException{
        boolean ok = true;
        File fileDir = new File(curEA.getTargetDirectory()+File.separator+PHOENIX_DIR+File.separator+METRICS_DIR+File.separator);
        File[] files = fileDir.listFiles();
        File current;
        if (files != null) {
        	for (int i= 0; i < files.length; i++) {
        		current = files[i];
        		loadMetricLines(curEA, current, progParagraphsMap);
        	}
        }
        return ok;
    }
	
    protected void loadMetricLines(EA curEA, File current, Map progParagraphsMap) throws DataAccessException, IOException{
    	if (current.exists()) {
    		CSVReader reader = new CSVReader(new FileReader(current), ';');
    	    String [] nextLine = null;
    	    String metric = null;
    	    String line = null;
    	    List paragraphColl = (List)progParagraphsMap.get(getProgName(current));
    	    ParagraphBean parag;
    	    while ((nextLine = reader.readNext()) != null) {
    	        // nextLine[] is an array of values from the line
    	    	line = nextLine[0];
    	    	metric = nextLine[7];
    	    	parag = getParagraph(paragraphColl, line);
    	    	if (parag != null) {
    	    		parag.addLine(metric, line);
    	    	}
    	    	// Ajout de la ligne au paragraphe
    	    	parag = (ParagraphBean)paragraphColl.get(paragraphColl.size() - 1);
	    		parag.addLine(metric, line);
    	    }
    	    reader.close();
    	    saveMetricLines(curEA, paragraphColl);
    	}
    }
    
    private ParagraphBean getParagraph(List paragraphColl, String sLine) {
    	ParagraphBean result = null;
    	if (paragraphColl != null) {
    		ParagraphBean tmp = null;
    		int line = getLineAsInt(sLine);
    		Iterator i = paragraphColl.iterator();
    		while (result == null && i.hasNext()) {
    			tmp = (ParagraphBean)i.next();
    			if (tmp.isParagraphForLine(line)) {
    				result = tmp;
    			}
    		}
    	}
    	return result;
    }
    
    private int getLineAsInt(String sLine) {
    	int result = 0;
    	try {
    		result = Integer.parseInt(sLine);
    	}
    	catch (NumberFormatException e) {
    		logger.debug("Invalid line: returning 0");
    	}
    	return result;
    }

    protected void saveMetricLines(EA curEA, List paragraphColl) throws DataAccessException {
    	if (paragraphColl != null) {
        	DaoFactory daoFactory = DaoFactory.getInstance(); 
        	ElementDao elementFacade = daoFactory.getElementDao();
        	MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
        	Map existingMetrics = metriqueFacade.retrieveAllMetriquesMap();
    		ElementBean eltBean = null;
    		ParagraphBean parag = null;
    		Iterator i = paragraphColl.iterator();
    		while (i.hasNext()) {
    			parag = (ParagraphBean)i.next();
    			if (parag.getMetricMap() != null) {
			    	eltBean = elementFacade.retrieveUnknownElement(parag.getName(), curEA.getId(), false);
			    	if (eltBean != null) {
			    		metriqueFacade.setMetrique(eltBean, parag.getMetricMap().values(), this.config.getBaselineId(), existingMetrics, UpdatePolicy.ERASE);
			    	}
    			}
    		}
    	}
    }    
}

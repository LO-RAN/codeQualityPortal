/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.business.load.db;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.caqs.exception.DataAccessException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-fdubois
 */
public class DataLoader {

    //logger
    static private Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    protected ProjectBean project;
	protected BaselineBean baseline;
	protected ElementBean superElement;
	protected boolean mainTool = false;
	protected HashMap metricMap;

	public DataLoader(ElementBean superElement, ProjectBean projectBean, BaselineBean baselineBean) {
		this.superElement = superElement;
		this.project = projectBean;
		this.baseline = baselineBean;
		retrieveExistingMetrics();
	}

	private void retrieveExistingMetrics() {
        DaoFactory daoFactory = DaoFactory.getInstance();
		MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
		metricMap = metriqueFacade.retrieveAllMetriquesMap();
	}

    /**
     * Create an ElementBean map from the given collection of ElementBean.
     * @param coll an ElementBean collection.
     * @return an ElementBean map.
     */
    protected Map<String, ElementBean> createMap(Collection<ElementBean> coll) {
    	Map<String, ElementBean> result = new HashMap<String, ElementBean>();
    	if (coll != null) {
    		ElementBean elementBean = null;
    		Iterator<ElementBean> i = coll.iterator();
    		while (i.hasNext()) {
    			elementBean = i.next();
                if (elementBean.getDesc() != null && elementBean.getDesc().length() > 0) {
        			result.put(elementBean.getDesc(), elementBean);
                }
                else {
                    result.put(elementBean.getLib(), elementBean);
                }
    		}
    	}
    	return result;
    }

	/**
	 * @return Returns the baseline.
	 */
	public BaselineBean getBaseline() {
		return baseline;
	}
	/**
	 * @param baseline The baseline to set.
	 */
	public void setBaseline(BaselineBean baseline) {
		this.baseline = baseline;
	}
	/**
	 * @return Returns the project.
	 */
	public ProjectBean getProject() {
		return project;
	}
	/**
	 * @param project The project to set.
	 */
	public void setProject(ProjectBean project) {
		this.project = project;
	}
	/**
	 * @return Returns the superElement.
	 */
	public ElementBean getSuperElement() {
		return superElement;
	}
	/**
	 * @param superElement The superElement to set.
	 */
	public void setSuperElement(ElementBean superElement) {
		this.superElement = superElement;
	}

	/**
	 * @return Returns the mainTool.
	 */
	public boolean isMainTool() {
		return mainTool;
	}

	/**
	 * @param mainTool The mainTool to set.
	 */
	public void setMainTool(boolean mainTool) {
		this.mainTool = mainTool;
	}

    /**
     * Load a list of element and metrics in the database.
     * @param eltMetList the element with metrics list to load.
     * @param updatePolicy the update policy if metrics already exist.
     * @throws LoaderException an exception occured during the data load.
     */
    public void loadData(List<ElementMetricsBean> eltMetList, UpdatePolicy updatePolicy) throws LoaderException {
        if (eltMetList != null) {
            try {
                DaoFactory daoFactory = DaoFactory.getInstance();
                ElementDao elementDao = daoFactory.getElementDao();
                Collection<ElementBean> parentColl = elementDao.retrieveExistingElement(superElement.getId());
                Map<String, ElementBean> parentMap = createMap(parentColl);
                Map<String, ElementBean> eltMap = createMap(parentColl);
                List<String> typeList = new ArrayList<String>();
                MetriqueDao metriquesDao = daoFactory.getMetriqueDao();
                CriterionDao criterionDao = daoFactory.getCriterionDao();

                String parentName = null;
                ElementBean parent = null;
                logger.debug("Loading elements:"+eltMetList.size());
                logger.debug("main tool:"+mainTool);
                for (ElementMetricsBean eltMetBean: eltMetList) {
                    boolean linkWithEa = false;
                    eltMetBean = (ElementMetricsBean)elementDao.retrieveUnknownElement(eltMetBean, superElement.getId(), mainTool);
                    if (mainTool) {
                        parentName = eltMetBean.getParentName();
                        if (parentName == null) {
                            linkWithEa = true;
                            parentName = extractParentName(eltMetBean);
                        }
                        parent = parentMap.get(parentName);
                        if (parent != null && parent.getId() != null) {
                            if (mainTool) {
                                elementDao.setLeafElementLink(eltMetBean, parent.getId());
                            }
                        }
                        else {
                            logger.warn("Parent not found:"+eltMetBean.getParentName()+" for element:"+eltMetBean.getDesc());
                        }
                    }
                    if (eltMetBean.getId() != null) {
                        if (mainTool) {
                            if (linkWithEa) {
                                elementDao.setLeafElementLink(eltMetBean, superElement.getId());
                            }
                            if (parentMap.get(eltMetBean.getDesc()) == null) {
                                parentMap.put(eltMetBean.getDesc(), eltMetBean);
                            }
                            if (eltMetBean.getLinePos() > 0) {
                                criterionDao.initElementBaselineInformation(eltMetBean.getId(), baseline.getId(), superElement.getId(), eltMetBean.getLinePos());
                            }
                        }
                        metriquesDao.setMetrique(eltMetBean, baseline.getId(), metricMap, updatePolicy);
                    }
                    eltMap.remove(eltMetBean.getDesc());
                    if (!typeList.contains(eltMetBean.getTypeElt())) {
                        typeList.add(eltMetBean.getTypeElt());
                    }
                }
                if (mainTool) {
                    setOutOfDateElements(eltMap, typeList, elementDao);
                }
            }
            catch (DataAccessException e) {
                throw new LoaderException("caqs.load.error.dao", "Error loading data", e);
            }
        }
        else {
            logger.error("No element found");
            throw new LoaderException("caqs.load.error.emptyfile", "Metric file is empty");
        }
    }

    /**
     * Expire elements found in the given map.
     * @param eltMap an ElementBean map.
     * @param typeList supported type list.
     * @param elementFacade an element dao.
     */
    private void setOutOfDateElements(Map<String, ElementBean> eltMap, List<String> typeList, ElementDao elementFacade) {
    	if (eltMap != null && eltMap.size() > 0) {
    		Collection<ElementBean> values = eltMap.values();
    		if (values != null && values.size() > 0) {
    			elementFacade.setOutOfDateElements(values, typeList);
    		}
    	}
    }

    /**
     * Extrait la description du parent � partir de l'�l�ment donn�.
     * @param elt l'�l�ment XML method en cours.
     * @return le parent trouv�.
     */
    protected String extractParentName(ElementMetricsBean elt) {
    	String result = null;
    	if (elt != null && elt.getDesc() != null) {
    		String desc = elt.getDesc();
    		if (desc.lastIndexOf('.') > 0) {
    			result = desc.substring(0, desc.lastIndexOf('.'));
    		}
    	}
    	return result;
    }

}

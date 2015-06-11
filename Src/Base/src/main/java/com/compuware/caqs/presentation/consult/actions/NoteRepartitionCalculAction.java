package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class NoteRepartitionCalculAction extends Action {


    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
    // --------------------------------------------------------- Public Methods


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException {

        LoggerManager.pushContexte("NoteRepartitionCalcul");
        DaoFactory daoFactory = DaoFactory.getInstance();
    	ProjectDao projectFacade = daoFactory.getProjectDao();
    	Collection projectCollection = projectFacade.retrieveAllProject();
        logger.debug("ProjectCollection: "+projectCollection.size());
        calculate(projectCollection);
        LoggerManager.popContexte();

        return mapping.findForward("success");

    }
    
    private void calculate(Collection projectCollection) {
    	DaoFactory daoFactory = DaoFactory.getInstance(); 
    	BaselineDao baselineFacade = daoFactory.getBaselineDao(); 
	    Iterator i = projectCollection.iterator();
	    while (i.hasNext()) {
			ProjectBean prj = (ProjectBean)i.next();
			Collection baselineCollection = baselineFacade.retrieveValidBaselinesByProjectId(prj.getId());
			calculate(prj, baselineCollection);
	    }
    }

    private void calculate(ProjectBean prj, Collection baselineCollection) {
    	DaoFactory daoFactory = DaoFactory.getInstance(); 
    	ElementDao elementFacade = daoFactory.getElementDao();
	    Collection eaCollection = null;
	    BaselineBean blineBean = null;
	    Iterator i = baselineCollection.iterator();
	    while (i.hasNext()) {
			blineBean = (BaselineBean)i.next();
	    	eaCollection = elementFacade.retrieveElementByType(prj.getId(), ElementType.EA, blineBean.getDmaj());
			calculate(prj, blineBean, eaCollection);
	    }
    }

    private void calculate(ProjectBean prj, BaselineBean baseline, Collection eaCollection) {
        DaoFactory daoFactory = DaoFactory.getInstance();
    	CriterionDao criterionFacade = daoFactory.getCriterionDao();
	    Iterator i = eaCollection.iterator();
	    while (i.hasNext()) {
			ElementBean ea = (ElementBean)i.next();
			criterionFacade.calculateCriterionNoteRepartition(ea.getId(), prj.getId(), baseline.getId());
	    }
    }
}

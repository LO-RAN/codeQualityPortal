/*
 * Baseline.java
 *
 * Created on 24 mars 2003, 11:24
 */

package com.compuware.caqs.business.calculation;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class Baseline extends BaselineBean {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1976604640810759358L;

	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_CALCUL_LOGGER_KEY);
    
    private Baseline previousBaseline;
    
    public Baseline() {
    }
    
    public Baseline(BaselineBean baseline) {
        this.setId(baseline.getId());
        this.setDinst(baseline.getDinst());
        this.setDmaj(baseline.getDmaj());
        this.setLib(baseline.getLib());
        this.setProject(baseline.getProject());
    }
    
    public Baseline getPreviousBaseline() {
        return previousBaseline;
    }
    
    public void setPreviousBaseline(Baseline previousBaseline) {
        this.previousBaseline = previousBaseline;
    }
    
    public boolean existPreviousBaseline() {
        String previousBaselineId = "";
        boolean retour = false;
        if(this.previousBaseline!=null) {
        	previousBaselineId = this.previousBaseline.getId();
            retour = !id.equals(previousBaselineId);
        }
        return retour;
    }
    
}

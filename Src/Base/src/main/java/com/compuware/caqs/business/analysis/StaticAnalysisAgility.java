/*
 * StaticAnalysisAgility.java
 *
 */

package com.compuware.caqs.business.analysis;

import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;
import java.io.File;

/**
 *
 * @author  cwfr-lizac
 */
public class StaticAnalysisAgility extends StaticAnalysisAntGeneric {

    private static final String XMLFILENAME = File.separator + "agilityreports" + File.separator + "report.xml";
	
    /** Creates a new instance of StaticAnalysisDevPartnerRuleByRule */
    public StaticAnalysisAgility() {
        super() ;
        logger.info("Tool is Agility");
    }
    
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysisAntGeneric#getXmlFileReportPath()
	 */
	@Override
	protected String getXmlFileReportPath() {
    	return XMLFILENAME;
	}
    
	@Override
	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
		if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "java")){
            logger.info("Agility Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "agilityAnalysis";
	}


    @Override
    protected void postToolAnalysis() {
    }
}

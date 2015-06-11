/*
 * StaticAnalysisDevPartnerRuleByRule.java
 *
 * Created on July 13, 2004, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;

import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

/**
 *
 * @author  cwfr-fdubois
 */
public class StaticAnalysisJdt extends StaticAnalysisAntGeneric {

    private static final String XMLFILENAME = File.separator + "jdtreports" + File.separator + "report.xml";
	
    /** Creates a new instance of StaticAnalysisDevPartnerRuleByRule */
    public StaticAnalysisJdt() {
        super() ;
        logger.info("Tool is JDT Compiler");
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
            logger.info("JDT Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "jdtAnalysis";
	}


    @Override
    protected void postToolAnalysis() {
    }
}

/*
 * StaticAnalysisDevPartnerRuleByRule.java
 *
 * Created on July 13, 2004, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

/**
 *
 * @author  cwfr-fxalbouy
 */
public class StaticAnalysisAntDynamic extends StaticAnalysisAntGeneric {

    private static final String XMLFILENAME = File.separator + "checkstylereports" + File.separator + "report.xml";

    private String targetName;
    private String supportedLanguage;

    public StaticAnalysisAntDynamic() {
        super() ;
        logger.info("Dynamic Ant analysis");
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysisAntGeneric#getXmlFileReportPath()
	 */
	@Override
	protected String getXmlFileReportPath() {
    	return File.separator + this.targetName + "reports" + File.separator + "report.xml";
	}

    protected void toolAnalysis(EA curEA) throws AnalysisException {
        if (isAnalysisPossible(curEA)) {
            logger.info("Start analysis for ant target " + this.targetName);
            super.toolAnalysis(curEA);
        }
    }

	@Override
	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
		if(!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), this.supportedLanguage)){
                    logger.info("Analysis can only be used on " + this.supportedLanguage+"; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return this.targetName;
	}

    @Override
    protected void postToolAnalysis() {
    }


}

/**
 * 
 */
package com.compuware.caqs.business.analysis;

import java.io.File;

import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

/**
 * @author cwfr-fdubois
 *
 */
public class StaticAnalysisPmd extends StaticAnalysisAntGeneric {

    public static final String XMLFILENAME = File.separator + "pmdreports" + File.separator + "report.xml";

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
            logger.info("PMD Analysis can only be used on Java; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "pmd";
	}


    @Override
    protected void postToolAnalysis() {
    }
}

/*
 * StaticAnalysisAbap.java
 *
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.Properties;

import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

public class StaticAnalysisAbap extends StaticAnalysisAntGeneric {
    
    public static final String XMLFILENAME = File.separator + "abapreports" + File.separator + "report.xml";

    /** Creates a new instance of StaticAnalysisAbap */
    public StaticAnalysisAbap() {
        super();
        logger.info("Tool is SAP Code Inspector");
    }
    
	/* (non-Javadoc)
	 * @see com.compuware.caqs.business.analysis.StaticAnalysisAntGeneric#getXmlFileReportPath()
	 */
	@Override
	protected String getXmlFileReportPath() {
    	return XMLFILENAME;
	}
    @Override
    protected void initSpecific(Properties dbProps){
    }
    
	@Override
	protected boolean isAnalysisPossible(EA curEA) {
		boolean result = true;
        if( !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "abap") ) {
            logger.info("SAP Code Inspector Analysis can only be used on Abap; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "codeinspectorAnalysis";
	}


    @Override
    protected void postToolAnalysis() {
    }
}

/*
 * StaticAnalysisSplint.java
 *
 * Created on July 31, 2007, 11:27 AM
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.util.Properties;

import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

public class StaticAnalysisSplint extends StaticAnalysisAntGeneric {
    
    public static final String XMLFILENAME = File.separator + "splintreports" + File.separator + "report.xml";

    /** Creates a new instance of StaticAnalysisSplint */
    public StaticAnalysisSplint() {
        super();
        logger.info("Tool is Splint");
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
        if( !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "cpp") 
    		&& !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "ansic")
    		&& !StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "proc") ) {
            logger.info("Splint Analysis can only be used on C/C++/Pro*C; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
		return result;
	}

	@Override
	protected String getAntTarget() {
		return "splintAnalysis";
	}
    

    @Override
    protected void postToolAnalysis() {
    }
}

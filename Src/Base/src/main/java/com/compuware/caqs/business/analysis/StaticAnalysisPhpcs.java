/*
 * StaticAnalysisPhpcs.java
 *
 * Created on July 13, 2004, 11:27 AM
 */
package com.compuware.caqs.business.analysis;

import java.io.File;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlCopyPasteFileLoader;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

/**
 *
 * @author  cwfr-lizac
 */
public class StaticAnalysisPhpcs extends StaticAnalysisAntGeneric {

    private static final String XMLFILENAME = File.separator + "phpcsreports" + File.separator + "report.xml";
    private static final String CPD_FILE = File.separator + "phpcsreports" + File.separator + "cpd.xml";

    /** Creates a new instance of StaticAnalysisPhpcs */
    public StaticAnalysisPhpcs() {
        super();
        logger.info("Tool is PHP CodeSniffer");
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
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "PHP")) {
            logger.info("PHP CodeSniffer Analysis can only be used on PHP; ignoring request for EA: "+curEA.getLib()+" !");
            result = false;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.business.analysis.StaticAnalysis#loadData(com.compuware.caqs.business.load.EA)
     */
    @Override
    protected void loadData(EA curEA) throws LoaderException {
        if (isAnalysisPossible(curEA)) {
            DataFile clsFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + getXmlFileReportPath(), true);

            ProjectBean projectBean = new ProjectBean();
            projectBean.setId(this.config.getProjectId());

            BaselineBean baselineBean = new BaselineBean();
            baselineBean.setId(this.config.getBaselineId());

            ElementBean eaElt = new ElementBean();
            eaElt.setId(curEA.getId());

            FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
            loader.setMainTool(this.config.isMasterTool());

            loader.load(clsFile);

            // is there any copy paste detection available ?
            File cpdToFile = new File(curEA.getTargetDirectory() + CPD_FILE);
            if (cpdToFile.exists()) {
                logger.info("    - Copy/Paste file");
                XmlCopyPasteFileLoader copyPasteloader = new XmlCopyPasteFileLoader(eaElt, projectBean, baselineBean);
                copyPasteloader.setBaseDir(curEA.getTargetDirectory() + "/src/");
                DataFile cpdFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + CPD_FILE, true);
                copyPasteloader.load(cpdFile);
            }
        }
    }

    @Override
    protected String getAntTarget() {
        return "phpcsAnalysis";
    }

    @Override
    protected void postToolAnalysis() {
    }
}

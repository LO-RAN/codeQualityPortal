/**
 * 
 */
package com.compuware.caqs.business.analysis;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlCopyPasteFileLoader;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import java.io.File;

import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;

/**
 * @author cwfr-fdubois
 *
 */
public class StaticAnalysisFlexPmd extends StaticAnalysisAntGeneric {

    private static final String NCSS_FILE = File.separator + "flexpmdreports"
            + File.separator + "traited_ncss.xml";
    private static final String PMD_FILE = File.separator + "flexpmdreports"
            + File.separator + "traited_pmd.xml";
    private static final String CPD_FILE = File.separator + "flexpmdreports"
            + File.separator + "cpd.xml";

    /**
     * @{@inheritDoc }
     */
    @Override
    protected String getXmlFileReportPath() {
        return NCSS_FILE;
    }

    @Override
    protected boolean isAnalysisPossible(EA curEA) {
        boolean result = true;
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "flex")) {
            logger.info("FlexPMD Analysis can only be used on Flex; ignoring request for EA: " + curEA.getLib() + " !");
            result = false;
        }
        return result;
    }

    @Override
    protected String getAntTarget() {
        return "flexpmdAnalysis";
    }

    @Override
    protected void postToolAnalysis() {
        //we have a javancss.xml to convert as report.xml (caqs format)
    }

    /**
     * @{@inheritDoc }
     */
    @Override
    protected AnalysisResult analysisCheck(EA curEA) {
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(true);
        if (isAnalysisPossible(curEA)) {
            result.setSuccess(checkForWellFormedXmlFile(curEA.getTargetDirectory()
                    + getXmlFileReportPath()));
        }
        return result;
    }

    @Override
    protected void loadData(EA curEA) throws LoaderException {
        //load Data
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "flex")) {
            logger.info("FlexPMD Analysis can only be used on Flex; ignoring request for EA: " + curEA.getLib() + " !");
            // On ignore le chargement...
        } else {
            logger.info("Loading FlexPMD result files:");

	        ProjectBean projectBean = new ProjectBean();
	        projectBean.setId(this.config.getProjectId());

	        BaselineBean baselineBean = new BaselineBean();
	        baselineBean.setId(this.config.getBaselineId());

	        ElementBean eaElt = new ElementBean();
	    	eaElt.setId(curEA.getId());
	    	eaElt.setSourceDir(curEA.getTargetDirectory());


            FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
            loader.setMainTool(true);

            // first load ncss.xml as it contains the full perimeter of audited elements
            // with their structural metrics
            DataFile ncssFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + NCSS_FILE, true);
            loader.load(ncssFile);

            // from there, we are not more using the main tool, i.e. we will add data regarding existing elements only
            loader.setMainTool(false);
            // load pmd detections
            DataFile pmdFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + PMD_FILE, true);
            loader.load(pmdFile);


            // load cpd detections
            File cpdToFile = new File(curEA.getTargetDirectory() + CPD_FILE);
            if (cpdToFile.exists()) {
                logger.info("    - Copy/Paste file");
                XmlCopyPasteFileLoader copyPasteloader = new XmlCopyPasteFileLoader(eaElt, projectBean, baselineBean);
                copyPasteloader.setBaseDir(curEA.getTargetDirectory() + "/src/");
                DataFile cpdFile = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + CPD_FILE, true);
                copyPasteloader.load(cpdFile);
            }
        }
        //END load Data
    }
}

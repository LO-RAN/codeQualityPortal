/*
 * StaticAnalysisRestNat.java
 *
 */
package com.compuware.caqs.business.analysis;

import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.toolbox.util.StringUtils;
import java.io.File;
import java.util.Properties;

public class StaticAnalysisRestNat extends StaticAnalysisAntGeneric {

    private static final String ANO_FILE = File.separator + "restnatreports"
            + File.separator + "anoReport.xml";
    private static final String ANO_CONSTRUCT_FILE = File.separator + "restnatreports"
            + File.separator + "anoConstructReport.xml";
    private static final String QUAL_FILE = File.separator + "restnatreports"
            + File.separator + "qualReport.xml";
    private static final String QUAL_CONSTRUCT_FILE = File.separator + "restnatreports"
            + File.separator + "qualConstructReport.xml";

    /** Creates a new instance of StaticAnalysisCodeAnalyzerPro */
    public StaticAnalysisRestNat() {
        super();
        logger.info("Tool is RestNat");
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.business.analysis.StaticAnalysisAntGeneric#getXmlFileReportPath()
     */
    @Override
    protected String getXmlFileReportPath() {
        return QUAL_FILE;
    }

    @Override
    protected void initSpecific(Properties dbProps) {
    }

    @Override
    protected boolean isAnalysisPossible(EA curEA) {
        boolean result = true;
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "natural")) {
            logger.info("RestNat Analysis can only be used on Natural; ignoring request for EA: " + curEA.getLib() + " !");
            result = false;
        }
        return result;
    }

    @Override
    protected String getAntTarget() {
        return "restnatAnalysis";
    }

    @Override
    protected void postToolAnalysis() {
    }

    @Override
    protected void loadData(EA curEA) throws LoaderException {
        //load Data
        if (!StringUtils.startsWithIgnoreCase(curEA.getDialecte().getId(), "natural")) {
            logger.info("RestNat Analysis can only be used on Natural; ignoring request for EA: " + curEA.getLib() + " !");
            // On ignore le chargement...
        } else {
            logger.info("Loading RestNat result files:");

            ProjectBean projectBean = new ProjectBean();
            projectBean.setId(this.config.getProjectId());

            BaselineBean baselineBean = new BaselineBean();
            baselineBean.setId(this.config.getBaselineId());

            ElementBean eaElt = new ElementBean();
            eaElt.setId(curEA.getId());
            eaElt.setSourceDir(curEA.getTargetDirectory());


            FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
            loader.setMainTool(true);

            // first load objects & units from "construct" results
            DataFile file = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + QUAL_CONSTRUCT_FILE, true);
            loader.load(file);

            // from there, we are no more using the main tool, i.e. we will add data regarding existing elements only
            loader.setMainTool(false);
            
            // overload with objects and units from results without "construct"
           file = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + QUAL_FILE, true);
            loader.load(file);

             // load anomalies from "construct" results
            file = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + ANO_CONSTRUCT_FILE, true);
            loader.load(file);

            // overload with anomalies from results without "construct"
            file = new DataFile(DataFileType.ALL, curEA.getTargetDirectory() + ANO_FILE, true);
            loader.load(file);


        }
        //END load Data
    }
}

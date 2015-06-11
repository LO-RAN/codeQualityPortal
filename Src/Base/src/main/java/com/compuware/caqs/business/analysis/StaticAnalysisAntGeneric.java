/**
 * 
 */
package com.compuware.caqs.business.analysis;

import java.io.IOException;
import java.util.Properties;

import com.compuware.caqs.business.analysis.exception.AnalysisException;
import com.compuware.caqs.business.load.db.DataFile;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.business.load.db.FileLoader;
import com.compuware.caqs.business.load.db.LoaderException;
import com.compuware.caqs.business.load.db.XmlFileLoader;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class StaticAnalysisAntGeneric extends StaticAnalysis {

    /* (non-Javadoc)
     * @see com.compuware.caqs.business.analysis.StaticAnalysis#initSpecific(java.util.Properties)
     */
    @Override
    protected void initSpecific(Properties dbProps) {
    }

    protected abstract boolean isAnalysisPossible(EA curEA);

    protected abstract String getAntTarget();

    protected abstract String getXmlFileReportPath();

    protected abstract void postToolAnalysis();

    /**
     * @{@inheritDoc }
     */
    @Override
    protected void toolAnalysis(EA curEA) throws AnalysisException {
        if (isAnalysisPossible(curEA)) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            ProjectDao projectDao = daoFactory.getProjectDao();
            ProjectBean projectBean = projectDao.retrieveProjectById(this.config.getProjectId());

            SourceManager srcManager = new SourceManager(logger);
            try {
                srcManager.manageEa(curEA.getId(), this.config.getBaselineId(), projectBean, getAntTarget(), null);
            } catch (IOException e) {
                throw new AnalysisException("IO Error during the Ant analysis", e);
            }
            this.postToolAnalysis();
        }
    }

    /**
     * @{@inheritDoc }
     */
    @Override
    protected void loadData(EA curEA) throws LoaderException {
        if (isAnalysisPossible(curEA)) {
            DataFile clsFile = new DataFile(DataFileType.CLS, curEA.getTargetDirectory()
                    + getXmlFileReportPath(), true);

            ProjectBean projectBean = new ProjectBean();
            projectBean.setId(this.config.getProjectId());

            BaselineBean baselineBean = new BaselineBean();
            baselineBean.setId(this.config.getBaselineId());

            ElementBean eaElt = new ElementBean();
            eaElt.setId(curEA.getId());

            FileLoader loader = new XmlFileLoader(eaElt, projectBean, baselineBean);
            loader.setMainTool(this.config.isMasterTool());
            logger.info("Load data");
            loader.load(clsFile);
        }
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
}

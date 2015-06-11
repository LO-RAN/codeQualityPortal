/*
 * ElementBean.java
 *
 * Created on 1 decembre 2003, 10:40
 */
package com.compuware.caqs.domain.dataschemas;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author  cwfr-fdubois
 */
public class ElementBean extends DefinitionBean implements Serializable {

    // declaration du logger
    static protected Logger logger = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    private static final long serialVersionUID = 7427651391531662189L;
    private BaselineBean baseline = null;
    private ProjectBean project = null;
    private DialecteBean dialecte = null;
    private UsageBean usage = null;
    protected String idTelt = null;
    private String stereotype = null;
    private String streamElt = null;
    private String pvobname = null;
    private String vobmountpoint = null;
    private String makefileDir = null;
    private String sourceDir = null;
    private String binDir = null;
    private String periodicDir = null;
    private String libraries = null;
    private String filePath = null;
    private int linePos = 0;
    private int poids = 1;
    private String scmRepository = null;
    private String scmModule = null;
    private String projectFilePath = null;
    private String info1 = null;
    private String info2 = null;
    private boolean hasSource;

    public ElementBean() {
    }

    /** Creates a new instance of ElementBean */
    public ElementBean(BaselineBean baseline, String id, String idTelt, String lib) {
        this.baseline = baseline;
        this.id = id;
        this.idTelt = idTelt;
        this.lib = lib;
    }

    /** {@inheritDoc}
     */
    public BaselineBean getBaseline() {
        return this.baseline;
    }

    /** {@inheritDoc}
     */
    public ProjectBean getProject() {
        return this.project;
    }

    /** {@inheritDoc}
     */
    public DialecteBean getDialecte() {
        return this.dialecte;
    }

    /** {@inheritDoc}
     */
    public UsageBean getUsage() {
        return this.usage;
    }

    /** {@inheritDoc}
     */
    public String getTypeElt() {
        return this.idTelt;
    }

    /** {@inheritDoc}
     */
    public String getStereotype() {
        return this.stereotype;
    }

    /** {@inheritDoc}
     */
    public String getStreamElt() {
        return this.streamElt;
    }

    /** {@inheritDoc}
     */
    public String getPVobName() {
        return this.pvobname;
    }

    /** {@inheritDoc}
     */
    public String getVobMountPoint() {
        return this.vobmountpoint;
    }

    /** {@inheritDoc}
     */
    public String getMakefileDir() {
        return this.makefileDir;
    }

    /** {@inheritDoc}
     */
    public String getSourceDir() {
        return this.sourceDir;
    }

    /** {@inheritDoc}
     */
    public String getFullSourceDirPath() {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String basePath = null;
        try {
            basePath = new File(dynProp.getProperty(Constants.SRC_BASE_PATH)).getCanonicalPath();
        } catch (IOException ex) {
            logger.error("Can't get full path : " + ex.getMessage());
        }
        String result = this.sourceDir;
        if (basePath != null) {
            result = FileTools.concatPath(basePath, result);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public String getBinDir() {
        return this.binDir;
    }

    /** {@inheritDoc}
     */
    public String getPeriodicDir() {
        return this.periodicDir;
    }

    /** {@inheritDoc}
     */
    public String getLibraries() {
        return this.libraries;
    }

    /** {@inheritDoc}
     */
    public int getPoids() {
        return this.poids;
    }

    /** {@inheritDoc}
     */
    public String getFilePath() {
        return filePath;
    }

    /** {@inheritDoc}
     */
    public int getLinePos() {
        return linePos;
    }

    /** {@inheritDoc}
     */
    public void setBaseline(BaselineBean baseline) {
        this.baseline = baseline;
    }

    /** {@inheritDoc}
     */
    public void setProject(ProjectBean project) {
        this.project = project;
    }

    /** {@inheritDoc}
     */
    public void setDialecte(DialecteBean dialecte) {
        this.dialecte = dialecte;
    }

    /** {@inheritDoc}
     */
    public void setUsage(UsageBean usage) {
        this.usage = usage;
    }

    /** {@inheritDoc}
     */
    public void setTypeElt(String typeElt) {
        this.idTelt = typeElt;
    }

    /** {@inheritDoc}
     */
    public void setStereotype(String stereotype) {
        this.stereotype = stereotype;
    }

    /** {@inheritDoc}
     */
    public void setStreamElt(String streamElt) {
        this.streamElt = streamElt;
    }

    /** {@inheritDoc}
     */
    public void setPVobName(String thisPvobname) {
        this.pvobname = thisPvobname;
    }

    /** {@inheritDoc}
     */
    public void setVobMountPoint(String pVobmountpoint) {
        this.vobmountpoint = pVobmountpoint;
    }

    /** {@inheritDoc}
     */
    public void setMakefileDir(String makefileDir) {
        this.makefileDir = makefileDir;
    }

    /** {@inheritDoc}
     */
    public void setSourceDir(String sourceDir) {
        if (sourceDir != null) {
            this.sourceDir = sourceDir.replaceAll("\\\\", "/");
        } else {
            this.sourceDir = sourceDir;
        }
    }

    /** {@inheritDoc}
     */
    public void setBinDir(String binDir) {
        this.binDir = binDir;
    }

    /** {@inheritDoc}
     */
    public void setPeriodicDir(String periodicDir) {
        this.periodicDir = periodicDir;
    }

    /** {@inheritDoc}
     */
    public void setLibraries(String libraries) {
        this.libraries = libraries;
    }

    /** {@inheritDoc}
     */
    public void setPoids(int poids) {
        if (poids > 0) {
            this.poids = poids;
        }
    }

    /** {@inheritDoc}
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /** {@inheritDoc}
     */
    public void setLinePos(int linePos) {
        this.linePos = linePos;
    }

    /**
     * @return Returns the projectFilePath.
     */
    public String getProjectFilePath() {
        return projectFilePath;
    }

    /**
     * @param projectFilePath The projectFilePath to set.
     */
    public void setProjectFilePath(String projectFilePath) {
        this.projectFilePath = projectFilePath;
    }

    /**
     * @return Returns the scmModule.
     */
    public String getScmModule() {
        return scmModule;
    }

    /**
     * @param scmModule The scmModule to set.
     */
    public void setScmModule(String scmModule) {
        this.scmModule = scmModule;
    }

    /**
     * @return Returns the scmRepository.
     */
    public String getScmRepository() {
        return scmRepository;
    }

    /**
     * @param scmRepository The scmRepository to set.
     */
    public void setScmRepository(String scmRepository) {
        this.scmRepository = scmRepository;
    }

    /*
     * @return Returns the Info1.
     */
    public String getInfo1() {
        return info1;
    }

    /** {@inheritDoc}
     */
    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    /** {@inheritDoc}
     */
    public String getInfo2() {
        return info2;
    }

    /** {@inheritDoc}
     */
    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    /** {@inheritDoc}
     */
    public boolean typeEquals(String type) {
        boolean result = false;
        if (idTelt != null && type != null) {
            result = idTelt.equals(type);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(super.toString());
        buffer.append(", TYPE=" + this.idTelt + "]");
        return buffer.toString();
    }

    public static String getHtmlSrcDir(String idBline, ElementBean ea,
            ProjectBean projectBean, String basePath) {
        StringBuffer result = new StringBuffer();
        result.append(basePath);
        if (!basePath.endsWith("/")) {
            result.append("/");
        }
        result.append(projectBean.getLib()).append("-").append(projectBean.getId()).append("/");
        result.append(ea.getLib()).append("-").append(ea.getId()).append("/").append(idBline);
        return result.toString();
    }

    public boolean hasSource() {
        return hasSource;
    }

    public void setHasSource(boolean hasSource) {
        this.hasSource = hasSource;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.consult.actions.upload.uploaders;

import com.compuware.caqs.business.load.db.DataLoader;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class AbstractCaqsUploader {

    protected static Logger logger = LoggerManager.getLogger("Ihm");

    /**
     * Element with associated metrics list.
     */
    protected List<ElementMetricsBean> metrics;

    /**
     * Element map.
     */
    private Map<String, ElementMetricsBean> elementMap = new HashMap<String, ElementMetricsBean>();;

    /**
     * entite applicative
     */
    protected ElementBean ea;

    /**
     * Should the uploader insert or delete elements? Default is not.
     */
    protected boolean master;

    /**
     * The uploaded file.
     */
    protected File uploadedFile;

    /**
     * Get the update policy for the upload in case of existing metrics.
     * @return the update policy for the upload.
     */
    public UpdatePolicy getUpdatePolicy() {
        return UpdatePolicy.ERASE;
    }

    /**
     * essaye de définir si le fichier en paramètre est un export correct de l'outil
     * ou non.
     * @param f le fichier à reconnaitre
     * @return <code>true</code> si le fichier envoyé en paramètre est un export
     * de l'outil, <code>false</code> sinon.
     */
    public abstract boolean isCorrectExport(File f);

    /**
     * lance l'import des metriques contenues dans le fichier reconnu
     * @return <code>true</code> si des metriques ont ete trouvees,
     * <code>false</code> sinon.
     */
    public abstract boolean extractMetrics();

    /**
     * Load the uploaded data.
     * @throws CaqsException if an error occurs during the loading.
     */
    public void loadData() throws CaqsException {
        if (this.metrics != null && this.ea != null) {
            DataLoader loader = getLoader();
            loader.loadData(this.metrics, getUpdatePolicy());
        }
        else {
            throw new CaqsException("caqs.upload.error.nodata", "Upload error: no data found");
        }
    }

    /**
     * Get a loader instance.
     * @return the data loader instance.
     */
    public DataLoader getLoader() {
        DataLoader result = new DataLoader(this.ea, this.ea.getProject(), this.ea.getBaseline());
        result.setMainTool(this.master);
        return result;
    }

    protected AbstractCaqsUploader(ElementBean e) {
        this.ea = e;
        this.metrics = new ArrayList<ElementMetricsBean>();
    }


    protected AbstractCaqsUploader() {
        this.metrics = new ArrayList<ElementMetricsBean>();
    }

    /**
     * Set the uploaded file.
     * @param uploadedFile the uploaded file.
     */
    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * @return uploaded elements and metrics list.
     */
    public List<ElementMetricsBean> getMetrics() {
        return this.metrics;
    }

    protected void addMetric(String idElt, String idMet, String libElt, String idTelt, double value) {
        ElementMetricsBean elt = this.elementMap.get(idElt);
        if (elt == null) {
            elt = new ElementMetricsBean();
            elt.setId(idElt);
            elt.setLib(libElt);
            elt.setTypeElt(idTelt);
            this.elementMap.put(idElt, elt);
            this.metrics.add(elt);
        }
        MetriqueBean met = new MetriqueBean();
        met.setId(idMet);
        met.setValbrute(value);
        elt.addMetrique(met);
    }

    protected void addMetric(String idMet, String libDescElt, String idTelt, double value, boolean isLibElt) {
        ElementMetricsBean elt = this.elementMap.get(libDescElt);
        if (elt == null) {
            elt = new ElementMetricsBean();
            if (isLibElt) {
                elt.setLib(libDescElt);
            }
            else {
                elt.setDesc(libDescElt);
            }
            elt.setTypeElt(idTelt);
            this.elementMap.put(libDescElt, elt);
            this.metrics.add(elt);
        }
        MetriqueBean met = new MetriqueBean();
        met.setId(idMet);
        met.setValbrute(value);
        elt.addMetrique(met);
    }

    /**
     * @return l'entite applicative pour laquelle on uploade
     */
    public ElementBean getApplicationEntity() {
        return this.ea;
    }

    public void setApplicationEntity(ElementBean ea) {
        this.ea = ea;
    }

    /**
     * Could the uploader insert or delete elements? Default is not.
     * @return true if the element can be inserted or deleted during the load.
     */
    public boolean isMaster() {
        return master;
    }

    /**
     * Set if the element can be inserted or deleted during the load.
     * @param master true if the element can be inserted or deleted during the load.
     */
    public void setMaster(boolean master) {
        this.master = master;
    }

}

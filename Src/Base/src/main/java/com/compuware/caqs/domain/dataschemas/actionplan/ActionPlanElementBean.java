/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.actionplan;

import com.compuware.toolbox.util.resources.Internationalizable;
import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class ActionPlanElementBean implements Serializable {
    /**
     * id
     */
    protected String id;
    /**
     * Criterion mark
     */
    protected double score = 0.0;
    /**
     * Flag to indicate if the criterion is included in the action plan
     */
    protected boolean elementCorrected = false;
    /**
     * Severity index
     */
    protected int indiceGravite = -1;
    /**
     * Getting worse, stable or better
     */
    protected int tendance = -1;
    /**
     * Score it should obtain to be declared as corrected
     */
    protected double correctedScore = 0.0;
    /**
     * Indicate if the criterion has to be corrected for the next baseline
     */
    protected ActionPlanPriority priority = ActionPlanPriority.SHORT_TERM;
    /**
     * Comment
     */
    protected String comment = "";
    /**
     * User who has made the comment
     */
    protected String commentUser;

    protected Internationalizable internationalizableProperties;

    public Internationalizable getInternationalizableProperties() {
        return internationalizableProperties;
    }

    /**
     * the element master's id for this criterion
     */
    private String elementMaster;

    public String getElementMaster() {
        return elementMaster;
    }

    /**
     * set element master.
     * @param elementMaster element master
     */
    public void setElementMaster(String elementMaster) {
        this.elementMaster = elementMaster;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    protected ActionPlanElementBean(Internationalizable i18n, String id) {
        this.id = id;
        this.internationalizableProperties = i18n;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double note) {
        this.score = note;
    }

    public int getIndiceGravite() {
        return indiceGravite;
    }

    public void setIndiceGravite(int indiceGravite) {
        this.indiceGravite = indiceGravite;
    }

    public int getTendance() {
        return tendance;
    }

    public void setTendance(int tendance) {
        this.tendance = tendance;
    }

    /**
     * @param o l'element a comparer
     * @param loc la locale pour la comparaison par libelle
     * @return -1 si cet element a une severite plus forte que celui donne en parametre, 0 si elle est egale et 1
     * si elle moins forte. La comparaison se fait d'abord sur les notes, ensuite sur les agregations, puis la repartition,
     * enfin sur le libelle traduit.
     */
    public abstract int compareSeverity(ActionPlanElementBean o, Locale loc);

    public boolean isCorrected() {
        return elementCorrected;
    }

    /**
     * set the element as included in an action plan if corrected is true, excluded
     * if corrected is false
     * @param corrected
     */
    public void setCorrected(boolean corrected) {
        this.elementCorrected = corrected;
    }

    public abstract double getCorrectedScore();

    public void setCorrectedScore(double correctedMark) {
        this.correctedScore = correctedMark;
    }

    public ActionPlanPriority getPriority() {
        return priority;
    }

    public void setPriority(ActionPlanPriority priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        boolean retour = false;
        String oId = null;
        if(o instanceof ActionPlanElementBean) {
            oId = ((ActionPlanElementBean)o).getId();
        } else if(o instanceof String) {
            oId = (String)o;
        }
        if(oId!=null) {
            retour = (this.id==null)? oId==null : oId.equals(this.id);
        }
        return retour;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

}

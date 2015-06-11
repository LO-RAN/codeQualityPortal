package com.compuware.caqs.domain.dataschemas.treemap;

import com.compuware.caqs.domain.dataschemas.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public class TreeMapElementBean extends ElementBean {
    /**
     * la note de l'element
     */
    private double mark = 0.0;

    /**
     * moyenne de la complexit� cyclomatique
     */
    private double avgVg = 0.0;

    private boolean avgVgComputed = false;

    private double ifpug = 0.0;

    private boolean ifpugComputed = false;

    private double nbFileElements = 0.0;

    private boolean nbFileElementsComputed = false;

    private double allCode = 0.0;

    private boolean allCodeComputed = false;

    private double actionsPlansCosts = 0.0;

    private boolean actionsPlansCostsComputed = false;

    /**
     * la liste des elements fils
     */
    private List<TreeMapElementBean> children;

    public TreeMapElementBean(String id) {
        super();
        this.id = id;
        this.children = new ArrayList<TreeMapElementBean>();
    }

    public TreeMapElementBean(ElementBean eb) {
        super();
        this.id = eb.getId();
        this.lib = eb.getLib();
        this.idTelt = eb.getTypeElt();
        if(eb.getProject()!=null) {
            ProjectBean p = new ProjectBean();
            p.setId(eb.getProject().getId());
            this.setProject(p);
        }
        this.children = new ArrayList<TreeMapElementBean>();
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public List<TreeMapElementBean> getChildren() {
        return children;
    }

    public void setChildren(List<TreeMapElementBean> children) {
        this.children = children;
    }

    public double getAllCode() {
        return allCode;
    }

    public void setAllCode(double allCode) {
        this.allCode = allCode;
        this.allCodeComputed = true;
    }

    public boolean isAllCodeComputed() {
        return allCodeComputed;
    }

    public double getAvgVg() {
        return avgVg;
    }

    public void setAvgVg(double avgVg) {
        this.avgVgComputed = true;
        this.avgVg = avgVg;
    }

    public boolean isAvgVgComputed() {
        return avgVgComputed;
    }

    public double getIfpug() {
        return ifpug;
    }

    public void setIfpug(double ifpug) {
        this.ifpug = ifpug;
        this.ifpugComputed = true;
    }

    public boolean isIfpugComputed() {
        return ifpugComputed;
    }

    public double getNbFileElements() {
        return nbFileElements;
    }

    public void setNbFileElements(double nbFileElements) {
        this.nbFileElementsComputed = true;
        this.nbFileElements = nbFileElements;
    }

    public boolean isNbFileElementsComputed() {
        return nbFileElementsComputed;
    }

    public double getActionsPlansCosts() {
        return actionsPlansCosts;
    }

    public void setActionsPlansCosts(double actionsPlansCosts) {
        this.actionsPlansCostsComputed = true;
        this.actionsPlansCosts = actionsPlansCosts;
    }

    public boolean isActionsPlansCostsComputed() {
        return actionsPlansCostsComputed;
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.domain.dataschemas.dashboard;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cwfr-dzysman
 */
public class DashboardElementBean extends ElementBean {

    /**
     * type de meteo du projet
     */
    private String meteo;
    /**
     * tooltip pour la meteo
     */
    private String meteoTooltip;
    /**
     * moyenne des objectifs
     */
    private double goalsAverage;
    /**
     * moyenne des objectifs pour la précédente analyse
     */
    private double previousGoalsAverage;
    /**
     * nombre de lignes de code
     */
    private int nbLOC;
    /**
     * nombre d'elements de type fichier
     */
    private int nbFileElements;

    /**
     * notes aux objectifs pour la dernière baseline
     * key = identifiant de l'objectif
     * value = note à l'objectif
     */
    private Map<String, Double> currentScores;

    /**
     * notes aux objectifs pour la baseline précédente
     * key = identifiant de l'objectif
     * value = note à l'objectif
     */
    private Map<String, Double> previousScores;

    public DashboardElementBean(ElementBean src) {
        this.currentScores = new HashMap<String, Double>();
        this.previousScores = new HashMap<String, Double>();
        super.setId(src.getId());
        super.setLib(src.getLib());
        super.setDesc(src.getDesc());
        super.setDinst(src.getDinst());
        super.setDmaj(src.getDmaj());
        super.setDperemption(src.getDperemption());
        super.setBaseline(src.getBaseline());
        super.setProject(src.getProject());
        super.setDialecte(src.getDialecte());
        super.setUsage(src.getUsage());
        super.setTypeElt(src.getId());
        super.setStereotype(src.getStereotype());
        super.setStreamElt(src.getStreamElt());
        super.setPVobName(src.getPVobName());
        super.setVobMountPoint(src.getVobMountPoint());
        super.setMakefileDir(src.getMakefileDir());
        super.setSourceDir(src.getSourceDir());
        super.setBinDir(src.getBinDir());
        super.setPeriodicDir(src.getPeriodicDir());
        super.setLibraries(src.getLibraries());
        super.setFilePath(src.getFilePath());
        super.setPoids(src.getPoids());
        super.setScmRepository(src.getScmRepository());
        super.setScmModule(src.getScmModule());
        super.setProjectFilePath(src.getProjectFilePath());
        super.setInfo1(src.getInfo1());
        super.setInfo2(src.getInfo2());
        super.setHasSource(src.hasSource());
    }

    public String getMeteo() {
        return meteo;
    }

    public void setMeteo(String meteo) {
        this.meteo = meteo;
    }

    public String getMeteoTooltip() {
        return meteoTooltip;
    }

    public void setMeteoTooltip(String meteoTooltip) {
        this.meteoTooltip = meteoTooltip;
    }

    public double getGoalsAverage() {
        return goalsAverage;
    }

    public void setGoalsAverage(double goalsAverage) {
        this.goalsAverage = goalsAverage;
    }

    public double getPreviousGoalsAverage() {
        return previousGoalsAverage;
    }

    public void setPreviousGoalsAverage(double previousGoalsAverage) {
        this.previousGoalsAverage = previousGoalsAverage;
    }

    public int getNbLOC() {
        return nbLOC;
    }

    public void setNbLOC(int nbCodeLines) {
        this.nbLOC = nbCodeLines;
    }

    public int getNbFileElements() {
        return nbFileElements;
    }

    public void setNbFileElements(int nbFileElements) {
        this.nbFileElements = nbFileElements;
    }

    public void addCurrentScoreToGoal(String goalId, double score) {
        this.currentScores.put(goalId, score);
    }

    public void addPreviousScoreToGoal(String goalId, double score) {
        this.previousScores.put(goalId, score);
    }

    public String getTrendForGoal(String goalId) {
        double current = 0.0;
        double previous = 0.0;
        Double d = this.currentScores.get(goalId);
        if(d != null) {
            current = d.doubleValue();
        }
        d = this.previousScores.get(goalId);
        if(d != null) {
            previous = d.doubleValue();
        }
        return StringFormatUtil.getTendanceLabel(previous, current);
    }

    public double getCurrentScoreForGoal(String id) {
        double retour = 0.0;
        Double d = this.currentScores.get(id);
        if(d != null) {
            retour = d.doubleValue();
        }
        return retour;
    }

    public List<String> getCurrentGoalsIds() {
        List<String> retour = new ArrayList<String>();
        for(String s : this.currentScores.keySet()) {
            retour.add(s);
        }
        return retour;
    }
}

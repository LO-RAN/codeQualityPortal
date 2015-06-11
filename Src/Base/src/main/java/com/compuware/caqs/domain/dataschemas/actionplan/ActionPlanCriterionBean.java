package com.compuware.caqs.domain.dataschemas.actionplan;

import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.compuware.caqs.domain.dataschemas.CriterionNoteRepartition;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;

public class ActionPlanCriterionBean extends ActionPlanElementBean {

    /**
     * Cost to correct it
     */
    private double cost = 0.0;
    /**
     *
     */
    private static final long serialVersionUID = 306253944425796184L;
    /**
     * Element type
     */
    private String typeElt = null;
    /**
     * Dispatching
     */
    private CriterionNoteRepartition repartition = null;
    /**
     * Aggregation set in the quality model
     */
    private Agregation aggregation = null;
    /**
     * Quality model id
     */
    private String idUsa = null;
    /**
     * Goals it is associated to in the quality model
     */
    private Map<String, Double> factors = null;
    /**
     * Corrected elements since the last analysis
     */
    private List<ActionPlanImpactedElementBean> correctedElts;
    /**
     * Partially corrected elements since the last analysis
     */
    private List<ActionPlanImpactedElementBean> partiallyCorrectedElts;
    /**
     * Deteriorated elements since the last analysis
     */
    private List<ActionPlanImpactedElementBean> deterioratedElts;
    /**
     * Stables elements since the last analysis
     */
    private List<ActionPlanImpactedElementBean> stablesElts;

    public ActionPlanCriterionBean(String id) {
        super(new CriterionDefinition(id), id);
        this.factors = new HashMap<String, Double>();
        this.cost = 1.0;
        this.deterioratedElts = new ArrayList<ActionPlanImpactedElementBean>();
        this.partiallyCorrectedElts = new ArrayList<ActionPlanImpactedElementBean>();
        this.stablesElts = new ArrayList<ActionPlanImpactedElementBean>();
        this.correctedElts = new ArrayList<ActionPlanImpactedElementBean>();
        this.repartition = new CriterionNoteRepartition();
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void addFactor(String idFact, double weight) {
        if (this.factors.get(idFact) == null) {
            this.factors.put(idFact, weight);
        }
    }

    public void setFactors(Map<String, Double> f) {
        this.factors = f;
    }

    public Map<String, Double> getFactors() {
        return this.factors;
    }

    public boolean isUsedByFactor(String idFact) {
        return (this.factors.get(idFact) != null);
    }

    public String getTypeElt() {
        return typeElt;
    }

    public void setTypeElt(String typeElt) {
        this.typeElt = typeElt;
    }

    public int getNumberElt() {
        //nombre d'elements ayant une note inferieure a 3
        int retour = 0;
        if (this.getRepartition() != null) {
            retour += this.getRepartition().getValue(0) +
                    this.getRepartition().getValue(1);
        }
        return retour;
    }

    public CriterionNoteRepartition getRepartition() {
        return repartition;
    }

    public void setRepartition(CriterionNoteRepartition repartition) {
        this.repartition = repartition;
    }

    public void retrieveTypeAggregation(String idModel) {
        if (this.aggregation == null) {
            CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(idModel);
            if (manager != null) {
                manager.setCritere(this.id);
                List<Agregation> aggregations = manager.getAggregations();
                if (!aggregations.isEmpty()) {
                    this.aggregation = aggregations.get(0);
                }/* else if (aggregations.length > 1) {
                this.aggregation = aggregations[0];
                }*/
            }
        }
    }

    public Agregation getAggregation() {
        if (this.aggregation == null) {
            this.retrieveTypeAggregation(this.getIdUsa());
        }
        return this.aggregation;
    }

    /**
     * @param o l'element a comparer
     * @param loc la locale pour la comparaison par libelle
     * @return -1 si ce criterionBean a une severite plus forte que celui donne en parametre, 0 si elle est egale et 1
     * si elle moins forte. La comparaison se fait d'abord sur les notes, ensuite sur les agregations, puis la repartition,
     * enfin sur le libelle traduit.
     */
    public int compareSeverity(ActionPlanElementBean obj, Locale loc) {
        int retour = 0;
        if (obj instanceof ActionPlanCriterionBean) {
            ActionPlanCriterionBean o = (ActionPlanCriterionBean) obj;
            //si les notes sont identiques
            if (this.getScore() == o.getScore()) {
                int compAgg = 0;
                if (this.getAggregation() != null && o.getAggregation() != null) {
                    compAgg = this.getAggregation().compareTo(o.getAggregation());
                }
                //on compare selon l'agregation
                if (compAgg == 0) {
                    //si l'agregation est identique, on compare selon la repartition
                    if (this.getRepartition().getValue(0) !=
                            o.getRepartition().getValue(0)) {
                        retour = (this.getRepartition().getValue(0) <
                                o.getRepartition().getValue(0)) ? -1 : 1;
                    } else if (this.getRepartition().getValue(1) !=
                            o.getRepartition().getValue(1)) {
                        retour = (this.getRepartition().getValue(1) <
                                o.getRepartition().getValue(1)) ? -1 : 1;
                    } else if (this.getRepartition().getValue(2) !=
                            o.getRepartition().getValue(2)) {
                        retour = (this.getRepartition().getValue(2) <
                                o.getRepartition().getValue(2)) ? -1 : 1;
                    } else if (this.getRepartition().getValue(3) !=
                            o.getRepartition().getValue(3)) {
                        retour = (this.getRepartition().getValue(3) <
                                o.getRepartition().getValue(3)) ? -1 : 1;
                    } else {
                        //si la repartition est identique, on compare selon le libelle
                        retour = this.getInternationalizableProperties().getLib(loc).compareTo(o.getInternationalizableProperties().getLib(loc));
                    }
                } else {
                    retour = compAgg;
                }
            } else {
                //les notes sont prioritaires sur la repartition
                retour = (this.getScore() < o.getScore()) ? -1 : 1;
            }
        }

        return retour;
    }

    /**
     * @param o l'element a comparer
     * @param loc la locale pour la comparaison par libelle
     * @return -1 si ce criterionBean a une severite plus forte que celui donne en parametre, 0 si elle est egale et 1
     * si elle moins forte. La comparaison se fait d'abord sur les notes, ensuite sur les agregations, puis la repartition,
     * enfin sur le libelle traduit.
     */
    public int compareAggregation(ActionPlanCriterionBean o) {
        int retour = 0;
        retour = this.getAggregation().compareTo(o.getAggregation());
        return retour;
    }

    public String getIdUsa() {
        return idUsa;
    }

    public void setIdUsa(String idUsa) {
        this.idUsa = idUsa;
    }

    public double getCorrectedScore() {
        if (this.correctedScore == 0.0) {
            CaqsQualimetricModelManager manager = CaqsQualimetricModelManager.getQualimetricModelManager(this.getIdUsa());
            if (manager != null) {
                manager.setCritere(this.id);
                List<FormuleForm> formulas = manager.getFormulas();
                if (!formulas.isEmpty()) {
                    for (FormuleForm formule : formulas) {
                        int note = formule.getScore();
                        if (note >= this.getScore()) {
                            if (note > 2) {
                                if (this.correctedScore == 0.0) {
                                    this.correctedScore = note;
                                } else {
                                    this.correctedScore = Math.min(this.correctedScore, note);
                                }
                            }
                        }
                    }
                }
            }
        }
        return correctedScore;
    }

    public List<ActionPlanImpactedElementBean> getPartiallyCorrectedElts() {
        return partiallyCorrectedElts;
    }

    public void setPartiallyCorrectedElts(
            List<ActionPlanImpactedElementBean> partiallyCorrectedElts) {
        this.partiallyCorrectedElts = partiallyCorrectedElts;
    }

    public List<ActionPlanImpactedElementBean> getDeterioratedElts() {
        return deterioratedElts;
    }

    public void setDeterioratedElts(List<ActionPlanImpactedElementBean> deterioratedElts) {
        this.deterioratedElts = deterioratedElts;
    }

    public List<ActionPlanImpactedElementBean> getStablesElts() {
        return stablesElts;
    }

    public void setStablesElts(List<ActionPlanImpactedElementBean> stablesElts) {
        this.stablesElts = stablesElts;
    }

    public List<ActionPlanImpactedElementBean> getCorrectedElts() {
        return correctedElts;
    }

    public void setCorrectedElts(List<ActionPlanImpactedElementBean> correctedElts) {
        this.correctedElts = correctedElts;
    }

    /**
     * met a jour l'association critere/objectif
     * @param ap le plan d'actions
     */
    public void updateActionPlanAssociatedFactorsList(ApplicationEntityActionPlanBean ap) {
        //a ce moment, le critere sait quels objectifs lui sont associes.

        //liste des objectifs associes au plan d'actions
        ActionPlanElementBeanCollection<ActionPlanFactorBean> actionsPlanFactors = ap.getFactors();
        
        //pour chaque objectif associe au critere
        for (String fbId : this.getFactors().keySet()) {
            //on recupere la representation de l'objectif dans la liste des objectifs associes au plan d'actions
            ActionPlanFactorBean factor = actionsPlanFactors.get(fbId);
            if (factor == null) {
                //s'il n'y en a pas, on cree une representation et on l'ajoute a la liste
                //des objectifs associes au plan d'actions
                factor = new ActionPlanFactorBean(fbId, ap.getIdElt(), ap.getIdBline());
                actionsPlanFactors.add(factor);
            }
            //on indique a l'objectif associe au plan d'actions qu'il utilise ce critere
            factor.getAssociatedCriterions().add(this);
        }
        //a la fin, le critere sait quels objectifs lui sont associes ET
        //chaque objectif sait que le critere lui est associe
    }
}

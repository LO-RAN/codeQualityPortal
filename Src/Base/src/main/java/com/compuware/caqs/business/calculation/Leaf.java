/*
 * MetriquesConteneur.java
 *
 * Created on 22 octobre 2002, 14:58
 */
package com.compuware.caqs.business.calculation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CallDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.MapUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * Definit un element contenant des metriques.
 * @author  cwfr-fdubois
 */
public class Leaf extends Conteneur {

    /** Contient les criteres definis pour le type l'element. */
    protected Collection<String> criterionList = null;
    /** Contient les metriques de l'element. */
    protected Map<String, ValuedObject> mMetriques = new HashMap<String, ValuedObject>();
    private String mIdTElt = null;
    private String mDescElt = null;

    /** Cree une nouvelle instance de MetriquesConteneur.
     * @param idElt l'identifiant de l'element.
     * @param poids le poids de l'element.
     */
    public Leaf(String idElt, String idTElt, double poids, String stereotype, ProjectBean projet, Baseline baseline) {
        // Initialisation des attributs.
        mIdElt = idElt;
        mPoids = poids;
        mStereotype = stereotype;
        mProjet = projet;
        mBaseline = baseline;
        mIdTElt = idTElt;
    }

    /** Cree une nouvelle instance de MetriquesConteneur.
     * @param idElt l'identifiant de l'element.
     * @param poids le poids de l'element.
     */
    public Leaf(String idElt, String idTElt, double poids, String stereotype, ProjectBean projet, Baseline baseline, UsageBean usage) {
        // Initialisation des attributs.
        this(idElt, idTElt, poids, stereotype, projet, baseline);
        mUsage = usage;
    }

    public void setDescElt(String descElt) {
        this.mDescElt = descElt;
    }

    public void init(
            ICalculationConfig config,
            Map<String, Map<String, Critere>> critJustifMap) throws DataAccessException {
        logger.info("Init element:" + this.mDescElt);
        initSubElements(config, critJustifMap);
        initCriteres(config, critJustifMap);
        initNbSubElementByType();
    }

    /**
     * Methode d'initialisation des methodes du programme.
     */
    protected void initSubElements(
            ICalculationConfig config,
            Map<String, Map<String, Critere>> critJustifMap) throws DataAccessException {
        if (mSubElt != null && mSubElt.size() > 0) {
            Iterator<Conteneur> i = mSubElt.iterator();
            Leaf currentLeaf = null;
            logger.info("Start Init sub elements for " + this.mDescElt);
            while (i.hasNext()) {
                currentLeaf = (Leaf) i.next();
                currentLeaf.init(config, critJustifMap);
                this.addTypeSubElement(currentLeaf.getTypeElement());
                this.addTypeSubElement(currentLeaf.getTypeSubElement());
            }
            logger.info("End Init sub elements for " + this.mDescElt);
        }
    }

    public Map<String, ValuedObject> getMetriques() {
        return mMetriques;
    }

    public void calculate(UsageCalculator calculator, ICalculationConfig config) throws DataAccessException {
        calculateSubElts(calculator, config);
        if (config.needCriterionCalculation()) {
            calculeCriteres(calculator);
            agregCriteresSubElts(calculator);
        }
        if (config.needGoalCalculation()) {
            calculeFacteurs();
        }
        if (config.needMetricCalculation() && MapUtils.get(mMetriques, Constants.ALL_CODE_KEYS)
                == null) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
            double allCode = calculeSum(mSubElt, Constants.ALL_CODE_KEYS);
            metriqueDao.setMetrique(mIdElt, mBaseline.getId(), "ALL_CODE", allCode);
            mMetriques.put("ALL_CODE", new Qametrique("ALL_CODE", allCode, 0.0, 0.0, 0.0));
            double comments = calculeSum(mSubElt, Constants.COMMENT_KEYS);
            metriqueDao.setMetrique(mIdElt, mBaseline.getId(), "COMMENTS", comments);
            mMetriques.put("COMMENTS", new Qametrique("COMMENTS", comments, 0.0, 0.0, 0.0));
        }
    }

    private double calculeSum(List<Conteneur> all, String[] metKeys) {
        double result = 0;
        if (all != null) {
            Map<String, ValuedObject> metriques = null;
            Leaf l = null;
            Qametrique met = null;
            Iterator<Conteneur> i = all.iterator();
            while (i.hasNext()) {
                l = (Leaf) i.next();
                metriques = l.getMetriques();
                if (metriques != null) {
                    met = (Qametrique) MapUtils.get(metriques, metKeys);
                    if (met != null) {
                        result += met.getValbrute();
                    }
                }
            }
        }
        return result;
    }

    private void calculateSubElts(UsageCalculator calculator, ICalculationConfig config) throws DataAccessException {
        if (mSubElt != null) {
            Iterator<Conteneur> i = mSubElt.iterator();
            while (i.hasNext()) {
                Leaf l = (Leaf) i.next();
                l.calculate(calculator, config);
            }
        }
    }

    public Collection<String> getCriterionIds() {
        return criterionList;
    }

    public void setCriterionIds(Collection<String> coll) {
        criterionList = coll;
    }

    public void performUpdate(Connection connection, ICalculationConfig config) throws CaqsException {
        LoggerManager.pushContexte("Elt(" + mIdElt + ")");
        logger.debug("Mise a jour BASE pour les Sous-Elts");
        if (mSubElt != null) {
            Iterator<Conteneur> i = mSubElt.iterator();
            int size = mSubElt.size();
            int idx = 0;
            // Parcours des classes du programme.
            while (i.hasNext()) {
                Leaf l = (Leaf) i.next();
                logger.debug("MAJ Sub-Elt " + l.getId() + ", (" + idx + "/"
                        + size + ")");
                // Mise a jour des donnees de la classe dans la base de donnees.
                l.performUpdate(connection, config);
                idx++;
            }
        }
        // Mise a jour des donnees du programme dans la base de donnees.
        try {
            connection.setAutoCommit(false);
            updateDataBase(connection, config);
            JdbcDAOUtils.commit(connection);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            JdbcDAOUtils.rollbackConnection(connection);
            throw new DataAccessException(e);
        }

        LoggerManager.popContexte();
    }

    /** Initialisation des criteres en fonction des justifications apportees a la baseline precedente ou actuelle.
     * @param connection la connexion a la base de donnees.
     */
    protected void initCriteres(
            ICalculationConfig config,
            Map<String, Map<String, Critere>> critJustifMap) throws DataAccessException {
        logger.debug("Initialisation des Criteres");
        // Recuperation des justifications apportees a la baseline actuelle ou la baseline precedente.
        Map<String, Critere> htab = critJustifMap.get(this.getId());
        if (config.needCriterionCalculation()) {
            Collection<String> allEllementCriterions = getAllElementCriterions(criterionList);
            Iterator<String> i = allEllementCriterions.iterator();
            Critere justif = null;
            while (i.hasNext()) {
                String crit = i.next();
                if (htab != null) {
                    justif = htab.get(crit);
                }
                mCriterions.put(crit, new Critere(crit, mPoids, justif));
            }
        } else {
            if (htab != null && !htab.isEmpty()) {
                Set<String> critKeySet = htab.keySet();
                Iterator<String> critIter = critKeySet.iterator();
                Critere currentCrit = null;
                String currentCritId = null;
                while (critIter.hasNext()) {
                    currentCritId = critIter.next();
                    currentCrit = this.mCriterions.get(currentCritId);
                    if (currentCrit != null) {
                        currentCrit.initJustification(htab.get(currentCritId));
                    }
                }
            }
        }
        logger.debug("FIN Initialisation des Criteres");
    }

    private Collection<String> getAllElementCriterions(Collection<String> critList) {
        Collection<String> result = null;
        if (mSubElt != null && mSubElt.size() > 0) {
            result = new ArrayList<String>();
            result.addAll(critList);
            result.addAll(getCriterionList(mSubElt));
        } else {
            result = critList;
            result.addAll(getCriterionsForElement(getTypeSubElement()));
        }
        return result;
    }

    /** Mise a jour des criteres et facteurs dans la base de donnees. */
    @Override
    protected void updateDataBase(Connection connection, ICalculationConfig config) throws CaqsException {
        if (config.needCalculation() || config.needConsolidation()
                || config.needCriterionCalculation()
                || config.needMetricCalculation()) {
            // Mise a jour des criteres.
            updateCritereDataBase(connection);
        }
        if (config.needCalculation() || config.needConsolidation()
                || config.needGoalCalculation() || config.needMetricCalculation()) {
            /// Mise a jour des facteurs.
            updateFacteurDataBase(connection);
        }
        if (config.needTrendUpdate() || this.mBaseline.existPreviousBaseline()) {
            // Si une baseline precedente existe, mise a jour des tendances.
            updateTendances(connection);
        }
        if (mSubElt != null && mSubElt.size() > 0) {
            updateLinks();
        }
    }

    /** Update links for element based on children links. */
    private void updateLinks() throws DataAccessException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        CallDao callDao = daoFactory.getCallDao();
        callDao.createParentLinks(this.mIdElt, this.mBaseline.getId());
    }

    protected String getElementInfos() {
        return this.mIdTElt + ": " + this.mIdElt;
    }

    protected String getTypeElement() {
        return this.mIdTElt;
    }

    /** Calcul des criteres de la classe.
     * Calcule les differents criteres en fonction des metriques.
     */
    protected void calculeCriteres(UsageCalculator calculator) {
        LoggerManager.pushContexte(this.mIdElt);
        logger.debug("Calcul des Criteres");
        // Recuperation des metriques utilisees dans la liste des metriques.
        Critere crit = null;
        Collection<Critere> coll = mCriterions.values();
        Iterator<Critere> i = coll.iterator();
        while (i.hasNext()) {
            crit = i.next();
            if (criterionList.contains(crit.getId())) {
                crit.setNote(calculator.eval(mUsage.getId(), crit.getId(), mMetriques, mStereotype));
                // compute cost only when score requires action
                if (crit.getNote() < 3) {
                    crit.setCost(calculator.evalCost(mUsage.getId(), crit.getId(), mMetriques));
                }
            }
        }

        logger.debug("FIN Calcul des Criteres");
        LoggerManager.popContexte();
    }

    protected boolean needAggregation(Critere crit) {
        boolean result = false;
        if (crit != null) {
            result = !this.criterionList.contains(crit.getId());
        }
        return result;
    }

    protected boolean isTreeStructureElement() {
        return false;
    }

    public void setMetrics(Map<String, ValuedObject> metrics) {
        if (metrics != null) {
            this.mMetriques = metrics;
            ;
        }
    }

    public double getPoids() {
        return this.mPoids;
    }

    protected boolean needElementBaselineInformationUpdate() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("DESC=").append(this.mDescElt);
        result.append("TYPE=").append(this.mIdTElt);
        result.append(',').append(super.toString());
        return result.toString();
    }
}

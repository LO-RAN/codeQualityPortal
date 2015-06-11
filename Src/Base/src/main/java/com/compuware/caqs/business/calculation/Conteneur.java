/*
 * Conteneur.java
 *
 * Created on 4 octobre 2002, 09:33
 */
package com.compuware.caqs.business.calculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.TendanceDao;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.AggregableMap;
import com.compuware.caqs.domain.calculation.rules.aggregation.Aggregation;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.calcul.Facteur;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.Counter;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import org.apache.log4j.Logger;

/**
 * Classe abstraite definissant un element contenant un ensemble de sous-elements.
 * Projet, Sous-projet, Entite applicative.
 * @author  cwfr-fdubois
 */
public abstract class Conteneur implements AggregableMap {

    protected Logger logger;
    protected static final int RANGE = 10;
    private String factorsRequest = "Select distinct id_fact From Facteur_Critere" +
            " Where id_usa = ?";
    private String retrieveFactorsRequest = "Select id_fac, note_facbl" +
            " From Facteur_bline" + " Where id_bline = ?" + " And id_elt = ?";
    /** Baseline actuelle. */
    protected Baseline mBaseline;
    /** Projet. */
    protected ProjectBean mProjet;
    /** Usage de l'element. */
    protected UsageBean mUsage;
    /** Identifiant de l'element. */
    protected String mIdElt;
    /** Identifiant de l'element. */
    protected String mIdMainElt;
    /** Le stereotype de l'element. */
    protected String mStereotype;
    protected List<Conteneur> mSubElt = new ArrayList<Conteneur>();
    /** Contient les facteurs de l'element. */
    protected Map<String, Facteur> mFactors = new HashMap<String, Facteur>();
    /** Contient les criteres de l'element. */
    protected Map<String, Critere> mCriterions = new HashMap<String, Critere>();
    /** Poids de l'element. */
    protected double mPoids = 1;
    protected double mAllCode = 0;
    protected double mComplexDest = 0;
    protected boolean mIsMainContainer = false;
    protected Collection<String> mSubElementTypes = new ArrayList<String>();
    protected Map<String, String> criterionElementTypeMap = null;

    public String getId() {
        return this.mIdElt;
    }

    protected String getMainElt() {
        return this.mIdMainElt;
    }

    protected void setMainElt(String id) {
        this.mIdMainElt = id;
    }

    protected String getElementInfos() {
        return "Element " + this.mIdElt;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Initialisation des facteurs.
     */
    protected void initFacteurs() throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement facstmt = null;
        ResultSet rs = null;
        // Initialisation des facteurs en fonctions des notes forc�es pr�alablement.
        try {
            // Pr�paration de la requ�te.
            facstmt = connection.prepareStatement(factorsRequest);
            facstmt.setString(1, mUsage.getId());
            // Ex�cution de la requ�te.
            rs = facstmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                String idFact = rs.getString(1);
                mFactors.put(idFact, new Facteur(idFact, mPoids));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error during factor initialisation", e);
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(facstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * Initialisation des facteurs.
     */
    protected final void retrieveFacteurs(Connection connection) throws DataAccessException {
        PreparedStatement facstmt = null;
        ResultSet rs = null;
        // Initialisation des facteurs en fonctions des notes forc�es pr�alablement.
        try {
            // Pr�paration de la requ�te.
            facstmt = connection.prepareStatement(retrieveFactorsRequest);
            facstmt.setString(1, mBaseline.getId());
            facstmt.setString(2, mIdElt);
            // Ex�cution de la requ�te.
            rs = facstmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                String idFact = rs.getString(1);
                Facteur fact = (Facteur) mFactors.get(idFact);
                if (fact != null) {
                    fact.setNote(rs.getDouble(2));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error during factor list retrieve", e);
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(facstmt);
        }
    }

    public Aggregable getAggregable(String key) {
        Aggregable result = (Aggregable) mCriterions.get(key);
        if (result == null) {
            result = (Aggregable) mFactors.get(key);
        }
        return result;
    }

    public Critere getCritere(String key) {
        return mCriterions.get(key);
    }

    protected Collection<String> getCriterionsForElement(String idTelt) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsageDao usageDao = daoFactory.getUsageDao();
        return usageDao.retrieveCriterionsForElement(this.mUsage.getId(), idTelt);
    }

    protected Collection<String> getCriterionsForElement(Collection<String> typeEltColl) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UsageDao usageDao = daoFactory.getUsageDao();
        return usageDao.retrieveCriterionsForElement(this.mUsage.getId(), typeEltColl);
    }

    /** Methode d'acces au critere de Maintenabilite.
     * @return le critere de Maintenabilite.
     */
    public double getAllCode() {
        // Le critere de Maintenabilite.
        return mAllCode;
    }

    public Facteur getFacteur(String key) {
        return mFactors.get(key);
    }

    public Map<String, Facteur> getFacteurs() {
        return mFactors;
    }

    public double getComplexDest() {
        return mComplexDest;
    }

    private boolean isExclusionCalculationMode() {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String result = dynProp.getProperty("calcul.aggregation.mode");
        return "exclusion".equalsIgnoreCase(result);
    }

    /**
     * Calcule les facteurs en fonction des criteres.
     */
    protected void calculeFacteurs() {
        boolean exclusionMode = isExclusionCalculationMode();
        Map<String, Map<String, Double>> factdefs = mUsage.getFactorDefinition();
        Iterator<Facteur> i = this.mFactors.values().iterator();
        while (i.hasNext()) {
            Facteur fact = i.next();
            calculeFacteur(fact, factdefs.get(fact.getId()), exclusionMode);
        }
    }

    private void calculeFacteur(Facteur fact, Map<String, Double> critdefs, boolean exclusionMode) {
        double sumNote = 0.0;
        double sumWeight = 0.0;
        double minNoteCrit = 4.0;
        int nbCrit = critdefs.size();
        Iterator<String> i = critdefs.keySet().iterator();
        while (i.hasNext()) {
            String idCrit = i.next();
            Critere crit = mCriterions.get(idCrit);
            Double poids = (Double) critdefs.get(idCrit);
            if (crit != null && poids != null && crit.getNote() > 0) {
                double adjustedWeight = getWeight(poids.doubleValue(), nbCrit, crit.getNote());
                sumNote += crit.getNote() * adjustedWeight;
                sumWeight += adjustedWeight;
                minNoteCrit = Math.min(minNoteCrit, crit.getNote());
            }
        }
        if (sumWeight > 0) {
            fact.setNote(getNote(sumNote / sumWeight, minNoteCrit, exclusionMode));
        }
    }

    private double getNote(double note, double minNote, boolean exclusionMode) {
        double result = note;
        if (minNote < 3 && exclusionMode) {
            result = minNote;
        } else if (minNote < 2 && result > 3) {
            result = 2.99;
        } else if (minNote < 3.95 && result == 4) {
            result = 3.95;
        }
        return result;
    }

    /**
     * The real weight is calculated as follow:
     * note <= 2 then weight = weight * number_of_crit / note
     * note > 4 then weight = weight
     * else weight = weight * number_of_crit / note
     * @param poids
     * @param nbCrit
     * @param note
     * @return
     */
    private double getWeight(double poids, int nbCrit, double note) {
        double result = poids;
        if (note > 3.5) {
            result = poids + (((double) nbCrit) / 10.0);
        } else if (note > 3.2) {
            result = poids + (((double) nbCrit) / 5.0);
        } else {
            result = poids + (nbCrit / note);
        }
        /*
        PoissonDistributionImpl normalDist = new PoissonDistributionImpl(getMean(nbCrit));
        result = poids * nbCrit * normalDist.probability(normalDist.getMean()+ note - 1);
         */
        return result;
    }

    /*
    public static void main (String[] args) throws MathException {
    PoissonDistributionImpl poissonDist = new PoissonDistributionImpl(5);
    System.out.println(poissonDist.cumulativeProbability(5-1));
    System.out.println(poissonDist.cumulativeProbability(5-2));
    System.out.println(poissonDist.cumulativeProbability(5-3));
    System.out.println(poissonDist.cumulativeProbability(5-4));
    }
     * /
    
    /**
     * @param criterionElementTypeMap the criterionElementTypeMap to set
     */
    public void setCriterionElementTypeMap(
            Map<String, String> criterionElementTypeMap) {
        this.criterionElementTypeMap = criterionElementTypeMap;
    }

    public int getVolumetry(String idCritFact) {
        int result = 1;
        if (this.mUsage != null && mSubElt != null && mSubElt.size() > 0) {
            result += getNbSubElement(this.criterionElementTypeMap.get(idCritFact));
        }
        return result;
    }
    private Map<String, Counter> nbSubElementByTypeMap = new HashMap<String, Counter>();

    protected Map<String, Counter> getNbSubElementByTypeMap() {
        return this.nbSubElementByTypeMap;
    }

    public void initNbSubElementByType() {
        if (mSubElt != null && mSubElt.size() > 0) {
            Iterator<Conteneur> i = mSubElt.iterator();
            Conteneur subElt = null;
            String subType = null;
            Counter subEltCount = null;
            while (i.hasNext()) {
                subElt = i.next();
                subElt.initNbSubElementByType();

                subType = subElt.getTypeElement();
                subEltCount = nbSubElementByTypeMap.get(subType);
                if (subEltCount == null) {
                    subEltCount = new Counter();
                    nbSubElementByTypeMap.put(subType, subEltCount);
                }
                subEltCount.inc(1 + subElt.getNbSubElement(subType));

                for (String idSubType : subElt.getNbSubElementByTypeMap().keySet()) {
                    subEltCount = nbSubElementByTypeMap.get(idSubType);
                    if (subEltCount == null) {
                        subEltCount = new Counter();
                        nbSubElementByTypeMap.put(idSubType, subEltCount);
                    }
                    if (!idSubType.equals(subType)) {
                        subEltCount.inc(subElt.getNbSubElement(idSubType));
                    }
                }
            }
        }
    }

    public int getNbSubElement(String idTelt) {
        int result = 0;
        if (idTelt != null && mSubElt != null && mSubElt.size() > 0) {
            Counter cnt = this.nbSubElementByTypeMap.get(idTelt);
            if (cnt != null) {
                result = cnt.getValue();
            }
        }
        return result;
    }

    /** Mise a jour de la base de donnees. */
    public abstract void performUpdate(Connection connection, ICalculationConfig config) throws CaqsException;

    /** Mise a jour des criteres et facteurs dans la base de donnees. */
    protected void updateDataBase(Connection connection, ICalculationConfig config) throws CaqsException {
        // Mise a jour des criteres.
        updateCritereDataBase(connection);
        /// Mise a jour des facteurs.
        updateFacteurDataBase(connection);
    }

    /**  Mise a jour des criteres dans la base de donnees. */
    protected void updateCritereDataBase(Connection connection) throws CaqsException {
        // Mise a jour des criteres dans la base de donnees.
        Collection<Critere> coll = mCriterions.values();
        DaoFactory daoFactory = DaoFactory.getInstance();
        CriterionDao criterionDao = daoFactory.getCriterionDao();
        criterionDao.updateCriterion(coll, mBaseline, mProjet, mIdElt, connection);

        if (this.getTypeElement().equals(ElementType.EA)) {
            criterionDao.calculateCriterionNoteRepartition(mIdElt, mProjet.getId(), mBaseline.getId(), connection);
        }
        if (needElementBaselineInformationUpdate()) {
            criterionDao.updateElementBaselineInformation(mIdElt, mBaseline.getId(), mIdMainElt, getNoteRepartition(mCriterions.values()), connection);
        }
    }

    protected boolean needElementBaselineInformationUpdate() {
        return false;
    }

    private int[] getNoteRepartition(Collection<Critere> criterionColl) {
        int[] result = new int[4];
        Critere crit = null;
        int idx;
        Iterator<Critere> i = criterionColl.iterator();
        while (i.hasNext()) {
            crit = i.next();
            if (crit.getNoteJustifiee() > crit.getNote()) {
                idx = (int) Math.floor(crit.getNoteJustifiee());
            } else {
                idx = (int) Math.floor(crit.getNote());
            }
            if (idx > 0) {
                result[idx - 1]++;
            }
        }
        return result;
    }

    /**
     * Identify if the current element is part fo the Project, subproject or EA tree structure.
     * @return true if the current element is part fo the Project, subproject or EA tree structure.
     */
    protected abstract boolean isTreeStructureElement();

    protected void updateFacteurDataBase(Connection connection) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        FactorDao factorDao = daoFactory.getFactorDao();
        factorDao.updateFacteurDataBase(this.mFactors.values(), mBaseline, mProjet, mIdElt, connection);
        Map<String, Map<String, Double>> factdefs = mUsage.getFactorDefinition();
        Iterator<Facteur> i = this.mFactors.values().iterator();
        while (i.hasNext()) {
            Facteur fact = i.next();
            if (fact.isNotNull()) {
                if (isTreeStructureElement()) {
                    // Update weight if the current element is a project, subproject or program
                    updateCriterePoids(fact, factdefs.get(fact.getId()), connection);
                }
            }
        }
    }

    private void updateCriterePoids(Facteur fact, Map<String, Double> critdefs, Connection connection) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        CriterionDao criterionDao = daoFactory.getCriterionDao();
        Iterator<String> i = critdefs.keySet().iterator();
        while (i.hasNext()) {
            String idCrit = i.next();
            Critere crit = mCriterions.get(idCrit);
            Double poids = (Double) critdefs.get(idCrit);
            if (crit != null && poids != null) {
                criterionDao.updateWeight(crit, mBaseline,
                        mProjet, mIdElt, fact.getId(),
                        poids.doubleValue(), connection);
            }
        }
    }

    /** Mise a jour des tendances. */
    protected void updateTendances(Connection connection) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        logger.debug("Update tendances");
        TendanceDao tendanceDao = daoFactory.getTendanceDao();
        if (mBaseline.getPreviousBaseline() != null) {
            // Mise a jour des tendances pour les metriques.
            tendanceDao.updateMetricTendances(mIdElt, mBaseline.getId(), mBaseline.getPreviousBaseline().getId(), connection);
            // Mise a jour des tendances pour les criteres.
            tendanceDao.updateCriterionTendances(mIdElt, mBaseline.getId(), mBaseline.getPreviousBaseline().getId(), connection);
            // Mise a jour des tendances pour les facteurs.
            tendanceDao.updateFactorTendances(mIdElt, mBaseline.getId(), mBaseline.getPreviousBaseline().getId(), connection);
        } else {
            tendanceDao.deleteAllTrends(mIdElt, mBaseline.getId());
        }
        logger.debug("Update tendances end");
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        if (!isTreeStructureElement()) {
            criterionFacade.updateElementBaselineInformationEvolution(mIdElt, mBaseline.getId(), connection);
        }
    }

    protected abstract String getTypeElement();

    protected Collection<String> getTypeSubElement() {
        return this.mSubElementTypes;
    }

    protected void addTypeSubElement(Collection<String> c) {
        if (c != null) {
            Iterator<String> i = c.iterator();
            while (i.hasNext()) {
                addTypeSubElement(i.next());
            }
        }
    }

    protected void addTypeSubElement(String type) {
        if (type != null && type.length() > 0 &&
                !this.mSubElementTypes.contains(type)) {
            this.mSubElementTypes.add(type);
        }
    }

    /**
     * Calcul des criteres de l'element en fonction des criteres de ses
     * sous-elements.
     */
    protected void agregCriteresSubElts(UsageCalculator calculator) {
        if (mSubElt != null && mSubElt.size() > 0) {
            Collection<String> criterionColl = getCriterionList(mSubElt);
            if (getTypeElement().equals(ElementType.EA)) {
                logger.info("Nombre de critere: " + criterionColl.size());
            }

            List<Conteneur> subEltAndMe = null;
            if (this.mSubElementTypes.contains(this.getTypeElement())) {
                subEltAndMe = new ArrayList<Conteneur>();
                subEltAndMe.addAll(mSubElt);
                subEltAndMe.add(this);
            }

            Iterator<String> j = criterionColl.iterator();
            while (j.hasNext()) {
                String idCrit = j.next();
                Critere crit = (Critere) mCriterions.get(idCrit);
                if (needAggregation(crit) || (subEltAndMe != null)) {
                    Aggregation aggreg = calculator.getAggregation(mUsage.getId(), idCrit, getTypeElement());
                    double note = aggreg.aggregate(idCrit, mSubElt);
                    if (subEltAndMe != null && crit.getNote() >= 1) {
                        crit.setNote(Math.min(crit.getNote(), note));
                    } else {
                        crit.setNote(note);
                    }

                    double cost = evalCost(idCrit, mSubElt);
                    if (cost > crit.getCost()) {
                        crit.setCost(cost);
                    }
                }
            }
        }
    }

    private double evalCost(String idCrit, List<Conteneur> subElt) {
        double result = 0;
        Critere currentCrit = null;
        for (Conteneur currentElt : subElt) {
            currentCrit = currentElt.getCritere(idCrit);
            if (currentCrit != null && currentCrit.getCost() > 0) {
                result += currentCrit.getCost();
            }
        }
        return result;
    }

    protected boolean needAggregation(Critere crit) {
        return (crit.getNote() == 0) || (crit.getNote() == 4);
    }

    /**
     * Calcul des criteres de l'element en fonction des criteres de ses
     * sous-elements.
     */
    protected void agregAllCriteresSubElts(UsageCalculator calculator) {
        if (mSubElt != null && mSubElt.size() > 0) {
            Iterator<Critere> j = mCriterions.values().iterator();
            while (j.hasNext()) {
                Critere crit = j.next();
                if ((crit.getNote() == 0) || (crit.getNote() == 4)) {
                    Aggregation aggreg = calculator.getAggregation(mUsage.getId(), crit.getId(), getTypeElement());
                    crit.setNote(aggreg.aggregate(crit.getId(), mSubElt));
                }
            }
        }
    }

    public Collection<String> getCriterionList(Collection<Conteneur> coll) {
        Collection<String> result = new ArrayList<String>();

        // Add sub-element types without the current one.
        Collection<String> typeEltColl = new ArrayList<String>();
        typeEltColl.addAll(this.mSubElementTypes);

        Iterator<Conteneur> i = coll.iterator();
        while (i.hasNext() && !typeEltColl.isEmpty()) {
            Conteneur cont = i.next();
            if (typeEltColl.contains(cont.getTypeElement())) {
                result.addAll(extractDiff(result, cont.getCriterionList()));
                //typeEltColl.remove(cont.getTypeElement());
            }
        }
        return result;
    }

    private Collection<String> extractDiff(Collection<String> base, Collection<String> newCrit) {
        Collection<String> result = new ArrayList<String>();
        if (base != null && newCrit != null) {
            Iterator<String> i = newCrit.iterator();
            while (i.hasNext()) {
                String idCrit = i.next();
                if (!base.contains(idCrit)) {
                    result.add(idCrit);
                }
            }
        }
        return result;
    }

    public Collection<String> getCriterionList() {
        Collection<String> result = new ArrayList<String>();
        Iterator<Critere> i = mCriterions.values().iterator();
        while (i.hasNext()) {
            Critere crit = i.next();
            result.add(crit.getId());
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("ID=").append(this.mIdElt);
        return result.toString();
    }

    /** Affichage des facteurs et des criteres. */
    public void print() {
        logger.info("**************************************************");
        // Identifiant de la methode.
        logger.info("Element : " + mIdElt);
        printFacteurs();
        printCriteres();
        if (mSubElt != null) {
            Iterator<Conteneur> i = mSubElt.iterator();
            while (i.hasNext()) {
                Conteneur l = i.next();
                l.print();
            }
        }
    }

    /** Affichage des facteurs. */
    private void printFacteurs() {
        // Affichage des facteurs:
        // Affichage des notes des differents facteurs.
        // La note stockee dans chaque facteur correspond a la somme des notes des criteres multiplie
        // par leur poids respectifs.
        // Le poids stocke dans chaque facteur correspond a la somme des poids des criteres.
        logger.debug("------ : FACTEURS");
        Collection<Facteur> coll = mFactors.values();
        Iterator<Facteur> i = coll.iterator();
        while (i.hasNext()) {
            Facteur fact = i.next();
            if (fact != null) {
                logger.debug("       : " + fact.getId() + " = " + fact.getNote());
            }
        }
    }

    /** Affichage des criteres. */
    private void printCriteres() {
        // Affichage des criteres:
        // Affichage des notes des differents criteres.
        // La note stockee dans chaque critere correspond a la somme des notes des criteres des sous elements multiplie
        // par leur poids respectifs.
        // Le poids stocke dans chaque critere correspond a la somme des poids des criteres des sous-elements.
        logger.debug("------ : CRITERES");
        Collection<Critere> coll = mCriterions.values();
        Iterator<Critere> i = coll.iterator();
        while (i.hasNext()) {
            Critere crit = i.next();
            if (crit != null) {
                logger.debug("       : " + crit.getId() + " = " + crit.getNote());
            }
        }
    }
}

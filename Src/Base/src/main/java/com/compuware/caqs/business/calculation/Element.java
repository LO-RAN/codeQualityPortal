/*
 * Element.java
 *
 * Created on 4 octobre 2002, 08:39
 */
package com.compuware.caqs.business.calculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.aggregation.AggregationUtil;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.calcul.Facteur;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

/**
 * Definit un element d'agregation.
 * @author  cwfr-fdubois
 */
public class Element extends Conteneur {

    /** L'ensemble des sous elements. */
    private List<Conteneur> subElt;
    /** Requete de recuperation des sous-elements. */
    private static final String SUB_ELT_REQUEST = "Select id_elt, id_telt, poids_elt, id_stereotype From Element, Elt_links"
            + " Where id_elt = elt_fils" + " And elt_pere = ?"
            + " And id_pro = ?"
            + " And (dperemption is null Or dperemption > ?)"
            + " And dinst_elt < ?";

    /** Cree une nouvelle instance d'Element
     * @param idElt identifiant de l'element.
     * @param poids le poids de l'element.
     * @param projet identifiant du projet.
     * @param baseline la baseline actuelle.
     */
    public Element(String idElt, double poids, ProjectBean projet, Baseline baseline) {
        // Initialisation des attributs.
        mIdElt = idElt;
        mPoids = poids;
        mBaseline = baseline;
        mProjet = projet;
        subElt = new ArrayList<Conteneur>();
    }

    /** Initialisation de l'element.
     * @param connection la connexion DB utilisee.
     * @throws SQLException
     */
    public void init(Map<String, String> eaBaselineAssocMap, Connection connection) throws DataAccessException {
        // Recuperations des sous-elements.
        retrieveSubElements(eaBaselineAssocMap, connection);
        initFacteurs();
    }

    /**
     * Initialisation des facteurs.
     */
    protected void initFacteurs() throws DataAccessException {
        Iterator<Conteneur> i = subElt.iterator();
        if (this.mFactors == null) {
            this.mFactors = new HashMap<String, Facteur>();
        }
        while (i.hasNext()) {
            Conteneur c = (Conteneur) i.next();
            Map<String, Facteur> fact = c.getFacteurs();
            addFactors(fact);
        }
    }

    private void addFactors(Map<String, Facteur> fact) {
        if (mFactors != null) {
            Set<String> factKeySet = fact.keySet();
            Iterator<String> factKeyIter = factKeySet.iterator();
            String key = null;
            while (factKeyIter.hasNext()) {
                key = factKeyIter.next();
                if (!mFactors.containsKey(key)) {
                    this.mFactors.put(key, new Facteur(key, mPoids));
                }
            }
        }
    }

    protected String getTypeElement() {
        return "ELT";
    }

    /** 
     * Mise a jour de la base de donnees pour l'element
     * et l'ensemble des sous-elements de type non Programme.
     */
    public void performUpdate(Connection connection, ICalculationConfig config) throws CaqsException {
        Iterator<Conteneur> i = subElt.iterator();
        // Parcours des sous-elements.
        while (i.hasNext()) {
            Conteneur sub = i.next();
            if (!(sub instanceof Programme)) {
                // Mise a jour des donnees des sous-elements non programme.
                sub.performUpdate(connection, config);
            }
        }
        if (!mIdElt.equals(Constants.ENTRYPOINT_ELEMENT_VALUE)
                && config.needGoalCalculation()) {
            // L'element n'est pas le point d'entree de l'arborescence.
            // Mise a jour de ses donnees.
            updateDataBase(connection);
        }
        if (config.needTrendUpdate() || mBaseline.existPreviousBaseline()) {
            // La baseline precedente existe, mise a jour des tendances.
            updateTendances(connection);
        }
    }

    protected void updateDataBase(Connection connection) throws CaqsException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        FactorDao factorDao = daoFactory.getFactorDao();
        factorDao.updateFacteurDataBase(this.mFactors.values(), mBaseline, mProjet, mIdElt, connection);
    }

    /**
     * Recuperation des sous-elements.
     */
    private void retrieveSubElements(Map<String, String> eaBaselineAssocMap, Connection connection) throws DataAccessException {
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        try {
            // Preparation de la requete de recuperation avec les informations de l'element.
            eltstmt = connection.prepareStatement(SUB_ELT_REQUEST);
            eltstmt.setString(1, mIdElt);
            eltstmt.setString(2, mProjet.getId());
            eltstmt.setTimestamp(3, mBaseline.getDmaj());
            eltstmt.setTimestamp(4, mBaseline.getDmaj());
            // Execution de la requete.
            rs = eltstmt.executeQuery();
            logger.info("Element :" + mIdElt);

            Baseline moduleBaseline = null;

            // Parcours du resultat de la requete.
            while (rs.next()) {
                // Identifiant du sous-element.
                String id = rs.getString(1);
                // Type du sous-element.
                String type = rs.getString(2);
                double poids = rs.getDouble(3);
                String stereotype = rs.getString(4);
                logger.info("        : found : " + id + ", " + type);
                if (type != null && type.equals(ElementType.EA)) {
                    // Le sous-element est une entite applicative.
                    // Creation d'un sous-element de type programme.
                    // La creation effectue le chargement et le calcul sur ces classes et methodes
                    // de maniere recursive.
                    if (eaBaselineAssocMap.containsKey(id)) {
                        moduleBaseline = new Baseline();
                        moduleBaseline.setId(eaBaselineAssocMap.get(id));
                        moduleBaseline.setProject(mBaseline.getProject());
                    } else {
                        moduleBaseline = new Baseline(mBaseline);
                    }

                    Programme prog = new Programme(id, poids, stereotype, mProjet, moduleBaseline);
                    prog.setLogger(logger);
                    prog.initUsage();
                    prog.initFacteurs();
                    // Mise a jour des donnees du programme.
                    prog.retrieveFacteurs(connection);
                    BaselineBean previousBaseline = DaoFactory.getInstance().getBaselineDao().getPreviousBaseline(moduleBaseline, id);
                    if (previousBaseline != null) {
                        moduleBaseline.setPreviousBaseline(new Baseline(previousBaseline));
                    }
                    // Ajout du programme a la liste des sous-elements.
                    subElt.add(prog);
                } else {
                    // Creation d'un sous-element et recuperation des informations recursivement.
                    moduleBaseline = new Baseline(mBaseline);
                    Element newElt = new Element(id, poids, mProjet, moduleBaseline);
                    newElt.setLogger(logger);
                    newElt.init(eaBaselineAssocMap, connection);
                    BaselineBean previousBaseline = DaoFactory.getInstance().getBaselineDao().getPreviousBaseline(moduleBaseline, id);
                    if (previousBaseline != null) {
                        moduleBaseline.setPreviousBaseline(new Baseline(previousBaseline));
                    }
                    // Ajout du sous-element a la liste.
                    subElt.add(newElt);
                }
            }
            if (!mIdElt.equals(Constants.ENTRYPOINT_ELEMENT_VALUE)) {
                DaoFactory daoFactory = DaoFactory.getInstance();
                MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
                metriqueDao.updateVolumetrie(mIdElt, mBaseline.getId(), connection);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting sub elements", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
        }
    }

    /**
     * Calcul des criteres de l'element en fonction des criteres de ses sous-elements.
     */
    public void calculate(Connection connection) {
        Iterator<Conteneur> i = subElt.iterator();
        // Parcours des sous-elements.
        while (i.hasNext()) {
            Conteneur sub = i.next();
            if (!(sub instanceof Programme)) {
                // Mise a jour des donnees des sous-elements non programme.
                ((Element) sub).calculate(connection);
            }
        }
        Iterator<Facteur> factIter = this.mFactors.values().iterator();
        while (factIter.hasNext()) {
            Facteur fact = factIter.next();
            fact.setNote(AggregationUtil.aggregate(fact.getId(), subElt, Aggregable.AVG));
        }
    }

    /**
     * Affichage des donnees de l'element.
     */
    public void print() {
        logger.info("**************************************************");
        // Identifiant de l'element.
        logger.info("ELEMENT : " + mIdElt);
        // Pas de donnees pour lengthpoint d'entree.
        if (!mIdElt.equals(Constants.ENTRYPOINT_ELEMENT_VALUE)) {
            super.print();
        }
        Iterator<Conteneur> i = subElt.iterator();
        // Parcours des sous-elements et affichage de leurs donnees.
        while (i.hasNext()) {
            Conteneur sub = i.next();
            sub.print();
        }
    }

    protected boolean isTreeStructureElement() {
        return true;
    }
}

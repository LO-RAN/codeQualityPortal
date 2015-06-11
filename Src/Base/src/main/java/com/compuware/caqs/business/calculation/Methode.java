/*
 * Programme.java
 *
 * Created on 3 octobre 2002, 11:24
 */

package com.compuware.caqs.business.calculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.calculation.quality.RuleSet;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.calcul.ICalculationConfig;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

/**
 *
 * @author  cwfr-fdubois
 */
public class Methode extends Leaf {
    
    /** Crée une nouvelle instance de Méthode.
     * @param idElt l'identifiant de l'élément.
     * @param poids le poids de la méthode.
     */
    public Methode(String idElt, String idTElt, double poids, String stereotype, ProjectBean projet, Baseline baseline, UsageBean usage) {
        // Initialisation des attributs de la méthode.
        super(idElt, idTElt, poids, stereotype, projet, baseline, usage);
    }
    
    public void init(
    		ICalculationConfig config,
    		Map<String, Map<String, Critere>> critJustifMap) throws DataAccessException {
        initCriteres(config, critJustifMap);
    }
        
    public void calculeCopierColler(Connection connection, String idLinkProg, List<Leaf> allmethods, UsageCalculator calculator, int i) throws DataAccessException {
        double doCalcul = calculator.eval(mUsage.getId(), "DO_COPIER_COLLER", mMetriques, RuleSet.STEREOTYPE_ALL);
        int nbCc = 0;
        if (doCalcul == 1) {
            for (int it = i; it < allmethods.size(); it++) {
                Methode methode2 = (Methode)allmethods.get(it);
                if (!methode2.getId().equals(mIdElt)) {
                    boolean res = calculeCopierColler(connection, idLinkProg, methode2, calculator);
                    if (res) {
                        nbCc++;
                    }
                }
            }
        }
        if (nbCc > 0) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
            metriqueDao.setMetrique(mIdElt, mBaseline.getId(), "COPY_PASTE", nbCc, connection, true);
        }
    }
    
    private boolean validData(Qametrique[] data) {
        boolean result = true;
        int i = 0;
        while ((i < data.length) && result) {
            if (data[i] == null) {
                result = false;
            }
            i++;
        }
        return result;
    }
    
    public boolean calculeCopierColler(Connection connection, String idLinkProg, Methode methode2, UsageCalculator calculator) throws DataAccessException {
        boolean result = false;
        Map<String, ValuedObject> metriques2 = methode2.getMetriques();
        Qametrique evg2 = (Qametrique)metriques2.get("EVG");
        double doCalcul = calculator.eval(this.mUsage.getId(), "DO_COPIER_COLLER", metriques2, RuleSet.STEREOTYPE_ALL);
        if ((doCalcul == 1) && (evg2 != null)) {
            if ((mMetriques != null) && (metriques2 != null)) {
                // Métrique de la méthode de base.
                Qametrique vg = (Qametrique)mMetriques.get("VG");
                Qametrique allCode = (Qametrique)mMetriques.get("ALL_CODE");
                Qametrique evg = (Qametrique)mMetriques.get("EVG");
                Qametrique ivg = (Qametrique)mMetriques.get("IVG");
                Qametrique halEffort = (Qametrique)mMetriques.get("HAL_EFFORT");
                Qametrique branch = (Qametrique)mMetriques.get("BRANCH");
                Qametrique uniqueOperand = (Qametrique)mMetriques.get("UNIQUE_OPERANDS");
                Qametrique[] dataMeth = {vg, allCode, evg, ivg, halEffort, branch, uniqueOperand};
                // Métriques de la méthode à comparer.
                Qametrique vg2 = (Qametrique)metriques2.get("VG");
                Qametrique allCode2 = (Qametrique)metriques2.get("ALL_CODE");
                Qametrique ivg2 = (Qametrique)metriques2.get("IVG");
                Qametrique halEffort2 = (Qametrique)metriques2.get("HAL_EFFORT");
                Qametrique branch2 = (Qametrique)metriques2.get("BRANCH");
                Qametrique uniqueOperand2 = (Qametrique)metriques2.get("UNIQUE_OPERANDS");
                Qametrique[] dataMeth2 = {vg2, allCode2, evg2, ivg2, halEffort2, branch2, uniqueOperand2};
                boolean valid = true;
                valid = validData(dataMeth);
                if (valid) {
                    valid = validData(dataMeth2);
                }
                if (valid) {
                    Map<String, ValuedObject> vars = new HashMap<String, ValuedObject>();
                    vars.putAll(mMetriques);
                    addMetriques(vars, metriques2, "2");
                    double cc = calculator.eval(this.mUsage.getId(), "NB_COPIER_COLLER", vars, RuleSet.STEREOTYPE_ALL);
                    if (cc == 1.0) {
                        storeCopierColler(connection, idLinkProg, methode2, cc);
                        result = true;
                    }
                }
            }
        }
        return result;
    }
    
    private void addMetriques(Map<String, ValuedObject> vars, Map<String, ValuedObject> toAdd, String suffixe) {
        Iterator<String> i = toAdd.keySet().iterator();
        while (i.hasNext()) {
            String key = (String)i.next();
            vars.put(key+suffixe, toAdd.get(key));
        }
    }
    
    private void storeCopierColler(Connection conn, String idLinkProg, Methode methode2, double cc) throws DataAccessException {
        String selectRequest = "Select link_id From LINK_ELT_BLINE Where id_bline=? And ((elt_from_id=? And elt_to_id=?) Or (elt_to_id=? And elt_from_id=?)) And real_link_id=?";
        String insertRequest = "Insert into LINK_ELT_BLINE (link_id, id_bline, elt_from_id, elt_to_id, real_link_id, type) VALUES (?, ?, ?, ?, ?, 'COPYPASTE')";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(selectRequest);
            pstmt.setString(1, mBaseline.getId());
            pstmt.setString(2, mIdElt);
            pstmt.setString(3, methode2.getId());
            pstmt.setString(4, mIdElt);
            pstmt.setString(5, methode2.getId());
            pstmt.setString(6, idLinkProg);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pstmt.close();
                // Already exist.
            }
            else {
                pstmt.close();
                if (cc >= 0.97 && cc <= 1.03) {
                    pstmt = conn.prepareStatement(insertRequest);
                    pstmt.setString(1, IDCreator.getID());
                    pstmt.setString(2, mBaseline.getId());
                    pstmt.setString(3, mIdElt);
                    pstmt.setString(4, methode2.getId());
                    pstmt.setString(5, idLinkProg);
                    pstmt.executeUpdate();
                    JdbcDAOUtils.commit(conn);
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Error saving copy-paste", e);
        }
        finally {
            // Fermeture du résultat et de la requête.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            //conn.setAutoCommit(true);
        }
    }
    
    /**
     * Méthode d'accès à la taille de la méthode.
     * @return la taille de la méthode.
     */
    public double getTailleMethode() {
        // Variable résultat.
        double result = 0.0;
        // Récupération de la métrique du nombre de ligne de code.
        Qametrique tm = (Qametrique)mMetriques.get("ALL_CODE");
        if (tm != null) {
            // Initialisation de la taille de la méthode.
            result = tm.getValbrute();
        }
        return result;
    }

}

/**
 * 
 */
package com.compuware.caqs.dao.interfaces;

import java.util.Collection;
import java.util.Map;

import com.compuware.caqs.business.calculation.Baseline;
import com.compuware.caqs.domain.calculation.rules.ValuedObject;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.exception.DataAccessException;

/**
 * @author cwfr-fdubois
 *
 */
public interface CalculationDao {

	public abstract Map<String, Map<String, ValuedObject>> retrieveMetrics(String idMainElt, String idBline) throws DataAccessException;
	
	public abstract Map<String, Map<String, Critere>> retrieveCriterion(String idMainElt, String idBline) throws DataAccessException;
	
	public abstract Collection<String> retrieveFactors(String idUsa) throws DataAccessException;

	public Map<String,String> retrieveModuleBaselineMap(String baselineId) throws DataAccessException;
	
	public abstract boolean metricExistsForBaseline(BaselineBean baseline) throws DataAccessException;
	
    /** R�cup�re les justificatifs de crit�re valid�s existant pour le module, la baseline donn�e ou la pr�c�dente.
     * @param idElt l'identifiant du module.
     * @param bline la baseline en cours d'analyse.
     * @return la table des justificatifs existants.
	 * @throws DataAccessException
     */
    public Map<String, Map<String, Critere>> getCritereJustificatifs(String idElt, Baseline bline) throws DataAccessException;
 
    public void createModuleBaselineAssoc(
			String projectId, BaselineBean baselineBean, String[] eaArray) throws DataAccessException;

    /**
     * remove all scores which where calculated and are not in the quality model anymore.
     * it is used for a re-compute when the quality model has been modified this the
     * first compute
     * @param idBline the baseline id to manage
     * @param idUsa the quality model's id
     * @param idEa application entity's id
     */
    public void removeResultsNotInQualityModelAnymore(String idBline, String idUsa, String idEa);
    
}

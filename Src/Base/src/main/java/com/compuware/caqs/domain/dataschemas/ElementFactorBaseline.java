package com.compuware.caqs.domain.dataschemas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ElementFactorBaseline extends ElementBean {

	private static final long serialVersionUID = 1738421288351843558L;
	
	ElementFactorBaseline father;
	List sons = new ArrayList();
	
	Map factors = new HashMap();

	/**
	 * @return Returns the factors.
	 */
	public Map getFactors() {
		return factors;
	}
	
	public Map getFactorMap(String idBline) {
		return (Map)factors.get(idBline);
	}

	public FactorBean getFactor(String idFact, String idBline) {
		FactorBean result = null;
		Map factorMap = (Map)factors.get(idBline);
		if (factorMap != null) {
			result = (FactorBean)factorMap.get(idFact);
		}
		return result;
	}

	public Collection getFactorList() {
		Collection result = null;
		if (!factors.values().isEmpty()) {
			Map factorMap = (Map)(factors.values().iterator().next());
			result = factorMap.values(); 
		}
		return result;
	}

	/**
	 * @param factors The factors to set.
	 */
	public void setFactors(Map factors) {
		this.factors = factors;
	}
	
	public void addFactor(String idBline, Map factorMap) {
		factors.put(idBline, factorMap);
	}

	/**
	 * @return Returns the father.
	 */
	public ElementFactorBaseline getFather() {
		return father;
	}

	/**
	 * @param father The father to set.
	 */
	public void setFather(ElementFactorBaseline father) {
		this.father = father;
	}

	/**
	 * @return Returns the son.
	 */
	public List getSons() {
		return sons;
	}

	/**
	 * @param son The son to set.
	 */
	public void setSons(List sons) {
		this.sons = sons;
	}
	
	public void addSon(ElementFactorBaseline son) {
		this.sons.add(son);
	}
	
	public int getDeep() {
		int result = 0;
		Iterator i = sons.iterator();
		ElementFactorBaseline son = null;
		while (i.hasNext()) {
			son = (ElementFactorBaseline)i.next();
			if (son.getDeep() > result) {
				result = son.getDeep();
			}
		}
		return result + 1;
	}
	
	public int getPosition() {
		int result = 1;
		if (father != null) {
			result = result + father.getPosition();
		}
		return result;
	}
	
}

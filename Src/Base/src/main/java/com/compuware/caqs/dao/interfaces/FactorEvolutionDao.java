package com.compuware.caqs.dao.interfaces;

import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorEvolutionBean;
import java.util.List;

public interface FactorEvolutionDao {

    public abstract List<FactorEvolutionBean> retrieveEvolutionMinAvg(ElementBean elt);

	public abstract Collection retrieveFactorEvolution(ElementBean eltBean);

	public abstract Collection<FactorBean> retrieveFactorEvolution(ElementBean eltBean, BaselineBean bline);

	public abstract Collection retrieveCriterionEvolution(ElementBean eltBean);

	public abstract List<BaselineBean> retrieveBaselines(ElementBean eltBean);

}
/**
 * 
 */
package com.compuware.caqs.dao.factory;

import com.compuware.caqs.dao.dbms.ActionPlanDbmsDao;
import com.compuware.caqs.dao.dbms.ActionPlanUnitDbmsDao;
import com.compuware.caqs.dao.dbms.ArchitectureDbmsDao;
import com.compuware.caqs.dao.dbms.BaselineDbmsDao;
import com.compuware.caqs.dao.dbms.CalculationDbmsDao;
import com.compuware.caqs.dao.dbms.CallDbmsDao;
import com.compuware.caqs.dao.dbms.CaqsMessageDbmsDao;
import com.compuware.caqs.dao.dbms.CriterionDbmsDao;
import com.compuware.caqs.dao.dbms.DialecteDbmsDao;
import com.compuware.caqs.dao.dbms.ElementDbmsDao;
import com.compuware.caqs.dao.dbms.ElementTypeDbmsDao;
import com.compuware.caqs.dao.dbms.EvolutionDbmsDao;
import com.compuware.caqs.dao.dbms.FactorDbmsDao;
import com.compuware.caqs.dao.dbms.FactorEvolutionDbmsDao;
import com.compuware.caqs.dao.dbms.InternationalizationDbmsDao;
import com.compuware.caqs.dao.dbms.JustificatifDbmsDao;
import com.compuware.caqs.dao.dbms.LabelDbmsDao;
import com.compuware.caqs.dao.dbms.LangueDbmsDao;
import com.compuware.caqs.dao.dbms.MetriqueDbmsDao;
import com.compuware.caqs.dao.dbms.OutilDbmsDao;
import com.compuware.caqs.dao.dbms.PortalUserDbmsDao;
import com.compuware.caqs.dao.dbms.ProjectDbmsDao;
import com.compuware.caqs.dao.dbms.SettingsDbmsDao;
import com.compuware.caqs.dao.dbms.StatisticsDbmsDao;
import com.compuware.caqs.dao.dbms.StereotypeDbmsDao;
import com.compuware.caqs.dao.dbms.TaskDbmsDao;
import com.compuware.caqs.dao.dbms.TendanceDbmsDao;
import com.compuware.caqs.dao.dbms.UsageDbmsDao;
import com.compuware.caqs.dao.dbms.UsersDbmsDao;
import com.compuware.caqs.dao.interfaces.ActionPlanDao;
import com.compuware.caqs.dao.interfaces.ActionPlanUnitDao;
import com.compuware.caqs.dao.interfaces.ArchitectureDao;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.CalculationDao;
import com.compuware.caqs.dao.interfaces.CallDao;
import com.compuware.caqs.dao.interfaces.CaqsMessageDao;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.DialecteDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.ElementTypeDao;
import com.compuware.caqs.dao.interfaces.EvolutionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.FactorEvolutionDao;
import com.compuware.caqs.dao.interfaces.InternationalizationDao;
import com.compuware.caqs.dao.interfaces.JustificatifDao;
import com.compuware.caqs.dao.interfaces.LabelDao;
import com.compuware.caqs.dao.interfaces.LangueDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.dao.interfaces.OutilDao;
import com.compuware.caqs.dao.interfaces.PortalUserDao;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.dao.interfaces.SettingsDao;
import com.compuware.caqs.dao.interfaces.StatisticsDao;
import com.compuware.caqs.dao.interfaces.StereotypeDao;
import com.compuware.caqs.dao.interfaces.TaskDao;
import com.compuware.caqs.dao.interfaces.TendanceDao;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.dao.interfaces.UsersDao;

/**
 * @author cwfr-fdubois
 *
 */
public class DbmsDaoFactory extends DaoFactory {

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getBaselineDao()
	 */
	public BaselineDao getBaselineDao() {
		return BaselineDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getCriterionDao()
	 */
	public CriterionDao getCriterionDao() {
		return CriterionDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getDialecteDao()
	 */
	public DialecteDao getDialecteDao() {
		return DialecteDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getElementDao()
	 */
	public ElementDao getElementDao() {
		return ElementDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getFactorDao()
	 */
	public FactorDao getFactorDao() {
		return FactorDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getFactorEvolutionDao()
	 */
	public FactorEvolutionDao getFactorEvolutionDao() {
		return FactorEvolutionDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getInternationalizationDao()
	 */
	public InternationalizationDao getInternationalizationDao() {
		return new InternationalizationDbmsDao();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getJustificatifDao()
	 */
	public JustificatifDao getJustificatifDao() {
		return new JustificatifDbmsDao();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getLabelDao()
	 */
	public LabelDao getLabelDao() {
		return LabelDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getMetriqueDao()
	 */
	public MetriqueDao getMetriqueDao() {
		return MetriqueDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getOutilDao()
	 */
	public OutilDao getOutilDao() {
		return OutilDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getProjectDao()
	 */
	public ProjectDao getProjectDao() {
		return ProjectDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getStereotypeDao()
	 */
	public StereotypeDao getStereotypeDao() {
		return StereotypeDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getUsageDao()
	 */
	public UsageDao getUsageDao() {
		return UsageDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getUsersDao()
	 */
	public UsersDao getUsersDao() {
		return UsersDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getPortalUserDao()
	 */
	public PortalUserDao getPortalUserDao() {
		return PortalUserDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getTendanceDao()
	 */
	public TendanceDao getTendanceDao() {
		return TendanceDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getArchitectureDao()
	 */
	public ArchitectureDao getArchitectureDao() {
		return ArchitectureDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.caqs.dao.factory.DaoFactory#getCallDao()
	 */
	public CallDao getCallDao() {
		return CallDbmsDao.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.dao.factory.DaoFactory#getCalculationDao()
	 */
	public CalculationDao getCalculationDao() {
		return CalculationDbmsDao.getInstance();
	}

	@Override
	public CaqsMessageDao getCaqsMessagesDao() {
		return CaqsMessageDbmsDao.getInstance();
	}

	@Override
	public TaskDao getTaskDao() {
		return TaskDbmsDao.getInstance();
	}

	@Override
	public SettingsDao getSettingsDao() {
		return SettingsDbmsDao.getInstance();
	}

	@Override
	public ActionPlanDao getActionPlanDao() {
		return ActionPlanDbmsDao.getInstance();
	}

    /**
     * @{@inheritDoc }
     */
	public ActionPlanUnitDao getActionPlanUnitDao() {
		return ActionPlanUnitDbmsDao.getInstance();
	}

    @Override
    public StatisticsDao getStatisticsDao() {
        return StatisticsDbmsDao.getInstance();
    }

    /**
     * @{@inheritDoc }
     */
    public ElementTypeDao getElementTypeDao() {
        return ElementTypeDbmsDao.getInstance();
    }

    /**
     * @{@inheritDoc }
     */
    public LangueDao getLangueDao() {
        return LangueDbmsDao.getInstance();
    }

    /**
     * @{@inheritDoc }
     */
    public EvolutionDao getEvolutionDao() {
        return EvolutionDbmsDao.getInstance();
    }

    @Override
    public UsersDao getUserDao() {
        return UsersDbmsDao.getInstance();
    }
}

package com.compuware.caqs.dao.factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
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
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.io.PropertiesReader;

public abstract class DaoFactory {
	
    //logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);

    private static DaoFactory singleton = DaoFactory.createDaoFactory();
	
    private static final String DEFAULT_DATABASE_KEY = "default";
    
    private static String getDatabaseName() {
		Connection connection = JdbcDAOUtils.getConnection(DaoFactory.class, Constants.CAQS_DATASOURCE_KEY);
		String databaseName = DEFAULT_DATABASE_KEY;
		if (connection != null) {
			try {
				databaseName = connection.getMetaData().getDatabaseProductName();
			}
			catch (SQLException e) {
				databaseName = DEFAULT_DATABASE_KEY;
			}
			finally {
				JdbcDAOUtils.closeConnection(connection);
			}
		}
		databaseName = databaseName.replaceAll(" ", "");
		logger.debug("CAQS database: " + databaseName.toLowerCase());
		return databaseName.toLowerCase();
    }
    
    private static Class getDaoFactoryClass() {
    	Class result = null;
		try {
			result = Class.forName("com.compuware.caqs.dao.factory.DaoFactory");
		} catch (ClassNotFoundException e) {
			logger.error("Error getting current class");
		}
		return result;
    }
    
	private static DaoFactory createDaoFactory() {
		DaoFactory result = null;
		Properties daoConfig;
		daoConfig = PropertiesReader.getProperties("/com/compuware/caqs/dao/config.properties", DaoFactory.getDaoFactoryClass(), true);
		String databaseName = getDatabaseName();
		String daoClassName = daoConfig.getProperty("dao." + databaseName);
		if (daoClassName == null) {
			daoClassName = daoConfig.getProperty("dao." + DEFAULT_DATABASE_KEY);
		}
		if (daoClassName != null) {
			Class cls;
			try {
				cls = Class.forName(daoClassName);
				if (cls != null) {
					result = (DaoFactory)cls.newInstance();
				}
			} catch (ClassNotFoundException e) {
				logger.error("Error creating a new dao factory", e);
			} catch (InstantiationException e) {
				logger.error("Error creating a new dao factory", e);
			} catch (IllegalAccessException e) {
				logger.error("Error creating a new dao factory", e);
			}
		}
		return result;
	}
	
	public static DaoFactory getInstance() {
		return singleton;
	}

	/**
	 * Get a DAO instance for baseline. 
	 * @return a DAO instance for baseline.
	 */
	public abstract BaselineDao getBaselineDao();
	
	/**
	 * Get a DAO instance for criterion. 
	 * @return a DAO instance for criterion.
	 */
	public abstract CriterionDao getCriterionDao(); 
	
	/**
	 * Get a DAO instance for dialecte. 
	 * @return a DAO instance for dialecte.
	 */
	public abstract DialecteDao getDialecteDao(); 
	
	/**
	 * Get a DAO instance for element. 
	 * @return a DAO instance for element.
	 */
	public abstract ElementDao getElementDao(); 

	/**
	 * Get a DAO instance for factor. 
	 * @return a DAO instance for factor.
	 */
	public abstract FactorDao getFactorDao(); 

	/**
	 * Get a DAO instance for factor evolution. 
	 * @return a DAO instance for factor evolution.
	 */
	public abstract FactorEvolutionDao getFactorEvolutionDao(); 

	/**
	 * Get a DAO instance for internationalization. 
	 * @return a DAO instance for internationalization.
	 */
	public abstract InternationalizationDao getInternationalizationDao(); 

	/**
	 * Get a DAO instance for justificatif. 
	 * @return a DAO instance for justificatif.
	 */
	public abstract JustificatifDao getJustificatifDao(); 

	/**
	 * Get a DAO instance for label. 
	 * @return a DAO instance for label.
	 */
	public abstract LabelDao getLabelDao(); 

	/**
	 * Get a DAO instance for metric. 
	 * @return a DAO instance for metric.
	 */
	public abstract MetriqueDao getMetriqueDao(); 

	/**
	 * Get a DAO instance for outil. 
	 * @return a DAO instance for outil.
	 */
	public abstract OutilDao getOutilDao(); 

	/**
	 * Get a DAO instance for project. 
	 * @return a DAO instance for project.
	 */
	public abstract ProjectDao getProjectDao(); 

	/**
	 * Get a DAO instance for stereotype. 
	 * @return a DAO instance for stereotype.
	 */
	public abstract StereotypeDao getStereotypeDao(); 

	/**
	 * Get a DAO instance for usage. 
	 * @return a DAO instance for usage.
	 */
	public abstract UsageDao getUsageDao(); 

	/**
	 * Get a DAO instance for user. 
	 * @return a DAO instance for user.
	 */
	public abstract UsersDao getUsersDao(); 

	/**
	 * Get a DAO instance for portal user.
	 * @return a DAO instance for portal user.
	 */
	public abstract PortalUserDao getPortalUserDao();

	/**
	 * Get a DAO instance for tendance. 
	 * @return a DAO instance for tendance.
	 */
	public abstract TendanceDao getTendanceDao(); 

	/**
	 * Get a DAO instance for architecture. 
	 * @return a DAO instance for architecture.
	 */
	public abstract ArchitectureDao getArchitectureDao(); 

	/**
	 * Get a DAO instance for architecture calls. 
	 * @return a DAO instance for architecture calls.
	 */
	public abstract CallDao getCallDao(); 

	/**
	 * Get a DAO instance for calculation. 
	 * @return a DAO instance for calculation.
	 */
	public abstract CalculationDao getCalculationDao(); 

	/**
	 * Get a DAO instance for task handling. 
	 * @return a DAO instance for task handling.
	 */
	public abstract TaskDao getTaskDao(); 

	/**
	 * Get a DAO instance for caqs messages handling. 
	 * @return a DAO instance for caqs messages handling.
	 */
	public abstract CaqsMessageDao getCaqsMessagesDao();
	
	/**
	 * Get a DAO instance for settings handling. 
	 * @return a DAO instance for settings handling.
	 */
	public abstract SettingsDao getSettingsDao();

	/**
	 * Get a DAO instance for action plan handling. 
	 * @return a DAO instance for action plan handling.
	 */
	public abstract ActionPlanDao getActionPlanDao();

    /**
	 * Get a DAO instance for action plan unit handling. 
	 * @return a DAO instance for action plan unit handling.
	 */
	public abstract ActionPlanUnitDao getActionPlanUnitDao();

    /**
	 * Get a DAO instance for statistics handling.
	 * @return a DAO instance for statistics handling.
	 */
	public abstract StatisticsDao getStatisticsDao();

 	/**
	 * Get a DAO instance for element type.
	 * @return a DAO instance for element type.
	 */
	public abstract ElementTypeDao getElementTypeDao();

 	/**
	 * Get a DAO instance for language.
	 * @return a DAO instance for language.
	 */
	public abstract LangueDao getLangueDao();

        /**
	 * Get a DAO instance for evolution.
	 * @return a DAO instance for evolution.
	 */
	public abstract EvolutionDao getEvolutionDao();

 	/**
	 * Get a DAO instance for user.
	 * @return a DAO instance for user.
	 */
    public abstract UsersDao getUserDao();
    
}
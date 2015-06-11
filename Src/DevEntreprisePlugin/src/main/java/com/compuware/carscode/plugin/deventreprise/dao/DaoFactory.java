package com.compuware.carscode.plugin.deventreprise.dao;

public class DaoFactory {

	private static DaoFactory singleton = new DaoFactory();
	
	private DaoFactory() {
	}

	public static DaoFactory getInstance() {
		return singleton;
	}
	
	public DevEntrepriseDao getDevPartnerDao() {
		return DevEntrepriseSqlServerDao.getInstance();
	}
	
}

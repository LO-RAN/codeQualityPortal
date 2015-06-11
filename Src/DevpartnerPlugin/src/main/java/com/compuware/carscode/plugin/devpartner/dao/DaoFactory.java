package com.compuware.carscode.plugin.devpartner.dao;

public class DaoFactory {

	private static DaoFactory singleton = new DaoFactory();
	
	public static final String DIALECT_VBDOTNET = ".Net";
	
	private DaoFactory() {
	}

	public static DaoFactory getInstance() {
		return singleton;
	}
	
	public DevPartnerDao getDevPartnerDao(String dialect) {
		DevPartnerDao retour = null;
		if(dialect.contains(DaoFactory.DIALECT_VBDOTNET)) {
			retour = new DevPartnerVBNetMdbDao();
		} else {
			retour = new DevPartnerMdbDao();
		}
		return retour;
	}
	
}

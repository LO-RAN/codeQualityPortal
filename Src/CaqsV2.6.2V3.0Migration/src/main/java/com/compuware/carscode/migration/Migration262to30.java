package com.compuware.carscode.migration;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.compuware.carscode.migration.bean.CritereBean;
import com.compuware.carscode.migration.bean.FacteurBean;
import com.compuware.carscode.migration.bean.MetriqueBean;
import com.compuware.carscode.migration.bean.ModeleBean;
import com.compuware.carscode.migration.bean.OutilsModeleBean;
import com.compuware.carscode.migration.bean.StereotypeBean;
import com.compuware.carscode.migration.bean.Type_elementBean;
import com.compuware.carscode.migration.dao.Migration262to30Dao;
import com.compuware.carscode.migration.exception.MigrationException;

public class Migration262to30 {
	
	protected static final Logger mLog = com.compuware.util.logging.LoggerManager.getLogger("Caqs");
	private Migration262to30Dao dao = null;
	
	public Migration262to30() {
		dao = new Migration262to30Dao();
		this.migrate();
	}
	
	private void migrate() {
		try {
			mLog.debug("D�but de la migration de la base");
			mLog.debug("R�cup�ration des mod�les");
			List<ModeleBean> modeles = dao.getModeles();
			mLog.debug("Internationalisation de "+modeles.size()+" mod�le(s)");
			for(ModeleBean modele : modeles) {
				dao.addI18NForModele(modele);
				List<OutilsModeleBean> l = dao.getOutilsModeles(modele.getId_usa());
				if(l!=null && !l.isEmpty()) {
					dao.addOutilsModele(l);
				}
			}
			mLog.debug("R�cup�ration des crit�res");
			List<CritereBean> criteres = dao.getCriteres();
			mLog.debug("Internationalisation de "+criteres.size()+" crit�re(s)");
			for(CritereBean critere : criteres) {
				dao.addI18NForCritere(critere);
			}
			mLog.debug("R�cup�ration des m�triques");
			List<MetriqueBean> metriques = dao.getMetriques();
			mLog.debug("Internationalisation de "+metriques.size()+" m�trique(s)");
			for(MetriqueBean metrique : metriques) {
				dao.addI18NForMetrique(metrique);
				dao.commitTransaction();
			}
			mLog.debug("R�cup�ration des m�triques");
			List<FacteurBean> facteurs = dao.getFacteurs();
			mLog.debug("Internationalisation de "+facteurs.size()+" objectifs(s)");
			for(FacteurBean facteur : facteurs) {
				dao.addI18NForFacteur(facteur);
				dao.commitTransaction();
			}
			mLog.debug("R�cup�ration des types d'�l�ments");
			List<Type_elementBean> type_elements = dao.getType_elements();
			mLog.debug("Internationalisation de "+type_elements.size()+" type(s) d'�l�ments");
			for(Type_elementBean type_element : type_elements) {
				dao.addI18NForType_element(type_element);
				dao.commitTransaction();
			}
			mLog.debug("R�cup�ration des st�rotypes");
			List<StereotypeBean> stereotypes = dao.getStereotypes();
			mLog.debug("Internationalisation de "+stereotypes.size()+" st�r�otype(s)");
			for(StereotypeBean stereotype : stereotypes) {
				dao.addI18NForStereotype(stereotype);
				dao.commitTransaction();
			}
			dao.commitTransaction();
		} catch(MigrationException exc) {
			mLog.error("Erreur survenue lors de la migration de la base vers la version 3.0",exc);
		}
	}
	
	public static void main(String[] args) throws SQLException, MigrationException {
		Migration262to30 mig = new Migration262to30();
		mig.migrate();
		mLog.debug("Migration finished");
	}
}
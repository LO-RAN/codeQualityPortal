package com.compuware.carscode.migration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.compuware.carscode.migration.bean.CritereBean;
import com.compuware.carscode.migration.bean.FacteurBean;
import com.compuware.carscode.migration.bean.MetriqueBean;
import com.compuware.carscode.migration.bean.ModeleBean;
import com.compuware.carscode.migration.bean.OutilsModeleBean;
import com.compuware.carscode.migration.bean.StereotypeBean;
import com.compuware.carscode.migration.bean.Type_elementBean;
import com.compuware.carscode.migration.exception.MigrationException;
import com.compuware.dbms.JdbcDAOUtils;

public class Migration262to30Dao {

    //logger
	protected static final Logger mLog = com.compuware.util.logging.LoggerManager.getLogger("Caqs");

	private Connection connection = null;
	
	public Migration262to30Dao() {
		connection = JdbcDAOUtils.getConnection(this, null);
	}
	
	public void commitTransaction() throws MigrationException {
		try {
			JdbcDAOUtils.commit(connection);
		}
		catch (SQLException e) {
			mLog.error("Error during commit", e);
			throw new MigrationException(e);
		}
	}
	
	public void rollbackTransaction() {
		JdbcDAOUtils.rollbackConnection(connection);
	}
	
	public List<ModeleBean> getModeles() {
		List<ModeleBean> result = new ArrayList<ModeleBean>();
		if(connection==null) {
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ModeleBean currentModele = null;
		try {
			pstmt = connection.prepareStatement("Select id_usa, lib_usa, desc_usa from Modele");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentModele = new ModeleBean();
				currentModele.setId_usa(rs.getString("id_usa"));
				currentModele.setLib_usa(rs.getString("lib_usa"));
				currentModele.setDesc_usa(rs.getString("desc_usa"));
				result.add(currentModele);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}

	public void addI18NForModele(ModeleBean bean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('modele', 'lib', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_usa());
			pstmt.setString(2, (bean.getLib_usa()==null)?bean.getId_usa():bean.getLib_usa());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('modele', 'lib', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_usa());
			pstmt.setString(2, (bean.getLib_usa()==null)?bean.getId_usa():bean.getLib_usa());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('modele', 'desc', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_usa());
			pstmt.setString(2, (bean.getDesc_usa()==null)?bean.getId_usa():bean.getDesc_usa());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('modele', 'desc', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_usa());
			pstmt.setString(2, (bean.getDesc_usa()==null)?bean.getId_usa():bean.getDesc_usa());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
	}

	public List<CritereBean> getCriteres() {
		List<CritereBean> result = new ArrayList<CritereBean>();
		if(connection==null) {
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CritereBean currentCritere = null;
		try {
			pstmt = connection.prepareStatement("Select id_crit, lib_crit, desc_crit from Critere");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentCritere = new CritereBean();
				currentCritere.setId_crit(rs.getString("id_crit"));
				currentCritere.setLib_crit(rs.getString("lib_crit"));
				currentCritere.setDesc_crit(rs.getString("desc_crit"));
				result.add(currentCritere);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}

	public void addI18NForCritere(CritereBean bean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('critere', 'lib', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_crit());
			pstmt.setString(2, (bean.getLib_crit()==null)?bean.getId_crit():bean.getLib_crit());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('critere', 'lib', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_crit());
			pstmt.setString(2, (bean.getLib_crit()==null)?bean.getId_crit():bean.getLib_crit());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('critere', 'desc', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_crit());
			pstmt.setString(2, (bean.getDesc_crit()==null)?bean.getId_crit():bean.getDesc_crit());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('critere', 'desc', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_crit());
			pstmt.setString(2, (bean.getDesc_crit()==null)?bean.getId_crit():bean.getDesc_crit());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
	}
	
	public List<MetriqueBean> getMetriques() {
		List<MetriqueBean> result = new ArrayList<MetriqueBean>();
		if(connection==null) {
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MetriqueBean currentMetrique = null;
		try {
			pstmt = connection.prepareStatement("Select id_met, lib_met, desc_met from Metrique");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentMetrique = new MetriqueBean();
				currentMetrique.setId_met(rs.getString("id_met"));
				currentMetrique.setLib_met(rs.getString("lib_met"));
				currentMetrique.setDesc_met(rs.getString("desc_met"));
				result.add(currentMetrique);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}
	
	public void addI18NForMetrique(MetriqueBean bean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('metrique', 'lib', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_met());
			pstmt.setString(2, (bean.getLib_met()==null)?bean.getId_met():bean.getLib_met());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('metrique', 'lib', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_met());
			pstmt.setString(2, (bean.getLib_met()==null)?bean.getId_met():bean.getLib_met());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('metrique', 'desc', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_met());
			pstmt.setString(2, (bean.getDesc_met()==null)?bean.getId_met():bean.getDesc_met());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('metrique', 'desc', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_met());
			pstmt.setString(2, (bean.getDesc_met()==null)?bean.getId_met():bean.getDesc_met());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
	}


	public List<FacteurBean> getFacteurs() {
		List<FacteurBean> result = new ArrayList<FacteurBean>();
		if(connection==null) {
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FacteurBean currentFacteur = null;
		try {
			pstmt = connection.prepareStatement("Select id_fact, lib_fact, desc_fact from Facteur");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentFacteur = new FacteurBean();
				currentFacteur.setId_fact(rs.getString("id_fact"));
				currentFacteur.setLib_fact(rs.getString("lib_fact"));
				currentFacteur.setDesc_fact(rs.getString("desc_fact"));
				result.add(currentFacteur);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}
	
	public void addI18NForFacteur(FacteurBean bean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('facteur', 'lib', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_fact());
			pstmt.setString(2, (bean.getLib_fact()==null)?bean.getId_fact():bean.getLib_fact());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('facteur', 'lib', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_fact());
			pstmt.setString(2, (bean.getLib_fact()==null)?bean.getId_fact():bean.getLib_fact());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('facteur', 'desc', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_fact());
			pstmt.setString(2, (bean.getDesc_fact()==null)?bean.getId_fact():bean.getDesc_fact());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('facteur', 'desc', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_fact());
			pstmt.setString(2, (bean.getDesc_fact()==null)?bean.getId_fact():bean.getDesc_fact());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
	}


	public List<StereotypeBean> getStereotypes() {
		List<StereotypeBean> result = new ArrayList<StereotypeBean>();
		if(connection==null) {
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StereotypeBean currentStereotype = null;
		try {
			pstmt = connection.prepareStatement("Select id_stereotype, lib_stereotype, desc_stereotype from Stereotype");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentStereotype = new StereotypeBean();
				currentStereotype.setId_stereotype(rs.getString("id_stereotype"));
				currentStereotype.setLib_stereotype(rs.getString("lib_stereotype"));
				currentStereotype.setDesc_stereotype(rs.getString("desc_stereotype"));
				result.add(currentStereotype);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}
	
	public void addI18NForStereotype(StereotypeBean bean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('stereotype', 'lib', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_stereotype());
			pstmt.setString(2, (bean.getLib_stereotype()==null)?bean.getId_stereotype():bean.getLib_stereotype());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('stereotype', 'lib', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_stereotype());
			pstmt.setString(2, (bean.getLib_stereotype()==null)?bean.getId_stereotype():bean.getLib_stereotype());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('stereotype', 'desc', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_stereotype());
			pstmt.setString(2, (bean.getDesc_stereotype()==null)?bean.getId_stereotype():bean.getDesc_stereotype());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('stereotype', 'desc', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_stereotype());
			pstmt.setString(2, (bean.getDesc_stereotype()==null)?bean.getId_stereotype():bean.getDesc_stereotype());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
	}
	
	public List<Type_elementBean> getType_elements() {
		List<Type_elementBean> result = new ArrayList<Type_elementBean>();
		if(connection==null) {
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Type_elementBean currentType_element = null;
		try {
			pstmt = connection.prepareStatement("Select id_telt, lib_telt, desc_telt from Type_element");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentType_element = new Type_elementBean();
				currentType_element.setId_telt(rs.getString("id_telt"));
				currentType_element.setLib_telt(rs.getString("lib_telt"));
				currentType_element.setDesc_telt(rs.getString("desc_telt"));
				result.add(currentType_element);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}
	
	public void addI18NForType_element(Type_elementBean bean) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('type_element', 'lib', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_telt());
			pstmt.setString(2, (bean.getLib_telt()==null)?bean.getId_telt():bean.getLib_telt());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('type_element', 'lib', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_telt());
			pstmt.setString(2, (bean.getLib_telt()==null)?bean.getId_telt():bean.getLib_telt());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('type_element', 'desc', ?, 'fr', ?)");
			pstmt.setString(1, bean.getId_telt());
			pstmt.setString(2, (bean.getDesc_telt()==null)?bean.getId_telt():bean.getDesc_telt());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		try {
			pstmt = connection.prepareStatement("insert into I18n(TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) values('type_element', 'desc', ?, 'en', ?)");
			pstmt.setString(1, bean.getId_telt());
			pstmt.setString(2, (bean.getDesc_telt()==null)?bean.getId_telt():bean.getDesc_telt());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			//mLog.debug("Error during insert : "+e.getMessage());
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
	}
	
	public List<OutilsModeleBean> getOutilsModeles(String id_usa) {
		List<OutilsModeleBean> retour = new ArrayList<OutilsModeleBean>();
		if(connection==null) {
			return retour;
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OutilsModeleBean currentOutilsModele = null;
		try {
			pstmt = connection.prepareStatement("select distinct outil_met from regle, metrique where regle.id_usa = ? and regle.id_met = metrique.id_met");
			pstmt.setString(1, id_usa);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentOutilsModele = new OutilsModeleBean();
				currentOutilsModele.setId_usa(id_usa);
				currentOutilsModele.setId_outils(rs.getString("outil_met"));
				retour.add(currentOutilsModele);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		
		return retour;
	}
	
	public void addOutilsModele(List<OutilsModeleBean> beans) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		for(OutilsModeleBean bean : beans) {
			try {
				pstmt = connection.prepareStatement("insert into OUTILS_MODELE(ID_USA, ID_OUTILS) values(?, ?)");
				pstmt.setString(1, bean.getId_usa());
				pstmt.setString(2, bean.getId_outils());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				//mLog.debug("Error during insert : "+e.getMessage());
			}
			finally {
				JdbcDAOUtils.closePrepareStatement(pstmt);
				JdbcDAOUtils.closeResultSet(rs);
			}
		}
	}
}

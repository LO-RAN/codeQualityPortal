package com.compuware.carscode.migration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.compuware.carscode.migration.bean.ClasseBean;
import com.compuware.carscode.migration.bean.EaBean;
import com.compuware.carscode.migration.bean.PackageBean;
import com.compuware.carscode.migration.bean.ProjectBean;
import com.compuware.carscode.migration.exception.MigrationException;
import com.compuware.dbms.JdbcDAOUtils;

public class Migration26to262Dao {

    //logger
	protected static final Logger mLog = com.compuware.util.logging.LoggerManager.getLogger("Caqs");

	private Connection connection = null;
	
	public Migration26to262Dao() {
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
	
	public List<ClasseBean> retrieveAllClasses(String eaId) throws MigrationException {
		List<ClasseBean> result = new ArrayList<ClasseBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ClasseBean currentClasse = null;
		try {
			pstmt = connection.prepareStatement("Select id_elt, desc_elt from element where id_main_elt=? And id_telt='CLS'");
			pstmt.setString(1, eaId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentClasse = new ClasseBean();
				currentClasse.setId(rs.getString("id_elt"));
				currentClasse.setDesc(rs.getString("desc_elt"));
				result.add(currentClasse);
			}
		} catch (SQLException e) {
			mLog.error("Error during class retrieve", e);
			throw new MigrationException(e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}
	
	public boolean containPackages(String eaId) throws MigrationException {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("Select count(*) from element where id_main_elt=? And id_telt='PKG'");
			pstmt.setString(1, eaId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			mLog.error("Error during package presence verification", e);
			throw new MigrationException(e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}
	
	public List<ProjectBean> retrieveAllProject() throws MigrationException {
		List<ProjectBean> result = new ArrayList<ProjectBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ProjectBean currentProject = null;
		try {
			pstmt = connection.prepareStatement("Select id_pro, lib_pro from projet where id_pro <> 'ENTRYPOINT'");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentProject = new ProjectBean();
				currentProject.setId(rs.getString("id_pro"));
				currentProject.setDesc(rs.getString("lib_pro"));
				result.add(currentProject);
			}
		} catch (SQLException e) {
			mLog.error("Error retrieving all project", e);
			throw new MigrationException(e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}

	public ProjectBean retrieveProject(String projectId) throws MigrationException {
		ProjectBean result = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement("Select id_pro, lib_pro from projet where id_pro = ?");
			pstmt.setString(1, projectId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = new ProjectBean();
				result.setId(rs.getString("id_pro"));
				result.setDesc(rs.getString("lib_pro"));
			}
		} catch (SQLException e) {
			mLog.error("Error retrieving project: " + projectId, e);
			throw new MigrationException(e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}

	public List<EaBean> retrieveAllEa(String projectId) throws MigrationException {
		List<EaBean> result = new ArrayList<EaBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		EaBean currentEa = null;
		try {
			pstmt = connection.prepareStatement("Select id_elt, lib_elt from element where id_pro=? and id_telt='EA' and id_dialecte like 'java %'");
			pstmt.setString(1, projectId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				currentEa = new EaBean();
				currentEa.setId(rs.getString("id_elt"));
				currentEa.setDesc(rs.getString("lib_elt"));
				result.add(currentEa);
			}
		} catch (SQLException e) {
			mLog.error("Error retrieving ea for project: " + projectId, e);
			throw new MigrationException(e);
		}
		finally {
			JdbcDAOUtils.closePrepareStatement(pstmt);
			JdbcDAOUtils.closeResultSet(rs);
		}
		return result;
	}

	public void createPackages(String projectId, String eaId, Map<String, PackageBean> packageMap) throws MigrationException {
		if (packageMap != null) {
			PreparedStatement pstmt = null;
			try {
				pstmt = connection.prepareStatement("Insert into element (id_elt, lib_elt, desc_elt, id_pro, id_main_elt, id_telt) values (?, ?, ?, ?, ?, 'PKG')");
				connection.setAutoCommit(false);
				Collection<PackageBean> packageColl = packageMap.values();
				Iterator<PackageBean> packageIter = packageColl.iterator();
				PackageBean currentPackage = null; 
				while (packageIter.hasNext()) {
					currentPackage = packageIter.next();
					pstmt.setString(1, currentPackage.getId());
					pstmt.setString(2, getLib(currentPackage.getDesc()));
					pstmt.setString(3, currentPackage.getDesc());
					pstmt.setString(4, projectId);
					pstmt.setString(5, eaId);
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			} catch (SQLException e) {
				JdbcDAOUtils.rollbackConnection(connection);
				mLog.error("Error creating packages for project: " + projectId + " and ea:" + eaId, e);
				throw new MigrationException(projectId, e);
			}
			finally {
				JdbcDAOUtils.closePrepareStatement(pstmt);
			}
		}
	}
	
	private String getLib(String desc) {
		String result = desc;
		if (result.length() > 64) {
			result = "..." + result.substring(result.length() - 60);
		}
		return result;
	}
	
	public void linkPackages(String projectId, String eaId, Map<String, PackageBean> packageMap) throws MigrationException {
		if (packageMap != null) {
			PreparedStatement pstmt = null;
			try {
				pstmt = connection.prepareStatement("Insert into elt_links (elt_fils, elt_pere, type) values (?, ?, 'L')");
				connection.setAutoCommit(false);
				Collection<PackageBean> packageColl = packageMap.values();
				Iterator<PackageBean> packageIter = packageColl.iterator();
				PackageBean currentPackage = null; 
				while (packageIter.hasNext()) {
					currentPackage = packageIter.next();
					pstmt.setString(1, currentPackage.getId());
					if (currentPackage.getParent() != null) {
						pstmt.setString(2, currentPackage.getParent().getId());
					}
					else {
						pstmt.setString(2, eaId);
					}
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			} catch (SQLException e) {
				JdbcDAOUtils.rollbackConnection(connection);
				mLog.error("Error linking packages for project: " + projectId + " and ea:" + eaId, e);
				throw new MigrationException(projectId, e);
			}
			finally {
				JdbcDAOUtils.closePrepareStatement(pstmt);
			}
		}
	}
	
	public void reLinkClasses(String projectId, String eaId, List<ClasseBean> classList) throws MigrationException {
		if (classList != null) {
			PreparedStatement pstmt = null;
			try {
				pstmt = connection.prepareStatement("Insert into elt_links (elt_fils, elt_pere, type) values (?, ?, 'L')");
				connection.setAutoCommit(false);
				Iterator<ClasseBean> classIter = classList.iterator();
				ClasseBean currentClass = null; 
				while (classIter.hasNext()) {
					currentClass = classIter.next();
					if (currentClass.getParent() != null) {
						pstmt.setString(1, currentClass.getId());
						pstmt.setString(2, currentClass.getParent().getId());
						pstmt.addBatch();
					}
				}
				pstmt.executeBatch();
			} catch (SQLException e) {
				JdbcDAOUtils.rollbackConnection(connection);
				mLog.error("Error relinking classes for project: " + projectId + " and ea:" + eaId, e);
				throw new MigrationException(projectId, e);
			}
			finally {
				JdbcDAOUtils.closePrepareStatement(pstmt);
			}
		}
	}
	
	public void deleteLinkClassesEa(String projectId, String eaId, List<ClasseBean> classList) throws MigrationException {
		if (classList != null) {
			PreparedStatement pstmt = null;
			try {
				pstmt = connection.prepareStatement("delete from elt_links where elt_fils = ? and elt_pere = ?");
				connection.setAutoCommit(false);
				Iterator<ClasseBean> classIter = classList.iterator();
				ClasseBean currentClass = null; 
				while (classIter.hasNext()) {
					currentClass = classIter.next();
					if (currentClass.getParent() != null) {
						pstmt.setString(1, currentClass.getId());
						pstmt.setString(2, eaId);
						pstmt.addBatch();
					}
				}
				pstmt.executeBatch();
			} catch (SQLException e) {
				JdbcDAOUtils.rollbackConnection(connection);
				mLog.error("Error deleting class/ea links for project: " + projectId + " and ea:" + eaId, e);
				throw new MigrationException(projectId, e);
			}
			finally {
				JdbcDAOUtils.closePrepareStatement(pstmt);
			}
		}
	}
	
}

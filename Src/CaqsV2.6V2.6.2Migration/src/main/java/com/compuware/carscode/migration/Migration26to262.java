package com.compuware.carscode.migration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.compuware.carscode.migration.bean.ClasseBean;
import com.compuware.carscode.migration.bean.EaBean;
import com.compuware.carscode.migration.bean.PackageBean;
import com.compuware.carscode.migration.bean.ProjectBean;
import com.compuware.carscode.migration.dao.Migration26to262Dao;
import com.compuware.carscode.migration.exception.MigrationException;

public class Migration26to262 {

    //logger
	protected static final Logger mLog = com.compuware.util.logging.LoggerManager.getLogger("Caqs");

	Migration26to262Dao dao = null;
	
	public Migration26to262() {
		dao = new Migration26to262Dao();
	}
	
	public void migrate() throws MigrationException {
		try {
			List<ProjectBean> projectList = dao.retrieveAllProject();
			Iterator<ProjectBean> projectIter = projectList.iterator();
			while (projectIter.hasNext()) {
				migrate(projectIter.next());
			}
			dao.commitTransaction();
		}
		catch (MigrationException e) {
			dao.rollbackTransaction();
		}
	}
	
	public void migrate(String projectId) throws MigrationException {
		try {
			ProjectBean project = dao.retrieveProject(projectId);
			migrate(project);
			dao.commitTransaction();
		}
		catch (MigrationException e) {
			dao.rollbackTransaction();
		}
	}
	
	private void migrate(ProjectBean project) throws MigrationException {
		mLog.info("Starting migration for project: " + project.getDesc());
		List<EaBean> eaList = dao.retrieveAllEa(project.getId());
		Iterator<EaBean> eaListIter = eaList.iterator();
		while (eaListIter.hasNext()) {
			EaBean ea = eaListIter.next();
			if (!dao.containPackages(ea.getId())) {
				migrate(project, ea);
			}
			else {
				mLog.info("Ea " + ea.getDesc() + " already contains packages...");
			}
		}
	}
	
	private void migrate(ProjectBean project, EaBean ea) throws MigrationException {
		mLog.info("Starting migration for ea: " + ea.getDesc());
		List<ClasseBean> classList = dao.retrieveAllClasses(ea.getId());
		Iterator<ClasseBean> classListIter = classList.iterator();
		Map<String, PackageBean> packageMap = new HashMap<String, PackageBean>();
		ClasseBean currentClass = null;
		while (classListIter.hasNext()) {
			currentClass = classListIter.next();
			String packageDesc = currentClass.getPackageDesc();
			if (!packageMap.containsKey(packageDesc)) {
				extractPackageTree(packageDesc, packageMap);
			}
			currentClass.setParent(packageMap.get(packageDesc));
		}
		mLog.info("Creating packages: " + project.getDesc());
		dao.createPackages(project.getId(), ea.getId(), packageMap);
		mLog.info("Linking packages: " + project.getDesc());
		dao.linkPackages(project.getId(), ea.getId(), packageMap);
		mLog.info("Re-linking classes: " + project.getDesc());
		dao.reLinkClasses(project.getId(), ea.getId(), classList);
		mLog.info("Deleting EA/Classes links: " + project.getDesc());
		dao.deleteLinkClassesEa(project.getId(), ea.getId(), classList);
	}
	
	private PackageBean extractPackageTree(String packageDesc, Map<String, PackageBean> packageMap) {
		PackageBean result = null;
		if (packageDesc != null  && packageDesc.length() > 0) {
			String tmpPackageDesc = packageDesc;
			result = new PackageBean();
			result.setDesc(tmpPackageDesc);
			packageMap.put(tmpPackageDesc, result);
			int dotIndex = tmpPackageDesc.lastIndexOf('.');
			if (dotIndex > 0) {
				String parentDesc = tmpPackageDesc.substring(0, tmpPackageDesc.lastIndexOf('.'));
				PackageBean parent = packageMap.get(parentDesc);
				if (parent == null) {
					parent = extractPackageTree(parentDesc, packageMap);
				}
				if (parent != null) {
					result.setParent(parent);
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws SQLException, MigrationException {
		Migration26to262 mig = new Migration26to262();
		if (args.length > 0) {
			mig.migrate(args[0]);
		}
		else {
			mig.migrate();
		}
	}
	
}

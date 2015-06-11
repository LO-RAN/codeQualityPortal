package com.compuware.caqs.service;

import java.util.List;
import java.util.Map;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.UsersDao;
import com.compuware.caqs.domain.dataschemas.rights.RightBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.toolbox.util.logging.LoggerManager;

public class UserRightsSvc {
	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
	
	private static final UserRightsSvc instance = new UserRightsSvc();
	
	private UserRightsSvc() {
	}
	
	public static UserRightsSvc getInstance() {
		return UserRightsSvc.instance;
	}
	
	public List<RightBean> getAllAccessRights() {
		UsersDao dao = DaoFactory.getInstance().getUsersDao();
		return dao.getAllAccessRights();
	}
	
	public List<RoleBean> getAllCaqsRoles() {
		UsersDao dao = DaoFactory.getInstance().getUsersDao();
		return dao.getAllCaqsRoles();
	}
	
	public List<RoleBean> getAllCaqsRolesWhichCanAccess(RightBean rb) {
		UsersDao dao = DaoFactory.getInstance().getUsersDao();
		return dao.getAllCaqsRolesWhichCanAccess(rb);
	}
	
	public boolean isAssociated(RoleBean role, RightBean right) {
		UsersDao dao = DaoFactory.getInstance().getUsersDao();
		return dao.isAssociated(role, right);
	}
	
    public void saveRightsChange(String rightId, String roleId, boolean newValue) {
        UsersDao dao = DaoFactory.getInstance().getUsersDao();
		dao.saveRightsChange(rightId, roleId, newValue);
    }
}

/**
 * 
 */
package com.compuware.caqs.security.auth;

import java.util.Properties;

import com.compuware.caqs.util.CaqsConfigUtil;

/**
 * @author cwfr-fdubois
 *
 */
public final class GroupUtil {

    private static final String ADMINISTRATOR_KEY = "user.group.administator";
    private static final String QUALITY_MANAGER_KEY = "user.group.qualitymanager";
    private static final String ARCHITECT_KEY = "user.group.architect";
    private static final String MANAGER_KEY = "user.group.manager";
    private static final String PROJECT_MANAGER_KEY = "user.group.projectmanager";
    private static final String DEVELOPER_KEY = "user.group.developer";
    
    private final String administratorName;
    private final String qualityManagerName;
    private final String architectName;
    private final String managerName;
    private final String projectManagerName;
    private final String developerName;
    
    /** Singleton of this class. */
    private static final GroupUtil SINGLETON = new GroupUtil();
    
    /**
     * Initialize group names from the configuration file.
     *
     */
    private GroupUtil() {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        administratorName = dynProp.getProperty(ADMINISTRATOR_KEY);
        qualityManagerName = dynProp.getProperty(QUALITY_MANAGER_KEY);
        architectName = dynProp.getProperty(ARCHITECT_KEY);
        managerName = dynProp.getProperty(MANAGER_KEY);
        projectManagerName = dynProp.getProperty(PROJECT_MANAGER_KEY);
        developerName = dynProp.getProperty(DEVELOPER_KEY);
    }
    
    /**
     * Get the instance of this class.
     * @return the singleton of this class.
     */
    public static GroupUtil getInstance() {
    	return SINGLETON;
    }
    
    /**
     * Check if too groups are equals ignoring case.
     * @param group1 the first group.
     * @param group2 the second group.
     * @return true if the two groups are non null and equals ignoring case, false otherwise.
     */
    private boolean checkEqualsIgnoreCase(String group1, String group2) {
    	boolean result = false;
    	if (group1 != null && group2 != null) {
    		result = group1.equalsIgnoreCase(group2);
    	}
    	return result;
    }
    
    /**
     * Check if the given group is administrator.
     * @param groupName the given group.
     * @return true if the given group is administrator, false otherwise.
     */
    public boolean isAdministrator(String groupName) {
    	return checkEqualsIgnoreCase(administratorName, groupName);
    }
    
    /**
     * Check if the given group is quality manager.
     * @param groupName the given group.
     * @return true if the given group is quality manager, false otherwise.
     */
    public boolean isQualityManager(String groupName) {
    	return checkEqualsIgnoreCase(qualityManagerName, groupName);
    }
    
    /**
     * Check if the given group is architect.
     * @param groupName the given group.
     * @return true if the given group is architect, false otherwise.
     */
    public boolean isArchitect(String groupName) {
    	return checkEqualsIgnoreCase(architectName, groupName);
    }
    
    /**
     * Check if the given group is manager.
     * @param groupName the given group.
     * @return true if the given group is manager, false otherwise.
     */
    public boolean isManager(String groupName) {
    	return checkEqualsIgnoreCase(managerName, groupName);
    }
    
    /**
     * Check if the given group is project manager.
     * @param groupName the given group.
     * @return true if the given group is project manager, false otherwise.
     */
    public boolean isProjectManager(String groupName) {
    	return checkEqualsIgnoreCase(projectManagerName, groupName);
    }
    
    /**
     * Check if the given group is developer.
     * @param groupName the given group.
     * @return true if the given group is developer, false otherwise.
     */
    public boolean isDeveloper(String groupName) {
    	return checkEqualsIgnoreCase(developerName, groupName);
    }
    
}

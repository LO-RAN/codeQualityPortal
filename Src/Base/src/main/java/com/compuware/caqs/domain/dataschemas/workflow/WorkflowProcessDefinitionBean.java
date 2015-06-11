package com.compuware.caqs.domain.dataschemas.workflow;

/**
 * Define a wrokflow process including:
 * <ul>
 *   <li>package name</li>
 *   <li>process name</li>
 *   <li>process version</li>
 * </ul>
 * @author FredericD
 */
public class WorkflowProcessDefinitionBean {

    /**
     * The workflow package.
     */
    private String packageName;
    /**
     * The workflow process.
     */
    private String processName;
    /**
     * The process version.
     */
    private String processVersion;

    /**
     * The analysis http server context path.
     */
    private String httpServerContextPath;

    /**
     * The second analysis http server context path.
     */
    private String httpServerContextPath2;

    /**
     * The third analysis http server context path.
     */
    private String httpServerContextPath3;

    /**
     * Get the package name.
     * @return the package name.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Set the package name.
     * @param packageName the package name.
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Get the process name.
     * @return the process name.
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Set the process name.
     * @param processName the process name.
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }

    /**
     * Get the process version.
     * @return the process version.
     */
    public String getProcessVersion() {
        return processVersion;
    }

    /**
     * Set the process version.
     * @param processVersion the process version.
     */
    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    /**
     * Get the default http server context path.
     * @return the default http server context path.
     */
    public String getHttpServerContextPath() {
        return httpServerContextPath;
    }

    /**
     * Set the default http server context path.
     * @param httpServerContextPath the default http server context path.
     */
    public void setHttpServerContextPath(String path) {
        this.httpServerContextPath = path;
    }

    /**
     * Get the second http server context path.
     * @return the second http server context path.
     */
    public String getHttpServerContextPath2() {
        return httpServerContextPath2;
    }

    /**
     * Set the second http server context path.
     * @param httpServerContextPath the default http server context path.
     */
    public void setHttpServerContextPath2(String path) {
        this.httpServerContextPath2 = path;
    }

    /**
     * Get the third http server context path.
     * @return the second http server context path.
     */
    public String getHttpServerContextPath3() {
        return httpServerContextPath3;
    }

    /**
     * Set the third http server context path.
     * @param httpServerContextPath the default http server context path.
     */
    public void setHttpServerContextPath3(String path) {
        this.httpServerContextPath3 = path;
    }
}

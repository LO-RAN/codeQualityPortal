/**
 * 
 */
package com.compuware.carscode.plugin.devpartner.dao;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class DevPartnerDaoAbstract implements DevPartnerDao {

	protected String srcDir = "";
	
	public void setSourceDirectory(String dir) {
		if (dir != null) {
			this.srcDir = dir.replaceAll("\\\\", "/");
		}
	}
	
	protected String getRelativePath(String filePath) {
		String result = filePath.replaceAll("\\\\", "/");
		result = result.replace(this.srcDir, "");
		if (result.startsWith("/")) {
			result = result.substring(1);
		}
		return result;
	}
	
}

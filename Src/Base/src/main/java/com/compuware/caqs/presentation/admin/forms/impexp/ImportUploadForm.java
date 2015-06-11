package com.compuware.caqs.presentation.admin.forms.impexp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class ImportUploadForm extends ActionForm {
	
	private static final long serialVersionUID = -6790823808202125537L;
	
	private FormFile file;
	private String target;
	
	/**
	 * @return Returns the file.
	 */
	public FormFile getFile() {
		return file;
	}

	/**
	 * @param file The file to set.
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}

	/**
	 * @return Returns the target.
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target The target to set.
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	
	
}

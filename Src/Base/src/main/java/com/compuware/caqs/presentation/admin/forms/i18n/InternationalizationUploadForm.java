package com.compuware.caqs.presentation.admin.forms.i18n;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class InternationalizationUploadForm extends ActionForm {
	
	private static final long serialVersionUID = 3121166676549277589L;
	
	protected FormFile file;

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
}

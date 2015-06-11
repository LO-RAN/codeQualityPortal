package com.compuware.caqs.presentation.consult.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * The form the user fill to upload a new data file.
 * @author cwfr-fdubois
 */
public class UploadDataForm extends ActionForm {

    /** The serial version UID. */
	private static final long serialVersionUID = 3121166676549277589L;

    /** The uploaded file. */
	private FormFile file;

    /** The tool associated to the given file. */
	private String tool;

    /** The target element type. */
	private String elementType;

	/** The file type: CSV, XML, Other. */
    private String fileType;

    /** Should the upload create or delete elements? */
    private boolean master;

    /** The separator for CSV upload. Default is ; . */
    private String csvSeparator = ";";

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
	 * @return Returns the elementType.
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * @param elementType The elementType to set.
	 */
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	/**
	 * @return Returns the fileType.
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType The fileType to set.
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * @return Returns the tool.
	 */
	public String getTool() {
		return tool;
	}

	/**
	 * @param tool The tool to set.
	 */
	public void setTool(String tool) {
		this.tool = tool;
	}

	/**
	 * @return Returns the master.
	 */
	public boolean getMaster() {
		return master;
	}

	/**
	 * @param master The master to set.
	 */
	public void setMaster(boolean master) {
		this.master = master;
	}

    /**
     * Get the CSV separator.
     * @return the CSV separator.
     */
    public String getCsvSeparator() {
        return csvSeparator;
    }

	/**
     * Set the CSV separator value.
	 * @param csvSeparator The CSV separator to set.
	 */
    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

}

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

public class InternationalizationBean implements Serializable {

	private static final long serialVersionUID = -7824169082780864014L;
	
	private String tableName;
	private String columnName;
	private String tableId;
	private String languageId;	
	private String text;
	
	/**
	 * @return Returns the columnName.
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName The columnName to set.
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return Returns the languageId.
	 */
	public String getLanguageId() {
		return languageId;
	}
	/**
	 * @param languageId The languageId to set.
	 */
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	/**
	 * @return Returns the tableId.
	 */
	public String getTableId() {
		return tableId;
	}
	/**
	 * @param tableId The tableId to set.
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	/**
	 * @return Returns the tableName.
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}

package com.algebraweb.editor.shared.logicalplan;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * An evaluation context. Hold information on how to assemble the serialize
 * relation node as well as database configurations.
 * 
 * @author Patrick Brosi
 * 
 */
public class EvaluationContext implements IsSerializable {

	private String databaseServer;
	private int databasePort;
	private String databaseUser;
	private String database;
	private String databasePassword;
	private boolean iterUseColumn;
	private boolean sortUseColumn;
	private String iterColumnName;
	private int iterColumnNat;
	private String sortColumnName;
	private String sortOrder;
	private String sortOrderColumnOn;
	private String[] itemColumns = new String[0];
	private boolean databaseSetGlobal = false;

	public EvaluationContext() {

	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return the databasePassword
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * @return the databasePort
	 */
	public int getDatabasePort() {
		return databasePort;
	}

	/**
	 * @return the databaseServer
	 */
	public String getDatabaseServer() {
		return databaseServer;
	}

	/**
	 * @return the databaseUser
	 */
	public String getDatabaseUser() {
		return databaseUser;
	}

	/**
	 * @return the itemColumns
	 */
	public String[] getItemColumns() {
		return itemColumns;
	}

	/**
	 * @return the iterColumnName
	 */
	public String getIterColumnName() {
		return iterColumnName;
	}

	/**
	 * @return the iterColumnNat
	 */
	public int getIterColumnNat() {
		return iterColumnNat;
	}

	/**
	 * @return the sortColumnName
	 */
	public String getSortColumnName() {
		return sortColumnName;
	}

	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}

	/**
	 * @return the sortOrderColumnOn
	 */
	public String getSortOrderColumnOn() {
		return sortOrderColumnOn;
	}

	/**
	 * @return the databaseSetGlobal
	 */
	public boolean isDatabaseSetGlobal() {
		return databaseSetGlobal;
	}

	/**
	 * @return the iterUseColumn
	 */
	public boolean isIterUseColumn() {
		return iterUseColumn;
	}

	/**
	 * @return the sortUseColumn
	 */
	public boolean isSortUseColumn() {
		return sortUseColumn;
	}

	/**
	 * @param database
	 *            the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	/**
	 * @param databasePort
	 *            the databasePort to set
	 */
	public void setDatabasePort(int databasePort) {
		this.databasePort = databasePort;
	}

	/**
	 * @param databaseServer
	 *            the databaseServer to set
	 */
	public void setDatabaseServer(String databaseServer) {
		this.databaseServer = databaseServer;
	}

	/**
	 * @param databaseSetGlobal
	 *            the databaseSetGlobal to set
	 */
	public void setDatabaseSetGlobal(boolean databaseSetGlobal) {
		this.databaseSetGlobal = databaseSetGlobal;
	}

	/**
	 * @param databaseUser
	 *            the databaseUser to set
	 */
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	/**
	 * @param itemColumns
	 *            the itemColumns to set
	 */
	public void setItemColumns(String[] itemColumns) {
		this.itemColumns = itemColumns;
	}

	/**
	 * @param iterColumnName
	 *            the iterColumnName to set
	 */
	public void setIterColumnName(String iterColumnName) {
		this.iterColumnName = iterColumnName;
	}

	/**
	 * @param iterColumnNat
	 *            the iterColumnNat to set
	 */
	public void setIterColumnNat(int iterColumnNat) {
		this.iterColumnNat = iterColumnNat;
	}

	/**
	 * @param iterUseColumn
	 *            the iterUseColumn to set
	 */
	public void setIterUseColumn(boolean iterUseColumn) {
		this.iterUseColumn = iterUseColumn;
	}

	/**
	 * @param sortColumnName
	 *            the sortColumnName to set
	 */
	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	/**
	 * @param sortOrder
	 *            the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @param sortOrderColumnOn
	 *            the sortOrderColumnOn to set
	 */
	public void setSortOrderColumnOn(String sortOrderColumnOn) {
		this.sortOrderColumnOn = sortOrderColumnOn;
	}

	/**
	 * @param sortUseColumn
	 *            the sortUseColumn to set
	 */
	public void setSortUseColumn(boolean sortUseColumn) {
		this.sortUseColumn = sortUseColumn;
	}

}

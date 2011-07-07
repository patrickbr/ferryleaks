package com.algebraweb.editor.client.logicalcanvas;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EvaluationContext implements IsSerializable {
	
	
	String databaseServer;
	int databasePort;
	String databaseUser;
	String database;
	String databasePassword;
	
	boolean iterUseColumn;
	boolean sortUseColumn;
	
	String iterColumnName;
	int iterColumnNat;
	String sortColumnName;
	String sortOrder;
	
	
	public EvaluationContext() {
		
	}


	/**
	 * @return the databaseServer
	 */
	public String getDatabaseServer() {
		return databaseServer;
	}


	/**
	 * @param databaseServer the databaseServer to set
	 */
	public void setDatabaseServer(String databaseServer) {
		this.databaseServer = databaseServer;
	}


	/**
	 * @return the databasePort
	 */
	public int getDatabasePort() {
		return databasePort;
	}


	/**
	 * @param databasePort the databasePort to set
	 */
	public void setDatabasePort(int databasePort) {
		this.databasePort = databasePort;
	}


	/**
	 * @return the databaseUser
	 */
	public String getDatabaseUser() {
		return databaseUser;
	}


	/**
	 * @param databaseUser the databaseUser to set
	 */
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}


	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}


	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}


	/**
	 * @return the databasePassword
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}


	/**
	 * @param databasePassword the databasePassword to set
	 */
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}


	/**
	 * @return the iterUseColumn
	 */
	public boolean isIterUseColumn() {
		return iterUseColumn;
	}


	/**
	 * @param iterUseColumn the iterUseColumn to set
	 */
	public void setIterUseColumn(boolean iterUseColumn) {
		this.iterUseColumn = iterUseColumn;
	}


	/**
	 * @return the sortUseColumn
	 */
	public boolean isSortUseColumn() {
		return sortUseColumn;
	}


	/**
	 * @param sortUseColumn the sortUseColumn to set
	 */
	public void setSortUseColumn(boolean sortUseColumn) {
		this.sortUseColumn = sortUseColumn;
	}


	/**
	 * @return the iterColumnName
	 */
	public String getIterColumnName() {
		return iterColumnName;
	}


	/**
	 * @param iterColumnName the iterColumnName to set
	 */
	public void setIterColumnName(String iterColumnName) {
		this.iterColumnName = iterColumnName;
	}


	/**
	 * @return the iterColumnNat
	 */
	public int getIterColumnNat() {
		return iterColumnNat;
	}


	/**
	 * @param iterColumnNat the iterColumnNat to set
	 */
	public void setIterColumnNat(int iterColumnNat) {
		this.iterColumnNat = iterColumnNat;
	}


	/**
	 * @return the sortColumnName
	 */
	public String getSortColumnName() {
		return sortColumnName;
	}


	/**
	 * @param sortColumnName the sortColumnName to set
	 */
	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}


	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}


	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
	

}

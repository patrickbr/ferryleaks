package com.algebraweb.editor.shared.logicalplan;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**}
 * An evaluation context. Hold information on how to assemble the serialize
 * relation node as well as database configurations.
 * 
 * @author Patrick Brosi
 * 
 */
public class EvaluationContext implements IsSerializable,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean iterUseColumn;
	private boolean sortUseColumn;
	private String iterColumnName;
	private int iterColumnNat;
	private String sortColumnName;
	private String sortOrder;
	private String sortOrderColumnOn;
	private String[] itemColumns = new String[0];

	public EvaluationContext() {

	}
	
	public EvaluationContext copy() {
		
		EvaluationContext copy = new EvaluationContext();
		
		copy.itemColumns = this.itemColumns;
		copy.iterColumnName = this.iterColumnName;
		copy.iterColumnNat = this.iterColumnNat;
		copy.iterUseColumn = this.iterUseColumn;
		copy.sortUseColumn = this.sortUseColumn;
		copy.sortColumnName = this.sortColumnName;
		copy.sortOrder = this.sortOrder;
		copy.sortOrderColumnOn = this.sortOrderColumnOn;
		
		return copy;
		
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

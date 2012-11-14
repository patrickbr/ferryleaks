package com.algebraweb.editor.client.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Holds various ValidationErros for a single plan
 * 
 * @author Patrick Brosi
 * 
 */
public class ValidationResult implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -231083329224580609L;
	private List<ValidationError> errors = new ArrayList<ValidationError>();
	private int planid = -1;

	public ValidationResult() {
	}

	public ValidationResult(int planid) {
		this.planid = planid;
	}

	/**
	 * Adds an ValidationError to this ValidationResult
	 * 
	 * @param e
	 *            the error to add
	 */
	public void addError(ValidationError e) {
		this.errors.add(e);
	}

	/**
	 * Returns a list of all errors
	 * 
	 * @return the list of errors
	 */
	public List<ValidationError> getErrors() {
		return errors;
	}

	/**
	 * Returns the plan id this ValidationResult is for
	 * 
	 * @return the plan id
	 */
	public int getPlanid() {
		return planid;
	}

	/**
	 * Returns true if this ValidationResult holds any errors
	 * 
	 * @return true if errors exist
	 */
	public boolean hasErrors() {
		return errors.size() > 0;
	}

	/**
	 * Sets the plan id this ValidationResult is for
	 * 
	 * @param planid
	 *            the plan id to set
	 */
	public void setPlanid(int planid) {
		this.planid = planid;
	}
}

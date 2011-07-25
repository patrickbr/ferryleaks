package com.algebraweb.editor.client.validation;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;



public class ValidationResult implements IsSerializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -231083329224580609L;

	private ArrayList<ValidationError> errors = new ArrayList<ValidationError>();
	
	private int planid=-1;
	
	public ValidationResult() {
		
		
	}
	
	public ValidationResult(int planid) {
		
		this.planid=planid;
		
	}

	public void addError(ValidationError e){
		this.errors.add(e);
	}

	public ArrayList<ValidationError> getErrors() {
		return errors;
	}

	public int getPlanid() {
		return planid;
	}

	public boolean hasErrors() {
		
		return errors.size() > 0;
		
	}
	
	public void setPlanid(int planid) {
		this.planid = planid;
	}
	

}

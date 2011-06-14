package com.algebraweb.editor.client.validation;

import java.io.Serializable;
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

	public ArrayList<ValidationError> getErrors() {
		return errors;
	}

	public void addError(ValidationError e){
		this.errors.add(e);
	}

	public int getPlanid() {
		return planid;
	}

	public void setPlanid(int planid) {
		this.planid = planid;
	}
	
	public boolean hasErrors() {
		
		return errors.size() > 0;
		
	}
	

}

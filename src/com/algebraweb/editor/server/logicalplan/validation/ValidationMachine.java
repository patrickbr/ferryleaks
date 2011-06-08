package com.algebraweb.editor.server.logicalplan.validation;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.QueryPlan;

public class ValidationMachine {

	
	
	private ArrayList<Validator> validators = new ArrayList<Validator>();
	
	
	public ValidationMachine() {
		
	}
	
	//TODO: make this an ValidationList
	
	public ArrayList<ValidationError> validate(PlanNode p) {
		
		ArrayList<ValidationError> ret = new ArrayList<ValidationError>();
		
		Iterator<Validator> it= validators.iterator();
		
		while (it.hasNext()) {
			
			
			it.next().validate(p);
			
			
		}
		
		
		return ret;
		
		
	}
	
	
	public void addValidator(Validator v) {
				
		this.validators.add(v);
		
	}
	
	
	
	
	
	
	
	
	
}

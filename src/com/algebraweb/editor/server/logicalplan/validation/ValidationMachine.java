package com.algebraweb.editor.server.logicalplan.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;

public class ValidationMachine {
	private ArrayList<Validator> validators = new ArrayList<Validator>();


	public ValidationMachine() {

	}

	public void addValidator(Validator v) {
		this.validators.add(v);
	}

	public ValidationResult validate(QueryPlan context,List<PlanNode> list) throws PlanHasCycleException {
		ValidationResult ret = new ValidationResult(context.getId());
		Iterator<Validator> it= validators.iterator();

		while (it.hasNext()) {
			it.next().validate(list,context.getPlan(),ret);
		}		
		return ret;
	}

	public List<ValidationError> validate(QueryPlan context,PlanNode p) throws PlanHasCycleException {

		List<PlanNode> ps = new ArrayList<PlanNode>();
		ps.add(p);

		ValidationResult ret = new ValidationResult(-1);
		Iterator<Validator> it= validators.iterator();
		while (it.hasNext()) {
			it.next().validate(ps,context.getPlan(),ret);
		}		
		return ret.getErrors();
	}
}

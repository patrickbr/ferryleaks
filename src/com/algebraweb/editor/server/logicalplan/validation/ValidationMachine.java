package com.algebraweb.editor.server.logicalplan.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.QueryPlan;

/**
 * The validation machine accepts validator registrations and 
 * processes them if called
 * @author Patrick Brosi
 *
 */
public class ValidationMachine {
	private ArrayList<Validator> validators = new ArrayList<Validator>();

	public ValidationMachine() {

	}

	public void addValidator(Validator v) {
		this.validators.add(v);
	}

	/**
	 * Validate a given list of plan nodes with a surrounding context QueryPlan
	 * @param context the surrounding query plan
	 * @param list the list of nodes to validate
	 * @return a filled ValidationResult object
	 * @throws PlanHasCycleException
	 */
	public ValidationResult validate(QueryPlan context,List<PlanNode> list) throws PlanHasCycleException {
		ValidationResult ret = new ValidationResult(context.getId());
		Iterator<Validator> it= validators.iterator();

		while (it.hasNext()) {
			it.next().validate(list,context.getPlan(),ret);
		}		
		return ret;
	}

	/**
	 * Validate a single plan node with a surrounding context QueryPlan
	 * @param context
	 * @param p the plan node to validate
	 * @return
	 * @throws PlanHasCycleException
	 */
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

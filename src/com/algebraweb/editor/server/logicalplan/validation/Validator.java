package com.algebraweb.editor.server.logicalplan.validation;

import java.util.ArrayList;

import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.validation.ValidationResult;

public interface Validator {
	
	
	
	public void validate(ArrayList<PlanNode> ps,ArrayList<PlanNode> plan, ValidationResult r);

	

}

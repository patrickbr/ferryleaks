package com.algebraweb.editor.server.logicalplan.validation;

import java.util.ArrayList;

import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.ContentNode;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.QueryPlan;

public interface Validator {
	
	
	
	public void validate(ArrayList<PlanNode> ps,ArrayList<PlanNode> plan, ValidationResult r);

	

}

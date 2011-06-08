package com.algebraweb.editor.server.logicalplan.validation;

import com.algebraweb.editor.server.logicalplan.ContentNode;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.QueryPlan;

public interface Validator {
	
	
	
	public ValidationResult validate(PlanNode p);
	
	

}

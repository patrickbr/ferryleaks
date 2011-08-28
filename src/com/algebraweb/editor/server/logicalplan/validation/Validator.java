package com.algebraweb.editor.server.logicalplan.validation;

import java.util.List;

import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.shared.node.PlanNode;

public interface Validator {
	public void validate(List<PlanNode> ps,List<PlanNode> plan, ValidationResult r) throws PlanHasCycleException;
}

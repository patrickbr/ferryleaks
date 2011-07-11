package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;

public class AbandondedNodeValidator implements Validator {

	@Override
	public void validate(ArrayList<PlanNode> ps, ArrayList<PlanNode> plan, ValidationResult r) {


		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {

			PlanNode current = it.next();

			if (current != null) {

				boolean isChild = false;

				Iterator<PlanNode> itt = plan.iterator();

				while (itt.hasNext()) {


					if (itt.next().getChilds().contains(current)) isChild=true;


				}

				if (!isChild && current.getChilds().size() == 0) r.addError(new ValidationError(current.getId(), "Node is abandonded. It has neither parents no childs."));

			}
		}



	}

}

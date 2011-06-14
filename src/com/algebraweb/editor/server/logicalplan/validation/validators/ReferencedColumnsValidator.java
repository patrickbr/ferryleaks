package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.ContentVal;
import com.algebraweb.editor.server.logicalplan.NodeContent;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.Property;
import com.algebraweb.editor.server.logicalplan.PropertyValue;
import com.algebraweb.editor.server.logicalplan.QueryPlan;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.ibm.icu.impl.PropsVectors;

public class ReferencedColumnsValidator implements Validator {

	@Override
	public void validate(ArrayList<PlanNode> ps, ArrayList<PlanNode> plan, ValidationResult r) {


		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {

			PlanNode current = it.next();

			ArrayList<Property> refedCols = current.getReferencedColumns();

			Iterator<Property> refIt = refedCols.iterator();

			while (refIt.hasNext()) {


				Property currentCol = refIt.next();

				if (!(containsPropertyByVal(currentCol,current.getReferencableColumnsWithoutAdded()))) {

					r.addError(new ValidationError(current.getId(),"Node referers to non existing column '" + currentCol.getPropertyVal().getVal() + "'"));
					System.out.println("Node #" + current.getId() + " referers to non existing column '" + currentCol.getPropertyVal().getVal() + "'");

				}


			}

		}

	}


	private boolean containsPropertyByVal(Property p, ArrayList<Property> props) {


		Iterator<Property> it = props.iterator();

		while (it.hasNext()) {

			if (p.getPropertyVal().getVal().equals(it.next().getPropertyVal().getVal())) return true;


		}


		return false;



	}


}





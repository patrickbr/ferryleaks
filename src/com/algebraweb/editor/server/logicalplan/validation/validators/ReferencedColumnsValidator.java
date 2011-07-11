package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;

import java.util.Iterator;


import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;

import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;


public class ReferencedColumnsValidator implements Validator {

	@Override
	public void validate(ArrayList<PlanNode> ps, ArrayList<PlanNode> plan, ValidationResult r) {


		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {

			PlanNode current = it.next();

			if (current == null) {

				ArrayList<Property> refedCols = current.getReferencedColumns();

				Iterator<Property> refIt = refedCols.iterator();

				while (refIt.hasNext()) {


					Property currentCol = refIt.next();

					if (!(containsPropertyByVal(currentCol,current.getReferencableColumnsWithoutAdded()))) {

						String errorMsg = "Node referers to non existing column <span class='tt'>" + currentCol.getPropertyVal().getVal() + "</span>";

						if (currentCol.getPropertyVal().getType().matches("__COLUMN\\{[0-9]*\\}")) {

							int num = Integer.parseInt(currentCol.getPropertyVal().getType().split("\\{")[1].replaceAll("\\}", ""));

							if (current.getChilds().size() > num-1) {
								errorMsg += " expected in node <span class='tt'>#" + current.getChilds().get(num-1).getId() + "</span> of type <span class='tt'>" + current.getChilds().get(num-1).getKind()+ "</span>";
							}else{
								errorMsg += " expected in child node <span class='tt'>#" + num + "</span>";
							}

						}


						r.addError(new ValidationError(current.getId(),errorMsg));

					}
				}


			}

			ArrayList<Property> introCols = current.getAddedColumns();
			Iterator<Property> itIntroCols = introCols.iterator();

			while (itIntroCols.hasNext()) {

				Property currentCol = itIntroCols.next();

				if ((!current.resetsColumns() && containsPropertyByVal(currentCol,current.getReferencableColumnsWithoutAdded()))) {

					String errorMsg = "Node introduces already existing column <span class='tt'>" + currentCol.getPropertyVal().getVal() + "</span>";
					r.addError(new ValidationError(current.getId(),errorMsg));
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





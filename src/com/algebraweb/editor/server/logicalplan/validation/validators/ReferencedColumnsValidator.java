package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.Property;

/**
 * A validator checking the referenced columns
 * @author Patrick Brosi
 *
 */
public class ReferencedColumnsValidator implements Validator {

	private boolean containsPropertyByVal(Property p, List<Property> list) {
		Iterator<Property> it = list.iterator();

		while (it.hasNext()) {
			if (p.getPropertyVal().getVal().equals(it.next().getPropertyVal().getVal())) return true;
		}
		return false;
	}

	@Override
	public void validate(List<PlanNode> ps, List<PlanNode> plan, ValidationResult r) throws PlanHasCycleException {
		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {
			PlanNode current = it.next();
			if (current != null) {
				List<Property> refedCols = current.getReferencedColumns();
				Iterator<Property> refIt = refedCols.iterator();

				while (refIt.hasNext()) {
					Property currentCol = refIt.next();

					if (currentCol.getPropertyVal().getType().matches("__COLUMN\\{[0-9]*\\}")) {
						int num = Integer.parseInt(currentCol.getPropertyVal().getType().split("\\{")[1].replaceAll("\\}", ""));
						if (!(containsPropertyByVal(currentCol,current.getReferencableColumnsWithoutAdded(num)))) {
							String errorMsg = "Node referers to non existing column <span class='tt'>" + currentCol.getPropertyVal().getVal() + "</span>";
							if (current.getChilds().size() > num-1 && current.getChilds().get(num-1) != null) {
								errorMsg += " expected in node <span class='tt'>#" + current.getChilds().get(num-1).getId() + "</span> of type <span class='tt'>" + current.getChilds().get(num-1).getKind()+ "</span>";
							}else{
								errorMsg += " expected in child node <span class='tt'>#" + num + "</span>";
							}
							r.addError(new ValidationError(current.getId(),errorMsg));
						}
					}else

						if (!(containsPropertyByVal(currentCol,current.getReferencableColumnsWithoutAdded()))) {
							String errorMsg = "Node referers to non existing column <span class='tt'>" + currentCol.getPropertyVal().getVal() + "</span>";
							r.addError(new ValidationError(current.getId(),errorMsg));
						}
				}
			}

			List<Property> introCols = current.getAddedColumns();
			Iterator<Property> itIntroCols = introCols.iterator();

			while (itIntroCols.hasNext()) {
				Property currentCol = itIntroCols.next();
				if ((!current.resetsColumns() && !containsPropertyByVal(currentCol,current.getRemovedColumns()) && containsPropertyByVal(currentCol,current.getReferencableColumnsWithoutAdded()))) {
					String errorMsg = "Node introduces already existing column <span class='tt'>" + currentCol.getPropertyVal().getVal() + "</span>";
					r.addError(new ValidationError(current.getId(),errorMsg));
				}
			}
		}
	}
}





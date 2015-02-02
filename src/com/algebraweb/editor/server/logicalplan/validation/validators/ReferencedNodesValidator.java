package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PlanNode;

/**
 * A validator checking for referenced nodes
 * 
 * @author Patrick Brosi
 * 
 */
public class ReferencedNodesValidator implements Validator {

	private boolean hasChildWithId(PlanNode n, int id) {
		Iterator<PlanNode> it = n.getChilds().iterator();

		while (it.hasNext()) {
			PlanNode current = it.next();
			if (current != null && current.getId() == id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void validate(List<PlanNode> ps, List<PlanNode> plan,
			ValidationResult r) {
		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {
			PlanNode current = it.next();
			if (current != null) {
				Iterator<NodeContent> edges = current
						.getDirectContentWithInternalName("edge").iterator();

				while (edges.hasNext()) {
					int curId = Integer.parseInt(edges.next().getAttributes()
							.get("to").getVal());
					if (!hasChildWithId(current, curId) && curId > -1) {
						String errorMsg = "Node wishes to be the loving mother of node #"
								+ curId
								+ ", but the child couldn't be found. Maybe you referred to a node introduced after this?";
						r.addError(new ValidationError(current.getId(),
								errorMsg));
					}
				}
			}
		}
	}
}
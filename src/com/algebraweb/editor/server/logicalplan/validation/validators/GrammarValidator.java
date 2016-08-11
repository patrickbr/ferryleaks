package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.scheme.Field;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.algebraweb.editor.shared.scheme.NodeScheme;
import com.algebraweb.editor.shared.scheme.Value;

/**
 * A validator for the grammar specified in the *.scheme.xml files.
 *
 * @author Patrick Brosi
 *
 */

public class GrammarValidator implements Validator {
	private String currentSchema = "";
	private int currentNodeValidaded = -1;

	public void fillContentNodeWithContentValidationResults(ContentNode n,
			List<GoAble> schema) {
		Iterator<GoAble> it = schema.iterator();
		while (it.hasNext()) {
			GoAble cur = it.next();
			Iterator<NodeContent> i = n.getDirectNodeContentByScheme(cur)
					.iterator();

			while (i.hasNext()) {
				NodeContent curr = i.next();
				curr.setEvalRes(validateContentNode(curr, cur, true));
			}
		}
	}

	private String getErrorMsg(String howMany, GoAble g) {
		String ret = "Expected " + howMany
				+ " element(s) of type <span class='tt'>" + g.getXmlObject()
				+ "</span>";
		if (g instanceof Value) {
			ret += " with fields";
			Iterator<Field> it = ((Value) g).getFields().iterator();

			while (it.hasNext()) {
				Field current = it.next();
				ret += " <span class='tt'>" + current.getName();
				if (current.getMust_be() != null) {
					ret += " = " + current.getMust_be();
				}
				ret += "</span>,";
			}
			if (ret.endsWith(",")) {
				ret = ret.substring(0, ret.length() - 1);
			}
		}

		ret += " for node type <span class='tt'>" + currentSchema + "</span>";
		return ret;
	}

	private boolean isInteger(String a) {
		return a.matches("[0-9]+");
	}

	@Override
	public void validate(List<PlanNode> ps, List<PlanNode> plan,
			ValidationResult r) {
		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {
			PlanNode current = it.next();
			if (current != null) {
				r.getErrors().addAll(validateNode(current));
			}
		}
	}

	public List<ValidationError> validateContentNode(ContentNode n, GoAble g) {
		return validateContentNode(n, g, false);
	}

	public List<ValidationError> validateContentNode(ContentNode n, GoAble g,
			boolean stayFlat) {
		List<ValidationError> res = new ArrayList<ValidationError>();
		if (g instanceof Value && n instanceof NodeContent) {
			Iterator<Field> it = ((Value) g).getFields().iterator();

			while (it.hasNext()) {
				Field current = it.next();
				String val = ((NodeContent) n).getAttributes().get(
						current.getName()).getVal();
				if (current.hasCanBe()
						&& !Arrays.asList(current.getCanBe()).contains(val)) {
					String retMsg = "Attribute " + current.getName()
							+ " is expected to be one of {";
					for (String i : current.getCanBe()) {
						retMsg += i + ", ";
					}
					retMsg += "}";
					retMsg.replaceAll(", \\}", "}");
					res.add(new ValidationError(currentNodeValidaded, retMsg));
				}
			}
		}
		Iterator<GoAble> it = g.getSchema().iterator();

		while (it.hasNext()) {
			GoAble current = it.next();
			String howOften = current.getHowOften();
			if (isInteger(howOften)) {
				if (n.getDirectNodeContentByScheme(current).size() < Integer
						.parseInt(howOften)) {
					res.add(new ValidationError(currentNodeValidaded,
							getErrorMsg("a number of " + howOften, current)));
				}
			}
			if (howOften.equals("?")) {
				if (n.getDirectNodeContentByScheme(current).size() > 1) {
					res.add(new ValidationError(currentNodeValidaded,
							getErrorMsg("one or no", current)));
				}
			}
			if (howOften.equals("+")) {
				if (n.getDirectNodeContentByScheme(current).size() < 1) {
					res.add(new ValidationError(currentNodeValidaded,
							getErrorMsg("at least one", current)));
				}
			}
			if (howOften.equals("*")) {
			}

			if (howOften.matches("\\{[0-9]+,[0-9]+\\}")) {
				int min = Integer.parseInt(howOften.split(",")[0].replaceAll(
						"\\{", ""));
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll(
						"\\}", ""));
				int size = n.getDirectNodeContentByScheme(current).size();

				if (size < min || size > max) {
					res.add(new ValidationError(currentNodeValidaded,
							getErrorMsg("a maximum of " + max
									+ "and a minum of " + min, current)));
				}
			}

			if (howOften.matches("\\{,[0-9]+\\}")) {
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll(
						"\\}", ""));
				int size = n.getDirectNodeContentByScheme(current).size();

				if (size > max) {
					res.add(new ValidationError(currentNodeValidaded,
							getErrorMsg("a maximum of " + max, current)));
				}
			}

			if (howOften.matches("\\{[0-9]+,\\}")) {
				int min = Integer.parseInt(howOften.split(",")[0].replaceAll(
						"\\{", ""));
				int size = n.getDirectNodeContentByScheme(current).size();

				if (size < min) {
					res.add(new ValidationError(currentNodeValidaded,
							getErrorMsg("a minimun of " + min, current)));
				}
			}

			if (!stayFlat) {
				Iterator<NodeContent> itt = n.getDirectNodeContentByScheme(
						current).iterator();

				while (itt.hasNext()) {
					res.addAll(validateContentNode(itt.next(), current));
				}
			}
		}
		return res;
	}

	public List<ValidationError> validateNode(PlanNode n) {
		List<ValidationError> res = new ArrayList<ValidationError>();
		NodeScheme nodeScheme = n.getScheme();
		currentNodeValidaded = n.getId();
		currentSchema = n.getKind();
		res.addAll(validateContentNode(n, nodeScheme));
		return res;
	}
}
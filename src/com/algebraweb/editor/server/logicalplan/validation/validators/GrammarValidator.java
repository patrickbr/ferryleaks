package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;

/**
 * A validator for the grammar specified in the *.scheme.xml files.
 * 
 * @author Patrick Brosi
 *
 */

public class GrammarValidator implements Validator {


	private String currentSchema = "";
	private int currentNodeValidaded = -1;

	private String getErrorMsg(String howMany, GoAble g) {

		String ret = "Expected " + howMany + " element(s) of type <span class='tt'>" + g.getXmlObject() + "</span>";

		if (g instanceof Value) {

			ret += " with fields";

			Iterator<Field> it =  ((Value)g).getFields().iterator();

			while (it.hasNext()) {

				Field current = it.next();
				ret += " <span class='tt'>" + current.getVal();

				if (current.getMust_be() != null) ret += " = " + current.getMust_be();

				ret += "</span>,";
			}

			if (ret.endsWith(",")) ret = ret.substring(0, ret.length()-1);

		}

		ret += " for node type <span class='tt'>" + currentSchema + "</span>";

		return ret;
	}

	public ArrayList<ValidationError> validateNode(PlanNode n) {

		ArrayList<ValidationError> res = new  ArrayList<ValidationError>();

		NodeScheme nodeScheme = n.getScheme();

		currentNodeValidaded = n.getId();
		currentSchema = n.getKind();

		res.addAll(validateContentNode(n,nodeScheme.getSchema()));
		return res;

	}

	public ArrayList<ValidationError> validateContentNode(ContentNode n, ArrayList<GoAble> g) {

		return validateContentNode(n, g, false);


	}


	public ArrayList<ValidationError> validateContentNode(ContentNode n, ArrayList<GoAble> g, boolean stayFlat) {

		ArrayList<ValidationError> res = new ArrayList<ValidationError>();

		Iterator<GoAble> it = g.iterator();

		while (it.hasNext()) {

			GoAble current = it.next();


			String howOften = current.getHowOften();

			if (isInteger(howOften)) {

				if (n.getDirectNodeContentByScheme(current).size() < Integer.parseInt(howOften)) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a number of " + howOften ,current)));
				}

			}

			if (howOften.equals("?")) {


				if (n.getDirectNodeContentByScheme(current).size() >1) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("one or no",current)));
				}


			}

			if (howOften.equals("+")) {

				if (n.getDirectNodeContentByScheme(current).size() <1) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("at least one",current)));
				}


			}

			if (howOften.equals("*")) {


			}

			if (howOften.matches("\\{[0-9]+,[0-9]+\\}")) {

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				int size = n.getDirectNodeContentByScheme(current).size();

				if (size <min || size > max) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a maximum of " + max + "and a minum of " + min,current)));
				}

			}

			if (howOften.matches("\\{,[0-9]+\\}")) {

				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				int size =n. getDirectNodeContentByScheme(current).size();

				if (size > max) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a maximum of " + max,current)));
				}

			}


			if (howOften.matches("\\{[0-9]+,\\}")) {

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));

				int size = n.getDirectNodeContentByScheme(current).size();

				if (size < min) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a minimun of " + min,current)));
				}

			}

			if (!stayFlat) {

				Iterator<NodeContent> itt = n.getDirectNodeContentByScheme(current).iterator();

				while (itt.hasNext()) {

					res.addAll(validateContentNode(itt.next(),current.getSchema()));

				}
			}


		}

		return res;


	}





	private boolean isInteger(String a) {

		return (a.matches("[0-9]+"));

	}

	@Override
	public void validate(ArrayList<PlanNode> ps, ArrayList<PlanNode> plan,
			ValidationResult r) {

		Iterator<PlanNode> it = ps.iterator();

		while (it.hasNext()) {

			r.getErrors().addAll(validateNode(it.next()));

		}


	}
}

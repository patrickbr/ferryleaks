package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.ContentNode;
import com.algebraweb.editor.server.logicalplan.ContentVal;
import com.algebraweb.editor.server.logicalplan.NodeContent;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Field;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.GoAble;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeScheme;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Value;

public class GrammarValidator implements Validator {



	private String currentSchema = "";
	private int currentNodeValidaded = -1;


	private String getErrorMsg(String howMany, GoAble g) {

		String ret = "Expected " + howMany + " element(s) of type '" + g.getXmlObject() + "'";

		if (g instanceof Value) {

			ret += " with fields";

			Iterator<Field> it =  ((Value)g).getFields().iterator();

			while (it.hasNext()) {

				Field current = it.next();
				ret += " " + current.getVal();

				if (current.getMust_be() != null) ret += " = " + current.getMust_be();

				ret += ",";
			}

			if (ret.endsWith(",")) ret = ret.substring(0, ret.length()-1);

		}

		ret += " for node type '" + currentSchema + "'";

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

	private ArrayList<ValidationError> validateContentNode(ContentNode n, ArrayList<GoAble> g) {

		ArrayList<ValidationError> res = new ArrayList<ValidationError>();

		Iterator<GoAble> it = g.iterator();

		while (it.hasNext()) {

			GoAble current = it.next();


			String howOften = current.getHowOften();

			if (isInteger(howOften)) {

				if (getNodeContentByScheme(current,n).size() < Integer.parseInt(howOften)) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a number of " + howOften ,current)));
				}

			}

			if (howOften.equals("?")) {


				if (getNodeContentByScheme(current,n).size() >1) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("one or no",current)));
				}


			}

			if (howOften.equals("+")) {

				if (getNodeContentByScheme(current,n).size() <1) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("at least one",current)));
				}


			}

			if (howOften.equals("*")) {


			}

			if (howOften.matches("\\{[0-9]+,[0-9]+\\}")) {

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				int size = getNodeContentByScheme(current,n).size();

				if (size <min || size > max) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a maximum of " + max + "and a minum of " + min,current)));
				}

			}

			if (howOften.matches("\\{,[0-9]+\\}")) {

				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				int size = getNodeContentByScheme(current,n).size();

				if (size > max) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a maximum of " + max,current)));
				}

			}


			if (howOften.matches("\\{[0-9]+,\\}")) {

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));

				int size = getNodeContentByScheme(current,n).size();

				if (size < min) {
					res.add(new ValidationError(currentNodeValidaded, 
							getErrorMsg("a minimun of " + min,current)));
				}

			}
			
			Iterator<NodeContent> itt = getNodeContentByScheme(current,n).iterator();

			while (itt.hasNext()) {

				res.addAll(validateContentNode(itt.next(),current.getSchema()));

			}


		}

		return res;


	}




	/**
	 * Gets all flat contents of a parent ContentNode fitting the given GoAble-schema g
	 * @param g
	 * @param c
	 * @return
	 */

	private ArrayList<NodeContent> getNodeContentByScheme(GoAble g,ContentNode parent) {

		ArrayList<NodeContent> res = new ArrayList<NodeContent>();


		Iterator<NodeContent> it = parent.getAllContentWithInternalName(g.getXmlObject()).iterator();

		while(it.hasNext()) {

			NodeContent node = it.next();

			if (g instanceof Value) {

				boolean fail = false;

				if (!(node instanceof ContentVal)) {
					fail=true;
				}else{

					ContentVal nodeVal = (ContentVal) node;

					ArrayList<Field> fields = ((Value)g).getFields();
					Iterator<Field> i = fields.iterator();


					while (i.hasNext()) {

						Field current = i.next();
						String att = current.getVal();

						if ((!nodeVal.getAttributes().containsKey(att) ||
								(current.hasMustBe() && !current.getMust_be().equals(nodeVal.getAttributes().get(att).getVal())))){

							fail=true;

						}					
					}
				}

				if (!fail) res.add(node);

			} else {
				res.add(node);
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

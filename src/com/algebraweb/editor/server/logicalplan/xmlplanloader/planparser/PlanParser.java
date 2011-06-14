package com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.algebraweb.editor.server.logicalplan.ContentVal;
import com.algebraweb.editor.server.logicalplan.NodeContent;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.Property;
import com.algebraweb.editor.server.logicalplan.PropertyValue;
import com.algebraweb.editor.server.logicalplan.QueryPlan;
import com.algebraweb.editor.server.logicalplan.ValGroup;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.algebraweb.editor.server.logicalplan.validation.validators.ReferencedColumnsValidator;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Field;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.GoAble;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.GoInto;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeScheme;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Value;



public class PlanParser {


	private File file;
	private HashMap<String,NodeScheme> schemes;
	private String currentSchema = "";

	public PlanParser(HashMap<String,NodeScheme> schemes, String file) {

		this.schemes=schemes;

		this.file = new File(file);

	}



	public QueryPlan parse() {

		QueryPlan ret =null;

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			Element query_plan = (Element) doc.getElementsByTagName("query_plan").item(0);

			NodeList planNodes = query_plan.getElementsByTagName("logical_query_plan").item(0).getChildNodes();

			ret = new QueryPlan(0);

			ret.setPlan(parseNodes((Element)planNodes,ret));



		}catch(IOException e) {e.printStackTrace();}
		catch(SAXException e) {e.printStackTrace();} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}
				
		Iterator<Property> i = ret.getPlanNodeById(4).getReferencableColumnsFromValues().iterator();

		System.out.println("Referencable columns:");
		
		while (i.hasNext()) {
			System.out.println(i.next().getPropertyVal().getVal());
		}
		
		return ret;

	}


	public ArrayList<PlanNode> parseNodes(Element parent, QueryPlan mother) {

		NodeList nodes = parent.getChildNodes();

		ArrayList<PlanNode> planNodes = new ArrayList<PlanNode>();

		for (int i=0;i<nodes.getLength();i++) {

			if (!(nodes.item(i) instanceof Text)) {

				Element el = (Element) nodes.item(i);


				PlanNode newNode = new PlanNode(
						Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue()),
						nodes.item(i).getAttributes().getNamedItem("kind").getNodeValue(),
						getScheme(nodes.item(i).getAttributes().getNamedItem("kind").getNodeValue()),
						mother
						
				);


				fillNode(newNode,el);				
				planNodes.add(newNode);

			}

		}

		return planNodes;

	}


	private void fillNode(PlanNode n, Element nodeEl) {

		NodeScheme s = getScheme(n.getKind());
		currentSchema = n.getKind();

		ArrayList<GoAble> schema = s.getSchema();
		n.setContent(gurr(nodeEl, n.getContent(), schema));

	}


	private NodeContent parseGoAble(GoAble g,Element e) {

		NodeContent retEl = null;

		if (g instanceof GoInto) {
			retEl = new ValGroup(g.getXmlObject());
		}


		if (g instanceof Value) {

			retEl = new ContentVal(((Value)g).getValName(),getTextValue(e));


			ArrayList<Field> fields = ((Value)g).getFields();

			Iterator<Field> i = fields.iterator();

			while(i.hasNext()) {

				Field current = i.next();

				String name = current.getVal();
				String type = current.getType();

				System.out.println(type);

				String value = e.getAttribute(name);

				((ContentVal)retEl).getAttributes().put(new Property(name,value, type));

			}


		}

		ArrayList<GoAble> schema = g.getSchema();

		gurr(e, retEl.getContent(), schema);
		return retEl;

	}



	private ArrayList<NodeContent> gurr(Element e, ArrayList<NodeContent> retEl,
			ArrayList<GoAble> schema) {
		Iterator<GoAble> it = schema.iterator();


		while (it.hasNext()) {

			GoAble next = it.next();

			if (isInteger(((GoInto) next).getHowOften())) {

				int howOften = Integer.parseInt(((GoInto) next).getHowOften());
				parseNumericCount(e, retEl, next, howOften);

			}

			if (((GoInto) next).getHowOften().equals("?")) {

				ArrayList<Element> childs=getElementsByScheme(e,next);

				if (childs.size() > 1) throw new RuntimeException(getErrorMsg("one or no",next,file));

				if (childs.size() == 1) {
					retEl.add(parseGoAble((GoInto) next,
							childs.get(0)));
				}


			}

			if (((GoInto) next).getHowOften().matches("\\{[0-9]+,[0-9]+\\}")) {



				String howOften = ((GoInto) next).getHowOften();

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				ArrayList<Element> childs=getElementsByScheme(e,next);

				if (childs.size() > max || childs.size() < min ) {
					throw new RuntimeException(getErrorMsg("a minimum of " + min + " and a maximum of " + max,next,file));
				}else{

					for (int i=0;i<childs.size();i++) {

						retEl.add(parseGoAble((GoInto) next,
								childs.get(i)));
					}

				}


			}

			if (((GoInto) next).getHowOften().matches("\\{,[0-9]+\\}")) {



				String howOften = ((GoInto) next).getHowOften();

				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				ArrayList<Element> childs=getElementsByScheme(e,next);

				if (childs.size() > max) {
					throw new RuntimeException(getErrorMsg("a maximum of " + max,next,file));
				}else{

					for (int i=0;i<childs.size();i++) {

						retEl.add(parseGoAble((GoInto) next,
								childs.get(i)));
					}

				}


			}

			if (((GoInto) next).getHowOften().matches("\\{[0-9]+,\\}")) {


				String howOften = ((GoInto) next).getHowOften();

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));


				ArrayList<Element> childs=getElementsByScheme(e,next);

				if ( childs.size() < min ) {
					throw new RuntimeException(getErrorMsg("a minumum of " + min,next,file));
				}else{

					for (int i=0;i<childs.size();i++) {

						retEl.add(parseGoAble((GoInto) next,
								childs.get(i)));
					}

				}


			}




			if (((GoInto) next).getHowOften().equals("*") ||
					((GoInto) next).getHowOften().equals("+")) {

				parseStarPlus(e, retEl, next);

			}
		}

		return retEl;
	}





	private ArrayList<Element> getElementsByScheme(Element parent, GoAble g) {



		ArrayList<Element> retList = new ArrayList<Element>();
		NodeList matchingTags = parent.getElementsByTagName(g.getXmlObject());


		for (int a=0;a<matchingTags.getLength();a++) {

			Element el = (Element) matchingTags.item(a);

			if (g instanceof Value) {

				ArrayList<Field> fields = ((Value)g).getFields();
				Iterator<Field> i = fields.iterator();

				boolean fail = false;

				while (i.hasNext()) {

					Field current = i.next();
					String att = current.getVal();

					if ((!el.hasAttribute(att)) ||
							(current.hasMustBe() && !current.getMust_be().equals(el.getAttribute(att)))){

						fail=true;

					}					
				}

				if (!fail) retList.add(el);


			} else {

				retList.add(el);

			}

		}


		return retList;
	}



	private void parseNumericCount(Element e, ArrayList<NodeContent> retEl,
			GoAble next, int howOften) {
		ArrayList<Element> childs=getElementsByScheme(e,next);

		if (childs.size() < howOften) throw new RuntimeException(getErrorMsg(howOften + "",next,file));

		for (int i=0;i<howOften;i++) {

			retEl.add(parseGoAble((GoInto) next,
					childs.get(i)));

		}
	}



	private void parseStarPlus(Element e, ArrayList<NodeContent> retEl, GoAble goAble) {
		ArrayList<Element> childs=getElementsByScheme(e,goAble);;

		for (int i=0;i<childs.size();i++) {

			retEl.add(parseGoAble((GoInto) goAble,
					childs.get(i)));
		}

		if (((GoInto) goAble).getHowOften().equals("+") && childs.size() == 0) {
			throw new RuntimeException(getErrorMsg("at least 1",goAble,file));
		}
	}






	private NodeScheme getScheme(String type) {

		NodeScheme s = schemes.get(type);

		if (s==null && type != "__standard") {
			System.out.println("Warning: Could not find scheme for node type '" + type + "'. Falling " +
			"back to standard scheme!");
			return getScheme("__standard");
		}else if (s==null && type == "__standard") {

			System.out.println("Warning: Could not find a standard scheme! Falling back...");
			s=new NodeScheme("___empty");

		}

		return s;

	}


	private String getTextValue(Element el) {

		NodeList childs = el.getChildNodes();

		for (int i=0;i<childs.getLength();i++) {

			if (childs.item(i) instanceof Text) return childs.item(i).getNodeValue();

		}

		return "";

	}


	private boolean isInteger(String a) {

		return (a.matches("[0-9]+"));


	}


	private String getErrorMsg(String howMany, GoAble g, File f) {

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

		ret += " for node type '" + currentSchema + "' in "  + f.getName();

		return ret;
	}



}

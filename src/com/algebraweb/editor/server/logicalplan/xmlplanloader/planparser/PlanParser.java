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
import com.algebraweb.editor.server.logicalplan.QueryPlan;
import com.algebraweb.editor.server.logicalplan.ValGroup;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Field;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.GoAble;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.GoInto;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeScheme;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Value;



public class PlanParser {


	File file;
	HashMap<String,NodeScheme> schemes;

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

			ret.setPlan(parseNodes((Element)planNodes));



		}catch(IOException e) {e.printStackTrace();}
		catch(SAXException e) {e.printStackTrace();} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}


		return ret;

	}


	public ArrayList<PlanNode> parseNodes(Element parent) {

		NodeList nodes = parent.getChildNodes();

		ArrayList<PlanNode> planNodes = new ArrayList<PlanNode>();

		for (int i=0;i<nodes.getLength();i++) {

			if (!(nodes.item(i) instanceof Text)) {

				Element el = (Element) nodes.item(i);
	

				PlanNode newNode = new PlanNode(
						Integer.parseInt(nodes.item(i).getAttributes().getNamedItem("id").getNodeValue()),
						nodes.item(i).getAttributes().getNamedItem("kind").getNodeValue()
						
				);
							

				fillNode(newNode,el);
				planNodes.add(newNode);

			}

		}

		return planNodes;

	}


	private void fillNode(PlanNode n, Element nodeEl) {

		NodeScheme s = getScheme(n.getKind());
		
		

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
				String value = e.getAttribute(name);

				((ContentVal)retEl).getAttributes().put(name, value);

			}



		}

		ArrayList<GoAble> schema = g.getSchema();

		gurr(e, retEl.getChilds(), schema);
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

				NodeList childs=e.getElementsByTagName(next.getXmlObject());

				if (childs.getLength() > 1) throw new RuntimeException("Expected one or no element(s) of type '" +
						next.getXmlObject() + "' in " + file.getName());

				if (childs.getLength() == 1) {
					retEl.add(parseGoAble((GoInto) next,
							(Element) e.getElementsByTagName(next.getXmlObject()).item(0)));
				}


			}

			if (((GoInto) next).getHowOften().matches("\\{[0-9]+,[0-9]+\\}")) {

		

				String howOften = ((GoInto) next).getHowOften();

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				NodeList childs=e.getElementsByTagName(next.getXmlObject());

				if (childs.getLength() > max || childs.getLength() < min ) {
					throw new RuntimeException("Expected a minum of " + min + " and a maxium of " + max + " element(s) of type '" +
							next.getXmlObject() + "' in " + file.getName());
				}else{

					for (int i=0;i<childs.getLength();i++) {

						retEl.add(parseGoAble((GoInto) next,
								(Element) e.getElementsByTagName(next.getXmlObject()).item(i)));
					}

				}


			}

			if (((GoInto) next).getHowOften().matches("\\{,[0-9]+\\}")) {

		

				String howOften = ((GoInto) next).getHowOften();
			
				int max = Integer.parseInt(howOften.split(",")[1].replaceAll("\\}", ""));

				NodeList childs=e.getElementsByTagName(next.getXmlObject());

				if (childs.getLength() > max) {
					throw new RuntimeException("Expected a maxium of " + max + " element(s) of type '" +
							next.getXmlObject() + "' in " + file.getName());
				}else{

					for (int i=0;i<childs.getLength();i++) {

						retEl.add(parseGoAble((GoInto) next,
								(Element) e.getElementsByTagName(next.getXmlObject()).item(i)));
					}

				}


			}
			
			if (((GoInto) next).getHowOften().matches("\\{[0-9]+,\\}")) {


				String howOften = ((GoInto) next).getHowOften();

				int min = Integer.parseInt(howOften.split(",")[0].replaceAll("\\{", ""));
				

				NodeList childs=e.getElementsByTagName(next.getXmlObject());

				if ( childs.getLength() < min ) {
					throw new RuntimeException("Expected a minum of " + min + " element(s) of type '" +
							next.getXmlObject() + "' in " + file.getName());
				}else{

					for (int i=0;i<childs.getLength();i++) {

						retEl.add(parseGoAble((GoInto) next,
								(Element) e.getElementsByTagName(next.getXmlObject()).item(i)));
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



	private void parseNumericCount(Element e, ArrayList<NodeContent> retEl,
			GoAble next, int howOften) {
		NodeList childs=e.getElementsByTagName(next.getXmlObject());

		if (childs.getLength() < howOften) throw new RuntimeException("Expected "+ howOften +" element(s) of type '" +
				next.getXmlObject() + "'" + "' in " + file.getName());

		for (int i=0;i<howOften;i++) {

			retEl.add(parseGoAble((GoInto) next,
					(Element) e.getElementsByTagName(next.getXmlObject()).item(i)));

		}
	}



	private void parseStarPlus(Element e, ArrayList<NodeContent> retEl, GoAble GoAble) {
		NodeList els = e.getElementsByTagName(GoAble.getXmlObject());

		for (int i=0;i<els.getLength();i++) {

			retEl.add(parseGoAble((GoInto) GoAble,
					(Element) els.item(i)));
		}

		if (((GoInto) GoAble).getHowOften().equals("+") && els.getLength() == 0) {
			throw new RuntimeException("Expected at least 1 element of type '" +
					GoAble.getXmlObject() + "'" + "' in " + file.getName());
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



}

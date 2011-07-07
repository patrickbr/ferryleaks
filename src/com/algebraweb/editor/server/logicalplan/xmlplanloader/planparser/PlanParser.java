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

import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.node.ValGroup;
import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.GoInto;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;

/**
 * A parser for webferry's XML-format using the node schemes specified 
 * in the documentation.
 * 
 * @author Patrick Brosi
 *
 */

public class PlanParser {


	private File file;
	private HashMap<String,NodeScheme> schemes;

	public PlanParser(HashMap<String,NodeScheme> schemes, String file) {

		this.schemes=schemes;
		this.file = new File(file);

	}

	public PlanParser(HashMap<String,NodeScheme> schemes) {

		this.schemes=schemes;

	}

	/**
	 * Parse the plan given to the constructor using the specified 
	 * node schemes. Returns a filled QueryPlan containing all the plans
	 * found in the file. Please note that <i>no validation</i> is going on
	 * here. It is not garantueed that the QueryPlans returned here are valid
	 * either in terms of grammatical or semantical correctness. 
	 * @return
	 */
	public QueryPlanBundle parse() {


		//TODO: this should also work with plan bundles...

		QueryPlanBundle ret =new QueryPlanBundle();
		EvaluationContextProvider ecp = new EvaluationContextProvider();

		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			NodeList plans =  doc.getElementsByTagName("query_plan");

			for (int i=0;i<plans.getLength();i++) {
			
				System.out.println(plans.item(i).getAttributes().getNamedItem("id").getNodeValue());
				QueryPlan p = new QueryPlan(Integer.parseInt(plans.item(i).getAttributes().getNamedItem("id").getNodeValue()));
				
				NodeList planNodes = ((Element)plans.item(i)).getElementsByTagName("logical_query_plan").item(0).getChildNodes();

				parseNodes((Element)planNodes,p);

				//TODO: not reliable :(
				p.setRoot(p.getPlan().get(p.getPlan().size()-1));
				System.out.println("Parsed and adding plan #" + p.getId());
				ecp.fillEvaluationContext(p);
				ret.addPlan(p);
			}

		}catch(IOException e) {e.printStackTrace();}
		catch(SAXException e) {e.printStackTrace();} catch (ParserConfigurationException e) {

			e.printStackTrace();
		}

		return ret;

	}


	/**
	 * Parses all PlanNodes from a given parent DOM-element to the mother-QueryPlan
	 * @param parent
	 * @param mother
	 * @return
	 */
	private void parseNodes(Element parent, QueryPlan mother) {

		NodeList nodes = parent.getElementsByTagName("node");

		ArrayList<PlanNode> planNodes = mother.getPlan();

		for (int i=0;i<nodes.getLength();i++) {

			if (!(nodes.item(i) instanceof Text)) {

				Element el = (Element) nodes.item(i);

				PlanNode newNode = parseNode(mother, el);				
				planNodes.add(newNode);

			}
		}

	}

	/**
	 * Parses a PlanNode from a single DOM-element
	 * @param mother
	 * @param el
	 * @return
	 */

	public PlanNode parseNode(QueryPlan mother, Element el) {

		PlanNode newNode = new PlanNode(
				Integer.parseInt(el.getAttributes().getNamedItem("id").getNodeValue()),
				el.getAttributes().getNamedItem("kind").getNodeValue(),
				getScheme(el.getAttributes().getNamedItem("kind").getNodeValue()),
				mother

		);
		fillNode(newNode,el);
		return newNode;

	}

	/**
	 * Fills an empty PlanNode with content and attributes
	 * @param n
	 * @param nodeEl
	 */

	private void fillNode(PlanNode n, Element nodeEl) {

		NodeScheme s = getScheme(n.getKind());
		ArrayList<GoAble> schema = s.getSchema();

		parseContent(nodeEl, n.getContent(), schema);

	}

	/**
	 * Parses an ArrayList of node contents
	 * @param e
	 * @param retEl
	 * @param schema
	 */

	private void parseContent(Element e, ArrayList<NodeContent> retEl, ArrayList<GoAble> schema) {

		Iterator<GoAble> it = schema.iterator();

		while (it.hasNext()) {

			GoAble next = it.next();

			ArrayList<Element> childs=getElementsByScheme(e,next);

			for (int i=0;i<childs.size();i++) {

				retEl.add(parseGoAble((GoInto) next, childs.get(i)));
			}
		}

	}

	/**
	 * Parses an element e with respect to a GoAble scheme g
	 * @param g
	 * @param e
	 * @return
	 */

	private NodeContent parseGoAble(GoAble g,Element e) {

		NodeContent retEl = null;

		if (g instanceof GoInto) {
			retEl = new ValGroup(g.getXmlObject());
		}

		if (g instanceof Value) {

			retEl = new ContentVal(((Value)g).getValName(),((Value)g).getXmlObject(),getTextValue(e));

			ArrayList<Field> fields = ((Value)g).getFields();

			Iterator<Field> i = fields.iterator();

			while(i.hasNext()) {

				Field current = i.next();

				String name = current.getVal();
				String type = current.getType();

				String value = e.getAttribute(name);

				((ContentVal)retEl).getAttributes().put(new Property(name,value, type));

			}
		}

		ArrayList<GoAble> schema = g.getSchema();

		parseContent(e, retEl.getContent(), schema);
		return retEl;

	}

	/**
	 * Returns all XML objects from the parent matching the given
	 * schema. Please note that this is not "depth-aware" for later editing
	 * reasons. See documentation for further details.
	 * 
	 * @param parent
	 * @param g
	 * @return
	 */

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

					if ((!el.hasAttribute(att)) || (current.hasMustBe() && !current.getMust_be().equals(el.getAttribute(att)))){
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


	//todo: should be somewhere central

	public NodeScheme getScheme(String type) {

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

	//TODO: nmust be a better solution

	private String getTextValue(Element el) {

		NodeList childs = el.getChildNodes();

		for (int i=0;i<childs.getLength();i++) {
			if (childs.item(i) instanceof Text) return childs.item(i).getNodeValue();
		}

		return "";
	}


}

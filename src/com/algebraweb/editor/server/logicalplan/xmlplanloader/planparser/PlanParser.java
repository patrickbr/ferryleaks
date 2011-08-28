package com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.algebraweb.editor.client.logicalcanvas.GraphIsEmptyException;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.ContentVal;
import com.algebraweb.editor.shared.node.LabelAttrIdentifierOb;
import com.algebraweb.editor.shared.node.LabelContentIdentifierOb;
import com.algebraweb.editor.shared.node.LabelOb;
import com.algebraweb.editor.shared.node.LabelStringOb;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.Property;
import com.algebraweb.editor.shared.node.PropertyValue;
import com.algebraweb.editor.shared.node.QueryPlan;
import com.algebraweb.editor.shared.node.ValGroup;
import com.algebraweb.editor.shared.scheme.Field;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.algebraweb.editor.shared.scheme.GoInto;
import com.algebraweb.editor.shared.scheme.NodeScheme;
import com.algebraweb.editor.shared.scheme.Value;

/**
 * A parser for webferry's XML-format using the node schemes specified 
 * in the documentation.
 * 
 * @author Patrick Brosi
 *
 */

public class PlanParser {

	private File file;
	private Map<String,NodeScheme> schemes;
	private HttpSession session;

	public PlanParser(Map<String,NodeScheme> schemes,HttpSession session) {
		this.schemes=schemes;
		this.session=session;
	}

	public PlanParser(Map<String, NodeScheme> nodeSchemes, String file,HttpSession session) {
		this.schemes=nodeSchemes;
		this.file = new File(file);
		this.session=session;
	}

	public PlanParser(HttpSession session) {
		this.session=session;
	}

	/**
	 * Fills an empty PlanNode with content and attributes
	 * @param n
	 * @param nodeEl
	 */

	private void fillNode(PlanNode n, Element nodeEl, QueryPlan mother, PlanNode node) {
		NodeScheme s = getScheme(n.getKind());
		List<GoAble> schema = s.getSchema();
		parseNodeLabelSchema(n,s);
		parseContent(nodeEl, n.getContent(), schema, mother, node);
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

	private List<Element> getElementsByScheme(Element parent, GoAble g) {
		ArrayList<Element> retList = new ArrayList<Element>();
		NodeList matchingTags = parent.getElementsByTagName(g.getXmlObject());

		for (int a=0;a<matchingTags.getLength();a++) {
			Element el = (Element) matchingTags.item(a);
			if (g instanceof Value) {
				List<Field> fields = ((Value)g).getFields();
				Iterator<Field> i = fields.iterator();
				boolean fail = false;

				while (i.hasNext()) {
					Field current = i.next();
					String att = current.getName();
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

	/**
	 * Gets a NodeScheme from the servlet context
	 * @param type the string type of the scheme
	 * @return the NodeScheme specifie by type
	 */
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

	private String getTextValue(Element el) {
		NodeList childs = el.getChildNodes();
		for (int i=0;i<childs.getLength();i++) {
			if (childs.item(i) instanceof Text) return childs.item(i).getNodeValue();
		}
		return "";
	}

	/**
	 * @throws SAXException 
	 * @throws IOException 
	 * Parse the plan given to the constructor using the specified 
	 * node schemes. Returns a filled QueryPlan containing all the plans
	 * found in the file. Please note that <i>no validation</i> is going on
	 * here. It is not garantueed that the QueryPlans returned here are valid
	 * either in terms of grammatical or semantical correctness. 
	 * @return
	 * @throws  
	 */
	public QueryPlanBundle parse() throws IOException, SAXException  {

		QueryPlanBundle ret =new QueryPlanBundle();
		EvaluationContextProvider ecp = new EvaluationContextProvider(session);
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			NodeList plans =  doc.getElementsByTagName("query_plan");

			for (int i=0;i<plans.getLength();i++) {
				QueryPlan p = new QueryPlan(Integer.parseInt(plans.item(i).getAttributes().getNamedItem("id").getNodeValue()));
				NodeList planNodes = ((Element)plans.item(i)).getElementsByTagName("logical_query_plan").item(0).getChildNodes();
				parseNodes((Element)planNodes,p);

				try {
					ecp.fillEvaluationContext(p);
					PlanNode root = p.getRootNode(false);
					if (root.getKind().equals("serialize relation")) {
						if (root.getChilds().size()>0) p.getPlan().remove(root.getChilds().get(0));
						p.getPlan().remove(root);
					}
				} catch (GraphNotConnectedException e) {
					System.out.println("Warning: parsed unconnected graph from input!");
				} catch (GraphIsEmptyException e) {
					System.out.println("Warning: parsed empty graph from input!");
				} catch (PlanHasCycleException e) {
					System.out.println("Warning: parsed graph with cycles from input!");
				}
				ret.addPlan(p);
			}
		}catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Parses a List of node contents
	 * @param e the underlying XML node 
	 * @param retEl the list the parse results should be attached to
	 * @param schema the scheme the parser should use
	 * @param mother the surrounding plan already parsed
	 * @param node the node this content belongs to
	 */

	private void parseContent(Element e, List<NodeContent> retEl, List<GoAble> schema,QueryPlan mother, PlanNode node) {
		Iterator<GoAble> it = schema.iterator();
		while (it.hasNext()) {
			GoAble next = it.next();
			List<Element> childs=getElementsByScheme(e,next);
			for (int i=0;i<childs.size();i++) {
				retEl.add(parseGoAble(next, childs.get(i), mother, node));
			}
		}
	}

	private void parseContentLabelSchema(NodeContent retEl, GoAble g) {
		String schema = g.getNameToPrint();
		parseLabelSchema(retEl, schema);
	}

	/**
	 * Parses an element e with respect to a GoAble scheme g
	 * @param g the schema to use for parsing
	 * @param e the element to be parse
	 * @return the NodeContent found
	 */

	private NodeContent parseGoAble(GoAble g,Element e,QueryPlan mother, PlanNode node) {
		NodeContent retEl = null;

		if (g instanceof GoInto) {
			retEl = new ValGroup(g.getXmlObject());
		}

		if (g instanceof Value) {
			retEl = new ContentVal(((Value)g).getValName(),((Value)g).getInternalName(),new PropertyValue(getTextValue(e),"string"));
			List<Field> fields = ((Value)g).getFields();
			Iterator<Field> i = fields.iterator();

			while(i.hasNext()) {
				Field current = i.next();
				String name = current.getName();
				String type = current.getType();
				String value = e.getAttribute(name);
				((ContentVal)retEl).getAttributes().put(new Property(name,value, type));
			}
			if (((Value)g).getInternalName().equals("edge")) {
				try {
					int to = Integer.parseInt(((ContentVal)retEl).getAttributes().get("to").getVal());
					node.getChilds().add(mother.getPlanNodeById(to));
				}catch(Exception ex) {
					System.out.println("Warning: An edge pointed to an invalid node id!");
				}
			}
		}
		parseContentLabelSchema(retEl, g);
		List<GoAble> schema = g.getSchema();
		parseContent(e, retEl.getContent(), schema, mother, node);
		return retEl;
	}

	/**
	 * Parse the string label schema for a specific ContentNode and writes
	 * label elements to the node.
	 * @param retEl the element to be parsed
	 * @param schema the schema to use
	 */

	private void parseLabelSchema(ContentNode retEl, String schema) {
		LabelOb c = new LabelStringOb("");

		for (int i=0;i<schema.length();i++) {
			if (schema.substring(i, i+1).equals("{")) {
				if (c!= null) retEl.addLabelOb(c);
				c = new LabelContentIdentifierOb("");
			}else if ((schema.substring(i, i+1).equals("}")) || (schema.substring(i, i+1).equals("]"))) {
				if (c!= null) retEl.addLabelOb(c);
				c = new LabelStringOb("");
			}else if (schema.substring(i, i+1).equals("[")) {
				if (c!= null) retEl.addLabelOb(c);
				c = new LabelAttrIdentifierOb("");
			}else{
				c.addChar(schema.substring(i, i+1));
			}
		}
		if (!(c == null) && !c.getVal().equals("")) retEl.addLabelOb(c);
	}

	/**
	 * Parses a PlanNode from a single DOM-element
	 * @param mother the surrounding plan
	 * @param el the element to be parsed
	 * @return the parsed and filled PlanNode
	 */ 

	public PlanNode parseNode(QueryPlan mother, Element el) {

		PlanNode newNode = new PlanNode(
				Integer.parseInt(el.getAttributes().getNamedItem("id").getNodeValue()),
				getScheme(el.getAttributes().getNamedItem("kind").getNodeValue()),
				mother
		);
		fillNode(newNode,el, mother, newNode);
		return newNode;
	}

	/**
	 * Parse the string label schema for a specific PlanNode and writes
	 * the result to the node.
	 * @param node the node to be processed
	 * @param s the label scheme to use
	 */

	public void parseNodeLabelSchema(PlanNode node, NodeScheme s) {
		String schema = s.getProperties().get("label_schema");
		if (schema == null) schema = "";
		parseLabelSchema(node, schema);
	}

	//TODO: nmust be a better solution

	/**
	 * Parses all PlanNodes from a given parent DOM-element to the mother-QueryPlan
	 * @param parent the underlying XML element
	 * @param mother the surrounding plan
	 */
	private void parseNodes(Element parent, QueryPlan mother) {
		NodeList nodes = parent.getElementsByTagName("node");
		List<PlanNode> planNodes = mother.getPlan();
		for (int i=0;i<nodes.getLength();i++) {
			if (!(nodes.item(i) instanceof Text)) {
				Element el = (Element) nodes.item(i);
				PlanNode newNode = parseNode(mother, el);				
				planNodes.add(newNode);
			}
		}
	}
}

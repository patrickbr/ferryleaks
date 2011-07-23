package com.algebraweb.editor.server.logicalplan.xmlbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.PlanManipulationException;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.QueryPlan;

public class XMLNodePlanBuilder {


	public XMLNodePlanBuilder() {



	}


	public Element getNodePlan(int id, PlanNode rootNode, EvaluationContext c, ServletContext context) throws PlanManipulationException {

		Element nodePlan = new Element("query_plan");
		nodePlan.setAttribute("id", Integer.toString(id));
		Element logicalPlan = new Element("logical_query_plan");
		logicalPlan.setAttribute("unique_names", "true");

		if (!rootNode.getKind().equals("serialize relation")) {

			System.out.println("Adding serialize relation from eval context...");
			SerializeRelationBuilder srb = new SerializeRelationBuilder(c,context);
			rootNode = srb.addSerializRelation(rootNode);

		}

		HashMap<Integer,Integer> repl = new HashMap<Integer,Integer>();//getNodeIdReplacements(rootNode,0);

		ArrayList<PlanNode> nodes = getAllNodesUnderThis(rootNode);

		Collections.reverse(nodes);
		Iterator<PlanNode> it = nodes.iterator();

		while (it.hasNext()) {
			logicalPlan.addContent(getXMLElementFromContentNode(it.next(),repl));
		}

		nodePlan.addContent(logicalPlan);
		return nodePlan;

	}






	public Document getNodePlan(QueryPlan p, ServletContext context) throws PlanManipulationException, GraphNotConnectedException {

		PlanNode root = p.getRootNode();

		System.out.println("Rootnode is #" + root.getId());

		Document d = new Document();

		d.addContent(getNodePlan(p.getId(),p.getRootNode(),p.getEvContext(),context));

		return d;

	}


	private HashMap<Integer,Integer> getNodeIdReplacements(PlanNode rootNode,int offset) {

		HashMap<Integer,Integer> retMap = new HashMap<Integer,Integer>();

		ArrayList<PlanNode> nodes = getAllNodesUnderThis(rootNode);

		Collections.reverse(nodes);
		Iterator<PlanNode> it = nodes.iterator();

		while (it.hasNext()) {

			int id = it.next().getId();

			System.out.println("Mapping " + id + " to " + offset);
			retMap.put(id,offset);
			offset++;

		}

		return retMap;


	}

	private ArrayList<PlanNode> getAllNodesUnderThis(PlanNode rootNode) {


		ArrayList<PlanNode> nodes = new ArrayList<PlanNode>();

		if (rootNode == null) return nodes;

		Iterator<PlanNode> it = rootNode.getChilds().iterator();

		nodes.add(rootNode);

		while (it.hasNext()) {

			Iterator<PlanNode> i = getAllNodesUnderThis(it.next()).iterator();

			while (i.hasNext()) {

				PlanNode current = i.next();

				if (!nodes.contains(current)) {

					nodes.add(current);

				}else{
					nodes.remove(current);
					nodes.add(current);
				}

			}

		}


		return nodes;



	}


	public Element getXMLElementFromContentNode(ContentNode n) {

		return getXMLElementFromContentNode(n, new HashMap<Integer,Integer>());

	}


	public Element getXMLElementFromContentNode(ContentNode n,HashMap<Integer,Integer> nodeIdReplacements) {


		Element ret = null;
		
		if (n instanceof PlanNode) {



			ret = new Element("node");
			if (nodeIdReplacements.get(((PlanNode) n).getId()) != null) {

				ret.setAttribute("id", Integer.toString(nodeIdReplacements.get(((PlanNode) n).getId())));

			}else{
				ret.setAttribute("id", Integer.toString(((PlanNode)n).getId()));
			}

			ret.setAttribute("kind", ((PlanNode)n).getKind());



		}else{

			ret = new Element(n.getInternalName());

			Iterator<Property> it = ((NodeContent)n).getAttributes().properties().iterator();

			while(it.hasNext()) {

				Property c = it.next();


				if (c.getPropertyVal().getType().equals("__NID") &&
						nodeIdReplacements.get(Integer.parseInt(c.getPropertyVal().getVal()))!=null) {


					System.out.println("Replacing #" + Integer.parseInt(c.getPropertyVal().getVal()) + " with " + Integer.toString(nodeIdReplacements.get(Integer.parseInt(c.getPropertyVal().getVal()))));
					ret.setAttribute(c.getPropertyName(), Integer.toString(nodeIdReplacements.get(Integer.parseInt(c.getPropertyVal().getVal()))));

				}else{
					if (c.getPropertyVal().getVal() != null)
					ret.setAttribute(c.getPropertyName(), c.getPropertyVal().getVal());
				}
			}

			if (n instanceof ContentVal && ((ContentVal)n).getValue() !=null && ((ContentVal)n).getValue().getVal() !="") {
				ret.addContent(new Text(((ContentVal)n).getValue().getVal()));
			}

		}

		Iterator<NodeContent> i = n.getContent().iterator();

		while (i.hasNext()) {

			ret.addContent(getXMLElementFromContentNode(i.next(),nodeIdReplacements));

		}


		return ret;

	}










}

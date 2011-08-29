package com.algebraweb.editor.server.logicalplan.xmlbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;

import com.algebraweb.editor.shared.exceptions.GraphIsEmptyException;
import com.algebraweb.editor.shared.exceptions.GraphNotConnectedException;
import com.algebraweb.editor.shared.exceptions.PlanHasCycleException;
import com.algebraweb.editor.shared.exceptions.PlanManipulationException;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.ContentVal;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.Property;
import com.algebraweb.editor.shared.node.QueryPlan;

/**
 * 
 * Builds XML documents out of query plan bundles
 * 
 * @author Patrick Brosi
 *
 */
public class XMLNodePlanBuilder {

	public XMLNodePlanBuilder() {
	}

	private List<PlanNode> getAllNodesUnderThis(PlanNode rootNode) throws PlanHasCycleException {
		return getAllNodesUnderThis(rootNode, new ArrayList<PlanNode>());
	}

	private List<PlanNode> getAllNodesUnderThis(PlanNode rootNode, List<PlanNode> way) throws PlanHasCycleException {
		List<PlanNode> nodes = new ArrayList<PlanNode>();

		if (rootNode == null) return nodes;

		Iterator<PlanNode> it = rootNode.getChilds().iterator();
		nodes.add(rootNode);

		while (it.hasNext()) {
			PlanNode cur = it.next();
			List<PlanNode> wayCopy = new ArrayList<PlanNode>();
			wayCopy.addAll(way);
			if (wayCopy.contains(cur) && cur != null) throw new PlanHasCycleException(cur.getId());
			wayCopy.add(cur);
			Iterator<PlanNode> i = getAllNodesUnderThis(cur,wayCopy).iterator();

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

	@SuppressWarnings("unused")
	private Map<Integer,Integer> getNodeIdReplacements(PlanNode rootNode,int offset) throws PlanHasCycleException {
		HashMap<Integer,Integer> retMap = new HashMap<Integer,Integer>();
		List<PlanNode> nodes = getAllNodesUnderThis(rootNode);

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

	/**
	 * Returns an XML element holding the plan specified by the attributes
	 * @param id the id the XML plan should have
	 * @param rootNode the root node of the plan
	 * @param c the evaluation context of the plan
	 * @param context the servlet context
	 * @return an XML element holding the query plan 
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public Element getNodePlan(int id, PlanNode rootNode, EvaluationContext c, ServletContext context) throws PlanManipulationException, PlanHasCycleException {
		Element nodePlan = new Element("query_plan");
		nodePlan.setAttribute("id", Integer.toString(id));
		Element logicalPlan = new Element("logical_query_plan");
		logicalPlan.setAttribute("unique_names", "true");

		System.out.println("rootnode is " + rootNode.getId() + "(" + rootNode.getKind() + ")");

		if (!rootNode.getKind().equals("serialize relation")) {
			System.out.println("Adding serialize relation from eval context...");
			SerializeRelationBuilder srb = new SerializeRelationBuilder(c,context);
			rootNode = srb.addSerializRelation(rootNode);
		}

		HashMap<Integer,Integer> repl = new HashMap<Integer,Integer>();//getNodeIdReplacements(rootNode,0);
		List<PlanNode> nodes = getAllNodesUnderThis(rootNode);

		Collections.reverse(nodes);
		Iterator<PlanNode> it = nodes.iterator();

		while (it.hasNext()) {
			logicalPlan.addContent(getXMLElementFromContentNode(it.next(),repl));
		}

		nodePlan.addContent(logicalPlan);
		return nodePlan;
	}

	/**
	 * Returns a plan XML document holding the query plan specified
	 * @param p the query plan
	 * @param context the servlet context to use
	 * @return an XML document holding the entire plan
	 * @throws PlanManipulationException
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public Document getNodePlan(QueryPlan p, ServletContext context) throws PlanManipulationException, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException {
		Document d = new Document();
		d.addContent(getNodePlan(p.getId(),p.getRootNode(),p.getEvContext(),context));
		return d;
	}

	/**
	 * Returns a plan XML document holding the query plan bundle specified
	 * @param b the query plan bundl
	 * @param context the servlet context to use
	 * @return an XML document holding the entire plan bundle
	 * @throws PlanManipulationException
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public Document getPlanBundle(QueryPlanBundle b, ServletContext context) throws PlanManipulationException, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException {
		Element planBundle = new Element ("query_plan_bundle");
		Iterator<QueryPlan> it = b.getPlans().values().iterator();

		while (it.hasNext()) {
			QueryPlan cur = it.next();
			planBundle.addContent(getNodePlan(cur.getId(), cur.getRootNode(), cur.getEvContext(), context));
		}

		Document d = new Document();
		d.addContent(planBundle);
		return d;
	}

	/**
	 * Returns an XML representation of a single content node
	 * @param n the content node to translate
	 * @return the XML representation as an XML element
	 */
	public Element getXMLElementFromContentNode(ContentNode n) {
		return getXMLElementFromContentNode(n, new HashMap<Integer,Integer>());
	}


	/**
	 * Returns an XML representation of a single content node. All node ids are replaced
	 * according to the nodeIdReplacement map 
	 * @param n the content node to translate
	 * @param nodeIdReplacements the replacement map
	 * @return
	 */
	public Element getXMLElementFromContentNode(ContentNode n,Map<Integer,Integer> nodeIdReplacements) {
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

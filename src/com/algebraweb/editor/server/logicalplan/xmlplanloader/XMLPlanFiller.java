package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.server.graphcanvas.remotefiller.GraphCanvasFiller;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.QueryPlan;
import com.algebraweb.editor.shared.scheme.NodeScheme;

/**
 * A GraphCanvasFiller for loading parsed xml files onto the client's graphcanvas
 * @author Patrick Brosi
 *
 */
public class XMLPlanFiller implements GraphCanvasFiller{

	private HttpSession session;
	private ServletContext context;
	private Iterator<RawNode> it;
	private int id;

	public XMLPlanFiller(HttpSession s, ServletContext context, int id) {
		this.session=s;
		this.id=id;
		this.context = context;
	}

	/**
	 * Translates a plan node into the raw node format that will be sent to
	 * the client
	 * @param node the node to translate
	 * @return the raw node
	 */
	@SuppressWarnings("unchecked")
	public RawNode getRawNode(PlanNode node) {
		RawNode temp = new RawNode(node.getId(), node.getLabel(), 0xCCCCCC, 130, 25);
		temp.setFixedChildCount(node.getMaxChildCount());
		Iterator<PlanNode> childs = node.getChilds().iterator();

		int c=1;

		while (childs.hasNext()) {
			PlanNode cur = childs.next();
			if (cur != null) {
				RawEdge tempEdge= new RawEdge(cur.getId(),temp.getNid());
				tempEdge.setFixedParentPos(c);
				temp.getEdgesToList().add(tempEdge);
			}
			c++;
		}

		Map<String,NodeScheme> schemes = (Map<String,NodeScheme>) context.getAttribute("nodeSchemes"); 
		if (getScheme(node.getKind(),schemes).getProperties().containsKey("color")) {
			temp.setColor(Integer.parseInt((getScheme(node.getKind(),schemes).getProperties().get("color")).split("x")[1],16));
		}
		return temp;
	}

	/**
	 * Get raw nodes for a whole query plan.
	 * @param qp the query plan to translate
	 * @return a list of all raw nodes
	 */
	public List<RawNode> getRawNodes(QueryPlan qp) {
		List<RawNode> rawNodes = new ArrayList<RawNode>();
		Iterator<PlanNode> it = qp.getPlan().iterator();

		while (it.hasNext()) {
			PlanNode current = it.next();
			RawNode temp = getRawNode(current);
			rawNodes.add(temp);
		}
		return rawNodes;
	}

	private NodeScheme getScheme(String type,Map<String, NodeScheme> schemes) {
		NodeScheme s = schemes.get(type);
		if (s==null && type != "__standard") {
			return getScheme("__standard",schemes);
		}else if (s==null && type == "__standard") {
			s=new NodeScheme("___empty");
		}
		return s;
	}

	@Override
	public boolean hasNextEdge() {
		return false;
	}

	@Override
	public boolean hasNextNode() {
		return it.hasNext();
	}

	@Override
	public void init() {
		QueryPlanBundle qps = (QueryPlanBundle) session.getAttribute("queryPlans");
		it = getRawNodes(qps.getPlan(id)).iterator();
	}

	@Override
	public RawNode nextNode() {
		return it.next();
	}

}

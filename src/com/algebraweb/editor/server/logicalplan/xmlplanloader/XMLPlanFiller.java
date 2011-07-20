package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.server.graphcanvas.remotefiller.GraphCanvasFiller;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;

public class XMLPlanFiller implements GraphCanvasFiller{


	private HttpSession session;
	private ServletContext context;
	private Iterator<RawNode> it;
	private int id;

	public XMLPlanFiller(HttpSession s, ServletContext context, int id) {

		this.session=s;
		this.id=id;
		this.context = context;
		
		System.out.println("Initialized XMLPlanFiller with id=" + id);

	}

	@Override
	public boolean hasNextEdge() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean hasNextNode() {
		
		System.out.println("call to hasnextnode for id=" + id);
		
		return it.hasNext();
	}

	@Override
	public void init() {
		
		System.out.println("call to init for id=" + id);

		QueryPlanBundle qps = (QueryPlanBundle) session.getAttribute("queryPlans");

		//TODO: make this workable for planids

		it = getRawNodes(qps.getPlan(id)).iterator();

	}

	@Override
	public RawNode nextNode() {
		// TODO Auto-generated method stub
		return it.next();
	}

	public ArrayList<RawNode> getRawNodes(QueryPlan qp) {


		ArrayList<RawNode> rawNodes = new ArrayList<RawNode>();
		Iterator<PlanNode> it = qp.getPlan().iterator();

		while (it.hasNext()) {

			PlanNode current = it.next();

			if (current != null) {

				RawNode temp = getRawNode(current);
				rawNodes.add(temp);
			}
		}

		return rawNodes;

	}

	public RawNode getRawNode(PlanNode current) {

		System.out.println(current.getLabel());
		RawNode temp = new RawNode(current.getId(), current.getLabel(), 0xCCCCCC, 130, 25);

		temp.setFixedChildCount(current.getMaxChildCount());
				
		Iterator<PlanNode> childs = current.getChilds().iterator();
		
		int c=1;

		while (childs.hasNext()) {

			PlanNode cur = childs.next();

			if (cur != null) {

				RawEdge tempEdge= new RawEdge(cur.getId(),temp.getNid());
				
				tempEdge.setFixedParentPos(c);
				System.out.println("XMLplanfiller: adding edge from " + tempEdge.getFrom() + " to " + tempEdge.getTo());
				temp.getEdgesToList().add(tempEdge);

			}
			
			c++;
		}

		HashMap<String,NodeScheme> schemes = (HashMap<String,NodeScheme>) context.getAttribute("nodeSchemes"); 

		if (getScheme(current.getKind(),schemes).getProperties().containsKey("color")) {
			temp.setColor(Integer.parseInt((getScheme(current.getKind(),schemes).getProperties().get("color")).split("x")[1],16));
		}

		return temp;

	}

	private NodeScheme getScheme(String type,HashMap<String,NodeScheme> schemes) {

		NodeScheme s = schemes.get(type);

		if (s==null && type != "__standard") {
			System.out.println("Warning: Could not find scheme for node type '" + type + "'. Falling " +
			"back to standard scheme!");
			return getScheme("__standard",schemes);
		}else if (s==null && type == "__standard") {

			System.out.println("Warning: Could not find a standard scheme! Falling back...");
			s=new NodeScheme("___empty");

		}

		return s;

	}




}

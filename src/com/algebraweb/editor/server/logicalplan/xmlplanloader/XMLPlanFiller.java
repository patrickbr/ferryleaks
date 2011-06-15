package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.server.graphcanvas.remotefiller.GraphCanvasFiller;
import com.algebraweb.editor.server.logicalplan.ContentVal;
import com.algebraweb.editor.server.logicalplan.NodeContent;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.QueryPlan;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeScheme;

public class XMLPlanFiller implements GraphCanvasFiller{
	
	
	private HttpSession session;
	private ServletContext context;
	private Iterator<RawNode> it;
	
	public XMLPlanFiller(HttpSession s, ServletContext context) {
		
		this.session=s;
		this.context = context;
		
	}

	@Override
	public boolean hasNextEdge() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean hasNextNode() {
		// TODO Auto-generated method stub
		return it.hasNext();
	}

	@Override
	public void init() {
		
		QueryPlanBundle qps = (QueryPlanBundle) session.getAttribute("queryPlans");
		
		//TODO: make this workable for planids
		
		it = getRawNodes(qps.getPlan(0)).iterator();
		
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
			
			RawNode temp = new RawNode(current.getId(), current.getKind(), 0xCCCCCC, 130, 25);
			
			NodeContent[] edges = current.getAllContentWithInternalName("edge").toArray(new NodeContent[0]);
			
			for (NodeContent e : edges) {
				
				temp.getEdgesToList().add(new RawEdge(Integer.parseInt(((ContentVal)e).getAttributes().get("to").getVal()),temp.getNid()));
				
			}
			
			HashMap<String,NodeScheme> schemes = (HashMap<String,NodeScheme>) context.getAttribute("nodeSchemes"); 
				
			if (getScheme(current.getKind(),schemes).getProperties().containsKey("color")) {
				temp.setColor(Integer.parseInt((getScheme(current.getKind(),schemes).getProperties().get("color")).split("x")[1],16));
			}
			
			
			rawNodes.add(temp);
		}
		
		return rawNodes;
		
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

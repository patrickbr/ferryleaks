package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;


import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.server.graphcanvas.remotefiller.GraphCanvasFiller;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.ContentVal;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.NodeContent;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanNode;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.QueryPlan;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeScheme;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeSchemeLoader;

public class XMLPlanLoader {


	private QueryPlan qp;
	private ArrayList<RawNode> rawNodes;
	private Iterator<RawNode> it;

	public QueryPlan parsePlan(String file,ServletContext context) {


		HashMap<String,NodeScheme> nodeSchemes = new HashMap<String,NodeScheme>();

		if (context.getAttribute("nodeSchemes") == null) {
			
			System.out.println(context.getRealPath("/"));

			NodeSchemeLoader l = new NodeSchemeLoader(context.getRealPath("/") + "testscheme.xml");


			Iterator<NodeScheme> i = l.parse().iterator();

			while (i.hasNext()) {

				NodeScheme n = i.next();

				nodeSchemes.put(n.getKind(), n);

			}
			
			
			context.setAttribute("nodeSchemes", nodeSchemes);

		}else{
			
			System.out.println("using prev. loaded scheme...");
			nodeSchemes = (HashMap<String,NodeScheme>) context.getAttribute("nodeSchemes");
		}


		PlanParser p = new PlanParser(nodeSchemes,file);

		QueryPlan qp = p.parse();

		System.out.println(qp.toString());

		return qp;

	}


}

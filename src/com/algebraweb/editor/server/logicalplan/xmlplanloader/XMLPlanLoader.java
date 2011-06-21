package com.algebraweb.editor.server.logicalplan.xmlplanloader;


import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;


import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeSchemeLoader;

public class XMLPlanLoader {
	

	/**
	 * Parses the plan file into the given server context. A previously loaded 
	 * @param file
	 * @param context
	 * @return
	 */

	public QueryPlan parsePlan(String file,ServletContext context) {


		HashMap<String,NodeScheme> nodeSchemes = new HashMap<String,NodeScheme>();

		if (context.getAttribute("nodeSchemes") == null) {
			
			//TODO: make this configurable
			NodeSchemeLoader l = new NodeSchemeLoader(context.getRealPath("/schemes"));

			Iterator<NodeScheme> i = l.parse().iterator();

			while (i.hasNext()) {

				NodeScheme n = i.next();
				nodeSchemes.put(n.getKind(), n);

			}
				
			context.setAttribute("nodeSchemes", nodeSchemes);

		}else{
			
			//we only want to load the scheme file once in a editors lifetime...
			System.out.println("using prev. loaded scheme...");
			nodeSchemes = (HashMap<String,NodeScheme>) context.getAttribute("nodeSchemes");
		}

		PlanParser p = new PlanParser(nodeSchemes,file);
		QueryPlan qp = p.parse();

		return qp;

	}

}

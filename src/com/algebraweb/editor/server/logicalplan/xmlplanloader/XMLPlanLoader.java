package com.algebraweb.editor.server.logicalplan.xmlplanloader;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeSchemeLoader;

public class XMLPlanLoader {


	/**
	 * Parses the plan file into the given server context. A previously loaded 
	 * @param file
	 * @param context
	 * @return
	 */

	public QueryPlanBundle parsePlans(String file,ServletContext context,HttpSession session) {


		Map<String,NodeScheme> nodeSchemes = new HashMap<String,NodeScheme>();

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
			nodeSchemes = (Map<String,NodeScheme>) context.getAttribute("nodeSchemes");
		}
		PlanParser p = new PlanParser(nodeSchemes,file,session);
		QueryPlanBundle qpb = p.parse();
		return qpb;
	}
}

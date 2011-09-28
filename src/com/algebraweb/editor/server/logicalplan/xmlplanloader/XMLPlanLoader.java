package com.algebraweb.editor.server.logicalplan.xmlplanloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;
import org.xml.sax.SAXException;

import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeSchemeLoader;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.shared.scheme.NodeScheme;

public class XMLPlanLoader {

	/**
	 * Parses the plan file into the given server context. A previously loaded
	 * node schema will be reused
	 * 
	 * @param file
	 *            the filename to use
	 * @param context
	 *            the context to load
	 * @return the parsed query plan bundle
	 * @throws IOException
	 * @throws SAXException
	 * @throws RemoteIOException
	 */

	public QueryPlanBundle parsePlans(String file, ServletContext context,
			HttpSession session) throws IOException, SAXException,
			RemoteIOException {
		Map<String, NodeScheme> nodeSchemes = new HashMap<String, NodeScheme>();

		nodeSchemes = initialize(context, nodeSchemes);

		PlanParser p = new PlanParser(nodeSchemes, file, session);
		QueryPlanBundle qpb = p.parse();
		return qpb;
	}
	
	public QueryPlanBundle parsePlans(InputStream inputStream, ServletContext context,
			HttpSession session) throws IOException, SAXException,
			RemoteIOException {
		Map<String, NodeScheme> nodeSchemes = new HashMap<String, NodeScheme>();

		nodeSchemes = initialize(context, nodeSchemes);

		PlanParser p = new PlanParser(nodeSchemes, inputStream, session);
		QueryPlanBundle qpb = p.parse();
		return qpb;
	}

	@SuppressWarnings("unchecked")
	private Map<String, NodeScheme> initialize(ServletContext context,
			Map<String, NodeScheme> nodeSchemes) throws RemoteIOException {
		if (context.getAttribute("nodeSchemes") == null) {

			NodeSchemeLoader l = new NodeSchemeLoader(context
					.getRealPath(((Configuration) context
							.getAttribute("configuration")).getString(
							"server.schemes.path", "/schemes")));
			Iterator<NodeScheme> i = l.parse().iterator();

			while (i.hasNext()) {
				NodeScheme n = i.next();
				nodeSchemes.put(n.getKind(), n);
			}
			context.setAttribute("nodeSchemes", nodeSchemes);

		} else {
			nodeSchemes = (Map<String, NodeScheme>) context
					.getAttribute("nodeSchemes");
		}
		return nodeSchemes;
	}
}

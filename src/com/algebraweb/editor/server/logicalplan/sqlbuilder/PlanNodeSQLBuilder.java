package com.algebraweb.editor.server.logicalplan.sqlbuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.algebraweb.editor.shared.exceptions.PathFinderCompilationErrorException;

/**
 * Builds SQL queries from plan bundles using an external Pathfinder
 * installation
 * 
 * @author Patrick Brosi
 * 
 */
public class PlanNodeSQLBuilder {
	private String[] args = new String[2];

	public PlanNodeSQLBuilder(String pfPath, String pfArgs) {
		this.args[0] = pfPath;
		this.args[1] = pfArgs;
	}

	/**
	 * Returns a map of sql queries from a given XML element
	 * 
	 * @param planToCompile
	 *            the plan or plan bundle to give Pathfinder
	 * @return a map of all SQL queries with plan ids as a key
	 * @throws PathFinderCompilationErrorException
	 */
	public Map<Integer, String> getCompiledSQL(Element planToCompile)
			throws PathFinderCompilationErrorException {
		Map<Integer, String> ret = new HashMap<Integer, String>();
		byte[] buffer = new byte[1024];

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(args);
			BufferedInputStream error = new BufferedInputStream(p
					.getErrorStream());
			BufferedOutputStream b = new BufferedOutputStream(p
					.getOutputStream());
			OutputStreamWriter w = new OutputStreamWriter(b);
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			String XMLString = outputter.outputString(planToCompile);
			// XMLOutputter outputterr = new
			// XMLOutputter(Format.getPrettyFormat());
			// System.out.println(outputterr.outputString(planToCompile));
			w.write(XMLString);
			w.close();

			int bytesRead = 0;
			String errorMsg = "";

			while ((bytesRead = error.read(buffer)) != -1) {
				String chunk = new String(buffer, 0, bytesRead);
				errorMsg += chunk;
			}
			try {
				org.w3c.dom.Document doc = db.parse(p.getInputStream());
				org.w3c.dom.Element bundle = (org.w3c.dom.Element) doc
						.getElementsByTagName("query_plan_bundle").item(0);
				NodeList plans = bundle.getElementsByTagName("query_plan");

				for (int i = 0; i < plans.getLength(); i++) {
					org.w3c.dom.Element plan = (org.w3c.dom.Element) plans
							.item(i);
					int id = Integer.parseInt(plan.getAttributes()
							.getNamedItem("id").getNodeValue());
					ret.put(id, plan.getElementsByTagName("query").item(0)
							.getTextContent());
				}
			} catch (SAXException e) {
				System.err.println(errorMsg);
				throw new PathFinderCompilationErrorException(errorMsg);
			}
		} catch (IOException e) {
			throw new PathFinderCompilationErrorException(e.getMessage());

		} catch (ParserConfigurationException e) {
			throw new PathFinderCompilationErrorException(e.getMessage());
		}
		return ret;
	}
}

package com.algebraweb.editor.server.logicalplan.sqlbuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.algebraweb.editor.client.logicalcanvas.PathFinderCompilationError;



public class PlanNodeSQLBuilder {



	private String pfPath = "/usr/local/bin/pf";
	private String[] args = {pfPath, "-IS"};


	public PlanNodeSQLBuilder() {


	}



	public HashMap<Integer,String> getCompiledSQL(Element planToCompile) throws PathFinderCompilationError {

		HashMap<Integer,String> ret = new HashMap<Integer,String>();
		byte[] buffer = new byte[1024];

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(args);

			BufferedInputStream error = new BufferedInputStream(p.getErrorStream());

			BufferedOutputStream b = new BufferedOutputStream(p.getOutputStream());
			OutputStreamWriter w = new OutputStreamWriter(b);


			XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat());
			String XMLString = outputter.outputString(planToCompile);


			w.write(XMLString);
			w.close();

			int bytesRead = 0;
			String errorMsg="";

			while ((bytesRead = error.read(buffer)) != -1) {

				String chunk = new String(buffer, 0, bytesRead);
				errorMsg += chunk;
			}		

			try {
				org.w3c.dom.Document doc = db.parse(p.getInputStream());


				org.w3c.dom.Element bundle = (org.w3c.dom.Element) doc.getElementsByTagName("query_plan_bundle").item(0);

				NodeList plans =  bundle.getElementsByTagName("query_plan");

				for (int i=0;i<plans.getLength();i++) {

					org.w3c.dom.Element plan = (org.w3c.dom.Element) plans.item(i);


					int id = Integer.parseInt(plan.getAttributes().getNamedItem("id").getNodeValue());

					ret.put(id, plan.getElementsByTagName("query").item(0).getTextContent());

				}
			} catch (SAXException e) {


				throw new PathFinderCompilationError(errorMsg);

			}

		} catch (IOException e) {
			throw new PathFinderCompilationError(e.getMessage());

		} catch (ParserConfigurationException e) {
			throw new PathFinderCompilationError(e.getMessage());

		}

		return ret;

	}

}

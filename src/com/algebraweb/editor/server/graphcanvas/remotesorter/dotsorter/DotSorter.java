package com.algebraweb.editor.server.graphcanvas.remotesorter.dotsorter;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.RemoteIOException;
import com.algebraweb.editor.server.graphcanvas.remotesorter.RemoteSorter;

/**
 * A simple dot-sorter using the SVG/XML output of dot
 * @author Patrick Brosi
 *
 */

public class DotSorter implements RemoteSorter {

	private double dotCorrector = 0.01818;
	private String dotPath = "dot";
	private String arg = "-Tsvg";
	
	private Configuration c;
	
	public DotSorter(String dotPath, String arg, double dotCorrector) {
		this.dotCorrector = dotCorrector;
		this.dotPath = dotPath;
		this.arg=arg;
		
	}
	
	/**
	 * Returns a HashMap containing node-IDs as keys and Node-Coordinates as value
	 * @param nodes
	 * @return
	 * @throws RemoteIOException 
	 */
	@Override
	public HashMap<Integer, Coordinate> getCoordinateHashMap(
			ArrayList<RawNode> nodes) throws RemoteIOException {
		
		
		HashMap<Integer, Coordinate> ret = new HashMap<Integer, Coordinate>();
	
		Document doc = getDotXml(getDotCode(nodes));
		Element graphEl= getNodeByTitle("sort_graph",doc.getDocumentElement());

		Iterator<RawNode> i = nodes.iterator();

		String transforms[] = graphEl.getAttribute("transform").split("\\)");

		double offsetX=0;
		double offsetY=0;

		for (String transform : transforms) {

			transform = transform.trim();

			if (transform.split("\\(")[0].equals("translate")) {
				offsetX = Double.parseDouble(transform.split("\\(")[1].split(" ")[0]);
				offsetY = Double.parseDouble(transform.split("\\(")[1].split(" ")[1]);
			}			
		}

		while (i.hasNext()) {
		
			RawNode c = i.next();

			Element coresNode = getNodeByTitle("n_" + c.getNid(),((Element)doc.getElementsByTagName("svg").item(0)));

			Element nodeRect = ((Element)coresNode.getElementsByTagName("polygon").item(0));

			double x = offsetX + Double.parseDouble(nodeRect.getAttribute("points").trim().split(" ")[1].split(",")[0]);
			double y = offsetY + Double.parseDouble(nodeRect.getAttribute("points").trim().split(" ")[1].split(",")[1]);

			Coordinate cord = new Coordinate(x,y);

			ret.put(c.getNid(), cord);

		}

		return ret;
	}
	
	/**
	 * Returns a string containing the dot-code of the graph specified in nodes
	 * @param nodes
	 * @return
	 */

	private String getDotCode(ArrayList<RawNode> nodes) {

		String ret = "digraph sort_graph {\n graph [ordering=out];";

		Iterator<RawNode> i = nodes.iterator();

		while (i.hasNext()) {

			ret += getDotNodeString(i.next()) + "\n";

		}

		ret +="\n\n";
		Iterator<RawNode> j = nodes.iterator();

		while (j.hasNext()) {

			RawNode current = j.next();
			Iterator<RawEdge> it = current.getEdgesToList().iterator();

			while (it.hasNext()) {

				ret += getDotEdgeString(current.getNid(), it.next().getTo()) + "\n";
			}
		}

		ret +="}";
		return ret;
	}
	
	/**
	 * Returns the dot code for an edge (from,to)
	 * @param from
	 * @param to
	 * @return
	 */

	private String getDotEdgeString(int from, int to) {

		return "n_" + from + " -> n_" + to + ";";

	}

	/**
	 * Returns the dot code for a given RawNode 
	 * @param n
	 * @return
	 */

	private String getDotNodeString(RawNode n) {

		double width = (n.getWidth()) * dotCorrector; 
		double height = (n.getHeight()) * dotCorrector;

		String ret ="";

		ret += "n_" + n.getNid() + " ";
		ret += "[shape=box fixedsize=true width=" + width + " height=" + height + " label=\"" + n.getText() + "\"];";

		return ret;	

	}
	
	/**
	 * returns an XML-Document containing dot's generated SVG
	 * @param dotSource
	 * @return
	 * @throws RemoteIOException 
	 */

	private Document getDotXml(String dotSource) throws RemoteIOException {

		Runtime rt = Runtime.getRuntime();

		//we want svg output
		String[] args = {dotPath, arg};

		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
			//disable validating using an external DTD, since dot's XML is a bit buggy
			//see http://xerces.apache.org/xerces2-j/features.html
			dbf.setValidating(false);
			dbf.setFeature("http://xml.org/sax/features/external-general-entities",false);
			dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			
			DocumentBuilder db = dbf.newDocumentBuilder();
		
			Process p = rt.exec(args);

			BufferedOutputStream b = new BufferedOutputStream(p.getOutputStream());
			OutputStreamWriter w = new OutputStreamWriter(b);

			w.write(dotSource);
			w.close();
	
			Document doc = db.parse(p.getInputStream());
		
			return doc;

		} catch (IOException e) {
			throw new RemoteIOException(e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


		return null;

	}

	/**
	 * returns a node by it's nested title-Tag.
	 * @param title
	 * @param root
	 * @return
	 */
	
	private Element getNodeByTitle(String title, Element root) {

		NodeList nodes = root.getElementsByTagName("g");

		for (int i=0;i<nodes.getLength();i++) {

			if (!(nodes.item(i) instanceof Text)) {

				if (((Element)nodes.item(i)).getElementsByTagName("title").item(0).getFirstChild().getNodeValue().equals(title)) {

					return (Element) nodes.item(i);

				}
			}	
		}
		return null;
	}

}

package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.GoInto;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;

/**
 * The node scheme loader and parser.  Node schemes have to
 * be saved as single scheme files or as scheme bundles using
 * the format specified in the * docs. Only filenames ending 
 * with .scheme.xml will be parsed.
 * 
 * @author Patrick Brosi
 *
 */

public class NodeSchemeLoader {

	private File file;

	/**
	 * Initializes a NodeSchemeLoader. You can use a 
	 * specific file or a whole directory here.
	 * @param file
	 */

	public NodeSchemeLoader(String file) {
		this.file = new File(file);
	}

	/**
	 * Start parsing. An ArrayList containing all NodeSchemes
	 * found in the file/directory will be returned or an empty
	 * ArrayList if no schemes could be found.
	 * @return
	 */
	public ArrayList<NodeScheme> parse() {

		ArrayList<NodeScheme> ret = new ArrayList<NodeScheme>();

		File[] files = file.listFiles(new SchemeFileFilter());

		for (File file : files) {

			try{
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(file);

				Element bundle = (Element) doc.getElementsByTagName("nodescheme_bundle").item(0);

				NodeList nodeschemas;

				if (bundle != null) {
					nodeschemas = bundle.getElementsByTagName("nodeschema");
				}else{
					nodeschemas = doc.getElementsByTagName("nodeschema");
				}

				for (int i =0;i<nodeschemas.getLength();i++) {

					ret.add(parseSchema((Element) nodeschemas.item(i)));

				}

			//TODO
			}catch(IOException e) {e.printStackTrace();}
			catch(SAXException e) {e.printStackTrace();} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return ret;

	}

	/**
	 * Parses a single nodeschema out of a parent XML element
	 * @param nodeschema
	 * @return
	 */

	private NodeScheme parseSchema(Element nodeschema) {

		NodeScheme ret;
		NodeList childs = nodeschema.getElementsByTagName("schema").item(0).getChildNodes();

		ret = new NodeScheme(nodeschema.getAttribute("kind"));

		Element properties = (Element) nodeschema.getElementsByTagName("properties").item(0);

		for (int i=0;i< properties.getElementsByTagName("property").getLength();i++) {

			Node current =  properties.getElementsByTagName("property").item(i);

			ret.getProperties().put(current.getAttributes().getNamedItem("name").getNodeValue(),
					current.getFirstChild().getNodeValue());

		}

		for (int i=0;i<childs.getLength();i++) {

			if (isGoAbleXMLOb(childs.item(i))) {

				Element e = (Element) childs.item(i);

				ret.addToSchema(parseSchemeXMLOb(e));


			}
		}
		return ret;
	}

	private GoAble parseSchemeXMLOb(Element e) {

		Node n = e;

		String xmlOb = n.getAttributes().getNamedItem("xmlob").getNodeValue();
		String name = n.getAttributes().getNamedItem("name") != null?n.getAttributes().getNamedItem("name").getNodeValue() : xmlOb;
		String howOften = n.getAttributes().getNamedItem("howoften").getNodeValue() != null?n.getAttributes().getNamedItem("howoften").getNodeValue():"1";
		String humanName = n.getAttributes().getNamedItem("humanname") != null?n.getAttributes().getNamedItem("humanname").getNodeValue():name;
		String nameField = n.getAttributes().getNamedItem("namefield") != null?n.getAttributes().getNamedItem("namefield").getNodeValue():"";
		boolean hasVals = Boolean.parseBoolean(n.getAttributes().getNamedItem("hasval") != null?n.getAttributes().getNamedItem("hasval").getNodeValue():"false");
		
		GoInto ret;
		
		if (e.getNodeName().equals("gointo")) {
			ret = new GoInto(xmlOb,howOften,humanName);
		}else{
			ret = new Value(xmlOb,howOften,name, humanName,nameField,hasVals);
			loadFields(e,(Value)ret);
		}
	
		for (int i=0;i<e.getChildNodes().getLength();i++) {

			if (isGoAbleXMLOb(e.getChildNodes().item(i))) {

				ret.addChild(parseSchemeXMLOb((Element) e.getChildNodes().item(i)));
		
			}

		}

		return ret;

	}

	public void loadFields(Element e,Value v) {

		Element fields = getFirstDirectElementsByTagName("fields",e);

		if (fields ==null) return;

		NodeList fieldList = fields.getElementsByTagName("field");

		for (int i=0;i<fieldList.getLength();i++) {

			Field f = new Field(fieldList.item(i).getAttributes().getNamedItem("type").getNodeValue(),
					getTextValue((Element)fieldList.item(i)));

			if (((Element)fieldList.item(i)).hasAttribute("must_be")) {
				f.setMust_be(fieldList.item(i).getAttributes().getNamedItem("must_be").getNodeValue());
			}

			v.addField(f);

		}

	}


	private String getTextValue(Element el) {

		NodeList childs = el.getChildNodes();

		for (int i=0;i<childs.getLength();i++) {

			if (childs.item(i) instanceof Text) return childs.item(i).getNodeValue();

		}

		return "";

	}

	private boolean isGoAbleXMLOb(Node n) {

		return (n.getNodeName().equals("val")) ||
		(n.getNodeName().equals("gointo")); 

	}


	private Element getFirstDirectElementsByTagName(String t, Node e) {

		ArrayList<Node> candidates = getDirectElementsByTagName(t,e);
		if (candidates.size()>0) return (Element) candidates.get(0); else return null;

	}

	private ArrayList<Node> getDirectElementsByTagName(String t, Node e) {

		NodeList n = e.getChildNodes();
		ArrayList<Node> ret = new ArrayList<Node>();

		for (int i=0;i<n.getLength();i++) {

			if (n.item(i).getNodeName().equals(t)) {

				ret.add(n.item(i));
			}
		}
		return ret;
	}

	private class SchemeFileFilter implements FileFilter
	{
		public boolean accept(File file)
		{
			return (file.getName().toLowerCase().endsWith(".scheme.xml"));
		}

	}

}

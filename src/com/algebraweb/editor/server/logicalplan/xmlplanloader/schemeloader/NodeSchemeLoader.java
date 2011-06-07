package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;
import java.io.File;
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

/**
 * The node scheme loader and parser
 * @author patrick
 *
 */

public class NodeSchemeLoader {

	File file;


	public NodeSchemeLoader(String file) {

		this.file = new File(file);
		System.out.println("Initializing NodeSchemeLoader...");

	}

	public ArrayList<NodeScheme> parse() {

		System.out.println("Parsing schemes...");
		ArrayList<NodeScheme> ret = new ArrayList<NodeScheme>();

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

		}catch(IOException e) {e.printStackTrace();}
		catch(SAXException e) {e.printStackTrace();} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return ret;

	}


	private NodeScheme parseSchema(Element nodeschema) {
	
		NodeScheme ret;
		NodeList childs = nodeschema.getElementsByTagName("schema").item(0).getChildNodes();

		System.out.println("found scheme for type '" + nodeschema.getAttribute("kind") + "'");
		
		ret = new NodeScheme(nodeschema.getAttribute("kind"));

	    Element properties = (Element) nodeschema.getElementsByTagName("properties").item(0);
	    
	    for (int i=0;i< properties.getElementsByTagName("property").getLength();i++) {
	    	
	    	Node current =  properties.getElementsByTagName("property").item(i);
	    	
	    	ret.getProperties().put(current.getAttributes().getNamedItem("name").getNodeValue(),
	    			current.getFirstChild().getNodeValue());
	    		    	
	    }

		for (int i=0;i<childs.getLength();i++) {

			if (!(childs.item(i) instanceof Text)) {

				Element e = (Element) childs.item(i);
			
				if (e.getTagName().equals("gointo")) {
					ret.addToSchema(goInto(e));
				}

				if (e.getNodeName().equals("val")) {
					ret.addToSchema(parseVal(e));
				}

			}
		}
		return ret;
	}




	private Value parseVal(Element e) {

		Node n = e;

		String xmlOb = n.getAttributes().getNamedItem("xmlob").getNodeValue();
		String name = n.getAttributes().getNamedItem("name").getNodeValue();
		String howOften = n.getAttributes().getNamedItem("howoften").getNodeValue();

		Value ret = new Value(xmlOb,howOften,name);

		loadFields(e,ret);


		for (int i=0;i<e.getChildNodes().getLength();i++) {

			if (!(e.getChildNodes().item(i) instanceof Text)) {

				if (e.getChildNodes().item(i).getNodeName().equals("val")) {

					ret.addChild(parseVal((Element) e.getChildNodes().item(i)));

				}

				if (e.getChildNodes().item(i).getNodeName().equals("gointo")) {

					ret.addChild(goInto((Element) e.getChildNodes().item(i)));

				}
			}

		}


		return ret;

	}

	public void loadFields(Element e,Value v) {

		Element fields = (Element) e.getElementsByTagName("fields").item(0);
		
		if (fields ==null) return;

		NodeList fieldList = fields.getElementsByTagName("field");

		for (int i=0;i<fieldList.getLength();i++) {

			v.addField(new Field(fieldList.item(i).getAttributes().getNamedItem("type").getNodeValue(),
					getTextValue((Element)fieldList.item(i))));

		}

	}

	private GoInto goInto(Element e) {

		Node n = e;

		String xmlOb = n.getAttributes().getNamedItem("xmlob").getNodeValue();
		String howOften = n.getAttributes().getNamedItem("howoften").getNodeValue();

		GoInto ret = new GoInto(xmlOb,howOften);

		for (int i=0;i<e.getChildNodes().getLength();i++) {


			if (!(e.getChildNodes().item(i) instanceof Text)) {

				if (e.getChildNodes().item(i).getNodeName().equals("val")) {

					ret.addChild(parseVal((Element) e.getChildNodes().item(i)));

				}

				if (e.getChildNodes().item(i).getNodeName().equals("gointo")) {

					ret.addChild(goInto((Element) e.getChildNodes().item(i)));

				}
			}
		}



		return ret;
	}
	
	private String getTextValue(Element el) {

		NodeList childs = el.getChildNodes();

		for (int i=0;i<childs.getLength();i++) {

			if (childs.item(i) instanceof Text) return childs.item(i).getNodeValue();

		}

		return "";

	}


}

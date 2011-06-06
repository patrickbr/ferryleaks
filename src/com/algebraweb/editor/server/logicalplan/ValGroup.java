package com.algebraweb.editor.server.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;



public class ValGroup implements NodeContent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4630462466854800864L;

	protected ArrayList<NodeContent> childs = new ArrayList<NodeContent>();

	ArrayList<Property> attributes = new ArrayList<Property>();

	String name;
	
	public ValGroup(String name) {
		
		this.name=name;
				
	}

	public ArrayList<NodeContent> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}

	public ArrayList<Property> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Property> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		
		String ret = "{VALGROUP '" + name + "' childs:";
		
		Iterator<NodeContent> i = childs.iterator();
		
		
		while (i.hasNext()) {
			
			ret+=i.next().toString();
			
		}
		
		return ret + "}";
	}
	
	public NodeContent[] getValuesByName(String name) {
		
		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();
				
		Iterator<NodeContent> i = childs.iterator();
				
		while (i.hasNext()) {
			
			NodeContent c = i.next();
			if (c.getName().equals(name)) temp.add(c);
					
		}
		
		NodeContent[] ret = new NodeContent[0];
			
		return temp.toArray(ret);	
				
	}

}

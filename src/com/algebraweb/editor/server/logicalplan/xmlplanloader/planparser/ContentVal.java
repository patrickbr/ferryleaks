package com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ContentVal implements NodeContent {
	
	
	protected ArrayList<NodeContent> childs = new ArrayList<NodeContent>();
	
	HashMap<String,String> attributes = new HashMap<String,String>();
	
	String name;
	String value;


	public ContentVal(String name,String value) {
		
		this.name=name;
		this.value=value;
		
	}


	public ArrayList<NodeContent> getChilds() {
		return childs;
	}


	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}


	public HashMap<String,String> getAttributes() {
		return attributes;
	}


	public void setAttributes(HashMap<String,String> attributes) {
		this.attributes = attributes;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		
		String ret = "[CONTENTVAL '" + name + "' value:" + value + " childs:(";
		
		Iterator<NodeContent> i = childs.iterator();
		
		
		while (i.hasNext()) {
			
			ret+=i.next().toString();
			
		}
		
		ret +=") attributes:";
		
		Set s = attributes.keySet();
		
		Iterator<String> a = s.iterator();
		
		while (a.hasNext()) {
			
			String n = a.next();
			
			ret+="{" + n + "="  + attributes.get(n) + "}";
			
		}
		
		
		return ret+"]";
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

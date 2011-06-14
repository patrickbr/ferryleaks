package com.algebraweb.editor.server.logicalplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ContentVal implements NodeContent, ContentNode {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -168461026763593678L;

	
	protected ArrayList<NodeContent> childs = new ArrayList<NodeContent>();
	
	PropertyMap attributes = new PropertyMap();
	
	String name;
	String value;


	public ContentVal(String name,String value) {
		
		this.name=name;
		this.value=value;
		
	}


	public ArrayList<NodeContent> getContent() {
		return childs;
	}


	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}


	public PropertyMap getAttributes() {
		return attributes;
	}


	public void setAttributes(PropertyMap attributes) {
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
	
	public ArrayList<NodeContent> getValuesByInternalName(String name) {
		
		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();
				
		Iterator<NodeContent> i = childs.iterator();
				
		while (i.hasNext()) {
			
			NodeContent c = i.next();
			if (c.getName().equals(name)) temp.add(c);
			
			temp.addAll(c.getValuesByInternalName(name));
					
		}
		
		return temp;
				
	}
	
	public boolean removeContent(NodeContent con) {


		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c == con) {
				i.remove();
				return true;
			}

			//go into child
			return c.removeContent(con);

		}

		return false;

	}


	
}

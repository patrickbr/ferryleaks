package com.algebraweb.editor.client.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ContentVal extends NodeContent {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -168461026763593678L;

	

	private PropertyValue value;


	public ContentVal() {
		
		
	}
	
	public ContentVal(String name, String internalName,PropertyValue value) {
		
		this.name=name;
		this.value=value;
		this.internalName = internalName;
		
	}

	@Override
	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	@Override
	public String getInternalName() {
		return internalName;
	}

	public PropertyValue getValue() {
		return value;
	}

	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}
	
	public void setValue(PropertyValue value) {
		this.value = value;
	}

	@Override
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

}

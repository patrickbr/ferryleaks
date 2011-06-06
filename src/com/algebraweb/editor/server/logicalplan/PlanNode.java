package com.algebraweb.editor.server.logicalplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class PlanNode implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1765471707304843471L;
	//TODO: schema
	
	ArrayList<NodeContent> content = new ArrayList<NodeContent>();
	HashMap<String,String> properties = new HashMap<String,String>();
	
	int id;
	String kind;

	
	public PlanNode(int id, String kind) {
		
		this.id=id;
		this.kind=kind;
		
	}

	public ArrayList<NodeContent> getContent() {
		return content;
	}
	
	public HashMap<String,String> getProperties() {
		return properties;
	}

	public void setContent(ArrayList<NodeContent> content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	
	public String toString() {
		
		
		String ret= "{NODE: id=" + id + " kind:" + kind + " CONTENT:[";
		
		Iterator<NodeContent> i = content.iterator();
		
				
		while (i.hasNext()) {
			
			ret+=i.next().toString();
			
		}
		
		
		
		return ret+"]}";
		
		
	}
	
	public NodeContent[] getValuesByName(String name) {
		
		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();
				
		Iterator<NodeContent> i = content.iterator();
				
		while (i.hasNext()) {
			
			NodeContent c = i.next();
			if (c.getName().equals(name)) temp.add(c);
					
		}
		
		NodeContent[] ret = new NodeContent[0];
			
		return temp.toArray(ret);	
				
	}
	
	
}

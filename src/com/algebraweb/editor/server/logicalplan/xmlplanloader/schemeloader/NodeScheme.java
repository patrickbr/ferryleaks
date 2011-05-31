package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class NodeScheme {
	
	private String kind;
	private HashMap<String,String> properties = new HashMap<String,String>();
	private ArrayList<GoAble> schema = new ArrayList<GoAble>();
	
	
	public NodeScheme(String kind) {
		this.kind=kind;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public void setSchema(ArrayList<GoAble> schema) {
		this.schema = schema;
	}

	
	public HashMap<String,String> getProperties() {
		return properties;
	}
	
	public ArrayList<GoAble> getSchema() {
		return schema;
	}
	
	public void addToSchema(GoAble goAble) {
		this.schema.add(goAble);
	}
	
	public String toString() {
		
		Iterator<GoAble> i = schema.iterator();
		
		String ret ="";
		
		while(i.hasNext()) {
			
			ret+="\n" + i.next().toString();
			
		}
		
		return ret;
		
		
	}
	
	
	
}

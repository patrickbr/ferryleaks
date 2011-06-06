package com.algebraweb.editor.server.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.Property;



public class QueryPlan {
	
	int id;
	ArrayList<Property> properties = new ArrayList<Property>();
	ArrayList<PlanNode> plan = new ArrayList<PlanNode>();
	
	
	public QueryPlan(int id) {
		this.id=id;
	}
	
	
	public ArrayList<Property> getProperties() {
		return properties;
	}
	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}
	public ArrayList<PlanNode> getPlan() {
		return plan;
	}
	public void setPlan(ArrayList<PlanNode> plan) {
		this.plan = plan;
	}
	
	
	public String toString() {
		
		
		String ret = "";
		
		Iterator<PlanNode> i = plan.iterator();
		
		while (i.hasNext()) {
			
			ret += i.next().toString() + "\n";
			
		}
		
		return ret;
	}
 	
	

}

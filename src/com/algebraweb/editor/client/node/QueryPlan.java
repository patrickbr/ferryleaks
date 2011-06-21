package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;





public class QueryPlan implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2371425529625584530L;
	int id = -1;
	ArrayList<Property> properties = new ArrayList<Property>();
	ArrayList<PlanNode> plan = new ArrayList<PlanNode>();
	
	
	public QueryPlan(int id) {
		this.id=id;
	}
	
	public QueryPlan() {
		
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
	
	public int getId() {
		return id;
	}
	
	
	
	public String toString() {
				
		String ret = "";
		
		Iterator<PlanNode> i = plan.iterator();
		
		while (i.hasNext()) {
			
			ret += i.next().toString() + "\n";
			
		}
		
		return ret;
	}
	
	public PlanNode getPlanNodeById(int id) {
		
		
		Iterator<PlanNode> i = plan.iterator();
		
		while (i.hasNext()) {
			
			
			PlanNode current = i.next();
			
			if (current.getId() == id) return current;
			
			
		}
				
		return null;
				
	}
 	
	

}

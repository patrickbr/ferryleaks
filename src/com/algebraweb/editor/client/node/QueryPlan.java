package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.scheme.NodeScheme;





public class QueryPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2371425529625584530L;
	int id = -1;
	ArrayList<Property> properties = new ArrayList<Property>();
	ArrayList<PlanNode> plan = new ArrayList<PlanNode>();
	private PlanNode root;


	public PlanNode getRoot() {
		return root;
	}

	public void setRoot(PlanNode root) {
		this.root = root;
	}

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

	public PlanNode addNode(NodeScheme s) {
		
		PlanNode n = new PlanNode(getFreeId(), s.getKind(), s, this);
		plan.add(n);
		return n;
		
	}
	
	
	private int getFreeId() {
		
		Iterator<PlanNode> it = plan.iterator();
		
		int current = 0;
		
		while (getPlanNodeById(current) != null) {
			
			current++;
			
		}
		
		return current;
		
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
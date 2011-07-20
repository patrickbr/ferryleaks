package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.GoInto;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;





public class QueryPlan implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2371425529625584530L;
	int id = -1;
	ArrayList<Property> properties = new ArrayList<Property>();
	ArrayList<PlanNode> plan = new ArrayList<PlanNode>();
	private PlanNode root;
	private EvaluationContext evContext;


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

		PlanNode n = new PlanNode(getFreeId(), s, this);
		
		//TODO: not nice
		for (int i=0;i<n.getMaxChildCount();i++) {
			
			n.getChilds().add(null);
			
		}
		
		Iterator<GoAble> it = s.getSchema().iterator();

		while (it.hasNext()) {
			
			GoAble cur = it.next();
			
			if (cur instanceof GoInto && !(cur instanceof Value)) {
				
				n.getContent().add(new ValGroup(((GoInto)cur).getInternalName()));
				
			}
			
			
		}
		
		plan.add(n);
		return n;

	}


	/**
	 * @return the evContext
	 */
	public EvaluationContext getEvContext() {
		return evContext;
	}

	/**
	 * @param evContext the evContext to set
	 */
	public void setEvContext(EvaluationContext evContext) {
		this.evContext = evContext;
	}

	public int getFreeId() {

		return getFreeId(new ArrayList<Integer>());

	}

	public int getFreeId(ArrayList<Integer> blackList) {

		int current = 0;

		while (blackList.contains(current) || getPlanNodeById(current) != null) {

			current++;

		}

		return current;

	}

	public String toString() {

		String ret = "";
		Iterator<PlanNode> i = plan.iterator();

		while (i.hasNext()) {
			
			PlanNode cur = i.next();
			
			if (cur != null) ret += i.next().toString() + "\n";
		}

		return ret;
	}

	public PlanNode getPlanNodeById(int id) {

		Iterator<PlanNode> i = plan.iterator();

		while (i.hasNext()) {

			PlanNode current = i.next();
			if (current != null && current.getId() == id) return current;

		}

		System.out.println("Plannode with id #" + id  + " not found!");
		return null;

	}



}

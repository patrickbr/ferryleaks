package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphIsEmptyException;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
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
	List<Property> properties = new ArrayList<Property>();
	List<PlanNode> plan = new ArrayList<PlanNode>();
	private EvaluationContext evContext;

	public QueryPlan() {

	}

	public QueryPlan(int id) {
		this.id=id;
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
	private void deleteChildsFromPlan(PlanNode p, List<PlanNode> temp) {

		Iterator<PlanNode> it = p.getChilds().iterator();

		while (it.hasNext()) {
			PlanNode current = it.next();
			temp.remove(current);
		}


	}
	/**
	 * @return the evContext
	 */
	public EvaluationContext getEvContext() {
		return evContext;
	}
	public int getFreeId() {

		return getFreeId(new ArrayList<Integer>());

	}

	public int getFreeId(List<Integer> nids) {

		int current = 0;

		while (nids.contains(current) || getPlanNodeById(current) != null) {
			current++;
		}

		return current;
	}

	public int getId() {
		return id;
	}

	public List<PlanNode> getParents(PlanNode n) {
		List<PlanNode> ret = new ArrayList<PlanNode>();
		Iterator<PlanNode> it = getPlan().iterator();

		while (it.hasNext()) {
			PlanNode cur = it.next();
			if(cur != null && cur.getChilds().contains(n)) ret.add(cur);
		}

		return ret;
	}

	public List<PlanNode> getPlan() {
		return plan;
	}

	public PlanNode getPlanNodeById(int id) {
		Iterator<PlanNode> i = plan.iterator();
		while (i.hasNext()) {
			PlanNode current = i.next();
			if (current != null && current.getId() == id) return current;
		}

		return null;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public PlanNode getRootNode() throws GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException{
		return getRootNode(true);
	}

	public PlanNode getRootNode(boolean skipSerializeRelation) throws GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException{
		List<PlanNode> temp = new ArrayList<PlanNode>();
		temp.addAll(this.getPlan());

		if (temp.size() == 0) throw new GraphIsEmptyException();

		Iterator<PlanNode> itChilds = this.getPlan().iterator();

		while (itChilds.hasNext()) {

			deleteChildsFromPlan(itChilds.next(),temp);

		}

		if (temp.size() == 0) throw new PlanHasCycleException(this.getPlan().get(0).getId());

		if (temp.size()>1) throw new GraphNotConnectedException();

		//TODO: throws error if plan has cycle

		if (skipSerializeRelation && temp.get(0).getKind().equals("serialize relation")) {
			return temp.get(0).getChilds().get(1);
		}else{
			return temp.get(0);
		}
	}

	/**
	 * @param evContext the evContext to set
	 */
	public void setEvContext(EvaluationContext evContext) {
		this.evContext = evContext;
	}

	public void setPlan(ArrayList<PlanNode> plan) {
		this.plan = plan;
	}


	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String ret = "";
		Iterator<PlanNode> i = plan.iterator();

		while (i.hasNext()) {
			PlanNode cur = i.next();
			if (cur != null) ret += i.next().toString() + "\n";
		}
		return ret;
	}
}

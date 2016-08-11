package com.algebraweb.editor.shared.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.shared.exceptions.GraphIsEmptyException;
import com.algebraweb.editor.shared.exceptions.GraphNotConnectedException;
import com.algebraweb.editor.shared.exceptions.PlanHasCycleException;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.algebraweb.editor.shared.scheme.GoInto;
import com.algebraweb.editor.shared.scheme.NodeScheme;
import com.algebraweb.editor.shared.scheme.Value;

/**
 * Holds an entire query plan.
 *
 * @author Patrick Brosi
 *
 */
public class QueryPlan implements Serializable, Cloneable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2371425529625584530L;
	private int id = -1;
	private List<Property> properties = new ArrayList<Property>();
	private List<PlanNode> plan = new ArrayList<PlanNode>();
	private EvaluationContext evContext;

	public QueryPlan() {
	}

	public QueryPlan(int id) {
		this.id = id;
	}

	public PlanNode addNode(NodeScheme s) {
		PlanNode n = new PlanNode(getFreeId(), s, this);

		// TODO: not nice
		for (int i = 0; i < n.getMaxChildCount(); i++) {
			n.getChilds().add(null);
		}
		Iterator<GoAble> it = s.getSchema().iterator();
		while (it.hasNext()) {
			GoAble cur = it.next();
			if (cur instanceof GoInto && !(cur instanceof Value)) {
				n.getContent().add(
						new ValGroup(((GoInto) cur).getInternalName()));
			}
		}
		plan.add(n);
		return n;
	}

	/**
	 * Removes a list of plan nodes from a node's childs
	 *
	 * @param p
	 *            the PlanNode the childs should be deleted from
	 * @param remove
	 *            the List of nodes to be deleted
	 */
	private void deleteChildsFromPlan(PlanNode p, List<PlanNode> remove) {
		Iterator<PlanNode> it = p.getChilds().iterator();
		while (it.hasNext()) {
			PlanNode current = it.next();
			remove.remove(current);
		}
	}

	/**
	 * Returns the EvaluationContext of this whole plan
	 *
	 * @return the evContext
	 */
	public EvaluationContext getEvContext() {
		return evContext;
	}

	/**
	 * Returns a free node id.
	 *
	 * @return a free node id
	 */
	public int getFreeId() {
		return getFreeId(new ArrayList<Integer>());
	}

	/**
	 * Returns a free node id not contained in the <i>nids</i> blacklist
	 *
	 * @param nids
	 *            the blacklist
	 * @return a free node id
	 */
	public int getFreeId(List<Integer> nids) {
		int current = 0;
		while (nids.contains(current) || getPlanNodeById(current) != null) {
			current++;
		}
		return current;
	}

	/**
	 * Returns the plan id
	 *
	 * @return the plan id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns a nodes parents
	 *
	 * @param n
	 *            the node
	 * @return a list of parents
	 */
	public List<PlanNode> getParents(PlanNode n) {
		List<PlanNode> ret = new ArrayList<PlanNode>();
		Iterator<PlanNode> it = getPlan().iterator();
		while (it.hasNext()) {
			PlanNode cur = it.next();
			if (cur != null && cur.getChilds().contains(n)) {
				ret.add(cur);
			}
		}
		return ret;
	}

	/**
	 * Returns a list of all plan nodes
	 *
	 * @return the plan node list
	 */
	public List<PlanNode> getPlan() {
		return plan;
	}

	/**
	 * Returns a plan node with a given id
	 *
	 * @param id
	 *            the id to look for
	 * @return the plan node
	 */
	public PlanNode getPlanNodeById(int id) {
		Iterator<PlanNode> i = plan.iterator();
		while (i.hasNext()) {
			PlanNode current = i.next();
			if (current != null && current.getId() == id) {
				return current;
			}
		}
		return null;
	}

	/**
	 * Returns all properties of this query plan as specified in the xml file
	 *
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * Returns the root node of this plan. Throws exceptions if plan has cycles,
	 * is empty or has multiple roots.
	 *
	 * @return the root node
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public PlanNode getRootNode() throws GraphNotConnectedException,
			GraphIsEmptyException, PlanHasCycleException {
		return getRootNode(true);
	}

	/**
	 * Returns the root node of this plan. Throws exceptions if plan has cycles,
	 * is empty or has multiple roots. Skips serialize relation if parameter is
	 * set to true
	 *
	 * @param skipSerializeRelation
	 *            true if serialize relation should be skipped
	 * @return the root node
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public PlanNode getRootNode(boolean skipSerializeRelation)
			throws GraphNotConnectedException, GraphIsEmptyException,
			PlanHasCycleException {
		List<PlanNode> temp = new ArrayList<PlanNode>();
		temp.addAll(this.getPlan());
		if (temp.size() == 0) {
			throw new GraphIsEmptyException();
		}
		Iterator<PlanNode> itChilds = this.getPlan().iterator();

		while (itChilds.hasNext()) {
			deleteChildsFromPlan(itChilds.next(), temp);
		}

		if (temp.size() == 0) {
			throw new PlanHasCycleException(this.getPlan().get(0).getId());
		}
		if (temp.size() > 1) {
			throw new GraphNotConnectedException();
		}

		// TODO: throws error if plan has cycle

		if (skipSerializeRelation
				&& temp.get(0).getKind().equals("serialize relation")) {
			return temp.get(0).getChilds().get(1);
		} else {
			return temp.get(0);
		}
	}

	/**
	 * Sets the evaluation context of this plan
	 *
	 * @param evContext
	 *            the evContext to set
	 */
	public void setEvContext(EvaluationContext evContext) {
		this.evContext = evContext;
	}

	/**
	 * Sets the plan of this query plan
	 *
	 * @param plan
	 *            the plan as a list of plan nodes
	 */
	public void setPlan(List<PlanNode> plan) {
		this.plan = plan;
	}

	/**
	 * Sets the properties of this query plan
	 *
	 * @param properties
	 *            the properties
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		String ret = "";
		Iterator<PlanNode> i = plan.iterator();

		while (i.hasNext()) {
			PlanNode cur = i.next();
			if (cur != null) {
				ret += i.next().toString() + "\n";
			}
		}
		return ret;
	}
}
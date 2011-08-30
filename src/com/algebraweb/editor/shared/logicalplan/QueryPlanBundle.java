package com.algebraweb.editor.shared.logicalplan;

import java.util.HashMap;
import java.util.Map;

import com.algebraweb.editor.shared.node.QueryPlan;
import com.google.gwt.user.client.rpc.IsSerializable;

public class QueryPlanBundle implements IsSerializable {

	private Map<Integer, QueryPlan> plans = new HashMap<Integer, QueryPlan>();

	public QueryPlanBundle() {

	}

	/**
	 * Adds a plan p to this bundle. Returns true if adding was successful,
	 * false if the bundle already contains a plan with the same id
	 * 
	 * @param p
	 *            the query plan to add
	 * @return
	 */
	public boolean addPlan(QueryPlan p) {
		if (!plans.containsKey(p.getId())) {
			plans.put(p.getId(), p);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return a free plan id
	 * 
	 * @return a free plan id
	 */
	public int getFreePlanId() {
		int i = 0;
		while (hasPlanWithId(i)) {
			i++;
		}
		return i;
	}

	/**
	 * Returns the plan with the specific id, null if the bundle does not
	 * contain any such plan
	 * 
	 * @param id
	 *            the id to look for
	 * @return
	 */
	public QueryPlan getPlan(int id) {
		return plans.get(id);
	}

	/**
	 * Returns all plans in this bundle
	 * 
	 * @return the plans as a map. Plan ids are the keys.
	 */
	public Map<Integer, QueryPlan> getPlans() {
		return plans;
	}

	/**
	 * Returns true if the bundle holds a plan with a given id
	 * 
	 * @param id
	 *            the plan id to look for
	 * @return true if the bundle holds a plan with the given id
	 */
	public boolean hasPlanWithId(int id) {
		return plans.containsKey(id);
	}
}

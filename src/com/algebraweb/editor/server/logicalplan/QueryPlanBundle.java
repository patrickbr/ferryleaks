package com.algebraweb.editor.server.logicalplan;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A queryplan-bundle
 */

import com.algebraweb.editor.client.node.QueryPlan;
import com.google.gwt.user.client.rpc.IsSerializable;

public class QueryPlanBundle implements IsSerializable {


	private HashMap<Integer,QueryPlan> plans = new HashMap<Integer,QueryPlan>();


	public QueryPlanBundle() {

	}

	/**
	 * Adds a plan p to this bundle. Returns true if adding 
	 * was successful, false if the bundle already contains
	 * a plan with the same id
	 * @param p
	 * @return
	 */

	public boolean addPlan(QueryPlan p) {

		if (!plans.containsKey(p.getId())) {

			plans.put(p.getId(), p);
			return true;

		}else{
			return false;
		}

	}

	/**
	 * Returns the plan with the specific id, null
	 * if the bundle does not contain any such plan
	 * @param id
	 * @return
	 */

	public QueryPlan getPlan(int id) {
		return plans.get(id);
	}


	public HashMap<Integer,QueryPlan> getPlans() {

		return plans;
	}

	public int getFreePlanId() {

		int i=0;
		while (hasPlanWithId(i)) i++;
		return i;

	}

	public boolean hasPlanWithId(int id) {

		return plans.containsKey(id);

	}


}

package com.algebraweb.editor.server.logicalplan;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QueryPlanBundle implements IsSerializable {
	
	
	private HashMap<Integer,QueryPlan> plans = new HashMap<Integer,QueryPlan>();
	
	
	public QueryPlanBundle() {
		
		
	}
	
	public boolean addPlan(QueryPlan p) {
		
		if (!plans.containsKey(p.getId())) {
			
			plans.put(p.getId(), p);
			return true;
			
		}else{
			return false;
		}
			
	}
	
	public QueryPlan getPlan(int id) {
		
		return plans.get(id);
		
		
	}
	

}

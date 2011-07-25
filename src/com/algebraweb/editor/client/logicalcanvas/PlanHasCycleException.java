package com.algebraweb.editor.client.logicalcanvas;

public class PlanHasCycleException extends Exception {
	
	public PlanHasCycleException() {
		
	}
	
	public PlanHasCycleException(int onId) {
		super("Graph contains a cycle beginning with node #" + onId);
	}

}

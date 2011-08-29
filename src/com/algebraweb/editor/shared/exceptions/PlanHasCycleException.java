package com.algebraweb.editor.shared.exceptions;

public class PlanHasCycleException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7980411829047802319L;

	public PlanHasCycleException() {
		
	}
	
	public PlanHasCycleException(int onId) {
		super("Graph contains a cycle beginning with node #" + onId);
	}

}

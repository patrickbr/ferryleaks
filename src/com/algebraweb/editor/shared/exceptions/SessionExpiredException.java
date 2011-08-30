package com.algebraweb.editor.shared.exceptions;

public class SessionExpiredException extends PlanManipulationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1671818406644437926L;

	public SessionExpiredException() {
		super("Your session has expired.");
	}
}

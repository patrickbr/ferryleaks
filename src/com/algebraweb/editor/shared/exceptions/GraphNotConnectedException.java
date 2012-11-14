package com.algebraweb.editor.shared.exceptions;

public class GraphNotConnectedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6578995356462778754L;

	public GraphNotConnectedException() {
		super("Graph is not connected!");
	}
}

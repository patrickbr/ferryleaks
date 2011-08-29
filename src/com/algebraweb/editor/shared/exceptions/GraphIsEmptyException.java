package com.algebraweb.editor.shared.exceptions;

public class GraphIsEmptyException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 255417360828372254L;

	public GraphIsEmptyException() {
		super("Graph is empty");
	}
}

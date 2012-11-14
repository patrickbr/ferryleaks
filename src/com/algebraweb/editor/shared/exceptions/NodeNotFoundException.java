package com.algebraweb.editor.shared.exceptions;

public class NodeNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int nid;

	public NodeNotFoundException(int nid) {
		this.nid = nid;
	}

	@Override
	public String getMessage() {
		return "No node with nid=" + nid + " was found.";
	}

}

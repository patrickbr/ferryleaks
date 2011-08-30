package com.algebraweb.editor.client.validation;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A validation error encountered during validation
 * 
 * @author Patrick Brosi
 * 
 */
public class ValidationError implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1855604011450094810L;
	private int nodeId;
	private String errorMsg;

	public ValidationError() {

	}

	public ValidationError(int nodeId, String errorMsg) {
		this.nodeId = nodeId;
		this.errorMsg = errorMsg;
	}

	/**
	 * Returns the human readable error message of this error
	 * 
	 * @return the error message
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * Returns the affected node's id
	 * 
	 * @return the node id
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the human readable error message
	 * 
	 * @param errorMsg
	 *            the error message to set
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * Set the affected node's id
	 * 
	 * @param nodeId
	 *            the node id to ste
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
}

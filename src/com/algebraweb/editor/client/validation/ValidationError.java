package com.algebraweb.editor.client.validation;

import com.google.gwt.user.client.rpc.IsSerializable;



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

	public String getErrorMsg() {
		return errorMsg;
	}

	public int getNodeId() {
		return nodeId;
	}



	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}



	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}



}

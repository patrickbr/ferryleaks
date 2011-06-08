package com.algebraweb.editor.server.logicalplan.validation;

public class ValidationError {



	private int nodeId;
	private String errorMsg;



	public ValidationError(int nodeId, String errorMsg) {

		this.nodeId = nodeId;
		this.errorMsg = errorMsg;


	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}



	public String getErrorMsg() {
		return errorMsg;
	}



	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}



}

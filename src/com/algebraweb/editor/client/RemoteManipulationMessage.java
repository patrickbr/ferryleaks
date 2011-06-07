package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoteManipulationMessage implements IsSerializable{
	
	
	private int returnCode;
	private String message;
	
	
	public RemoteManipulationMessage() {
		
	}
	
	public RemoteManipulationMessage(int returnCode, String message) {
		
		this.returnCode = returnCode;
		this.message = message;
				
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}

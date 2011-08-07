package com.algebraweb.editor.client.logicalcanvas;

public class RemoteConfigurationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteConfigurationException(String msg) {
		super("Error while loading configuration from server: " + msg);
	}
	
	public RemoteConfigurationException() {
	}

}

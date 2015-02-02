package com.algebraweb.editor.shared.exceptions;

public class RemoteConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteConfigurationException() {
	}

	public RemoteConfigurationException(String msg) {
		super("Error while loading configuration from server: " + msg);
	}
}
package com.algebraweb.editor.client.services;

import com.algebraweb.editor.client.RemoteConfiguration;
import com.algebraweb.editor.shared.exceptions.RemoteConfigurationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The registration service, providing methods for user registration
 * 
 * @author Patrick Brosi
 * 
 */
@RemoteServiceRelativePath("register")
public interface RegistrationService extends RemoteService {

	/**
	 * Send a keep alive to the server
	 */
	public void keepAlive();

	/**
	 * Register on the server. Will return a RemoteConfiguratoin object holding
	 * the server configuration as well as already loaded plans.
	 * 
	 * @return the RemogeConfiguration object
	 * @throws RemoteConfigurationException
	 */
	public RemoteConfiguration register() throws RemoteConfigurationException;
	
	public RemoteConfiguration register(String id) throws RemoteConfigurationException;

}

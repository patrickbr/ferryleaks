package com.algebraweb.editor.client;

import org.apache.commons.configuration.ConfigurationException;

import com.algebraweb.editor.client.logicalcanvas.RemoteConfigurationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("register")

public interface RegistrationService extends RemoteService {
	
		
	public void keepAlive();
	public RemoteConfiguration register() throws RemoteConfigurationException;
	
	

}

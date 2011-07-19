package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("register")

public interface RegistrationService extends RemoteService {
	
		
	public Configuration register();
	public void keepAlive();
	
	

}

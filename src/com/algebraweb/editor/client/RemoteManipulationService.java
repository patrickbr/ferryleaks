package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("manipulate")

public interface RemoteManipulationService extends RemoteService {
	
	
	public RemoteManipulationMessage deleteNode(int nid, int planid);
	
	

}

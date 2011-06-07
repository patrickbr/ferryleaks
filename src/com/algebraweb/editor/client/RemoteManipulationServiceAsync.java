package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteManipulationServiceAsync {

	void deleteNode(int nid, int planid, AsyncCallback<RemoteManipulationMessage> callback);
	
	

}

package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegistrationServiceAsync {

	void register(AsyncCallback<Configuration> callback);

	void keepAlive(AsyncCallback<Void> callback);
		

}

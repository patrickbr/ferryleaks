package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegistrationServiceAsync {
	void keepAlive(AsyncCallback<Void> callback);
	void register(AsyncCallback<Configuration> callback);
}

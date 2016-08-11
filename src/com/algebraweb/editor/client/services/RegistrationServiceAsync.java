package com.algebraweb.editor.client.services;

import com.algebraweb.editor.client.RemoteConfiguration;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * (See RegistrationService)
 *
 * @author Patrick Brosi
 *
 */
public interface RegistrationServiceAsync {
	void keepAlive(AsyncCallback<Void> callback);
	void register(AsyncCallback<RemoteConfiguration> callback);
	void register(String id, AsyncCallback<RemoteConfiguration> callback);
}
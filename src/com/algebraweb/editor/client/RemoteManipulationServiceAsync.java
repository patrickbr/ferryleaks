package com.algebraweb.editor.client;

import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteManipulationServiceAsync {

	void deleteNode(int nid, int planid, AsyncCallback<RemoteManipulationMessage> callback);

	void getValidation(int planid, AsyncCallback<ValidationResult> callback);

	void getNodeInformationHTML(int nid, int planid,
			AsyncCallback<String> callback);
	
	

}

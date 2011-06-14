package com.algebraweb.editor.client;

import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("manipulate")

public interface RemoteManipulationService extends RemoteService {
	
	
	public RemoteManipulationMessage deleteNode(int nid, int planid);
	
	public ValidationResult getValidation(int planid);
	
	public String getNodeInformationHTML(int nid, int planid);

}

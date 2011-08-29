package com.algebraweb.editor.client.exampleplanloadre;

import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("example")
public interface ExamplePlanLoaderCommunicationService extends RemoteService {
		
	public Integer[] loadExamplePlan(String fileName) throws RemoteIOException;

}
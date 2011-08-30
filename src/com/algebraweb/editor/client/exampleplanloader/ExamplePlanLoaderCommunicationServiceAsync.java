package com.algebraweb.editor.client.exampleplanloader;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * (see ExamplePlanLoaderCommunicationService)
 * 
 * @author Patrick Brosi
 *
 */
public interface ExamplePlanLoaderCommunicationServiceAsync {

	void loadExamplePlan(String fileName, AsyncCallback<Integer[]> callback);

}

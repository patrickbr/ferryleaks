package com.algebraweb.editor.client.exampleplanloadre;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExamplePlanLoaderCommunicationServiceAsync {

	void loadExamplePlan(String fileName, AsyncCallback<Integer[]> callback);

}

package com.algebraweb.editor.client.graphcanvas;


import com.google.gwt.user.client.rpc.AsyncCallback;


public abstract class GraphCanvasCommunicationCallback<T> implements AsyncCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		
		new GraphCanvasErrorDialogBox("<span style='font-weight:bold;color:red'>Could not save node.</span> Reason was:<br><br>" + caught.getMessage());
		
	}

	@Override
	public abstract void onSuccess(T result);

}

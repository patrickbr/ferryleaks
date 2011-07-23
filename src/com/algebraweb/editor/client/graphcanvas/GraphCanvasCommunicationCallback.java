package com.algebraweb.editor.client.graphcanvas;


import com.google.gwt.user.client.rpc.AsyncCallback;


public abstract class GraphCanvasCommunicationCallback<T> implements AsyncCallback<T> {

	
	private String whileString;
	
	
	public GraphCanvasCommunicationCallback(String errorWhileString) {
		this.whileString = errorWhileString;
	}
	
	@Override
	public void onFailure(Throwable caught) {
	
		new GraphCanvasErrorDialogBox("<span style='font-weight:bold;color:red'>Error while " + whileString + "!</span><br><br>" + caught.getMessage());
		
	}

	@Override
	public abstract void onSuccess(T result);

}

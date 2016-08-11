package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An extension to GWT's standard AsyncCallback class. Already ships with a
 * standard error handling mechanism.
 *
 * @author Patrick Brosi
 *
 * @param <T>
 *            the type of the RPC answer
 */
public abstract class EditorCommunicationCallback<T> implements
		AsyncCallback<T> {
	private String whileString;

	public EditorCommunicationCallback(String errorWhileString) {
		this.whileString = errorWhileString;
	}

	@Override
	public void onFailure(Throwable caught) {
		GraphCanvas.hideLoading();
		new GraphCanvasErrorDialogBox(
				"<span style='font-weight:bold;color:red'>Error while "
						+ whileString + "!</span><br><br>"
						+ caught.getMessage());
	}

	@Override
	public abstract void onSuccess(T result);
}
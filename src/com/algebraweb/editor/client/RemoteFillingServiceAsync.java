package com.algebraweb.editor.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteFillingServiceAsync {
	
	public void getRawNodes(int graphId,AsyncCallback<ArrayList<RawNode>> callback);

	public void getRawEdges(int graphId,AsyncCallback<ArrayList<RawEdge>> callback);
	

}

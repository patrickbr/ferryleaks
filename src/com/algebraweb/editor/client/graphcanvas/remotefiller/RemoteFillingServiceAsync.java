package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.ArrayList;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteFillingServiceAsync {
	
	public void getRawNodes(int graphId,AsyncCallback<ArrayList<RawNode>> callback);

	public void getRawEdges(int graphId,AsyncCallback<ArrayList<RawEdge>> callback);
	

}

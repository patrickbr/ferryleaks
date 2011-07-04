package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.ArrayList;

import com.algebraweb.editor.client.RawNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteFillingServiceAsync {
	
	void getRawNodes(String filler, String args,
			AsyncCallback<ArrayList<RawNode>> callback);


}

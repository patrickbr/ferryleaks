package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.List;

import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteFillingServiceAsync {

	void getRawNodes(String filler, String args,
			AsyncCallback<List<RawNode>> callback);
}

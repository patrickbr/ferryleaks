package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteSorterServiceAsync {

	public void doSort(String sorter, List<RawNode> nodes,
			AsyncCallback<Map<Integer, Tuple>> callback);

}

package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteSorterServiceAsync {
		
	public void doSort(String sorter,List<RawNode> nodes, AsyncCallback<Map<Integer,Coordinate>> callback);

}

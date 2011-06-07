package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteSorterServiceAsync {
		
	public void doSort(String sorter,ArrayList<RawNode> nodes, AsyncCallback<HashMap<Integer,Coordinate>> callback);

}

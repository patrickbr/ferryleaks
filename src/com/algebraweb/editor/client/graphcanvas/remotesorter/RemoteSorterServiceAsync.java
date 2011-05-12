package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteSorterServiceAsync {
		
	public void doSort(ArrayList<RawNode> nodes, ArrayList<RawEdge> edges,  AsyncCallback<HashMap<Integer,Coordinate>> callback);

}

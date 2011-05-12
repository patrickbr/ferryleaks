package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorterService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteSorter extends RemoteServiceServlet implements RemoteSorterService  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -284758450069045235L;
	
	public RemoteSorter() {
		
		
	}

	@Override
	public HashMap<Integer, Coordinate> doSort(ArrayList<RawNode> nodes,ArrayList<RawEdge> edges) {
		
		
		
		//for testing purposes
		
		System.out.println("Sorting...");
		
		CircleSorter cs = new CircleSorter();
		
		return cs.getCoordinateHashMap(nodes, null);

		
		
	}

}

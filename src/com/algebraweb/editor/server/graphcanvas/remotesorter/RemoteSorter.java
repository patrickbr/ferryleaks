package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;

public interface RemoteSorter {
	
	public HashMap<Integer,Coordinate> getCoordinateHashMap(ArrayList<RawNode> nodes,ArrayList<RawEdge> edges);
	

}

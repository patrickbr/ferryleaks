package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;


public interface RemoteSorter {
	
	public HashMap<Integer,Coordinate> getCoordinateHashMap(ArrayList<RawNode> nodes);
	

}

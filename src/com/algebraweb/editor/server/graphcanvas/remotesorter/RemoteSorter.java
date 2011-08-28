package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.RemoteIOException;


public interface RemoteSorter {
	
	public Map<Integer,Coordinate> getCoordinateHashMap(List<RawNode> nodes) throws RemoteIOException;
	

}

package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.node.RawNode;

public interface RemoteSorter {

	public Map<Integer, Tuple> getCoordinateHashMap(List<RawNode> nodes)
			throws RemoteIOException;

}

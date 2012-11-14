package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sorter")
public interface RemoteSorterService extends RemoteService {

	public Map<Integer, Tuple> doSort(String sorter, List<RawNode> nodes)
			throws RemoteIOException;

}

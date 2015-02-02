package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.List;

import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("communication")
public interface RemoteFillingService extends RemoteService {
	public List<RawNode> getRawNodes(String filler, String args);
}
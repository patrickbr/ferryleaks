package com.algebraweb.editor.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("communication")

public interface RemoteFillingService extends RemoteService  {
	
	public ArrayList<RawNode> getRawNodes(int graphId);
	
	public ArrayList<RawEdge> getRawEdges(int graphId);

}

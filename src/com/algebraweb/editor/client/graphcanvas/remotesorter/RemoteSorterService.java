package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;


import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceRelativePath("sorter")

public interface RemoteSorterService extends RemoteService{
	
	
	public HashMap<Integer,Coordinate> doSort(String sorter,ArrayList<RawNode> nodes);
	

}

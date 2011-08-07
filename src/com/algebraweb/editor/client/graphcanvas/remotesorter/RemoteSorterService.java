package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.RemoteIOException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sorter")

public interface RemoteSorterService extends RemoteService{
	
	
	public HashMap<Integer,Coordinate> doSort(String sorter,ArrayList<RawNode> nodes) throws RemoteIOException;
	

}

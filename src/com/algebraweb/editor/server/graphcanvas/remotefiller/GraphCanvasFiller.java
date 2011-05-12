package com.algebraweb.editor.server.graphcanvas.remotefiller;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;


public interface GraphCanvasFiller {
	
	
	public RawNode nextNode();
	
	public boolean hasNextNode();
	
	public RawEdge nextEdge();
	
	public boolean hasNextEdge();
	
	public void init();
	
	

}

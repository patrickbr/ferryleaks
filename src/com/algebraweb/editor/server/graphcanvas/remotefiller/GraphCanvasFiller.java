package com.algebraweb.editor.server.graphcanvas.remotefiller;

import com.algebraweb.editor.client.RawNode;


public interface GraphCanvasFiller {
	
	
	public boolean hasNextEdge();
	
	public boolean hasNextNode();
	
	public void init();
	
	public RawNode nextNode();
	
	

}

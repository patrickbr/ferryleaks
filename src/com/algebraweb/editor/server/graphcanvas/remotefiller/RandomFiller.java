package com.algebraweb.editor.server.graphcanvas.remotefiller;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;


public class RandomFiller implements GraphCanvasFiller {
	
	
	private int howManyNodes = 10;
	private int count=0;
	private int edgeCount =0;

	@Override
	public boolean hasNextEdge() {
		
		return (edgeCount < 7);
	}

	@Override
	public boolean hasNextNode() {
		howManyNodes--;
		return (howManyNodes > -1);
	}

	@Override
	public RawEdge nextEdge() {
		
				
		int toid = 1+((int)(( Math.random() * (count))));
		int fromid = 1+((int)(( Math.random() * (count))));
		
		if (toid != fromid) {
			edgeCount++;
			return new RawEdge(toid,fromid);
				
		}else{
			return nextEdge();
		}
	}

	@Override
	public RawNode nextNode() {
		
		count++;
		
		return new RawNode(count, "Node #" + count, "black",(int)(70+Math.random()*100),(int)(20 + Math.random() * 40));
		
	
	}

	@Override
	public void init() {
		
		
		howManyNodes = 10;
		count=0;
	    edgeCount =0;
		
		
	}

}

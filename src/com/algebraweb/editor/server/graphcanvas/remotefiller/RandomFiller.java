package com.algebraweb.editor.server.graphcanvas.remotefiller;


import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;


public class RandomFiller implements GraphCanvasFiller {
	
	
	private int howManyNodes = 80;
	private int count=0;
	private int edgeCount =0;

	@Override
	public boolean hasNextEdge() {
		
		return (edgeCount < 100);
	}

	@Override
	public boolean hasNextNode() {
		howManyNodes--;
		return (howManyNodes > -1);
	}

	public int getTo(int fromid) {
		
				
		int toid = 1+((int)(( Math.random() * (howManyNodes-1))));

		
		if (toid != fromid) {
			edgeCount++;
			
			System.out.println("edge!");
			return toid;
			
				
		}else{
			return getTo(fromid) ;
		}
	
	}

	@Override
	public RawNode nextNode() {
		
		count++;
		
	    int c = (int)(Math.random() * 0xFFFFFF);
	    
	    System.out.println(Integer.toHexString(c));
			
		RawNode n = new RawNode(count, "Node #" + count, c,(int)(40+Math.random()*60),(int)(10 + Math.random() * 20));
		
		n.getEdgesToList().add(new RawEdge(getTo(n.getNid()),n.getNid()));
		
		return n;
	
	}

	@Override
	public void init() {
		
		
		howManyNodes = 80;
		count=0;
	    edgeCount =0;
		
		
	}

}

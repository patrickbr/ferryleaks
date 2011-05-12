package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.SimpleSorter;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;

public class GraphCanvasRemoteFillingMachine {
	
	/**
	 * A filling machine for the GraphCanvas. Takes a GraphCanvasFiller-object 
	 * and puts its content on the canvas.
	 */
	
	
	private GraphCanvas c;
	
	
	public GraphCanvasRemoteFillingMachine(GraphCanvas graphCanvas) {
		
		this.c = graphCanvas;
		
	}
	
	
	public void fillWith(ArrayList<RawNode> nodes, ArrayList<RawEdge> edges) {
		
		c.clear();
		
		Iterator<RawNode> ni = nodes.iterator();
		Iterator<RawEdge> ne = edges.iterator();
		
		while (ni.hasNext()) {
			
			RawNode current = ni.next();
			c.addNode(current.getNid(),current.getColor(),current.getWidth(), current.getHeight(), current.getText());
			
		}
		
		while (ne.hasNext()) {
			
			RawEdge current = ne.next();
			c.createEdge(c.getGraphNodeById(current.getFrom()), c.getGraphNodeById(current.getTo()));
		}
		
		c.sort(new SimpleSorter());
		
		
	}
	
	
	public void fill(RemoteFiller filler) {
		
		filler.init(this);
				
		
	}
	
	

}

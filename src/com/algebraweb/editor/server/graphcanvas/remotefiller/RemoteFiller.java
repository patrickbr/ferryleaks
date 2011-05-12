package com.algebraweb.editor.server.graphcanvas.remotefiller;

import java.util.ArrayList;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteFiller extends RemoteServiceServlet implements RemoteFillingService {
	
		

	/**
	 * 
	 */
	private static final long serialVersionUID = -7762896385918482810L;
	
	
	// for testing...
	private GraphCanvasFiller filler = new RandomFiller();
	
	
	public RemoteFiller() {
		
		
		
	}

	@Override
	public ArrayList<RawEdge> getRawEdges(int graphId) {

		ArrayList<RawEdge> es = new ArrayList<RawEdge>();
		
				
		while (filler.hasNextEdge()) {
			
			es.add(filler.nextEdge());
			
		}

		return es;

	}
	
	
	@Override
	public ArrayList<RawNode> getRawNodes(int graphId) {
		
		ArrayList<RawNode> ns = new ArrayList<RawNode>();
		
		filler.init();

		while (filler.hasNextNode()) {
			
			ns.add(filler.nextNode());
			
		}
		
		return ns;

	}

}

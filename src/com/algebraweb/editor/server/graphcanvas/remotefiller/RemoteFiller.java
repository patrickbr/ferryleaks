package com.algebraweb.editor.server.graphcanvas.remotefiller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;


import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingService;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanFiller;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanLoader;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteFiller extends RemoteServiceServlet implements RemoteFillingService {
	
		

	/**
	 * 
	 */
	private static final long serialVersionUID = -7762896385918482810L;
	
	
	// for testing...
	private GraphCanvasFiller filler;
	
	
	public RemoteFiller() {
				
		
	}
	
	
	@Override
	public ArrayList<RawNode> getRawNodes() {
				
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		
		ArrayList<RawNode> ns = new ArrayList<RawNode>();
		
		filler = new XMLPlanFiller(session,this.getServletContext());
		
		filler.init();

		while (filler.hasNextNode()) {
			
			ns.add(filler.nextNode());
			
		}
		
		return ns;

	}



}

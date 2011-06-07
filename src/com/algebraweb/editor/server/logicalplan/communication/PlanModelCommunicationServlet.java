package com.algebraweb.editor.server.logicalplan.communication;

import com.algebraweb.editor.client.RemoteManipulationMessage;
import com.algebraweb.editor.client.RemoteManipulationService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Provides all basic plan manipulation methods like adding and deleting nodes, creating edges etc.
 * @author Patrick Brosi
 *
 */

public class PlanModelCommunicationServlet extends RemoteServiceServlet implements RemoteManipulationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3178031477829024689L;

	@Override
	public RemoteManipulationMessage deleteNode(int nid, int planid) {
		

		//TODO
		
		
		return null;
	}
	
	
	
	
	
	
	
	
	

}

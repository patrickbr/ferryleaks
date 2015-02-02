package com.algebraweb.editor.server.graphcanvas.remotefiller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingService;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanFiller;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Servlet for the remote filling class
 * 
 * @author Patrick Brosi
 * 
 */
public class RemoteFiller extends RemoteServiceServlet implements
		RemoteFillingService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7762896385918482810L;

	public RemoteFiller() {
	}

	@Override
	public List<RawNode> getRawNodes(String fillingMachine, String args) {
		HttpSession session = this.getThreadLocalRequest().getSession(true);
		List<RawNode> ns = new ArrayList<RawNode>();
		final GraphCanvasFiller filler;

		if (fillingMachine.equals("xml")) {
			filler = new XMLPlanFiller(session, this.getServletContext(),
					Integer.parseInt(args));
		} else {
			filler = new RandomFiller();
		}
		filler.init();

		while (filler.hasNextNode()) {
			ns.add(filler.nextNode());
		}
		return ns;
	}
}
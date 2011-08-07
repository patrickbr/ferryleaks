package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.configuration.Configuration;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorterService;
import com.algebraweb.editor.client.logicalcanvas.RemoteIOException;
import com.algebraweb.editor.server.graphcanvas.remotesorter.dotsorter.DotSorter;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteSortingServlet extends RemoteServiceServlet implements RemoteSorterService  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -284758450069045235L;
	
	public RemoteSortingServlet() {
		
		
	}

	@Override
	public HashMap<Integer, Coordinate> doSort(String sorter,ArrayList<RawNode> nodes) throws RemoteIOException {
			
		RemoteSorter cs = new InlineSorter();
		
		
		if (sorter.equals("dot")) {
			
			Configuration c = (Configuration) getServletContext().getAttribute("configuration");
			String dotPath = c.getString("server.dot.path");
			String dotArgs = c.getString("server.dot.args");
			double dotCorr = c.getDouble("server.dot.corrector");
			
			cs = new DotSorter(dotPath, dotArgs, dotCorr);
		}
		
		if (sorter.equals("circle")) cs = new CircleSorter();
		if (sorter.equals("inline")) cs = new InlineSorter();
		
		return cs.getCoordinateHashMap(nodes);

				
	}

}

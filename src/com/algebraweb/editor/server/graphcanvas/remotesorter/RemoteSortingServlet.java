package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorterService;
import com.algebraweb.editor.server.graphcanvas.remotesorter.dotsorter.DotSorter;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteSortingServlet extends RemoteServiceServlet implements
		RemoteSorterService {
	/**
	 *
	 */
	private static final long serialVersionUID = -284758450069045235L;

	public RemoteSortingServlet() {

	}

	@Override
	public Map<Integer, Tuple> doSort(String sorter, List<RawNode> nodes)
			throws RemoteIOException {

		RemoteSorter cs = new InlineSorter();
		if (sorter.equals("dot")) {
			Configuration c = (Configuration) getServletContext().getAttribute(
					"configuration");
			String dotPath = c.getString("server.dot.path");
			String dotArgs = c.getString("server.dot.args");
			double dotCorr = c.getDouble("server.dot.corrector");
			cs = new DotSorter(dotPath, dotArgs, dotCorr);
		}

		if (sorter.equals("circle")) {
			cs = new CircleSorter();
		}
		if (sorter.equals("inline")) {
			cs = new InlineSorter();
		}

		return cs.getCoordinateHashMap(nodes);
	}
}
package com.algebraweb.editor.client.graphcanvas;

import java.util.List;

public interface GraphSorter {

	/**
	 * Sort the given nodes with the given edges.
	 * 
	 * @param nodes
	 *            the list of nodes
	 * @param edges
	 *            the list of edges
	 * @param cb
	 *            the callback to call after completion
	 */
	public void doSort(List<GraphNode> nodes, List<GraphEdge> edges,
			GraphManipulationCallback cb);

}

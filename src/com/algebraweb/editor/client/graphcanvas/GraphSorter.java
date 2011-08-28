package com.algebraweb.editor.client.graphcanvas;

import java.util.List;



public interface GraphSorter {
		
	public void doSort(List<GraphNode> nodes,List<GraphEdge> edges, GraphManipulationCallback cb);
	
}

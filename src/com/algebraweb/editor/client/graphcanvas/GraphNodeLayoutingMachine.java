package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Provides animated layouting after sorting by a GraphSorter
 * @author Patrick Brosi
 *
 */

public class GraphNodeLayoutingMachine {


	protected ArrayList<GraphNode> nodes;
	protected GraphNodeModifier gnm;

	private static int offsetX = 600;
	private static int offsetY = 500;
	
	
	public GraphNodeLayoutingMachine(GraphNodeModifier gnm) {

		this.gnm = gnm;

	}

	/**
	 * Sort the given nodes with the GraphSorter sorter
	 * @param nodes
	 * @param sorter
	 */

	public void sortGraph(ArrayList<GraphNode> nodes,GraphSorter sorter) {

		this.nodes=nodes;

		Iterator<GraphNode> i = nodes.iterator();

		GraphNode current;

		while(i.hasNext()) {

			current = i.next();
			gnm.hideEdges(current);

		}			

		sorter.doSort(nodes);
		Iterator<GraphNode> a = nodes.iterator();

		while(a.hasNext()) {

			current = a.next();

			gnm.animateTo(current,current.getX()+offsetX, current.getY()+offsetY);

		}
	}

}

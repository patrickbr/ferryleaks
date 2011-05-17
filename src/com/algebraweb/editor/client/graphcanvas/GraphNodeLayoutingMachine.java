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
	protected ArrayList<GraphEdge> edges;
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

	public void sortGraph(ArrayList<GraphNode> nodes,ArrayList<GraphEdge> edges,GraphSorter sorter) {

		this.nodes=nodes;
		this.edges=edges;
		
		Iterator<GraphNode> i = nodes.iterator();

		GraphNode current;

		while(i.hasNext()) {

			current = i.next();
			gnm.hideEdges(current);

		}		
		
		
		//TODO: lock screen, show something like "sorting"...
		

		sorter.doSort(nodes,edges, new GraphManipulationCallback() {

			@Override
			public void onComplete() {
				
				Iterator<GraphNode> a = GraphNodeLayoutingMachine.this.nodes.iterator();

				
				while(a.hasNext()) {

					GraphNode current = a.next();

					gnm.animateTo(current,current.getX()+offsetX, current.getY()+offsetY);

				}
				
				((DragPanel)gnm.getCanvas().getParent()).center(gnm.getCanvas().getWidth(),gnm.getCanvas().getHeight());
				
				
			}
		});
				
	
	}
	

}

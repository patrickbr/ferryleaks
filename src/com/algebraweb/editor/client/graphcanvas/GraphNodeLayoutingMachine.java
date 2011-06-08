package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


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
		
		
		
		GWT.log("Layouting...");
		GraphCanvas.showLoading("Layouting...");
		

		sorter.doSort(nodes,edges, new GraphManipulationCallback() {

			@Override
			public void onComplete() {
				
				GraphCanvas.hideLoading();
				layout(GraphNodeLayoutingMachine.this.nodes);
							
			}
			
		});
			
	}
	
	public void layout(ArrayList<GraphNode> nodes) {
		
		
		Iterator<GraphNode> a = nodes.iterator();
		
		int width=0;
		int height=0;
		
		int negW=0;
		int negH=0;
		
		while(a.hasNext()) {
			
			GraphNode current = a.next();
			
			if (current.getX() < negW) {
				negW = (int)current.getX();
			}
			if (current.getY() < negW) {
				negH = (int)current.getY();
			}
			
			if (current.getX() + Math.abs(negW) > width) width =  Math.abs(negW) + (int) current.getX();
			if (current.getY() + Math.abs(negH) > height) height =  Math.abs(negH) + (int) current.getY();
			
		}
		
		int newH = Window.getClientHeight()-30;
		int newW = Window.getClientWidth()-30;
	
		
		if (height/gnm.getCanvas().getScale() > newH) newH= (int)(height/gnm.getCanvas().getScale());
		if (width/gnm.getCanvas().getScale() > newW) newW= (int)(width/gnm.getCanvas().getScale());
		
		gnm.getCanvas().setSize(newW,newH);

		/**
		offsetX =(gnm.getCanvas().getWidth()/2) - (Window.getClientWidth()/2);
		offsetY =(gnm.getCanvas().getHeight()/2) - (Window.getClientHeight()/2);
			
		offsetX = (int)(gnm.getCanvas().getScale() * offsetX);
		offsetY = (int)(gnm.getCanvas().getScale() * offsetY);
		
		**/
		
		offsetX=0;
		offsetY=0;
		
		a = GraphNodeLayoutingMachine.this.nodes.iterator();
		
		
		while(a.hasNext()) {

			GraphNode current = a.next();
			gnm.animateTo(current,current.getX()+offsetX, current.getY()+offsetY);

		}
		
		((DragPanel)gnm.getCanvas().getParent()).center(gnm.getCanvas().getWidth(),gnm.getCanvas().getHeight());
	}
	

}

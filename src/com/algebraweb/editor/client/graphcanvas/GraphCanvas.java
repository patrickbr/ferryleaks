package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.DOM;
import com.hydro4ge.raphaelgwt.client.Raphael;

/**
 * A widget to draw directed graphs.
 * @author Patrick Brosi
 *
 */

public class GraphCanvas extends Raphael  {


	private GraphNode dragNode = null;
	private int dragOffsetX = 0;
	private int dragOffsetY = 0;
	private double scale = 1;

	private GraphNodeModifier gnm;
	private GraphEdgeModifier gem;

	private GraphNode selected = null;


	private int height;
	private int width;


	private ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
	private ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();

	public GraphCanvas(int width, int height) {
		
		super(width, height);

		this.height=height;
		this.width=width;

		gnm = new GraphNodeModifier(this);
		gem = new GraphEdgeModifier(this);
	}


	/**
	 * Returns the current height of the canvas
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the current width of the canvas
	 */

	public int getWidth() {
		return width;
	}

	/**
	 * Returns the GraphNodeModifier of this canvas
	 */

	public GraphNodeModifier getGraphNodeModifier() {
		return gnm;
	}

	/**
	 * Return the GragEdgeModifier of this canvas
	 */

	public GraphEdgeModifier getGraphEdgeModifier() {
		return gem;
	}

	/**
	 * Set the GraphNode n selected
	 * @param n
	 */

	public void setSelectedNode(GraphNode n) {

		this.selected = n;

	}

	/**
	 * Unset the GraphNode n selected
	 * @param n
	 */

	public GraphNode getSelectedNode() {

		return selected;
	}


	/**
	 * Sets the size of the canvas to the given dimension
	 * @param x
	 * @param y
	 */

	public void setSize(int x, int y) {

		this.height=y;
		this.width=x;
		updateZoom();
		this.overlay().setSize(width, height);

	}

	/**
	 * Sets the width of the canvas
	 * @param x
	 */

	public void setWidth(int x) {

		this.width=x;
		this.overlay().setSize(width, height);
		this.setWidth(x + "px");
		updateZoom();

	}

	/**
	 * Sets the height ot the canvas
	 * @param y
	 */

	public void setHeight(int y) {

		this.height=y;
		this.overlay().setSize(width, height);
		this.setHeight(y + "px");
		updateZoom();

	}

	public void onLoad() {

		super.onLoad();

		this.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				if (GraphCanvas.this.dragNode != null) {
					DOM.eventGetCurrentEvent().preventDefault();
					double x =scale * (event.getRelativeX(GraphCanvas.this.getElement())-dragOffsetX);
					double y = scale *(event.getRelativeY(GraphCanvas.this.getElement())-dragOffsetY);
					gnm.moveTo(dragNode,x,y);
				}

			}

		}, MouseMoveEvent.getType());

		this.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				GraphCanvas.this.clearDrag();
			}

		}, MouseUpEvent.getType());


		this.addDomHandler(new MouseWheelHandler() {

			@Override
			public void onMouseWheel(MouseWheelEvent event) {

				DOM.eventGetCurrentEvent().preventDefault();

				if (event.isNorth()) zoom(((1 / getScale()) * 100) + 10);
				if (event.isSouth()) zoom(((1 / getScale()) * 100) - 10);

			}

		}, MouseWheelEvent.getType());
	}

	/**
	 * Zoom to percent
	 * @param percent
	 */

	public void zoom (double percent) {

		scale = 100/percent;
		updateZoom();

	}


	private void updateZoom() {

		this.getElement().getFirstChildElement().setAttribute("viewBox", "0 0 " + (int)(this.width * scale) + " " +  (int)(this.height * scale));

	}

	/**
	 * Returns the current scale. For example, zooming to 50% will make this
	 * return 2.
	 * @return
	 */

	public double getScale() {
		return scale;
	}


	/**
	 * Draw an edge from node "from" to node "to"
	 * @param from
	 * @param to
	 */

	public void createEdge(GraphNode from, GraphNode to) {

		this.edges.add(new GraphEdge(this,from,to));
	}


	protected void registerDrag(GraphNode n,int offsetX,int offsetY) {

		this.dragNode = n;
		this.dragOffsetX=offsetX-5;
		this.dragOffsetY=offsetY-5;

	}


	private void clearDrag() {

		this.dragNode = null;

	}

	/**
	 * Adds a new node to the canvas at (x,y).
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param text
	 * @return
	 */

	public GraphNode addNode(int id,int x, int y,int width, int height,String text) {

		GraphNode g =  new GraphNode(this,x,y, width, height,text,id);

		this.nodes.add( g);

		//add dom handlers for node dragging

		g.getShape().addDomHandler(GraphNodeModifier.mouseMoveHandlerBuilder(g), MouseMoveEvent.getType());
		g.getText().addDomHandler(GraphNodeModifier.mouseMoveHandlerBuilder(g), MouseMoveEvent.getType());

		g.getShape().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g), MouseOutEvent.getType());
		g.getText().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g), MouseOutEvent.getType());

		return g;

	}
	
	
	public GraphNode addNode(int id,int width, int height,String text) {

		int x = this.width / 2;
		int y = this.height / 2;
		
		return this.addNode(id,x,y,width,height,text);

	}

	/**
	 * Deletes a node from the canvas. Edges comming from or going to this node
	 * will be removed.
	 * @param n
	 */

	public void deleteNode(GraphNode n) {

		ArrayList<GraphEdge> to = n.getEdgesTo();
		ArrayList<GraphEdge> from = n.getEdgesFrom();

		Iterator<GraphEdge> it = to.iterator();
		GraphEdge current;

		while (it.hasNext()) {

			current = it.next();			
			gem.snakeIn(current);
			gem.deleteFromFrom(current);

		}

		it = from.iterator();

		while (it.hasNext()) {

			current = it.next();			
			gem.snakeIn(current);
			gem.deleteFromTo(current);

		}	

		gnm.dimOut(n);
		gnm.kill(n);
		nodes.remove(n);

	}

	/**
	 * Returns a flat list of all nodes on the canvas
	 * @return
	 */

	public ArrayList<GraphNode> getNodes() {
		return this.nodes;
	}

	/**
	 * Sort this canvas with GraphSorter s
	 * @param s
	 */

	public void sort(GraphSorter s) {

		GraphNodeLayoutingMachine m = new GraphNodeLayoutingMachine(gnm);
		m.sortGraph(nodes, s);

	}
	
	
	public GraphNode getGraphNodeById(int id) throws NodeNotFoundException{
		
		Iterator<GraphNode> i = nodes.iterator();
		
		while(i.hasNext()) {
			
			GraphNode current = i.next();
			if (current.getId() == id) return current;
				
		}
		
		throw new NodeNotFoundException(id);
		
	}
	
	public void clear() {
		
		this.nodes.clear();
		this.edges.clear();
		super.clear();		
		
	}
	
	public void showEdges() {
		
	Iterator<GraphNode> i = nodes.iterator();
		
		while(i.hasNext()) {
		
			this.getGraphNodeModifier().showEdges(i.next());
			
				
		}
		
	}
	



}

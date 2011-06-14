package com.algebraweb.editor.client.graphcanvas;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.hydro4ge.raphaelgwt.client.Raphael.Rect;
import com.hydro4ge.raphaelgwt.client.Raphael.Shape;
import com.hydro4ge.raphaelgwt.client.Raphael.Text;

/**
 * A node on the GraphCanvas
 * @author Patrick Brosi
 *
 */

public class GraphNode {

	private Rect rect;
	private Text text;
	private GraphCanvas c;
	private ArrayList<GraphEdge> edgesTo = new ArrayList<GraphEdge>();
	private ArrayList<GraphEdge> edgesFrom = new ArrayList<GraphEdge>();
	
	private HashMap<String,ConnectedShape> connectedShapes = new HashMap<String,ConnectedShape>();
	
	private String textString;

	private int width = 200;
	private int height = 50;
	private double x;
	private double y;
	private boolean aniLock=false;

	private boolean isDragged = false;
	
	private int id;

	public double stepX;
	public double stepY;
	private int color;

	public GraphNode(GraphCanvas c, int color,int x, int y,int width, int height, String textStr,int id) {

		this.id=id;
		this.c=c;
		this.height = height;
		this.width = width;
		this.color=color;
		
		this.x=x;
		this.y=y;

		this.rect = c.new Rect(x,y,width,height,5);
		rect.getElement().setAttribute("class", "node");
		
		textString = textStr;

		text = c.new Text(x + width/2,y+height/2,textStr);
		this.text.attr("text-anchor","left");

		text.getElement().setAttribute("class", "node-text");
		
		rect.getElement().setAttribute("r", "");
		
		
		
		String cString = Integer.toHexString(0x1000000 + color).substring(1, 7);
				
		rect.attr("fill", "#"+cString);
		rect.attr("stroke", "#555");
		text.attr("fill", "#000");

		
		MouseMoveHandler mouseMoveH = new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				
								
				GraphNode.this.c.openPopUp(Window.getScrollLeft() + event.getClientX(), Window.getScrollTop() + event.getClientY(),GraphNode.this.getId(),400);
				
				
			}

		};
		
		

		MouseDownHandler mouseDownH = new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				
				GraphNode.this.getShape().toFront();
				GraphNode.this.text.toFront();
							
				GraphNode.this.c.getGraphNodeModifier().edgesToFront(GraphNode.this);

				if (GraphNode.this.c.getSelectedNode() != null)	GraphNode.this.c.getGraphNodeModifier().setNotSelected(GraphNode.this.c.getSelectedNode());
				GraphNode.this.c.setSelectedNode(GraphNode.this);
				GraphNode.this.c.getGraphNodeModifier().setSelected(GraphNode.this);

				double rx;
				double ry;
				
				rx=event.getRelativeX(GraphNode.this.c.getElement()) -(1/GraphNode.this.c.getScale()* GraphNode.this.getX()) ;
				ry=event.getRelativeY(GraphNode.this.c.getElement()) -(1/GraphNode.this.c.getScale()*GraphNode.this.getY()) ;
				
				
				
				
				
				GraphNode.this.setDragged(true);
				GraphNode.this.c.registerDrag(GraphNode.this,(int)rx,(int)ry);
			}

		};

		text.sinkEvents(Event.MOUSEEVENTS);
		
		
		getShape().addDomHandler(mouseDownH, MouseDownEvent.getType());
		text.addDomHandler(mouseDownH, MouseDownEvent.getType());
		getShape().addDomHandler(mouseMoveH, MouseMoveEvent.getType());
		text.addDomHandler(mouseMoveH, MouseMoveEvent.getType());
		
		
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
	
	public HashMap<String, ConnectedShape> getConnectedShapes() {
		return connectedShapes;
	}

	public int getId() {
		return id;
	}

	public ArrayList<GraphEdge> getEdgesFrom() {
		return edgesFrom;
	}

	public ArrayList<GraphEdge> getEdgesTo() {
		return edgesTo;
	}

	public Text getText() {
		return text;
	}

	public int getWidth() {
		return width;
	}

	
	public int getHeight() {
		return height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x=x;
	}

	public void setY(double y) {
		this.y=y;
	}

	public void setAniLock() {
		this.aniLock=true;
	}

	public boolean aniLock() {
		return aniLock;
	}

	public void unsetAniLock() {
		this.aniLock=false;
	}

	public Shape getShape() {
		return this.rect;
	}

	public void addEdgeFrom(GraphEdge e) {
		this.edgesFrom.add(e);
	}

	public void addEdgeTo(GraphEdge e) {
		this.edgesTo.add(e);
	}

	public String getTextString() {
		return textString;
	}
	
	public boolean isDragged() {
		return isDragged;
	}

	public void setDragged(boolean isDragged) {
		this.isDragged = isDragged;
	}

}

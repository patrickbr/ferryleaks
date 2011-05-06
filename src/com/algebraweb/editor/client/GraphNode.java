package com.algebraweb.editor.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
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

	private int width = 200;
	private int height = 50;
	private double x;
	private double y;
	private boolean aniLock=false;

	private int id;

	public double stepX;
	public double stepY;

	public GraphNode(GraphCanvas c, int x, int y,int width, int height, String textStr,int id) {

		this.id=id;
		this.c=c;
		this.height = height;
		this.width = width;

		this.rect = c.new Rect(c.getWidth()/2,c.getHeight()/2,width,height,5);
		rect.getElement().setAttribute("class", "node");

		text = c.new Text(c.getWidth()/2 + width/2,c.getHeight()/2+height/2,textStr);
		this.text.attr("text-anchor","left");

		text.getElement().setAttribute("class", "node-text");

		rect.attr("fill", "#EEE");
		text.attr("fill", "#000");

		c.getGraphNodeModifier().animateTo(this,x,y);


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

				if (event.getSource().equals(text)) {

					rx=event.getRelativeX(GraphNode.this.rect.getElement());
					ry=event.getRelativeY(GraphNode.this.rect.getElement());

				}else{
					rx=event.getX();
					ry=event.getY();

				}

				GraphNode.this.c.registerDrag(GraphNode.this,(int)rx,(int)ry);
			}

		};

		getShape().addDomHandler(mouseDownH, MouseDownEvent.getType());
		text.addDomHandler(mouseDownH, MouseDownEvent.getType());

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


}

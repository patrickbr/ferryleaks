package com.algebraweb.editor.client.graphcanvas;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
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
	private Text[] textLines;
	private double lineHeight;
	private GraphCanvas c;
	private ArrayList<GraphEdge> edgesTo = new ArrayList<GraphEdge>();
	private ArrayList<GraphEdge> edgesFrom = new ArrayList<GraphEdge>();

	private HashMap<String,ConnectedShape> connectedShapes = new HashMap<String,ConnectedShape>();
	private HashMap<String,ConnectedWidget> connectedWidgets = new HashMap<String,ConnectedWidget>();



	private String textString;

	private int width = 200;
	private int height = 50;

	private int minWidth;
	private int minHeight;
	private double x;
	private double y;
	private boolean aniLock=false;

	private int fixedChildCount=-1;

	private boolean isDragged = false;

	private int id;

	public double stepX;
	public double stepY;
	private int color;

	public GraphNode(GraphCanvas c, int color,int x, int y, int width, int height, String textStr,int id) {

		this.id=id;
		this.c=c;

		this.color=color;

		this.width = width;
		this.height = height;
		
		this.minWidth = width;
		this.minHeight = height;

		this.x=x;
		this.y=y;

		this.rect = c.new Rect(x,y,width,height,5);
		rect.getElement().setAttribute("class", "node");
		rect.getElement().setAttribute("r", "");


		String cString = Integer.toHexString(0x1000000 + color).substring(1, 7);

		rect.attr("fill", "#"+cString);
		rect.attr("stroke", "#555");

		setText(textStr);

		getShape().addDomHandler(mouseDownH, MouseDownEvent.getType());
		getShape().addDomHandler(mouseMoveH, MouseMoveEvent.getType());
		getShape().addDomHandler(doubleC, DoubleClickEvent.getType());
		getShape().addDomHandler(contextMenuH, ContextMenuEvent.getType());


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

	public Text[] getText() {
		return textLines;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int w) {

		if (w>=minWidth) {
			width = w;
			rect.attr("width", width);
		}else{
			width = minWidth;
			rect.attr("width", width);
		}

	}

	public void setHeight(int h) {

		if (h>=minHeight) {
			height = h;
			rect.attr("height", height);
		}else{
			height = minHeight;
			rect.attr("height", height);
		}

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

	/**
	 * @return the fixedChildCount
	 */
	public int getFixedChildCount() {
		return fixedChildCount;
	}

	/**
	 * @param fixedChildCount the fixedChildCount to set
	 */
	public void setFixedChildCount(int fixedChildCount) {
		this.fixedChildCount = fixedChildCount;
	}





	/**
	 * @return the connectedWidgets
	 */
	public HashMap<String, ConnectedWidget> getConnectedWidgets() {
		return connectedWidgets;
	}

	/**
	 * @return the lineHeight
	 */
	public double getLineHeight() {
		return lineHeight;
	}

	public void setText(String txt) {

		boolean update=false;

		if (textLines != null) {

			update=true;
			for (Text line:textLines) {

				line.remove();

			}

		}


		String[] lines = txt.split("\\\\n");

		textLines = new Text[lines.length];

		int newWidth = 0;
		int textHeight = 0;

		int i=0;

		for (String line:lines) {

			Text cur = c.new Text(0,0,line);
			cur.getElement().setAttribute("class", "node-text node-text-line_" + i);
			cur.attr("text-anchor","left");
			cur.sinkEvents(Event.MOUSEEVENTS);

			if (cur.getBBox().width() > newWidth) newWidth = (int) cur.getBBox().width();
			if (cur.getBBox().height() > textHeight) textHeight = (int) cur.getBBox().height();

			textLines[i] = cur;


			cur.addDomHandler(mouseDownH, MouseDownEvent.getType());
			cur.addDomHandler(mouseMoveH, MouseMoveEvent.getType());
			cur.addDomHandler(doubleC, DoubleClickEvent.getType());


			cur.addDomHandler(GraphNodeModifier.mouseMoveHandlerBuilder(this), MouseMoveEvent.getType());
			cur.addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(this), MouseOutEvent.getType());
			cur.addDomHandler(contextMenuH, ContextMenuEvent.getType());


			i++;

		}

		lineHeight = textHeight;
		setWidth(newWidth+10);
		setHeight((((int)lineHeight) * textLines.length) + 10);

		i=0;

		for (Text text:textLines) {

			text.attr("x", getX() + getWidth()/2);
			text.attr("y", getY() + (textHeight/2) + 5 + (i*textHeight));

			i++;
		}

		if (update) c.getGraphNodeModifier().update(this, false, false);

	}

	private MouseMoveHandler mouseMoveH = new MouseMoveHandler() {

		@Override
		public void onMouseMove(MouseMoveEvent event) {


			GraphNode.this.c.openPopUp(Window.getScrollLeft() + event.getClientX(), Window.getScrollTop() + event.getClientY(),GraphNode.this.getId(),700);


		}

	};

	private DoubleClickHandler doubleC = new DoubleClickHandler() {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {

			GraphNode.this.c.selectNodeWithSubs(GraphNode.this);

		}
	};


	private ContextMenuHandler contextMenuH = new ContextMenuHandler() {

		@Override
		public void onContextMenu(ContextMenuEvent event) {

			event.preventDefault();
			if (c.getContextMenu() != null) {

				c.showContextMenu(GraphNode.this, event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());

			}

		}
	};

	private MouseDownHandler mouseDownH = new MouseDownHandler() {

		@Override
		public void onMouseDown(MouseDownEvent event) {

			if (event.getNativeButton() != NativeEvent.BUTTON_LEFT) return;

			GraphNode.this.getShape().toFront();

			for (Text t : textLines) {
				t.toFront();
			}


			GraphNode.this.c.getGraphNodeModifier().edgesToFront(GraphNode.this);

					double rx;
			double ry;

			rx=event.getRelativeX(GraphNode.this.c.getElement()) -(1/GraphNode.this.c.getScale()* GraphNode.this.getX()) ;
			ry=event.getRelativeY(GraphNode.this.c.getElement()) -(1/GraphNode.this.c.getScale()*GraphNode.this.getY()) ;

			GraphNode.this.setDragged(true);
			GraphNode.this.c.registerDrag(GraphNode.this,(int)rx,(int)ry);
		
			if (event.isControlKeyDown()) {
				GraphNode.this.c.addNodeToSelection(GraphNode.this);
			}else{
				GraphNode.this.c.setSelectedNode(GraphNode.this);
			}
		
		}

	};






}

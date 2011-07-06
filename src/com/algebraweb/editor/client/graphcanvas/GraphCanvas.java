package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
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
	
	private ArrayList<NodeSelectionHandler> selectionHandlers = new ArrayList<NodeSelectionHandler>();

	private double scale = 1;

	private Timer popupDelay;

	private boolean mouseOverNode = false;

	private NodePopup popup;

	private boolean isHidden=true;


	private static LoadingMessagePopUp loadingMessagePopUp = new LoadingMessagePopUp();
	private int oldWidth=0;
	private int oldHeight=0;
	private int oldScrollX=0;
	private int oldScrollY=0;

	private GraphNodeModifier gnm;
	private GraphEdgeModifier gem;
	private boolean invertArrows = false;
	private HashMap<Integer,GraphNode> selected = new HashMap<Integer,GraphNode>();


	private int height;
	private int width;

	protected int marginTop =0;
	protected int marginLeft =0;

	private ArrayList<GraphNode> nodes = new ArrayList<GraphNode>();
	private ArrayList<GraphEdge> edges = new ArrayList<GraphEdge>();

	public GraphCanvas(int width, int height,boolean invertArrows) {

		super(width, height);

		this.invertArrows=invertArrows;

		preventTextSelection(this.getElement(),true);

		this.height=height;
		this.width=width;

	}



	public void setMargin(int x, int y) {

		marginTop=y;
		marginLeft=x;

		this.getElement().getStyle().setMarginLeft(x, Unit.PX);
		this.getElement().getStyle().setMarginTop(y, Unit.PX);
	}

	public void setPadding(int x, int y) {

		marginTop=y;
		marginLeft=x;

		this.getElement().getStyle().setPaddingLeft(x, Unit.PX);
		this.getElement().getStyle().setPaddingTop(y, Unit.PX);
	}



	protected boolean isInvertArrows() {
		return invertArrows;
	}

	public GraphEdgeModifier getGem() {
		return gem;
	}


	public boolean isHidden() {
		return isHidden;
	}



	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
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

	public void setGraphNodeModifier(GraphNodeModifier gnm) {
		this.gnm = gnm;
	}

	public void setGraphEdgeModifier(GraphEdgeModifier gem) {
		this.gem = gem;
	}

	public NodePopup getPopup() {
		return popup;
	}


	public void setPopup(NodePopup popup) {
		this.popup = popup;
	}


	/**
	 * Return the GragEdgeModifier of this canvas
	 */

	public GraphEdgeModifier getGraphEdgeModifier() {
		return gem;
	}

	public void setSelectedNode(GraphNode n) {

		ArrayList<GraphNode> t = new ArrayList<GraphNode>();
		t.add(n);
		setSelectedNodes(t);

	}
	
	public void clearSelection() {
		
		
		Iterator<GraphNode> it = selected.values().iterator();
		
		while(it.hasNext()) gnm.setNotSelected(it.next());
		
		selected.clear();
		
	}

	/**
	 * Set the GraphNodes nodes selected
	 * @param n
	 */

	public void setSelectedNodes(ArrayList<GraphNode> nodes) {

		Iterator<GraphNode> it = nodes.iterator();
	
		clearSelection();
		
		while(it.hasNext()) {

			GraphNode n = it.next();

			this.selected.put(n.getId(), n);
			getGraphNodeModifier().setSelected(n);

		}
		
		
		Iterator<NodeSelectionHandler> itH = selectionHandlers.iterator();
		
		
		while (itH.hasNext()) {
			
			itH.next().isSelected(selected);
			
		}

	}

	/**
	 * Unset the GraphNode n selected
	 * @param n
	 */

	public HashMap<Integer,GraphNode> getSelectedNode() {

		return selected;
	}

	public boolean isMouseOverNode() {
		return mouseOverNode;
	}


	public void setMouseOverNode(boolean mouseOverNode) {
		this.mouseOverNode = mouseOverNode;
	}


	/**
	 * Sets the size of the canvas to the given dimension
	 * @param x
	 * @param y
	 */

	public void setSize(int x, int y) {

		this.height=y;
		this.width=x;
		this.overlay().setSize(width, height);
		this.setWidth(x + "px");
		this.setHeight(y + "px");
		updateZoom();
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

				if (popup.getNodeId() > -1 && !mouseOverNode(event.getClientX(), event.getClientY(), popup.getNodeId())) {
					popup.hide();

				}

				if (popup!= null && popupDelay != null && !mouseOverNode(event.getClientX(), event.getClientY())) {

					popupDelay.cancel();
				}

				if (GraphCanvas.this.dragNode != null) {
					DOM.eventGetCurrentEvent().preventDefault();
					double x = scale * (event.getRelativeX(GraphCanvas.this.getElement())-dragOffsetX);
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

		if (percent < 5) percent = 5;

		this.oldScrollX = Window.getScrollLeft();
		this.oldScrollY = Window.getScrollTop();


		this.oldWidth =(int)( width * scale);
		this.oldHeight =(int)( height * scale);


		scale =100/(percent);



		updateZoom();

		if ((oldScrollX+ (oldWidth -(width * scale))/2 <= 0) || (oldScrollY + (oldScrollY + (oldHeight -(height * scale))/2) <= 0)) {
			this.oldWidth = (int)(width * scale);
			this.oldHeight = (int)(height * scale);


		}

		//Window.scrollTo((int)(oldScrollX + (oldWidth -(width * scale))/2), (int)(oldScrollY + (oldHeight -(height * scale))/2));





	}


	private void updateZoom() {

		Iterator<GraphNode> i = nodes.iterator();

		while (i.hasNext()) {
			GraphNode current = i.next();
			gnm.checkDimension(current, current.getX(), current.getY());
		}

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
	 * @param quiet
	 */

	public void createEdge(GraphNode from, GraphNode to, boolean quiet) {


		this.edges.add(new GraphEdge(this,from,to,quiet, !this.isHidden()));

	}


	protected void registerDrag(GraphNode n,int offsetX,int offsetY) {

		this.dragNode = n;
		this.dragOffsetX=offsetX;
		this.dragOffsetY=offsetY;

	}


	private void clearDrag() {

		if (this.dragNode != null) {
			dragNode.setDragged(false);
			this.dragNode = null;
		}

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

	public GraphNode addNode(int id,int color,int x, int y,int width, int height,String text) {

		GraphNode g =  new GraphNode(this,color,x,y, width, height,text,id);

		this.nodes.add( g);

		//add dom handlers for node dragging

		g.getShape().addDomHandler(GraphNodeModifier.mouseMoveHandlerBuilder(g), MouseMoveEvent.getType());
		g.getText().addDomHandler(GraphNodeModifier.mouseMoveHandlerBuilder(g), MouseMoveEvent.getType());

		g.getShape().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g), MouseOutEvent.getType());
		g.getText().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g), MouseOutEvent.getType());

		return g;

	}



	public GraphNode addNode(int id,int color,int width, int height,String text) {

		int x = (int)(this.width * this.getScale()) / 2;
		int y = (int)(this.height * this.getScale()) / 2;

		return this.addNode(id,color,x,y,width,height,text);

	}

	/**
	 * Deletes a node from the canvas. Edges comming from or going to this node
	 * will be removed.
	 * @param n
	 */

	public void deleteNode(GraphNode n) {

		clearEdgesTo(n);
		clearEdgesFrom(n);


		gnm.dimOut(n);
		gnm.kill(n);
		nodes.remove(n);

	}

	public void clearEdgesFrom(GraphNode n) {



		ArrayList<GraphEdge> from = n.getEdgesFrom();

		Iterator<GraphEdge> it = from.iterator();
		GraphEdge current;

		while (it.hasNext()) {

			current = it.next();			
			gem.snakeIn(current,!this.isHidden());
			gem.deleteFromTo(current);
			this.edges.remove(current);
			it.remove();

		}


	}

	public void clearEdgesTo(GraphNode n) {


		ArrayList<GraphEdge> to = n.getEdgesTo();


		Iterator<GraphEdge> it = to.iterator();
		GraphEdge current;

		while (it.hasNext()) {

			current = it.next();			
			gem.snakeIn(current,!this.isHidden());
			gem.deleteFromFrom(current);
			this.edges.remove(current);
			it.remove();

		}



	}

	public void removeEdge(GraphNode n,int to) {


		ArrayList<GraphEdge> from = n.getEdgesFrom();


		GraphEdge current;
		Iterator<GraphEdge> it = from.iterator();

		while (it.hasNext()) {


			current = it.next();			
			if (current.getTo().getId() == to) {
				gem.snakeIn(current,!this.isHidden());
				//gem.deleteFromTo(current);
				//gem.deleteFromFrom(current);
				this.edges.remove(current);
				//it.remove();
			}

		}	

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

		GraphNodeLayoutingMachine m = new GraphNodeLayoutingMachine(this,gnm);
		m.sortGraph(nodes,edges, s);

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

			this.getGraphNodeModifier().showEdges(i.next(),!this.isHidden());


		}

	}


	public void centerAt(int x,int y) {

		Window.scrollTo((int)((x) - (Window.getClientWidth()/2)),(int)((y) - (Window.getClientHeight()/2)));

	}



	public void openPopUp(final int x, final int y, final int nodeid, int delay) {


		if ((getPopup().getNodeId() != nodeid) &&
				!getPopup().isShowing()) {

			if (popupDelay != null) popupDelay.cancel();

			popupDelay = new Timer() {

				@Override
				public void run() {

					popup.setNodeId(nodeid);
					getPopup().showAt( x + 5, y + 5);


				}
			};

			popupDelay.schedule(delay);
		}
	}


	private boolean mouseOverNode(int mouseX, int mouseY, int nodeid) {


		int x = (int)((Window.getScrollLeft()-marginLeft + mouseX));
		int y = (int)((Window.getScrollTop()  -marginTop+ mouseY));

		GraphNode current = this.getGraphNodeById(nodeid);

		return ((x*getScale() >= current.getX() && x*getScale() <= current.getX() + current.getWidth() + 4 ) &&
				y*getScale() >= current.getY() && y*getScale() <= current.getY() + current.getHeight() + 4);		


	}

	private boolean mouseOverNode(int mouseX, int mouseY) {


		int x = (int)((Window.getScrollLeft() -marginLeft + mouseX));
		int y = (int)((Window.getScrollTop() -marginTop + mouseY));


		Iterator<GraphNode> i = nodes.iterator();

		while (i.hasNext()) {

			GraphNode current =i.next();

			if ((x*getScale() >= current.getX() && x*getScale() <= current.getX() + current.getWidth() + 4 ) &&
					y*getScale() >= current.getY() && y*getScale() <= current.getY() + current.getHeight() + 4) {
				return true;
			}

		}
		return false;
	}

	public static void showLoading(String msg) {


		loadingMessagePopUp.show(msg);


	}

	public static void hideLoading() {

		loadingMessagePopUp.hide();

	}

	public Rect rectFactory(double x, double y, double w, double h) {

		return new Rect(x, y, w, h);

	}

	public Text textFactory(double x, double y, String text) {

		return new Text(x, y, text);

	}

	public void hangShapeOntoNode(String identifier, ConnectedShape s, int nid) {

		gnm.connectShapeToNode(identifier, s, this.getGraphNodeById(nid));

	}

	public void unHangShapeFromNode(String identifier, int nid) {


		gnm.removeShapeFromNode(identifier,this.getGraphNodeById(nid));

	}

	public void setBlurred(boolean blurr) {

		String browser = Window.Navigator.getUserAgent();
		String browserVersion = Window.Navigator.getAppVersion();

		RegExp p = RegExp.compile(("([0-9]+\\.?[0-9]*)"));

		MatchResult m = p.exec(browserVersion);
		double browserVer=0;


		GWT.log(browserVersion);
		if (m.getGroupCount()>1) {
			browserVer=Double.parseDouble(m.getGroup(0));
		}

		if (browser.matches(".*[fF]irefox.*") && browserVer >= 3) {

			if (blurr) {



				this.getElement().getFirstChildElement().setAttribute("filter", "url(#Gaussian_Blur)");


			}else{

				this.getElement().getFirstChildElement().removeAttribute("filter");


			}
		}

	}
	
	public boolean addNodeSelectionHandler(NodeSelectionHandler h) {
		
		
		return this.selectionHandlers.add(h);
		
	}
	
	public boolean removeNodeSelectionHandler(NodeSelectionHandler h) {
		
		
		return this.selectionHandlers.remove(h);
		
	}



	/**
	 * We need this to prevent a text selection while dragging around nodes.
	 * 
	 * @param el
	 * @param disable
	 */

	protected native static void preventTextSelection(Element el, boolean disable)/*-{

	    if (disable) {
	        el.ondrag = function () { return false; };
	        el.onselectstart = function () { return false; };
	    } else {
	        el.ondrag = null;
	        el.onselectstart = null;
	    }

	}-*/;




}

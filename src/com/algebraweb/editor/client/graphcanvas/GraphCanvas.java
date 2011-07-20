package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;


import com.algebraweb.editor.client.AlgebraEditor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
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
import com.google.gwt.user.client.Event;
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

	private boolean isNotActive=true;
	private boolean sortOnActivation=false;

	private static LoadingMessagePopUp loadingMessagePopUp = new LoadingMessagePopUp();
	private int oldWidth=0;
	private int oldHeight=0;
	private int oldScrollX=0;
	private int oldScrollY=0;

	private GraphNodeModifier gnm;
	private GraphEdgeModifier gem;
	private boolean invertArrows = false;
	private HashMap<Integer,GraphNode> selected = new HashMap<Integer,GraphNode>();
	private HashMap<Coordinate,GraphEdge> selectedEdges = new HashMap<Coordinate,GraphEdge>();

	private NodeContextMenu m;
	private ContextMenu canvasMenu;

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
		sinkEvents(Event.KEYEVENTS);

		this.height=height;
		this.width=width;

	}



	public void setMargin(int x, int y) {

		marginTop=y;
		marginLeft=x;

		this.getRaphaelElement().getStyle().setMarginLeft(x, Unit.PX);
		this.getRaphaelElement().getStyle().setMarginTop(y, Unit.PX);
	}

	public void setPadding(int x, int y) {

		marginTop=y;
		marginLeft=x;

		this.getRaphaelElement().getStyle().setPaddingLeft(x, Unit.PX);
		this.getRaphaelElement().getStyle().setPaddingTop(y, Unit.PX);
	}



	protected boolean isInvertArrows() {
		return invertArrows;
	}

	public GraphEdgeModifier getGem() {
		return gem;
	}


	public boolean isNotActive() {
		return isNotActive;
	}



	public void setNotActive(boolean isHidden) {
		this.isNotActive = isHidden;
		if (!isHidden && sortOnActivation) {
			layout(false);
			sortOnActivation = false;
		}
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

	public void setSelectedEdge(GraphEdge n) {

		ArrayList<GraphEdge> t = new ArrayList<GraphEdge>();
		t.add(n);
		setSelectedEdges(t);

	}

	public void clearSelection() {


		Iterator<GraphNode> it = selected.values().iterator();
		while(it.hasNext()) gnm.setNotSelected(it.next());

		Iterator<GraphEdge> itE = selectedEdges.values().iterator();
		while(itE.hasNext()) gem.setNotSelected(itE.next());

		selected.clear();
		selectedEdges.clear();

	}


	public void addNodeToSelection(GraphNode node) {


		this.selected.put(node.getId(), node);
		getGraphNodeModifier().setSelected(node);


		Iterator<NodeSelectionHandler> itH = selectionHandlers.iterator();

		while (itH.hasNext()) {
			itH.next().isSelected(node);
		}




	}

	/**
	 * Set the GraphNodes nodes selected
	 * @param n
	 */

	public void setSelectedNodes(ArrayList<GraphNode> nodes) {

		Iterator<GraphNode> it = nodes.iterator();
		popupDelay.cancel();

		clearSelection();

		while(it.hasNext()) {

			GraphNode n = it.next();

			this.selected.put(n.getId(), n);
			getGraphNodeModifier().setSelected(n);

			Iterator<NodeSelectionHandler> itH = selectionHandlers.iterator();

			while (itH.hasNext()) {
				itH.next().isSelected(n);
			}
		}
	}

	public void setSelectedEdges(ArrayList<GraphEdge> edges) {


		Iterator<GraphEdge> it = edges.iterator();

		clearSelection();

		while(it.hasNext()) {

			GraphEdge e = it.next();

			this.selectedEdges.put(new Coordinate(e.getFrom().getId(),e.getTo().getId()), e);
			getGraphEdgeModifier().setSelected(e);

		}




	}

	public boolean hasEdge (int from, int to) {


		GraphNode fromN = getGraphNodeById(from);
		GraphNode toN = getGraphNodeById(to);

		Iterator<GraphEdge> itTo = toN.getEdgesTo().iterator();
		Iterator<GraphEdge> itFrom = fromN.getEdgesFrom().iterator();

		boolean success = false;

		while (itTo.hasNext()) {

			if (itTo.next().getFrom().getId() == from) success = true;

		}

		while (success==true && itFrom.hasNext()) {

			if (itFrom.next().getTo().getId() == to) return success;

		}

		return false;

	}


	public HashMap<Integer,GraphNode> getSelectedNode() {

		return selected;
	}

	public HashMap<Coordinate,GraphEdge> getSelectedEdges() {

		return selectedEdges;
	}

	public HashMap<Coordinate,Integer> getSelectedEdgesWithPos() {

		HashMap<Coordinate,Integer> ret = new HashMap<Coordinate,Integer>();

		Iterator<GraphEdge> it = getSelectedEdges().values().iterator();

		while (it.hasNext()) {

			GraphEdge cur = it.next();

			ret.put(new Coordinate(cur.getFrom().getId(),cur.getTo().getId()), cur.getFixedParentPos());


		}

		return ret;
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
					dragNode.setHasBeenDragged(true);
					FullScreenDragPanel.clearDrag();
					DOM.eventGetCurrentEvent().preventDefault();
					double x = scale * (event.getRelativeX(GraphCanvas.this.getRaphaelElement())-dragOffsetX);
					double y = scale *(event.getRelativeY(GraphCanvas.this.getRaphaelElement())-dragOffsetY);

					hidePopUp();

					if (getSelectedNode().size()>1) {

						double oldX = dragNode.getX();
						double oldY = dragNode.getY();

						Iterator<GraphNode> u = getSelectedNode().values().iterator();

						while (u.hasNext()) {

							GraphNode cur = u.next();
							gnm.moveTo(cur, (cur.getX()-oldX) + x, (cur.getY()-oldY) + y);

						}

					}else{
						gnm.moveTo(dragNode,x,y);
					}



				}

			}

		}, MouseMoveEvent.getType());


		this.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				GraphCanvas.this.clearDrag();
				
				if (!mouseOverNode(event.getClientX(), event.getClientY())) {
					
					clearSelection();
					
				}
				
			}

		}, MouseUpEvent.getType());

		this.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {


				if (!mouseOverNode(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY()) && event.getNativeEvent().getButton() == NativeEvent.BUTTON_RIGHT) {

					event.preventDefault();
					showCanvasContextMenu(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());

				}

			}

		},ContextMenuEvent.getType());

		this.addDomHandler(new MouseWheelHandler() {

			@Override
			public void onMouseWheel(MouseWheelEvent event) {

				if (event.isMetaKeyDown() || event.isAltKeyDown() || event.isShiftKeyDown() || event.isControlKeyDown()) {

					DOM.eventGetCurrentEvent().preventDefault();

					if (event.isNorth()) zoom(((1 / getScale()) * 100) + 10);
					if (event.isSouth()) zoom(((1 / getScale()) * 100) - 10);

				}
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
			gnm.updateConnectedWidgets(current);
		}

		this.getRaphaelElement().getFirstChildElement().setAttribute("viewBox", "0 0 " + (int)(this.width * scale) + " " +  (int)(this.height * scale));





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

	public void createEdge(GraphNode from, GraphNode to, int fixedPos,boolean quiet) {

		AlgebraEditor.log("(GraphCanvas) Creating edge from " + from.getId() + " to " + to.getId() + (fixedPos != -1?" with fixed pos " + fixedPos:""));
		GraphEdge t = new GraphEdge(this,from,to,fixedPos,quiet, !this.isNotActive());
		this.edges.add(t);

	}

	public void selectNodeWithSubs(GraphNode n) {

		clearSelection();
		ArrayList<GraphNode> l = new ArrayList<GraphNode>();
		selectNodeWithSubs(n,l);

		setSelectedNodes(l);



	}

	private void selectNodeWithSubs(GraphNode n, ArrayList<GraphNode> nodesToSelect) {

		if (nodesToSelect.contains(n)) return;

		nodesToSelect.add(n);

		Iterator<GraphEdge> itEdgesFromThis = n.getEdgesFrom().iterator();

		while (itEdgesFromThis.hasNext()) selectNodeWithSubs(itEdgesFromThis.next().getTo(),nodesToSelect);			



	}


	protected void registerDrag(GraphNode n,int offsetX,int offsetY) {

		this.dragNode = n;
		this.dragOffsetX=offsetX;
		this.dragOffsetY=offsetY;

	}


	public void clearDrag() {

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

	public GraphNode addNode(int id,int color,int x, int y,int width, int height,String text, int fixedChildCount) {

		GraphNode g =  new GraphNode(this,color,x,y, width, height,text,id);
		g.setFixedChildCount(fixedChildCount);

		this.nodes.add( g);

		//add dom handlers for node dragging

		g.getShape().addDomHandler(GraphNodeModifier.mouseMoveHandlerBuilder(g), MouseMoveEvent.getType());
		g.getShape().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g), MouseOutEvent.getType());

		gnm.checkDimension(g,x,y);
		
		return g;

	}



	public GraphNode addNode(int id,int color,int width, int height,String text, int fixedChildCount) {

		int x = (int)(this.width * this.getScale()) / 2;
		int y = (int)(this.height * this.getScale()) / 2;

		return this.addNode(id,color,x,y,width,height,text,fixedChildCount);

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
			GWT.log(Boolean.toString(!this.isNotActive()));
			current = it.next();			
			gem.snakeIn(current,!this.isNotActive());
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
			gem.snakeIn(current,!this.isNotActive());
			gem.deleteFromFrom(current);
			this.edges.remove(current);
			it.remove();

		}



	}

	public void removeEdge(GraphNode n,int to) {


		ArrayList<GraphEdge> from = n.getEdgesFrom();


		GraphEdge current;
		Iterator<GraphEdge> it = from.iterator();

		GWT.log(Integer.toString(to));


		while (it.hasNext()) {

			current = it.next();			
			if (current.getTo().getId() == to) {


				gem.snakeIn(current,!this.isNotActive());
				gem.deleteFromTo(current);
				//gem.deleteFromFrom(current);
				this.edges.remove(current);
				it.remove();
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
		AlgebraEditor.log(isNotActive?"Sorting hidden graph...":"Sorting visible graph...");
		m.sortGraph(nodes,edges, s,!isNotActive);
		sortOnActivation = isNotActive;

	}

	public void layout(boolean animate) {

		GraphNodeLayoutingMachine m = new GraphNodeLayoutingMachine(this,gnm);
		m.layout(this.getNodes(),animate);


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

			this.getGraphNodeModifier().showEdges(i.next(),!this.isNotActive());


		}

	}


	public void centerAt(int x,int y) {

		Window.scrollTo((int)((x) - (Window.getClientWidth()/2)),(int)((y) - (Window.getClientHeight()/2)));

	}

	public void hidePopUp() {

		if (popupDelay != null) popupDelay.cancel();
		if (getPopup() != null) getPopup().hide();

	}



	public void openPopUp(final int x, final int y, final int nodeid, int delay) {


		if ((getPopup().getNodeId() != nodeid) &&
				!getPopup().isShowing() && dragNode == null) {

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

	protected boolean mouseOverNode(int mouseX, int mouseY) {


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

	public Circle circleFactory(double x, double y, double r) {

		return new Circle(x, y, r);

	}

	public Text textFactory(double x, double y, String text) {

		return new Text(x, y, text);

	}

	public void hangShapeOntoNode(String identifier, ConnectedShape s, int nid) {

		gnm.connectShapeToNode(identifier, s, this.getGraphNodeById(nid));

	}

	public void unHangShapeFromNode(String identifier, int nid) {

		gnm.removeShapeFromNode(identifier, this.getGraphNodeById(nid));

	}

	public void hangWidgetOntoNode(String identifier, ConnectedWidget w, int nid) {

		this.add(w);

		gnm.connectWidgetToNode(identifier, w, this.getGraphNodeById(nid));

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



				this.getRaphaelElement().getFirstChildElement().setAttribute("filter", "url(#Gaussian_Blur)");


			}else{

				this.getRaphaelElement().getFirstChildElement().removeAttribute("filter");


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
	 * @return the marginTop
	 */
	public int getMarginTop() {
		return marginTop;
	}



	/**
	 * @return the marginLeft
	 */
	public int getMarginLeft() {
		return marginLeft;
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



	public void unHangWidgetFromNode(String string, int nid) {

		this.remove(gnm.unhandWidgetToNode(string, getGraphNodeById(nid)));

	}

	/**
	 * @return the m
	 */
	public NodeContextMenu getContextMenu() {
		return m;
	}

	/**
	 * @param m the m to set
	 */
	public void setContextMenu(NodeContextMenu m) {
		this.m = m;
	}



	public void showContextMenu(GraphNode graphNode, int clientX, int clientY) {

		hidePopUp();
		unbugMe();
		getContextMenu().show(graphNode, clientX+Window.getScrollLeft(),clientY+Window.getScrollTop());

	}

	public void showCanvasContextMenu(int clientX, int clientY) {

		hidePopUp();
		unbugMe();
		getCanvasMenu().show(clientX+Window.getScrollLeft(),clientY+Window.getScrollTop());

	}



	/**
	 * @return the canvasMenu
	 */
	public ContextMenu getCanvasMenu() {
		return canvasMenu;
	}



	/**
	 * @param canvasMenu the canvasMenu to set
	 */
	public void setCanvasMenu(ContextMenu canvasMenu) {
		this.canvasMenu = canvasMenu;
	}


	public void unbugMe() {

		if (dragNode != null) {
			dragNode= null;
			dragNode.setDragged(false);
		}

		FullScreenDragPanel.clearDrag();

	}



}

package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.AlgebraEditor;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.hydro4ge.raphaelgwt.client.Raphael;

/**
 * A widget to draw directed graphs. Uses the Raphaël-Library
 * (http://raphaeljs.com/) and its GWT-wrapper raphaelgwt
 * (http://code.google.com/p/raphaelgwt/)
 * 
 * Version Beta 0.6
 * 
 * @author Patrick Brosi
 * 
 */

public class GraphCanvas extends Raphael implements Fillable {
	/**
	 * Hides the loading message display with showLoading(msg)
	 */
	public static void hideLoading() {
		loadingMessagePopUp.hide();
	}

	/**
	 * We need this to prevent a text selection while dragging around nodes.
	 * 
	 * @param el
	 * @param disable
	 */
	protected native static void preventTextSelection(Element el,
			boolean disable)/*-{

							if (disable) {
							el.ondrag = function () { return false; };
							el.onselectstart = function () { return false; };
							} else {
							el.ondrag = null;
							el.onselectstart = null;
							}

							}-*/;

	/**
	 * Shows a loading message
	 * 
	 * @param msg
	 *            the message to show
	 */
	public static void showLoading(String msg) {
		loadingMessagePopUp.show(msg);
	}

	private GraphNode dragNode = null;
	private int dragOffsetX = 0;
	private int dragOffsetY = 0;
	private List<NodeSelectionHandler> selectionHandlers = new ArrayList<NodeSelectionHandler>();
	private double scale = 1;
	private Timer popupDelay;
	private boolean mouseOverNode = false;
	private boolean mouseOverEdge = false;
	private NodePopup popup;
	private boolean isNotActive = true;
	private boolean sortOnActivation = false;
	private static LoadingMessagePopUp loadingMessagePopUp = new LoadingMessagePopUp();
	private int oldWidth = 0;
	private int oldHeight = 0;
	private int oldScrollX = 0;
	private int oldScrollY = 0;
	private GraphNodeModifier gnm;
	private GraphEdgeModifier gem;
	private boolean invertArrows = false;
	private Map<Integer, GraphNode> selected = new HashMap<Integer, GraphNode>();
	private Map<Tuple, GraphEdge> selectedEdges = new HashMap<Tuple, GraphEdge>();
	private GraphNode hoverNode = null;
	private NodeContextMenu m;
	private ContextMenu canvasMenu;
	private boolean preventHoverMenu = false;

	private int height;
	private int width;

	protected int marginTop = 0;
	protected int marginLeft = 0;

	private List<GraphNode> nodes = new ArrayList<GraphNode>();
	private List<GraphEdge> edges = new ArrayList<GraphEdge>();

	public GraphCanvas(int i, int j) {
		this(i, j, false);
	}

	public GraphCanvas(int width, int height, boolean invertArrows) {
		super(width, height);
		this.invertArrows = invertArrows;

		preventTextSelection(this.getElement(), true);
		sinkEvents(Event.KEYEVENTS);

		this.height = height;
		this.width = width;
	}

	public GraphNode addNode(int id, int color, int x, int y, int width,
			int height, String text) {
		GraphNode g = new GraphNode(this, color, x, y, width, height, text, id);
		this.nodes.add(g);
		g.getShape().addDomHandler(
				GraphNodeModifier.mouseMoveHandlerBuilder(g),
				MouseMoveEvent.getType());
		g.getShape().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g),
				MouseOutEvent.getType());
		gnm.checkDimension(g, x, y);
		return g;
	}

	/**
	 * Adds a new node to the canvas at (x,y). Returns the created GraphNode
	 * 
	 * @param id
	 *            the id of the new node
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @param width
	 *            the width of the new node
	 * @param height
	 *            the height of the new node
	 * @param text
	 *            the caption of the new node
	 * @return the created node
	 */
	public GraphNode addNode(int id, int color, int x, int y, int width,
			int height, String text, int fixedChildCount) {
		GraphNode g = new GraphNode(this, color, x, y, width, height, text, id);
		g.setFixedChildCount(fixedChildCount);

		this.nodes.add(g);
		g.getShape().addDomHandler(
				GraphNodeModifier.mouseMoveHandlerBuilder(g),
				MouseMoveEvent.getType());
		g.getShape().addDomHandler(GraphNodeModifier.mouseOutHandlerBuilder(g),
				MouseOutEvent.getType());

		gnm.checkDimension(g, x, y);

		return g;
	}

	/**
	 * Adds a new node to the canvas at (x,y) with a <b>fixed count</b> of
	 * childs. Returns the created GraphNode
	 * 
	 * @param id
	 *            the id of the new node
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @param width
	 *            the width of the new node
	 * @param height
	 *            the height of the new node
	 * @param text
	 *            the caption of the new node
	 * @param fixedChildCount
	 *            the number of childs the node can hold
	 * @return
	 */
	public GraphNode addNode(int id, int color, int width, int height,
			String text, int fixedChildCount) {
		int x = (int) (this.width * this.getScale()) / 2;
		int y = (int) (this.height * this.getScale()) / 2;
		return this.addNode(id, color, x, y, width, height, text,
				fixedChildCount);
	}

	/**
	 * Adds a new node selection handler to be called on a node selection.
	 * 
	 * @param h
	 *            the handler to be added
	 * @return true of adding was successful
	 */
	public boolean addNodeSelectionHandler(NodeSelectionHandler h) {
		return this.selectionHandlers.add(h);
	}

	/**
	 * Adds a node to the current selection. Node selection handlers will be
	 * called.
	 * 
	 * @param node
	 *            the node to be added to the selection
	 */
	public void addNodeToSelection(GraphNode node) {

		boolean dontSelect = false;
		Iterator<NodeSelectionHandler> itH = selectionHandlers.iterator();
		while (itH.hasNext()) {
			if (!itH.next().isSelected(node)) {
				dontSelect = true;
			}
		}
		if (!dontSelect) {
			this.selected.put(node.getId(), node);
			getGraphNodeModifier().setSelected(node);
		}
	}

	/**
	 * Center the scroll position at (x,y)
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 */
	public void centerAt(int x, int y) {

		Window.scrollTo((x - Window.getClientWidth() / 2), (y - Window
				.getClientHeight() / 2));

	}

	@Override
	public void clear() {
		this.nodes.clear();
		this.edges.clear();
		super.clear();
	}

	protected void clearDrag() {
		if (this.dragNode != null) {
			dragNode.setDragged(false);
			this.dragNode = null;
		}
	}

	protected void clearEdgesFrom(GraphNode n) {
		List<GraphEdge> from = n.getEdgesFrom();
		Iterator<GraphEdge> it = from.iterator();
		GraphEdge current;
		while (it.hasNext()) {
			current = it.next();
			gem.snakeIn(current, !this.isNotActive());
			gem.deleteFromTo(current);
			this.edges.remove(current);
			it.remove();
		}
	}

	protected void clearEdgesTo(GraphNode n) {
		List<GraphEdge> to = n.getEdgesTo();
		Iterator<GraphEdge> it = to.iterator();
		GraphEdge current;
		while (it.hasNext()) {
			current = it.next();
			gem.snakeIn(current, !this.isNotActive());
			gem.deleteFromFrom(current);
			this.edges.remove(current);
			it.remove();
		}
	}

	/**
	 * Blurs the canvases selection
	 */
	public void clearSelection() {
		Iterator<GraphNode> it = selected.values().iterator();
		while (it.hasNext()) {
			gnm.setNotSelected(it.next());
		}
		Iterator<GraphEdge> itE = selectedEdges.values().iterator();
		while (itE.hasNext()) {
			gem.setNotSelected(itE.next());
		}
		selected.clear();
		selectedEdges.clear();
	}

	public void createEdge(int i, int j) {
		createEdge(i, j, 1, false);
	}

	/**
	 * Draws an edge from node "from" to node "to"
	 * 
	 * @param from
	 * @param to
	 * @param quiet
	 */

	public void createEdge(int from, int to, int fixedPos, boolean quiet) {
		GraphEdge t = new GraphEdge(this, getGraphNodeById(from),
				getGraphNodeById(to), fixedPos, quiet, !this.isNotActive());
		this.edges.add(t);
	}

	/**
	 * Deletes a node from the canvas. Edges comming from or going to this node
	 * will be removed.
	 * 
	 * @param n
	 */

	public void deleteNode(GraphNode n) {
		clearEdgesTo(n);
		clearEdgesFrom(n);

		gnm.dimOut(n);
		gnm.kill(n);
		nodes.remove(n);
	}

	/**
	 * @return the canvasMenu
	 */
	public ContextMenu getCanvasMenu() {
		return canvasMenu;
	}

	/**
	 * @return the context menu used for nodes
	 */
	public NodeContextMenu getContextMenu() {
		return m;
	}

	protected GraphEdgeModifier getGem() {
		return gem;
	}

	protected GraphEdgeModifier getGraphEdgeModifier() {
		return gem;
	}

	/**
	 * Returns the GraphNode with the specified id. Null if np such GraphNode
	 * could be found.
	 * 
	 * @param id
	 *            the id to look for
	 * @return the GraphNode with the id or null if not found
	 */
	public GraphNode getGraphNodeById(int id) {
		Iterator<GraphNode> i = nodes.iterator();
		while (i.hasNext()) {
			GraphNode current = i.next();
			if (current.getId() == id) {
				return current;
			}
		}
		return null;
	}

	protected GraphNodeModifier getGraphNodeModifier() {
		return gnm;
	}

	/**
	 * Returns the current height of the canvas
	 * 
	 * @return the height of the canvas
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the hoverNode
	 */
	public GraphNode getHoverNode() {
		return hoverNode;
	}

	/**
	 * @return the marginLeft
	 */
	public int getMarginLeft() {
		return marginLeft;
	}

	/**
	 * @return the marginTop
	 */
	public int getMarginTop() {
		return marginTop;
	}

	/**
	 * Returns a flat list of all nodes on the canvas
	 * 
	 * @return the nodes on the canvas
	 */

	public List<GraphNode> getNodes() {
		return this.nodes;
	}

	/**
	 * returns the popup used on node hovering
	 * 
	 * @return the popup used for node hovering
	 */
	public NodePopup getPopup() {
		return popup;
	}

	/**
	 * Returns the current scale. For example, zooming to 50% will make this
	 * return 2.
	 * 
	 * @return the current scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Returns a coordinate tuple (x,y) specifying scroll position so that node
	 * with the given id is visible.
	 * 
	 * @param nid
	 *            the node to scroll to
	 * @return a coordinate for the browser window to scroll to
	 */
	public Tuple getScrollIntoViewPos(int nid) {
		GraphNode n = getGraphNodeById(nid);
		return new Tuple(n.getWidth() / 2 + getMarginLeft()
				+ (int) (n.getX() * getScale()) - Window.getClientWidth() / 2,
				getMarginTop()
						+ (int) (n.getY() * getScale() - Window
								.getClientHeight() / 2));
	}

	/**
	 * Get the selected edges
	 * 
	 * @return the selected edges as a HashMap
	 */
	public Map<Tuple, GraphEdge> getSelectedEdges() {
		return selectedEdges;
	}

	/**
	 * Get the selected edges with their position on the mother node
	 * 
	 * @return the selected edges as a HashMap
	 */
	public HashMap<Tuple, Integer> getSelectedEdgesWithPos() {
		HashMap<Tuple, Integer> ret = new HashMap<Tuple, Integer>();
		Iterator<GraphEdge> it = getSelectedEdges().values().iterator();

		while (it.hasNext()) {
			GraphEdge cur = it.next();
			ret.put(new Tuple(cur.getFrom().getId(), cur.getTo().getId()), cur
					.getFixedParentPos());
		}
		return ret;
	}

	/**
	 * Gets the selected nodes
	 * 
	 * @return the nodes selected as a HashMap
	 */
	public Map<Integer, GraphNode> getSelectedNodes() {
		return selected;
	}

	/**
	 * Returns the current width of the canvas
	 * 
	 * @return the current width of the canvas
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Hangs a shape onto a node. It will be scrolled, animated and removed with
	 * the node's shape.
	 * 
	 * @param identifier
	 *            a string to identify the shape
	 * @param s
	 *            the ConnectedShape to connect
	 * @param nid
	 *            the id of the node to hang this shape onto
	 */
	public void hangShapeOntoNode(String identifier, ConnectedShape s, int nid) {
		gnm.connectShapeToNode(identifier, s, this.getGraphNodeById(nid));
	}

	/**
	 * Hangs a widget onto a node. Acts like hangShapeOntoNode() except that it
	 * works for GWT widgets instead of Raphael-Shapes.
	 * 
	 * @param identifier
	 * @param w
	 * @param nid
	 */
	public void hangWidgetOntoNode(String identifier, ConnectedWidget w, int nid) {
		this.add(w);
		gnm.connectWidgetToNode(identifier, w, this.getGraphNodeById(nid));
	}

	public boolean hasEdge(int from, int to, int position) {
		GraphNode fromN = getGraphNodeById(from);
		GraphNode toN = getGraphNodeById(to);

		Iterator<GraphEdge> itTo = toN.getEdgesTo().iterator();
		Iterator<GraphEdge> itFrom = fromN.getEdgesFrom().iterator();

		boolean success = false;

		while (itTo.hasNext()) {
			if (itTo.next().getFrom().getId() == from) {
				success = true;
			}
		}

		while (success == true && itFrom.hasNext()) {
			GraphEdge cur = itFrom.next();
			if (cur.getTo().getId() == to
					&& cur.getFixedParentPos() == position) {
				return success;
			}
		}
		return false;
	}

	public void hidePopUp() {
		if (popupDelay != null) {
			popupDelay.cancel();
		}
		if (getPopup() != null) {
			getPopup().hide();
		}
	}

	protected boolean isInvertArrows() {
		return invertArrows;
	}

	/**
	 * @return the mouseOverEdge
	 */
	public boolean isMouseOverEdge() {
		return mouseOverEdge;
	}

	public boolean isMouseOverNode() {
		return mouseOverNode;
	}

	public boolean isNotActive() {
		return isNotActive;
	}

	/**
	 * @return the preventHoverMenu
	 */
	public boolean isPreventHoverMenu() {
		return preventHoverMenu;
	}

	public void layout(boolean animate) {
		GraphNodeLayoutingMachine m = new GraphNodeLayoutingMachine(this, gnm);
		m.layout(this.getNodes(), animate);
	}

	protected boolean mouseOverNode(int mouseX, int mouseY) {
		int x = Window.getScrollLeft() - marginLeft + mouseX;
		int y = Window.getScrollTop() - marginTop + mouseY;

		Iterator<GraphNode> i = nodes.iterator();

		while (i.hasNext()) {
			GraphNode current = i.next();

			if (x * getScale() >= current.getX()
					&& x * getScale() <= current.getX() + current.getWidth()
							+ 4
					&& y * getScale() >= current.getY()
					&& y * getScale() <= current.getY() + current.getHeight()
							+ 4) {
				return true;
			}
		}
		return false;
	}

	private boolean mouseOverNode(int mouseX, int mouseY, int nodeid) {
		int x = Window.getScrollLeft() - marginLeft + mouseX;
		int y = Window.getScrollTop() - marginTop + mouseY;

		GraphNode current = this.getGraphNodeById(nodeid);

		return x * getScale() >= current.getX()
				&& x * getScale() <= current.getX() + current.getWidth() + 4
				&& y * getScale() >= current.getY()
				&& y * getScale() <= current.getY() + current.getHeight() + 4;
	}

	@Override
	public void onLoad() {
		super.onLoad();

		this.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				if (popup.getNodeId() > -1
						&& !mouseOverNode(event.getClientX(), event
								.getClientY(), popup.getNodeId())) {
					popup.hide();
				}

				if (popup != null
						&& popupDelay != null
						&& !mouseOverNode(event.getClientX(), event
								.getClientY())) {
					popupDelay.cancel();
				}

				if (GraphCanvas.this.dragNode != null) {
					dragNode.setHasBeenDragged(true);
					FullScreenDragPanel.clearDrag();
					DOM.eventGetCurrentEvent().preventDefault();
					double x = scale
							* (event.getRelativeX(GraphCanvas.this
									.getRaphaelElement()) - dragOffsetX);
					double y = scale
							* (event.getRelativeY(GraphCanvas.this
									.getRaphaelElement()) - dragOffsetY);

					hidePopUp();

					if (getSelectedNodes().size() > 1) {
						double oldX = dragNode.getX();
						double oldY = dragNode.getY();

						Iterator<GraphNode> u = getSelectedNodes().values()
								.iterator();

						while (u.hasNext()) {
							GraphNode cur = u.next();
							gnm.moveTo(cur, cur.getX() - oldX + x, cur.getY()
									- oldY + y);
						}
					} else {
						gnm.moveTo(dragNode, x, y);
					}
				}
			}
		}, MouseMoveEvent.getType());

		this.addDomHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				GraphCanvas.this.clearDrag();
				if (!isMouseOverEdge()
						&& !mouseOverNode(event.getClientX(), event
								.getClientY())) {
					clearSelection();
				}
			}
		}, MouseUpEvent.getType());

		this.addDomHandler(new ContextMenuHandler() {
			@Override
			public void onContextMenu(ContextMenuEvent event) {

				if (!mouseOverNode(event.getNativeEvent().getClientX(), event
						.getNativeEvent().getClientY())
						&& event.getNativeEvent().getButton() == NativeEvent.BUTTON_RIGHT) {
					event.preventDefault();
					showCanvasContextMenu(event.getNativeEvent().getClientX(),
							event.getNativeEvent().getClientY());
				}
			}
		}, ContextMenuEvent.getType());

		this.addDomHandler(new MouseWheelHandler() {
			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				if (event.isMetaKeyDown() || event.isAltKeyDown()
						|| event.isShiftKeyDown() || event.isControlKeyDown()) {
					DOM.eventGetCurrentEvent().preventDefault();

					if (event.isNorth()) {
						zoomIn();
					}
					if (event.isSouth()) {
						zoomOut();
					}
				}
			}

		}, MouseWheelEvent.getType());
	}

	public void openPopUp(final int x, final int y, final int nodeid, int delay) {
		if (!preventHoverMenu && getPopup().getNodeId() != nodeid
				&& !getPopup().isShowing() && dragNode == null) {

			if (popupDelay != null) {
				popupDelay.cancel();
			}
			popupDelay = new Timer() {
				@Override
				public void run() {
					popup.setNodeId(nodeid);
					getPopup().showAt(x + 5, y + 5);
				}
			};
			popupDelay.schedule(delay);
		}
	}

	protected void registerDrag(GraphNode n, int offsetX, int offsetY) {
		this.dragNode = n;
		this.dragOffsetX = offsetX;
		this.dragOffsetY = offsetY;
	}

	public void removeEdge(GraphNode n, int to) {
		List<GraphEdge> from = n.getEdgesFrom();
		GraphEdge current;
		Iterator<GraphEdge> it = from.iterator();

		while (it.hasNext()) {
			current = it.next();
			if (current.getTo().getId() == to) {
				gem.snakeIn(current, !this.isNotActive());
				gem.deleteFromTo(current);
				// gem.deleteFromFrom(current);
				this.edges.remove(current);
				it.remove();
			}
		}
	}

	public void removeEdge(int nid, int to, int position) {
		List<GraphEdge> from = getGraphNodeById(nid).getEdgesFrom();
		GraphEdge current;

		for (int i = 0; i < from.size(); i++) {
			current = from.get(i);
			if (current.getTo().getId() == to
					&& current.getFixedParentPos() == position) {
				gem.snakeIn(current, !this.isNotActive());
				gem.deleteFromTo(current);
				this.edges.remove(current);
				if (from.contains(current)) {
					from.remove(i);
				}
			}
		}
	}

	public void removeNodeFromSelection(GraphNode node) {
		this.selected.remove(node.getId());
		getGraphNodeModifier().setNotSelected(node);
	}

	public boolean removeNodeSelectionHandler(NodeSelectionHandler h) {
		return this.selectionHandlers.remove(h);
	}

	public void selectNodeWithSubs(GraphNode n) {
		clearSelection();
		ArrayList<GraphNode> l = new ArrayList<GraphNode>();
		selectNodeWithSubs(n, l);
		setSelectedNodes(l);
	}

	private void selectNodeWithSubs(GraphNode n,
			ArrayList<GraphNode> nodesToSelect) {
		if (nodesToSelect.contains(n)) {
			return;
		}
		nodesToSelect.add(n);
		Iterator<GraphEdge> itEdgesFromThis = n.getEdgesFrom().iterator();
		while (itEdgesFromThis.hasNext()) {
			selectNodeWithSubs(itEdgesFromThis.next().getTo(), nodesToSelect);
		}
	}

	public void setBlurred(boolean blurr) {
		String browser = Window.Navigator.getUserAgent();
		String browserVersion = Window.Navigator.getAppVersion();
		RegExp p = RegExp.compile("([0-9]+\\.?[0-9]*)");

		MatchResult m = p.exec(browserVersion);
		double browserVer = 0;

		if (m.getGroupCount() > 1) {
			browserVer = Double.parseDouble(m.getGroup(0));
		}

		if (browser.matches(".*[fF]irefox.*") && browserVer >= 3) {

			if (blurr) {
				this.getRaphaelElement().getFirstChildElement().setAttribute(
						"filter", "url(#Gaussian_Blur)");
			} else {
				this.getRaphaelElement().getFirstChildElement()
						.removeAttribute("filter");
			}
		}
	}

	/**
	 * @param canvasMenu
	 *            the canvasMenu to set
	 */
	public void setCanvasMenu(ContextMenu canvasMenu) {
		this.canvasMenu = canvasMenu;
	}

	/**
	 * @param m
	 *            the m to set
	 */
	public void setContextMenu(NodeContextMenu m) {
		this.m = m;
	}

	public void setGraphEdgeModifier(GraphEdgeModifier gem) {
		this.gem = gem;
	}

	public void setGraphNodeModifier(GraphNodeModifier gnm) {
		this.gnm = gnm;
	}

	/**
	 * Sets the height ot the canvas
	 * 
	 * @param y
	 */

	public void setHeight(int y) {
		this.height = y;
		this.overlay().setSize(width, height);
		this.setHeight(y + "px");
		updateZoom();
	}

	/**
	 * @param hoverNode
	 *            the hoverNode to set
	 */
	public void setHoverNode(GraphNode hoverNode) {
		this.hoverNode = hoverNode;
	}

	public void setMargin(int x, int y) {
		marginTop = y;
		marginLeft = x;

		this.getRaphaelElement().getStyle().setMarginLeft(x, Unit.PX);
		this.getRaphaelElement().getStyle().setMarginTop(y, Unit.PX);
	}

	/**
	 * @param mouseOverEdge
	 *            the mouseOverEdge to set
	 */
	public void setMouseOverEdge(boolean mouseOverEdge) {
		this.mouseOverEdge = mouseOverEdge;
	}

	public void setMouseOverNode(boolean mouseOverNode) {
		this.mouseOverNode = mouseOverNode;
	}

	public void setNotActive(boolean isHidden) {
		this.isNotActive = isHidden;
		if (!isHidden && sortOnActivation) {
			layout(false);
			sortOnActivation = false;
		}
	}

	public void setPadding(int x, int y) {
		marginTop = y;
		marginLeft = x;

		this.getRaphaelElement().getStyle().setPaddingLeft(x, Unit.PX);
		this.getRaphaelElement().getStyle().setPaddingTop(y, Unit.PX);
	}

	public void setPopup(NodePopup popup) {
		this.popup = popup;
	}

	/**
	 * @param preventHoverMenu
	 *            the preventHoverMenu to set
	 */
	public void setPreventHoverMenu(boolean preventHoverMenu) {
		this.preventHoverMenu = preventHoverMenu;
	}

	public void setSelectedEdge(GraphEdge n) {
		ArrayList<GraphEdge> t = new ArrayList<GraphEdge>();
		t.add(n);
		setSelectedEdges(t);
	}

	public void setSelectedEdges(ArrayList<GraphEdge> edges) {
		clearSelection();
		Iterator<GraphEdge> it = edges.iterator();
		while (it.hasNext()) {
			GraphEdge e = it.next();
			this.selectedEdges.put(new Tuple(e.getFrom().getId(), e.getTo()
					.getId()), e);
			getGraphEdgeModifier().setSelected(e);
		}
	}

	public void setSelectedNode(GraphNode n) {
		ArrayList<GraphNode> t = new ArrayList<GraphNode>();
		t.add(n);
		setSelectedNodes(t);
	}

	/**
	 * Set the GraphNodes nodes selected
	 * 
	 * @param n
	 */

	public void setSelectedNodes(ArrayList<GraphNode> nodes) {
		Iterator<GraphNode> it = nodes.iterator();
		if (popupDelay != null) {
			popupDelay.cancel();
		}

		clearSelection();
		while (it.hasNext()) {
			GraphNode n = it.next();
			Iterator<NodeSelectionHandler> itH = selectionHandlers.iterator();
			boolean dontSelect = false;
			while (itH.hasNext()) {
				if (!itH.next().isSelected(n)) {
					dontSelect = true;
				}
			}
			if (!dontSelect) {
				this.selected.put(n.getId(), n);
				getGraphNodeModifier().setSelected(n);
			}
		}
	}

	/**
	 * Sets the size of the canvas to the given dimension
	 * 
	 * @param x
	 * @param y
	 */

	public void setSize(int x, int y) {
		this.height = y;
		this.width = x;
		this.overlay().setSize(width, height);
		this.setWidth(x + "px");
		this.setHeight(y + "px");
		updateZoom();
	}

	/**
	 * Sets the width of the canvas
	 * 
	 * @param x
	 *            the width to be set
	 */

	public void setWidth(int x) {
		this.width = x;
		this.overlay().setSize(width, height);
		this.setWidth(x + "px");
		updateZoom();
	}

	public void showCanvasContextMenu(int clientX, int clientY) {
		hidePopUp();
		unbugMe();
		getCanvasMenu().show(clientX + Window.getScrollLeft(),
				clientY + Window.getScrollTop());
	}

	public void showContextMenu(GraphNode graphNode, int clientX, int clientY) {
		hidePopUp();
		unbugMe();
		getContextMenu().show(graphNode, clientX + Window.getScrollLeft(),
				clientY + Window.getScrollTop());
	}

	public void showEdges() {
		Iterator<GraphNode> i = nodes.iterator();
		while (i.hasNext()) {
			this.getGraphNodeModifier()
					.showEdges(i.next(), !this.isNotActive());
		}
	}

	/**
	 * Sort this canvas with GraphSorter s
	 * 
	 * @param s
	 *            the GraphSorter to use.
	 */
	public void sort(GraphSorter s) {
		GraphNodeLayoutingMachine m = new GraphNodeLayoutingMachine(this, gnm);
		AlgebraEditor.log(isNotActive ? "Sorting hidden graph..."
				: "Sorting visible graph...");
		m.sortGraph(nodes, edges, s, !isNotActive);
		sortOnActivation = isNotActive;
	}

	public void unbugMe() {
		if (dragNode != null) {
			dragNode.setDragged(false);
			dragNode = null;
		}
		FullScreenDragPanel.clearDrag();
	}

	/**
	 * Unhangs a ConnectedShape from a node
	 * 
	 * @param identifier
	 *            the identifier string of the ConnectedShape to unhang
	 * @param nid
	 *            the id of the nod the ConnectedShape should be unhanged from
	 */
	public void unHangShapeFromNode(String identifier, int nid) {
		gnm.removeShapeFromNode(identifier, this.getGraphNodeById(nid));
	}

	/**
	 * Unhangs a ConnectedWidget from a node. Acts like unHangShapeFromNode()
	 * except for the fact that it it for GWT-widgets instead of shapes.
	 * 
	 * @param identifier
	 *            the identifier string of the ConnectedWidget to unhang
	 * @param nid
	 *            the id of the nod the ConnectedWidget should be unhanged from
	 */
	public void unHangWidgetFromNode(String string, int nid) {
		this.remove(gnm.unhandWidgetToNode(string, getGraphNodeById(nid)));
	}

	private void updateZoom() {
		Iterator<GraphNode> i = nodes.iterator();
		while (i.hasNext()) {
			GraphNode current = i.next();
			gnm.checkDimension(current, current.getX(), current.getY());
			gnm.updateConnectedWidgets(current);
		}

		this.getRaphaelElement().getFirstChildElement().setAttribute(
				"viewBox",
				"0 0 " + (int) (this.width * scale) + " "
						+ (int) (this.height * scale));
	}

	/**
	 * Zoom the canvas
	 * 
	 * @param percent
	 *            the percent to zoom to
	 */
	public void zoom(double percent) {
		if (percent < 5) {
			percent = 5;
		}

		this.oldScrollX = Window.getScrollLeft();
		this.oldScrollY = Window.getScrollTop();

		this.oldWidth = (int) (width * scale);
		this.oldHeight = (int) (height * scale);

		scale = 100 / percent;

		updateZoom();

		if (oldScrollX + (oldWidth - width * scale) / 2 <= 0
				|| oldScrollY + (oldScrollY + (oldHeight - height * scale) / 2) <= 0) {
			this.oldWidth = (int) (width * scale);
			this.oldHeight = (int) (height * scale);
		}
	}

	/**
	 * Zooms in
	 */
	public void zoomIn() {
		zoom(1 / getScale() * 100 + 10);
	}

	/**
	 * Zooms out
	 */
	public void zoomOut() {
		zoom(1 / getScale() * 100 - 10);
	}
}
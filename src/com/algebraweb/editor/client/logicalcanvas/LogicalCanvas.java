package com.algebraweb.editor.client.logicalcanvas;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.AlgebraEditorCanvasView;
import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.PlanSwitchButton;
import com.algebraweb.editor.client.RemoteConfiguration;
import com.algebraweb.editor.client.graphcanvas.ConnectedShape;
import com.algebraweb.editor.client.graphcanvas.ConnectedWidget;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.NodeSelectionHandler;
import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * A wrapper for the GraphCanvas. Provides methods needed for the editor's view
 * 
 * @author Patrick Brosi
 * 
 */
public class LogicalCanvas extends GraphCanvas implements
		AlgebraEditorCanvasView {

	private int state = 0;
	private int id;
	private final PlanModelManipulator m;
	private String addingModeNodeType;
	private GraphNode drawEdgeTo;
	private GraphNode drawEdgeFrom;
	private int drawEdgeFromPos;
	private NewEdgeDrawer currentNewEdgeDrawer;
	private PlanSwitchButton myTabButton;
	private boolean overEdgeConnector = false;
	private Map<Integer, SQLBubble> sqlBubbleList = new HashMap<Integer, SQLBubble>();
	private int errorCount = 0;

	public LogicalCanvas(int id, final PlanModelManipulator m, int width,
			int height, RemoteConfiguration config, PlanSwitchButton myTabButton) {
		super(width, height, config.isInvertArrows());
		this.id = id;
		this.m = m;
		this.myTabButton = myTabButton;

		super.addNodeSelectionHandler(new NodeSelectionHandler() {
			@Override
			public boolean isSelected(GraphNode node) {
				switch (state) {
				case 2:
					break;
				case 3:
					drawEdgeTo = node;
					clearDrag();
					currentNewEdgeDrawer.getPath().remove();
					currentNewEdgeDrawer.getArrowPath().remove();
					state = 0;
					setPreventHoverMenu(false);
					Iterator<GraphNode> it = LogicalCanvas.this.getNodes()
							.iterator();

					while (it.hasNext()) {
						GraphNode n = it.next();
						clearEdgeConnectors(n);
					}
					m.addEdge(new Tuple(drawEdgeFrom.getId(), drawEdgeTo
							.getId()), LogicalCanvas.this.getId(),
							drawEdgeFromPos);
					return false;
				}
				return true;
			}
		});

		super.addDomHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (state == 3) {
					setOverEdgeConnector(false);
					if (getHoverNode() != null
							&& mouseOverNode(event.getClientX(), event
									.getClientY())) {
						currentNewEdgeDrawer.moveTo(getHoverNode().getX()
								+ getHoverNode().getWidth() / 2, getHoverNode()
								.getY());
					} else {
						currentNewEdgeDrawer
								.moveTo(
										getScale()
												* (event
														.getRelativeX(LogicalCanvas.this
																.getRaphaelElement()) - getMarginLeft()),
										getScale()
												* (event
														.getRelativeY(LogicalCanvas.this
																.getRaphaelElement()) - getMarginTop()));
					}
				}
			}
		}, MouseMoveEvent.getType());

		super.addDomHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				if (!isOverEdgeConnector()
						&& !mouseOverNode(event.getClientX(), event
								.getClientY()) && (state == 3 || state == 2)) {
					state = 0;
					setPreventHoverMenu(false);
					Iterator<GraphNode> it = LogicalCanvas.this.getNodes()
							.iterator();
					if (currentNewEdgeDrawer != null) {
						currentNewEdgeDrawer.getPath().remove();
						currentNewEdgeDrawer.getArrowPath().remove();
					}
					while (it.hasNext()) {
						GraphNode n = it.next();
						clearEdgeConnectors(n);
					}
				}
				if (state == 1) {
					state = 0;
					LogicalCanvas.this.removeStyleName("node-adding");
					int x = -marginLeft
							+ (int) (getScale() * event
									.getRelativeX(LogicalCanvas.super
											.getElement()));
					int y = -marginTop
							+ (int) (getScale() * event
									.getRelativeY(LogicalCanvas.super
											.getElement()));
					LogicalCanvas.this.m.addNode(LogicalCanvas.this.getId(),
							addingModeNodeType, x, y);
				}
			}

		}, MouseUpEvent.getType());
	}

	public void addSQLListener(int nid, EvaluationContext c) {
		SQLBubble listener = new SQLBubble(nid, this.getId(), m
				.getManipulationService(), c, this);
		GraphNode n = getGraphNodeById(nid);
		super.hangWidgetOntoNode("sql-listener", new ConnectedWidget(listener,
				n.getWidth(), 0), nid);
		sqlBubbleList.put(nid, listener);
		listener.update();

	}

	private void clearEdgeConnectors(GraphNode n) {
		for (int i = 0; i < n.getFixedChildCount(); i++) {
			unHangShapeFromNode("edge_circle_pos" + (i + 1), n.getId());
		}
	}

	public void clearErroneous() {
		errorCount = 0;
		myTabButton.setErrorCount(errorCount);
		Iterator<GraphNode> it = super.getNodes().iterator();

		while (it.hasNext()) {
			GraphNode current = it.next();
			if (current.getConnectedShapes().containsKey("__logicalplan_error")) {
				super.unHangShapeFromNode("__logicalplan_error", current
						.getId());
			}
		}
	}

	private MouseUpHandler createEdgeShapeMouseHandler(final GraphNode n,
			final int pos) {
		return new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				state = 3;
				drawEdgeFrom = n;
				drawEdgeFromPos = pos;

				double y = n.getHeight() + 3 + n.getY();
				double x = n.getX() + n.getWidth()
						/ (getFreeChildEdgePositions(n).size() + 1) * pos;

				currentNewEdgeDrawer = new NewEdgeDrawer(x, y,
						LogicalCanvas.this);
				Iterator<GraphNode> it = LogicalCanvas.this.getNodes()
						.iterator();

				while (it.hasNext()) {
					GraphNode n = it.next();
					clearEdgeConnectors(n);
				}
			}
		};
	}

	public void enterEdgeAddingMode() {
		Iterator<GraphNode> it = super.getNodes().iterator();
		boolean hasConnectors = false;

		while (it.hasNext()) {
			GraphNode n = it.next();
			Map<Integer, Boolean> freeChilds = getFreeChildEdgePositions(n);
			Iterator<Integer> itt = freeChilds.keySet().iterator();

			while (itt.hasNext()) {
				int current = itt.next();
				if (freeChilds.get(current).booleanValue()) {
					hasConnectors = true;
					Circle cr = new Circle(0, 0, 6);
					cr.getElement().setAttribute("class", "edge-connector");
					cr.addDomHandler(createEdgeShapeMouseHandler(n, current),
							MouseUpEvent.getType());
					cr.addDomHandler(new MouseMoveHandler() {
						@Override
						public void onMouseMove(MouseMoveEvent event) {
							setOverEdgeConnector(true);

						}
					}, MouseMoveEvent.getType());
					cr.addDomHandler(new MouseOutHandler() {
						@Override
						public void onMouseOut(MouseOutEvent event) {
							setOverEdgeConnector(false);
						}
					}, MouseOutEvent.getType());
					int y = n.getHeight() + 3;
					int x = n.getWidth() / (freeChilds.size() + 1) * current;

					ConnectedShape csr = new ConnectedShape(cr, x + 3, y);
					super.hangShapeOntoNode("edge_circle_pos" + current, csr, n
							.getId());
				}
			}

			if (hasConnectors) {
				state = 2;
				setPreventHoverMenu(true);
			}
		}
	}

	public void enterNodeAddingMode(String addingModeNodeType) {
		state = 1;
		this.addingModeNodeType = addingModeNodeType;
		this.addStyleName("node-adding");
	}

	private GraphEdge getEdgeWithParentPos(List<GraphEdge> list, int p) {
		Iterator<GraphEdge> it = list.iterator();
		while (it.hasNext()) {
			GraphEdge cur = it.next();
			if (cur.getFixedParentPos() == p) {
				return cur;
			}
		}

		return null;
	}

	private Map<Integer, Boolean> getFreeChildEdgePositions(GraphNode n) {
		Map<Integer, Boolean> ret = new HashMap<Integer, Boolean>();
		for (int i = 0; i < n.getFixedChildCount(); i++) {
			GraphEdge cur = getEdgeWithParentPos(n.getEdgesFrom(), i + 1);
			if (cur == null) {
				ret.put(i + 1, true);
			} else {
				ret.put(i + 1, false);
			}
		}
		return ret;
	}

	public int getId() {
		return id;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	/**
	 * @return the overEdgeConnector
	 */
	protected boolean isOverEdgeConnector() {
		return overEdgeConnector;
	}

	public void removeSQLListener(SQLBubble b) {
		super.unHangWidgetFromNode("sql-listener", b.getNid());
		sqlBubbleList.remove(b);
	}

	public void setErroneous(int nid) {
		if (!(super.getGraphNodeById(nid) == null)
				&& !super.getGraphNodeById(nid).getConnectedShapes()
						.containsKey("__logicalplan_error")) {
			errorCount++;
			myTabButton.setErrorCount(errorCount);
			Text errorMark = new Text(0, 0, "!");
			errorMark.getElement().setAttribute("class",
					"logicalplan-node-errormark-text");
			errorMark.attr("fill", "red");
			errorMark.attr("font", "35px Arial");
			errorMark.attr("style", "text-anchor: middle; font: 35px Arial;");
			super.hangShapeOntoNode("__logicalplan_error", new ConnectedShape(
					errorMark, -6, -8), nid);
		}
	}

	/**
	 * @param overEdgeConnector
	 *            the overEdgeConnector to set
	 */
	protected void setOverEdgeConnector(boolean overEdgeConnector) {
		this.overEdgeConnector = overEdgeConnector;
	}

	public void updateSQLListener() {
		Iterator<SQLBubble> it = sqlBubbleList.values().iterator();
		while (it.hasNext()) {
			it.next().update();
		}
	}
}
package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;
import com.hydro4ge.raphaelgwt.client.AnimationCallback;
import com.hydro4ge.raphaelgwt.client.Raphael.Circle;
import com.hydro4ge.raphaelgwt.client.Raphael.Text;

public class GraphNodeModifier {

	private GraphCanvas c;

	public GraphNodeModifier(GraphCanvas c) {
		this.c = c;
	}
	
	public static MouseMoveHandler mouseMoveHandlerBuilder(final GraphNode n) {
		return new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				FullScreenDragPanel.preventDrag();
				n.getShape().attr("fill-opacity", 0.9);
			}
		};
	}

	public static MouseOutHandler mouseOutHandlerBuilder(final GraphNode n) {
		return new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				FullScreenDragPanel.unPreventDrag();
				n.getShape().attr("fill-opacity", 1);
			}
		};
	}

	protected void animateTo(GraphNode n, double x, double y) {
		n.setAniLock();
		n.setX(x);
		n.setY(y);

		checkDimension(n, x, y);
		update(n, true, true);

		JSONObject newAttrs = new JSONObject();

		newAttrs.put("x", new JSONNumber(x));
		newAttrs.put("y", new JSONNumber(y));

		int i = 0;

		for (Text t : n.getText()) {
			JSONObject newAttrsText = new JSONObject();

			newAttrsText.put("x", new JSONNumber(x + n.getWidth() / 2));
			newAttrsText.put("y", new JSONNumber(y + n.getLineHeight() / 2 + 5
					+ i * n.getLineHeight()));

			t.animate(newAttrsText, 1000, "backIn");
			i++;
		}

		n.getShape().animate(newAttrs, 1000, "backIn",
				buildAnimationCallback(n));

		Iterator<ConnectedShape> it = n.getConnectedShapes().values()
				.iterator();

		while (it.hasNext()) {

			ConnectedShape current = it.next();
			JSONObject newAttrsShape = new JSONObject();
			if (current.getShape() instanceof Circle) {
				newAttrsShape.put("cx", new JSONNumber(x + current.getX()));
				newAttrsShape.put("cy", new JSONNumber(y + current.getY()));
			} else {
				newAttrsShape.put("x", new JSONNumber(x + current.getX()));
				newAttrsShape.put("y", new JSONNumber(y + current.getY()));
			}
			current.getShape().animate(newAttrsShape, 1000, "backIn");
		}

		Iterator<ConnectedWidget> itw = n.getConnectedWidgets().values()
				.iterator();

		while (itw.hasNext()) {
			ConnectedWidget current = itw.next();
			current
					.getElement()
					.getStyle()
					.setTop(
							((y + n.getHeight() / 2 + current.getY())
									/ c.getScale() + c.getMarginTop()), Unit.PX);
			current.getElement().getStyle().setLeft(
					(x + current.getX()) / c.getScale() + c.getMarginLeft(),
					Unit.PX);
		}
	}

	private AnimationCallback buildAnimationCallback(final GraphNode n) {
		return new AnimationCallback() {
			@Override
			public void onComplete() {
				// update(n,true);
				n.unsetAniLock();
				showEdges(n, true);
			}
		};
	}

	protected void checkDimension(GraphNode n, double x, double y) {
		if (x / c.getScale() + n.getWidth() / c.getScale() > c.getWidth()) {
			c
					.setWidth((int) (c.getWidth() + 50 / c.getScale() + (x
							/ c.getScale() + n.getWidth() / c.getScale() - c
							.getWidth())));
		}

		if (y / c.getScale() + n.getHeight() / c.getScale() > c.getHeight()) {
			c.setHeight((int) (c.getHeight() + 50 / c.getScale() + (y
					/ c.getScale() + n.getHeight() / c.getScale() - c
					.getHeight())));
		}
	}

	protected void connectShapeToNode(String identifier, ConnectedShape s,
			GraphNode n) {
		n.getConnectedShapes().put(identifier, s);

		if (s.getShape() instanceof Circle) {

			s.getShape().attr("cx", n.getX() + s.getX());
			s.getShape().attr("cy", n.getY() + s.getY());

		} else {

			s.getShape().attr("x", n.getX() + s.getX());
			s.getShape().attr("y", n.getY() + s.getY());

		}
	}

	public void connectWidgetToNode(String identifier, ConnectedWidget w,
			GraphNode n) {
		n.getConnectedWidgets().put(identifier, w);
		w.getElement().getStyle().setTop(
				(n.getY() + n.getHeight() / 2 + w.getY()) / c.getScale()
						+ c.getMarginTop(), Unit.PX);
		w.getElement().getStyle().setLeft(
				(n.getX() + w.getX()) / c.getScale() + +c.getMarginLeft(),
				Unit.PX);
	}

	protected void dimOut(GraphNode n) {
		JSONObject newAttrs = new JSONObject();
		newAttrs.put("opacity", new JSONNumber(0));
		n.getShape().animate(newAttrs, 1000);
		for (Text t : n.getText()) {
			t.animate(newAttrs, 1000);
		}
		Iterator<ConnectedShape> it = n.getConnectedShapes().values()
				.iterator();

		while (it.hasNext()) {
			ConnectedShape current = it.next();
			current.getShape().animate(newAttrs, 1000);
		}
	}

	protected void edgesToFront(GraphNode n) {
		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) {
			c.getGraphEdgeModifier().toFront(i.next());
		}
		while (j.hasNext()) {
			c.getGraphEdgeModifier().toFront(j.next());
		}
	}

	public GraphCanvas getCanvas() {
		return c;
	}

	private List<GraphEdge> getEdgesByOrientation(int orientation,
			List<GraphEdge> e) {
		Iterator<GraphEdge> i = e.iterator();
		List<GraphEdge> temp = new ArrayList<GraphEdge>();

		while (i.hasNext()) {
			GraphEdge current = i.next();
			if (current.getOrientation() == orientation) {
				temp.add(current);
			}
		}
		return temp;
	}

	private List<GraphEdge> getEdgesByOrientationFrom(int orientation,
			List<GraphEdge> list) {

		Iterator<GraphEdge> i = list.iterator();
		List<GraphEdge> temp = new ArrayList<GraphEdge>();

		while (i.hasNext()) {
			GraphEdge current = i.next();
			if (current.getOrientationFrom() == orientation) {
				temp.add(current);
			}
		}
		return temp;
	}

	private int getGurrByOrientation(GraphNode n, int o) {

		if (o == 0 || o == 1) {
			return (int) n.getShape().getBBox().width();
		} else {
			return (int) n.getShape().getBBox().height();
		}
	}

	protected int getOffset(GraphNode n, int orientation, GraphEdge e,
			boolean hasChanged, int oldOrientation) {
		return getOffset(n, orientation, e, hasChanged, oldOrientation, false,
				false, false);
	}

	protected int getOffset(GraphNode n, int orientation, GraphEdge e,
			boolean hasChanged, int oldOrientation, boolean forceall,
			boolean quiet, boolean animated) {

		if (hasChanged) {
			getOffset(n, oldOrientation, null, false, -1, true, quiet, animated);
		}

		int ret = -1;

		int gurr = getGurrByOrientation(n, orientation);

		List<GraphEdge> edges = getEdgesByOrientation(orientation, n
				.getEdgesTo());
		List<GraphEdge> edgesF = getEdgesByOrientationFrom(orientation, n
				.getEdgesFrom());

		int length = edges.size() + edgesF.size();

		Iterator<GraphEdge> i = edges.iterator();
		Iterator<GraphEdge> a = edgesF.iterator();

		int c = 1;
		int step = Math.round(gurr / (length + 1));

		GraphEdge current;

		while (i.hasNext()) {
			current = i.next();
			if (forceall || !current.equals(e)) {
				this.c.getGraphEdgeModifier().updateOffset(current, step * c,
						quiet, animated);

			} else {
				ret = step * c;
			}
			c++;
		}

		while (a.hasNext()) {
			current = a.next();

			if (forceall || !current.equals(e)) {
				if (current.getFrom().getFixedChildCount() != -1
						&& current.getFixedParentPos() != -1) {

					step = Math.round(gurr
							/ (current.getFrom().getFixedChildCount() + 1));
					c = current.getFixedParentPos();
					this.c.getGraphEdgeModifier().updateOffsetFrom(current,
							step * c, quiet, animated);
				} else {
					this.c.getGraphEdgeModifier().updateOffsetFrom(current,
							step * c, quiet, animated);
					c++;
				}

			} else {
				if (current.getFrom().getFixedChildCount() != -1
						&& current.getFixedParentPos() != -1) {

					step = Math.round(gurr
							/ (current.getFrom().getFixedChildCount() + 1));

					ret = step * current.getFixedParentPos();
				} else {
					ret = step * c;
					c++;
				}
			}
		}

		return ret;
	}

	protected void hideEdges(GraphNode n, boolean animated) {
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();
		while (j.hasNext()) {
			c.getGraphEdgeModifier().snakeIn(j.next(), animated);
		}
	}

	protected void kill(GraphNode n) {
		for (Text t : n.getText()) {
			t.remove();
		}
		n.getShape().remove();

		Iterator<ConnectedShape> it = n.getConnectedShapes().values()
				.iterator();

		while (it.hasNext()) {
			ConnectedShape current = it.next();
			current.getShape().remove();
		}

		Iterator<ConnectedWidget> iter = n.getConnectedWidgets().values()
				.iterator();

		while (iter.hasNext()) {
			ConnectedWidget current = iter.next();
			c.remove(current);
		}
	}

	protected void moveTo(GraphNode n, double x, double y) {

		if ((n.getX() != x || n.getY() != x) && x >= 0 && y >= 0) {

			checkDimension(n, x, y);

			n.getShape().attr("x", x);
			n.getShape().attr("y", y);

			int i = 0;
			for (Text t : n.getText()) {
				t.attr("x", x + n.getWidth() / 2);
				t.attr("y", y + n.getLineHeight() / 2 + 5 + n.getLineHeight()
						* i);
				i++;
			}

			Iterator<String> it = n.getConnectedShapes().keySet().iterator();

			while (it.hasNext()) {

				String cur = it.next();
				ConnectedShape current = n.getConnectedShapes().get(cur);

				if (current.getShape() instanceof Circle) {
					current.getShape().attr("cx", x + current.getX());
					current.getShape().attr("cy", y + current.getY());
				} else {
					current.getShape().attr("x", x + current.getX());
					current.getShape().attr("y", y + current.getY());
				}

			}

			Iterator<ConnectedWidget> itw = n.getConnectedWidgets().values()
					.iterator();

			while (itw.hasNext()) {

				ConnectedWidget current = itw.next();
				current.getElement().getStyle().setTop(
						(y + n.getHeight() / 2 + current.getY()) / c.getScale()
								+ c.getMarginTop(), Unit.PX);
				current.getElement().getStyle()
						.setLeft(
								(x + current.getX()) / c.getScale()
										+ c.getMarginLeft(), Unit.PX);
			}

			n.setX(x);
			n.setY(y);
			update(n, false, false);
		}
	}

	protected void removeEdge(GraphNode n, GraphEdge e) {

		n.getEdgesTo().remove(e);
		n.getEdgesFrom().remove(e);
		getOffset(n, e.getOrientationFrom(), null, false, -1, true, false,
				false);
		getOffset(n, e.getOrientation(), null, false, -1, true, false, false);

	}

	protected void removeShapeFromNode(String identifier, GraphNode n) {
		ConnectedShape s = n.getConnectedShapes().get(identifier);
		if (s != null) {
			s.getShape().remove();
			n.getConnectedShapes().remove(identifier);
		}
	}

	protected void setNotSelected(GraphNode n) {
		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-width", new JSONNumber(1));
		n.getShape().animate(newAttrs, 300);
		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) {
			c.getGraphEdgeModifier().setNotSelected(i.next());
		}
		while (j.hasNext()) {
			c.getGraphEdgeModifier().setNotSelected(j.next());
		}
	}

	protected void setSelected(GraphNode n) {
		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-width", new JSONNumber(3));
	n.getShape().animate(newAttrs, 300);

		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) {
			c.getGraphEdgeModifier().setSelected(i.next());
		}
		while (j.hasNext()) {
			c.getGraphEdgeModifier().setSelected(j.next());
		}
	}

	protected void showEdges(GraphNode n, boolean animated) {
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();
		while (j.hasNext()) {
			c.getGraphEdgeModifier().snakeOut(j.next(), animated);
		}
	}

	public Widget unhandWidgetToNode(String identifier, GraphNode n) {
		Widget w = n.getConnectedWidgets().get(identifier);
		n.getConnectedWidgets().remove(identifier);
		return w;
	}

	protected void update(GraphNode n, boolean quiet, boolean animated) {
		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) {
			c.getGraphEdgeModifier().update(i.next(), quiet, animated);
		}
		while (j.hasNext()) {
			c.getGraphEdgeModifier().update(j.next(), quiet, animated);
		}
	}

	protected void updateConnectedWidgets(GraphNode n) {
		Iterator<ConnectedWidget> itw = n.getConnectedWidgets().values()
				.iterator();

		while (itw.hasNext()) {
			ConnectedWidget current = itw.next();
			current.getElement().getStyle().setTop(
					(n.getY() + n.getHeight() / 2 + current.getY())
							/ c.getScale() + c.getMarginTop(), Unit.PX);
			current.getElement().getStyle().setLeft(
					(n.getX() + current.getX()) / c.getScale()
							+ c.getMarginLeft(), Unit.PX);
		}
	}

}

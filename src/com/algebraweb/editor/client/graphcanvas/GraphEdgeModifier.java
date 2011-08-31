package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.hydro4ge.raphaelgwt.client.AnimationCallback;

/**
 * Provides certain edge manipulation methodes. This class uses the algorithm
 * from http://raphaeljs.com/graffle.html for edge calculation.
 * 
 * @author Patrick Brosi
 * 
 */

public class GraphEdgeModifier {

	private GraphCanvas c;

	public GraphEdgeModifier(GraphCanvas c) {
		this.c = c;
	}

	private Tuple calcOrientation(GraphEdge e, GraphNode from, GraphNode to) {
		Tuple[] p = new Tuple[8];

		int fromWidth = from.getWidth();
		int fromHeight = from.getHeight();
		double fromX = from.getX();
		double fromY = from.getY();

		int toWidth = to.getWidth();
		int toHeight = to.getHeight();
		double toX = to.getX();
		double toY = to.getY();

		int offSetTo = 0;
		int offSetFrom = 0;

		if (!c.isInvertArrows()) {
			offSetTo = e.getArrowSize();
		} else {
			offSetFrom = e.getArrowSize();
		}

		// generate the path the connection will be drawn along

		p[0] = new Tuple(fromX + fromWidth / 2, fromY - 1 - offSetFrom);
		p[1] = new Tuple(fromX + fromWidth / 2, fromY + fromHeight + 1
				+ offSetFrom);
		p[2] = new Tuple(fromX - 1 - offSetFrom, fromY + fromHeight / 2);
		p[3] = new Tuple(fromX + fromWidth + 1 + offSetFrom, fromY + fromHeight
				/ 2);

		p[4] = new Tuple(toX + toWidth / 2, toY - 1 - offSetTo);
		p[5] = new Tuple(toX + toWidth / 2, toY + toHeight + 1 + offSetTo);
		p[6] = new Tuple(toX - 1 - offSetTo, toY + toHeight / 2);
		p[7] = new Tuple(toX + toWidth + 1 + offSetTo, toY + toHeight / 2);

		e.setP(p);

		Map<Integer, Tuple> d = new HashMap<Integer, Tuple>();
		List<Integer> dis = new ArrayList<Integer>();

		for (int i = 0; i < 4; i++) {
			for (int j = 4; j < 8; j++) {
				double dx = Math.abs(p[i].getX() - p[j].getX());
				double dy = Math.abs(p[i].getY() - offSetTo - p[j].getY());

				if (i == j - 4
						|| (i != 3 && j != 6 || p[i].getX() - offSetTo < p[j]
								.getX()
								+ offSetTo)

						&& (i != 2 && j != 7 || p[i].getX() + offSetTo > p[j]
								.getX()
								- offSetTo)
						&& (i != 0 && j != 5 || p[i].getY() - offSetTo > p[j]
								.getY()
								+ offSetTo)
						&& (i != 1 && j != 4 || p[i].getY() + offSetTo < p[j]
								.getY()
								- offSetTo)) {

					// TODO: if tree!!!
					if (j != 5) {

						dis.add((int) (dx + dy));
						d.put(dis.get(dis.size() - 1), new Tuple(i, j));

					}
				}
			}
		}

		Tuple res;

		if (dis.size() == 0) {
			res = new Tuple(0, 4);
		} else {
			res = d.get(getSmallestVal(dis));

			// TODO: if tree!!!!!!!
			res = new Tuple(1, res.getY());
		}

		return res;
	}

	protected void deleteFromFrom(GraphEdge e) {
		c.getGraphNodeModifier().removeEdge(e.getFrom(), e);
	}

	protected void deleteFromTo(GraphEdge e) {
		c.getGraphNodeModifier().removeEdge(e.getTo(), e);
	}

	protected void drawEdge(GraphEdge e, boolean quiet, boolean animated) {

		if (e.getEdgePath() == null) {
			e.setEdgePath(c.new Path());
			e.getEdgePath().hide();
		}

		if (c.isInvertArrows()) {
			e.setPathStringSmall("M" + e.getX4() + "," + e.getY4());
		} else {
			e.setPathStringSmall("M" + e.getX1() + "," + e.getY1());
		}

		e.setEdgePathString("M" + e.getX1() + "," + e.getY1() + "C" + e.getX2()
				+ "," + e.getY2() + "," + e.getX3() + "," + e.getY3() + ","
				+ e.getX4() + "," + e.getY4());

		if (!quiet) {
			e.getEdgePath().stop();

			if (e.isSnakingIn()) {
				snakeInCallbackBuilder(e).onComplete();
			}
			if (e.isSnakingOut()) {
				snakeCallbackBuilder(e).onComplete();
			}

			e.getEdgePath().attr("path", e.getEdgePathString());
		}

		double[] xA = { e.getX4(), e.getX4(), e.getX4() + e.getArrowSize(),
				e.getX4() - e.getArrowSize() };
		double[] yA = { e.getY4() + e.getArrowSize(),
				e.getY4() - e.getArrowSize(), e.getY4(), e.getY4() };

		double[] xB = { e.getX1(), e.getX1(), e.getX1() + e.getArrowSize(),
				e.getX1() - e.getArrowSize() };
		double[] yB = { e.getY1() + e.getArrowSize(),
				e.getY1() - e.getArrowSize(), e.getY1(), e.getY1() };

		double angle = Math.atan2(e.getX3() - xA[e.getToPosition()], yA[e
				.getToPosition()]
				- e.getY3());
		angle = angle / (2 * Math.PI) * 360;

		String arrowPath;
		int toSwitch;

		if (!c.isInvertArrows()) {
			toSwitch = e.getToPosition();
			arrowPath = "M" + xA[e.getToPosition()] + ","
					+ yA[e.getToPosition()] + ","
					+ (xA[e.getToPosition()] - e.getArrowSize()) + ","
					+ (yA[e.getToPosition()] - e.getArrowSize()) + " L"
					+ (xA[e.getToPosition()] - e.getArrowSize()) + ","
					+ (yA[e.getToPosition()] + e.getArrowSize()) + " L"
					+ xA[e.getToPosition()] + "," + yA[e.getToPosition()];
		} else {
			toSwitch = e.getFromPosition();
			arrowPath = "M" + xB[e.getFromPosition()] + ","
					+ yB[e.getFromPosition()] + ","
					+ (xB[e.getFromPosition()] - e.getArrowSize()) + ","
					+ (yB[e.getFromPosition()] - e.getArrowSize()) + " L"
					+ (xB[e.getFromPosition()] - e.getArrowSize()) + ","
					+ (yB[e.getFromPosition()] + e.getArrowSize()) + " L"
					+ xB[e.getFromPosition()] + "," + yB[e.getFromPosition()];

		}

		if (e.getArrowPath() == null) {
			e.setArrowPath(c.new Path());
			e.getArrowPath().hide();
		}

		e.getArrowPath().attr("path", arrowPath);
		e.getArrowPath().attr("stroke-width", "1");
		e.getArrowPath().attr("fill", "#FAFAFA");

		switch (toSwitch) {

		case 0:
			angle = 90;
			break; // oben
		case 1:
			angle = 270;
			break; // unten
		case 2:
			angle = 0;
			break; // left
		case 3:
			angle = 180;
			break; // right

		}

		if (!c.isInvertArrows()) {
			e.getArrowPath().rotate(angle, xA[e.getToPosition()],
					yA[e.getToPosition()]);
		} else {
			e.getArrowPath().rotate(angle, xB[e.getFromPosition()],
					yB[e.getFromPosition()]);
		}

		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-opacity", new JSONNumber(1));

		if (!quiet) {
			snakeOut(e, animated);
		}

	}

	protected void generatePath(GraphEdge e, GraphNode from, GraphNode to,
			int toPosition, int fromPosition) {

		int fromHeight = from.getHeight();
		double fromX = from.getX();
		double fromY = from.getY();

		int toWidth = to.getWidth();
		int toHeight = to.getHeight();
		double toX = to.getX();
		double toY = to.getY();

		int offSetTo = 0;
		int offSetFrom = 0;

		if (!c.isInvertArrows()) {
			offSetTo = e.getArrowSize();
		} else {
			offSetFrom = e.getArrowSize();
		}

		Tuple[] p = e.getP();

		p[0] = new Tuple(fromX + e.getOffsetFrom(), fromY - 1 - offSetFrom);
		p[1] = new Tuple(fromX + e.getOffsetFrom(), fromY + fromHeight + 1
				+ offSetFrom);
		p[2] = new Tuple(fromX - 1 - offSetFrom, fromY + e.getOffsetFrom());
		p[3] = new Tuple(fromX + from.getWidth() + 1 + offSetFrom, fromY
				+ e.getOffsetFrom());

		p[4] = new Tuple(toX + e.getOffset(), toY - 1 - offSetTo);
		p[5] = new Tuple(toX + e.getOffset(), toY + toHeight + 1 + offSetTo);
		p[6] = new Tuple(toX - 1 - offSetTo, toY + e.getOffset());
		p[7] = new Tuple(toX + toWidth + 1 + offSetTo, toY + e.getOffset());

		e.setP(p);

		e.setX1(p[fromPosition].getX());
		e.setY1(p[fromPosition].getY());
		e.setX4(p[toPosition + 4].getX());
		e.setY4(p[toPosition + 4].getY());

		double dx = Math.max(Math.abs(e.getX1() - e.getX4()) / 2, 10);
		double dy = Math.max(Math.abs(e.getY1() - e.getY4()) / 2, 10);

		double[] x2l = { e.getX1(), e.getX1(), e.getX1() - dx, e.getX1() + dx };
		double[] y2l = { e.getY1() - dy, e.getY1() + dy, e.getY1(), e.getY1() };
		double[] x3l = { 0, 0, 0, 0, e.getX4(), e.getX4(), e.getX4() - dx,
				e.getX4() + dx };
		double[] y3l = { 0, 0, 0, 0, e.getY1() + dy, e.getY1() - dy, e.getY4(),
				e.getY4() };

		e.setX2(x2l[fromPosition]);
		e.setY2(y2l[fromPosition]);
		e.setX3(x3l[toPosition + 4]);
		e.setY3(y3l[toPosition + 4]);
	}

	private int getSmallestVal(List<Integer> dis) {
		Iterator<Integer> i = dis.iterator();
		int smallest = Integer.MAX_VALUE;
		int current;

		while (i.hasNext()) {
			current = i.next();
			if (current < smallest) {
				smallest = current;
			}
		}
		return smallest;
	}

	protected void hide(GraphEdge e) {
		e.getEdgePath().hide();
	}

	protected void hideArrow(GraphEdge e) {
		e.getArrowPath().hide();
	}

	protected void makeConnection(GraphEdge e, GraphNode from, GraphNode to,
			boolean quiet, boolean animated) {

		Tuple res = calcOrientation(e, from, to);

		int toPositionOld = e.getToPosition();
		int fromPositionOld = e.getFromPosition();

		e.setToPosition((int) res.getY() - 4);
		e.setFromPosition((int) res.getX());

		if (e.getOffset() == -1 || toPositionOld != e.getToPosition()) {
			e.setOffset(c.getGraphNodeModifier()
					.getOffset(
							to,
							e.getToPosition(),
							e,
							(toPositionOld != e.getToPosition() && e
									.getOffset() != -1), toPositionOld, false,
							quiet, animated));
		}

		if (e.getOffsetFrom() == -1 || fromPositionOld != e.getFromPosition()) {
			e.setOffsetFrom(c.getGraphNodeModifier().getOffset(
					from,
					e.getFromPosition(),
					e,
					(fromPositionOld != e.getFromPosition() && e
							.getOffsetFrom() != -1), fromPositionOld, false,
					quiet, animated));
		}

		generatePath(e, from, to, e.getToPosition(), e.getFromPosition());
		drawEdge(e, quiet, animated);
	}

	protected void setNotSelected(GraphEdge e) {
		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-width", new JSONNumber(1));
		e.getEdgePath().animate(newAttrs, 300);
		e.getArrowPath().animate(newAttrs, 300);
	}

	protected void setSelected(GraphEdge e) {

		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-width", new JSONNumber(2));
		e.getEdgePath().animate(newAttrs, 300);
		e.getArrowPath().animate(newAttrs, 300);

	}

	protected void show(GraphEdge e) {

		e.getEdgePath().show();

	}

	protected void showArrow(GraphEdge e) {

		e.getArrowPath().show();

	}

	protected AnimationCallback snakeCallbackBuilder(final GraphEdge e) {

		return new AnimationCallback() {

			@Override
			public void onComplete() {
				showArrow(e);
				e.setSnakingOut(false);
			}
		};
	}

	protected void snakeIn(GraphEdge e, boolean animated) {

		if (e.isSnakedIn()) {
			return;
		}
		e.setSnakedIn(true);

		if (animated) {
			e.setSnakingIn(true);
			hideArrow(e);
			e.getEdgePath().attr("path", e.getEdgePathString());
			JSONObject attrs = new JSONObject();
			attrs.put("path", new JSONString(e.getEdgePathSmallString()));
			e.getEdgePath().animate(attrs, 600, snakeInCallbackBuilder(e));

		} else {

			hideArrow(e);
			e.getEdgePath().attr("path", e.getEdgePathSmallString());
			snakeInCallbackBuilder(e).onComplete();

		}

	}

	protected AnimationCallback snakeInCallbackBuilder(final GraphEdge e) {

		return new AnimationCallback() {

			@Override
			public void onComplete() {
				hide(e);
				e.getEdgePath().attr("path", e.getEdgePathSmallString());
				e.setSnakingIn(false);
			}
		};
	}

	protected void snakeOut(GraphEdge e, boolean animated) {

		if (!e.isSnakedIn()) {
			return;
		}
		e.setSnakedIn(false);

		if (animated) {
			e.setSnakingOut(true);
			e.getEdgePath().attr("path", e.getEdgePathSmallString());
			show(e);
			JSONObject attrs = new JSONObject();
			attrs.put("path", new JSONString(e.getEdgePathString()));
			e.getEdgePath().animate(attrs, 600, snakeCallbackBuilder(e));
		} else {
			show(e);
			e.getEdgePath().attr("path", e.getEdgePathString());
			snakeCallbackBuilder(e).onComplete();

		}

	}

	protected void toBack(GraphEdge e) {
		e.getEdgePath().toBack();
		e.getArrowPath().toBack();
	}

	protected void toFront(GraphEdge e) {
		e.getEdgePath().toFront();
		e.getArrowPath().toFront();
	}

	protected void update(GraphEdge e, boolean quiet, boolean animated) {
		makeConnection(e, e.getFrom(), e.getTo(), quiet, animated);

	}

	protected void updateOffset(GraphEdge e, int count, boolean quiet,
			boolean animated) {

		e.setOffset(count);

		generatePath(e, e.getFrom(), e.getTo(), e.getToPosition(), e
				.getFromPosition());
		drawEdge(e, quiet, animated);

	}

	protected void updateOffsetFrom(GraphEdge e, int count, boolean quiet,
			boolean animated) {

		e.setOffsetFrom(count);

		generatePath(e, e.getFrom(), e.getTo(), e.getToPosition(), e
				.getFromPosition());
		drawEdge(e, quiet, animated);

	}

}

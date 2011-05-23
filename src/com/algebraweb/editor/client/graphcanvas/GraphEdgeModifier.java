package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.hydro4ge.raphaelgwt.client.AnimationCallback;


/**
 * Provides certain edge manipulation methodes
 * @author Patrick Brosi
 *
 */


public class GraphEdgeModifier {

	private GraphCanvas c;

	protected GraphEdgeModifier(GraphCanvas c) {

		this.c=c;

	}

	protected void setSelected(GraphEdge e) {

		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-width", new JSONNumber(2));
		e.getEdgePath().animate(newAttrs, 300);

	}

	protected void setNotSelected(GraphEdge e) {

		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-width", new JSONNumber(1));
		e.getEdgePath().animate(newAttrs, 300);

	}

	protected void show(GraphEdge e) {

		e.getEdgePath().show();

	}

	protected void hide(GraphEdge e) {

		e.getEdgePath().hide();

	}


	protected void showArrow(GraphEdge e) {

		e.getArrowPath().show();

	}

	protected void hideArrow(GraphEdge e) {

		e.getArrowPath().hide();

	}


	private AnimationCallback snakeInCallbackBuilder(final GraphEdge e) {

		return new AnimationCallback() {

			@Override
			public void onComplete() {
				hide(e);
			}
		};
	}


	private AnimationCallback snakeCallbackBuilder(final GraphEdge e) {

		return new AnimationCallback() {

			@Override
			public void onComplete() {
				showArrow(e);
			}
		};
	}


	protected void snakeOut(GraphEdge e) {

		if (!e.isSnakedIn()) return;
		e.setSnakedIn(false);
		e.getEdgePath().attr("path", e.getEdgePathSmallString());
		show(e);
		JSONObject attrs = new JSONObject();
		attrs.put("path", new JSONString(e.getEdgePathString()));
		e.getEdgePath().animate(attrs,600,snakeCallbackBuilder(e));

	}


	protected void snakeIn(GraphEdge e) {

		if (e.isSnakedIn()) return;
		e.setSnakedIn(true);
		hideArrow(e);
		e.getEdgePath().attr("path", e.getEdgePathString());
		JSONObject attrs = new JSONObject();
		attrs.put("path", new JSONString(e.getEdgePathSmallString()));
		e.getEdgePath().animate(attrs,600,snakeInCallbackBuilder(e));
	}


	protected void makeConnection(GraphEdge e, GraphNode from, GraphNode to, boolean quiet) {

		Coordinate res = calcOrientation(e,from,to);

		int toPositionOld = e.getToPosition();
		int fromPositionOld = e.getFromPosition();

		e.setToPosition( (int) res.getY() -4);
		e.setFromPosition( (int) res.getX()); 

		if (e.getOffset()==-1 || toPositionOld != e.getToPosition()) {
			e.setOffset(c.getGraphNodeModifier().getOffset(to,e.getToPosition(),e,(toPositionOld != e.getToPosition() && e.getOffset()!=-1), toPositionOld,false,quiet));		
		}

		if (e.getOffsetFrom()==-1 || fromPositionOld != e.getFromPosition()) {
			e.setOffsetFrom(c.getGraphNodeModifier().getOffset(from,e.getFromPosition(),e,(fromPositionOld != e.getFromPosition() && e.getOffsetFrom()!=-1), fromPositionOld,false,quiet));		
		}

		generatePath(e,from,to, e.getToPosition(),e.getFromPosition());
		drawEdge(e,quiet);

	}


	protected void generatePath(GraphEdge e,GraphNode from, GraphNode to, int toPosition, int fromPosition) {

		int fromHeight = from.getHeight();
		double fromX = from.getX();
		double fromY = from.getY();

		int toWidth = to.getWidth();
		int toHeight = to.getHeight();
		double toX = to.getX();
		double toY = to.getY();

		Coordinate[] p = e.getP();

		p[0] = new Coordinate(fromX  + e.getOffsetFrom(),fromY - 1);
		p[1] = new Coordinate(fromX  + e.getOffsetFrom(),fromY + fromHeight + 1);
		p[2] = new Coordinate(fromX  - 1,fromY + e.getOffsetFrom());
		p[3] = new Coordinate(fromX  + from.getWidth() + 1,fromY + e.getOffsetFrom());
		
		p[4] = new Coordinate(toX  + e.getOffset(),toY - 1-(e.getArrowSize()));
		p[5] = new Coordinate(toX  + e.getOffset(),toY + toHeight + 1 + (e.getArrowSize()));
		p[6] = new Coordinate(toX  - 1 - (e.getArrowSize()),toY+ e.getOffset());
		p[7] = new Coordinate(toX  + toWidth + 1 + (e.getArrowSize()),toY + e.getOffset());

		e.setP(p);

		e.setX1(p[(int) fromPosition].getX());
		e.setY1(p[(int) fromPosition].getY());
		e.setX4(p[(int) toPosition+4].getX());
		e.setY4(p[(int) toPosition+4].getY());

		double dx = Math.max(Math.abs(e.getX1() - e.getX4()) / 2, 10);
		double dy = Math.max(Math.abs(e.getY1() - e.getY4()) / 2, 10);


		double[] x2l = {e.getX1(), e.getX1(), e.getX1() - dx, e.getX1() + dx};
		double[] y2l = {e.getY1() - dy, e.getY1() + dy, e.getY1(), e.getY1()};
		double[] x3l = {0, 0, 0, 0, e.getX4(), e.getX4(), e.getX4() - dx, e.getX4() + dx};
		double[] y3l = {0, 0, 0, 0, e.getY1() + dy, e.getY1() - dy, e.getY4(), e.getY4()};

		e.setX2(x2l[(int) fromPosition]);
		e.setY2(y2l[(int) fromPosition]);
		e.setX3(x3l[(int) toPosition+4]);
		e.setY3(y3l[(int) toPosition+4]);
	}



	protected void drawEdge(GraphEdge e,boolean quiet) {

		if (e.getEdgePath() == null) {
			e.setEdgePath(c.new Path());
			e.getEdgePath() .hide();
		}

		e.setPathStringSmall("M" + e.getX1() + "," + e.getY1()) ;

		e.setEdgePathString("M" + e.getX1() + "," + e.getY1() + "C" + e.getX2() + "," + e.getY2() + "," + e.getX3() + "," + e.getY3() + "," + e.getX4() + "," + e.getY4());

		if (!quiet) {
			e.getEdgePath().attr("path",e.getEdgePathString());
		}

		double[] xA = {e.getX4(), e.getX4(), e.getX4()+e.getArrowSize(),e.getX4()-e.getArrowSize()};
		double[] yA = {e.getY4()+e.getArrowSize() ,e.getY4()-e.getArrowSize(), e.getY4(), e.getY4()};

		double angle = Math.atan2(e.getX3()-xA[e.getToPosition()],yA[e.getToPosition()]-e.getY3());
		angle = ((angle / (2 * Math.PI)) * 360);

		String arrowPath = "M" + xA[e.getToPosition()] + "," + yA[e.getToPosition()] + "," + (xA[e.getToPosition()] - e.getArrowSize()) + "," + (yA[e.getToPosition()] - e.getArrowSize()) + " L" + (xA[e.getToPosition()] - e.getArrowSize()) + "," + (yA[e.getToPosition()] + e.getArrowSize()) + " L" + xA[e.getToPosition()] + "," + yA[e.getToPosition()];

		if (e.getArrowPath() ==null) {
			e.setArrowPath(c.new Path());
			e.getArrowPath().hide();
		}
		
		e.getArrowPath().attr("path",arrowPath);

		switch(e.getToPosition()) {
		
			case 0: angle = 90;break; //oben
			case 1: angle = 270;break;
			case 2: angle = 0;break;
			case 3: angle = 180;break; 
		
		}

		e.getArrowPath().rotate(angle,xA[e.getToPosition()],yA[e.getToPosition()]);

		JSONObject newAttrs = new JSONObject();
		newAttrs.put("stroke-opacity", new JSONNumber(1));

	}


	private Coordinate calcOrientation(GraphEdge e, GraphNode from, GraphNode to) {

		Coordinate[] p = new Coordinate[8];

		int fromWidth = from.getWidth();
		int fromHeight = from.getHeight();
		double fromX = from.getX();
		double fromY = from.getY();

		int toWidth = to.getWidth();
		int toHeight = to.getHeight();
		double toX = to.getX();
		double toY = to.getY();

		//generate the path the connection will be drawn along

		p[0] = new Coordinate(fromX + fromWidth / 2,fromY - 1);
		p[1] = new Coordinate(fromX + fromWidth / 2,fromY + fromHeight + 1);
		p[2] = new Coordinate(fromX - 1,fromY + fromHeight/ 2);
		p[3] = new Coordinate(fromX + fromWidth + 1,fromY + fromHeight / 2);
		p[4] = new Coordinate(toX + toWidth  / 2,toY - 1-e.getArrowSize());
		p[5] = new Coordinate(toX + toWidth  / 2,toY + toHeight + 1 + e.getArrowSize());
		p[6] = new Coordinate(toX - 1 - e.getArrowSize(),toY + toHeight / 2);
		p[7] = new Coordinate(toX + toWidth  + 1 + e.getArrowSize(),toY + toHeight / 2);


		
		e.setP(p);

		HashMap<Integer,Coordinate> d = new HashMap<Integer,Coordinate>();
		ArrayList<Integer> dis = new ArrayList<Integer>();

		for (int i = 0; i < 4; i++) {
			for (int j = 4; j < 8; j++) {
				double dx = Math.abs(p[i].getX() - p[j].getX() );
				double dy = Math.abs(p[i].getY()-e.getArrowSize() - p[j].getY());
				
				//TODO: arrow angle on overlapping nodes
				
				if ((i == j - 4) || 
						(((i != 3 && j != 6) || p[i].getX()-e.getArrowSize()  < p[j].getX()+e.getArrowSize()) 
								
								&& ((i != 2 && j != 7) || p[i].getX()+e.getArrowSize() > p[j].getX()-e.getArrowSize()) 
								&& ((i != 0 && j != 5) || p[i].getY()-e.getArrowSize()  > p[j].getY() +e.getArrowSize()) 
								&& ((i != 1 && j != 4) || p[i].getY()+e.getArrowSize() < p[j].getY()-e.getArrowSize()))) {
					dis.add((int) (dx + dy));

					d.put(dis.get(dis.size() - 1), new Coordinate(i, j));
				}
			}
		}

		Coordinate res;

		if (dis.size() == 0) {
			res = new Coordinate(0,4);
		} else {
			res = d.get(getSmallestVal(dis));
		}

		return res;
	}


	protected void updateOffset(GraphEdge e,int count,boolean quiet) {

		e.setOffset(count);

		generatePath(e,e.getFrom(),e.getTo(), e.getToPosition(), e.getFromPosition());
		drawEdge(e,quiet);

	}

	protected void updateOffsetFrom(GraphEdge e,int count, boolean quiet) {

		e.setOffsetFrom(count);

		generatePath(e,e.getFrom(),e.getTo(), e.getToPosition(), e.getFromPosition());
		drawEdge(e,quiet);

	}


	protected void update(GraphEdge e, boolean quiet) {
		makeConnection(e, e.getFrom(),e.getTo(), quiet);

	}



	private int getSmallestVal(ArrayList<Integer> list) {

		Iterator<Integer> i = list.iterator();

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


	protected void deleteFromFrom(GraphEdge e) {
		c.getGraphNodeModifier().removeEdge(e.getFrom(),e);
	}

	protected void deleteFromTo(GraphEdge e) {
		c.getGraphNodeModifier().removeEdge(e.getTo(),e);
	}


	protected void toFront(GraphEdge e) {
		e.getEdgePath().toFront();
		e.getArrowPath().toFront();
	}


	protected void toBack(GraphEdge e) {
		e.getEdgePath().toBack();
		e.getArrowPath().toBack();
	}

}

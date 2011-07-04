package com.algebraweb.editor.client.graphcanvas;


import com.google.gwt.core.client.GWT;
import com.hydro4ge.raphaelgwt.client.Raphael.Path;


public class GraphEdge {


	private GraphCanvas c;
	private Path edgePath;
	private Path arrowPath;
	private int toPosition;
	private int fromPosition;
	private GraphNode fromNode;
	private GraphNode toNode;

	private String pathSmall;

	private double x1;
	private double x2;
	private double x3;
	private double x4;
	private double y1;
	private double y2;
	private double y3;
	private double y4;

	private int arrowSize = 7;

	private Coordinate[] p;

	int offset=-1;
	int offsetFrom=-1;

	private String paths;


	public GraphEdge(GraphCanvas c, GraphNode from, GraphNode to,boolean quiet,boolean animated) {

		this.c =c;

		this.fromNode=from;
		this.toNode=to;

		from.addEdgeFrom(this);
		to.addEdgeTo(this);
		c.getGraphEdgeModifier().makeConnection(this,from,to,quiet,animated);

	}

	public int getOffsetFrom() {
		return offsetFrom;
	}

	public void setOffsetFrom(int offsetFrom) {
		this.offsetFrom = offsetFrom;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}


	public int getArrowSize() {
		return arrowSize;
	}


	public void setArrowSize(int size) {
		this.arrowSize = size;
	}


	public int getToPosition() {
		return toPosition;
	}

	public int getFromPosition() {
		return fromPosition;
	}

	public void setToPosition(int pos) {
		toPosition = pos;
	}

	public void setFromPosition(int pos) {
		fromPosition= pos;
	}


	public int getOrientation() {
		return toPosition;
	}

	public int getOrientationFrom() {

		return fromPosition;
	}

	public int getOffset() {

		return offset;

	}

	public Path getEdgePath() {
		return edgePath;
	}

	public Path getArrowPath() {
		return arrowPath;
	}


	public boolean isSnakedIn() {
		return snakedIn;
	}

	public void setSnakedIn(boolean s) {
		snakedIn = s;
	}


	public Coordinate[] getP() {
		return p;
	}


	public void setP(Coordinate[] p) {
		this.p = p;
	}


	public double getX1() {
		return x1;
	}


	public void setX1(double x1) {
		this.x1 = x1;
	}


	public double getX2() {
		return x2;
	}


	public void setX2(double x2) {
		this.x2 = x2;
	}


	public double getX3() {
		return x3;
	}


	public void setX3(double x3) {
		this.x3 = x3;
	}


	public double getX4() {
		return x4;
	}


	public void setX4(double x4) {
		this.x4 = x4;
	}


	public double getY1() {
		return y1;
	}


	public void setY1(double y1) {
		this.y1 = y1;
	}


	public double getY2() {
		return y2;
	}


	public void setY2(double y2) {
		this.y2 = y2;
	}


	public double getY3() {
		return y3;
	}


	public void setY3(double y3) {
		this.y3 = y3;
	}


	public double getY4() {
		return y4;
	}


	public void setY4(double y4) {
		this.y4 = y4;
	}


	public GraphNode getFrom() {
		return fromNode;
	}


	public GraphCanvas getC() {
		return c;
	}


	public void setC(GraphCanvas c) {
		this.c = c;
	}


	public void setEdgePath(Path ep) {
		this.edgePath = ep;
	}


	public void setArrowPath(Path ap) {
		this.arrowPath = ap;
	}


	public void setPathStringSmall(String pathSmall) {
		this.pathSmall = pathSmall;
	}


	public void setFrom(GraphNode from) {
		this.fromNode = from;
	}


	public GraphNode getTo() {
		return toNode;
	}


	public void setTo(GraphNode to) {
		this.toNode = to;
	}


	private boolean snakedIn = true;


	public String getEdgePathString() {
		return paths;
	}

	public void setEdgePathString(String paths) {
		this.paths = paths;
	}

	public String getEdgePathSmallString() {
		return pathSmall;
	}


	public void setAniStep(int x, int y) {

	}





}

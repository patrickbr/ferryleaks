package com.algebraweb.editor.client.graphcanvas;

public interface Fillable {

	public void clear();

	public GraphNode addNode(int nid, int color, int width, int height, String text,			int fixedChildCount);
	public void createEdge(int nid, int to, int fixedParentPos, boolean b);
	public GraphNode addNode(int nid, int color, int x, int y, int width, int height, String text, int fixedChildCount);
	


}

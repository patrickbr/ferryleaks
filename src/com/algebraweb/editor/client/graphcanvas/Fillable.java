package com.algebraweb.editor.client.graphcanvas;

/**
 * Fillable object can be filled with nodes.
 * 
 * @author Patrick Brosi
 *
 */
public interface Fillable {

	/**
	 * Add a new node.
	 * @param nid the node's id
	 * @param color the node's color as a hex integer
	 * @param x the node's x position
	 * @param y the node's y position
	 * @param width the node's width
	 * @param height the node's height
	 * @param text the node's caption
	 * @param fixedChildCount the number of childs the node can be a parent of
	 * @return a loaded GraphNode
	 */
	public GraphNode addNode(int nid, int color, int x, int y, int width,
			int height, String text, int fixedChildCount);

	public GraphNode addNode(int nid, int color, int width, int height,
			String text, int fixedChildCount);

	/**
	 * Clear all nodes and edges.
	 */
	public void clear();

	/**
	 * Add a new edge.
	 * @param nid the from-node's id
	 * @param to the to-node's id
	 * @param fixedParentPos the child position
	 * @param b
	 */
	public void createEdge(int nid, int to, int fixedParentPos, boolean b);

}

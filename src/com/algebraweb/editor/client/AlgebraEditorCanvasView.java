package com.algebraweb.editor.client;

import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Fillable;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphSorter;
import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.user.client.ui.Widget;

/**
 * Declares methods a view for the editor has to implement.
 * 
 * @author Patrick Brosi
 * 
 */
public interface AlgebraEditorCanvasView extends Fillable {

	/**
	 * Hangs a SQL listener to a node
	 * 
	 * @param nid
	 *            the nid of the node
	 * @param c
	 *            the evaluation context to use
	 */
	public void addSQLListener(int nid, EvaluationContext c);

	/**
	 * Removes all error marks
	 */
	public void clearErroneous();

	/**
	 * Removes a node
	 * 
	 * @param graphNodeById
	 *            the node's id
	 */
	public void deleteNode(GraphNode graphNodeById);

	/**
	 * Enter the edge adding mode where users should be able to draw edges
	 */
	public void enterEdgeAddingMode();

	/**
	 * Enter the node adding mode where users should be able to add new nodes.
	 * 
	 * @param selectedScheme
	 *            the scheme of the node to be added
	 */
	public void enterNodeAddingMode(String selectedScheme);

	/**
	 * Returns a loaded GraphNode object specified by its id
	 * 
	 * @param nid
	 *            the node's id
	 * @return the GraphNode object
	 */
	public GraphNode getGraphNodeById(int nid);

	/**
	 * Returne the id of this view
	 * 
	 * @return the id
	 */
	public int getId();

	/**
	 * Returns the current zoom scale
	 * 
	 * @return the scale
	 */
	public double getScale();

	/**
	 * Returns a map of selected edges. A coordinate object is used for tuple
	 * representation (from,to)
	 * 
	 * @return the map of edges
	 */
	public Map<Tuple, Integer> getSelectedEdgesWithPos();

	/**
	 * Returns a map of selected nodes. The keys are the node ids.
	 * 
	 * @return the map of selected nodes
	 */
	public Map<Integer, GraphNode> getSelectedNodes();

	/**
	 * Returns the underlying widget element
	 * 
	 * @return the widget
	 */
	public Widget getWidget();

	/**
	 * Checks whether an edge between two nodes (specified by their id) exists
	 * at the given child position of the from-node
	 * 
	 * @param id
	 *            the id of the from-node
	 * @param id2
	 *            the id of the to-node
	 * @param fixedParentPos
	 *            the child position
	 * @return true of an edge exists
	 */
	public boolean hasEdge(int id, int id2, int fixedParentPos);

	/**
	 * Removes an edge (from,to) from the given position at the from node
	 * 
	 * @param from
	 *            the from node's id
	 * @param to
	 *            the to node's id
	 * @param pos
	 *            the position at the from node
	 */
	public void removeEdge(int from, int to, int pos);

	/**
	 * Select a node with all its sub nodes
	 * 
	 * @param n
	 *            the node's id
	 */
	public void selectNodeWithSubs(GraphNode n);

	/**
	 * If true, the canvas should be blurred
	 * 
	 * @param blur
	 */
	public void setBlurred(boolean blur);

	/**
	 * Mark a node as erroneous
	 * 
	 * @param nodeId
	 *            the id of the node to mark
	 */
	public void setErroneous(int nodeId);

	/**
	 * Set the view to not active
	 * 
	 * @param b
	 */
	public void setNotActive(boolean b);

	/**
	 * Sets the selected node
	 * 
	 * @param n
	 *            the graph node to select
	 */
	public void setSelectedNode(GraphNode n);

	/**
	 * shows all previously hidden edges
	 */
	public void showEdges();

	/**
	 * Sort the canvas with a given remoteSorter
	 * 
	 * @param remoteSorter
	 *            the sorter to use
	 */
	public void sort(GraphSorter remoteSorter);

	/**
	 * Stop all popup counter, close all popups
	 */
	public void unbugMe();

	/**
	 * Updates SQl listeners
	 */
	public void updateSQLListener();

	/**
	 * Zoom in
	 */
	public void zoomIn();

	/**
	 * Zoom out
	 */
	public void zoomOut();

}

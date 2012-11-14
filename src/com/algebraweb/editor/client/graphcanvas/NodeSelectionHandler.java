package com.algebraweb.editor.client.graphcanvas;

/**
 * A handler for node selection. Can be given to the GraphCanvas. Its isSelected
 * method will be called on node selections.
 * 
 * @author Patrick Brosi
 * 
 */
public abstract class NodeSelectionHandler {

	/**
	 * 
	 * @param node
	 *            the selected node.
	 * @return should return true. If the method returns false, the node will
	 *         not be selected.
	 */
	public abstract boolean isSelected(GraphNode node);
}

package com.algebraweb.editor.client.graphcanvas;

/**
 * An interface for items of the node context menu.
 *
 * @author Patrick Brosi
 *
 */
public interface NodeContextMenuItem {
	/**
	 * Returns the title of this menu item.
	 *
	 * @return the title string
	 */
	public String getItemTitle();

	/**
	 * Will be called on a click on this item
	 *
	 * @param nid
	 *            the id of the node this context menu was opened from
	 */
	public void onClick(int nid);
}
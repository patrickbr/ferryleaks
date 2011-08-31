package com.algebraweb.editor.client.graphcanvas;

/**
 * Methods that have to be implemented by context menu items.
 * 
 * @author Üatrick Brosi
 * 
 */
public interface ContextMenuItem {

	/**
	 * Returns the item's title
	 * 
	 * @return the title as a string
	 */
	public String getItemTitle();

	/**
	 * The onClick method.
	 */
	public void onClick();

}

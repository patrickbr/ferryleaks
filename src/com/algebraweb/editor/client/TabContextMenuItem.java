package com.algebraweb.editor.client;

/**
 * Declaring methods for tab context menu items
 * 
 * @author patrick
 * 
 */
public interface TabContextMenuItem {

	/**
	 * Returns the title if the item
	 * 
	 * @return the title as a string
	 */
	public String getItemTitle();

	/**
	 * The onClick methods.
	 * 
	 * @param pid
	 *            the plan id of the clicked tab.
	 */
	public void onClick(int pid);

}

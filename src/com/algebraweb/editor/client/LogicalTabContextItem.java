package com.algebraweb.editor.client;

/**
 * An item within the tab context menu
 * 
 * @author Patrick Brosi
 * 
 */
public abstract class LogicalTabContextItem implements TabContextMenuItem {
	private String title;

	public LogicalTabContextItem(String title) {
		this.title = title;
	}

	@Override
	public String getItemTitle() {
		return title;
	}

	@Override
	public abstract void onClick(int pid);
}
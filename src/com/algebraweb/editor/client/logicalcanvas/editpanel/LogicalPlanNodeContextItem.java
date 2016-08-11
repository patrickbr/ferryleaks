package com.algebraweb.editor.client.logicalcanvas.editpanel;

import com.algebraweb.editor.client.graphcanvas.ContextMenuItem;

public abstract class LogicalPlanNodeContextItem implements ContextMenuItem {
	private String title;

	public LogicalPlanNodeContextItem(String title) {

		this.title = title;

	}

	@Override
	public String getItemTitle() {
		return title;
	}

	@Override
	public abstract void onClick();

}

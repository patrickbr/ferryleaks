package com.algebraweb.editor.client;

public abstract class LogicalTabContextItem implements TabContextMenuItem {

	private String title;


	public LogicalTabContextItem(String title) {

		this.title=title;
	
	}


	@Override
	public String getItemTitle() {
		return title;
	}

	@Override
	public abstract void onClick(int pid);
}

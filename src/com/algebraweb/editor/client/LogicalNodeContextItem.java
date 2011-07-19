package com.algebraweb.editor.client;

import com.algebraweb.editor.client.graphcanvas.NodeContextMenuItem;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;

public abstract class LogicalNodeContextItem implements NodeContextMenuItem {


	private String title;


	public LogicalNodeContextItem(String title) {

		this.title=title;
	
	}


	@Override
	public String getItemTitle() {
		return title;
	}

	@Override
	public abstract void onClick(int nid);





}

package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.graphcanvas.FullScreenDragPanel;

public class LogicalDragPanel extends FullScreenDragPanel {
	
	private LogicalCanvas c;
	
	public LogicalDragPanel(LogicalCanvas c) {
		super();
		super.add(c);
		this.c=c;
	}
	
	public LogicalCanvas getLogicalCanvas() {
		return c;
	}

}

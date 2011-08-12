package com.algebraweb.editor.client;

import com.algebraweb.editor.client.graphcanvas.FullScreenDragPanel;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;

public class EditorDragPanel extends FullScreenDragPanel {
	
	private AlgebraEditorCanvasView c;
	
	public EditorDragPanel(AlgebraEditorCanvasView c) {
		super();
		super.add(c.getWidget());
		this.c=c;
	}
	
	public AlgebraEditorCanvasView getLogicalCanvas() {
		return c;
	}

}

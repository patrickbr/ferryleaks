package com.algebraweb.editor.client.dialogs;

import com.algebraweb.editor.client.AlgebraEditorCanvasView;
import com.algebraweb.editor.client.graphcanvas.FullScreenDragPanel;

/**
 * The editors drag panel
 * 
 * @author Patrick Brosi
 * 
 */
public class EditorDragPanel extends FullScreenDragPanel {
	private AlgebraEditorCanvasView c;

	public EditorDragPanel(AlgebraEditorCanvasView c) {
		super();
		super.add(c.getWidget());
		this.c = c;
	}

	public AlgebraEditorCanvasView getLogicalCanvas() {
		return c;
	}
}
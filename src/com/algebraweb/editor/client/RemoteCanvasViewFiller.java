package com.algebraweb.editor.client;

import com.algebraweb.editor.client.graphcanvas.remotefiller.GraphCanvasRemoteFillingMachine;

/**
 * A CanvasFiller using remote content
 * 
 * @author Patrick Brosi
 * 
 */
public class RemoteCanvasViewFiller extends GraphCanvasRemoteFillingMachine {
	public RemoteCanvasViewFiller(AlgebraEditorCanvasView v) {
		super(v);
	}
}

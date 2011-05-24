package com.algebraweb.editor.client;

import com.algebraweb.editor.client.graphcanvas.DragPanel;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * A Web Editor for the Table Algebra
 * @author Patrick Brosi
 *
 */

public class AlgebraEditor implements EntryPoint {


	/**
	 * Everything begins here...
	 */
	public void onModuleLoad() {
				
		GraphCanvas graphCanvas = new GraphCanvas(Window.getClientWidth()-30,Window.getClientHeight()-30);
		
		DragPanel d = new DragPanel();
		
		d.add(graphCanvas);
		
		RootPanel.get("editor").add(d);
		RootPanel.get("editor").add(new ControllPanel(300,300,graphCanvas));
		
		d.center(graphCanvas.getWidth(), graphCanvas.getHeight());
			
		graphCanvas.lock();
		graphCanvas.unLock();
	}
}

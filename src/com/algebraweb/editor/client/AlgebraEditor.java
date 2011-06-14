package com.algebraweb.editor.client;

import com.algebraweb.editor.client.graphcanvas.DragPanel;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.LogicalNodePopup;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
		
		RemoteManipulationServiceAsync rmsa = (RemoteManipulationServiceAsync) GWT.create(RemoteManipulationService.class);

		LogicalCanvas lCanvas = new LogicalCanvas(Window.getClientWidth()-30,Window.getClientHeight()-30);
		
		GraphNodeModifier gnm = new GraphNodeModifier(lCanvas);
		GraphEdgeModifier gem = new GraphEdgeModifier(lCanvas);
		
		lCanvas.setGraphEdgeModifier(gem);
		lCanvas.setGraphNodeModifier(gnm);
		lCanvas.setPopup(new LogicalNodePopup(lCanvas,rmsa));
		
		DragPanel d = new DragPanel();
		
		d.add(lCanvas);
		
		
		
		RootPanel.get("editor").add(d);
		RootPanel.get("editor").add(new ControllPanel(300,300,lCanvas,rmsa));
		
		d.center(lCanvas.getWidth(), lCanvas.getHeight());
			
		lCanvas.lock();
		lCanvas.unLock();
	}
}

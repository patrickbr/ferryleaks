package com.algebraweb.editor.client;

import com.algebraweb.editor.client.graphcanvas.FullScreenDragPanel;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.LogicalNodePopup;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * the bugFerry
 * A Web-Based Debugging Editor for the Table Algebra
 * 
 * @author Patrick Brosi
 *
 */

public class AlgebraEditor implements EntryPoint {


	/**
	 * Everything begins here...
	 */
	public void onModuleLoad() {
		
	
		
		RemoteManipulationServiceAsync rmsa = (RemoteManipulationServiceAsync) GWT.create(RemoteManipulationService.class);

		PlanModelManipulator m = new PlanModelManipulator(rmsa);
		
		
		LogicalCanvas lCanvas = new LogicalCanvas(m,Window.getClientWidth()-30,Window.getClientHeight()-30);
		
		GraphNodeModifier gnm = new GraphNodeModifier(lCanvas);
		GraphEdgeModifier gem = new GraphEdgeModifier(lCanvas);
		
		lCanvas.setGraphEdgeModifier(gem);
		lCanvas.setGraphNodeModifier(gnm);
		lCanvas.setPopup(new LogicalNodePopup(lCanvas,rmsa));
		lCanvas.setPadding(60, 45);


		
		FullScreenDragPanel d = new FullScreenDragPanel();
	
	
		
		d.add(lCanvas);
		
		
		
		RootPanel.get("editor").add(d);
		RootPanel.get("editor").add(new ControllPanel(m,300,300,lCanvas,rmsa));
		
		d.center(lCanvas.getWidth(), lCanvas.getHeight());
			

	}
}

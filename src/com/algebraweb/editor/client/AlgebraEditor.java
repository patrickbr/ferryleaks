package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

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


	//TODO: this should be in an extra graph MultiEditor or sth like this

	private ArrayList<LogicalCanvas> canvi = new ArrayList<LogicalCanvas>();
	private ArrayList<FullScreenDragPanel> panels = new ArrayList<FullScreenDragPanel>();
	private LogicalCanvas activeCanvas=null;
	private PlanSwitcher s;
	private RemoteManipulationServiceAsync rmsa;
	private PlanModelManipulator m;
	
	/**
	 * Everything begins here...
	 */
	public void onModuleLoad() {



		rmsa = (RemoteManipulationServiceAsync) GWT.create(RemoteManipulationService.class);

		m = new PlanModelManipulator(rmsa);

		/*
		LogicalCanvas lCanvas = new LogicalCanvas(0,m,Window.getClientWidth()-30,Window.getClientHeight()-30);

		lCanvas.setPopup(new LogicalNodePopup(lCanvas,rmsa));
		lCanvas.setPadding(60, 45);

*/
		s= new PlanSwitcher(this);
		RootPanel.get("editor").add(new ControllPanel(this,m,300,300,rmsa));
		RootPanel.get("editor").add(s);


		//addCanvas(0);
		//changeCanvas(0);


		//d.center(lCanvas.getWidth(), lCanvas.getHeight());


	}
	
	


	public LogicalCanvas addCanvas(int id) {

		LogicalCanvas c = new LogicalCanvas(id,m,Window.getClientWidth()-30,Window.getClientHeight()-30);
		
		
		c.setGraphNodeModifier(new GraphNodeModifier(c));
		c.setGraphEdgeModifier(new GraphEdgeModifier(c));
		
		c.setPopup(new LogicalNodePopup(c,rmsa));
		c.setPadding(60, 45);
		
		FullScreenDragPanel d = new FullScreenDragPanel();
		d.add(c);
		d.hide();
		RootPanel.get("editor").add(d);
		s.addPlan(c.getId());
		canvi.add(c);
		panels.add(d);
		
		return c;


	}

	public void clearCanvases() {

		Iterator<FullScreenDragPanel> it = panels.iterator();

		while (it.hasNext()) {
			FullScreenDragPanel cur = it.next();
			RootPanel.get("editor").remove(cur);
			removeLogicalCanvas((LogicalCanvas) cur.getWidget(0));
		}

	}
	
	public void removeLogicalCanvas(LogicalCanvas c) {
		
		RootPanel.get("editor").remove(c);
		s.removePlan(c.getId());
		
	}
	
	public LogicalCanvas getActiveCanvas() {
		return activeCanvas;
	}

	public void changeCanvas(int id) {


		Iterator<FullScreenDragPanel> it = panels.iterator();


		while (it.hasNext()) {

			//TODO: make own canvasdragpanel!!

			FullScreenDragPanel cur = it.next();

			if (((LogicalCanvas)cur.getWidget(0)).getId() != id) {
				
				
				cur.hide();

			}else{

				activeCanvas = ((LogicalCanvas)cur.getWidget(0));
				cur.show();
			}


		}


	}
}

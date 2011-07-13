package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * the bugFerry
 * A Web-Based Debugging Editor for the Table Algebra
 * 
 * @author Patrick Brosi
 *
 */

public class AlgebraEditor implements EntryPoint {

	private static String VERSION = "Alpha 5.61";

	private static String TITLE = "the bugFerry";
	private static String AUTHOR = "Patrick Brosi";
	private static String YEAR = "2011";
	private static String FACILITY = "Universität Tübingen";



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
		m.setEditor(this);


		s= new PlanSwitcher(this);
		RootPanel.get("editor").add(new ControllPanel(this,m,300,300,rmsa));
		RootPanel.get("editor").add(s);

		Window.setTitle(TITLE + " - " + VERSION);
		RootPanel.get("impressum").getElement().setInnerHTML(TITLE + " " + VERSION + " - " + FACILITY + " - " + YEAR + " " + AUTHOR); 
		RootPanel.get("bugferrylogo").getElement().setInnerHTML(TITLE); 


		GWT.log("loading empty plan...");
		rmsa.createNewPlan(createCb);


	}


	public LogicalCanvas getCanvas(int pid) {

		Iterator<LogicalCanvas> it = canvi.iterator();

		while (it.hasNext()) {

			LogicalCanvas cur = it.next();

			if (cur.getId() == pid) return cur;

		}

		return null;


	}

	public LogicalCanvas addCanvas(int id) {

		
		
		LogicalCanvas c = new LogicalCanvas(id,m,Window.getClientWidth()-30,Window.getClientHeight()-30,s.addPlan(id));


		c.setGraphNodeModifier(new GraphNodeModifier(c));
		c.setGraphEdgeModifier(new GraphEdgeModifier(c));

		c.setPopup(new LogicalNodePopup(c,rmsa));
		c.setPadding(60, 45);

		FullScreenDragPanel d = new FullScreenDragPanel();
		d.add(c);
		d.hide();
		RootPanel.get("editor").add(d);
	
		canvi.add(c);
		panels.add(d);

		return c;


	}

	public void clearCanvases() {

		Iterator<FullScreenDragPanel> it = panels.iterator();

		while (it.hasNext()) {
			FullScreenDragPanel cur = it.next();
			removeLogicalCanvas((LogicalCanvas) cur.getWidget(0));
			cur.clear();
			RootPanel.get("editor").remove(cur);
			it.remove();

		}

	}

	public void removeLogicalCanvas(LogicalCanvas c) {

		canvi.remove(c);
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
				((LogicalCanvas)cur.getWidget(0)).setNotActive(true);
				cur.hide();

			}else{

				activeCanvas = ((LogicalCanvas)cur.getWidget(0));
				activeCanvas.setNotActive(false);
				s.setActive(id);
				cur.show();

			}


		}


	}

	private  AsyncCallback<Integer> createCb = new  AsyncCallback<Integer>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(Integer result) {


			addCanvas(result);
			changeCanvas(result);


		}

	};


	public static void setSubTitle(String s) {


		Window.setTitle(s + " - " + TITLE + " - " + VERSION);

	}
}

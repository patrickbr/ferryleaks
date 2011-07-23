package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.graphcanvas.ContextMenu;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.FullScreenDragPanel;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.graphcanvas.NodeContextMenu;
import com.algebraweb.editor.client.graphcanvas.remotefiller.GraphCanvasRemoteFillingMachine;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;
import com.algebraweb.editor.client.logicalcanvas.AddSQListenerDIalog;
import com.algebraweb.editor.client.logicalcanvas.CreateSQLDialog;
import com.algebraweb.editor.client.logicalcanvas.EvaluationDialog;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.LogicalNodePopup;
import com.algebraweb.editor.client.logicalcanvas.LogicalPlanNodeContextItem;
import com.algebraweb.editor.client.logicalcanvas.NodeEditDialog;
import com.algebraweb.editor.client.logicalcanvas.PlanManipulationException;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;


/**
 * the bugFerry
 * A Web-Based Debugging Editor for the Table Algebra
 * 
 * @author Patrick Brosi
 *
 */

public class AlgebraEditor implements EntryPoint {

	private static String VERSION = "Alpha 6.10";

	private static String TITLE = "the bugFerry";
	private static String AUTHOR = "Patrick Brosi";
	private static String YEAR = "2011 (July 23rd)";
	private static String FACILITY = "Universität Tübingen";
	private static TextArea log = new TextArea();
	private static String BROWSER_NAME = "";
	private static String BROWSER_VER = "";
	private static String BROWSER_OS = "";
	private static boolean LOGGING=false;
	private static RegistrationServiceAsync registor = GWT.create(RegistrationService.class);
	private static Timer keepAliveTimer;
	
	private static HashMap<Integer,Coordinate> scrollPositions = new HashMap<Integer,Coordinate>();

	//TODO: this should be in an extra graph MultiEditor or sth like this

	private ArrayList<LogicalCanvas> canvi = new ArrayList<LogicalCanvas>();
	private ArrayList<FullScreenDragPanel> panels = new ArrayList<FullScreenDragPanel>();
	private static LogicalCanvas activeCanvas=null;
	private PlanSwitcher s;
	private RemoteManipulationServiceAsync rmsa;
	private PlanModelManipulator m;
	private NodeContextMenu nodeContextMenu = new NodeContextMenu();
	private ContextMenu planContextMenu = new ContextMenu();

	/**
	 * Everything begins here...
	 */
	public void onModuleLoad() {


		if (Window.Location.getParameter("logger") != null) {
			RootPanel.get("debugger").add(AlgebraEditor.log);
			RootPanel.get("debugger").getElement().getStyle().setDisplay(Display.BLOCK);
			LOGGING=true;
			AlgebraEditor.log.setReadOnly(true);
			
		}


		BROWSER_NAME = Navigator.getAppCodeName();
		BROWSER_VER = Navigator.getAppVersion();
		BROWSER_OS = Navigator.getPlatform();


		if (Math.random()<0.1) TITLE = "FerryLeaks";

		AlgebraEditor.log(TITLE + " " + VERSION + " - " + FACILITY + " - " + YEAR + " " + AUTHOR);
		AlgebraEditor.log("   running in " + BROWSER_NAME + " " + BROWSER_VER + " (" + BROWSER_OS + ") (" + (Navigator.isCookieEnabled()?"Cookies enabled":"Cookies _NOT_ enabled. Session handling will not work properly!")+ ")");
		AlgebraEditor.log("   servlet on " + GWT.getModuleBaseURL() + " running in " + (GWT.isProdMode()?"production":"development") + " mode");

		AlgebraEditor.log("initializing remote manupulation service...");
		rmsa = (RemoteManipulationServiceAsync) GWT.create(RemoteManipulationService.class);

		m = new PlanModelManipulator(rmsa);
		m.setEditor(this);


		s= new PlanSwitcher(this);
		AlgebraEditor.log("initializing controll panel...");
		RootPanel.get("editor").add(new ControllPanel(this,m,300,300,rmsa));
		RootPanel.get("editor").add(s);

		Window.setTitle(TITLE + " - " + VERSION);
		RootPanel.get("impressum").getElement().setInnerHTML(TITLE + " " + VERSION + " - " + FACILITY + " - " + YEAR + " " + AUTHOR); 
		RootPanel.get("bugferrylogo").getElement().setInnerHTML(TITLE); 
		initContextMenu();
		initPlanContextMenu();

		AlgebraEditor.log("Sending registration...");

		registor.register(new AsyncCallback<Configuration>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(final Configuration result) {
				processConfiguration(result);
			}

			private void processConfiguration(final Configuration result) {
				keepAliveTimer = new Timer() {

					@Override
					public void run() {
						AlgebraEditor.log("Sending keep alive...");
						registor.keepAlive(new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Void result) {
							}});

					}

				};

				keepAliveTimer.scheduleRepeating(60000);
				
				

				if (result instanceof ConfigurationWithPlansInSession) {

					AlgebraEditor.log("Found existing session on server...");

					YesNoPanel ynp = new YesNoPanel("A previous session has been found on the server. Do you want to load it?","Information");
					ynp.registerYesClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							AlgebraEditor.log("Loading existing plans from previous session...");
							for (Integer id:((ConfigurationWithPlansInSession)result).getPlanIds()) {
								loadFinishedPlanFromServer(id);
							}
						}
					});
					ynp.registerNoClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							AlgebraEditor.log("loading empty plan...");
							rmsa.createNewPlan(createCb);

						}
					});
					ynp.center();
					ynp.show();

				}else{

					AlgebraEditor.log("loading empty plan...");
					rmsa.createNewPlan(createCb);

				}
			}
		});
	}
	
	
	public void createNewPlan() {
		
		rmsa.createNewPlan(createCb);
			
		
	}
	
	public void removePlan(int id) {
		
		
		rmsa.removePlan(id, removeCb);
		
		
	}


	public LogicalCanvas getCanvas(int pid) {

		Iterator<LogicalCanvas> it = canvi.iterator();

		while (it.hasNext()) {

			LogicalCanvas cur = it.next();

			if (cur.getId() == pid) return cur;

		}

		return null;


	}

	public int getFreeCanvasId() {
		int i=0;
		while (hasCanvasWithId(i)) i++;
		return i;
	}

	public boolean hasCanvasWithId(int i) {
		Iterator<LogicalCanvas> it = canvi.iterator();
		while (it.hasNext()) if (it.next().getId() == i) return true;
		return false;
	}

	public LogicalCanvas addCanvas(int id) {

		if (hasCanvasWithId(id)) return getCanvas(id);

		LogicalCanvas c = new LogicalCanvas(id,m,Window.getClientWidth()-30,Window.getClientHeight()-30,s.addPlan(id));

		scrollPositions.put(id, new Coordinate(0,0));

		c.setGraphNodeModifier(new GraphNodeModifier(c));
		c.setGraphEdgeModifier(new GraphEdgeModifier(c));

		c.setPopup(new LogicalNodePopup(c,rmsa));
		c.setContextMenu(nodeContextMenu);
		c.setCanvasMenu(planContextMenu);
		c.setPadding(60, 45);

		FullScreenDragPanel d = new FullScreenDragPanel();
		d.add(c);
		d.hide();
		RootPanel.get("editor").add(d);

		canvi.add(c);
		panels.add(d);

		if (canvi.size() == 1) changeCanvas(id);

		return c;


	}

	public void clearCanvases() {

		AlgebraEditor.log("Clearing canvases...");

		Iterator<FullScreenDragPanel> it = panels.iterator();

		canvi.clear();

		while (it.hasNext()) {
			FullScreenDragPanel cur = it.next();
			removeLogicalCanvas((LogicalCanvas) cur.getWidget(0));
			cur.clear();
			RootPanel.get("editor").remove(cur);
			it.remove();
		}

	}

	public void removeLogicalCanvas(LogicalCanvas c) {

		AlgebraEditor.log("Removing canvas #" + c.getId());
		canvi.remove(c);
		scrollPositions.remove(c.getId());
		s.removePlan(c.getId());

	}

	public static LogicalCanvas getActiveCanvas() {
		return activeCanvas;
	}

	public void changeCanvas(int id) {

		AlgebraEditor.log("Changing to canvas #" + id);
		Iterator<FullScreenDragPanel> it = panels.iterator();

		if (activeCanvas != null) scrollPositions.put(activeCanvas.getId(), new Coordinate(Window.getScrollLeft(),Window.getScrollTop()));
		
		
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
				Window.scrollTo((int)scrollPositions.get(id).getX(), (int)scrollPositions.get(id).getY());
				

			}


		}


	}

	public static void log(String s) {

		if (!LOGGING) return;
		Date today = new Date();
		log.setText(log.getText() + "\n" + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_MEDIUM).format(today) + " "  + s);
		log.setCursorPos(log.getText().length());


	}

	public void loadFinishedPlanFromServer(final Integer id) {


		final LogicalCanvas c = addCanvas(id);

		GraphCanvasRemoteFillingMachine f = new GraphCanvasRemoteFillingMachine(c);

		AlgebraEditor.log("Calling remote filler for plan #" + id);
		f.fill(new RemoteFiller("xml",Integer.toString(id)), new GraphManipulationCallback() {

			@Override
			public void onComplete() {

				AlgebraEditor.log("Remote filler for plan #" + id + " finished. Calling validator...");
				c.sort(new RemoteSorter("dot"));
				getPlanManipulator().validate(id);

			}
		});
	}

	private  AsyncCallback<Integer> createCb = new  GraphCanvasCommunicationCallback<Integer>("adding new empty canvas") {

		@Override
		public void onFailure(Throwable caught) {
			super.onFailure(caught);
		}

		@Override
		public void onSuccess(Integer result) {


			if (addCanvas(result) != activeCanvas) changeCanvas(result);

		}

	};
	
	private  AsyncCallback<Integer> removeCb = new  GraphCanvasCommunicationCallback<Integer>("removing canvas") {

		@Override
		public void onFailure(Throwable caught) {
			super.onFailure(caught);
		}

		@Override
		public void onSuccess(Integer result) {

			if (hasCanvasWithId(result)) removeLogicalCanvas(getCanvas(result));

		}

	};


	public static void setSubTitle(String s) {


		Window.setTitle(s + " - " + TITLE + " - " + VERSION);

	}


	/**
	 * @return the nodeContextMenu
	 */
	public NodeContextMenu getNodeContextMenu() {
		return nodeContextMenu;
	}

	/**
	 * @return the contextMenu
	 */
	public ContextMenu getContextMenu() {
		return planContextMenu;
	}



	public PlanModelManipulator getPlanManipulator() {

		return m;

	}

	public RemoteManipulationServiceAsync getManServ() {

		return rmsa;

	}

	private void initPlanContextMenu() {

		AlgebraEditor.log("initializing context menu...");

		final ContextMenu m = getContextMenu();


		m.addItem(new LogicalPlanNodeContextItem("Add node") {

			@Override
			public void onClick() {
				getPlanManipulator().getNodeTypes();
			}
		});

		m.addSeperator();

		m.addItem(new LogicalPlanNodeContextItem("Paste") {

			@Override
			public void onClick() {
				getPlanManipulator().paste(getActiveCanvas().getId(), (int)(m.getX()*getActiveCanvas().getScale()),  (int)(m.getY()*getActiveCanvas().getScale()));
			}
		});

	}


	private void initContextMenu() {

		AlgebraEditor.log("initializing context menu...");

		final NodeContextMenu m = getNodeContextMenu();


		m.addItem(new LogicalNodeContextItem("Delete") {

			@Override
			public void onClick(int nid) {

				Integer[] nids = new Integer[1];
				nids[0] = nid;
				getPlanManipulator().deleteNode(nids, getActiveCanvas().getId());

			}
		});

		m.addSeperator();

		m.addItem(new LogicalNodeContextItem("Select") {

			@Override
			public void onClick(int nid) {
				getActiveCanvas().setSelectedNode(getActiveCanvas().getGraphNodeById(nid));
			}
		});

		m.addItem(new LogicalNodeContextItem("Select subtree") {

			@Override
			public void onClick(int nid) {
				getActiveCanvas().selectNodeWithSubs(getActiveCanvas().getGraphNodeById(nid));
			}
		});

		m.addItem(new LogicalNodeContextItem("Copy") {

			@Override
			public void onClick(int nid) {
				getPlanManipulator().copy(getActiveCanvas().getId());
			}
		});

		m.addSeperator();

		m.addItem(new LogicalNodeContextItem("View XML source") {

			@Override
			public void onClick(int nid) {
				getManServ().getXMLFromPlanNode(getActiveCanvas().getId(), nid, xmlCb);
			}
		});

		m.addItem(new LogicalNodeContextItem("Get compiled SQL") {

			@Override
			public void onClick(int nid) {
				new CreateSQLDialog(getActiveCanvas().getId(),nid,getManServ());
			}
		});


		m.addItem(new LogicalNodeContextItem("Add SQL Listener") {

			@Override
			public void onClick(int nid) {
				new AddSQListenerDIalog(nid, rmsa, getActiveCanvas());
			}
		});



		m.addItem(new LogicalNodeContextItem("Evaluate") {

			@Override
			public void onClick(int nid) {
				new EvaluationDialog(getActiveCanvas().getId(),nid,rmsa);
			}
		});


		m.addSeperator();

		m.addItem(new LogicalNodeContextItem("Edit") {

			@Override
			public void onClick(int nid) {

				new NodeEditDialog(getPlanManipulator(),rmsa,nid,getActiveCanvas().getId());

			}
		});

	}

	private GraphCanvasCommunicationCallback<String> xmlCb = new GraphCanvasCommunicationCallback<String>("getting XML") {

		@Override
		public void onSuccess(String result) {


			Window.alert(result);


		}

	};


}

package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import com.algebraweb.editor.client.graphcanvas.ContextMenu;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.graphcanvas.NodeContextMenu;
import com.algebraweb.editor.client.graphcanvas.TabContextMenu;
import com.algebraweb.editor.client.graphcanvas.remotefiller.GraphCanvasRemoteFillingMachine;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;
import com.algebraweb.editor.client.logicalcanvas.AddSQListenerDIalog;
import com.algebraweb.editor.client.logicalcanvas.ConfigurePlanDialog;
import com.algebraweb.editor.client.logicalcanvas.CreateSQLDialog;
import com.algebraweb.editor.client.logicalcanvas.EvaluatePlanDialog;
import com.algebraweb.editor.client.logicalcanvas.EvaluationDialog;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.LogicalDragPanel;
import com.algebraweb.editor.client.logicalcanvas.LogicalNodePopup;
import com.algebraweb.editor.client.logicalcanvas.LogicalPlanNodeContextItem;
import com.algebraweb.editor.client.logicalcanvas.NodeEditDialog;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;


/**
 * <b>the bugFerry</b><br>
 * A Web-Based (Debugging) Editor for the Table Algebra
 * <br><i>
 * see readme for information on how to compile this application</i>
 * 
 * @author Patrick Brosi
 *
 */

public class AlgebraEditor implements EntryPoint {

	private static String VERSION = "Beta 1.04";

	private static String TITLE = "FerryLeaks";
	private static String AUTHOR = "Patrick Brosi";
	private static String YEAR = "2011 (August 8th)";
	private static String FACILITY = "Universität Tübingen";
	private static TextArea log = new TextArea();
	private static String BROWSER_NAME = "";
	private static String BROWSER_VER = "";
	private static String BROWSER_OS = "";
	private static boolean LOGGING=false;
	private static RegistrationServiceAsync registor = GWT.create(RegistrationService.class);
	private static Timer keepAliveTimer;
	
	private RemoteConfiguration config;

	/**
	 * genesis...
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

		if (Math.random()<0.2) TITLE = "the bugFerry";

		AlgebraEditor.log("Hi! This is " + TITLE + " " + VERSION + " - " + FACILITY + " - " + YEAR + " " + AUTHOR);
		AlgebraEditor.log("   running in " + BROWSER_NAME + " " + BROWSER_VER + " (" + BROWSER_OS + ") (" + (Navigator.isCookieEnabled()?"Cookies enabled":"Cookies _NOT_ enabled. Session handling will not work properly!")+ ")");
		AlgebraEditor.log("   servlet on " + GWT.getModuleBaseURL() + " running in " + (GWT.isProdMode()?"production":"development") + " mode");
		Window.setTitle(TITLE + " - " + VERSION);

		AlgebraEditor.log("initializing remote manupulation service...");
		rmsa = (RemoteManipulationServiceAsync) GWT.create(RemoteManipulationService.class);

		m = new PlanModelManipulator(rmsa);
		m.setEditor(this);
		s= new PlanSwitcher(this);

		AlgebraEditor.log("initializing controll panel...");
		RootPanel.get("editor").add(new ControlPanel(this,m,300,300,rmsa));
		RootPanel.get("editor").add(s);
		RootPanel.get("impressum").getElement().setInnerHTML(TITLE + " " + VERSION + " - " + FACILITY + " - " + YEAR + " " + AUTHOR); 
		RootPanel.get("bugferrylogo").getElement().setInnerHTML(TITLE); 

		initContextMenu();
		initPlanContextMenu();
		initTabContextMenu();

		AlgebraEditor.log("Sending registration...");

		registor.register(new GraphCanvasCommunicationCallback<RemoteConfiguration>("registering session") {

			@Override
			public void onSuccess(final RemoteConfiguration result) {
				processConfiguration(result);
			}

		});
	}
	
	/**
	 * Returns the active canvas.
	 * @return the active canvas
	 */
	public static LogicalCanvas getActiveCanvas() {
		return activeCanvas;
	}
	/**
	 * Write something to the log. A timestamp will be added.
	 * @param s the message to log
	 */
	public static void log(String s) {
		if (!LOGGING) return;
		Date today = new Date();
		log.setText(log.getText() + "\n" + DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_MEDIUM).format(today) + " "  + s);
		log.setCursorPos(log.getText().length());
	}
	/**
	 * change the subtitle of the browser window
	 * @param s
	 */
	public static void setSubTitle(String s) {
		Window.setTitle(s + " - " + TITLE + " - " + VERSION);
	}
	private ArrayList<LogicalCanvas> canvi = new ArrayList<LogicalCanvas>();
	private ArrayList<LogicalDragPanel> panels = new ArrayList<LogicalDragPanel>();
	private static LogicalCanvas activeCanvas=null;
	private PlanSwitcher s;
	private RemoteManipulationServiceAsync rmsa;
	private PlanModelManipulator m;

	private NodeContextMenu nodeContextMenu = new NodeContextMenu();

	private ContextMenu planContextMenu = new ContextMenu();

	private TabContextMenu tabContextMenu = new TabContextMenu();
	
	private  GraphCanvasCommunicationCallback<Integer> createCb = new  GraphCanvasCommunicationCallback<Integer>("adding new empty canvas") {
		@Override
		public void onSuccess(Integer result) {
			if (addCanvas(result) != activeCanvas) changeCanvas(result);
		}
	};

	private  GraphCanvasCommunicationCallback<Integer> removeCb = new  GraphCanvasCommunicationCallback<Integer>("removing canvas") {
		@Override
		public void onSuccess(Integer result) {
			if (hasCanvasWithId(result)) {
				Iterator<LogicalDragPanel> it = panels.iterator();

				while (it.hasNext()) {
					LogicalDragPanel cur = it.next();
					if (cur.getLogicalCanvas().getId() == result) {
						removeLogicalCanvas(cur.getLogicalCanvas());
						cur.clear();
						RootPanel.get("editor").remove(cur);
						it.remove();
					}
				}
			}
		}
	};

	private GraphCanvasCommunicationCallback<String> xmlCb = new GraphCanvasCommunicationCallback<String>("getting XML") {
		@Override
		public void onSuccess(String result) {
			new TextPresentationDialog("XML source",result);
		}
	};

	private GraphCanvasCommunicationCallback<String> sqlCb = new GraphCanvasCommunicationCallback<String>("compiling SQL") {
		@Override
		public void onSuccess(String result) {
			new TextPresentationDialog("Compiled SQL",result);
		}
	};
	
	/**
	 * Add a new canvas to the editor with the given id. Note that
	 * this will <i>not</i> add a new plan to the bundle stored on the
	 * server! Use createNewPlan(id) for this.
	 * If a canvas with the given id already exists, it is returned
	 * @param id the id of the new canvas
	 * @return the new canvas or the canvas with the given id if it already exists
	 */
	public LogicalCanvas addCanvas(int id) {

		if (hasCanvasWithId(id)) return getCanvas(id);

		LogicalCanvas c = new LogicalCanvas(id,m,Window.getClientWidth()-30,Window.getClientHeight()-30,config,s.addPlan(id));

		c.setGraphNodeModifier(new GraphNodeModifier(c));
		c.setGraphEdgeModifier(new GraphEdgeModifier(c));

		c.setPopup(new LogicalNodePopup(c,rmsa));
		c.setContextMenu(nodeContextMenu);
		c.setCanvasMenu(planContextMenu);
		c.setPadding(60, 45);

		LogicalDragPanel d = new LogicalDragPanel(c);

		d.hide();
		RootPanel.get("editor").add(d);

		canvi.add(c);
		panels.add(d);
		if (canvi.size() == 1) changeCanvas(id);

		return c;
	}

	/**
	 * Switches to the canvas with the given id
	 * @param id the id of the canvas to switch to
	 */
	public void changeCanvas(int id) {
		AlgebraEditor.log("Changing to canvas #" + id);

		Iterator<LogicalDragPanel> it = panels.iterator();
		int activeCanvasId=-1;
		if (activeCanvas!= null) activeCanvasId = activeCanvas.getId();

		while (it.hasNext()) {
			LogicalDragPanel cur = it.next();
			if (cur.getLogicalCanvas().getId() == activeCanvasId) {
				cur.hide();
				cur.getLogicalCanvas().setNotActive(true);
			}
		}

		it = panels.iterator();
		while (it.hasNext()) {
			LogicalDragPanel cur = it.next();
			if (cur.getLogicalCanvas().getId() == id) {
				activeCanvas = cur.getLogicalCanvas();
				activeCanvas.setNotActive(false);
				s.setActive(id);
				cur.show();
			}
		}
	}

	/**
	 * Clear all canvases. <i>Note</i>, however, that this will
	 * not affect the query bundle stored on the server.
	 */
	public void clearCanvases() {

		AlgebraEditor.log("Clearing canvases...");
		Iterator<LogicalDragPanel> it = panels.iterator();
		canvi.clear();

		while (it.hasNext()) {
			LogicalDragPanel cur = it.next();
			removeLogicalCanvas(cur.getLogicalCanvas());
			cur.clear();
			RootPanel.get("editor").remove(cur);
			it.remove();
		}
	}

	/**
	 * Adds a new plan to the bundle
	 * @param clearFirst if true, all other plans will be removed first
	 */
	public void createNewPlan(boolean clearFirst) {
		rmsa.createNewPlan(clearFirst,createCb);
	}
	
	/**
	 * Returns the canvas object with the given id or null
	 * if no canvas with that id exists
	 * @param pid the id to look for
	 * @return the LogicalCanvas with the specified id
	 */
	public LogicalCanvas getCanvas(int pid) {
		Iterator<LogicalCanvas> it = canvi.iterator();
		while (it.hasNext()) {
			LogicalCanvas cur = it.next();
			if (cur.getId() == pid) return cur;
		}
		return null;
	}
	
	/**
	 * returns the context menu used on the canvas
	 * @return the contextMenu
	 */
	public ContextMenu getContextMenu() {
		return planContextMenu;
	}

	/**
	 * Returns an integer not yet used as a canvas id
	 * @return an integer that can be safely used as an id for a new canvas
	 */
	public int getFreeCanvasId() {
		int i=0;
		while (hasCanvasWithId(i)) i++;
		return i;
	}

	/**
	 * returns the manipulation service
	 * @return the manipulation service
	 */
	public RemoteManipulationServiceAsync getManServ() {
		return rmsa;
	}

	/**
	 * returns the context menu used on nodes
	 * @return the nodeContextMenu
	 */
	public NodeContextMenu getNodeContextMenu() {
		return nodeContextMenu;
	}

	/**
	 * returns the plan manipulator
	 * @return the plan manipulator
	 */
	public PlanModelManipulator getPlanManipulator() {
		return m;
	}

	/**
	 * returns the context menu used on the tabs
	 * @return the contextMenu
	 */
	public TabContextMenu getTabContextMenu() {
		return tabContextMenu;
	}

	/**
	 * Checks whether a canvas with the given id exists
	 * @param i the id to look for
	 * @return true if a canvas with the given id exists
	 */
	public boolean hasCanvasWithId(int i) {
		return getCanvas(i) != null;
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

	private void initPlanContextMenu() {
		AlgebraEditor.log("initializing plan context menu...");

		final ContextMenu m = getContextMenu();

		m.addItem(new LogicalPlanNodeContextItem("Add node") {
			@Override
			public void onClick() {
				getPlanManipulator().showNodeTypes();
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

	private void initTabContextMenu() {
		AlgebraEditor.log("initializing tab context menu...");

		final TabContextMenu m = getTabContextMenu();
		m.addItem(new LogicalTabContextItem("Evaluate") {
			@Override
			public void onClick(final int pid) {
				new EvaluatePlanDialog(pid,rmsa);
			}
		});

		m.addItem(new LogicalTabContextItem("Get SQL") {
			@Override
			public void onClick(int pid) {
				rmsa.getSQLFromPlan(pid, sqlCb);
			}
		});

		m.addItem(new LogicalTabContextItem("Download") {
			@Override
			public void onClick(int pid) {
				GraphCanvas.showLoading("Preparing file...");
				Window.open(GWT.getModuleBaseURL() + "fileserver?pid="+pid, "_self", "");
				GraphCanvas.hideLoading();
			}
		});

		m.addSeperator();
		m.addItem(new LogicalTabContextItem("Configure") {
			@Override
			public void onClick(final int pid) {
				new ConfigurePlanDialog(pid,rmsa);
			}
		});

		m.addSeperator();
		m.addItem(new LogicalTabContextItem("Close") {
			@Override
			public void onClick(int pid) {
				removePlan(pid);
			}
		});
	}

	/**
	 * load a plan stored on the server into the view
	 * @param id the id of the plan to load
	 */
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

	private void processConfiguration(final RemoteConfiguration result) {
		
		config=result;
		
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

		keepAliveTimer.scheduleRepeating(result.getKeepAliveInterval());

		if (result instanceof RemoteConfigurationWithPlansInSession) {

			AlgebraEditor.log("Found existing session on server...");

			YesNoPanel ynp = new YesNoPanel("A previous session has been found on the server. Do you want to load it?","Information");
			ynp.registerYesClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					AlgebraEditor.log("Loading existing plans from previous session...");
					for (Integer id:((RemoteConfigurationWithPlansInSession)result).getPlanIds()) {
						loadFinishedPlanFromServer(id);
					}
				}
			});
			ynp.registerNoClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					AlgebraEditor.log("loading empty plan...");
					createNewPlan(true);
				}
			});
			ynp.center();
			ynp.show();
		}else if (result.isLoadEmptyCanvas()){
			AlgebraEditor.log("loading empty plan...");
			createNewPlan(true);
		}
	}

	private void removeLogicalCanvas(LogicalCanvas c) {
		AlgebraEditor.log("Removing canvas #" + c.getId());
		canvi.remove(c);
		s.removePlan(c.getId());
	}

	/**
	 * Removes a plan from the bundle
	 * @param id the id of the plan to be added. See the servlet for
	 * a more detailed information on how this works.
	 */
	public void removePlan(int id) {
		rmsa.removePlan(id, removeCb);
	}
}

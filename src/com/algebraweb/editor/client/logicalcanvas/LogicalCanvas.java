package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.PlanSwitchButton;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.graphcanvas.ConnectedShape;
import com.algebraweb.editor.client.graphcanvas.ConnectedWidget;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.graphcanvas.NodeSelectionHandler;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class LogicalCanvas extends GraphCanvas{



	private int state = 0;
	private int id;
	final private PlanModelManipulator m;
	private String addingModeNodeType;

	private GraphNode drawEdgeTo;
	private GraphNode drawEdgeFrom;
	private int drawEdgeFromPos;
	private PlanSwitchButton myTabButton;

	private HashMap<Integer,SQLBubble> sqlBubbleList = new HashMap<Integer,SQLBubble>();

	private int errorCount=0;


	public LogicalCanvas(int id,final PlanModelManipulator m,int width, int height, PlanSwitchButton myTabButton) {
		super(width, height,true);

		this.id=id;
		this.m=m;
		this.myTabButton = myTabButton;

		super.addNodeSelectionHandler(new NodeSelectionHandler() {

			@Override
			public void isSelected(GraphNode node) {


				switch (state) {

				case 2:

					break;
				case 3:

					drawEdgeTo = node;
					clearDrag();
					m.addEdge(new Coordinate(drawEdgeFrom.getId(),drawEdgeTo.getId()), LogicalCanvas.this.getId(), drawEdgeFromPos);

					state=0;

					Iterator<GraphNode> it = LogicalCanvas.this.getNodes().iterator();

					while (it.hasNext()) {

						GraphNode n = it.next();
						clearEdgeConnectors(n);
					}

					break;
				}
			}
		});




		super.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {


			}

		},MouseMoveEvent.getType());


		super.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {


				if (state == 1) {

					state = 0;
					LogicalCanvas.this.removeStyleName("node-adding");

					int x=-marginLeft+(int)(getScale() * (event.getRelativeX(LogicalCanvas.super.getElement())));
					int y=-marginTop+(int)(getScale() * (event.getRelativeY(LogicalCanvas.super.getElement())));
					//TODO: pid

					LogicalCanvas.this.m.addNode(LogicalCanvas.this.getId(), addingModeNodeType, x,y);
				}
			}

		}, MouseUpEvent.getType());

	}


	public int getId() {
		return id;
	}


	public void setErroneous(int nid) {

		if (!super.getGraphNodeById(nid).getConnectedShapes().containsKey("__logicalplan_error")) {

			errorCount++;
			myTabButton.setErrorCount(errorCount);
			Text errorMark = super.textFactory(0, 0,"!");
			errorMark.getElement().setAttribute("class", "logicalplan-node-errormark-text");
			errorMark.attr("fill","red");

			super.hangShapeOntoNode("__logicalplan_error", new ConnectedShape(errorMark, -6, -8), nid);				

		}
	}

	public void clearErroneous() {

		errorCount=0;
		myTabButton.setErrorCount(errorCount);
		Iterator<GraphNode> it = super.getNodes().iterator();

		while (it.hasNext()) {

			GraphNode current = it.next();

			if (current.getConnectedShapes().containsKey("__logicalplan_error")) {

				super.unHangShapeFromNode("__logicalplan_error", current.getId());

			}


		}
	}

	private void clearEdgeConnectors(GraphNode n) {


		for (int i=0;i<n.getFixedChildCount();i++) {
			unHangShapeFromNode("edge_circle_pos" + (i+1), n.getId());
		}

	}




	public void enterNodeAddingMode(String addingModeNodeType) {

		state=1;
		this.addingModeNodeType= addingModeNodeType;
		this.addStyleName("node-adding");

	}


	public void enterEdgeAddingMode() {

		GWT.log("entering...");
		state=2;

		Iterator<GraphNode> it = super.getNodes().iterator();


		while (it.hasNext()) {

			GraphNode n = it.next();

			HashMap<Integer,Boolean> freeChilds = getFreeChildEdgePositions(n);

			Iterator<Integer> itt = freeChilds.keySet().iterator();

			while (itt.hasNext()) {

				int current = itt.next();

				if (freeChilds.get(current).booleanValue()) {


					Circle cr = super.circleFactory(0,0, 6);
					cr.getElement().setAttribute("class", "edge-connector");
					cr.addDomHandler(createEdgeShapeMouseHandler(n,current),  MouseDownEvent.getType());

					int y= n.getHeight()+3;
					int x= (n.getWidth() / (freeChilds.size() +1)) * current;


					ConnectedShape csr = new ConnectedShape(cr, x+3, y);

					super.hangShapeOntoNode("edge_circle_pos" + current, csr, n.getId());


				}


			}





		}



	}


	private MouseDownHandler createEdgeShapeMouseHandler(final GraphNode n, final int pos) {

		return new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {

				state=3;
				drawEdgeFrom = n;
				drawEdgeFromPos = pos;
			}

		};


	}



	private HashMap<Integer,Boolean> getFreeChildEdgePositions(GraphNode n) {

		HashMap<Integer,Boolean> ret = new  HashMap<Integer,Boolean>();

		for (int i=0;i<n.getFixedChildCount();i++) {


			GraphEdge cur = getEdgeWithParentPos(n.getEdgesFrom(),i+1);

			if (cur == null) {

				ret.put(i+1, true);

			}else{

				ret.put(i+1, false);

			}
		}

		return ret;


	}


	private GraphEdge getEdgeWithParentPos(ArrayList<GraphEdge> es,int p) {


		Iterator<GraphEdge> it = es.iterator();

		while (it.hasNext()) {

			GraphEdge cur = it.next();

			if (cur.getFixedParentPos() == p) return cur;


		}

		return null;


	}

	public void addSQLListener(int nid, RemoteManipulationServiceAsync rmsa, EvaluationContext c) {

		SQLBubble listener = new SQLBubble(nid,this.getId(),rmsa,c,this);
		GraphNode n = getGraphNodeById(nid);
		super.hangWidgetOntoNode("sql-listener", new ConnectedWidget(listener,n.getWidth(),0), nid);
		sqlBubbleList.put(nid, listener);
		listener.update();

	}

	public void removeSQLListener(SQLBubble b) {

		super.unHangWidgetFromNode("sql-listener", b.getNid());
		sqlBubbleList.remove(b);


	}

	public void updateSQLListener() {

		Iterator<SQLBubble> it = sqlBubbleList.values().iterator();

		while (it.hasNext()) it.next().update();


	}





}

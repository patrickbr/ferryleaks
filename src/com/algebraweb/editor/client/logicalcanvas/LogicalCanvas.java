package com.algebraweb.editor.client.logicalcanvas;

import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.graphcanvas.ConnectedShape;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.graphcanvas.NodeSelectionHandler;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

public class LogicalCanvas extends GraphCanvas{



	private int state = 0;
	private int id;
	private PlanModelManipulator m;
	private String addingModeNodeType;

	private GraphNode drawEdgeTo;
	private GraphNode drawEdgeFrom;


	public LogicalCanvas(int id,PlanModelManipulator m,int width, int height) {
		super(width, height,true);

		this.id=id;
		this.m=m;


		super.addNodeSelectionHandler(new NodeSelectionHandler() {

			@Override
			public void isSelected(HashMap<Integer, GraphNode> nodes) {

			
				switch (state) {


				case 2:
					
				
					state=3;

					drawEdgeFrom = nodes.values().toArray(new GraphNode[0])[0];
					break;

				case 3:
					
				

					drawEdgeTo = nodes.values().toArray(new GraphNode[0])[0];
					
					
					LogicalCanvas.this.createEdge(drawEdgeFrom, drawEdgeTo, false);

						
					state=0;
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

			Text errorMark = super.textFactory(0, 0,"!");
			errorMark.getElement().setAttribute("class", "logicalplan-node-errormark-text");
			errorMark.attr("fill","red");

			super.hangShapeOntoNode("__logicalplan_error", new ConnectedShape(errorMark, -6, -8), nid);				

		}


	}

	public void clearErroneous() {


		Iterator<GraphNode> it = super.getNodes().iterator();

		while (it.hasNext()) {

			GraphNode current = it.next();

			if (current.getConnectedShapes().containsKey("__logicalplan_error")) {

				super.unHangShapeFromNode("__logicalplan_error", current.getId());

			}


		}




	}




	public void enterNodeAddingMode(String addingModeNodeType) {


		state=1;
		this.addingModeNodeType= addingModeNodeType;




	}


	public void enterEdgeAddingMode() {

		GWT.log("entering...");
		state=2;
		


	}





}

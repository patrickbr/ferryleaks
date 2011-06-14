package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.graphcanvas.ConnectedShape;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphEdgeModifier;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphNodeModifier;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;

public class LogicalCanvas extends GraphCanvas{




	public LogicalCanvas(int width, int height) {
		super(width, height);
	}


	public void setErroneous(int nid) {


		if (!super.getGraphNodeById(nid).getConnectedShapes().containsKey("__logicalplan_error")) {
			
			Text errorMark = super.textFactory(0, 0,"!");
			errorMark.getElement().setAttribute("class", "logicalplan-node-errormark-text");
			errorMark.attr("fill","red");
			
			super.hangShapeOntoNode("__logicalplan_error", new ConnectedShape(errorMark, -6, -8), nid);				

		}


	}
	
	
	//TODO: DUMMY
	public int getActivePlanId() {
		
		return 0;
		
		
	}




}

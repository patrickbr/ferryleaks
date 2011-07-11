package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

/**
 * Handles PlanManipulation and synchronization with the server modell
 * @author patrick
 *
 */

public class PlanModelManipulator {


	private RemoteManipulationServiceAsync manServ;
	private AlgebraEditor e;


	public PlanModelManipulator(RemoteManipulationServiceAsync manServ) {


		this.manServ = manServ;

	}

	public void setEditor(AlgebraEditor e) {
		this.e=e;
	}


	public void deleteNode(Integer[] nids, int planid) {

		GraphCanvas.showLoading("Deleting node...");
		manServ.deleteNodes(nids, planid, manipulationCallback);



	}

	public void deleteEdges(Coordinate[] edges, int planid) {

		GraphCanvas.showLoading("Deleting edge...");
		manServ.deleteEdge(edges, planid, manipulationCallback);



	}

	public void addNode(int pid, String type, int x, int y) {

		GraphCanvas.showLoading("Adding node...");
		manServ.addNode(pid, type,x,y, manipulationCallback);



	}

	public void addEdge(Coordinate e, int planid, int pos) {

		GraphCanvas.showLoading("Adding edge...");
		manServ.addEdge(planid, e, pos, manipulationCallback);

	}


	public void validate(int planid) {

		manServ.getValidation(planid,validationCallback);

	}


	private void showValidation(ValidationResult r) {

		e.getCanvas(r.getPlanid()).clearErroneous();


		Iterator<ValidationError> it = r.getErrors().iterator();

		while(it.hasNext()) {
			e.getCanvas(r.getPlanid()).setErroneous(it.next().getNodeId());
		}

	}




	public void updateNodeContent(int planid, PlanNode p) {


		manServ.updatePlanNode(p.getId(), planid, p, manipulationCallback);


	}

	public void updateNodeContent(int planid, int nid, String XML) {


		manServ.updatePlanNode(nid, planid, XML, manipulationCallback);


	}

	public void blurr(boolean blur) {

		e.getActiveCanvas().setBlurred(blur);

	}


	private GraphCanvasCommunicationCallback<ValidationResult> validationCallback = new GraphCanvasCommunicationCallback<ValidationResult>() {


		@Override
		public void onSuccess(ValidationResult result) {


			showValidation(result);


		}

	};

	private GraphCanvasCommunicationCallback<RemoteManipulationMessage> manipulationCallback = new GraphCanvasCommunicationCallback<RemoteManipulationMessage>() {


		@Override
		public void onFailure(Throwable caught) {

			super.onFailure(caught);
			GraphCanvas.hideLoading();
		}

		@Override
		public void onSuccess(RemoteManipulationMessage result) {


			GraphCanvas.hideLoading();


			if (result.getReturnCode() == 1 ) {

				//success

				if (result.getAction().equals("delete")) {

					Iterator<RawNode> it = result.getNodesAffected().iterator();

					while(it.hasNext()) {

						e.getCanvas(result.getPlanid()).deleteNode(e.getCanvas(result.getPlanid()).getGraphNodeById(it.next().getNid()));

					}

				}


				if (result.getAction().equals("update")) {

					Iterator<RawNode> it = result.getNodesAffected().iterator();

					while(it.hasNext()) {

						RawNode current = it.next();
						e.getCanvas(result.getPlanid()).clearEdgesFrom(e.getCanvas(result.getPlanid()).getGraphNodeById(current.getNid()));

						Iterator<RawEdge> i = current.getEdgesToList().iterator();
						GraphNode from = e.getCanvas(result.getPlanid()).getGraphNodeById(current.getNid());

						while(i.hasNext()) {

							RawEdge ed = i.next();

							GraphNode to = e.getCanvas(result.getPlanid()).getGraphNodeById(ed.getTo());
							
							//only draw if not already there
							if (!e.getCanvas(result.getPlanid()).hasEdge(from.getId(), to.getId())) {
								e.getCanvas(result.getPlanid()).createEdge(from, to, ed.getFixedParentPos(),true);
							}
						}

						e.getCanvas(result.getPlanid()).showEdges();

					}
				}


				if (result.getAction().equals("add")) {

					Iterator<RawNode> it = result.getNodesAffected().iterator();

					while(it.hasNext()) {

						RawNode current = it.next();

						e.getCanvas(result.getPlanid()).addNode(current.getNid(), current.getColor(), 
								(int)result.getCoordinates().get(current.getNid()).getX(), 
								(int)result.getCoordinates().get(current.getNid()).getY(),
								(int)current.getWidth(),
								(int)current.getHeight(),
								current.getText(),								
								current.getFixedChildCount());

						GWT.log("a:"+ current.getFixedChildCount());
					}
				}

				showValidation(result.getValidationResult());
			}




		}



	};



}

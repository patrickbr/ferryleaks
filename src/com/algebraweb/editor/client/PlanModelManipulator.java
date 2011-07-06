package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
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

	public void addNode(int pid, String type, int x, int y) {

		GraphCanvas.showLoading("Adding node...");
		manServ.addNode(pid, type,x,y, manipulationCallback);



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


	private AsyncCallback<ValidationResult> validationCallback = new AsyncCallback<ValidationResult>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ValidationResult result) {


			showValidation(result);


		}

	};

	private AsyncCallback<RemoteManipulationMessage> manipulationCallback = new AsyncCallback<RemoteManipulationMessage>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

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

							GraphNode to = e.getCanvas(result.getPlanid()).getGraphNodeById(i.next().getTo());
							e.getCanvas(result.getPlanid()).createEdge(from, to, true);


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
								current.getText());

					}




				}




				showValidation(result.getValidationResult());






			}


			if (result.getReturnCode() == 3) {


				final DialogBox d = new DialogBox();

				d.setText("Error");
				Button ok = new Button("OK");
				ok.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						d.hide();
					}
				});
				;
				VerticalPanel p = new VerticalPanel();

				p.add(new HTML("<span style='font-weight:bold;color:red'>Could not save node.</span> Reason was:<br><br>" + SafeHtmlUtils.htmlEscape(result.getMessage())));
				p.add(ok);
				ok.getElement().getStyle().setMargin(20, Unit.PX);
				p.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);

				d.add(p);



				d.center();
				d.show();

			}

		}



	};



}

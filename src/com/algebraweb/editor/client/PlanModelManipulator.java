package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles PlanManipulation and synchronization with the server modell
 * @author patrick
 *
 */

public class PlanModelManipulator {


	private RemoteManipulationServiceAsync manServ;
	private LogicalCanvas c;


	public PlanModelManipulator(LogicalCanvas c, RemoteManipulationServiceAsync manServ) {

		this.c=c;
		this.manServ = manServ;

	}


	public void deleteNode(int nid, int planid) {

		GraphCanvas.showLoading("Deleting node...");
		manServ.deleteNode(nid, planid, manipulationCallback);



	}


	public void validate(int planid) {

		manServ.getValidation(planid,validationCallback);

	}


	private void showValidation(ValidationResult r) {

		Iterator<ValidationError> it = r.getErrors().iterator();

		while(it.hasNext()) {
			c.setErroneous(it.next().getNodeId());
		}


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

					Iterator<Integer> it = result.getNodesAffected().iterator();
					
					while(it.hasNext()) {
						
						c.deleteNode(c.getGraphNodeById(it.next()));
											
					}

				}
				
				if (result.getValidationResult().hasErrors()) {
					
					Iterator<ValidationError> it = result.getValidationResult().getErrors().iterator();
					
					while (it.hasNext()) {
						
						c.setErroneous(it.next().getNodeId());
						
					}
					
					
				}




			}

	
			if (result.getReturnCode() == 3) {

				//fail




			}

		}



	};



}

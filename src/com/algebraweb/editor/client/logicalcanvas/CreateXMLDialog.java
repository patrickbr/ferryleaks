package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.TextPresentationDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

public class CreateXMLDialog extends CreateEvaluationContextDialog{

	
	private CheckBox cb;
	

	private AsyncCallback<String> xmlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String result) {

			new TextPresentationDialog("XML source",result);

		}

	};
	
	public CreateXMLDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {


		super(pid,nid,manServ);
		super.setText("XML generation");
		GWT.log("id:" + nid);
		
		cb = new CheckBox();
		cb.setText("Save for current node");

		addButton(cb);

	}
	
		
	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		getManServ().getXMLLogicalPlanFromRootNode(super.getPid(), super.getNid(), c, cb.getValue(), xmlCb);
	}


}

package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.TextPresentationDialog;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;

public class CreateXMLDialog extends CreateEvaluationContextDialog{

	
	private int pid;
	private int nid;
	private CheckBox cb;
	

	public CreateXMLDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {


		super(pid,nid,manServ);
		super.setText("XML generation");
		
		cb = new CheckBox();
		cb.setText("Save for current node");

		addButton(cb);

	}
	
	@Override
	protected void submit() {
		
		EvaluationContext c = saveContext();
		getManServ().getXMLLogicalPlanFromRootNode(pid, nid, c, cb.getValue(), xmlCb);
			
	}
	
		
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


}

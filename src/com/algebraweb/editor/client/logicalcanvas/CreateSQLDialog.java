package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.TextPresentationDialog;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

public class CreateSQLDialog extends CreateEvaluationContextDialog {

	private CheckBox cb;

	private AsyncCallback<String> sqlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String result) {
			new TextPresentationDialog("Compiled SQL",result);
		}
	};
	
	public CreateSQLDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {

		super(pid,nid,manServ);
		super.setText("SQL generation");
	
		cb = new CheckBox();
		cb.setText("Save for current node");

		addButton(cb);
	
	}
	
	protected CheckBox getSaveCheckBox() {
		return cb;
	}
	
	protected boolean getSaveCurrenNodeValue() {
		
		return cb.getValue();
		
	}
	
	@Override
	protected EvaluationContext saveContext() {
		
		EvaluationContext c = super.saveContext();
		return c;
		
	}
	
	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		getManServ().getSQLFromPlanNode(getPid(), getNid(), c,cb.getValue(), sqlCb);
	}


}

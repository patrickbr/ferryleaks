package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateSQLDialog extends CreateXMLDialog {



	public CreateSQLDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {

		super(pid,nid,manServ);
		super.setText("SQL generation");
		
	
	}
	
	@Override
	protected void submit() {
		
		
		EvaluationContext c = new EvaluationContext();
		
		getSerializationPanel().fillEvaluationContext(c);
		
		getManServ().getSQLFromPlanNode(getPid(), getNid(), c, sqlCb);
		
		
	}
	
	private AsyncCallback<String> sqlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String result) {


			Window.alert(result);


		}

	};


}

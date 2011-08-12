package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EvaluatePlanDialog extends EvaluationDialog {

	private  AsyncCallback<List<Map<String, String>>> evalCb = new  GraphCanvasCommunicationCallback<List<Map<String,String>>>("evaluating") {

		@Override
		public void onSuccess(List<Map<String,String>> result) {


			new SqlResDialog(result);


		}

	};
	
	public EvaluatePlanDialog(int pid, RemoteManipulationServiceAsync manServ) {
		
		super(pid, -1, manServ);
		super.setText("Evaluate Plan");
		

	}
	
	@Override
	protected void submit() {


		EvaluationContext c = saveContext();
		getManServ().evalPlan(getPid(), c,getSaveCurrenNodeValue(), evalCb);

	}
	
	

}
